package com.wintertonsmith.snoolexa.intent;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.User;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Log4j
@NoArgsConstructor
public class HotPostsIntent implements Intent {

    @Override
    public SpeechletResponse doIntent(@NonNull final Session session) {
        String speechText = "The the hottest five posts on Reddit are ";

        User sessionUser = session.getUser();

        HttpGet getReq = new HttpGet("https://oauth.reddit.com/hot.json");
        getReq.setHeader("Authorization","Bearer " + sessionUser.getAccessToken());

        CloseableHttpClient client = HttpClients.custom().setUserAgent("com.wintertonsmith.snoolexa 0.1.0-SNAPSHOT").build();

        //TODO: Either build or use an API wrapper for Reddit
        try {
            HttpResponse r = client.execute(getReq);

            String entity = EntityUtils.toString(r.getEntity());

            JsonParser parser =  new JsonParser();
            JsonObject rootObj = parser.parse(entity).getAsJsonObject();
            JsonArray posts = rootObj.get("data").getAsJsonObject().get("children").getAsJsonArray();

            //TODO: Allow for post by post reading and user reprompts for manipulation/paging
            for(int i = 0; i < ((posts.size() > 5) ? 5 : posts.size()); ++i) {

                //TODO: Build a POJO representation of the JSON data
                JsonObject postData = posts.get(i).getAsJsonObject().get("data").getAsJsonObject();

                JsonElement media = postData.get("media");
                String type = "self";
                if (media != null && !media.isJsonNull()) {
                    JsonElement oembed = media.getAsJsonObject().get("oembed");
                    if (oembed != null && !oembed.isJsonNull()) {
                        type = oembed.getAsJsonObject().get("type").getAsString();
                        if ("image".equals(type)) {
                            type = "an " + type;
                        } else {
                            type = "a " + type;
                        }
                    }
                }

                String title = postData.get("title").getAsString();
                String subreddit = postData.get("subreddit").getAsString();
                speechText += type + " post with the title, " + title + " on r/" + subreddit + ".";

                if (i != posts.size() || i != 5) {
                    speechText += " A ";
                }

            }

            speechText += " Those are the top 5 posts on Reddit. Goodbye!";

        } catch (IOException e) {
            log.error("Hot API req failed with IOException!", e);
        } finally {
            try {
                client.close();
            } catch (IOException ioe) {
                log.error("Failed to close the HTTP client stream.", ioe);
            }
        }

        SimpleCard card = new SimpleCard();
        card.setTitle("Snoolexa");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);



        return SpeechletResponse.newTellResponse(speech, card);
    }
}

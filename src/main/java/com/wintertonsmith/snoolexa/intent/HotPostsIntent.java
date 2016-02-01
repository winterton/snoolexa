package com.wintertonsmith.snoolexa.intent;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.User;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.TimePeriod;

@Log4j
@NoArgsConstructor
public class HotPostsIntent implements Intent {

    @Override
    public SpeechletResponse doIntent(@NonNull final Session session) {

        String speechText = "The hottest posts on reddit right now. ";
        UserAgent agent = UserAgent.of("echo","com.wintertonsmith.snoolexa","0.1.0","jake_w_smith");

        //TODO: handle unchecked exceptions

        //Since we already have an access token from Alexa, bypass client auth and
        //add auth header to http adapter
        User sessionUser = session.getUser();
        String authStr = "Bearer " + sessionUser.getAccessToken();
        RedditClient redditClient = new RedditClient(agent);
        redditClient.getHttpAdapter().getDefaultHeaders().put("Authorization", authStr);

        SubredditPaginator paginator = new SubredditPaginator(redditClient);
        paginator.setLimit(5);
        paginator.setSorting(Sorting.HOT);
        paginator.setTimePeriod(TimePeriod.DAY);

        paginator.next(true);

        Listing<Submission> posts = paginator.getCurrentListing();


        for (Submission submission : posts) {
            speechText += " " + submission.getTitle();
        }

        //TODO: Allow for post by post reading and user reprompts for manipulation/paging


        SimpleCard card = new SimpleCard();
        card.setTitle("Snoolexa");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);



        return SpeechletResponse.newTellResponse(speech, card);
    }
}

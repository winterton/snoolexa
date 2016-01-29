package com.wintertonsmith.snoolexa;

import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.wintertonsmith.snoolexa.intent.IntentType;

public class SnoolexaSpeechlet implements Speechlet {
    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {

    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        String speechText = "Hello world, this is Snoolexa! Try asking for hot posts!";

        //TODO: Query the me endpoint and tell asker about currently signed in redditor and any notifications

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Snoolexa");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        return IntentType.lookup(request.getIntent().getName()).doIntent(session);
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }
}

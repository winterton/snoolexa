package com.wintertonsmith.snoolexa;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class SnoolexaSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = ImmutableSet.of("amzn1.echo-sdk-ams.app.058a1ca3-436d-4f8f-8cf9-069dd2e6399f");

    public SnoolexaSpeechletRequestStreamHandler() {
        super(new SnoolexaSpeechlet(), supportedApplicationIds);
    }

    public static void main(String[] args) {
        System.out.print(supportedApplicationIds);
    }
}

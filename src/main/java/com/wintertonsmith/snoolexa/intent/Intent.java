package com.wintertonsmith.snoolexa.intent;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

public interface Intent {
    SpeechletResponse doIntent(Session session);
}

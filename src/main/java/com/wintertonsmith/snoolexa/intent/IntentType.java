package com.wintertonsmith.snoolexa.intent;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;

@Getter
@RequiredArgsConstructor
public enum IntentType {
    HOT_POSTS("HotPostsIntent", new HotPostsIntent());

    @NonNull private final String name;
    @NonNull private final Intent intent;

    public static Intent lookup(@NonNull final String name) {
        return stream(values())
                .filter(i -> i.getName().equals(name))
                .map(IntentType::getIntent).findFirst().orElse(null);
    }
}
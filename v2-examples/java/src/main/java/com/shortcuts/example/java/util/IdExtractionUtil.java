package com.shortcuts.example.java.util;

import lombok.NonNull;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class IdExtractionUtil {

    public String extractId(
            @NonNull String name,
            @NonNull String href) {
        Deque<String> segments = new ArrayDeque<>(Arrays.asList(href.split("/")));
        String result = null;
        while (!segments.isEmpty()) {
            String segment = segments.removeFirst();
            if (segment.equals(name)) {
                if (!segments.isEmpty()) {
                    result = segments.removeFirst();
                    break;
                }
            }
        }
        return result;
    }

}

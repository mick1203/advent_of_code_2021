package com.michaelburgstaller.adventofcode.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class Exercise {

    protected static BufferedReader getFileReader(String path) {
        var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        var inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    protected static Stream<String> getLineStream() {
        return getFileReader("input.txt").lines();
    }

}

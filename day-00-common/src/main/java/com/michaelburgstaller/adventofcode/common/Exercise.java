package com.michaelburgstaller.adventofcode.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Exercise {

    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLACK = "\u001B[30m";
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_GREEN = "\u001B[32m";
    protected static final String ANSI_YELLOW = "\u001B[33m";
    protected static final String ANSI_BLUE = "\u001B[34m";
    protected static final String ANSI_PURPLE = "\u001B[35m";
    protected static final String ANSI_CYAN = "\u001B[36m";
    protected static final String ANSI_WHITE = "\u001B[37m";

    protected static final Timer timer = new Timer();

    protected static void waitHere() {
        try {
            System.in.read();
        } catch (Exception e) {
            // don't care
        }
    }

    protected static void time(Runnable callToTime) {
        timer.start();
        callToTime.run();
        timer.end();
        System.out.println(timer);
    }

    protected static BufferedReader getFileReader(String path) {
        var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        var inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    protected static Stream<String> getLineStream() {
        return getLineStream("input.txt");
    }

    protected static Stream<String> getLineStream(String path) {
        return getFileReader(path).lines();
    }

    protected static Stream<List<String>> getBufferedLineStream() {
        return getBufferedLineStream(getLineStream(), "");
    }

    protected static void printOnSameLine(String value) {
        System.out.print("\r" + value);
    }

    protected static void newLine() {
        System.out.println();
    }

    protected static Stream<List<String>> getBufferedLineStream(Stream<String> lineStream, String separator) {
        var data = lineStream.toList();
        var batches = new ArrayList<List<String>>();
        var batch = new ArrayList<String>();

        for (var line : data) {
            if (line.contentEquals(separator)) {
                batches.add(List.copyOf(batch));
                batch.clear();
                continue;
            }
            batch.add(line.strip());
        }
        batches.add(batch);

        return batches.stream();
    }

}

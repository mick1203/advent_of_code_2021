package com.michaelburgstaller.adventofcode.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Exercise {

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

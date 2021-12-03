package com.michaelburgstaller.adventofcode.binarydiagnostic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinaryDiagnostic {

    private static BufferedReader getFileReader(String path) {
        var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        var inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    public static void main(String[] args) {
        var fileReader = getFileReader("input.txt");
        var numbers = fileReader.lines().toList();

        var count = new ArrayList<Integer>();
        IntStream.range(0, 12).forEach(i -> count.add(0));

        numbers.forEach(number -> {
            var bits = Arrays.stream(number.split("")).map(Integer::parseInt).toList();
            for (var i = 0; i < count.size(); i++) {
                count.set(i, count.get(i) + bits.get(i));
            }
        });

        var mostCommon = count.stream().map(c -> c > (numbers.size() / 2) ? 1 : 0).map(String::valueOf).collect(Collectors.joining(""));
        var leastCommon = count.stream().map(c -> c < (numbers.size() / 2) ? 1 : 0).map(String::valueOf).collect(Collectors.joining(""));

        var gammaRate = Integer.parseInt(mostCommon, 2);
        var epsilonRate = Integer.parseInt(leastCommon, 2);
        var powerConsumption = gammaRate * epsilonRate;

        System.out.println("The gamma rate is '" + gammaRate + "' and the epsilon rate '" + epsilonRate + "' resulting in a power consumption of '" + powerConsumption + "'");
    }

}

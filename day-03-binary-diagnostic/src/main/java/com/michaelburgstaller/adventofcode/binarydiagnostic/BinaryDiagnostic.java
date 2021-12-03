package com.michaelburgstaller.adventofcode.binarydiagnostic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinaryDiagnostic {

    private static BufferedReader getFileReader(String path) {
        var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        var inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    private static void powerConsumption(List<String> numbers) {
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

    private static List<String> extractPossibleNumbers(List<String> numbers, Integer index, RatingType ratingType) {
        var ones = numbers.stream().map(number -> Integer.parseInt(number.substring(index, index + 1))).reduce(0, Integer::sum);
        var zeros = numbers.size() - ones;

        var mostCommon = Math.max(ones, zeros) == ones ? 1 : 0;
        var leastCommon = Math.min(ones, zeros) == ones ? 1 : 0;

        return numbers.stream().filter(number -> {
            var bit = Integer.parseInt(number.substring(index, index + 1));
            return switch (ratingType) {
                case OxygenGenerator -> mostCommon == leastCommon ? bit == 1 : bit == mostCommon;
                case CO2Scrubber -> mostCommon == leastCommon ? bit == 0 : bit == leastCommon;
            };
        }).toList();
    }

    private static void lifeSupportRating(List<String> numbers) {
        List<String> oxygenGeneratorRatingCandidates = new ArrayList<>(numbers);
        for (var i = 0; i < 12 && oxygenGeneratorRatingCandidates.size() > 1; i++) {
            oxygenGeneratorRatingCandidates = extractPossibleNumbers(oxygenGeneratorRatingCandidates, i, RatingType.OxygenGenerator);
        }

        List<String> co2ScrubberRatingCandidates = new ArrayList<>(numbers);
        for (var i = 0; i < 12 && co2ScrubberRatingCandidates.size() > 1; i++) {
            co2ScrubberRatingCandidates = extractPossibleNumbers(co2ScrubberRatingCandidates, i, RatingType.CO2Scrubber);
        }

        var oxygenGeneratorRating = Integer.parseInt(oxygenGeneratorRatingCandidates.get(0), 2);
        var co2ScrubberRating = Integer.parseInt(co2ScrubberRatingCandidates.get(0), 2);
        var lifeSupportRating = oxygenGeneratorRating * co2ScrubberRating;

        System.out.println("The oxygen generator rating is '" + oxygenGeneratorRating + "' and the CO2 scrubber rating is '" + co2ScrubberRating + "' resulting in a life support rating of '" + lifeSupportRating + "'");
    }

    public static void main(String[] args) {
        var fileReader = getFileReader("input.txt");
        var numbers = fileReader.lines().toList();

        powerConsumption(numbers);
        lifeSupportRating(numbers);
    }

}

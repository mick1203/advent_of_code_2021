package com.michaelburgstaller.adventofcode.binarydiagnostic;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BinaryDiagnostic extends Exercise {

    private static final Integer NUMBER_OF_BITS = 12;

    private static class BitOccurrence {
        public Integer mostCommon;
        public Integer leastCommon;

        public BitOccurrence(Integer mostCommon, Integer leastCommon) {
            this.mostCommon = mostCommon;
            this.leastCommon = leastCommon;
        }
    }

    private static BitOccurrence determineBitOccurrence(List<String> binaryNumbers, Integer position) {
        var ones = binaryNumbers.stream().map(number -> number.substring(position, position + 1)).map(Integer::parseInt).reduce(0, Integer::sum);
        var zeros = binaryNumbers.size() - ones;

        var mostCommon = Math.max(ones, zeros) == ones ? 1 : 0;
        var leastCommon = Math.min(ones, zeros) == ones ? 1 : 0;

        return new BitOccurrence(mostCommon, leastCommon);
    }

    private static void powerConsumption(List<String> numbers) {
        var count = new ArrayList<Integer>();
        IntStream.range(0, NUMBER_OF_BITS).forEach(i -> count.add(0));

        var binaryGammaRate = "";
        var binaryEpsilonRate = "";
        for (var i = 0; i < NUMBER_OF_BITS; i++) {
            var bitOccurrence = determineBitOccurrence(numbers, i);
            binaryGammaRate += bitOccurrence.mostCommon;
            binaryEpsilonRate += bitOccurrence.leastCommon;
        }

        var gammaRate = Integer.parseInt(binaryGammaRate, 2);
        var epsilonRate = Integer.parseInt(binaryEpsilonRate, 2);
        var powerConsumption = gammaRate * epsilonRate;

        System.out.println("The gamma rate is '" + gammaRate + "' and the epsilon rate '" + epsilonRate + "' resulting in a power consumption of '" + powerConsumption + "'");
    }

    private static void lifeSupportRating(List<String> numbers) {
        List<String> oxygenGeneratorRatingCandidates = new ArrayList<>(numbers);
        for (var i = 0; i < NUMBER_OF_BITS && oxygenGeneratorRatingCandidates.size() > 1; i++) {
            final var position = i;
            var bitOccurrence = determineBitOccurrence(oxygenGeneratorRatingCandidates, position);
            var bitMask = bitOccurrence.mostCommon == bitOccurrence.leastCommon ? 1 : bitOccurrence.mostCommon;


            oxygenGeneratorRatingCandidates =
                    oxygenGeneratorRatingCandidates.stream()
                            .filter(binaryNumber -> Integer.parseInt(binaryNumber.substring(position, position + 1)) == bitMask).toList();
        }


        List<String> co2ScrubberRatingCandiates = new ArrayList<>(numbers);
        for (var i = 0; i < NUMBER_OF_BITS && co2ScrubberRatingCandiates.size() > 1; i++) {
            final var position = i;
            var bitOccurrence = determineBitOccurrence(co2ScrubberRatingCandiates, position);
            var bitMask = bitOccurrence.mostCommon == bitOccurrence.leastCommon ? 0 : bitOccurrence.leastCommon;
            co2ScrubberRatingCandiates =
                    co2ScrubberRatingCandiates.stream()
                            .filter(binaryNumber -> Integer.parseInt(binaryNumber.substring(position, position + 1)) == bitMask).toList();
        }

        var oxygenGeneratorRating = Integer.parseInt(oxygenGeneratorRatingCandidates.get(0), 2);
        var co2ScrubberRating = Integer.parseInt(co2ScrubberRatingCandiates.get(0), 2);
        var lifeSupportRating = oxygenGeneratorRating * co2ScrubberRating;

        System.out.println("The oxygen generator rating is '" + oxygenGeneratorRating + "' and the CO2 scrubber rating is '" + co2ScrubberRating + "' resulting in a life support rating of '" + lifeSupportRating + "'");
    }

    public static void main(String[] args) {
        var numbers = getLineStream().toList();

        powerConsumption(numbers);
        lifeSupportRating(numbers);
    }

}

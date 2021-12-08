package com.michaelburgstaller.adventofcode.sevensegmentsearch;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SevenSegmentSearch extends Exercise {

    private static class Pattern {
        public String pattern;

        public Pattern(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return pattern;
        }

        public static Pattern parse(String rawValue) {
            var tokens = rawValue.split("");
            var pattern = Arrays.stream(tokens).sorted().collect(Collectors.joining());
            return new Pattern(pattern);
        }
    }

    private static class Entry {
        public List<Pattern> notes;
        public List<Pattern> outputs;

        public Map<Integer, String> patternMap;
        public Map<String, Integer> valueMap;

        public Entry(List<Pattern> notes, List<Pattern> outputs) {
            this.notes = notes;
            this.outputs = outputs;

            this.patternMap = new HashMap<>();
            this.valueMap = new HashMap<>();
        }

        private Integer getRemainingSegmentCount(String pattern, Integer number) {
            var remainingPatternSegments = pattern;
            var numberSegments = patternMap.get(number).split("");

            for (var numberSegment : numberSegments) {
                remainingPatternSegments = remainingPatternSegments.replace(numberSegment, "");
            }

            return remainingPatternSegments.length();
        }

        private void storePatternAndValue(String pattern, Integer value) {
            valueMap.put(pattern, value);
            patternMap.put(value, pattern);
        }

        private void decodeNotes() {
            notes.forEach(p -> {
                switch (p.pattern.length()) {
                    case 2 -> storePatternAndValue(p.pattern, 1);
                    case 3 -> storePatternAndValue(p.pattern, 7);
                    case 4 -> storePatternAndValue(p.pattern, 4);
                    case 7 -> storePatternAndValue(p.pattern, 8);
                }
            });

            // calculate 2
            notes.forEach(p -> {
                if (p.pattern.length() != 5 || valueMap.containsKey(p.pattern)) return;
                if (getRemainingSegmentCount(p.pattern, 4) == 3) {
                    storePatternAndValue(p.pattern, 2);
                }
            });

            // calculate 3 and 5
            notes.forEach(p -> {
                if (p.pattern.length() != 5 || valueMap.containsKey(p.pattern)) return;
                var remainingSegmentCount = getRemainingSegmentCount(p.pattern, 2);
                if (remainingSegmentCount == 2) {
                    storePatternAndValue(p.pattern, 5);
                } else if (remainingSegmentCount == 1) {
                    storePatternAndValue(p.pattern, 3);
                }
            });

            // calculate 0
            notes.forEach(p -> {
                if (p.pattern.length() != 6 || valueMap.containsKey(p.pattern)) return;
                if (getRemainingSegmentCount(p.pattern, 5) == 2) {
                    storePatternAndValue(p.pattern, 0);
                }
            });

            // calculate 6 and 9
            notes.forEach(p -> {
                if (p.pattern.length() != 6 || valueMap.containsKey(p.pattern)) return;
                var remainingSegmentCount = getRemainingSegmentCount(p.pattern, 7);
                if (remainingSegmentCount == 3) {
                    storePatternAndValue(p.pattern, 9);
                } else if (remainingSegmentCount == 4) {
                    storePatternAndValue(p.pattern, 6);
                }
            });
        }

        public Integer decode() {
            if (valueMap.size() != 10) decodeNotes();

            var value = outputs.stream()
                    .map(output -> valueMap.get(output.pattern).toString())
                    .collect(Collectors.joining());

            return Integer.parseInt(String.join("", value));
        }

        @Override
        public String toString() {
            return "Entry: " + notes + " | " + outputs;
        }

        public static Entry parse(String rawValue) {
            var entryTokens = rawValue.split("\\|");
            var patterns = Arrays.stream(entryTokens[0].strip().split(" ")).map(Pattern::parse).toList();
            var outputs = Arrays.stream(entryTokens[1].strip().split(" ")).map(Pattern::parse).toList();
            return new Entry(patterns, outputs);
        }
    }

    private static void countOccurrencesOfOneFourSevenAndEight(List<Entry> entries) {
        var occurrences = entries.stream()
                .flatMap(entry -> Arrays.stream(entry.decode().toString().split("")).map(Integer::parseInt))
                .filter(digit -> digit == 1 || digit == 4 || digit == 7 || digit == 8)
                .count();

        System.out.println("There are a total of '" + occurrences + "' 1s, 4s, 7s, and 8s in the input");
    }

    private static void calculateSumOfOutputValues(List<Entry> entries) {
        var sumOfOutpuValues = entries.stream()
                .map(entry -> entry.decode())
                .reduce(0, Integer::sum);

        System.out.println("The sum of all the outputs is '" + sumOfOutpuValues + "'");
    }

    public static void main(String[] args) {
        var entries = getLineStream().map(Entry::parse).peek(Entry::decode).toList();

        countOccurrencesOfOneFourSevenAndEight(entries);
        calculateSumOfOutputValues(entries);
    }

}

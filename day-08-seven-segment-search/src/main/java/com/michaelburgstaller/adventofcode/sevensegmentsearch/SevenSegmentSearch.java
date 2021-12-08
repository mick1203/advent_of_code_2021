package com.michaelburgstaller.adventofcode.sevensegmentsearch;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SevenSegmentSearch extends Exercise {

    private static final Map<String, Integer> NORMAL_PATTERNS = Map.of(
            "abcefg", 0,
            "cf", 1,
            "acdeg", 2,
            "acdfg", 3,
            "bcdf", 4,
            "abdfg", 5,
            "abdefg", 6,
            "acf", 7,
            "abcdefg", 8,
            "abcdfg", 9
    );

    private static class Output {
        public String pattern;
        public Integer value;

        public Output(String pattern) {
            this.pattern = pattern;
            this.value = null;
        }

        public void decode(Map<String, Integer> decodingTable) {
            value = decodingTable.get(pattern);
        }

        @Override
        public String toString() {
            return pattern + '(' + value + ')';
        }

        public static Output parse(String rawValue) {
            var outputTokens = rawValue.strip().split("");
            var pattern = Arrays.stream(outputTokens).sorted().collect(Collectors.joining(""));
            return new Output(pattern);
        }
    }

    private static class Pattern {
        public String pattern;
        public Integer value;

        public Pattern(String pattern) {
            this.pattern = pattern;
            this.value = null;
        }

        public void decode() {
            switch (pattern.length()) {
                case 2:
                    value = 1;
                    break;
                case 3:
                    value = 7;
                    break;
                case 4:
                    value = 4;
                    break;
                case 5:
                    break; // 2, 3, 5
                case 6:
                    break; // 0, 6, 9
                case 7:
                    value = 8;
                    break;
            }
        }

        @Override
        public String toString() {
            return pattern + '(' + value + ')';
        }

        public static Pattern parse(String rawValue) {
            var valueTokens = rawValue.strip().split("");
            var value = Arrays.stream(valueTokens).sorted().collect(Collectors.joining(""));
            return new Pattern(value);
        }
    }

    private static class Entry {
        public List<Pattern> patterns;
        public List<Output> outputs;
        public Map<String, Integer> decodingTable;

        public Entry(List<Pattern> patterns, List<Output> outputs) {
            this.patterns = patterns;
            this.outputs = outputs;
            this.decodingTable = new HashMap<>();
        }

        public void decode() {
            patterns.forEach(Pattern::decode);

            for (var pattern : patterns) {
                decodingTable.put(pattern.pattern, pattern.value);
            }

            outputs.forEach(output -> output.decode(decodingTable));
        }

        @Override
        public String toString() {
            return "Entry: [" + patterns + " | " + outputs + ']';
        }

        public static Entry parse(String rawValue) {
            var entryTokens = rawValue.split("\\|");
            var patterns = Arrays.stream(entryTokens[0].strip().split(" ")).map(Pattern::parse).toList();
            var outputs = Arrays.stream(entryTokens[1].strip().split(" ")).map(Output::parse).toList();
            return new Entry(patterns, outputs);
        }
    }

    private static void countOccurrencesOfOneFourSevenAndEight(List<Entry> entries) {
        var occurrences = entries.stream()
                .flatMap(entry -> entry.outputs.stream())
                .filter(output -> output.value != null
                ).count();

        System.out.println("There are a total of '" + occurrences + "' 1s, 4s, 7s, and 8s in the input");
    }

    public static void main(String[] args) {
        var entries = getLineStream().map(Entry::parse).toList();
        entries.forEach(Entry::decode);

        countOccurrencesOfOneFourSevenAndEight(entries);

        entries.forEach(System.out::println);
    }

}

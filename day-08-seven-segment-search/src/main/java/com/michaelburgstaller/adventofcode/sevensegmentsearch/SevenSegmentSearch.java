package com.michaelburgstaller.adventofcode.sevensegmentsearch;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private static class Pattern {
        public String value;

        public Pattern(String value) {
            this.value = value;
        }

        public static Pattern parse(String rawValue) {
            var value = rawValue.strip();
            return new Pattern(value);
        }
    }

    private static class Entry {
        public List<Pattern> patterns;
        public List<Pattern> output;

        public Entry(List<Pattern> patterns, List<Pattern> output) {
            this.patterns = patterns;
            this.output = output;
        }

        public static Entry parse(String rawValue) {
            var entryTokens = rawValue.split("\\|");
            var patterns = Arrays.stream(entryTokens[0].split(" ")).map(Pattern::parse).toList();
            var outputs = Arrays.stream(entryTokens[1].split(" ")).map(Pattern::parse).toList();
            return new Entry(patterns, outputs);
        }
    }

    private static void countOccurrencesOfOneFourSevenAndEight(List<Entry> entries) {
        var occurrences = entries.stream()
                .flatMap(entry -> entry.output.stream())
                .filter(output ->
                        output.value.length() == 2 // 1
                        || output.value.length() == 3 // 7
                        || output.value.length() == 4 // 4
                        || output.value.length() == 7 // 8
                ).count();

        System.out.println("There are a total of '" + occurrences + "' 1s, 4s, 7s, and 8s in the input");
    }

    public static void main(String[] args) {
        var lines = getLineStream().map(Entry::parse).toList();

        countOccurrencesOfOneFourSevenAndEight(lines);
    }

}

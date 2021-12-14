package com.michaelburgstaller.adventofcode.extendedpolymerization;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedPolymerization extends Exercise {

    private static class Extender {
        public Map<String, String> mapping;

        public Extender(Map<String, String> mapping) {
            this.mapping = mapping;
        }

        public Map<String, Long> extend(Map<String, Long> state, Map<String, Long> counts) {
            Map<String, Long> nextState = new HashMap<>();

            for (var entry : state.entrySet()) {
                var amount = entry.getValue();
                var keyTokens = mapping.get(entry.getKey()).split("");
                counts.put(keyTokens[1], counts.getOrDefault(keyTokens[1], 0L) + amount);
                for (var i = 0; i < keyTokens.length - 1; i++) {
                    var pair = keyTokens[i] + keyTokens[i + 1];
                    nextState.put(pair, nextState.getOrDefault(pair, 0L) + amount);
                }
            }

            return nextState;
        }

        public static Extender parse(List<String> rules) {
            var mapping = new HashMap<String, String>();

            for (var rule : rules) {
                var ruleTokens = rule.strip().split(" -> ");
                var lhs = ruleTokens[0];
                var rhs = ruleTokens[1];
                var lhsTokens = lhs.split("");
                mapping.put(lhs, lhsTokens[0] + rhs + lhsTokens[1]);
            }

            return new Extender(mapping);
        }
    }

    private static void subtractMostCommonFromLeastCommonElementOccurrencesAfterSteps(Extender extender, String initial, Integer steps) {
        var polymerTokens = initial.split("");
        Map<String, Long> state = new HashMap<>();
        Map<String, Long> counts = new HashMap<>();

        for (var token : polymerTokens) {
            counts.put(token, counts.getOrDefault(token, 0L) + 1L);
        }

        for (var i = 0; i < polymerTokens.length - 1; i++) {
            var pair = polymerTokens[i] + polymerTokens[i + 1];
            state.put(pair, state.getOrDefault(pair, 0L) + 1L);
        }

        for (var i = 0; i < steps; i++) {
            state = extender.extend(state, counts);
        }

        var mostCommon = Long.MIN_VALUE;
        var leastCommon = Long.MAX_VALUE;
        for (var entry : counts.values()) {
            if (entry < leastCommon) leastCommon = entry;
            if (entry > mostCommon) mostCommon = entry;
        }

        System.out.println("After '" + steps + "' steps, subtracting the least common from the most common element results in a value of '" + (mostCommon - leastCommon) + "'");
    }

    public static void main(String[] args) {
        var batches = getBufferedLineStream(getLineStream(), "").toList();
        var initialPolymer = batches.get(0).get(0);
        var extender = Extender.parse(batches.get(1));

        subtractMostCommonFromLeastCommonElementOccurrencesAfterSteps(extender, initialPolymer, 10);
        subtractMostCommonFromLeastCommonElementOccurrencesAfterSteps(extender, initialPolymer, 40);
    }
}

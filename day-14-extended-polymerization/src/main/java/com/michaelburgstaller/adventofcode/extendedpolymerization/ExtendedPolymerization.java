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

        public String extend(String base) {
            var extended = new StringBuilder();

            var baseTokens = base.split("");
            for (var i = 0; i < baseTokens.length - 1; i++) {
                var pair = baseTokens[i] + baseTokens[i + 1];
                var extension = mapping.get(pair);
                extended.append(baseTokens[i] + extension);
            }
            extended.append(baseTokens[baseTokens.length - 1]);

            return extended.toString();
        }

        public static Extender parse(List<String> rules) {
            var mapping = new HashMap<String, String>();

            for (var rule : rules) {
                var ruleTokens = rule.strip().split(" -> ");
                mapping.put(ruleTokens[0], ruleTokens[1]);
            }

            return new Extender(mapping);
        }
    }

    private static void subtractMostCommonFromLeastCommonElementOccurrencesAfterSteps(Extender extender, String initial, Integer steps) {
        var polymerToBeExtended = initial.split("");
        var count = new HashMap<String, Integer>();

        for (var i = 0; i < polymerToBeExtended.length - 1; i++) {
            var base = polymerToBeExtended[i] + polymerToBeExtended[i + 1];
            for (var j = 0; j < steps; j++) {
                base = extender.extend(base);
            }

            if (i < polymerToBeExtended.length - 2) {
                base = base.substring(0, base.length() - 1);
            }

            for (var element : base.split("")) {
                count.put(element, count.getOrDefault(element, 0) + 1);
            }
        }

        var mostCommon = Integer.MIN_VALUE;
        var leastCommon = Integer.MAX_VALUE;
        for (var entry : count.values()) {
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
        // subtractMostCommonFromLeastCommonElementOccurrencesAfterSteps(extender, initialPolymer, 40);
    }
}

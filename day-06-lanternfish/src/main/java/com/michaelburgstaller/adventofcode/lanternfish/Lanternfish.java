package com.michaelburgstaller.adventofcode.lanternfish;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lanternfish extends Exercise {

    private static final Long FISH_MATURITY_TIME_FRAME = 2L;
    private static final Long FISH_REPRODUCTION_CYCLE = 6L;
    private static final Long NEW_FISH_REPRODUCTION_CYCLE = FISH_REPRODUCTION_CYCLE + FISH_MATURITY_TIME_FRAME;

    private static void simulate(List<Long> fishSwarm, Integer days) {
        Map<Long, Long> descendants = new HashMap<>();
        descendants.put(0L, 0L); // base case, no time left thus no future offspring

        for (var day = 0L; day < days; day++) {
            for (var age = 0L; age < NEW_FISH_REPRODUCTION_CYCLE; age++) {
                if (age >= day) continue; // skip fish that have no chance of reproducing

                // normalize the fish, as a fish with age 3 and 6 days left, in short (3,6)-fish,
                // produces the same amount of descendents as a (0,3)-fish

                var normalizedDays = day - age;

                // the (0,normalizedDays)-fish produces a new fish, [+1]
                // and keeps producing until the end,               [descendants.get(normalizedDays)]
                // the new fish is the same as a
                //     (0, normalizedDays - NEW_FISH_REPRODUCTION_CYCLE)-fish
                // and these are also descendants of the current fish

                var nextCycle = Math.max(normalizedDays - FISH_REPRODUCTION_CYCLE, 0L);
                var firstCycleOfNewFish = Math.max(normalizedDays - NEW_FISH_REPRODUCTION_CYCLE, 0L);

                var ownDescendents = descendants.get(nextCycle);
                var newFishDescendents = descendants.get(firstCycleOfNewFish);

                var children = 1 + ownDescendents + newFishDescendents;

                descendants.put(normalizedDays, children);
            }
        }

        var lanternfishAtTheEnd = 0L;
        for (var fish : fishSwarm) {
            lanternfishAtTheEnd += descendants.get(days - fish);
        }

        System.out.println("After '" + days + "' days there are a total of '" + lanternfishAtTheEnd + "' lanternfish!");
    }

    public static void main(String[] args) {
        var lanternfish = getLineStream("example.txt")
                .flatMap(line -> Arrays.stream(line.split(",")))
                .map(Long::parseLong)
                .toList();

        simulate(lanternfish, 18);
        simulate(lanternfish, 80);
        simulate(lanternfish, 256);
    }

}

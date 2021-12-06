package com.michaelburgstaller.adventofcode.lanternfish;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Lanternfish extends Exercise {

    private static Stream<Integer> handleLanternfish(Integer fish) {
        if (fish == 0) {
            return Stream.of(6, 8);
        } else {
            return Stream.of(fish - 1);
        }
    }

    private static void simulate(List<Integer> lanternfish, Integer days) {
        List<Integer> localPopulation = new ArrayList<>(lanternfish);

        for (var i = 0; i < days; i++) {
            localPopulation = localPopulation.stream().flatMap(Lanternfish::handleLanternfish).toList();
        }

        System.out.println("After '" + days + "' days there are a total of '" + localPopulation.size() + "' lanternfish");
    }

    public static void main(String[] args) {
        var lanternfish = getLineStream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .map(Integer::parseInt)
                .toList();

        simulate(lanternfish, 18);
        simulate(lanternfish, 80);
    }

}

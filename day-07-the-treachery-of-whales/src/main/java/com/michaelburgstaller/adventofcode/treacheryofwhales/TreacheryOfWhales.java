package com.michaelburgstaller.adventofcode.treacheryofwhales;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.Arrays;

public class TreacheryOfWhales extends Exercise {

    public static void main(String[] args) {
        var crabSubmarines = getLineStream("example.txt")
                .flatMap(line -> Arrays.stream(line.split(",")))
                .map(Integer::parseInt)
                .toList();

        System.out.println(crabSubmarines);
    }

}

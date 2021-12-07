package com.michaelburgstaller.adventofcode.treacheryofwhales;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TreacheryOfWhales extends Exercise {

    private static final Integer calculateFuelExpense(List<Integer> crabSubmarines, Integer desiredPosition) {
        var fuelExpense = 0;

        for (var crabSubmarine : crabSubmarines) {
            fuelExpense += Math.abs(crabSubmarine - desiredPosition);
        }

        return fuelExpense;
    }

    private static void findLeastExpensivePosition(List<Integer> crabSubmarines) {
        var crabs = new ArrayList<>(crabSubmarines);
        crabs.sort(Comparator.naturalOrder());

        var minimumPosition = crabs.get(0);
        var maximumPosition = crabs.get(crabs.size() - 1) + 1; // also include maximum position

        var smallestFuelExpense = Integer.MAX_VALUE;
        var optimalPosition = 0;
        for (var i = minimumPosition; i < maximumPosition; i++) {
            var fuelExpense = calculateFuelExpense(crabs, i);
            if (fuelExpense < smallestFuelExpense) {
                smallestFuelExpense = fuelExpense;
                optimalPosition = i;
            }
        }

        System.out.println("The optimal position is '" + optimalPosition + "' with a total fuel expense of '" + smallestFuelExpense + "'");
    }

    public static void main(String[] args) {
        var crabSubmarines = getLineStream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .map(Integer::parseInt)
                .toList();

        findLeastExpensivePosition(crabSubmarines);
    }
}

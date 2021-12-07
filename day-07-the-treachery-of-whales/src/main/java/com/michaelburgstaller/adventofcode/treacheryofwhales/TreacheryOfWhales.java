package com.michaelburgstaller.adventofcode.treacheryofwhales;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class TreacheryOfWhales extends Exercise {

    private static final Integer calculateFuelExpense(List<Integer> crabSubmarines, Integer desiredPosition, BiFunction<Integer, Integer, Integer> fuelConsumption) {
        var fuelExpense = 0;

        for (var crabSubmarine : crabSubmarines) {
            fuelExpense += fuelConsumption.apply(crabSubmarine, desiredPosition);
        }

        return fuelExpense;
    }

    private static void findLeastExpensivePosition(List<Integer> submarines, BiFunction<Integer, Integer, Integer> fuelConsumption) {
        var sortedSubmarines = new ArrayList<>(submarines);
        sortedSubmarines.sort(Comparator.naturalOrder());

        var minimumPosition = sortedSubmarines.get(0);
        var maximumPosition = sortedSubmarines.get(sortedSubmarines.size() - 1);

        var smallestFuelExpense = Integer.MAX_VALUE;
        var optimalPosition = 0;
        for (var i = minimumPosition; i <= maximumPosition; i++) { // also include maximumPosition
            var fuelExpense = calculateFuelExpense(sortedSubmarines, i, fuelConsumption);
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

        findLeastExpensivePosition(crabSubmarines, (crabSubmarine, desiredPosition) -> Math.abs(crabSubmarine - desiredPosition));
        findLeastExpensivePosition(crabSubmarines, (crabSubmarine, desiredPosition) -> IntStream.range(0, Math.abs(crabSubmarine - desiredPosition) + 1).sum());
    }
}

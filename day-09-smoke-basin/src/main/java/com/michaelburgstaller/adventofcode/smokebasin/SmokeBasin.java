package com.michaelburgstaller.adventofcode.smokebasin;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SmokeBasin extends Exercise {

    private static class Floor {
        public Integer[][] grid;

        public Floor(Integer[][] grid) {
            this.grid = grid;
        }

        public static Floor parse(Stream<String> rawValues) {
            var rawValueList = rawValues.toList();
            var height = rawValueList.size();
            var width = rawValueList.get(0).length();
            var grid = new Integer[height][width];

            for (var row = 0; row < height; row++) {
                var values = rawValueList.get(row).split("");
                for (var column = 0; column < width; column++) {
                    grid[row][column] = Integer.parseInt(values[column]);
                }
            }

            return new Floor(grid);
        }
    }

    private static class Tuple<L, R> {
        public L left;
        public R right;

        public Tuple(L left, R right) {
            this.left = left;
            this.right = right;
        }
    }

    private static Boolean isLowest(Integer currentPosition, List<Integer> neighbors) {
        for (var neighbor : neighbors) {
            if (neighbor <= currentPosition) {
                return false;
            }
        }
        return true;
    }

    private static List<Tuple<Integer, Integer>> getNeighborCoordinates(Tuple<Integer, Integer> current, Integer height, Integer width) {
        var coordinates = new ArrayList<Tuple<Integer, Integer>>();

        if (current.left - 1 >= 0) {
            coordinates.add(new Tuple<>(current.left - 1, current.right));
        }

        if (current.left + 1 < height) {
            coordinates.add(new Tuple<>(current.left + 1, current.right));
        }

        if (current.right - 1 >= 0) {
            coordinates.add(new Tuple<>(current.left, current.right - 1));
        }

        if (current.right + 1 < width) {
            coordinates.add(new Tuple<>(current.left, current.right + 1));
        }

        return coordinates;
    }

    private static void findSumOfRiskLevelForLowestPointsOnFloor(Floor floor) {
        var sumOfRisk = 0;
        for (var row = 0; row < floor.grid.length; row++) {
            for (var column = 0; column < floor.grid[0].length; column++) {
                var current = floor.grid[row][column];
                var neighbors = getNeighborCoordinates(new Tuple<>(row, column), floor.grid.length, floor.grid[row].length)
                        .stream().map(neighbor -> floor.grid[neighbor.left][neighbor.right])
                        .toList();

                if (isLowest(current, neighbors)) {
                    sumOfRisk += 1 + current;
                }
            }
        }

        System.out.println("The sum of the risk levels of the lowest points on this floor is '" + sumOfRisk + "'");
    }

    public static void main(String[] args) {
        var floor = Floor.parse(getLineStream());

        findSumOfRiskLevelForLowestPointsOnFloor(floor);
    }

}

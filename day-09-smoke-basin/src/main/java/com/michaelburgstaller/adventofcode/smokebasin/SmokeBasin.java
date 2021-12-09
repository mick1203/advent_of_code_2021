package com.michaelburgstaller.adventofcode.smokebasin;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.Comparator;
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

    private static class Basin {
        public List<Tuple<Integer, Integer>> points;
        public Floor floor;

        public Basin(Floor floor, Tuple<Integer, Integer> lowestPoint) {
            this.points = new ArrayList<>();
            points.add(lowestPoint);
            this.floor = floor;
        }

        public void expand() {
            var shouldExpand = true;
            var expandedBasin = new ArrayList<Tuple<Integer, Integer>>();
            while (shouldExpand) {
                for (var point : points) {
                    if (!containsPoint(expandedBasin, point)) expandedBasin.add(point);
                    for (var neighbor : getNeighborCoordinates(point, floor.grid.length, floor.grid[0].length)) {
                        if (!containsPoint(expandedBasin, neighbor) && floor.grid[neighbor.left][neighbor.right] != 9) {
                            expandedBasin.add(neighbor);
                        }
                    }
                }

                shouldExpand = points.size() != expandedBasin.size();
                points = expandedBasin;
                expandedBasin = new ArrayList();
            }
        }

        public Integer getSize() {
            return points.size();
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();

            for (var row = 0; row < floor.grid.length; row++) {
                for (var column = 0; column < floor.grid[0].length; column++) {
                    if (containsPoint(points, new Tuple<>(row, column))) {
                        builder.append("#");
                    } else {
                        builder.append(".");
                    }
                }
                builder.append("\n");
            }

            return builder.toString();
        }
    }

    public static Boolean containsPoint(List<Tuple<Integer, Integer>> list, Tuple<Integer, Integer> point) {
        return list.stream()
                .filter(p -> p.left.compareTo(point.left) == 0 && p.right.compareTo(point.right) == 0)
                .count() > 0;
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

    private static Boolean isLowest(Integer currentPosition, List<Integer> neighbors) {
        for (var neighbor : neighbors) {
            if (neighbor <= currentPosition) {
                return false;
            }
        }
        return true;
    }

    private static List<Tuple<Integer, Integer>> findLowestPoints(Floor floor) {
        var lowestPoints = new ArrayList<Tuple<Integer, Integer>>();

        for (var row = 0; row < floor.grid.length; row++) {
            for (var column = 0; column < floor.grid[0].length; column++) {
                var current = floor.grid[row][column];
                var neighbors = getNeighborCoordinates(new Tuple<>(row, column), floor.grid.length, floor.grid[row].length)
                        .stream().map(neighbor -> floor.grid[neighbor.left][neighbor.right])
                        .toList();

                if (isLowest(current, neighbors)) {
                    lowestPoints.add(new Tuple<>(row, column));
                }
            }
        }

        return lowestPoints;
    }

    private static void findSumOfRiskLevelForLowestPointsOnFloor(Floor floor) {
        var lowestPoints = findLowestPoints(floor);

        var sumOfRisk = 0;
        for (var lowestPoint : lowestPoints) {
            sumOfRisk += 1 + floor.grid[lowestPoint.left][lowestPoint.right];
        }

        System.out.println("The sum of the risk levels of the lowest points on this floor is '" + sumOfRisk + "'");
    }

    private static void calculateProductOfLargestBasins(Floor floor, Integer number) {
        var lowestPoints = findLowestPoints(floor);

        var basins = new ArrayList<Basin>();
        for (var lowestPoint : lowestPoints) {
            basins.add(new Basin(floor, lowestPoint));
        }

        for (var basin : basins) {
            basin.expand();
        }

        basins.sort(Comparator.comparingInt(Basin::getSize).reversed());

        var product = 1;
        for (var i = 0; i < number; i++) {
            product *= basins.get(i).getSize();
        }

        System.out.println("The product of the '" + number + "' largest basins is '" + product + "'");
    }


    public static void main(String[] args) {
        var floor = Floor.parse(getLineStream());

        findSumOfRiskLevelForLowestPointsOnFloor(floor);
        calculateProductOfLargestBasins(floor, 3);
    }

}

package com.michaelburgstaller.adventofcode.dumbooctopus;

import com.michaelburgstaller.adventofcode.common.Exercise;
import com.michaelburgstaller.adventofcode.common.Tuple;

import java.util.ArrayList;
import java.util.List;

public class DumboOctopus extends Exercise {

    private static final Integer MAX_ENERGY_LEVEL = 9;

    private static class Octopus {
        private Integer initialEnergyLevel;
        public Integer energyLevel;
        public Boolean hasFlashed;
        public Tuple<Integer, Integer> position;

        public Octopus(Integer energyLevel) {
            this.initialEnergyLevel = energyLevel;
            this.energyLevel = energyLevel;
            this.hasFlashed = false;
            this.position = null;
        }

        public void increase() {
            energyLevel++;
        }

        public void flash() {
            if (hasFlashed) return;
            hasFlashed = true;
        }

        public void reset() {
            energyLevel = initialEnergyLevel;
        }

        @Override
        public String toString() {
            if (energyLevel > MAX_ENERGY_LEVEL && hasFlashed) {
                return ANSI_BLACK + energyLevel + ANSI_RESET;
            } else if (energyLevel > MAX_ENERGY_LEVEL) {
                return ANSI_RED + energyLevel + ANSI_RESET;
            } else {
                return energyLevel.toString();
            }
        }

        public static Octopus parse(String rawValue) {
            var energyLevel = Integer.parseInt(rawValue.strip());
            return new Octopus(energyLevel);
        }
    }

    private static class Floor {
        public Octopus[][] grid;
        public Integer totalFlashes;
        public Boolean isSynchronous;

        public Floor(Octopus[][] grid) {
            this.grid = grid;
            this.totalFlashes = 0;
            this.isSynchronous = false;
        }

        private void increaseAllEnergyLevels() {
            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[y].length; x++) {
                    grid[y][x].increase();
                }
            }
        }

        private List<Octopus> getReadyToFlashOctopuses() {
            var readyToFlashOctopuses = new ArrayList<Octopus>();

            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[y].length; x++) {
                    var octopus = grid[y][x];
                    if (octopus.energyLevel > MAX_ENERGY_LEVEL && !octopus.hasFlashed) {
                        readyToFlashOctopuses.add(octopus);
                    }
                }
            }

            return readyToFlashOctopuses;
        }

        private List<Octopus> getSurroundingOctopuses(Octopus octopus) {
            var surroundingOctopuses = new ArrayList<Octopus>();

            for (var y = -1; y < 2; y++) {
                for (var x = -1; x < 2; x++) {
                    if ((octopus.position.left + y) >= 0
                            && (octopus.position.right + x) >= 0
                            && (octopus.position.left + y) < grid.length
                            && (octopus.position.right + x) < grid[0].length) {
                        surroundingOctopuses.add(grid[octopus.position.left][octopus.position.right]);
                    }
                }
            }

            return surroundingOctopuses;
        }

        private Integer resetFlashedOctopuses() {
            var affectedOctopuses = 0;

            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[y].length; x++) {
                    var octopus = grid[y][x];
                    if (octopus.hasFlashed) {
                        octopus.energyLevel = 0;
                        octopus.hasFlashed = false;
                        affectedOctopuses++;
                    }
                }
            }

            return affectedOctopuses;
        }

        public void step() {
            increaseAllEnergyLevels();

            var readyToFlashOctopuses = getReadyToFlashOctopuses();
            while (!readyToFlashOctopuses.isEmpty()) {
                for (var octopus : readyToFlashOctopuses) {
                    octopus.flash();
                    var surroundingOctopuses = getSurroundingOctopuses(octopus);
                    for (var surroundingOctopus : surroundingOctopuses) {
                        surroundingOctopus.increase();
                    }
                }

                totalFlashes += readyToFlashOctopuses.size();
                readyToFlashOctopuses = getReadyToFlashOctopuses();
            }

            var affectedOctopuses = resetFlashedOctopuses();
            isSynchronous = affectedOctopuses == (grid.length * grid[0].length);
        }

        public void reset() {
            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[y].length; x++) {
                    grid[y][x].reset();
                }
            }
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();

            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[y].length; x++) {
                    builder.append(grid[y][x] + "\t");
                }
                builder.append("\n");
            }

            return builder.toString();
        }

        public static Floor parse(List<String> rawValues) {
            var grid = new Octopus[rawValues.size()][rawValues.get(0).length()];

            for (var y = 0; y < rawValues.size(); y++) {
                var row = rawValues.get(y).split("");
                for (var x = 0; x < rawValues.get(y).length(); x++) {
                    var octopus = Octopus.parse(row[x]);
                    octopus.position = new Tuple<>(y, x);
                    grid[y][x] = octopus;
                }
            }

            return new Floor(grid);
        }
    }

    private static void simulateNumberOfSteps(Floor floor, Integer numberOfSteps) {
        floor.reset();

        for (var i = 0; i < numberOfSteps; i++) {
            floor.step();
        }

        System.out.println("After '" + numberOfSteps + "' steps there were a total of '" + floor.totalFlashes + "' flashes.");
    }

    private static void simulateUntilFirstSynchronousFlash(Floor floor) {
        floor.reset();

        var i = 0;
        while (!floor.isSynchronous) {
            floor.step();
            i++;
        }

        System.out.println("After '" + i + "' steps the first synchronous flash occurred.");
    }

    public static void main(String[] args) {
        var floor = Floor.parse(getLineStream().toList());

        simulateNumberOfSteps(floor, 100);
        simulateUntilFirstSynchronousFlash(floor);
    }
}

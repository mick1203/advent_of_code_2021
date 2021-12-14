package com.michaelburgstaller.adventofcode.transparentorigami;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.Comparator;
import java.util.List;

public class TransparentOrigami extends Exercise {

    private static class Point {
        public Integer x;
        public Integer y;

        public Point(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getY() {
            return y;
        }

        public void setY(Integer y) {
            this.y = y;
        }

        public static Point parse(String rawValue) {
            var pointTokens = rawValue.split(",");
            var x = Integer.parseInt(pointTokens[0].strip());
            var y = Integer.parseInt(pointTokens[1].strip());
            return new Point(x, y);
        }
    }

    private static class Paper {
        public Boolean[][] grid;

        public Paper(Boolean[][] grid) {
            this.grid = grid;
        }

        public void fold(Fold fold) {
            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[y].length; x++) {
                    switch (fold.direction) {
                        case UP -> {
                            if (y > fold.line) {
                                var distance = fold.line - y;
                                var newY = fold.line + distance;
                                grid[newY][x] = grid[newY][x] || grid[y][x];
                                grid[y][x] = false;
                            }
                        }
                        case LEFT -> {
                            if (x > fold.line) {
                                var distance = fold.line - x;
                                var newX = fold.line + distance;
                                grid[y][newX] = grid[y][newX] || grid[y][x];
                                grid[y][x] = false;
                            }
                        }
                    }
                }
            }

            var newMaxY = fold.direction == FoldDirection.UP ? fold.line : grid.length;
            var newMaxX = fold.direction == FoldDirection.LEFT ? fold.line : grid[0].length;
            var newGrid = new Boolean[newMaxY][newMaxX];
            for (var y = 0; y < newMaxY; y++) {
                for (var x = 0; x < newMaxX; x++) {
                    newGrid[y][x] = grid[y][x];
                }
            }

            grid = newGrid;
        }

        public Integer visiblePoints() {
            var count = 0;

            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[y].length; x++) {
                    if (grid[y][x]) {
                        count++;
                    }
                }
            }

            return count;
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();

            for (var row = 0; row < grid.length; row++) {
                for (var column = 0; column < grid[row].length; column++) {
                    builder.append(grid[row][column] ? "#" : ".");
                }
                builder.append("\n");
            }

            return builder.toString();
        }

        public static Paper parse(List<String> rawValues) {
            var points = rawValues.stream().map(Point::parse).toList();
            var maxX = points.stream().map(Point::getX).max(Comparator.naturalOrder()).get();
            var maxY = points.stream().map(Point::getY).max(Comparator.naturalOrder()).get();
            var grid = new Boolean[maxY + 1][maxX + 1];

            for (var y = 0; y < grid.length; y++) {
                for (var x = 0; x < grid[y].length; x++) {
                    grid[y][x] = false;
                }
            }

            points.forEach(point -> grid[point.y][point.x] = true);

            return new Paper(grid);
        }
    }

    private enum FoldDirection {
        UP,
        LEFT;

        public static FoldDirection parse(String rawValue) {
            return switch (rawValue) {
                case "x" -> LEFT;
                case "y" -> UP;
                default -> null;
            };
        }
    }

    private static class Fold {
        public Integer line;
        public FoldDirection direction;

        public Fold(Integer line, FoldDirection direction) {
            this.line = line;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "Fold{" +
                    "line=" + line +
                    ", direction=" + direction +
                    '}';
        }

        public static Fold parse(String rawValue) {
            var rawTokens = rawValue.split(" ");
            var foldTokens = rawTokens[2].split("=");
            var line = Integer.parseInt(foldTokens[1]);
            var direction = FoldDirection.parse(foldTokens[0]);
            return new Fold(line, direction);
        }
    }

    private static void foldPaper(Paper paper, List<Fold> folds) {
        for (var fold : folds) {
            paper.fold(fold);
            System.out.println(paper);
        }
    }

    public static void main(String[] args) {
        var lines = getBufferedLineStream(getLineStream(), "").toList();
        var paper = Paper.parse(lines.get(0));
        var folds = lines.get(1).stream().map(Fold::parse).toList();

        foldPaper(paper, folds);

        System.out.println(paper);
        System.out.println(folds);
    }

}

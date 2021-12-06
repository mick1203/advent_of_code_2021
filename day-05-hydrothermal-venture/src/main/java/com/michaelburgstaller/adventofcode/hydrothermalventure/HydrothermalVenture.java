package com.michaelburgstaller.adventofcode.hydrothermalventure;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.List;

public class HydrothermalVenture extends Exercise {

    private static class Point {
        public Integer x;
        public Integer y;

        public Point(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        public static Point parse(String rawValue) {
            var tokens = rawValue.split(",");
            var x = Integer.parseInt(tokens[0]);
            var y = Integer.parseInt(tokens[1]);
            return new Point(x, y);
        }
    }

    private static class Line {
        public Point start;
        public Point end;

        public Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "Line{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }

        public static Line parse(String rawValue) {
            var tokens = rawValue.strip().split(" -> ");
            var start = Point.parse(tokens[0]);
            var end = Point.parse(tokens[1]);
            return new Line(start, end);
        }
    }

    private static class Grid {
        public Integer height;
        public Integer width;
        public Integer[][] grid;

        public Grid(Integer width, Integer height) {
            this.width = width;
            this.height = height;
            this.grid = new Integer[this.width][this.height];
            reset();
        }

        public void draw(Line line) {
            var xDiff = Math.abs(line.start.x - line.end.x);
            var yDiff = Math.abs(line.start.y - line.end.y);
            var xStep = line.start.x.compareTo(line.end.x) * -1;
            var yStep = line.start.y.compareTo(line.end.y) * -1;

            if (xDiff == 0) {
                for (var i = 0; i <= yDiff; i++) {
                    var y = line.start.y + i * yStep;
                    grid[line.start.x][y] += 1;
                }
            } else if (yDiff == 0) {
                for (var i = 0; i <= xDiff; i++) {
                    var x = line.start.x + i * xStep;
                    grid[x][line.start.y] += 1;
                }
            } else {
                for (var i = 0; i <= xDiff; i++) {
                    var x = line.start.x + i * xStep;
                    var y = line.start.y + i * yStep;
                    grid[x][y] += 1;
                }
            }
        }

        public void reset() {
            for (var x = 0; x < width; x++) {
                for (var y = 0; y < height; y++) {
                    grid[x][y] = 0;
                }
            }
        }

        public List<Point> findPointsWithOverlaps(Integer threshold) {
            var points = new ArrayList<Point>();

            for (var x = 0; x < width; x++) {
                for (var y = 0; y < height; y++) {
                    if (grid[x][y] >= threshold) {
                        points.add(new Point(x, y));
                    }
                }
            }

            return points;
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();

            for (var x = 0; x < width; x++) {
                for (var y = 0; y < height; y++) {
                    var value = grid[x][y];
                    if (value == 0) {
                        builder.append(".");
                    } else {
                        builder.append(value);
                    }
                }
                builder.append("\n");
            }

            return builder.toString();
        }
    }

    public static void main(String[] args) {
        var lines = getLineStream().map(Line::parse).toList();
        var height = lines.stream().map(line -> Math.max(line.start.y, line.end.y)).reduce(0, Math::max);
        var width = lines.stream().map(line -> Math.max(line.start.x, line.end.x)).reduce(0, Math::max);
        var grid = new Grid(width + 1, height + 1); // +1 due to zero-indexing

        lines.stream()
            // .filter(line -> (line.start.x.compareTo(line.end.x) == 0) || (line.start.y.compareTo(line.end.y) == 0))
            .forEach(grid::draw);

        var points = grid.findPointsWithOverlaps(2);

        System.out.println(grid);
        System.out.println("There are '" + points.size() + "' points where two or more lines cross!");
    }

}

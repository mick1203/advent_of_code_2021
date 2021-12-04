package com.michaelburgstaller.adventofcode.giantsquid;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GiantSquid extends Exercise {

    private static class BingoField {
        private static final String ANSI_GREEN = "\u001B[32m";
        private static final String ANSI_RESET = "\u001B[0m";

        public Integer number;
        public Boolean marked;

        public BingoField(Integer number, Boolean marked) {
            this.number = number;
            this.marked = marked;
        }

        public static BingoField parse(String rawValue) {
            var number = Integer.parseInt(rawValue);
            var marked = false;
            return new BingoField(number, marked);
        }

        @Override
        public String toString() {
            return marked ? ANSI_GREEN + number + ANSI_RESET : number.toString();
        }
    }

    private static class BingoBoard implements Iterable<BingoField> {
        public BingoField[][] grid;

        public BingoBoard(BingoField[][] grid) {
            this.grid = grid;
        }

        @Override
        public Iterator<BingoField> iterator() {
            return new BingoFieldIterator(grid);
        }

        public void markNumberIfPresent(Integer number) {
            for (var field : this) {
                if (field.number == number) {
                    field.marked = true;
                }
            }
        }

        public boolean hasWon() {
            for (var row = 0; row < grid.length; row++) {
                var validRow = true;
                for (var column = 0; column < grid[row].length; column++) {
                    validRow = validRow && grid[row][column].marked;
                }
                if (validRow) {
                    return true;
                }
            }

            for (var column = 0; column < grid[0].length; column++) {
                var validColumn = true;
                for (var row = 0; row < grid.length; row++) {
                    validColumn = validColumn && grid[row][column].marked;
                }
                if (validColumn) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();

            for (var row = 0; row < grid.length; row++) {
                for (var column = 0; column < grid[row].length; column++) {
                    builder.append(grid[row][column] + "\t");
                }
                builder.append("\n");
            }

            return builder.toString();
        }

        public static BingoBoard parse(String rawValue) {
            var lines = rawValue.split("\\n");

            var columns = lines[0].split(" +").length;
            var rows = lines.length;
            var grid = new BingoField[rows][columns];

            for (var row = 0; row < rows; row++) {
                var line = lines[row].split(" +");
                for (var column = 0; column < columns; column++) {
                    grid[row][column] = BingoField.parse(line[column]);
                }
            }

            return new BingoBoard(grid);
        }

        private static class BingoFieldIterator implements Iterator<BingoField> {
            private Integer currentIndex;
            private BingoField[][] grid;

            public BingoFieldIterator(BingoField[][] grid) {
                this.grid = grid;
                currentIndex = 0;
            }

            @Override
            public boolean hasNext() {
                var rows = grid.length;
                var columns = grid[0].length;
                return currentIndex < rows * columns;
            }

            @Override
            public BingoField next() {
                var row = currentIndex / grid[0].length;
                var column = currentIndex % grid[0].length;
                var field = grid[row][column];

                currentIndex++;

                return field;
            }
        }
    }

    private static Stream<List<String>> bufferLines(Stream<String> lineStream) {
        var data = lineStream.toList();
        var batches = new ArrayList<List<String>>();
        var batch = new ArrayList<String>();

        for (var line : data) {
            if (line.isBlank()) {
                batches.add(List.copyOf(batch));
                batch.clear();
                continue;
            }
            batch.add(line.strip());
        }
        batches.add(batch);

        return batches.stream();
    }

    public static void main(String[] args) {
        var batches = bufferLines(getLineStream()).toList();
        var drawnNumbers = batches.get(0).stream().flatMap(list -> Arrays.stream(list.split(","))).map(Integer::parseInt).toList();
        var boards = batches.stream().dropWhile(line -> line.size() == 1).map(lines -> lines.stream().collect(Collectors.joining("\n"))).map(BingoBoard::parse).toList();

        for (var drawnNumber : drawnNumbers) {
            boards.forEach(board -> board.markNumberIfPresent(drawnNumber));
            var boardsThatWon = boards.stream().filter(BingoBoard::hasWon).toList();

            if (boardsThatWon.size() != 0) {
                var winningBoard = boardsThatWon.get(0);

                var sumOfUnmarkedFields = 0;
                for (var field : winningBoard) {
                    if (field.marked) continue;
                    sumOfUnmarkedFields += field.number;
                }

                System.out.println(winningBoard);
                System.out.println(sumOfUnmarkedFields * drawnNumber);
                break;
            }
        }
    }
}

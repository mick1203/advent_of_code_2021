package com.michaelburgstaller.adventofcode.giantsquid;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
        public Integer score;

        public BingoBoard(BingoField[][] grid) {
            this.grid = grid;
            this.score = 0;
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

        public void calculateScore(Integer lastDrawnNumber) {
            score = 0;
            for (var field : this) {
                if (field.marked) continue;
                score += field.number;
            }
            score *= lastDrawnNumber;
        }

        public void reset() {
            score = 0;
            for (var field : this) {
                field.marked = false;
            }
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

    private static List<List<BingoBoard>> findWinningRoundsInOrder(List<Integer> drawnNumbers, List<BingoBoard> boards) {
        boards.forEach(BingoBoard::reset);
        var playingBoards = new ArrayList<>(boards);
        var finishedBoards = new ArrayList<List<BingoBoard>>();

        for (var drawnNumber : drawnNumbers) {
            playingBoards.forEach(board -> board.markNumberIfPresent(drawnNumber));
            var boardsThatWon = playingBoards.stream().filter(BingoBoard::hasWon).toList();
            if (boardsThatWon.size() > 0) {
                playingBoards.removeAll(boardsThatWon);
                boardsThatWon.forEach(board -> board.calculateScore(drawnNumber));
                finishedBoards.add(boardsThatWon.stream().toList());
            }
        }

        return finishedBoards;
    }

    private static void findFirstWinningBoard(List<Integer> drawnNumbers, List<BingoBoard> boards) {
        var winningRoundsInOrder = findWinningRoundsInOrder(drawnNumbers, boards);

        var firstWinningRound = winningRoundsInOrder.get(0);
        var firstWinningBoard = firstWinningRound.get(0);
        System.out.println("First Winning Board:" + "\n" + firstWinningBoard);
        System.out.println("Score: " + firstWinningBoard.score);
    }

    private static void findLastWinningBoard(List<Integer> drawnNumbers, List<BingoBoard> boards) {
        var winningRoundsInOrder = findWinningRoundsInOrder(drawnNumbers, boards);

        var lastWinningRound = winningRoundsInOrder.get(winningRoundsInOrder.size() - 1);
        var lastWinningBoard = lastWinningRound.get(0);
        System.out.println("Last Winning Board:" + "\n" + lastWinningBoard);
        System.out.println("Score: " + lastWinningBoard.score);
    }

    public static void main(String[] args) {
        var batches = bufferLines(getLineStream()).toList();
        var drawnNumbers = batches.get(0).stream().flatMap(list -> Arrays.stream(list.split(","))).map(Integer::parseInt).toList();
        var boards = batches.stream().dropWhile(line -> line.size() == 1).map(lines -> lines.stream().collect(Collectors.joining("\n"))).map(BingoBoard::parse).toList();

        findFirstWinningBoard(drawnNumbers, boards);
        findLastWinningBoard(drawnNumbers, boards);
    }
}

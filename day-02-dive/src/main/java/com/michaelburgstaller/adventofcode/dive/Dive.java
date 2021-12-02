package com.michaelburgstaller.adventofcode.dive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Dive {

    private static BufferedReader getFileReader(String path) {
        var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        var inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    private static void simpleCommands(List<Command> commands) {
        var horizontalPosition = commands.stream()
                .filter(command -> command.commandType == CommandType.FORWARD)
                .map(command -> command.distance)
                .reduce(0, Integer::sum);

        var depth = commands.stream()
                .filter(command -> command.commandType != CommandType.FORWARD)
                .map(command -> command.commandType == CommandType.UP ? -command.distance : command.distance)
                .reduce(0, Integer::sum);

        var distanceTravelled = horizontalPosition * depth;

        System.out.println("The submarine is located at [" + horizontalPosition + ", " + depth + "] travelled a distance of " + distanceTravelled + " units.");
    }

    private static void complexCommands(List<Command> commands) {
        var divingContext = new DivingContext();

        commands.forEach(command -> {
            switch (command.commandType) {
                case FORWARD -> {
                    divingContext.horizontalPosition += command.distance;
                    divingContext.depth += (divingContext.aim * command.distance);
                }
                case UP -> divingContext.aim -= command.distance;
                case DOWN -> divingContext.aim += command.distance;
            }
        });

        var distanceTravelled = divingContext.horizontalPosition * divingContext.depth;

        System.out.println("The submarine is located at [" + divingContext.horizontalPosition + ", " + divingContext.depth + "] with an aim of '" + divingContext.aim + "' travelled a distance of " + distanceTravelled + " units.");
    }

    public static void main(String[] args) {
        var fileReader = getFileReader("commands.txt");
        var commands = fileReader.lines().map(Command::parse).toList();

        simpleCommands(commands);
        complexCommands(commands);
    }

}

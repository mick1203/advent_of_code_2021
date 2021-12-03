package com.michaelburgstaller.adventofcode.dive;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.List;

public class Dive extends Exercise {

    private static class DivingContext {
        public Integer horizontalPosition = 0;
        public Integer depth = 0;
        public Integer aim = 0;
    }

    private enum CommandType {
        FORWARD,
        UP,
        DOWN;

        public static CommandType parse(String rawValue) {
            return switch (rawValue) {
                case "forward" -> FORWARD;
                case "up" -> UP;
                case "down" -> DOWN;
                default -> throw new IllegalArgumentException("'" + rawValue + "' is not a valid command!");
            };
        }
    }

    public static class Command {
        public CommandType commandType;
        public Integer distance;

        public Command(CommandType commandType, Integer distance) {
            this.commandType = commandType;
            this.distance = distance;
        }

        public static Command parse(String rawValue) {
            var tokens = rawValue.split(" ");
            var commandType = CommandType.parse(tokens[0]);
            var distance = Integer.parseInt(tokens[1]);
            return new Command(commandType, distance);
        }
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
        var commands = getLineStream().map(Command::parse).toList();

        simpleCommands(commands);
        complexCommands(commands);
    }

}

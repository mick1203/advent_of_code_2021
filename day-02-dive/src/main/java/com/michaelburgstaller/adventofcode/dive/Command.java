package com.michaelburgstaller.adventofcode.dive;

public class Command {
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
package com.michaelburgstaller.adventofcode.dive;

public enum CommandType {
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

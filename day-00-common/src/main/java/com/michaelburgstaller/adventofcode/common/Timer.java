package com.michaelburgstaller.adventofcode.common;

public class Timer {

    private Long start;
    private Long end;

    public Timer() {
    }

    public void start() {
        reset();
        start = System.nanoTime();
    }

    public void end() {
        end = System.nanoTime();
    }

    public void reset() {
        start = null;
        end = null;
    }

    @Override
    public String toString() {
        return "It took '" + (end - start) + "' nanoseconds to execute!";
    }
}

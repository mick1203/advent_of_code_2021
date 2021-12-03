package com.michaelburgstaller.adventofcode.sonarsweep;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SonarSweep extends Exercise {

    private static <T> Stream<T[]> slideOver(Class<T> clazz, List<T> elements, Integer windowSize) {
        if (windowSize > elements.size() || windowSize <= 0) {
            return Stream.empty();
        }

        var windows = new ArrayList<T[]>();
        for (var i = 0; i < elements.size() - (windowSize - 1); i++) {
            var window = (T[]) Array.newInstance(clazz, windowSize);
            for (var j = 0; j < windowSize; j++) {
                window[j] = elements.get(i + j);
            }
            windows.add(window);
        }
        return windows.stream();
    }

    private static Long sweepOceanFloor(List<Long> depths, Integer windowSize) {
        var windows = slideOver(Long.class, depths, windowSize).map(window -> Arrays.stream(window).reduce(0L, Long::sum)).toList();
        return slideOver(Long.class, windows, 2).filter(window -> window[0] < window[1]).count(); // always compare two windows
    }

    public static void main(String[] args) {
        var depths = getLineStream().map(Long::parseLong).toList();

        System.out.println("[Window: 1 element] The depth increased " + sweepOceanFloor(depths, 1) + " times!");
        System.out.println("[Window: 3 elements] The depth increased " + sweepOceanFloor(depths, 3) + " times!");
    }

}

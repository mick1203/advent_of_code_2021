package submarine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class DepthMeasurement {

    private static BufferedReader getFileReader(String path) {
        var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        var inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    public static void main(String[] args) {
        var fileReader = getFileReader("depths.txt");
        var depths = fileReader.lines().map(Long::valueOf).toList();

        var increased = 0;
        for (var i = 0; i < depths.size() - 1; i++) {
            var previousDepth = depths.get(i);
            var currentDepth = depths.get(i + 1);

            if (currentDepth > previousDepth) {
                increased++;
            }
        }

        System.out.println("The depth increased " + increased + " times!");
    }

}

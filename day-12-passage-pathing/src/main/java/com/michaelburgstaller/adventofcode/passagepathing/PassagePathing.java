package com.michaelburgstaller.adventofcode.passagepathing;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class PassagePathing extends Exercise {

    private static class CaveNode {
        public String name;
        public List<CaveNode> connections;

        public CaveNode(String name, List<CaveNode> connections) {
            this.name = name;
            this.connections = connections;
        }

        public static CaveNode parse(String rawValue) {
            var name = rawValue.strip();
            var connections = new ArrayList<CaveNode>();
            return new CaveNode(name, connections);
        }
    }

    private static class Connection {
        public CaveNode from;
        public CaveNode to;

        public Connection(CaveNode from, CaveNode to) {
            this.from = from;
            this.to = to;
        }

        public static Connection parse(String rawValue, Map<String, CaveNode> caveNodes) {
            var connectionTokens = rawValue.split("-");

            var from = caveNodes.getOrDefault(connectionTokens[0].strip(), CaveNode.parse(connectionTokens[0].strip()));
            var to = caveNodes.getOrDefault(connectionTokens[1].strip(), CaveNode.parse(connectionTokens[1].strip()));

            from.connections.add(to);
            to.connections.add(from);

            caveNodes.put(from.name, from);
            caveNodes.put(to.name, to);

            return new Connection(from, to);
        }
    }

    private static class CaveSystem {
        public CaveNode start;
        public CaveNode end;

        public CaveSystem(CaveNode start, CaveNode end) {
            this.start = start;
            this.end = end;
        }

        public static CaveSystem parse(List<String> rawValues) {
            var caveNodes = new HashMap<String, CaveNode>();

            for (var connection : rawValues) {
                Connection.parse(connection, caveNodes);
            }

            return new CaveSystem(caveNodes.get("start"), caveNodes.get("end"));
        }
    }

    private static Boolean isLowercase(String value) {
        return value.contentEquals(value.toLowerCase());
    }

    private static Boolean isUppercase(String value) {
        return !isLowercase(value);
    }

    private static void enumeratePaths(List<List<CaveNode>> paths, List<CaveNode> currentPath, CaveNode currentCave, BiFunction<List<CaveNode>, CaveNode, Boolean> canVisit) {
        if (currentCave.name.contentEquals("end")) {
            currentPath.add(currentCave);
            paths.add(currentPath);
            return;
        }

        if (canVisit.apply(currentPath, currentCave)) {
            currentPath.add(currentCave);
        } else {
            return; // cannot visit current cave, return to stop
        }

        for (var caveNode : currentCave.connections) {
            enumeratePaths(paths, new ArrayList<>(currentPath), caveNode, canVisit);
        }
    }

    private static void calculateNumberOfPathsWithNoRevisitOfSmallCaves(CaveSystem caveSystem) {
        List<List<CaveNode>> paths = new ArrayList<>();

        enumeratePaths(paths, new ArrayList<>(), caveSystem.start, (currentPath, currentCave) -> !currentPath.contains(currentCave) || isUppercase(currentCave.name));

        System.out.println("There are '" + paths.size() + "' possible paths with no revisits of small caves allowed!");
    }

    private static void calculateNumberOfPathsWithARevisitOfASingleSmallCave(CaveSystem caveSystem) {
        List<List<CaveNode>> paths = new ArrayList<>();

        enumeratePaths(paths, new ArrayList<>(), caveSystem.start, (currentPath, currentCave) -> {
            if (isUppercase(currentCave.name)) {
                return true; // visit uppercase caves infinitely many times
            }

            if (!currentPath.contains(currentCave)) {
                return true; // never visited this cave before
            }

            if (currentCave.name.contentEquals("start")) {
                return false; // do not visit start again
            }

            Map<CaveNode, Integer> smallCaveVisits = new HashMap<>();
            for (var caveNode : currentPath) {
                smallCaveVisits.put(caveNode, smallCaveVisits.getOrDefault(caveNode, 0) + 1);
            }

            for (var visits : smallCaveVisits.entrySet()) {
                if (isLowercase(visits.getKey().name) && visits.getValue() == 2) {
                    return false; // already visited another small cave twice
                }
            }

            return true; // never visited another small cave twice
        });

        System.out.println("There are '" + paths.size() + "' possible paths when revisiting a single small cave is allowed!");
    }

    public static void main(String[] args) {
        var caveSystem = CaveSystem.parse(getLineStream().toList());

        calculateNumberOfPathsWithNoRevisitOfSmallCaves(caveSystem);
        calculateNumberOfPathsWithARevisitOfASingleSmallCave(caveSystem);
    }
}

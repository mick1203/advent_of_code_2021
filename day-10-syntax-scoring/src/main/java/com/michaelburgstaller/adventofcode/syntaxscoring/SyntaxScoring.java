package com.michaelburgstaller.adventofcode.syntaxscoring;

import com.michaelburgstaller.adventofcode.common.Exercise;

import java.util.*;
import java.util.stream.Collectors;

public class SyntaxScoring extends Exercise {

    private static Map<String, Integer> syntaxErrorScore = Map.of(")", 3, "]", 57, "}", 1197, ">", 25137);
    private static Map<String, Integer> autocompleteScore = Map.of(")", 1, "]", 2, "}", 3, ">", 4);

    private enum LineStatus {
        CORRUPTED, INCOMPLETE
    }

    private static class Chunk {
        public String value;
        public String firstIllegalCharacter;
        public LineStatus status;
        public Stack<String> openCharacters;

        public Chunk(String value, String firstIllegalCharacter, LineStatus status, Stack<String> openCharacters) {
            this.value = value;
            this.firstIllegalCharacter = firstIllegalCharacter;
            this.status = status;
            this.openCharacters = openCharacters;
        }

        public String autocomplete() {
            var builder = new StringBuilder();

            while (!openCharacters.isEmpty()) {
                var closingCharacter = switch (openCharacters.pop()) {
                    case "(" -> ")";
                    case "[" -> "]";
                    case "{" -> "}";
                    case "<" -> ">";
                    default -> "";
                };
                builder.append(closingCharacter);
            }

            return builder.toString();
        }

        public static Chunk parse(String rawValue) {
            Stack<String> readCharacters = new Stack<>();

            for (var character : rawValue.split("")) {
                switch (character) {
                    case "(", "[", "{", "<" -> {
                        readCharacters.push(character);
                    }
                    default -> {
                        var previousCharacter = readCharacters.pop();
                        switch (character) {
                            case ")" -> {
                                if (!previousCharacter.equalsIgnoreCase("(")) {
                                    readCharacters.push(previousCharacter);
                                    return new Chunk(rawValue, character, LineStatus.CORRUPTED, null);
                                }
                            }
                            case "]" -> {
                                if (!previousCharacter.equalsIgnoreCase("[")) {
                                    readCharacters.push(previousCharacter);
                                    return new Chunk(rawValue, character, LineStatus.CORRUPTED, null);
                                }
                            }
                            case "}" -> {
                                if (!previousCharacter.equalsIgnoreCase("{")) {
                                    readCharacters.push(previousCharacter);
                                    return new Chunk(rawValue, character, LineStatus.CORRUPTED, null);
                                }
                            }
                            case ">" -> {
                                if (!previousCharacter.equalsIgnoreCase("<")) {
                                    readCharacters.push(previousCharacter);
                                    return new Chunk(rawValue, character, LineStatus.CORRUPTED, null);
                                }
                            }
                        }
                    }
                }
            }

            return new Chunk(rawValue, null, LineStatus.INCOMPLETE, readCharacters);
        }
    }

    private static void calculateSyntaxErrorScore(List<Chunk> chunks) {
        var corruptedChunks = chunks.stream().filter(chunk -> chunk.status == LineStatus.CORRUPTED).toList();

        var syntaxErrorScore = 0;
        for (var corruptedChunk : corruptedChunks) {
            syntaxErrorScore += SyntaxScoring.syntaxErrorScore.get(corruptedChunk.firstIllegalCharacter);
        }

        System.out.println("The first invalid characters of these chunks have a overall syntax error score of '" + syntaxErrorScore + "'");
    }

    private static void calculateAutocompleteToolScore(List<Chunk> chunks) {
        var incompleteChunks = chunks.stream().filter(chunk -> chunk.status == LineStatus.INCOMPLETE).collect(Collectors.toList());

        var autocompleteToolScores = new ArrayList<Long>();
        for (var incompleteChunk : incompleteChunks) {
            var autocompleteToolScore = 0L;
            var autocompleteString = incompleteChunk.autocomplete();
            for (var character : autocompleteString.split("")) {
                autocompleteToolScore = (autocompleteToolScore * 5L) + autocompleteScore.get(character);
            }
            autocompleteToolScores.add(autocompleteToolScore);
        }

        autocompleteToolScores.sort(Comparator.naturalOrder());

        System.out.println("The middle score of all autocomplete scores is '" + autocompleteToolScores.get(autocompleteToolScores.size() / 2) + "'");
    }

    public static void main(String[] args) {
        var chunks = getLineStream().map(Chunk::parse).toList();

        calculateSyntaxErrorScore(chunks);
        calculateAutocompleteToolScore(chunks);
    }

}

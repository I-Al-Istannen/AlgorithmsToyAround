package me.ialistannen.algorithms.math.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.util.Pair;

public class MatrixCombinationsLaAssignmentFour {

  public static void main(String[] args) {
    generatePossibilities(7).stream()
        .map(integer -> {
          List<Integer> sizes = isolateBlockSizes(7, integer);
          sizes.sort(Comparator.reverseOrder());
          return sizes;
        })
        .distinct()
        .forEach(System.out::println);

    IntStream.rangeClosed(1, 20)
        .mapToObj(value -> new Pair<>(value, generatePossibilities(value)))
        .map(pair -> new Pair<>(
            pair.getKey(),
            pair.getValue().stream()
                .map(it -> {
                  List<Integer> blocks = isolateBlockSizes(pair.getKey(), it);
                  blocks.sort(Comparator.reverseOrder());
                  return blocks;
                })
                .distinct()
                .collect(Collectors.toList())
        ))
        .forEach(pair -> System.out.println(pair.getKey() + ": " + pair.getValue().size()));
  }

  private static List<Integer> isolateBlockSizes(int width, int input) {
    List<Integer> sizes = new ArrayList<>();

    int sequenceLength = 0;
    for (int i = 0; i < width; i++) {
      if (isBitSet(i, input)) {
        sequenceLength++;
      } else {
        if (sequenceLength > 0) {
          sizes.add(sequenceLength + 1);
        } else {
          sizes.add(1);
        }
        sequenceLength = 0;
      }
    }

    // we read from right to left
    Collections.reverse(sizes);

    return sizes;
  }

  private static List<Integer> generatePossibilities(int width) {
    return IntStream.range(0, (int) Math.pow(2, width - 1))
        .boxed()
        .collect(Collectors.toList());
  }

  private static boolean isBitSet(int pos, int input) {
    return (input & (1 << pos)) != 0;
  }
}

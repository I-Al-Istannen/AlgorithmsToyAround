package me.ialistannen.algorithms.sorts;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SortTester {

  public static <T extends Comparable<T>> void sortAndPrint(List<T> list, Sort sort) {
    System.out.println("===== PRE SORT ====");
    System.out.println(list);

    sort.sort(list);

    System.out.println("==== POST SORT ====");
    System.out.println(list);
    System.out.println();
  }

  public static void sortAndPrint(Sort sort) {
    sortAndPrint(Arrays.asList(7, 2, 8, 5, 3), sort);
  }

  public static <T extends Comparable<T>> void sortAndTime(List<T> list, Sort sort) {
    long start = System.currentTimeMillis();

    sort.sort(list);

    long delta = System.currentTimeMillis() - start;

    System.out.println("Delta ms: " + delta + " (" + TimeUnit.MILLISECONDS.toSeconds(delta) + "s)");

    for (int i = 0; i < list.size() - 1; i++) {
      if (list.get(i).compareTo(list.get(i + 1)) > 0) {
        System.out.println("Not correctly sorted!");
        return;
      }
    }
  }

  public static void sortAndTimeAHundredThousandInts(Sort sort) {
    List<Integer> data = IntStream.range(0, 100_000)
        .boxed()
        .collect(Collectors.toList());
    Collections.shuffle(data);
    sortAndTime(data, sort);
  }
}

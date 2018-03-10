package me.ialistannen.algorithms.sorts;

import java.util.List;

@FunctionalInterface
public interface Sort {

  <T extends Comparable<T>> void sort(List<T> input);
}

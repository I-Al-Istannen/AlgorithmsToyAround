package me.ialistannen.algorithms.sorts;

import java.util.List;
import me.ialistannen.collections.heap.BinaryHeap;

public class HeapSort implements Sort {

  @Override
  public <T extends Comparable<T>> void sort(List<T> input) {
    BinaryHeap<T> heap = new BinaryHeap<>(input.size());
    for (T t : input) {
      heap.add(t);
    }

    for (int i = input.size() - 1; i >= 0; i--) {
      input.set(i, heap.pop());
    }
  }

  public static void main(String[] args) {
    SortTester.sortAndTimeAHundredThousandInts(new HeapSort());
  }
}

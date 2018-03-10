package me.ialistannen.algorithms.sorts;

import java.util.List;

public class BubbleSort implements Sort {

  @Override
  public <T extends Comparable<T>> void sort(List<T> input) {
    int lastElement = input.size();
    int firstElement = 0;

    while (lastElement != 0) {
      for (int i = firstElement; i < lastElement - 1; i++) {
        compareAndSwap(i, i + 1, input);
      }
      lastElement--;
      for (int i = lastElement; i > 0; i--) {
        compareAndSwap(i - 1, i, input);
      }
      firstElement++;
    }
  }

  private <T extends Comparable<T>> void compareAndSwap(int leftIndex, int rightIndex,
      List<T> list) {
    T left = list.get(leftIndex);
    T right = list.get(rightIndex);

    boolean breaksOrder = left.compareTo(right) > 0;

    if (breaksOrder) {
      list.set(leftIndex, right);
      list.set(rightIndex, left);
    }
  }

  public static void main(String[] args) {
    SortTester.sortAndPrint(new BubbleSort());
    SortTester.sortAndTimeAHundredThousandInts(new BubbleSort());
  }
}

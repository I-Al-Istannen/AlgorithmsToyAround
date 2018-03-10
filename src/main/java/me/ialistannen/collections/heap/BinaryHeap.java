package me.ialistannen.collections.heap;

import java.util.Arrays;

/**
 * A heap expressed through a binary tree, in place.
 */
public class BinaryHeap<T extends Comparable<T>> {

  private T[] data;

  private int size;

  @SuppressWarnings("unchecked")
  public BinaryHeap(int initialSize) {
    data = (T[]) new Comparable[initialSize];
  }

  public void add(T t) {
    upHeap(t);

    size++;
  }

  private void upHeap(T t) {
    ensureCapacity();

    data[size] = t;
    heapify(size);
  }

  private void ensureCapacity() {
    int newSize = size * 2;
    if (data.length > size) {
      return;
    }

    data = Arrays.copyOf(data, newSize);
  }

  private void heapify(int index) {
    if (index == 0) {
      return;
    }

    int parentIndex = getParentIndex(index);

    T element = data[index];
    T parent = data[parentIndex];

    if (!isSmaller(parent, element)) {
      return;
    }

    data[index] = parent;
    data[parentIndex] = element;

    heapify(parentIndex);
  }

  public T pop() {
    return downHeap();
  }

  private T downHeap() {
    if (size == 0) {
      return null;
    }

    T root = data[0];
    T last = data[size - 1];

    data[size - 1] = null;
    data[0] = last;

    size--;

    downHeapify(0);

    return root;
  }

  private void downHeapify(int parentIndex) {
    if (!hasChildren(parentIndex)) {
      return;
    }

    int leftChildIndex = getLeftChildIndex(parentIndex);
    int rightChildIndex = getRightChildIndex(parentIndex);

    T parent = data[parentIndex];
    T left = data[leftChildIndex];
    T right = data[rightChildIndex];

    int swapIndex;

    if (right == null) {
      swapIndex = leftChildIndex;
    } else {
      swapIndex = isSmaller(left, right) ? rightChildIndex : leftChildIndex;
    }

    if (!isSmaller(parent, data[swapIndex])) {
      return;
    }

    data[parentIndex] = data[swapIndex];
    data[swapIndex] = parent;

    downHeapify(swapIndex);
  }

  private boolean isSmaller(T one, T two) {
    return one.compareTo(two) < 0;
  }

  private int getParentIndex(int nodeIndex) {
    return Math.floorDiv(nodeIndex - 1, 2);
  }

  private int getLeftChildIndex(int nodeIndex) {
    return nodeIndex * 2 + 1;
  }

  private int getRightChildIndex(int nodeIndex) {
    return nodeIndex * 2 + 2;
  }

  private boolean hasChildren(int index) {
    return getRightChildIndex(index) < size || getLeftChildIndex(index) < size;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < size; i++) {

      if (hasChildren(i)) {
        builder.append(data[i]);
      }

      if (getLeftChildIndex(i) < size) {
        builder
            .append(":  ")
            .append(data[getLeftChildIndex(i)]);
      }
      if (getRightChildIndex(i) < size) {
        builder
            .append(" - ")
            .append(data[getRightChildIndex(i)]);
      }

      if (hasChildren(i)) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }

  public static void main(String[] args) {
    BinaryHeap<Integer> heap = new BinaryHeap<>(5);
    heap.add(1);
    heap.add(3);
    heap.add(2);
    heap.add(4);
    heap.add(5);
    heap.add(4);
    heap.add(4);
    heap.add(7);
    System.out.println(heap);

    while (heap.size > 0) {
      System.out.println(heap.pop());
    }
  }
}

package me.ialistannen.collections.heap;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple heap (realized with a backing array) with a variable amount of child nodes.
 *
 * <p><br><em>Nulls are <strong>not</strong> permitted!</em></p>
 *
 * @param <E> the type of the elements
 */
public class NChildHeap<E extends Comparable<E>> extends AbstractQueue<E> {

  private E[] underlying;
  private int elementCount;
  private int childCount;

  /**
   * Creates a new heap.
   *
   * @param childCount the amount of children per node
   */
  public NChildHeap(int childCount) {
    this.childCount = childCount;

    @SuppressWarnings("unchecked")
    E[] underlying = (E[]) new Comparable[16];
    this.underlying = underlying;
  }

  private void ensureSize() {
    if (elementCount >= underlying.length) {
      underlying = Arrays.copyOf(underlying, underlying.length * 2);
    } else if (elementCount < underlying.length / 2) {
      underlying = Arrays.copyOf(underlying, underlying.length / 2);
    }
  }

  @Override
  public Iterator<E> iterator() {
    return Arrays.stream(underlying).limit(elementCount).iterator();
  }

  @Override
  public int size() {
    return elementCount;
  }

  @Override
  public boolean offer(E e) {
    Objects.requireNonNull(e, "e can not be null!");

    ensureSize();
    underlying[elementCount++] = e;
    siftUp(elementCount - 1);
    return true;
  }

  private void siftUp(int start) {
    int parentIndex = start / childCount;

    // parent <= start ==> All is well
    if (underlying[parentIndex].compareTo(underlying[start]) <= 0) {
      return;
    }

    swap(start, parentIndex);

    siftUp(parentIndex);
  }

  @Override
  public E poll() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    E min = underlying[0];

    underlying[0] = underlying[elementCount - 1];
    underlying[elementCount - 1] = null;

    elementCount--;

    siftDown(0);

    return min;
  }

  @Override
  public E peek() {
    return underlying[0];
  }

  /**
   * Returns a copy of the underlying array.
   *
   * @param clazz the type of elements you want back
   * @return a copy of the underlying array.
   */
  public E[] getUnderlying(Class<E[]> clazz) {
    return Arrays.copyOf(underlying, underlying.length, clazz);
  }

  private void siftDown(int start) {
    List<IndexedElement<E>> children = children(start);
    Optional<IndexedElement<E>> smallestOpt = children.stream()
        .min(Comparator.comparing(IndexedElement::getElement));

    if (!smallestOpt.isPresent()) {
      // No children
      return;
    }

    E me = underlying[start];

    IndexedElement<E> smallest = smallestOpt.get();

    // Already correct
    if (smallest.element.compareTo(me) >= 0) {
      return;
    }

    // swap it, it's wrong
    swap(start, smallest.index);

    // go down
    siftDown(smallest.index);
  }

  private void swap(int first, int second) {
    E tmp = underlying[first];
    underlying[first] = underlying[second];
    underlying[second] = tmp;
  }

  private List<IndexedElement<E>> children(int parentIndex) {
    List<IndexedElement<E>> result = new ArrayList<>();
    int firstChildIndex = childCount * parentIndex + 1;

    for (int i = 0; i < childCount; i++) {
      int childIndex = firstChildIndex + i;
      if (childIndex >= elementCount) {
        break;
      }

      result.add(new IndexedElement<>(underlying[childIndex], childIndex));
    }
    return result;
  }

  private static class IndexedElement<E> {

    private E element;
    private int index;

    private IndexedElement(E element, int index) {
      this.element = element;
      this.index = index;
    }

    E getElement() {
      return element;
    }
  }

  public static void main(String[] args) {
    NChildHeap<Integer> heap = new NChildHeap<>(3);
    heap.offer(1);
    heap.offer(3);
    heap.offer(6);
    heap.offer(4);
    heap.offer(9);
    heap.offer(8);
    heap.offer(7);
    System.out.println(heap);

    List<Integer> result = Stream.generate(heap::poll)
        .limit(heap.size())
        .collect(Collectors.toList());
    System.out.println(result);
    System.out.println(heap);
  }
}

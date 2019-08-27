package me.ialistannen.collections.list;

import java.util.StringJoiner;

/**
 * A doubly linked list.
 *
 * @param <E> the type of the element
 */
public class DummyDoublyLinked<E> {

  private ListNode<E> head;
  private ListNode<E> tail;

  /**
   * Creates a new doubly linked list.
   */
  public DummyDoublyLinked() {
    this.head = this.tail = new ListNode<>(null);
  }

  /**
   * Returns a given index.
   *
   * @param index the index
   * @return the value at this index
   * @throws IndexOutOfBoundsException if the index is too large
   */
  public E get(int index) {
    ListNode<E> current = head.next;
    for (int i = 0; i < index; i++) {
      current = current.next;
      if (current.value == null) {
        throw new IndexOutOfBoundsException("Index out of bounds " + index);
      }
    }
    return current.value;
  }

  /**
   * Checks whether this list contains the given element.
   *
   * @param element the element
   * @return true if the element is contained in the list
   */
  public boolean contains(E element) {
    if (element == null) {
      return false;
    }
    head.value = element;

    ListNode<E> current = head.next;

    while (!current.value.equals(element)) {
      current = current.next;
    }

    head.value = null;

    return head != current;
  }

  /**
   * Appends a value.
   *
   * @param value the value to append
   */
  public void append(E value) {
    tail.next = new ListNode<>(value, tail, head);
    tail = tail.next;
    head.prev = tail;
  }

  /**
   * Returns the size of the list.
   *
   * @return the size of the list
   */
  public int size() {
    ListNode<E> current = head.next;
    int size = 0;

    while (current != head) {
      current = current.next;
      size++;
    }

    return size;
  }

  /**
   * Splices the list from first to last behind target.
   *
   * <p><br>{@code <first, ..., last> | <..., target, next, ...>}
   * <br>to
   * <br> {@code <..., target, first, ..., last, next, ...}</p>
   *
   * @param first the first node
   * @param last the last node
   * @param target the target node to splice them behind
   */
  public void splice(ListNode<E> first, ListNode<E> last, ListNode<E> target) {
    ListNode<E> beforeFirst = first.prev;
    ListNode<E> afterLast = last.next;

    ListNode<E> afterTarget = target.next;

    // Fix original list
    beforeFirst.next = afterLast;
    afterLast.prev = beforeFirst;

    target.next = first;
    first.prev = target;

    last.next = afterTarget;
    afterTarget.prev = last;
  }

  @Override
  public String toString() {
    StringJoiner result = new StringJoiner(", ", "[", "]");

    int size = size();
    for (int i = 0; i < size; i++) {
      result.add(String.valueOf(get(i)));
    }

    return result.toString();
  }

  private static class ListNode<E> {

    private E value;
    private ListNode<E> prev;
    private ListNode<E> next;

    ListNode(E value) {
      this.value = value;
      this.next = this;
      this.prev = this;
    }

    ListNode(E value, ListNode<E> prev, ListNode<E> next) {
      this.value = value;
      this.prev = prev;
      this.next = next;
    }
  }

  public static void main(String[] args) {
    DummyDoublyLinked<String> list = new DummyDoublyLinked<>();
    System.out.println(list.size());
    list.append("Hey");
    System.out.println(list.size());
    list.append("You");
    System.out.println(list.size());

    System.out.println();
    System.out.println(list);

    System.out.println(list.contains("Hey"));
    System.out.println(list.contains("You"));
    System.out.println(list.contains("Whatever"));
    System.out.println(list.contains(null));

    ListNode<String> node = new ListNode<>("AAAH");

    list.splice(node, node, list.head);
    System.out.println(list);
  }
}

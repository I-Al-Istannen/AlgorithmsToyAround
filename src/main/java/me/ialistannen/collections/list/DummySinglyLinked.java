package me.ialistannen.collections.list;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.Map;

public class DummySinglyLinked<E> extends AbstractList<E> {

  private Node dummy = new Node();

  @Override
  public E get(int index) {
    Node current = dummy.next;

    for (int i = 0; i < index; i++) {
      current = current.next;
      if (current == dummy) {
        throw new IndexOutOfBoundsException("Got'cha dummy!");
      }
    }
    return current.value;
  }

  @Override
  public boolean add(E e) {
    Node newNode = new Node(e);
    Node last = findLast();
    last.next = newNode;
    newNode.next = dummy;
    return true;
  }

  private Node findLast() {
    if (dummy.next == dummy) {
      return dummy;
    }
    Node current = dummy;

    while (current.next != dummy) {
      current = current.next;
    }
    return current;
  }

  @Override
  public int size() {
    int count = 0;
    Node current = dummy.next;

    while (current != dummy) {
      count++;
      current = current.next;
    }

    return count;
  }

  public void reverse() {
    Node prev = dummy;
    Node current = dummy.next;

    do {
      Node next = current.next;

      current.next = prev;
      prev = current;
      current = next;
    } while (prev != dummy);
  }

  /**
   * Finds the position of a cycle in the list.
   *
   * @return the index of the erroneous node or -1 if the list is valid. Does not count the dummy
   *     element.
   */
  private int findCycle() {
    int count = 0;

    Node harePointer = dummy;
    Node tortoisePointer = dummy;

    do {
      if (harePointer.next == dummy || harePointer.next.next == dummy) {
        return -1;
      }
      harePointer = harePointer.next.next;
      tortoisePointer = tortoisePointer.next;
    } while (harePointer != tortoisePointer);

    harePointer = dummy;

    while (tortoisePointer != harePointer) {
      tortoisePointer = tortoisePointer.next;
      harePointer = harePointer.next;
      count++;
    }

    return count;
  }

  public static DummySinglyLinked<Integer> withLinks(int... ints) {
    DummySinglyLinked<Integer> list = new DummySinglyLinked<>();
    Map<Integer, DummySinglyLinked.Node> nodes = new HashMap<>();

    DummySinglyLinked.Node first = null;
    DummySinglyLinked.Node last = null;
    for (int i : ints) {
      DummySinglyLinked.Node node = nodes.computeIfAbsent(i, integer -> list.new Node(integer));

      if (last != null) {
        last.next = node;
      }
      if (first == null) {
        first = last;
      }
      last = node;
    }

    list.dummy = first;
    return list;
  }

  private class Node {

    private E value;
    private Node next;

    private Node() {
      this(null);
    }

    private Node(E value) {
      this.value = value;
      this.next = this;
    }

    @Override
    public String toString() {
      String nextStr;
      if (next == dummy) {
        nextStr = "dummy";
      } else if (next == this) {
        nextStr = "this";
      } else {
        nextStr = "" + next;
      }
      return "Node{" +
          "value=" + value +
          ", next=" + nextStr +
          '}';
    }
  }

  public static void main(String[] args) {
    DummySinglyLinked<String> list = new DummySinglyLinked<>();
    list.add("Hey");
    list.add("yOu");
    list.add("mate");
    System.out.println(list);
    System.out.println(list.dummy);
    list.reverse();
    System.out.println();
    System.out.println(list);
    System.out.println(list.dummy);
    System.out.println();
    DummySinglyLinked<Integer> links = withLinks(-1, 1, 2, 3, 4, 5, 6, 10, -1);
    System.out.println(links.findCycle());
  }
}

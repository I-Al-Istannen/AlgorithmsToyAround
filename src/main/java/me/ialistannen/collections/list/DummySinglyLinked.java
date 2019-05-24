package me.ialistannen.collections.list;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    int collisionPosition = executeFloyds(dummy);

    if (collisionPosition < 0) {
      return collisionPosition;
    }
    System.out.println(
        "Collision was at " + collisionPosition
            + " (node " + getNode(collisionPosition).value + ")"
    );

    int errorPos = collisionPosition;
    Node current = getNode(errorPos);
    int cycleLength = getCycleLength(current);

    System.out.println("Cycle length is: " + cycleLength);

    while (reachesSelf(current, cycleLength)) {
      System.out.println("I could reach myself from " + current.value);
      errorPos--;
      current = getNode(errorPos);
    }

    System.out.println("Error is at value: " + current.next.value);

    // does not count the dummy element
    return errorPos;
  }

  private int getCycleLength(Node start) {
    Node current = start.next;

    int count = 0;
    while (current != start) {
      count++;
      current = current.next;
    }
    return count + 1;
  }

  private boolean reachesSelf(Node start, int distance) {
    Node current = start;
    for (int i = 0; i < distance; i++) {
      current = current.next;
      if (current == start) {
        return true;
      }
    }
    return false;
  }

  private Node getNode(int distance) {
    Node current = dummy;
    for (int i = 0; i < distance; i++) {
      current = current.next;
    }
    return current;
  }

  private int executeFloyds(Node start) {
    Function<Node, Node> hare = node -> node.next.next;
    Function<Node, Node> tortoise = node -> node.next;

    int count = 1;
    Node harePointer = hare.apply(start);
    Node tortoisePointer = tortoise.apply(start);
    for (int i = 0; i < 100; i++) {
      if (harePointer == tortoisePointer) {
        break;
      }
      harePointer = hare.apply(harePointer);
      tortoisePointer = tortoise.apply(tortoisePointer);
      count++;
    }
    if (harePointer == dummy) {
      return -1;
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
    DummySinglyLinked<Integer> links = withLinks(-1, 1, 2, 3, 4, 5, 6, 7, -1);
//    System.out.println(links);
    System.out.println(links.findCycle());
  }
}

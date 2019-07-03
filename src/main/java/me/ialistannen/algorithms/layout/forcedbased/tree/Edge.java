package me.ialistannen.algorithms.layout.forcedbased.tree;

import java.util.Objects;

public class Edge<T> {

  private Node<T> start;
  private Node<T> end;
  private boolean bidirectional;

  public Edge(Node<T> start, Node<T> end, boolean bidirectional) {
    this.start = start;
    this.end = end;
    this.bidirectional = bidirectional;
  }

  public Node<T> getStart() {
    return start;
  }

  public Node<T> getEnd() {
    return end;
  }

  public boolean isBidirectional() {
    return bidirectional;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Edge<?> edge = (Edge<?>) o;
    return bidirectional == edge.bidirectional &&
        Objects.equals(start, edge.start) &&
        Objects.equals(end, edge.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, bidirectional);
  }
}

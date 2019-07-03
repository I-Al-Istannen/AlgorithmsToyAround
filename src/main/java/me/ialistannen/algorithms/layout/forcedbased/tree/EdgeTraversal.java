package me.ialistannen.algorithms.layout.forcedbased.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Traverses all {@link Edge}s in a collection of nodes.
 */
public class EdgeTraversal {

  /**
   * Reurns all edges in a collection of nodes.
   *
   * @param nodes the nodes
   * @param <T> the type of the nodes
   * @return all edges
   */
  public <T> Collection<Edge<T>> getEdges(Collection<Node<T>> nodes) {
    Set<Edge<T>> visited = new HashSet<>();

    for (Node<T> first : nodes) {
      for (Node<T> second : first.getNeighbours()) {
        Edge<T> edge = new Edge<>(first, second, second.isConnected(first));
        if (visited.contains(edge)) {
          continue;
        }
        visited.add(edge);
      }
    }

    return visited;
  }
}

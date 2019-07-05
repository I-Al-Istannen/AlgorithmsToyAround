package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.List;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A graph traversal.
 */
public interface Traversal {

  /**
   * Executes this traversal.
   *
   * @param nodes all nodes
   * @param <T> the type of the node
   * @return a list with all node change actions
   */
  <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes);
}

package me.ialistannen.algorithms.layout.forcedbased;

import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * Some actor that applies a force to some nodes.
 */
public interface ForceActor {

  /**
   * Applies some kind of force between the two nodes, if needed.
   *
   * @param a the first node
   * @param b the second node
   */
  void apply(Node a, Node b);
}

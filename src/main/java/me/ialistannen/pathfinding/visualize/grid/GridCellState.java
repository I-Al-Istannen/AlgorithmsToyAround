package me.ialistannen.pathfinding.visualize.grid;

import javafx.scene.Node;

public interface GridCellState {

  /**
   * Returns a node to use for this state. Do not cache the node, always create new one.
   *
   * @return the created node
   */
  Node getNode();

  /**
   * @return true if this state is an end state
   */
  boolean isEnd();

  /**
   * @return true if this state is a start state
   */
  boolean isStart();

  /**
   * Marks if you can walk through this state
   *
   * @return true if you can walk through this node.
   */
  boolean isPassable();
}

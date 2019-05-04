package me.ialistannen.treeviewer.model;

import java.util.List;
import me.ialistannen.algorithms.math.parser.token.Token;

/**
 * A simple tree
 */
public interface Tree {

  /**
   * @return The parent node. May be null
   */
  Tree getParent();

  /**
   * @return The children of the tree
   */
  List<? extends Tree> getChildren();

  /**
   * @return The {@link Token}
   */
  Token getToken();

  /**
   * Evaluates this node.
   *
   * @return The value of this node.
   */
  double evaluate();
}

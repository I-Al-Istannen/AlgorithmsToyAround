package me.ialistannen.algorithms.math.parser.token;

import me.ialistannen.treeviewer.Tree;

/**
 * A simple token. Empty super class of value and operator tokens.
 */
public interface Token {

  /**
   * @return The token text
   */
  String getTokenText();

  /**
   * @return The {@link TokenType}
   */
  TokenType getType();

  /**
   * Evaluates this {@link Token}'s value using the passed node.
   *
   * @param node The node this {@link Token} belongs to
   * @return The node's value
   */
  double evaluate(Tree node);
}

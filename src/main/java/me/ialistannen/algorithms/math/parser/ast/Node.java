package me.ialistannen.algorithms.math.parser.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.ialistannen.algorithms.math.parser.token.Token;
import me.ialistannen.treeviewer.model.Tree;

/**
 * A node containing a {@link Token}.
 */
public class Node implements Tree {

  private Token token;
  private List<Node> children;
  private Node parent;

  /**
   * @param token The {@link Token} to use for this {@link Node}
   */
  public Node(Token token) {
    this.token = token;
    this.children = new ArrayList<>();
  }

  /**
   * @return The {@link Token}
   */
  public Token getToken() {
    return token;
  }

  /**
   * @return The children. Unmodifiable.
   */
  public List<Node> getChildren() {
    return Collections.unmodifiableList(children);
  }

  /**
   * @param node The child to add
   */
  public void addChild(Node node) {
    children.add(node);
    node.parent = this;
  }

  /**
   * Adds all nodes.
   *
   * @param nodes The nodes to add
   * @see #addChild(Node)
   */
  public void addChildren(Iterable<Node> nodes) {
    nodes.forEach(this::addChild);
  }

  /**
   * @return The parent node or null if none.
   */
  public Node getParent() {
    return parent;
  }

  @Override
  public double evaluate() {
    return getToken().evaluate(this);
  }

  @Override
  public String toString() {
    return "Node{"
        + "token=" + token
        + ", children=" + children
        + '}';
  }
}

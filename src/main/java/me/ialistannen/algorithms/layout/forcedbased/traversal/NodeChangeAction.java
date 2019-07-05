package me.ialistannen.algorithms.layout.forcedbased.traversal;

import me.ialistannen.algorithms.layout.forcedbased.tree.Node;
import me.ialistannen.algorithms.layout.forcedbased.view.NodeCircle;

/**
 * Represents a change to a {@link Node}s text or content.
 *
 * @param <T> the type of the node
 */
public class NodeChangeAction<T> {

  private Node<T> node;
  private String leftText;
  private String rightText;
  private String title;
  private boolean highlighted;

  private NodeChangeAction(Node<T> node, String leftText, String rightText, String title,
      boolean highlighted) {
    this.node = node;
    this.leftText = leftText;
    this.rightText = rightText;
    this.title = title;
    this.highlighted = highlighted;
  }

  /**
   * Returns the affected node.
   *
   * @return the affected node.
   */
  public Node<T> getNode() {
    return node;
  }

  /**
   * Applies the action to the given circle.
   *
   * @param circle the circle to apply it to
   */
  public void apply(NodeCircle<T> circle) {
    if (leftText != null) {
      circle.setLeftText(leftText);
    }
    if (rightText != null) {
      circle.setRightText(rightText);
    }
    if (title != null) {
      circle.setText(title);
    }
    circle.setHighlight(highlighted);
  }

  /**
   * Creates a new builder.
   *
   * @param node the target node
   * @param <T> the type of the node
   * @return the builder
   */
  public static <T> Builder<T> builder(Node<T> node) {
    return new Builder<>(node);
  }

  /**
   * A builder for {@link NodeChangeAction}s.
   *
   * @param <T> the type of the node
   */
  public static class Builder<T> {

    private String leftText;
    private String rightText;
    private String title;
    private Node<T> node;
    private boolean highlighted;

    private Builder(Node<T> node) {
      this.node = node;
    }

    /**
     * Sets the left text.
     *
     * @param text the text
     * @return this builder
     */
    public Builder<T> withLeftText(String text) {
      this.leftText = text;
      return this;
    }

    /**
     * Sets the right text.
     *
     * @param text the text
     * @return this builder
     */
    public Builder<T> withRightText(String text) {
      this.rightText = text;
      return this;
    }

    /**
     * Sets the title text.
     *
     * @param text the text
     * @return this builder
     */
    public Builder<T> withTitle(String text) {
      this.title = text;
      return this;
    }

    /**
     * Sets the highlight status.
     *
     * @param highlight the highlight status
     * @return this builder
     */
    public Builder<T> withHighlight(boolean highlight) {
      this.highlighted = highlight;
      return this;
    }

    /**
     * Builds this change.
     *
     * @return the resulting {@link NodeChangeAction}
     */
    public NodeChangeAction<T> build() {
      return new NodeChangeAction<>(node, leftText, rightText, title, highlighted);
    }
  }
}

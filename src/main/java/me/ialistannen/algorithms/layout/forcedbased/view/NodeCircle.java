package me.ialistannen.algorithms.layout.forcedbased.view;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A simple model to display a node.
 *
 * @param <T> the type of the stored value
 */
public class NodeCircle<T> extends StackPane {

  private final Node<T> node;

  /**
   * Creates a new node circle for the given node.
   *
   * @param node the node to create the circle for
   */
  public NodeCircle(Node<T> node) {
    this.node = node;

    Circle circle = new Circle(getRadius() * 2, Color.ROYALBLUE);
    Label label = new Label(node.getValue().toString());
    label.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    label.setTextFill(Color.WHITE);

    circle.radiusProperty().bind(label.widthProperty());

    this.getChildren().add(circle);
    this.getChildren().add(label);

    update();
  }

  /**
   * Updates this circle to reflect the new node position.
   */
  public void update() {
    Vector2D position = node.getPosition();
    translateXProperty().set(position.getX());
    translateYProperty().set(position.getY());
  }

  public double getCenterX() {
    return getTranslateX() + getWidth() / 2;
  }

  public double getCenterY() {
    return getTranslateY() + getHeight() / 2;
  }

  /**
   * Returns this node's radius.
   *
   * @return the radius of this node
   */
  public double getRadius() {
    return 10;
  }

  /**
   * Returns the wrapped node.
   *
   * @return the wrapped node
   */
  public Node<T> getNode() {
    return node;
  }
}

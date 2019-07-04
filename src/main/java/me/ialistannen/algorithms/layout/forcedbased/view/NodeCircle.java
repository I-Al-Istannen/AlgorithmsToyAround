package me.ialistannen.algorithms.layout.forcedbased.view;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
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
  private final Label label;

  private DoubleProperty radiusProperty;

  /**
   * Creates a new node circle for the given node.
   *
   * @param node the node to create the circle for
   */
  public NodeCircle(Node<T> node) {
    this.node = node;
    this.radiusProperty = new SimpleDoubleProperty();

    getStylesheets().add("/css/nodelayout/NodeCircle.css");

    label = new Label(node.getValue().toString());
    label.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    label.setTextFill(Color.WHITE);
    StackPane.setAlignment(label, Pos.CENTER);

    Circle circle = new Circle(label.getWidth(), Color.ROYALBLUE);
    circle.radiusProperty().bind(label.widthProperty());

    this.getChildren().add(circle);
    this.getChildren().add(label);

    setMaxWidth(Double.MAX_VALUE);
    setMaxHeight(Double.MAX_VALUE);

    circle.radiusProperty().addListener((observable, oldValue, newValue) -> {
      setPrefWidth(newValue.doubleValue() * 2);
      setPrefHeight(newValue.doubleValue() * 2);
      requestLayout();
      Platform.runLater(() -> radiusProperty.setValue(newValue));
    });

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

  /**
   * Returns the x coordinate of the center.
   *
   * @return the x coordinate of the center.
   */
  public double getCenterX() {
    return getTranslateX() + getWidth() / 2;
  }

  /**
   * Returns the y coordinate of the center.
   *
   * @return the y coordinate of the center.
   */
  public double getCenterY() {
    return getTranslateY() + getHeight() / 2;
  }

  /**
   * Returns the radius.
   *
   * @return the radius
   */
  public ReadOnlyDoubleProperty radiusProperty() {
    return radiusProperty;
  }

  /**
   * Returns this node's radius.
   *
   * @return the radius of this node
   * @see #radiusProperty()
   */
  public double getRadius() {
    return radiusProperty.get();
  }

  /**
   * Returns the wrapped node.
   *
   * @return the wrapped node
   */
  public Node<T> getNode() {
    return node;
  }

  /**
   * Sets the displayed text of this node.
   *
   * @param text the text to display
   */
  public void setText(String text) {
    label.setText(text);
  }

  public void setHighlight(boolean highlight) {
    getStyleClass().remove("highlighted-node");

    if (highlight) {
      getStyleClass().add("highlighted-node");
    }
  }
}

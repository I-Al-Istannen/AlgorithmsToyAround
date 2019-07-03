package me.ialistannen.algorithms.layout.forcedbased.view;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 * A line connecting two nodes.
 *
 * @param <T> the type of the nodes
 */
public class ConnectionLine<T> extends Pane {

  /**
   * Creates a new connection line.
   *
   * @param start the start point
   * @param end the end point
   */
  public ConnectionLine(NodeCircle<T> start, NodeCircle<T> end) {

    Line line = new Line();

    line.startXProperty().bind(start.centerXProperty());
    line.startYProperty().bind(start.centerYProperty());

    line.endXProperty().bind(end.centerXProperty());
    line.endYProperty().bind(end.centerYProperty());

    getChildren().add(line);
  }
}

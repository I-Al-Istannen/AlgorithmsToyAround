package me.ialistannen.algorithms.layout.forcedbased.view;

import java.text.DecimalFormat;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;
import me.ialistannen.algorithms.layout.forcedbased.tree.Edge;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;
import me.ialistannen.algorithms.layout.forcedbased.view.util.EditableLabel;
import org.jetbrains.annotations.NotNull;

/**
 * A line connecting two nodes.
 *
 * @param <T> the type of the nodes
 */
public class ConnectionLine<T> extends AnchorPane {

  private final Line line;
  @NotNull
  private final NodeCircle<T> start;
  @NotNull
  private final NodeCircle<T> end;

  // NEVER MAKE THESE LOCAL VARIABLES. Due to some interesting design decisions in JavaFx,
  // the bindings **will be garbage collected while the nodes are still active**, if you do not
  // hold a strong reference to them yourself!
  @SuppressWarnings("FieldCanBeLocal")
  private ObjectBinding<Vector2D> startHeadBinding;
  @SuppressWarnings("FieldCanBeLocal")
  private ObjectBinding<Vector2D> endHeadBinding;

  private StringProperty edgeWeight;

  /**
   * Creates a new connection line.
   *
   * @param start the start point
   * @param end the end point
   * @param edge the edge this line models
   */
  public ConnectionLine(NodeCircle<T> start, NodeCircle<T> end, Edge<T> edge) {
    this.start = start;
    this.end = end;
    getStylesheets().add("/css/nodelayout/ConnectionLine.css");
    setPickOnBounds(false);

    this.line = new Line();
    this.edgeWeight = new SimpleStringProperty();

    ChangeListener<Object> changeListener = (observable, oldValue, newValue) ->
        updateStartEnd(start, end);
    start.translateXProperty().addListener(changeListener);
    start.translateYProperty().addListener(changeListener);
    end.translateXProperty().addListener(changeListener);
    end.translateYProperty().addListener(changeListener);
    start.widthProperty().addListener(changeListener);
    end.widthProperty().addListener(changeListener);

    getChildren().add(line);

    if (edge.getWeight() != 0) {
      EditableLabel weightLabel = new EditableLabel<>(
          input -> DecimalFormat.getNumberInstance().parse(input).doubleValue(),
          newWeight -> {
            start.getNode().alterEdgeWeight(edge, newWeight);
            edgeWeight.set(DecimalFormat.getNumberInstance().format(newWeight));
          }
      );
      edgeWeight.setValue(DecimalFormat.getNumberInstance().format(edge.getWeight()));
      weightLabel.textProperty().bind(edgeWeight);
      weightLabel.getStyleClass().add("weight-label");

      weightLabel.translateXProperty().bind(
          line.startXProperty().add(line.endXProperty()).divide(2)
      );
      weightLabel.translateYProperty().bind(
          line.startYProperty().add(line.endYProperty()).divide(2)
      );
      getChildren().add(weightLabel);
    }

    if (start.getNode().isConnected(end.getNode())) {
      startHeadBinding = getLineVector(1);
      ArrowHead arrowHead = new ArrowHead(startHeadBinding);
      getChildren().add(arrowHead);
      arrowHead.translateXProperty().bind(line.endXProperty());
      arrowHead.translateYProperty().bind(line.endYProperty());
    }

    if (end.getNode().isConnected(start.getNode())) {
      endHeadBinding = getLineVector(-1);
      ArrowHead arrowHead = new ArrowHead(endHeadBinding);
      getChildren().add(arrowHead);
      arrowHead.translateXProperty().bind(line.startXProperty());
      arrowHead.translateYProperty().bind(line.startYProperty());
    }
  }

  @NotNull
  private ObjectBinding<Vector2D> getLineVector(double scalar) {
    return Bindings.createObjectBinding(
        () -> new Vector2D(
            line.getEndX() - line.getStartX(),
            line.getEndY() - line.getStartY()
        ).multiply(scalar),
        line.startXProperty(), line.startYProperty(), line.endXProperty(), line.endYProperty()
    );
  }

  private void updateStartEnd(NodeCircle<T> start, NodeCircle<T> end) {
    // Retrieve bounds
    Bounds startBounds = start.localToParent(start.getLayoutBounds());
    Bounds endBounds = end.localToParent(end.getLayoutBounds());

    // Find the centers
    Vector2D startCenter = new Vector2D(
        (startBounds.getMinX() + startBounds.getMaxX()) / 2,
        (startBounds.getMinY() + startBounds.getMaxY()) / 2
    );
    Vector2D endCenter = new Vector2D(
        (endBounds.getMinX() + endBounds.getMaxX()) / 2,
        (endBounds.getMinY() + endBounds.getMaxY()) / 2
    );

    // calculate direction
    Vector2D direction = endCenter
        .subtract(startCenter)
        .normalize();

    // Escape the start node to accommodate weird shapes
    Vector2D current = startCenter;
    while (!direction.equals(Vector2D.ZERO) && startBounds.contains(current.toPoint2D())) {
      current = current.add(direction);
    }

    Vector2D startPoint = current;

    // Escape the end node to accommodate weird shapes
    current = endCenter;
    while (!direction.equals(Vector2D.ZERO) && endBounds.contains(current.toPoint2D())) {
      current = current.subtract(direction);
    }

    Vector2D endPoint = current;

    line.setStartX(startPoint.getX());
    line.setStartY(startPoint.getY());
    line.setEndX(endPoint.getX());
    line.setEndY(endPoint.getY());
  }

  /**
   * Returns whether the start or end point matches the given point.
   *
   * @param circle the point to check
   * @return true if the given point is the start or end point of this line
   */
  public boolean isEndOrStartFor(NodeCircle<T> circle) {
    return start.equals(circle) || end.equals(circle);
  }
  /**
   * Returns whether the start or end point matches the given point.
   *
   * @param node the node to check
   * @return true if the given point is the start or end point of this line
   */
  public boolean isEndOrStartFor(Node<T> node) {
    return start.getNode().equals(node) || end.getNode().equals(node);
  }
}

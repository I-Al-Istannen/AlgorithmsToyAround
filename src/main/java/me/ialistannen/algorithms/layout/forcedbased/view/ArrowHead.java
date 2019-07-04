package me.ialistannen.algorithms.layout.forcedbased.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;

/**
 * A simple arrow head.
 */
public class ArrowHead extends AnchorPane {

  // This must not be garbage collected. Praise the JavaFx overlords and leave it as a field!
  @SuppressWarnings("FieldCanBeLocal")
  private final ChangeListener<Vector2D> updateListener;
  private Line left;
  private Line right;

  /**
   * Creates a new arrow head following the given line.
   *
   * @param lineVector the line vector
   */
  public ArrowHead(ObservableValue<Vector2D> lineVector) {
    updateListener = (observable, oldValue, newValue) -> update(newValue);
    lineVector.addListener(updateListener);

    left = new Line(0, 0, 0, 0);
    right = new Line(0, 0, 0, 0);

    update(lineVector.getValue());

    getChildren().addAll(left, right);
  }

  private void update(Vector2D lineDir) {
    double size = 15;
    double angle = Math.atan2(lineDir.getY(), lineDir.getX());
    double leftAngle = angle - Math.PI / 8;
    double rightAngle = angle + Math.PI / 8;

    Vector2D leftStart = new Vector2D(
        Math.cos(leftAngle) * (size / 2),
        Math.sin(leftAngle) * (size / 2)
    )
        .subtract(lineDir.normalize().multiply(size));
    Vector2D rightStart = new Vector2D(
        Math.cos(rightAngle) * (size / 2),
        Math.sin(rightAngle) * (size / 2)
    )
        .subtract(lineDir.normalize().multiply(size));

    setStartToVec(left, leftStart);
    setStartToVec(right, rightStart);
  }

  private void setStartToVec(Line line, Vector2D vec) {
    line.setStartX(vec.getX());
    line.setStartY(vec.getY());
  }
}

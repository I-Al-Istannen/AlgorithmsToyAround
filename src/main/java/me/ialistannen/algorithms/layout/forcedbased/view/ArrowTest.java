package me.ialistannen.algorithms.layout.forcedbased.view;

import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;

public class ArrowTest extends AnchorPane {

  public ArrowTest(ObservableValue<Vector2D> lineVector) {
    lineVector.addListener((observable, oldValue, newValue) -> {
      setRotate(
          Math.toDegrees(Math.atan2(newValue.getY(), newValue.getX())) - 90
      );
      System.out.println(newValue);
      System.out.println(Math.toDegrees(Math.atan2(newValue.getY(), newValue.getX())));
    });
//    RotateTransition rotateTransition = new RotateTransition(Duration.seconds(5));
//    rotateTransition.setFromAngle(0);
//    rotateTransition.setToAngle(360);
//    rotateTransition.setCycleCount(Animation.INDEFINITE);
//    rotateTransition.setAutoReverse(true);
//
//    rotateTransition.setNode(this);
//    rotateTransition.play();

    int endY = 50;
    Line arrow = new Line(10, 0, 11, endY);
    arrow.setStroke(Color.TOMATO);
    Line leftFlap = new Line(5, 10, 11, 0);
    leftFlap.setStroke(Color.ROYALBLUE);
    Line rightFlap = new Line(15, 10, 11, 0);
    rightFlap.setStroke(Color.GREEN);

    getChildren().addAll(arrow, leftFlap, rightFlap);
  }
}

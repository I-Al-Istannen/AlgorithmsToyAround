package me.ialistannen.collections.quadtree.view;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.ialistannen.collections.quadtree.Point;
import me.ialistannen.collections.quadtree.QuadTree;
import me.ialistannen.collections.quadtree.Rectangle;

public class Viewer extends Application {

  private static final int POINT_SIZE = 10;
  private TreeRenderer treeRenderer;

  public Viewer() {
    this.treeRenderer = new TreeRenderer(Color.TOMATO, Color.ROYALBLUE, Color.RED, Color.DARKVIOLET,
        POINT_SIZE);
  }

  @Override
  public void start(Stage primaryStage) {
    Canvas canvas = new Canvas(1000, 1000);
    AtomicBoolean drawPoints = new AtomicBoolean(true);

    BorderPane pane = new BorderPane(canvas);
    BorderPane.setMargin(canvas, new Insets(20));

    QuadTree quadTree = new QuadTree(
        new Rectangle(
            (int) (canvas.getWidth() / 2),
            (int) canvas.getHeight() / 2,
            (int) (canvas.getWidth() / 2),
            (int) canvas.getHeight() / 2
        ),
        4
    );

    canvas.setOnMouseClicked(event -> {
      for (int i = 0; i < 5; i++) {
        int x = (int) (event.getX() + ThreadLocalRandom.current().nextInt(-25, 25));
        int y = (int) (event.getY() + ThreadLocalRandom.current().nextInt(-25, 25));

        quadTree.insert(new Point(x, y));

        treeRenderer.render(canvas, quadTree, drawPoints.get());
      }
    });
    canvas.setOnMouseMoved(event -> {
      if (!drawPoints.get()) {
        return;
      }
      Rectangle area = new Rectangle((int) event.getX(), (int) event.getY(), 100, 100);

      canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

      treeRenderer.render(canvas, quadTree);
      treeRenderer.highlightPoints(area, canvas, quadTree);
    });
    canvas.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.P) {
        drawPoints.set(!drawPoints.get());
      } else if (event.getCode() == KeyCode.C) {
        quadTree.clear();
      }
      canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      treeRenderer.render(canvas, quadTree, drawPoints.get());
    });

    treeRenderer.render(canvas, quadTree, drawPoints.get());

    primaryStage.setScene(new Scene(pane));
    primaryStage.sizeToScene();
    primaryStage.centerOnScreen();
    primaryStage.show();

    canvas.requestFocus();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

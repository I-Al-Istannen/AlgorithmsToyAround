package me.ialistannen.algorithms.layout.forcedbased;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.ialistannen.algorithms.layout.forcedbased.forces.BlackHoleAttractionForce;
import me.ialistannen.algorithms.layout.forcedbased.forces.ElectricalRepulsionForce;
import me.ialistannen.algorithms.layout.forcedbased.forces.SpringAttractionForce;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;
import me.ialistannen.algorithms.layout.forcedbased.view.GraphView;

public class Test extends Application {

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();

    List<Node<String>> nodes = getNodes(500, 500);
    GraphView<String> graphView = new GraphView<>(nodes);

    LayoutManager<String> layoutManager = new LayoutManager<>(
        nodes,
        Arrays.asList(
            new ElectricalRepulsionForce(1e5),
            new SpringAttractionForce(50, 0.1),
            new BlackHoleAttractionForce(new Vector2D(200, 200), 9.81e3)
        ),
        0.5
    );

    int delay = 50;
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delay), event -> {
      layoutManager.run();
      graphView.update();
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    root.setCenter(graphView);

    primaryStage.setScene(new Scene(root));
    primaryStage.setWidth(500);
    primaryStage.setHeight(500);
    primaryStage.centerOnScreen();
    primaryStage.show();

    primaryStage.getScene().setOnKeyPressed(event -> {
      if (event.getText().equals("R")) {
        randomizeLocation(400, 400, nodes);
      }
    });
  }

  private List<Node<String>> getNodes(int maxX, int maxY) {
    Node<String> a = new Node<>("H");
    Node<String> b = new Node<>("E");
    Node<String> c = new Node<>("Y");

    Node<String> d = new Node<>("Y");
    Node<String> e = new Node<>("O");
    Node<String> f = new Node<>("U");

    Node<String> g = new Node<>("Z");

    a.addConnection(b);
    a.addConnection(c);
    b.addConnection(c);

    d.addConnection(e);
    e.addConnection(f);

    g.addConnection(a);
    g.addConnection(f);

    List<Node<String>> nodes = Arrays.asList(a, b, c, d, e, f, g);
    randomizeLocation(maxX, maxY, nodes);
    return nodes;
  }

  private void randomizeLocation(int maxX, int maxY, List<Node<String>> nodes) {
    for (Node<String> node : nodes) {
      Vector2D newPos = new Vector2D(
          ThreadLocalRandom.current().nextDouble(maxX),
          ThreadLocalRandom.current().nextDouble(maxY)
      );
      node.setPosition(newPos);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

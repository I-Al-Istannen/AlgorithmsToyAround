package me.ialistannen.algorithms.layout.forcedbased;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
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
import me.ialistannen.algorithms.layout.forcedbased.traversal.DepthFirst;
import me.ialistannen.algorithms.layout.forcedbased.traversal.NodeChangeAction;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;
import me.ialistannen.algorithms.layout.forcedbased.view.GraphView;
import me.ialistannen.algorithms.layout.forcedbased.view.NodeCircle;

public class Test extends Application {

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();

    List<Node<String>> nodes = getNodes(500, 500);
    GraphView<String> graphView = new GraphView<>(nodes);

    LayoutManager<String> layoutManager = new LayoutManager<>(
        nodes,
        Arrays.asList(
            new ElectricalRepulsionForce(400000),
            new SpringAttractionForce(50, 0.1),
            new BlackHoleAttractionForce(new Vector2D(250, 250), 9.81e3)
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
        randomizeLocation(500, 500, nodes);
      }
    });

    Map<Node<String>, NodeCircle<String>> circleMap = graphView.getCircles().stream()
        .collect(Collectors.toMap(NodeCircle::getNode, Function.identity()));
    List<NodeChangeAction<String>> list = new DepthFirst().run(nodes);
    Timeline ticker = new Timeline(new KeyFrame(
        Duration.millis(500),
        event -> {
          NodeChangeAction<String> action = list.get(0);
          action.apply(circleMap.get(action.getNode()));
          list.remove(0);
        }
    ));
    ticker.setCycleCount(list.size());
    ticker.play();
  }

  private List<Node<String>> getNodes(int maxX, int maxY) {
    List<Node<String>> nodes = new ArrayList<>();

    for (int i = 0; i < 12; i++) {
      Node<String> newNode = new Node<>(String.format("%s", (char) (i + 'A')));

      for (Node<String> node : nodes) {
        if (ThreadLocalRandom.current().nextInt(10) < 4) {
//          node.addBidirectionalConnection(newNode);
          node.addUnidirectionalConnection(newNode);
        }
      }

      nodes.add(newNode);
    }

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

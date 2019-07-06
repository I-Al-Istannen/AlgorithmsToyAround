package me.ialistannen.algorithms.layout.forcedbased;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.ialistannen.algorithms.layout.forcedbased.forces.BlackHoleAttractionForce;
import me.ialistannen.algorithms.layout.forcedbased.forces.ElectricalRepulsionForce;
import me.ialistannen.algorithms.layout.forcedbased.forces.SpringAttractionForce;
import me.ialistannen.algorithms.layout.forcedbased.traversal.DijkstraTraversal;
import me.ialistannen.algorithms.layout.forcedbased.traversal.NodeChangeAction;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;
import me.ialistannen.algorithms.layout.forcedbased.view.GraphView;
import me.ialistannen.algorithms.layout.forcedbased.view.NodeCircle;

public class Test extends Application {

  /**
   * I don't want it to be garbage collected IntelliJ
   */
  @SuppressWarnings("FieldCanBeLocal")
  private ObjectBinding<Vector2D> windowCenterBinding;

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();

    ObservableList<Node<String>> nodes = FXCollections.observableArrayList(
        getDominoNodes(500, 500)
    );
    GraphView<String> graphView = new GraphView<>(nodes);

    windowCenterBinding = Bindings.createObjectBinding(
        () -> new Vector2D(root.getWidth() / 2, root.getHeight() / 2),
        root.widthProperty(), root.heightProperty()
    );

    LayoutManager<String> layoutManager = new LayoutManager<>(
        nodes,
        Arrays.asList(
            new ElectricalRepulsionForce(400000),
            new SpringAttractionForce(50, 0.1),
            new BlackHoleAttractionForce(windowCenterBinding, 9.81e3)
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
    List<NodeChangeAction<String>> list = new DijkstraTraversal().run(nodes);
    Timeline ticker = new Timeline(new KeyFrame(
        Duration.millis(500),
        event -> {
          NodeChangeAction<String> action = list.get(0);
          action.apply(circleMap.get(action.getNode()));
          list.remove(0);
        }
    ));
    ticker.setCycleCount(list.size());
//    ticker.play();

    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
    pauseTransition.setOnFinished(event -> {
      Node<String> node = new Node<>("Test");
      node.addUnidirectionalConnection(nodes.get(2), 20);
      nodes.add(node);
    });
    pauseTransition.playFromStart();
  }

  private List<Node<String>> getNodes(int maxX, int maxY) {
    List<Node<String>> nodes = new ArrayList<>();

    for (int i = 0; i < 12; i++) {
      Node<String> newNode = new Node<>(String.format("%s", (char) (i + 'A')));

      for (Node<String> node : nodes) {
        if (ThreadLocalRandom.current().nextInt(10) < 4) {
          node.addBidirectionalConnection(newNode, 1);
//          node.addUnidirectionalConnection(newNode);
        }
      }

      nodes.add(newNode);
    }

    randomizeLocation(maxX, maxY, nodes);
    return nodes;
  }

  private List<Node<String>> getDominoNodes(int maxX, int maxY) {
    List<Node<String>> nodes = new ArrayList<>();

    Node<String> one = new Node<>("1");
    Node<String> two = new Node<>("2");
    Node<String> three = new Node<>("3");
    Node<String> four = new Node<>("4");
    Node<String> five = new Node<>("5");
    Node<String> six = new Node<>("6");

    DoubleSupplier weight = () -> ThreadLocalRandom.current().nextInt(1, 5);
//    one.addBidirectionalConnection(two);
    one.addBidirectionalConnection(three, weight.getAsDouble());

    one.addBidirectionalConnection(four, weight.getAsDouble());
    one.addBidirectionalConnection(five, weight.getAsDouble());
    one.addBidirectionalConnection(six, weight.getAsDouble());

    two.addBidirectionalConnection(three, weight.getAsDouble());
    two.addBidirectionalConnection(four, weight.getAsDouble());
    two.addBidirectionalConnection(five, weight.getAsDouble());

    three.addBidirectionalConnection(four, weight.getAsDouble());

    four.addBidirectionalConnection(five, weight.getAsDouble());

    five.addBidirectionalConnection(six, weight.getAsDouble());

    Collections.addAll(
        nodes,
        one, two, three, four, five, six
    );

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

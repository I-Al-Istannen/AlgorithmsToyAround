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
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
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
        getMetaNodes(500, 500)
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

    root.setOnContextMenuRequested(event -> {
      ContextMenu contextMenu = new ContextMenu();
      MenuItem createNode = new MenuItem("Create node");

      createNode.setOnAction(e -> {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Select a name");
        textInputDialog.setHeaderText("Select the name of the node");
        textInputDialog.showAndWait().ifPresent(name -> {
          Node<String> node = new Node<>(name);
          node.setPosition(windowCenterBinding.get().multiply(2));
          nodes.add(node);
        });
      });

      contextMenu.getItems().add(createNode);

      contextMenu.show(graphView, event.getScreenX(), event.getScreenY());
    });

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

  private List<Node<String>> getMetaNodes(int maxX, int maxY) {
    List<Node<String>> nodes = new ArrayList<>();

    Node<String> s = new Node<>("s");
    Node<String> a = new Node<>("a");
    Node<String> c = new Node<>("c");
    Node<String> b = new Node<>("b");
    Node<String> d = new Node<>("d");
    Node<String> e = new Node<>("e");
    Node<String> f = new Node<>("f");

    Collections.addAll(
        nodes,
        s, a, b, c, d, e, f
    );

    s.addUnidirectionalConnection(a, 1.0 / nodes.size());
    s.addUnidirectionalConnection(c, 1.0 / nodes.size());

    a.addUnidirectionalConnection(b, 1.0 / nodes.size());

    b.addUnidirectionalConnection(d, 1.0 / nodes.size());

    c.addUnidirectionalConnection(b, 1.0 / nodes.size());
    c.addUnidirectionalConnection(e, 1.0 / nodes.size());

    e.addUnidirectionalConnection(f, 1.0 / nodes.size());

//    e.addUnidirectionalConnection(b, 1.0 / nodes.size());
//    e.addUnidirectionalConnection(f, 1.0 / nodes.size());

    randomizeLocation(maxX, maxY, nodes);
    return nodes;
  }

  private List<Node<String>> getANodes(int maxX, int maxY) {
    List<Node<String>> nodes = new ArrayList<>();

    Node<String> a = new Node<>("a");
    Node<String> b = new Node<>("b");
    Node<String> c = new Node<>("c");
    Node<String> d = new Node<>("d");

    Collections.addAll(
        nodes,
        a, b, c, d
    );

    b.addUnidirectionalConnection(a, 0);
    c.addUnidirectionalConnection(a, 0);
    d.addUnidirectionalConnection(b, 0);
    d.addUnidirectionalConnection(c, 0);

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

package me.ialistannen.algorithms.layout.forcedbased.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;
import me.ialistannen.algorithms.layout.forcedbased.traversal.AlgoAPainting;
import me.ialistannen.algorithms.layout.forcedbased.traversal.AlgoB;
import me.ialistannen.algorithms.layout.forcedbased.traversal.AlgoBNeaterSimulation;
import me.ialistannen.algorithms.layout.forcedbased.traversal.BellmanFord;
import me.ialistannen.algorithms.layout.forcedbased.traversal.BreadthFirst;
import me.ialistannen.algorithms.layout.forcedbased.traversal.DepthFirst;
import me.ialistannen.algorithms.layout.forcedbased.traversal.DepthFirstAlgoA;
import me.ialistannen.algorithms.layout.forcedbased.traversal.DijkstraTraversal;
import me.ialistannen.algorithms.layout.forcedbased.traversal.NodeChangeAction;
import me.ialistannen.algorithms.layout.forcedbased.traversal.WeirdAlgoSolution;
import me.ialistannen.algorithms.layout.forcedbased.tree.Edge;
import me.ialistannen.algorithms.layout.forcedbased.tree.EdgeTraversal;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A view pane for a graph.
 *
 * @param <T> the type of the graph
 */
public class GraphView<T> extends StackPane {

  private final List<NodeCircle<T>> circles;
  private final AnchorPane circlePane;
  private final List<ConnectionLine<T>> connectionLines;
  private final AnchorPane linePane;
  private final DragInteractionManager<T> dragInteractionManager;
  private final ObservableList<Node<T>> nodes;
  private Timeline animationTimer;

  /**
   * Creates a new graph view.
   *
   * @param nodes the nodes to display
   */
  public GraphView(ObservableList<Node<T>> nodes) {
    this.nodes = nodes;
    this.circlePane = new AnchorPane();
    this.linePane = new AnchorPane();

    circlePane.setPickOnBounds(false);
    linePane.setPickOnBounds(false);
    setPickOnBounds(false);

    getChildren().addAll(linePane, circlePane);

    nodes.addListener(new ListChangeListener<Node<T>>() {
      @Override
      public void onChanged(Change<? extends Node<T>> c) {
        while (c.next()) {
          if (c.wasRemoved()) {
            circles.stream()
                .filter(it -> c.getRemoved().contains(it.getNode()))
                .collect(Collectors.toList()) // copy needed as we are iterating over it
                .stream()
                .forEach(GraphView.this::removeCircle);

          }

          if (c.wasAdded()) {
            for (Node<T> added : c.getAddedSubList()) {
              addCircle(new NodeCircle<>(added));
            }
          }
        }
      }
    });

    circles = nodes.stream()
        .map(NodeCircle::new)
        .collect(Collectors.toList());

    connectionLines = connectionLines(circles);

    circles.forEach(circlePane.getChildren()::add);
    connectionLines.forEach(linePane.getChildren()::add);

    dragInteractionManager = new DragInteractionManager<>();

    circles
        .forEach(dragInteractionManager::registerCircleDragAndDrop);
    circles.forEach(registerDeleteContextMenu(nodes));
    circles.forEach(this::registerEdgeListener);

    setOnMouseDragged(event -> dragInteractionManager.executeIfDragging((circle, startPos) -> {
      Vector2D currentPos = new Vector2D(event.getX(), event.getY())
          .subtract(circle.getWidth() / 2, circle.getHeight() / 2);

      Vector2D direction = currentPos.subtract(startPos);

      circle.getNode().setActingForce(direction);
      circle.getNode().setPosition(currentPos);
    }));
  }

  private Consumer<NodeCircle<T>> registerDeleteContextMenu(ObservableList<Node<T>> nodes) {
    return circle -> registerDeleteContextMenu(nodes, circle);
  }

  private void registerDeleteContextMenu(ObservableList<Node<T>> nodes, NodeCircle<T> circle) {
    circle.setOnContextMenuRequested(event -> {
      ContextMenu contextMenu = new ContextMenu();

      MenuItem delete = new MenuItem("Delete");
      delete.setOnAction(e -> nodes.remove(circle.getNode()));
      contextMenu.getItems().add(delete);

      MenuItem startDFS = new MenuItem("Start DFS");
      startDFS.setOnAction(e -> replayActions(
          new DepthFirst().run(Collections.singletonList(circle.getNode()))
      ));
      contextMenu.getItems().add(startDFS);

      MenuItem startBFS = new MenuItem("Start BFS");
      startBFS.setOnAction(e -> replayActions(
          new BreadthFirst().run(Collections.singletonList(circle.getNode()))
      ));
      contextMenu.getItems().add(startBFS);

      MenuItem startDijkstra = new MenuItem("Start Dijkstra");
      startDijkstra.setOnAction(e -> replayActions(
          new DijkstraTraversal().run(Collections.singletonList(circle.getNode()))
      ));
      contextMenu.getItems().add(startDijkstra);

      MenuItem startBellmanFord = new MenuItem("Start BellmanFord");
      startBellmanFord.setOnAction(e -> replayActions(
          new BellmanFord().run(nodes)
      ));
      contextMenu.getItems().add(startBellmanFord);

      MenuItem startAlgoDFS = new MenuItem("Algo A DFS");
      startAlgoDFS.setOnAction(e -> {
        List<Node<T>> allNodes = new ArrayList<>(nodes);
        allNodes.add(0, circle.getNode());
        replayActions(new DepthFirstAlgoA().run(allNodes));
      });
      contextMenu.getItems().add(startAlgoDFS);

      MenuItem startAlgoAPainting = new MenuItem("Algo A Painting");
      startAlgoAPainting.setOnAction(e -> {
        List<Node<T>> allNodes = new ArrayList<>(nodes);
        allNodes.add(0, circle.getNode());
        replayActions(new AlgoAPainting().run(allNodes));
      });
      contextMenu.getItems().add(startAlgoAPainting);

      MenuItem startAlgoBTest = new MenuItem("Algo B test");
      startAlgoBTest.setOnAction(e -> {
        List<Node<T>> allNodes = new ArrayList<>(nodes);
        allNodes.add(0, circle.getNode());
        replayActions(new AlgoB().run(allNodes));
      });
      contextMenu.getItems().add(startAlgoBTest);

      MenuItem startAlgoBSimulation = new MenuItem("Algo B simulation");
      startAlgoBSimulation.setOnAction(e -> {
        List<Node<T>> allNodes = new ArrayList<>(nodes);
        allNodes.add(0, circle.getNode());
        replayActions(new AlgoBNeaterSimulation().run(allNodes));
      });
      contextMenu.getItems().add(startAlgoBSimulation);

      MenuItem weirdAlgoSolution = new MenuItem("Weird algo solution");
      weirdAlgoSolution.setOnAction(e -> {
        List<Node<T>> allNodes = new ArrayList<>(nodes);
        replayActions(new WeirdAlgoSolution().run(allNodes));
      });
      contextMenu.getItems().add(weirdAlgoSolution);

      event.consume();
      contextMenu.show(circle, event.getScreenX(), event.getScreenY());
    });
  }

  private void replayActions(List<NodeChangeAction<T>> actions) {
    if (animationTimer != null && animationTimer.getStatus() != Status.STOPPED) {
      animationTimer.stop();
    }

    // reset circles
    for (NodeCircle<T> circle : getCircles()) {
      circle.setLeftText("");
      circle.setRightText("");
      circle.setHighlight(false);
    }

    animationTimer = new Timeline(new KeyFrame(
        Duration.millis(500),
        event -> {
          if (actions.isEmpty()) {
            return;
          }
          NodeChangeAction<T> action = actions.get(0);
          findCircleForNode(action.getNode()).ifPresent(action::apply);
          actions.remove(0);
        }
    ));
    animationTimer.setCycleCount(actions.size());
    animationTimer.play();
  }

  private void removeCircle(NodeCircle<T> circle) {
    circles.remove(circle);
    circlePane.getChildren().remove(circle);

    removeEdgesConnectedTo(circle);
  }

  private void removeEdgesConnectedTo(NodeCircle<T> circle) {
    connectionLines.stream()
        .filter(line -> line.isEndOrStartFor(circle))
        .forEach(linePane.getChildren()::remove);
  }

  private void addCircle(NodeCircle<T> circle) {
    circles.add(circle);
    circlePane.getChildren().add(circle);
    dragInteractionManager.registerCircleDragAndDrop(circle);

    Node<T> node = circle.getNode();
    for (Edge<T> edge : node.getEdges()) {
      Node<T> end = edge.getEnd();

      findCircleForNode(end)
          .ifPresent(endCircle -> {
            ConnectionLine<T> line = new ConnectionLine<>(circle, endCircle, edge);
            connectionLines.add(line);
            linePane.getChildren().add(line);
          });
    }

    registerEdgeListener(circle);
    registerDeleteContextMenu(nodes, circle);
  }

  private void registerEdgeListener(NodeCircle<T> circle) {
    Node<T> node = circle.getNode();

    node.registerEdgeListener(change -> {
      if (change.wasRemoved()) {
        Predicate<ConnectionLine<T>> isForChangedEdge = line -> {
          Edge<T> removed = change.getValueRemoved();
          return line.isEndOrStartFor(removed.getEnd()) && line.isEndOrStartFor(removed.getStart());
        };
        List<ConnectionLine<T>> toRemove = connectionLines.stream()
            .filter(isForChangedEdge)
            .collect(Collectors.toList());
        toRemove.forEach(line -> {
          connectionLines.remove(line);
          linePane.getChildren().remove(line);
        });
      }
      if (change.wasAdded()) {
        Edge<T> added = change.getValueAdded();
        findCircleForNode(added.getEnd()).ifPresent(endCircle -> {
          // Just modified, nothing new
          for (ConnectionLine<T> line : connectionLines) {
            if (line.isEndOrStartFor(added.getStart()) && line.isEndOrStartFor(added.getEnd())) {
              return;
            }
          }
          ConnectionLine<T> connectionLine = new ConnectionLine<>(circle, endCircle, added);
          connectionLines.add(connectionLine);
          linePane.getChildren().add(connectionLine);
        });
      }
    });
  }

  @NotNull
  private Optional<NodeCircle<T>> findCircleForNode(Node<T> end) {
    return circles.stream()
        .filter(it -> it.getNode().equals(end))
        .findFirst();
  }

  private List<ConnectionLine<T>> connectionLines(List<NodeCircle<T>> nodes) {
    if (nodes.isEmpty()) {
      return Collections.emptyList();
    }

    List<ConnectionLine<T>> result = new ArrayList<>();

    Map<Node<T>, NodeCircle<T>> circleMap = nodes.stream()
        .collect(Collectors.toMap(NodeCircle::getNode, Function.identity()));

    Collection<Edge<T>> edges = new EdgeTraversal().getEdges(circleMap.keySet());
    for (Edge<T> edge : edges) {
      NodeCircle<T> start = circleMap.get(edge.getStart());
      NodeCircle<T> end = circleMap.get(edge.getEnd());
      result.add(new ConnectionLine<>(start, end, edge));
    }

    return result;
  }

  /**
   * Updates the graph.
   */
  public void update() {
    circles.forEach(NodeCircle::update);
  }

  /**
   * Returns all node circles.
   *
   * @return the node circles
   */
  public List<NodeCircle<T>> getCircles() {
    return Collections.unmodifiableList(circles);
  }
}

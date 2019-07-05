package me.ialistannen.algorithms.layout.forcedbased.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;
import me.ialistannen.algorithms.layout.forcedbased.tree.Edge;
import me.ialistannen.algorithms.layout.forcedbased.tree.EdgeTraversal;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A view pane for a graph.
 *
 * @param <T> the type of the graph
 */
public class GraphView<T> extends FlowPane {

  private AnchorPane circlePane;
  private AnchorPane linePane;
  private final List<NodeCircle<T>> circles;

  /**
   * Creates a new graph view.
   *
   * @param nodes the nodes to display
   */
  public GraphView(List<Node<T>> nodes) {
    StackPane basePane = new StackPane();
    this.circlePane = new AnchorPane();
    this.linePane = new AnchorPane();

    basePane.getChildren().addAll(linePane, circlePane);
    getChildren().add(basePane);

    circles = nodes.stream()
        .map(NodeCircle::new)
        .collect(Collectors.toList());

    circles
        .forEach(node -> circlePane.getChildren().add(node));

    connectionLines(circles)
        .forEach(it -> linePane.getChildren().add(it));

    DragInteractionManager<T> dragInteractionManager = new DragInteractionManager<>();

    circles
        .forEach(dragInteractionManager::registerCircleDragAndDrop);

    setOnMouseDragged(event -> dragInteractionManager.executeIfDragging((circle, startPos) -> {
      Vector2D currentPos = new Vector2D(event.getX(), event.getY())
          .subtract(circle.getWidth() / 2, circle.getHeight() / 2);

      Vector2D direction = currentPos.subtract(startPos);

      circle.getNode().setActingForce(direction);
      circle.getNode().setPosition(currentPos);
    }));
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

package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import me.ialistannen.algorithms.layout.forcedbased.tree.Edge;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;
import org.jetbrains.annotations.NotNull;

/**
 * Traverses the tree by using Dijkstra's algorithm for shortest paths.
 */
public class DijkstraTraversal implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    List<NodeChangeAction<T>> changeActions = new ArrayList<>();

    PriorityQueue<DijkstraNode<T>> openSet = new PriorityQueue<>();

    DijkstraNode<T> startNode = new DijkstraNodeFactory<T>().getOrCached(nodes.get(0));
    startNode.distance = 0;

    changeActions.add(NodeChangeAction.builder(startNode.wrapped)
        .withLeftText("S")
        .withRightText("0")
        .withHighlight(true)
        .build()
    );

    openSet.add(startNode);

    while (!openSet.isEmpty()) {
      DijkstraNode<T> node = openSet.poll();

      changeActions.add(NodeChangeAction.builder(node.wrapped)
          .withRightText(DecimalFormat.getNumberInstance().format(node.distance))
          .withHighlight(true)
          .build()
      );

      for (DijkstraNode<T> neighbour : node.getNeighbours()) {
        double weight = node.weightTo(neighbour.wrapped);
        double origDist = neighbour.distance;

        if (neighbour.relax(weight + node.distance)) {
          changeActions.add(NodeChangeAction.builder(neighbour.wrapped)
              .withLeftText(DecimalFormat.getNumberInstance().format(origDist))
              .withRightText(DecimalFormat.getNumberInstance().format(neighbour.distance))
              .build()
          );
          openSet.add(neighbour);
        }
      }
    }

    return changeActions;
  }

  /**
   * A simple dijkstra node storing the parent.
   *
   * @param <T> the type of the node
   */
  private static class DijkstraNode<T> implements Comparable<DijkstraNode<T>> {

    private Node<T> wrapped;
    private double distance = Double.POSITIVE_INFINITY;
    private DijkstraNodeFactory<T> nodeFactory;

    /**
     * Creates a new dijkstra node wrapping a normal one.
     *
     * @param wrapped the wrapped node
     * @param nodeFactory the node factory
     */
    DijkstraNode(Node<T> wrapped, DijkstraNodeFactory<T> nodeFactory) {
      this.wrapped = wrapped;
      this.nodeFactory = nodeFactory;
    }

    /**
     * Relaxes the node, i.e. changes the distance if it is smaller.
     *
     * @param distance the potential new distance
     * @return true if the distance was better
     */
    private boolean relax(double distance) {
      if (this.distance > distance) {
        this.distance = distance;
        return true;
      }
      return false;
    }

    /**
     * Returns a list with neighbours.
     *
     * @return all neighbours
     */
    private List<DijkstraNode<T>> getNeighbours() {
      return wrapped.getNeighbours().stream()
          .map(nodeFactory::getOrCached)
          .collect(Collectors.toList());
    }

    /**
     * Returns the weight to the given other node.
     *
     * @param other the other node
     * @return the weight to it
     */
    private double weightTo(Node<T> other) {
      return wrapped.getEdges().stream()
          .filter(tEdge -> tEdge.getEnd().equals(other))
          .findFirst()
          .map(Edge::getWeight)
          .orElse(0d);
    }

    @Override
    public int compareTo(@NotNull DijkstraNode<T> o) {
      return Double.compare(distance, o.distance);
    }
  }

  /**
   * A caching factory for dijkstra nodes.
   *
   * @param <T> the type of the nodes
   */
  private static class DijkstraNodeFactory<T> {

    private Map<Node<T>, DijkstraNode<T>> cache;

    /**
     * Creates a new node factory.
     */
    DijkstraNodeFactory() {
      this.cache = new HashMap<>();
    }

    /**
     * Returns a new dijkstra node or a cached one, if one exists.
     *
     * @param node the node to wrap
     * @return the new or cached node
     */
    DijkstraNode<T> getOrCached(Node<T> node) {
      return cache.computeIfAbsent(node, newNode -> new DijkstraNode<>(newNode, this));
    }
  }
}

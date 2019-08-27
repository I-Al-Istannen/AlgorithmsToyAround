package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import me.ialistannen.algorithms.layout.forcedbased.tree.Edge;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

public class BellmanFord implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    Context<T> context = new Context<>();

    context.distances.put(nodes.get(0), 0d);

    for (int i = 0; i < nodes.size() - 1; i++) {
      for (Node<T> node : nodes) {
        for (Edge<T> edge : node.getEdges()) {
          relax(context, edge);
        }
      }
    }

    for (Node<T> node : nodes) {
      for (Edge<T> edge : node.getEdges()) {
        if (relax(context, edge)) {
          infect(node, context);
        }
      }
    }

    for (Node<T> node : nodes) {
      context.actions.add(NodeChangeAction.builder(node)
          .withLeftText(
              DecimalFormat.getNumberInstance().format(context.distances.getOrDefault(node, -1d))
          )
          .build()
      );

    }

    return context.actions;
  }

  private <T> boolean relax(Context<T> context, Edge<T> edge) {
    Node<T> parent = edge.getStart();

    double baseWeight = context.distances.getOrDefault(parent, 0d);
    double edgeWeight = edge.getWeight();
    double fullWeight = edgeWeight + baseWeight;

    if (fullWeight < context.distances.getOrDefault(edge.getEnd(), Double.POSITIVE_INFINITY)) {
      context.distances.put(edge.getEnd(), fullWeight);
      context.openSet.add(edge.getEnd());
      context.parents.put(edge.getEnd(), parent);
      return true;
    }
    return false;
  }

  private <T> void infect(Node<T> start, Context<T> context) {
    if (context.distances.getOrDefault(start, 0d) == Double.POSITIVE_INFINITY) {
      return;
    }
    context.distances.put(start, Double.POSITIVE_INFINITY);

    start.getNeighbours().forEach(it -> infect(it, context));
  }

  private static class Context<T> {

    private Map<Node<T>, Double> distances;
    private Map<Node<T>, Node<T>> parents;
    private TreeSet<Node<T>> openSet;
    private List<NodeChangeAction<T>> actions;

    private Context() {
      distances = new HashMap<>();
      parents = new HashMap<>();
      actions = new ArrayList<>();

      openSet = new TreeSet<>(
          Comparator.<Node<T>, Double>comparing(o -> distances.getOrDefault(o, 0d))
              .thenComparingInt(System::identityHashCode)
      );
    }
  }

}

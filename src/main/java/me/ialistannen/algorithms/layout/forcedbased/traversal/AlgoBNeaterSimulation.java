package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * Solves the algorithm assignment B.
 *
 * <p><br>Runtime:
 * <br>Alle Pfade berechnen: n * Semi-DFS ==> n * (|V| + |E|) ==> n^2? n^3?
 * <br>Min: n
 * </p>
 */
public class AlgoBNeaterSimulation implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    List<NodeChangeAction<T>> actions = new ArrayList<>();

    Node<T> start = nodes.get(0);

    Map<Node<T>, Integer> reachableNodes = new HashMap<>();

    for (Node<T> toKill : nodes) {
      actions.add(NodeChangeAction.builder(toKill)
          .withLeftText("✘")
          .withHighlight(true)
          .build()
      );
      for (Node<T> node : getReachable(start, toKill, new HashSet<>())) {
        reachableNodes.merge(node, 1, Integer::sum);
        actions.add(NodeChangeAction.builder(node)
            .withLeftText("✓")
            .build()
        );
      }
      for (Entry<Node<T>, Integer> entry : reachableNodes.entrySet()) {
        actions.add(NodeChangeAction.builder(entry.getKey())
            .withLeftText("")
            .withRightText(String.valueOf(entry.getValue()))
            .withHighlight(false)
            .build()
        );
      }
    }

    reachableNodes.entrySet().stream()
        .min(Entry.comparingByValue())
        .ifPresent(it -> actions.add(NodeChangeAction.builder(it.getKey())
            .withHighlight(true)
            .withLeftText("✓")
            .build()
        ));

    return actions;
  }

  private <T> List<Node<T>> getReachable(Node<T> start, Node<T> killedNode, Set<Node<T>> visited) {
    List<Node<T>> reachable = new ArrayList<>();

    if (!visited.add(start) || start.equals(killedNode)) {
      return reachable;
    }

    reachable.add(start);

    for (Node<T> neighbour : start.getNeighbours()) {
      reachable.addAll(getReachable(neighbour, killedNode, visited));
    }

    return reachable;
  }
}

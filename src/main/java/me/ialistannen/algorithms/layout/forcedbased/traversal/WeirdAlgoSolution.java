package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import me.ialistannen.algorithms.layout.forcedbased.tree.Edge;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * Basically the given solution for 2018-2 C1
 */
public class WeirdAlgoSolution implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    Map<Node<T>, Integer> deg = new HashMap<>();
    Set<Node<T>> active = new HashSet<>(nodes);
    Map<Node<T>, Integer> core = new HashMap<>();

    PriorityQueue<Node<T>> queue = new PriorityQueue<>(Comparator.comparing(deg::get));

    List<Edge<T>> dedupedEdges = nodes.stream()
        .flatMap(it -> it.getEdges().stream())
        .distinct()
        .collect(Collectors.toList());

    for (Edge<T> edge : dedupedEdges) {
      deg.merge(edge.getStart(), 1, Integer::sum);
      deg.merge(edge.getEnd(), 1, Integer::sum);
    }

    queue.addAll(nodes);

    while (!queue.isEmpty()) {
      Node<T> v = queue.poll();
      core.put(v, deg.get(v));
      active.remove(v);

      for (Edge<T> edge : v.getEdges()) {
        if (!active.contains(edge.getEnd())) {
          continue;
        }
        // Without that if this is completely off!
        if (!deg.get(edge.getEnd()).equals(deg.get(v))) {
          deg.merge(edge.getEnd(), -1, Integer::sum);
        }

        // Update by reinserting - there is no decreasePriority in java's queue
        queue.remove(edge.getEnd());
        queue.add(edge.getEnd());
      }
    }

    List<NodeChangeAction<T>> changes = new ArrayList<>();

    for (Entry<Node<T>, Integer> entry : core.entrySet()) {
      changes.add(
          NodeChangeAction.builder(entry.getKey())
              .withLeftText("" + entry.getValue())
              .build()
      );
    }

    return changes;
  }
}

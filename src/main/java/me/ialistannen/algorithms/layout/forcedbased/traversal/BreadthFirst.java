package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A Breadth first traversal.
 */
public class BreadthFirst implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    List<NodeChangeAction<T>> result = new ArrayList<>();

    Queue<Node<T>> thisLayer = new ArrayDeque<>();
    Queue<Node<T>> nextLayer = new ArrayDeque<>();
    Set<Node<T>> visited = new HashSet<>();

    Node<T> start = nodes.get(0);

    result.add(NodeChangeAction.builder(start)
        .withLeftText("0")
        .withRightText("0")
        .withHighlight(true)
        .build()
    );

    thisLayer.add(start);

    int counter = 0;
    for (int level = 1; !thisLayer.isEmpty(); level++) {

      while (!thisLayer.isEmpty()) {
        Node<T> node = thisLayer.poll();
        visited.add(node);
        for (Node<T> neighbour : node.getNeighbours()) {
          if (!visited.add(neighbour)) {
            continue;
          }
          result.add(
              NodeChangeAction.builder(neighbour)
                  .withLeftText("" + level)
                  .withRightText("" + ++counter)
                  .withHighlight(true)
                  .build()
          );
          nextLayer.add(neighbour);
        }
      }

      thisLayer = new ArrayDeque<>(nextLayer);
      nextLayer.clear();
    }

    return result;
  }
}

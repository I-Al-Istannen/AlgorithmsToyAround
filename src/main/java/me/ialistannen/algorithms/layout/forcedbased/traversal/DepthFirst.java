package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A Depth first traversal.
 */
public class DepthFirst implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    List<NodeChangeAction<T>> actions = new ArrayList<>();
    Set<Node<T>> visited = new HashSet<>();

    for (Node<T> node : nodes) {
      visit(node, 1, actions, visited);
    }

    return actions;
  }

  private <T> int visit(Node<T> start, int index, List<NodeChangeAction<T>> actions,
      Collection<Node<T>> visited) {
    if (!visited.add(start)) {
      return index - 1;
    }
    actions.add(NodeChangeAction.builder(start).withLeftText("" + index).build());

    for (Node<T> neighbour : start.getNeighbours()) {
      index = visit(neighbour, index + 1, actions, visited);
    }
    index++;

    actions.add(NodeChangeAction.builder(start).withRightText("" + index).build());

    return index;
  }
}

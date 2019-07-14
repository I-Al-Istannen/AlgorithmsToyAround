package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.ialistannen.algorithms.layout.forcedbased.tree.Edge;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A Depth first traversal.
 */
public class DepthFirstAlgoA implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    List<NodeChangeAction<T>> actions = new ArrayList<>();
    Set<Node<T>> visited = new HashSet<>();

    visit(nodes.get(0), 1, actions, visited, nodes);

    return actions;
  }

  private <T> int visit(Node<T> start, int index, List<NodeChangeAction<T>> actions,
      Collection<Node<T>> visited, List<Node<T>> all) {
    if (!visited.add(start)) {
      return index - 1;
    }
    actions.add(
        NodeChangeAction.builder(start).withLeftText("" + index).withHighlight(true).build()
    );

    for (Node<T> neighbour : getConnectedTo(start, all)) {
      index = visit(neighbour, index + 1, actions, visited, all);
    }

    index++;

    actions.add(
        NodeChangeAction.builder(start).withRightText("" + index).withHighlight(true).build()
    );

    return index;
  }

  private <T> Collection<Node<T>> getConnectedTo(Node<T> node, List<Node<T>> nodes) {
    Set<Node<T>> found = new HashSet<>();

    for (Node<T> tNode : nodes) {
      for (Edge<T> edge : tNode.getEdges()) {
        if (edge.getEnd().equals(node)) {
          found.add(edge.getStart());
        }
      }
    }

    found.remove(node);

    return found;
  }
}

package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * Solves the first assignment with a painting DFS.
 *
 * <p><br>Runtime:
 * <br>Traverses each node exactly once.
 * <br>Traverses each edge at most 2 times.
 * </p>
 */
public class AlgoAPainting implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    List<NodeChangeAction<T>> actions = new ArrayList<>();

    HashMap<Node<T>, MarkStatus> markings = new HashMap<>();
    Node<T> start = nodes.get(0);

    actions.add(NodeChangeAction.builder(start).withHighlight(true).build());

    for (Node<T> node : nodes) {
      actions.addAll(runFor(node, start, markings));
    }

    return actions;
  }

  private <T> List<NodeChangeAction<T>> runFor(Node<T> node, Node<T> start,
      Map<Node<T>, MarkStatus> markings) {
    List<NodeChangeAction<T>> changeActions = new ArrayList<>();
    MarkStatus myMark = node.equals(start) ? MarkStatus.SUCCESSFUL : MarkStatus.FAILED;

    if (markings.containsKey(node)) {
      return changeActions;
    }

    for (Node<T> neighbour : node.getNeighbours()) {
      changeActions.addAll(runFor(neighbour, start, markings));

      if (markings.get(neighbour) == MarkStatus.SUCCESSFUL) {
        myMark = MarkStatus.SUCCESSFUL;
        break;
      }
    }

    changeActions.add(
        NodeChangeAction.builder(node)
            .withLeftText(myMark.toString())
            .withHighlight(start.equals(node))
            .build()
    );

    markings.put(node, myMark);

    return changeActions;
  }

  private enum MarkStatus {
    SUCCESSFUL("✓"),
    FAILED("✘");

    private String display;

    MarkStatus(String display) {
      this.display = display;
    }

    @Override
    public String toString() {
      return display;
    }
  }
}

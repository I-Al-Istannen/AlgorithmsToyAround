package me.ialistannen.treeviewer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import me.ialistannen.algorithms.math.parser.token.Token;

/**
 * A simple tree
 */
public interface Tree {

  /**
   * @return The parent node. May be null
   */
  Tree getParent();

  /**
   * @return The children of the tree
   */
  List<? extends Tree> getChildren();

  /**
   * @return The {@link Token}
   */
  Token getToken();

  /**
   * Evaluates this node.
   *
   * @return The value of this node.
   */
  double evaluate();

  /**
   * @return ALl children in level order. The key is the level, the value the nodes
   */
  default Map<Integer, List<? extends Tree>> getChildrenLevels() {
    Map<Integer, List<? extends Tree>> map = new HashMap<>();

    List<Tree> nodes = new ArrayList<>();
    Queue<Tree> queue = new ArrayDeque<>();
    queue.add(this);
    int currentCount = 1;
    int nextCount = 0;
    int level = 0;
    while (!queue.isEmpty()) {
      currentCount--;
      Tree node = queue.poll();
      nodes.add(node);

      queue.addAll(node.getChildren());

      nextCount += node.getChildren().size();

      if (currentCount == 0) {
        map.put(level++, nodes);
        nodes = new ArrayList<>();
        currentCount = nextCount;
        nextCount = 0;
      }
    }

    return map;
  }
}

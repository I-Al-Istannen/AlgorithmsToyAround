package me.ialistannen.pathfinding.visualize.algorithms.breadthfirst;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmResult;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;
import me.ialistannen.pathfinding.visualize.grid.StatefulGridCoordinate;

public class BreadthFirstAlgorithm implements Algorithm<DefaultGridState> {

  private Map<GridCoordinate, BreadthNode> nodeCache;
  private Queue<BreadthNode> queue;
  private Set<GridCoordinate> closedSet;
  private List<StatefulGridCoordinate<DefaultGridState>> steps;
  private List<Direction> directions;

  public BreadthFirstAlgorithm(List<Direction> directions) {
    this.directions = directions;
  }

  @Override
  public AlgorithmResult<DefaultGridState> compute(AlgorithmGrid<DefaultGridState> grid) {
    reset();

    grid.getStarts().stream()
        .map(coordinate -> new BreadthNode(0, coordinate, null))
        .forEach(breadthNode -> queue.add(breadthNode));

    BreadthNode tmp;
    while ((tmp = queue.poll()) != null) {
      GridCoordinate coordinate = tmp.getCoordinate();

      if (grid.getStateAt(coordinate).isEnd()) {
        break;
      }

      addStep(coordinate, DefaultGridState.EXAMINED);
      expand(tmp, grid);
    }

    return backtrackResolve(tmp, grid);
  }

  private AlgorithmResult<DefaultGridState> backtrackResolve(BreadthNode node,
      AlgorithmGrid<DefaultGridState> grid) {
    if (node == null || !grid.getStateAt(node.getCoordinate()).isEnd()) {
      return new AlgorithmResult<>(false, steps);
    }

    while (node != null) {
      addStep(node.getCoordinate(), DefaultGridState.SOLUTION);
      node = node.getParent();
    }

    return new AlgorithmResult<>(true, steps);
  }

  private void expand(BreadthNode node, AlgorithmGrid<DefaultGridState> grid) {
    closedSet.add(node.getCoordinate());

    for (Direction direction : directions) {
      GridCoordinate newCoordinate = node.getCoordinate().getNeighbour(direction);

      if (closedSet.contains(newCoordinate) || !grid.canMove(node.getCoordinate(), newCoordinate)) {
        continue;
      }

      double newDistance = node.getDistanceToStart() + direction.getCost();

      BreadthNode child;

      if (nodeCache.containsKey(newCoordinate)) {
        child = nodeCache.get(newCoordinate);
      } else {
        child = new BreadthNode(newDistance, newCoordinate, node);
        nodeCache.put(newCoordinate, child);

        addStep(newCoordinate, DefaultGridState.OPEN_SET);
        queue.add(child);
      }

      if (newDistance < child.getDistanceToStart()) {
        child.setDistanceToStart(newDistance);
        child.setParent(node);
      }
    }
  }

  private void reset() {
    queue = new ArrayDeque<>();
    closedSet = new HashSet<>();
    steps = new ArrayList<>();
    nodeCache = new HashMap<>();
  }

  private void addStep(GridCoordinate coordinate, DefaultGridState state) {
    steps.add(new StatefulGridCoordinate<>(coordinate, state));
  }

  private static class BreadthNode extends BaseNode<BreadthNode> {

    BreadthNode(double distanceToStart, GridCoordinate coordinate, BreadthNode parent) {
      super(distanceToStart, coordinate, null, parent);
    }
  }
}

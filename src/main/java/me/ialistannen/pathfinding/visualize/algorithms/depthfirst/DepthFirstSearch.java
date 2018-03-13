package me.ialistannen.pathfinding.visualize.algorithms.depthfirst;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmResult;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.algorithms.base.BaseAlgorithm;
import me.ialistannen.pathfinding.visualize.algorithms.depthfirst.DepthFirstSearch.DepthFirstNode;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public class DepthFirstSearch extends BaseAlgorithm<DefaultGridState, DepthFirstNode> {

  private Deque<DepthFirstNode> nodes;

  public DepthFirstSearch(List<Direction> directions) {
    super(reverse(directions));
  }

  private static <T> List<T> reverse(List<T> list) {
    ArrayList<T> arrayList = new ArrayList<>(list);
    Collections.reverse(arrayList);
    return arrayList;
  }

  @Override
  protected AlgorithmResult<DefaultGridState> compute() {
    for (GridCoordinate gridCoordinate : grid.getStarts()) {
      nodes.add(new DepthFirstNode(0, gridCoordinate, grid.getEnd(), null));
    }

    DepthFirstNode tmp;
    while ((tmp = nodes.pollFirst()) != null) {
      GridCoordinate coordinate = tmp.getCoordinate();

      if (grid.getStateAt(coordinate).isEnd()) {
        break;
      }

      addStep(coordinate, DefaultGridState.EXAMINED);
      expand(tmp);
    }

    return backtrackResolve(tmp, DefaultGridState.SOLUTION);
  }

  @Override
  protected void reset(AlgorithmGrid<DefaultGridState> grid) {
    super.reset(grid);
    nodes = new ArrayDeque<>();
  }

  private void expand(DepthFirstNode node) {
    markAsClosed(node.getCoordinate());

    for (Direction direction : directions) {
      GridCoordinate newCoordinate = node.getCoordinate().getNeighbour(direction);
      double newDistance = node.getDistanceToStart() + direction.getCost();

      if (isClosed(newCoordinate) || !canMoveTo(node.getCoordinate(), newCoordinate)) {
        continue;
      }

      DepthFirstNode child = newNodeOrCached(
          newCoordinate,
          new DepthFirstNode(newDistance, newCoordinate, node.getTarget(), node),
          newNode -> {
            addStep(newCoordinate, DefaultGridState.OPEN_SET);
            nodes.addFirst(newNode);
          }
      );

      if (newDistance < child.getDistanceToStart()) {
        child.setDistanceToStart(newDistance);
        child.setParent(node);
      }
    }
  }

  static class DepthFirstNode extends BaseNode<DepthFirstNode> {

    DepthFirstNode(double distanceToStart, GridCoordinate coordinate, GridCoordinate target,
        DepthFirstNode parent) {
      super(distanceToStart, coordinate, target, parent);
    }
  }
}

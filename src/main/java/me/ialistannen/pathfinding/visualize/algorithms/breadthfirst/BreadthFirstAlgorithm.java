package me.ialistannen.pathfinding.visualize.algorithms.breadthfirst;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmResult;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.algorithms.base.BaseAlgorithm;
import me.ialistannen.pathfinding.visualize.algorithms.breadthfirst.BreadthFirstAlgorithm.BreadthNode;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public class BreadthFirstAlgorithm extends BaseAlgorithm<DefaultGridState, BreadthNode> {

  private Queue<BreadthNode> queue;

  public BreadthFirstAlgorithm(List<Direction> directions) {
    super(directions);
  }

  @Override
  public AlgorithmResult<DefaultGridState> compute() {
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

    return backtrackResolve(tmp, DefaultGridState.SOLUTION);
  }

  private void expand(BreadthNode node, AlgorithmGrid<DefaultGridState> grid) {
    markAsClosed(node.getCoordinate());

    for (Direction direction : directions) {
      GridCoordinate newCoordinate = node.getCoordinate().getNeighbour(direction);

      if (isClosed(newCoordinate) || !grid.canMove(node.getCoordinate(), newCoordinate)) {
        continue;
      }

      double newDistance = node.getDistanceToStart() + direction.getCost();

      BreadthNode child = newNodeOrCached(newCoordinate,
          new BreadthNode(newDistance, newCoordinate, node),
          newNode -> {
            addStep(newCoordinate, DefaultGridState.OPEN_SET);
            queue.add(newNode);
          }
      );

      if (newDistance < child.getDistanceToStart()) {
        child.setDistanceToStart(newDistance);
        child.setParent(node);
      }
    }
  }

  @Override
  protected void reset(AlgorithmGrid<DefaultGridState> grid) {
    super.reset(grid);
    queue = new ArrayDeque<>();
  }

  static class BreadthNode extends BaseNode<BreadthNode> {

    BreadthNode(double distanceToStart, GridCoordinate coordinate, BreadthNode parent) {
      super(distanceToStart, coordinate, null, parent);
    }
  }
}

package me.ialistannen.pathfinding.visualize.algorithms.bfsgreedy;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmResult;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.algorithms.base.BaseAlgorithm;
import me.ialistannen.pathfinding.visualize.algorithms.bfsgreedy.GreedyBFSAlgorithm.GreedyNode;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DistanceFunction;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public class GreedyBFSAlgorithm extends BaseAlgorithm<DefaultGridState, GreedyNode> {

  private PriorityQueue<GreedyNode> openNodes;
  private DistanceFunction distanceFunction;

  public GreedyBFSAlgorithm(List<Direction> directions, DistanceFunction distanceFunction) {
    super(directions);
    this.distanceFunction = distanceFunction;
  }

  @Override
  protected AlgorithmResult<DefaultGridState> compute() {
    for (GridCoordinate gridCoordinate : grid.getStarts()) {
      openNodes.add(new GreedyNode(gridCoordinate, grid.getEnd(), null));
    }

    GreedyNode tmp;
    while ((tmp = openNodes.poll()) != null) {
      GridCoordinate coordinate = tmp.getCoordinate();

      if (grid.getStateAt(coordinate).isEnd()) {
        break;
      }

      addStep(coordinate, DefaultGridState.EXAMINED);
      expand(tmp);
    }

    return backtrackResolve(tmp, DefaultGridState.SOLUTION);
  }

  private void expand(GreedyNode node) {
    markAsClosed(node.getCoordinate());

    for (Direction direction : directions) {
      GridCoordinate newCoordinate = node.getCoordinate().getNeighbour(direction);

      if (isClosed(newCoordinate) || !canMoveTo(node.getCoordinate(), newCoordinate)) {
        continue;
      }

      newNodeOrCached(
          newCoordinate,
          new GreedyNode(newCoordinate, node.getTarget(), node),
          newNode -> {
            openNodes.add(newNode);
            addStep(newCoordinate, DefaultGridState.OPEN_SET);
          }
      );
    }
  }

  @Override
  protected void reset(AlgorithmGrid<DefaultGridState> grid) {
    super.reset(grid);
    openNodes = new PriorityQueue<>(Comparator.comparingDouble(this::getHCost));
  }

  private double getHCost(GreedyNode node) {
    return distanceFunction.getDistance(node.getCoordinate(), node.getTarget());
  }

  static class GreedyNode extends BaseNode<GreedyNode> {

    GreedyNode(GridCoordinate coordinate, GridCoordinate target,
        GreedyNode parent) {
      super(0, coordinate, target, parent);
    }
  }
}

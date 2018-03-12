package me.ialistannen.pathfinding.visualize.algorithms.astar;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.algorithms.astar.AStarAlgorithm.AStarNode;
import me.ialistannen.pathfinding.visualize.algorithms.dijkstra.DijkstraBaseAlgorithm;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DefaultDistanceFunction;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DistanceFunction;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public class AStarAlgorithm extends DijkstraBaseAlgorithm<AStarNode> {

  private DistanceFunction distanceFunction;

  public AStarAlgorithm(DistanceFunction distanceFunction, List<Direction> directions) {
    super(directions);
    this.distanceFunction = distanceFunction;
  }

  @Override
  protected Comparator<AStarNode> createOpenSetComparator() {
    return Comparator.comparingDouble(this::getTotalDistance)
        .thenComparingDouble(this::getEuclideanDistanceToTarget)
        .thenComparingInt(Objects::hashCode);
  }

  private double getTotalDistance(AStarNode node) {
    return node.getDistanceToStart() + distanceFunction.getDistance(
        node.getCoordinate(), node.getTarget()
    );
  }

  private double getEuclideanDistanceToTarget(AStarNode node) {
    return DefaultDistanceFunction.EUCLIDEAN.getDistance(node.getCoordinate(), node.getTarget());
  }

  @Override
  protected AStarNode createStartNode(GridCoordinate coordinate,
      AlgorithmGrid<DefaultGridState> grid) {
    return new AStarNode(0, coordinate, grid.getEnd(), null);
  }

  @Override
  protected void expandImpl(AStarNode parent, Direction direction, GridCoordinate newCoordinate) {
    double newDistance = parent.getDistanceToStart() + direction.getCost();

    AStarNode node = getCachedOrCreate(
        newCoordinate,
        new AStarNode(newDistance, newCoordinate, parent.getTarget(), parent)
    );

    if (newDistance < node.getDistanceToStart()) {
      node.setParent(parent);
      node.setDistanceToStart(newDistance);
    }

    markNodeForExamination(node);
  }

  static class AStarNode extends BaseNode<AStarNode> {

    AStarNode(double distanceToStart, GridCoordinate coordinate, GridCoordinate target,
        AStarNode parent) {
      super(distanceToStart, coordinate, target, parent);
    }
  }
}

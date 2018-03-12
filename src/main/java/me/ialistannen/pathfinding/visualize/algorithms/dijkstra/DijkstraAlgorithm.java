package me.ialistannen.pathfinding.visualize.algorithms.dijkstra;

import java.util.Comparator;
import java.util.List;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.algorithms.dijkstra.DijkstraAlgorithm.DijkstraNode;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DefaultDistanceFunction;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public class DijkstraAlgorithm extends DijkstraBaseAlgorithm<DijkstraNode> {

  public DijkstraAlgorithm(List<Direction> directions) {
    super(directions);
  }

  @Override
  protected Comparator<DijkstraNode> createOpenSetComparator() {
    return Comparator.comparingDouble(DijkstraNode::getDistanceToStart)
        .thenComparingDouble(DijkstraNode::getDistanceToTarget)
        .thenComparing(System::identityHashCode);
  }

  @Override
  protected DijkstraNode createStartNode(GridCoordinate coordinate,
      AlgorithmGrid<DefaultGridState> grid) {
    return new DijkstraNode(0, coordinate, grid.getEnd(), null);
  }

  @Override
  protected void expandImpl(DijkstraNode parent, Direction direction,
      GridCoordinate newCoordinate) {

    double newDistance = parent.getDistanceToStart() + direction.getCost();
    DijkstraNode node = getCachedOrCreate(
        newCoordinate,
        new DijkstraNode(
            newDistance, newCoordinate, parent.getTarget(), parent
        )
    );

    if (newDistance < node.getDistanceToStart()) {
      node.setParent(parent);
      node.setDistanceToStart(newDistance);
    }

    markNodeForExamination(node);
  }

  static class DijkstraNode extends BaseNode<DijkstraNode> {

    private DijkstraNode(double distanceToStart, GridCoordinate coordinate, GridCoordinate target,
        DijkstraNode parent) {
      super(distanceToStart, coordinate, target, parent);
    }

    private double getDistanceToTarget() {
      return DefaultDistanceFunction.EUCLIDEAN.getDistance(getCoordinate(), getTarget());
    }
  }
}

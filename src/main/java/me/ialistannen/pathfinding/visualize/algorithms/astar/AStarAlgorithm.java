package me.ialistannen.pathfinding.visualize.algorithms.astar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmResult;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;
import me.ialistannen.pathfinding.visualize.grid.StatefulGridCoordinate;

public class AStarAlgorithm implements Algorithm<DefaultGridState> {

  private Map<GridCoordinate, AStarNode> nodeCache;
  private TreeSet<AStarNode> openSet;
  private Set<GridCoordinate> closedSet;
  private List<StatefulGridCoordinate<DefaultGridState>> steps;

  @Override
  public AlgorithmResult<DefaultGridState> compute(AlgorithmGrid<DefaultGridState> grid) {
    nodeCache = new HashMap<>();
    closedSet = new HashSet<>();
    openSet = new TreeSet<>(
        Comparator.comparingDouble(AStarNode::getTotalDistance)
            .thenComparingInt(Objects::hashCode)
    );
    steps = new ArrayList<>();

    for (GridCoordinate gridCoordinate : grid.getStarts()) {
      openSet.add(newNodeOrCached(gridCoordinate, 0, null, grid.getEnd()));
    }

    AStarNode tmp;
    while ((tmp = openSet.pollFirst()) != null) {
      GridCoordinate coordinate = tmp.getCoordinate();

      if (grid.getStateAt(coordinate) == DefaultGridState.END) {
        break;
      }

      expand(tmp, grid);

      addStep(coordinate, DefaultGridState.EXAMINED);
    }

    if (tmp == null || grid.getStateAt(tmp.getCoordinate()) != DefaultGridState.END) {
      return new AlgorithmResult<>(false, steps);
    }

    while (tmp.getParent() != null) {
      addStep(tmp.getCoordinate(), DefaultGridState.SOLUTION);
      tmp = tmp.getParent();
    }

    return new AlgorithmResult<>(true, steps);
  }

  private void addStep(GridCoordinate coordinate, DefaultGridState state) {
    steps.add(new StatefulGridCoordinate<>(coordinate, state));
  }

  private void expand(AStarNode node, AlgorithmGrid<DefaultGridState> grid) {
    closedSet.add(node.getCoordinate());

    for (Direction direction : Direction.values()) {
      GridCoordinate newCoordinate = node.getCoordinate().getNeighbour(direction);

      if (grid.isOutside(newCoordinate) || !closedSet.add(newCoordinate)) {
        continue;
      }

      if (grid.getStateAt(newCoordinate) == DefaultGridState.WALL) {
        continue;
      }

      double newDistance = direction.getCost() + node.getDistanceToStart();
      AStarNode newNode = newNodeOrCached(
          newCoordinate, newDistance, node, grid.getEnd()
      );

      if (newDistance < newNode.getDistanceToStart()) {
        newNode.setParent(node);
        newNode.setDistanceToStart(newDistance);
      }

      addStep(newCoordinate, DefaultGridState.OPEN_SET);

      openSet.add(newNode);
      nodeCache.put(newCoordinate, newNode);
    }
  }

  private AStarNode newNodeOrCached(GridCoordinate coordinate, double distanceToStart,
      AStarNode parent, GridCoordinate target) {
    return nodeCache
        .getOrDefault(coordinate, new AStarNode(coordinate, parent, distanceToStart, target));
  }

  private static class AStarNode {

    private GridCoordinate coordinate;
    private AStarNode parent;
    private double distanceToStart;
    private GridCoordinate target;

    public AStarNode(GridCoordinate coordinate, AStarNode parent, double distanceToStart,
        GridCoordinate target) {
      this.coordinate = Objects.requireNonNull(coordinate, "coordinate can not be null!");
      this.parent = parent;
      this.distanceToStart = distanceToStart;
      this.target = Objects.requireNonNull(target, "target can not be null!");
    }

    public GridCoordinate getCoordinate() {
      return coordinate;
    }

    public double getDistanceToStart() {
      return distanceToStart;
    }

    public void setDistanceToStart(double distanceToStart) {
      this.distanceToStart = distanceToStart;
    }

    public AStarNode getParent() {
      return parent;
    }

    public void setParent(AStarNode parent) {
      this.parent = parent;
    }

    public double getTotalDistance() {
      return distanceToStart + target.manhattenDistanceTo(coordinate);
    }

    public double getTotalDistanceEuclid() {
      return distanceToStart + target.euclidianDistanceTo(coordinate);
    }
  }
}

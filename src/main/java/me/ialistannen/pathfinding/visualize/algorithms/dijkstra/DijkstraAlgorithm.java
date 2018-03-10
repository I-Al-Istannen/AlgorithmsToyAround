package me.ialistannen.pathfinding.visualize.algorithms.dijkstra;

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

public class DijkstraAlgorithm implements Algorithm<DefaultGridState> {

  private Map<GridCoordinate, DijkstraNode> nodeCache;
  private TreeSet<DijkstraNode> openSet;
  private Set<DijkstraNode> closedSet;

  private List<StatefulGridCoordinate<DefaultGridState>> steps;

  @Override
  public AlgorithmResult<DefaultGridState> compute(AlgorithmGrid<DefaultGridState> grid) {
    openSet = new TreeSet<>(
        Comparator.comparingDouble(DijkstraNode::getDistance)
            .thenComparing(Objects::hashCode)
    );
    nodeCache = new HashMap<>();
    closedSet = new HashSet<>();
    steps = new ArrayList<>();

    openSet.add(new DijkstraNode(
        grid.getStarts().iterator().next(),
        0,
        null
    ));

    DijkstraNode tmp;
    while ((tmp = openSet.pollFirst()) != null) {
      DefaultGridState state = grid.getStateAt(tmp.coordinate);
      if (state == DefaultGridState.END) {
        break;
      }
      addStep(tmp.getCoordinate(), DefaultGridState.EXAMINED);

      expand(tmp, grid);
    }

    if (tmp == null || grid.getStateAt(tmp.getCoordinate()) != DefaultGridState.END) {
      return new AlgorithmResult<>(false, steps);
    }

    while (tmp.getParent() != null) {
      addStep(tmp.coordinate, DefaultGridState.SOLUTION);
      tmp = tmp.getParent();
    }

    return new AlgorithmResult<>(true, steps);
  }

  private void expand(DijkstraNode node, AlgorithmGrid<DefaultGridState> grid) {
    closedSet.add(node);

    for (Direction direction : Direction.values()) {
      GridCoordinate coordinate = node.getCoordinate().getNeighbour(direction);

      if (grid.isOutside(coordinate) || grid.getStateAt(coordinate) == DefaultGridState.WALL) {
        continue;
      }

      double newDistance = node.getDistance() + direction.getCost();
      DijkstraNode newNeighbour = newNode(newDistance, node, coordinate);

      if (closedSet.contains(newNeighbour)) {
        continue;
      }

      if (newNeighbour.getDistance() > newDistance) {
        newNeighbour.setDistance(newDistance);
        newNeighbour.setParent(node);
      }

      addStep(coordinate, DefaultGridState.OPEN_SET);

      openSet.add(newNeighbour);
      nodeCache.put(coordinate, newNeighbour);
    }
  }

  private DijkstraNode newNode(double distance, DijkstraNode parent, GridCoordinate coordinate) {
    return nodeCache.getOrDefault(coordinate, new DijkstraNode(coordinate, distance, parent));
  }

  private void addStep(GridCoordinate coordinate, DefaultGridState state) {
    steps.add(new StatefulGridCoordinate<>(coordinate, state));
  }

  private static class DijkstraNode {

    private GridCoordinate coordinate;
    private double distance;
    private DijkstraNode parent;

    DijkstraNode(GridCoordinate coordinate, double distance, DijkstraNode parent) {
      this.coordinate = coordinate;
      this.distance = distance;
      this.parent = parent;
    }

    double getDistance() {
      return distance;
    }

    void setDistance(double distance) {
      this.distance = distance;
    }

    GridCoordinate getCoordinate() {
      return coordinate;
    }

    DijkstraNode getParent() {
      return parent;
    }

    void setParent(DijkstraNode parent) {
      this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      DijkstraNode that = (DijkstraNode) o;
      return Objects.equals(coordinate, that.coordinate);
    }

    @Override
    public int hashCode() {
      return Objects.hash(coordinate);
    }

    @Override
    public String toString() {
      return "DijkstraNode{" +
          "coordinate=" + coordinate +
          ", distance=" + distance +
          '}';
    }
  }
}

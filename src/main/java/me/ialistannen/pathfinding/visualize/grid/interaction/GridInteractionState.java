package me.ialistannen.pathfinding.visualize.grid.interaction;

import java.util.Objects;
import javafx.scene.Node;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;

public class GridInteractionState<T extends GridCellState> {

  private GridCoordinate coordinate;
  private T state;
  private Node node;
  private AlgorithmGrid<T> grid;

  GridInteractionState(GridCoordinate coordinate, T state, Node node, AlgorithmGrid<T> grid) {
    this.coordinate = Objects.requireNonNull(coordinate, "coordinate can not be null!");
    this.state = Objects.requireNonNull(state, "state can not be null!");
    this.node = Objects.requireNonNull(node, "node can not be null!");
    this.grid = Objects.requireNonNull(grid, "grid can not be null!");
  }

  public GridCoordinate getCoordinate() {
    return coordinate;
  }

  public T getState() {
    return state;
  }

  public Node getNode() {
    return node;
  }

  public AlgorithmGrid<T> getGrid() {
    return grid;
  }

  @Override
  public String toString() {
    return "GridInteractionState{" +
        "coordinate=" + coordinate +
        ", state=" + state +
        '}';
  }
}


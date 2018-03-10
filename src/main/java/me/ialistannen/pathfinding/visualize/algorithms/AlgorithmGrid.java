package me.ialistannen.pathfinding.visualize.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;

public class AlgorithmGrid<T extends GridCellState> {

  private Map<GridCoordinate, T> values;
  private T defaultState;
  private GridCoordinate endState;
  private Set<GridCoordinate> startStates;

  private int width;
  private int height;

  public AlgorithmGrid(T defaultState, int width, int height) {
    this.defaultState = defaultState;
    this.width = width;
    this.height = height;

    this.values = new HashMap<>();
    this.startStates = new HashSet<>();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  /**
   * Returns the state at the given coordinate or the default value, if not set.
   *
   * @param coordinate the coordinate to get it at
   * @return the state at the coordinate or the default value
   */
  public T getStateAt(GridCoordinate coordinate) {
    return values.getOrDefault(coordinate, defaultState);
  }

  /**
   * Returns the state at the given coordinate or the default value, if not set.
   *
   * @param column the column
   * @param row the row
   * @return the state at the coordinate or the default value
   */
  public T getStateAt(int column, int row) {
    return getStateAt(new GridCoordinate(column, row));
  }

  /**
   * Sets the state at the given coordinate.
   *
   * @param coordinate the coordinate to set it at
   * @param state the new state
   */
  public void setStateAt(GridCoordinate coordinate, T state) {
    handleStateRemoval(coordinate);

    values.put(coordinate, state);

    handleStateAddition(coordinate, state);
  }

  /**
   * Sets the state at the given coordinate.
   *
   * @param column the column
   * @param row the row
   * @param state the new state
   */
  public void setStateAt(int column, int row, T state) {
    setStateAt(new GridCoordinate(column, row), state);
  }

  private void handleStateRemoval(GridCoordinate coordinate) {
    if (values.containsKey(coordinate)) {
      T old = getStateAt(coordinate);

      if (old.isStart()) {
        startStates.remove(coordinate);
      }

      if (old.isEnd()) {
        endState = null;
      }
    }
  }

  private void handleStateAddition(GridCoordinate coordinate, T state) {
    if (state.isEnd()) {
      endState = coordinate;
    }

    if (state.isStart()) {
      startStates.add(coordinate);
    }
  }

  /**
   * Returns the start coordinates.
   *
   * @return the start coordinates or an empty set, if none
   */
  public Set<GridCoordinate> getStarts() {
    return startStates;
  }

  /**
   * Returns the end to search for.
   *
   * @return The end coordinate or null if none
   */
  public GridCoordinate getEnd() {
    return endState;
  }
}

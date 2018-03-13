package me.ialistannen.pathfinding.visualize.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;

public class AlgorithmGrid<T extends GridCellState> {

  private Map<GridCoordinate, T> values;
  private T defaultState;
  private GridCoordinate endState;
  private Set<GridCoordinate> startStates;

  private ChangeCallback<T> changeCallback;

  private int width;
  private int height;

  public AlgorithmGrid(T defaultState, int width, int height) {
    this.defaultState = defaultState;
    this.width = width;
    this.height = height;

    this.values = new HashMap<>();
    this.startStates = new HashSet<>();
  }

  public void setChangeCallback(ChangeCallback<T> changeCallback) {
    this.changeCallback = changeCallback;
  }

  public T getDefaultState() {
    return defaultState;
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
  private T getStateAt(int column, int row) {
    return getStateAt(new GridCoordinate(column, row));
  }

  /**
   * Sets the state at the given coordinate.
   *
   * @param coordinate the coordinate to set it at
   * @param state the new state
   */
  public void setStateAt(GridCoordinate coordinate, T state) {
    Objects.requireNonNull(coordinate, "coordinate can not be null!");
    Objects.requireNonNull(state, "state can not be null!");

    handleStateRemoval(coordinate);

    T oldState = getStateAt(coordinate);

    values.put(coordinate, state);

    handleStateAddition(coordinate, state);

    if (changeCallback != null) {
      changeCallback.onStateChanged(coordinate, oldState, state);
    }
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

  /**
   * Replaces all states that match the predicate with the passed one.
   *
   * @param replacement the replacement state
   * @param filter the filter
   */
  public void replaceIf(T replacement, Predicate<T> filter) {
    for (int column = 0; column < getWidth(); column++) {
      for (int row = 0; row < getHeight(); row++) {
        if (filter.test(getStateAt(column, row))) {
          setStateAt(column, row, replacement);
        }
      }
    }
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
      if (endState != null) {
        setStateAt(endState, defaultState);
      }
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

  /**
   * Checks if you can move from the given coordinate to the given neighbour.
   *
   * @param from the start coordinate
   * @param to the neighbour to move to
   * @return true if you can move that way
   */
  public boolean canMove(GridCoordinate from, GridCoordinate to) {
    if (isOutside(from) || isOutside(to)) {
      return false;
    }

    T state = getStateAt(to);
    if (!state.isPassable()) {
      return false;
    }

    int dX = to.getColumn() - from.getColumn();
    int dY = to.getRow() - from.getRow();

    return getStateAt(from.getColumn() + dX, from.getRow()).isPassable()
        || getStateAt(from.getColumn(), from.getRow() + dY).isPassable();
  }

  /**
   * Checks if a coordinate is outside the grid.
   *
   * @param coordinate the {@link GridCoordinate} to check
   * @return true if the coordinate is outside the grid
   */
  private boolean isOutside(GridCoordinate coordinate) {
    return coordinate.getColumn() >= getWidth() || coordinate.getRow() >= getHeight()
        || coordinate.getColumn() < 0 || coordinate.getRow() < 0;
  }

  /**
   * Checks if you can not move through a specified coordinate.
   *
   * @param coordinate the {@link GridCoordinate} to check
   * @return true if the coordinate is blocked
   */
  public boolean isBlocked(GridCoordinate coordinate) {
    return !getStateAt(coordinate).isPassable();
  }


  public interface ChangeCallback<T> {

    /**
     * Called when a state was changed.
     *
     * @param coordinate the coordinate that was changed
     * @param oldState the old state
     * @param newState the new state
     */
    void onStateChanged(GridCoordinate coordinate, T oldState, T newState);
  }
}

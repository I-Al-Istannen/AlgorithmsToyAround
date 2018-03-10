package me.ialistannen.pathfinding.visualize.grid;

import java.util.Objects;

public class StatefulGridCoordinate<T extends GridCellState> {

  private GridCoordinate coordinate;
  private T state;

  public StatefulGridCoordinate(GridCoordinate coordinate, T state) {
    this.coordinate = coordinate;
    this.state = state;
  }

  public GridCoordinate getCoordinate() {
    return coordinate;
  }

  public T getState() {
    return state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatefulGridCoordinate<?> that = (StatefulGridCoordinate<?>) o;
    return Objects.equals(coordinate, that.coordinate) &&
        Objects.equals(state, that.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinate, state);
  }

  @Override
  public String toString() {
    return "StatefulGridCoordinate{" +
        "coordinate=" + coordinate +
        ", state=" + state +
        '}';
  }
}

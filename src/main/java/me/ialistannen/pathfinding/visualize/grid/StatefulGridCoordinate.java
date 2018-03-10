package me.ialistannen.pathfinding.visualize.grid;

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
}

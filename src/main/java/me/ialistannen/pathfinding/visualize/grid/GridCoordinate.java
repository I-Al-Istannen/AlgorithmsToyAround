package me.ialistannen.pathfinding.visualize.grid;

import java.util.Objects;

public class GridCoordinate {

  private int column;
  private int row;

  public GridCoordinate(int column, int row) {
    this.column = column;
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public int getRow() {
    return row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GridCoordinate that = (GridCoordinate) o;
    return column == that.column &&
        row == that.row;
  }

  @Override
  public int hashCode() {

    return Objects.hash(column, row);
  }
}

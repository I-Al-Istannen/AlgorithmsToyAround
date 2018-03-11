package me.ialistannen.pathfinding.visualize.grid;

import java.util.Arrays;
import java.util.List;
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

  public GridCoordinate getNeighbour(Direction direction) {
    return new GridCoordinate(
        getColumn() + direction.getxMod(),
        getRow() + direction.getyMod()
    );
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

  @Override
  public String toString() {
    return "GridCoordinate{" +
        "column=" + column +
        ", row=" + row +
        '}';
  }

  public enum Direction {
    NORTH(0, 1, 1), SOUTH(0, -1, 1), WEST(-1, 0, 1), EAST(1, 0, 1),
    NORTH_EAST(1, 1, Math.sqrt(2)), SOUTH_EAST(1, -1, Math.sqrt(2)),
    SOUTH_WEST(-1, -1, Math.sqrt(2)), NORTH_WEST(-1, 1, Math.sqrt(2));

    public static List<Direction> NO_DIAGONAL = Arrays.asList(NORTH, SOUTH, EAST, WEST);
    public static List<Direction> WITH_DIAGONAL = Arrays.asList(Direction.values());

    private int xMod;
    private int yMod;
    private double cost;

    Direction(int xMod, int yMod, double cost) {
      this.xMod = xMod;
      this.yMod = yMod;
      this.cost = cost;
    }

    public int getxMod() {
      return xMod;
    }

    public int getyMod() {
      return yMod;
    }

    public double getCost() {
      return cost;
    }
  }
}

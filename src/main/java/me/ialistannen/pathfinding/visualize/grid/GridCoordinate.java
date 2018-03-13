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

  /**
   * Returns a neighbouring coordinate.
   *
   * @param direction the direction to walk into.
   * @return the neighbouring coordinate
   */
  public GridCoordinate getNeighbour(Direction direction) {
    return new GridCoordinate(
        getColumn() + direction.getxMod(),
        getRow() + direction.getyMod()
    );
  }

  /**
   * Returns a new {@link GridCoordinate} with the given values added to this coordinate.
   *
   * @param column the columns to add
   * @param row the rows to add
   * @return the new coordinate
   */
  public GridCoordinate add(int column, int row) {
    return new GridCoordinate(getColumn() + column, getRow() + row);
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
    NORTH(0, -1, 1), SOUTH(0, 1, 1), WEST(-1, 0, 1), EAST(1, 0, 1),
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

    /**
     * @return the opposite direction
     */
    public Direction opposite() {
      return forMod(getxMod() * -1, getyMod() * -1);
    }

    public boolean isDiagonal() {
      return xMod != 0 && yMod != 0;
    }

    /**
     * Returns the {@link Direction} for the given modifies
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the direction or null if none matched
     */
    public static Direction forMod(int x, int y) {
      return Arrays
          .stream(values())
          .filter(direction ->
              direction.getxMod() == Math.signum(x) && direction.getyMod() == Math.signum(y)
          )
          .findFirst()
          .orElse(null);
    }
  }
}

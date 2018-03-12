package me.ialistannen.pathfinding.visualize.algorithms.distance;

import java.util.function.BiFunction;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;

public enum DefaultDistanceFunction implements DistanceFunction {
  MANHATTEN((one, two) -> {
    double dX = one.getColumn() - two.getColumn();
    double dY = one.getRow() - two.getRow();

    return Math.abs(dX) + Math.abs(dY);
  }),
  EUCLIDEAN((one, two) -> {
    double dX = one.getColumn() - two.getColumn();
    double dY = one.getRow() - two.getRow();

    return Math.sqrt(dX * dX + dY * dY);
  }),
  CHEBYSHEV((one, two) -> {
    double dX = one.getColumn() - two.getColumn();
    double dY = one.getRow() - two.getRow();

    return Math.max(Math.abs(dX), Math.abs(dY));
  });

  private BiFunction<GridCoordinate, GridCoordinate, Double> function;

  DefaultDistanceFunction(BiFunction<GridCoordinate, GridCoordinate, Double> function) {
    this.function = function;
  }

  @Override
  public double getDistance(GridCoordinate from, GridCoordinate to) {
    return function.apply(from, to);
  }
}

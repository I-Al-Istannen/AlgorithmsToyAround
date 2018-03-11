package me.ialistannen.pathfinding.visualize.algorithms.distance;

import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;

public interface DistanceFunction {

  /**
   * Computes the distance between two nodes.
   *
   * @param from the {@link GridCoordinate} to walk from
   * @param to the {@link GridCoordinate} to walk to
   * @return the distance between the two
   */
  double getDistance(GridCoordinate from, GridCoordinate to);
}

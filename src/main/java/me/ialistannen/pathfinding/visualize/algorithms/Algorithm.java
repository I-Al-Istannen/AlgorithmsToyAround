package me.ialistannen.pathfinding.visualize.algorithms;

import java.util.List;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public interface Algorithm<T extends GridCellState> {

  /**
   * Applies to algorithm to the passed grid.
   *
   * @param grid the grid to apply it to
   * @param directions a list with directions the algorithm can walk along
   * @return the computed result
   */
  AlgorithmResult<T> compute(AlgorithmGrid<T> grid, List<Direction> directions);

}

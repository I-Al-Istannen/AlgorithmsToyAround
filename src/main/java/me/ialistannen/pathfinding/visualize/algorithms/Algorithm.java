package me.ialistannen.pathfinding.visualize.algorithms;

import me.ialistannen.pathfinding.visualize.grid.DisplayedGrid;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;

public interface Algorithm<T extends GridCellState> {

  /**
   * Applies to algorithm to the passed grid.
   *
   * @param grid the grid to apply it to
   * @return the computed result
   */
  AlgorithmResult compute(DisplayedGrid<T> grid);

}

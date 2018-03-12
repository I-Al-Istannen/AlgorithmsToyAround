package me.ialistannen.pathfinding.visualize.algorithms.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmResult;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;
import me.ialistannen.pathfinding.visualize.grid.StatefulGridCoordinate;

public abstract class BaseAlgorithm<T extends GridCellState, U extends BaseNode<U>> implements
    Algorithm<T> {

  private Map<GridCoordinate, U> nodeCache;
  private Set<GridCoordinate> closedSet;
  protected AlgorithmGrid<T> grid;
  private List<StatefulGridCoordinate<T>> steps;
  protected List<Direction> directions;

  public BaseAlgorithm(List<Direction> directions) {
    this.directions = new ArrayList<>(directions);
  }

  @Override
  public AlgorithmResult<T> compute(AlgorithmGrid<T> grid) {
    reset(grid);

    return compute();
  }

  /**
   * Runs this actual algorithm. Reset will be called before this and all fields set to the right
   * values.
   *
   * @return the resulting {@link AlgorithmResult}.
   */
  protected abstract AlgorithmResult<T> compute();

  /**
   * Rests this algorithm. You need to call super.
   *
   * @param grid the grid
   */
  protected void reset(AlgorithmGrid<T> grid) {
    this.nodeCache = new HashMap<>();
    this.closedSet = new HashSet<>();
    this.steps = new ArrayList<>();
    this.grid = grid;
  }


  /**
   * Returns a new node or a cached version.
   *
   * @param coordinate the coordinate to get it for
   * @param defaultNode the default node to cache, if the cache was a miss
   * @param newNodeConsumer the consumer to be invoked when a new node was created
   * @return the newly created node, or a cached one at the same position
   */
  protected U newNodeOrCached(GridCoordinate coordinate, U defaultNode,
      Consumer<U> newNodeConsumer) {
    if (nodeCache.containsKey(coordinate)) {
      return nodeCache.get(coordinate);
    }

    nodeCache.put(coordinate, defaultNode);

    newNodeConsumer.accept(defaultNode);

    return defaultNode;
  }

  /**
   * Resolves the path to the start via backtracking.
   *
   * @param node the node to start at
   * @param solutionState the state to use for the solution
   * @return the created {@link AlgorithmResult}. Can handle nulls and failures.
   */
  protected AlgorithmResult<T> backtrackResolve(U node, T solutionState) {
    if (node == null || !grid.getStateAt(node.getCoordinate()).isEnd()) {
      return new AlgorithmResult<>(false, steps);
    }

    while (node != null) {
      addStep(node.getCoordinate(), solutionState);
      node = node.getParent();
    }

    return new AlgorithmResult<>(true, steps);
  }

  /**
   * Makrs a given {@link GridCoordinate} as closed.
   *
   * @param coordinate the coordinate to mark as closed
   */
  protected void markAsClosed(GridCoordinate coordinate) {
    closedSet.add(coordinate);
  }

  /**
   * Checks if a {@link GridCoordinate} is marked as closed.
   *
   * @param coordinate the coordinate to check for
   * @return true if the coordinate is closed
   */
  protected boolean isClosed(GridCoordinate coordinate) {
    return closedSet.contains(coordinate);
  }

  /**
   * Adds a new step.
   *
   * @param coordinate the coordinate to add it for
   * @param state the state the cell had at this point
   */
  protected void addStep(GridCoordinate coordinate, T state) {
    steps.add(new StatefulGridCoordinate<>(coordinate, state));
  }

  /**
   * Checks if you can move from the given coordinate to the given neighbour.
   *
   * @param from the start coordinate
   * @param to the neighbour to move to
   * @return true if you can move that way
   */
  protected boolean canMoveTo(GridCoordinate from, GridCoordinate to) {
    return grid.canMove(from, to);
  }
}

package me.ialistannen.pathfinding.visualize.algorithms.dijkstra;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmResult;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.algorithms.base.BaseAlgorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public abstract class DijkstraBaseAlgorithm<T extends BaseNode<T>> extends
    BaseAlgorithm<DefaultGridState, T> {

  private final Consumer<T> DO_NOTHING = t -> {
  };

  private TreeSet<T> openSet;

  public DijkstraBaseAlgorithm(List<Direction> directions) {
    super(directions);
  }

  @Override
  protected AlgorithmResult<DefaultGridState> compute() {
    grid.getStarts().stream()
        .map(coordinate -> createStartNode(coordinate, grid))
        .forEach(openSet::add);

    T tmp;
    while ((tmp = openSet.pollFirst()) != null) {
      GridCoordinate coordinate = tmp.getCoordinate();
      if (isEnd(coordinate)) {
        break;
      }

      addStep(coordinate, DefaultGridState.EXAMINED);

      expand(tmp);
    }

    return backtrackResolve(tmp, DefaultGridState.SOLUTION);
  }

  /**
   * Resets this {@link Algorithm} to be able to search again.
   */
  protected void reset(AlgorithmGrid<DefaultGridState> algorithmGrid) {
    super.reset(algorithmGrid);

    this.openSet = new TreeSet<>(createOpenSetComparator());
  }

  /**
   * Creates the {@link Comparator} to use for the open set.
   *
   * @return the {@link Comparator} to use
   */
  protected abstract Comparator<T> createOpenSetComparator();

  /**
   * Creates a new initial start node.
   *
   * @param coordinate the coordinate the start node is at
   * @param grid the {@link AlgorithmGrid} that is being used
   * @return the created start node
   */
  protected abstract T createStartNode(GridCoordinate coordinate,
      AlgorithmGrid<DefaultGridState> grid);

  /**
   * Checks if a given node is an end node.
   *
   * @param coordinate the coordinate to check at
   * @return true if the node is an end node.
   */
  private boolean isEnd(GridCoordinate coordinate) {
    Objects.requireNonNull(coordinate, "coordinate can not be null!");

    return grid.getStateAt(coordinate).isEnd();
  }

  /**
   * Checks if you can walk through the block at the given coordinate.
   *
   * @param from the coordinate you come from
   * @param to the coordinate you walk to
   * @return true if you can walk that way
   */
  @SuppressWarnings("WeakerAccess")
  protected boolean isPassable(GridCoordinate from, GridCoordinate to) {
    Objects.requireNonNull(from, "from can not be null!");
    Objects.requireNonNull(to, "to can not be null!");

    return grid.canMove(from, to);
  }

  /**
   * Expands the selection by visiting the given node.
   *
   * @param node the node to visit
   */
  @SuppressWarnings("WeakerAccess")
  protected void expand(T node) {
    Objects.requireNonNull(node, "node can not be null!");

    markAsClosed(node.getCoordinate());

    for (Direction direction : directions) {
      GridCoordinate newCoordinate = node.getCoordinate().getNeighbour(direction);

      if (!isPassable(node.getCoordinate(), newCoordinate) || isClosed(newCoordinate)) {
        continue;
      }

      expandImpl(node, direction, newCoordinate);
    }
  }

  /**
   * Expands this algorithm by creating and adding the neighbouring node.
   *
   * @param parent the parent node
   * @param direction the {@link Direction} it went
   * @param newCoordinate the {@link GridCoordinate} of the new node
   */
  protected abstract void expandImpl(T parent, Direction direction, GridCoordinate newCoordinate);

  /**
   * Returns a cached node or adds the passed default to the cache and returns it.
   *
   * @param coordinate the coordinate the node is at
   * @param defaultNode the default node to add, if there is no node already cached
   * @return the resulting node
   */
  protected T getCachedOrCreate(GridCoordinate coordinate, T defaultNode) {
    Objects.requireNonNull(coordinate, "coordinate can not be null!");
    Objects.requireNonNull(defaultNode, "defaultNode can not be null!");

    return newNodeOrCached(coordinate, defaultNode, DO_NOTHING);
  }

  /**
   * Marks this node for examination, by adding it to the open set.
   *
   * @param node the node to mark
   */
  protected void markNodeForExamination(T node) {
    Objects.requireNonNull(node, "node can not be null!");

    // only add a step for newly found nodes
    if (openSet.add(node)) {
      addStep(node.getCoordinate(), DefaultGridState.OPEN_SET);
    }
  }
}

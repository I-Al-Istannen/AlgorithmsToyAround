package me.ialistannen.pathfinding.visualize.algorithms.dijkstra;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmResult;
import me.ialistannen.pathfinding.visualize.algorithms.BaseNode;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;
import me.ialistannen.pathfinding.visualize.grid.StatefulGridCoordinate;

public abstract class DijkstraBaseAlgorithm<T extends BaseNode<T>>
    implements Algorithm<DefaultGridState> {

  private Map<GridCoordinate, T> nodeCache;
  private Set<GridCoordinate> closedSet;
  private TreeSet<T> openSet;
  private List<StatefulGridCoordinate<DefaultGridState>> steps;
  private AlgorithmGrid<DefaultGridState> grid;
  private List<Direction> directions;

  public DijkstraBaseAlgorithm(List<Direction> directions) {
    this.directions = directions;
  }

  @Override
  public AlgorithmResult<DefaultGridState> compute(AlgorithmGrid<DefaultGridState> grid) {
    reset(grid);

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

    return backtrackResolve(tmp);
  }

  /**
   * Resets this {@link Algorithm} to be able to search again.
   */
  protected void reset(AlgorithmGrid<DefaultGridState> algorithmGrid) {
    this.nodeCache = new HashMap<>();
    this.closedSet = new HashSet<>();
    this.steps = new ArrayList<>();
    this.openSet = new TreeSet<>(createOpenSetComparator());
    this.grid = Objects.requireNonNull(algorithmGrid, "algorithmGrid can not be null!");
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
  protected boolean isEnd(GridCoordinate coordinate) {
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
  protected boolean isPassable(GridCoordinate from, GridCoordinate to) {
    Objects.requireNonNull(from, "from can not be null!");
    Objects.requireNonNull(to, "to can not be null!");

    return grid.getStateAt(to).isPassable() && !grid.isOutside(to);
  }

  /**
   * Expands the selection by visiting the given node.
   *
   * @param node the node to visit
   */
  protected void expand(T node) {
    Objects.requireNonNull(node, "node can not be null!");

    closedSet.add(node.getCoordinate());

    for (Direction direction : directions) {
      GridCoordinate newCoordinate = node.getCoordinate().getNeighbour(direction);

      if (!isPassable(node.getCoordinate(), newCoordinate) || closedSet.contains(newCoordinate)) {
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

    if (nodeCache.containsKey(coordinate)) {
      return nodeCache.get(coordinate);
    }
    nodeCache.put(coordinate, defaultNode);

    return nodeCache.get(coordinate);
  }

  /**
   * Marks this node for examination, by adding it to the open set.
   *
   * @param node the node to mark
   */
  protected void markNodeForExamination(T node) {
    Objects.requireNonNull(node, "node can not be null!");

    openSet.add(node);
    addStep(node.getCoordinate(), DefaultGridState.OPEN_SET);
  }

  /**
   * Resolves the way back, from the passed node to the start and adds them to steps in reverse
   * order.
   *
   * @param current the current node, i.e. the last one that was examined (typically the end node)
   * @return the resulting {@link AlgorithmResult}
   */
  protected AlgorithmResult<DefaultGridState> backtrackResolve(T current) {
    if (current == null || !grid.getStateAt(current.getCoordinate()).isEnd()) {
      return new AlgorithmResult<>(false, steps);
    }

    while (current != null) {
      addStep(current.getCoordinate(), DefaultGridState.SOLUTION);
      current = current.getParent();
    }
    return new AlgorithmResult<>(true, steps);
  }

  /**
   * Adds a step to the output result.
   *
   * @param coordinate the coordinate that changed
   * @param state the new state
   */
  protected void addStep(GridCoordinate coordinate, DefaultGridState state) {
    Objects.requireNonNull(coordinate, "coordinate can not be null!");
    Objects.requireNonNull(state, "state can not be null!");

    steps.add(new StatefulGridCoordinate<>(coordinate, state));
  }
}

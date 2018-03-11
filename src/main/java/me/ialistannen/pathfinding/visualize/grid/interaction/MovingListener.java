package me.ialistannen.pathfinding.visualize.grid.interaction;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;

/**
 * Allows you to move a {@link me.ialistannen.pathfinding.visualize.grid.GridCellState} around.
 */
class MovingListener implements GridInteractionListener<DefaultGridState> {

  private Set<DefaultGridState> whitelist;

  private GridInteractionState<DefaultGridState> start;
  private GridCoordinate previous;

  MovingListener(DefaultGridState... whitelist) {
    this.whitelist = new HashSet<>(Arrays.asList(whitelist));
  }

  MovingListener(Collection<DefaultGridState> whitelist) {
    this.whitelist = new HashSet<>(whitelist);
  }

  @Override
  public void onClick(GridInteractionState<DefaultGridState> state) {
  }

  @Override
  public void onDragStart(GridInteractionState<DefaultGridState> state) {
    if (isOnWhitelist(state)) {
      start = state;
      previous = state.getCoordinate();
    }
  }

  @Override
  public void onDragOver(GridInteractionState<DefaultGridState> state,
      GridInteractionState<DefaultGridState> dragState) {

    if (start == null) {
      return;
    }

    AlgorithmGrid<DefaultGridState> grid = state.getGrid();

    if (canNotOverwriteState(grid.getStateAt(state.getCoordinate()))) {
      return;
    }

    if (previous != null) {
      grid.setStateAt(previous, DefaultGridState.EMPTY);
    }

    grid.setStateAt(state.getCoordinate(), start.getState());

    previous = state.getCoordinate();
  }

  @Override
  public void onDragStop(GridInteractionState<DefaultGridState> state,
      GridInteractionState<DefaultGridState> dragState) {

    if (start == null) {
      return;
    }

    AlgorithmGrid<DefaultGridState> grid = state.getGrid();

    if (canNotOverwriteState(state.getState()) && state.getState() != start.getState()) {
      // move back to start, we can't place it anywhere
      if (previous == null) {
        grid.setStateAt(start.getCoordinate(), start.getState());
      } else {
        grid.setStateAt(previous, start.getState());
      }
      return;
    }

    grid.setStateAt(state.getCoordinate(), start.getState());

    start = null;
  }

  @Override
  public boolean suppressDuplicateDragEvents() {
    return false;
  }

  private boolean isOnWhitelist(GridInteractionState<DefaultGridState> state) {
    return whitelist.contains(state.getState());
  }

  private boolean isOnWhitelist(DefaultGridState state) {
    return whitelist.contains(state);
  }

  private boolean canNotOverwriteState(DefaultGridState state) {
    return state == DefaultGridState.WALL || isOnWhitelist(state);
  }
}

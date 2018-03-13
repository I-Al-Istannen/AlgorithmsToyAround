package me.ialistannen.pathfinding.visualize.grid.interaction;

import java.util.EnumSet;
import java.util.Set;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;

/**
 * A {@link GridInteractionListener} that allows to paint the grid.
 */
public class PaintingGridListener implements GridInteractionListener<DefaultGridState> {

  private Set<DefaultGridState> movableStates = EnumSet.of(
      DefaultGridState.START, DefaultGridState.END
  );

  private GridInteractionListener<DefaultGridState> currentListener;

  @Override
  public void onClick(GridInteractionState<DefaultGridState> state) {
    if (movableStates.contains(state.getState())) {
      return;
    }
    if (state.getState() == DefaultGridState.WALL) {
      state.getGrid().setStateAt(state.getCoordinate(), DefaultGridState.EMPTY);
    } else {
      state.getGrid().setStateAt(state.getCoordinate(), DefaultGridState.WALL);
    }
  }

  @Override
  public void onDragStart(GridInteractionState<DefaultGridState> state) {
    DefaultGridState gridState = state.getState();

    if (currentListener == null) {
      if (movableStates.contains(gridState)) {
        currentListener = new MovingListener(movableStates);
      } else {
        DefaultGridState newState = gridState == DefaultGridState.WALL
            ? DefaultGridState.EMPTY
            : DefaultGridState.WALL;
        currentListener = new PaintOneStateListener(newState);
      }
    }
    currentListener.onDragStart(state);
  }

  @Override
  public void onDragOver(GridInteractionState<DefaultGridState> state,
      GridInteractionState<DefaultGridState> dragState) {
    if (currentListener != null) {
      currentListener.onDragOver(state, dragState);
    }
  }

  @Override
  public void onDragStop(GridInteractionState<DefaultGridState> state,
      GridInteractionState<DefaultGridState> dragState) {
    if (currentListener != null) {
      currentListener.onDragStop(state, dragState);
    }

    currentListener = null;
  }

  @Override
  public boolean suppressDuplicateDragEvents() {
    return currentListener == null || currentListener.suppressDuplicateDragEvents();
  }
}
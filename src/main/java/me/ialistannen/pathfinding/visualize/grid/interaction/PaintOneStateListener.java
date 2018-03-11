package me.ialistannen.pathfinding.visualize.grid.interaction;

import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;

/**
 * Just paints in one state.
 */
class PaintOneStateListener implements GridInteractionListener<DefaultGridState> {

  private DefaultGridState targetState;

  public PaintOneStateListener(DefaultGridState state) {
    this.targetState = state;
  }

  @Override
  public void onClick(GridInteractionState<DefaultGridState> state) {
    if (isPaintable(state.getState())) {
      paint(state);
    }
  }

  private static boolean isPaintable(DefaultGridState state) {
    return state != DefaultGridState.END && state != DefaultGridState.START;
  }


  @Override
  public void onDragStart(GridInteractionState<DefaultGridState> state) {
    if (isPaintable(state.getState())) {
      paint(state);
    }
  }

  @Override
  public void onDragOver(GridInteractionState<DefaultGridState> state,
      GridInteractionState<DefaultGridState> dragState) {

    if (isPaintable(state.getState())) {
      paint(state);
    }
  }

  @Override
  public void onDragStop(GridInteractionState<DefaultGridState> state,
      GridInteractionState<DefaultGridState> dragState) {
  }

  @Override
  public boolean suppressDuplicateDragEvents() {
    return true;
  }

  private void paint(GridInteractionState<DefaultGridState> state) {
    state.getGrid().setStateAt(state.getCoordinate(), targetState);
  }
}

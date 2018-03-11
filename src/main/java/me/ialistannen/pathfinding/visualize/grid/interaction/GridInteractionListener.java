package me.ialistannen.pathfinding.visualize.grid.interaction;

import me.ialistannen.pathfinding.visualize.grid.GridCellState;

public interface GridInteractionListener<T extends GridCellState> {

  /**
   * Called when a cell in the grid is clicked.
   *
   * @param state the state containing information about the event
   */
  void onClick(GridInteractionState<T> state);

  /**
   * Called when a node in a grid is being dragged.
   *
   * @param state the state containing information about the event
   */
  void onDragStart(GridInteractionState<T> state);

  /**
   * Called when a node is being dragged over.
   *
   * @param state the state containing information about the event
   * @param dragState constains information about the drag origin
   */
  void onDragOver(GridInteractionState<T> state, GridInteractionState<T> dragState);

  /**
   * Called when the drag has ended.
   *
   * @param state the state containing information about the event
   * @param dragState constains information about the drag origin
   */
  void onDragStop(GridInteractionState<T> state, GridInteractionState<T> dragState);

  /**
   * Defines whether duplicate events (basically millions per second...) should be suppressed.
   *
   * @return true if this listener should not receive two consecutive drag events for the same
   */
  boolean suppressDuplicateDragEvents();
}


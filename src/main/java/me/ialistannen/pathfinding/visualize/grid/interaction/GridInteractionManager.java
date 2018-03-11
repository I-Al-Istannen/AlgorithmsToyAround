package me.ialistannen.pathfinding.visualize.grid.interaction;

import java.util.HashSet;
import java.util.Set;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;

public class GridInteractionManager<T extends GridCellState> {

  private static final String DRAG_KEY = "MyDragKey";

  private AlgorithmGrid<T> grid;
  private GridInteractionListener<T> interactionListener;
  private GridInteractionState<T> dragStartState;

  private Set<GridCoordinate> visitedInDrag;

  public GridInteractionManager(AlgorithmGrid<T> grid) {
    this.grid = grid;

    this.visitedInDrag = new HashSet<>();
  }

  public void setInteractionListener(GridInteractionListener<T> interactionListener) {
    this.interactionListener = interactionListener;
  }

  public void configureNode(Node node) {
    node.setOnMouseClicked(event -> {
      GridInteractionState<T> state = getStateFromEvent(event);

      if (state == null) {
        return;
      }

      interactionListener.onClick(state);
    });

    node.setOnDragDetected(event -> {
      dragStartState = getStateFromEvent(event);

      if (dragStartState == null) {
        return;
      }

      startDragAndDrop(node);

      interactionListener.onDragStart(dragStartState);
      visitedInDrag = new HashSet<>();
    });
    node.setOnDragOver(event -> {
      GridInteractionState<T> state = getStateFromEvent(event);

      if (state == null || dragStartState == null) {
        return;
      }

      acceptDragAndDrop(node, event);
    });
    node.setOnDragEntered(event -> {
      GridInteractionState<T> state = getStateFromEvent(event);

      if (state == null || dragStartState == null) {
        return;
      }

      if (interactionListener.suppressDuplicateDragEvents()
          && !visitedInDrag.add(state.getCoordinate())) {
        return;
      }

      interactionListener.onDragOver(state, dragStartState);
    });
    node.setOnDragDropped(event -> {
      GridInteractionState<T> state = getStateFromEvent(event);

      if (state == null || dragStartState == null) {
        return;
      }

      interactionListener.onDragStop(state, dragStartState);
    });
  }

  private GridInteractionState<T> getStateFromEvent(Event event) {
    if (!(event.getSource() instanceof Node) || interactionListener == null) {
      return null;
    }
    Node clicked = (Node) event.getSource();
    GridCoordinate coordinate = getGridCoordinateForChild(clicked);

    if (coordinate == null) {
      return null;
    }
    return new GridInteractionState<>(coordinate, grid.getStateAt(coordinate), clicked, grid);
  }

  private GridCoordinate getGridCoordinateForChild(Node child) {
    Integer column = GridPane.getColumnIndex(child);
    Integer row = GridPane.getRowIndex(child);

    if (row == null || column == null) {
      return null;
    }

    return new GridCoordinate(column, row);
  }

  private void startDragAndDrop(Node node) {
    Dragboard dragboard = node.startDragAndDrop(TransferMode.ANY);
    ClipboardContent content = new ClipboardContent();
    content.putString(DRAG_KEY);
    dragboard.setContent(content);
  }

  private void acceptDragAndDrop(Node node, DragEvent event) {
    if (event.getGestureSource() == node || !DRAG_KEY.equals(event.getDragboard().getString())) {
      return;
    }

    event.acceptTransferModes(TransferMode.ANY);
  }
}

package me.ialistannen.pathfinding.visualize.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;

public class DisplayedGrid<T extends GridCellState> extends GridPane {

  private ClickListener<T> clickListener;
  private AlgorithmGrid<T> grid;

  private Map<GridCoordinate, Node> nodeMap;

  private Set<GridCoordinate> visitedCoordinatesInDrag;

  public DisplayedGrid(AlgorithmGrid<T> grid) {
    this.grid = grid;

    this.nodeMap = new HashMap<>();
    this.visitedCoordinatesInDrag = new HashSet<>();

    initializeGrid(grid.getWidth(), grid.getHeight());

    grid.setChangeCallback((coordinate, oldState, newState) -> setCellState(coordinate, newState));
  }

  private void initializeGrid(int columns, int rows) {
    getRowConstraints().clear();
    getColumnConstraints().clear();

    List<RowConstraints> rowConstraints = new ArrayList<>(rows);
    for (int i = 0; i < rows; i++) {
      RowConstraints rowConstraint = new RowConstraints();
      rowConstraint.setFillHeight(true);
      rowConstraint.setPercentHeight(100D / rows);
      rowConstraints.add(rowConstraint);
    }

    getRowConstraints().setAll(rowConstraints);

    List<ColumnConstraints> columnConstraints = new ArrayList<>(rows);
    for (int i = 0; i < columns; i++) {
      ColumnConstraints columnConstraint = new ColumnConstraints();
      columnConstraint.setFillWidth(true);
      columnConstraint.setPercentWidth(100D / columns);
      columnConstraints.add(columnConstraint);
    }

    getColumnConstraints().setAll(columnConstraints);
  }

  public void setClickListener(ClickListener<T> clickListener) {
    this.clickListener = clickListener;

    for (Node node : getChildren()) {
      node.setOnMouseClicked(this::handleChildMouseEvent);
      addDragListener(node);
    }
  }

  public Node getNodeAt(GridCoordinate coordinate) {
    return nodeMap.get(coordinate);
  }

  /**
   * Adds a cell to this grid.
   *
   * @param coordinate the coordinate to change
   * @param state the state to add
   * @return the node created by {@link GridCellState#getNode()}
   */
  private Node setCellState(GridCoordinate coordinate, T state) {
    Node node = state.getNode();

    node.setOnMouseClicked(this::handleChildMouseEvent);
    addDragListener(node);

    getChildren().removeIf(child -> {
      Integer rowIndex = getRowIndex(child);
      Integer columnIndex = getColumnIndex(child);

      return rowIndex != null && columnIndex != null
          && columnIndex == coordinate.getColumn() && rowIndex == coordinate.getRow();
    });

    add(node, coordinate.getColumn(), coordinate.getRow());

    nodeMap.put(coordinate, node);

    return node;
  }

  private void handleChildMouseEvent(Event event) {
    if (!(event.getSource() instanceof Node) || clickListener == null) {
      return;
    }
    Node clicked = (Node) event.getSource();
    GridCoordinate coordinate = getGridCoordinateForChild(clicked);

    if (coordinate == null) {
      return;
    }

    T cellState = grid.getStateAt(coordinate);

    clickListener.onClick(coordinate, cellState, clicked);
  }

  private GridCoordinate getGridCoordinateForChild(Node child) {
    Integer column = getColumnIndex(child);
    Integer row = getRowIndex(child);

    if (row == null || column == null) {
      return null;
    }

    return new GridCoordinate(column, row);
  }

  private void addDragListener(Node child) {
    child.setOnDragEntered(event -> {
      if (event.getGestureSource() == child) {
        visitedCoordinatesInDrag.clear();
      }

      if (!visitedCoordinatesInDrag.add(getGridCoordinateForChild(child))) {
        return;
      }

      handleChildMouseEvent(event);

      event.consume();
    });
    child.setOnDragDetected(event -> {
      Dragboard dragboard = child.startDragAndDrop(TransferMode.COPY_OR_MOVE);
      ClipboardContent content = new ClipboardContent();
      content.putString("hey");
      dragboard.setContent(content);
      event.consume();
    });
  }

  public interface ClickListener<T extends GridCellState> {

    /**
     * Called when a cell in the grid is clicked.
     *
     * @param coordinate the coordinate that was clicked
     * @param state the state the cell is currently in
     * @param clickedNode the node that was clicked
     */
    void onClick(GridCoordinate coordinate, T state, Node clickedNode);
  }
}

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

public class DisplayedGrid<T extends GridCellState> extends GridPane {

  private int columns;
  private int rows;

  private Map<GridCoordinate, T> cellValues;
  private ClickListener<T> clickListener;

  private Set<GridCoordinate> visitedCoordinatesInDrag;

  public DisplayedGrid(int columns, int rows) {
    this.columns = columns;
    this.rows = rows;

    setGridLinesVisible(true);

    cellValues = new HashMap<>();
    visitedCoordinatesInDrag = new HashSet<>();

    initializeGrid(columns, rows);
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

  public int getColumns() {
    return columns;
  }

  public int getRows() {
    return rows;
  }

  public void setClickListener(ClickListener<T> clickListener) {
    this.clickListener = clickListener;

    for (Node node : getChildren()) {
      node.setOnMouseClicked(this::handleChildMouseEvent);
      addDragListener(node);
    }
  }

  /**
   * Adds a cell to this grid.
   *
   * @param column the column to add it at
   * @param row the row to add it at
   * @param state the state to add
   * @return the node created by {@link GridCellState#getNode()}
   */
  public Node setCellState(int column, int row, T state) {
    Node node = state.getNode();

    node.setOnMouseClicked(this::handleChildMouseEvent);
    addDragListener(node);

    GridCoordinate coordinate = new GridCoordinate(column, row);

    if (cellValues.containsKey(coordinate)) {
      getChildren().removeIf(child -> {
        Integer rowIndex = getRowIndex(child);
        Integer columnIndex = getColumnIndex(child);

        return rowIndex != null && columnIndex != null
            && rowIndex == row && columnIndex == column;
      });
    }

    add(node, column, row);

    cellValues.put(coordinate, state);

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

    T cellState = getCellState(coordinate.getColumn(), coordinate.getRow());

    clickListener.onClick(coordinate.getColumn(), coordinate.getRow(), cellState, clicked);
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

  /**
   * Returns the state at the given cell.
   *
   * @param column the column to add it at
   * @param row the row to add it at
   * @return the state
   */
  public T getCellState(int column, int row) {
    return cellValues.get(new GridCoordinate(column, row));
  }

  public interface ClickListener<T> {

    /**
     * Called when a cell in the grid is clicked.
     *
     * @param column the column that was clicked
     * @param row the row that was clicked
     * @param state the state the cell is currently in
     * @param clickedNode the node that was clicked
     */
    void onClick(int column, int row, T state, Node clickedNode);
  }
}

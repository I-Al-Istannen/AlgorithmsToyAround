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

  private InteractionListener<T> interactionListener;
  private AlgorithmGrid<T> grid;

  private Map<GridCoordinate, Node> nodeMap;

  private Set<GridCoordinate> visitedCoordinatesInDrag;
  private InteractionState<T> dragContext;

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

  public void setInteractionListener(InteractionListener<T> interactionListener) {
    this.interactionListener = interactionListener;

    for (Node node : getChildren()) {
      node.setOnMouseClicked(this::handleClick);
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

    node.setOnMouseClicked(this::handleClick);
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

  private void handleClick(Event event) {
    InteractionState<T> state = getStateFromEvent(event);

    if (state == null) {
      return;
    }

    interactionListener.onClick(state);
  }

  private InteractionState<T> getStateFromEvent(Event event) {
    if (!(event.getSource() instanceof Node) || interactionListener == null) {
      return null;
    }
    Node clicked = (Node) event.getSource();
    GridCoordinate coordinate = getGridCoordinateForChild(clicked);

    if (coordinate == null) {
      return null;
    }
    return new InteractionState<>(coordinate, grid.getStateAt(coordinate), clicked, grid);
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
      InteractionState<T> state = getStateFromEvent(event);

      if (state == null || event.getGestureSource() == child || dragContext == null) {
        return;
      }

      if (!visitedCoordinatesInDrag.add(getGridCoordinateForChild(child))) {
        return;
      }

      interactionListener.onDragOver(state, dragContext);

      event.consume();
    });
    child.setOnDragDetected(event -> {
      InteractionState<T> state = getStateFromEvent(event);

      if (state == null) {
        return;
      }

      visitedCoordinatesInDrag.clear();
      dragContext = state;
      interactionListener.onDragStart(state);

      Dragboard dragboard = child.startDragAndDrop(TransferMode.COPY_OR_MOVE);
      ClipboardContent content = new ClipboardContent();
      content.putString("sample_data");
      dragboard.setContent(content);
      event.consume();
    });
    child.setOnDragOver(event -> {
      if (event.getGestureSource() != child) {
        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
      }
    });
    child.setOnDragDropped(event -> {
      if (event.getGestureSource() == child) {
        return;
      }

      InteractionState<T> state = getStateFromEvent(event);

      if (state == null || dragContext == null) {
        return;
      }

      interactionListener.onDragStop(state, dragContext);
    });
  }

  public interface InteractionListener<T extends GridCellState> {

    /**
     * Called when a cell in the grid is clicked.
     *
     * @param state the state containing information about the event
     */
    void onClick(InteractionState<T> state);

    /**
     * Called when a node in a grid is being dragged.
     *
     * @param state the state containing information about the event
     */
    void onDragStart(InteractionState<T> state);

    /**
     * Called when a node is being dragged over.
     *
     * @param state the state containing information about the event
     * @param dragState constains information about the drag origin
     */
    void onDragOver(InteractionState<T> state, InteractionState<T> dragState);

    /**
     * Called when the drag has ended.
     *
     * @param state the state containing information about the event
     * @param dragState constains information about the drag origin
     */
    void onDragStop(InteractionState<T> state, InteractionState<T> dragState);
  }

  public static class InteractionState<T extends GridCellState> {

    private GridCoordinate coordinate;
    private T state;
    private Node node;
    private AlgorithmGrid<T> grid;

    InteractionState(GridCoordinate coordinate, T state, Node node,
        AlgorithmGrid<T> grid) {
      this.coordinate = coordinate;
      this.state = state;
      this.node = node;
      this.grid = grid;
    }

    public GridCoordinate getCoordinate() {
      return coordinate;
    }

    public T getState() {
      return state;
    }

    public Node getNode() {
      return node;
    }

    public AlgorithmGrid<T> getGrid() {
      return grid;
    }
  }
}

package me.ialistannen.pathfinding.visualize.grid.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.interaction.GridInteractionListener;
import me.ialistannen.pathfinding.visualize.grid.interaction.GridInteractionManager;

public class DisplayedGrid<T extends GridCellState> extends GridPane {

  private GridInteractionManager<T> gridInteractionManager;

  public DisplayedGrid(AlgorithmGrid<T> grid) {
    gridInteractionManager = new GridInteractionManager<>(grid);

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

  /**
   * Sets the {@link GridInteractionListener} this {@link DisplayedGrid} uses.
   *
   * @param interactionListener the {@link GridInteractionListener} to use
   */
  public void setInteractionListener(GridInteractionListener<T> interactionListener) {
    gridInteractionManager.setInteractionListener(interactionListener);
    for (Node node : getChildren()) {
      gridInteractionManager.configureNode(node);
    }
  }

  /**
   * Adds a cell to this grid.
   *
   * @param coordinate the coordinate to change
   * @param state the state to add
   * @return the node created by {@link GridCellState#getNode()}
   */
  private Node setCellState(GridCoordinate coordinate, T state) {
    Objects.requireNonNull(coordinate, "coordinate can not be null!");
    Objects.requireNonNull(state, "state can not be null!");

    Node node = state.getNode();

    gridInteractionManager.configureNode(node);

    removeChildAt(coordinate);

    add(node, coordinate.getColumn(), coordinate.getRow());

    return node;
  }

  private void removeChildAt(GridCoordinate coordinate) {
    getChildren().removeIf(node -> coordinate.equals(getChildGridCoordinate(node)));
  }

  private GridCoordinate getChildGridCoordinate(Node child) {
    Integer rowIndex = getRowIndex(child);
    Integer columnIndex = getColumnIndex(child);

    if (rowIndex == null || columnIndex == null) {
      return null;
    }

    return new GridCoordinate(columnIndex, rowIndex);
  }
}

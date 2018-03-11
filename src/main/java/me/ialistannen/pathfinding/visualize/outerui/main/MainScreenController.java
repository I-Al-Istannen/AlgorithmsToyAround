package me.ialistannen.pathfinding.visualize.outerui.main;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.interaction.PaintingGridListener;
import me.ialistannen.pathfinding.visualize.grid.node.DisplayedGrid;
import me.ialistannen.pathfinding.visualize.outerui.controls.ControlPanel;

public class MainScreenController {

  @FXML
  private ControlPanel controlPanel;

  @FXML
  private BorderPane rootPane;

  @FXML
  private void initialize() {
    AlgorithmGrid<DefaultGridState> algorithmGrid = new AlgorithmGrid<>(
        DefaultGridState.EMPTY, 40, 40
    );
    DisplayedGrid<DefaultGridState> displayedGrid = new DisplayedGrid<>(algorithmGrid);
    displayedGrid.setInteractionListener(new PaintingGridListener());

    addStartAndEnd(algorithmGrid);

    rootPane.setCenter(displayedGrid);

    StackPane.setMargin(displayedGrid, new Insets(20));

    controlPanel.setGrid(algorithmGrid);
  }

  private void addStartAndEnd(AlgorithmGrid<DefaultGridState> grid) {
    int middleX = grid.getWidth() / 2;
    int middleY = grid.getHeight() / 2;

    grid.setStateAt(middleX - 5, middleY, DefaultGridState.START);
    grid.setStateAt(middleX + 5, middleY, DefaultGridState.END);
  }
}

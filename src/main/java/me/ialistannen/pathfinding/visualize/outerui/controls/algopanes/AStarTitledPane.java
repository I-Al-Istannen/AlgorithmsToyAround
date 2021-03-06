package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.astar.AStarAlgorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.parts.DistanceSelectionPanel;

public class AStarTitledPane extends AlgorithmTitledPane {

  @FXML
  private CheckBox diagonalCheckbox;

  @FXML
  private DistanceSelectionPanel distancePanel;

  public AStarTitledPane() {
    super("/fxml/AStarTitledPane.fxml", "A*");
  }


  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new AStarAlgorithm(
        distancePanel.getDistanceFunction(),
        withDiagonal(diagonalCheckbox.isSelected())
    );
  }
}

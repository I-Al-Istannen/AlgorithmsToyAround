package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.bfsgreedy.GreedyBFSAlgorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.parts.DistanceSelectionPanel;

public class GreedyBFSPane extends AlgorithmTitledPane {

  @SuppressWarnings("unused")
  @FXML
  private CheckBox diagonalCheckbox;

  @SuppressWarnings("unused")
  @FXML
  private DistanceSelectionPanel distancePanel;

  public GreedyBFSPane() {
    super("/fxml/GreedyBFSTitledPane.fxml", "Greedy BFS");
  }

  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new GreedyBFSAlgorithm(
        withDiagonal(diagonalCheckbox.isSelected()),
        distancePanel.getDistanceFunction()
    );
  }
}

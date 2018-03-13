package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import javafx.fxml.FXML;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.jps.JumpPointSearchAlgorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.parts.DistanceSelectionPanel;

public class JumpPointSearchPane extends AlgorithmTitledPane {

  @FXML
  private DistanceSelectionPanel distancePanel;

  public JumpPointSearchPane() {
    super("/fxml/JumpPointSearchTitledPane.fxml", "Jump point search");
  }

  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new JumpPointSearchAlgorithm(
        distancePanel.getDistanceFunction()
    );
  }
}

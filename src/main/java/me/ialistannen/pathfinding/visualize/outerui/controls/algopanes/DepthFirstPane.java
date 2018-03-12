package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.depthfirst.DepthFirstSearch;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;

public class DepthFirstPane extends AlgorithmTitledPane {

  @FXML
  private CheckBox diagonalCheckbox;

  public DepthFirstPane() {
    super("/fxml/DepthFirstTitledPane.fxml", "Depth first");
  }


  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new DepthFirstSearch(withDiagonal(diagonalCheckbox.isSelected()));
  }
}

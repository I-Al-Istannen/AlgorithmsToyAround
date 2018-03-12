package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.breadthfirst.BreadthFirstAlgorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;

public class BreadthFirstPane extends AlgorithmTitledPane {

  @FXML
  private CheckBox diagonalCheckbox;

  public BreadthFirstPane() {
    super("/fxml/BreadthFirstTitledPane.fxml", "Breadth first");
  }

  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new BreadthFirstAlgorithm(withDiagonal(diagonalCheckbox.isSelected()));
  }
}

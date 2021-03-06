package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.dijkstra.DijkstraAlgorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;

public class DijkstraTitledPane extends AlgorithmTitledPane {

  @FXML
  private CheckBox diagonalCheckbox;

  public DijkstraTitledPane() {
    super("/fxml/DijkstraTitledPane.fxml", "Dijkstra");
  }

  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new DijkstraAlgorithm(withDiagonal(diagonalCheckbox.isSelected()));
  }
}

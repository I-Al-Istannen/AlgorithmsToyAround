package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.dijkstra.DijkstraAlgorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public class DijkstraTitledPane extends AlgorithmTitledPane {

  @FXML
  private CheckBox diagonalCheckbox;

  public DijkstraTitledPane() {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/fxml/DijkstraTitledPane.fxml")
    );
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    setText("Dijkstra");
  }

  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new DijkstraAlgorithm(
        diagonalCheckbox.isSelected() ? Direction.WITH_DIAGONAL : Direction.NO_DIAGONAL
    );
  }
}

package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.astar.AStarAlgorithm;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DefaultDistanceFunction;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public class AStarTitledPane extends AlgorithmTitledPane {

  @FXML
  private CheckBox diagonalCheckbox;

  @FXML
  private RadioButton euclidianButton;

  @FXML
  private RadioButton manhattenButton;


  public AStarTitledPane() {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/fxml/AStarTitledPane.fxml")
    );
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    setText("A*");
  }

  @FXML
  private void initialize() {
    ToggleGroup toggleGroup = new ToggleGroup();
    manhattenButton.setToggleGroup(toggleGroup);
    euclidianButton.setToggleGroup(toggleGroup);
  }


  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new AStarAlgorithm(
        manhattenButton.isSelected()
            ? DefaultDistanceFunction.MANHATTEN
            : DefaultDistanceFunction.EUCLIDIAN,
        diagonalCheckbox.isSelected()
            ? Direction.WITH_DIAGONAL
            : Direction.NO_DIAGONAL
    );
  }
}

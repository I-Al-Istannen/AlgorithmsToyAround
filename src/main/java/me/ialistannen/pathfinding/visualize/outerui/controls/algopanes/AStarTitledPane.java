package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.astar.AStarAlgorithm;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DefaultDistanceFunction;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DistanceFunction;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;

public class AStarTitledPane extends AlgorithmTitledPane {

  @FXML
  private CheckBox diagonalCheckbox;

  @FXML
  private RadioButton euclideanButton;

  @FXML
  private RadioButton manhattenButton;

  @FXML
  private RadioButton chebyshevButton;


  public AStarTitledPane() {
    super("/fxml/AStarTitledPane.fxml", "A*");
  }

  @FXML
  private void initialize() {
    ToggleGroup toggleGroup = new ToggleGroup();
    manhattenButton.setToggleGroup(toggleGroup);
    euclideanButton.setToggleGroup(toggleGroup);
    chebyshevButton.setToggleGroup(toggleGroup);
  }


  @Override
  public Algorithm<DefaultGridState> getAlgorithm() {
    return new AStarAlgorithm(
        getDistanceFunction(),
        withDiagonal(diagonalCheckbox.isSelected())
    );
  }

  private DistanceFunction getDistanceFunction() {
    if (manhattenButton.isSelected()) {
      return DefaultDistanceFunction.MANHATTEN;
    }
    if (euclideanButton.isSelected()) {
      return DefaultDistanceFunction.EUCLIDEAN;
    }
    return DefaultDistanceFunction.CHEBYSHEV;
  }
}

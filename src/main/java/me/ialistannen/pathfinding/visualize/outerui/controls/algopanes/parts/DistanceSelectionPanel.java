package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.parts;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DefaultDistanceFunction;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DistanceFunction;

public class DistanceSelectionPanel extends VBox {

  @FXML
  private RadioButton euclideanButton;

  @FXML
  private RadioButton manhattenButton;

  @FXML
  private RadioButton chebyshevButton;

  public DistanceSelectionPanel() {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/fxml/DistanceSelectionPanel.fxml")
    );
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void initialize() {
    ToggleGroup toggleGroup = new ToggleGroup();
    manhattenButton.setToggleGroup(toggleGroup);
    euclideanButton.setToggleGroup(toggleGroup);
    chebyshevButton.setToggleGroup(toggleGroup);
  }


  /**
   * @return the currently selected {@link DistanceFunction}.
   */
  public DistanceFunction getDistanceFunction() {
    if (manhattenButton.isSelected()) {
      return DefaultDistanceFunction.MANHATTEN;
    }
    if (euclideanButton.isSelected()) {
      return DefaultDistanceFunction.EUCLIDEAN;
    }
    return DefaultDistanceFunction.CHEBYSHEV;
  }
}

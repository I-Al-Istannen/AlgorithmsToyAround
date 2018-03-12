package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public abstract class AlgorithmTitledPane extends TitledPane {

  /**
   * Creates a new titled pane.
   *
   * @param fxmlFile the name of the FXML file to use
   * @param name the name of this pane
   */
  public AlgorithmTitledPane(String fxmlFile, String name) {
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource(fxmlFile)
    );
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    setText(name);
  }

  /**
   * Returns the algorithm from this pane.
   *
   * @return the built algorithm.
   */
  public abstract Algorithm<DefaultGridState> getAlgorithm();

  /**
   * Returns the correct list of {@link Direction}s, if you want diagonal or not.
   *
   * @param diagonal whether to also return diagonal ones
   * @return a list with {@link Direction}s, with or without diagonals
   */
  protected List<Direction> withDiagonal(boolean diagonal) {
    return diagonal ? Direction.WITH_DIAGONAL : Direction.NO_DIAGONAL;
  }
}

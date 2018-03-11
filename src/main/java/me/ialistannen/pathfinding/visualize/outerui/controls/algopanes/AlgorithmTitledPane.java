package me.ialistannen.pathfinding.visualize.outerui.controls.algopanes;

import javafx.scene.control.TitledPane;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;

public abstract class AlgorithmTitledPane extends TitledPane {

  /**
   * Returns the algorithm from this pane.
   *
   * @return the built algorithm.
   */
  public abstract Algorithm<DefaultGridState> getAlgorithm();
}

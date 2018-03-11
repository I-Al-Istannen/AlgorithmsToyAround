package me.ialistannen.pathfinding.visualize.outerui.controls;

import static me.ialistannen.pathfinding.visualize.grid.DefaultGridState.EMPTY;
import static me.ialistannen.pathfinding.visualize.grid.DefaultGridState.END;
import static me.ialistannen.pathfinding.visualize.grid.DefaultGridState.START;
import static me.ialistannen.pathfinding.visualize.grid.DefaultGridState.WALL;

import java.io.IOException;
import javafx.animation.Animation.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.outerui.controls.Runner.StateCallback;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.AStarTitledPane;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.AlgorithmTitledPane;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.DijkstraTitledPane;

@SuppressWarnings("unused")
public class ControlPanel extends BorderPane implements StateCallback {

  @FXML
  private Accordion accordion;

  @FXML
  private Button goButton;

  private AlgorithmGrid<DefaultGridState> grid;
  private Runner<DefaultGridState> runner;

  public ControlPanel() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ControlPanel.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    accordion.getPanes().add(new DijkstraTitledPane());
    accordion.getPanes().add(new AStarTitledPane());
    accordion.setExpandedPane(accordion.getPanes().get(0));

    runner = new Runner<>(6, this);
  }

  public void setGrid(AlgorithmGrid<DefaultGridState> grid) {
    this.grid = grid;
  }

  @FXML
  void onClearPath() {
    grid.replaceIf(EMPTY, state -> state != WALL && state != START && state != END);
  }

  @FXML
  void onClearWalls() {
    grid.replaceIf(EMPTY, state -> state == WALL);
  }

  @FXML
  void onGo() {
    if (runner.isRunning()) {
      runner.stop();
      onClearPath();
      return;
    }
    onClearPath();

    TitledPane expandedPane = accordion.getExpandedPane();

    if (!(expandedPane instanceof AlgorithmTitledPane)) {
      return;
    }

    Algorithm<DefaultGridState> algorithm = ((AlgorithmTitledPane) expandedPane).getAlgorithm();
    runner.run(algorithm, grid);
  }

  @Override
  public void statusChanged(Status status) {
    if (status == Status.RUNNING) {
      goButton.setText("Stop");
    } else {
      goButton.setText("Go!");
    }
  }
}

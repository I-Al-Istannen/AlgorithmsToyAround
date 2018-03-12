package me.ialistannen.pathfinding.visualize.outerui.controls;

import static me.ialistannen.pathfinding.visualize.grid.DefaultGridState.EMPTY;
import static me.ialistannen.pathfinding.visualize.grid.DefaultGridState.END;
import static me.ialistannen.pathfinding.visualize.grid.DefaultGridState.START;
import static me.ialistannen.pathfinding.visualize.grid.DefaultGridState.WALL;

import com.jfoenix.controls.JFXSlider;
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
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.BreadthFirstPane;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.DepthFirstPane;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.DijkstraTitledPane;
import me.ialistannen.pathfinding.visualize.outerui.controls.algopanes.GreedyBFSPane;

@SuppressWarnings("unused")
public class ControlPanel extends BorderPane implements StateCallback {

  @FXML
  private Accordion accordion;

  @FXML
  private Button goButton;

  @FXML
  private JFXSlider speedSlider;

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
  }

  @FXML
  private void initialize() {
    accordion.getPanes().add(new DijkstraTitledPane());
    accordion.getPanes().add(new AStarTitledPane());
    accordion.getPanes().add(new BreadthFirstPane());
    accordion.getPanes().add(new GreedyBFSPane());
    accordion.getPanes().add(new DepthFirstPane());
    accordion.setExpandedPane(accordion.getPanes().get(0));
  }

  public void setGrid(AlgorithmGrid<DefaultGridState> grid) {
    this.grid = grid;
  }

  @FXML
  void onClearPath() {
    grid.replaceIf(EMPTY,
        state -> state != EMPTY && state != WALL && state != START && state != END
    );
  }

  @FXML
  void onClearWalls() {
    grid.replaceIf(EMPTY, state -> state == WALL);
  }

  @FXML
  void onGo() {
    if (runner != null && runner.isRunning()) {
      runner.stop();
      onClearPath();
      return;
    }
    onClearPath();

    runner = new Runner<>((int) speedSlider.getValue(), this);

    TitledPane expandedPane = accordion.getExpandedPane();

    if (!(expandedPane instanceof AlgorithmTitledPane)) {
      return;
    }

    new Thread(() -> {
      Algorithm<DefaultGridState> algorithm = ((AlgorithmTitledPane) expandedPane).getAlgorithm();
      runner.run(algorithm, grid);
    }).start();
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

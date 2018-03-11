package me.ialistannen.pathfinding.visualize.outerui.controls;

import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.StatefulGridCoordinate;

public class Runner<T extends GridCellState> {

  private Timeline ticker;
  private long delayMillis;
  private int counter;

  Runner(long delayMillis, StateCallback stateCallback) {
    this.delayMillis = delayMillis;

    ticker = new Timeline();
    ticker.setCycleCount(Animation.INDEFINITE);
    ticker.statusProperty()
        .addListener((observable, oldValue, newValue) -> Platform.runLater(
            () -> stateCallback.statusChanged(newValue)
        ));
  }

  /**
   * Runs the given algorithm and displays it in the grid.
   *
   * @param algorithm the {@link AlgorithmGrid} to run
   * @param grid the {@link AlgorithmGrid} to use
   */
  public void run(Algorithm<T> algorithm, AlgorithmGrid<T> grid) {
    if (ticker != null) {
      ticker.stop();
    }
    counter = 0;

    List<StatefulGridCoordinate<T>> steps = algorithm.compute(grid).getSteps();

    ticker.getKeyFrames().setAll(new KeyFrame(
        Duration.millis(delayMillis),
        event -> tick(steps, grid, 2)
    ));

    ticker.playFromStart();
  }

  private void tick(List<StatefulGridCoordinate<T>> steps, AlgorithmGrid<T> grid, int count) {
    for (int i = 0; i < count; i++) {
      tick(steps, grid);
    }
  }

  private void tick(List<StatefulGridCoordinate<T>> steps, AlgorithmGrid<T> grid) {
    if (counter >= steps.size()) {
      ticker.stop();
      return;
    }

    StatefulGridCoordinate<T> state = steps.get(counter++);
    GridCoordinate coordinate = state.getCoordinate();

    T currentState = grid.getStateAt(coordinate);

    if (currentState.isEnd() || currentState.isStart() || !currentState.isPassable()) {
      return;
    }

    grid.setStateAt(coordinate, state.getState());
  }

  /**
   * Checks if this Runner is running.
   *
   * @return true if this object is running
   */
  public boolean isRunning() {
    return ticker.getStatus() == Status.RUNNING;
  }

  /**
   * Pauses the playback.
   */
  public void stop() {
    if (ticker.getStatus() == Status.RUNNING) {
      ticker.stop();
    }
  }

  public interface StateCallback {

    /**
     * Called when the status changed.
     *
     * @param status the new {@link Status}
     */
    void statusChanged(Status status);
  }
}

package me.ialistannen.pathfinding.visualize.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.ialistannen.pathfinding.visualize.grid.GridCellState;
import me.ialistannen.pathfinding.visualize.grid.StatefulGridCoordinate;

public class AlgorithmResult<T extends GridCellState> {

  private boolean successful;
  private List<StatefulGridCoordinate<T>> steps;

  public AlgorithmResult(boolean successful, List<StatefulGridCoordinate<T>> steps) {
    this.successful = successful;
    this.steps = new ArrayList<>(steps);
  }

  public boolean isSuccessful() {
    return successful;
  }

  public List<StatefulGridCoordinate<T>> getSteps() {
    return Collections.unmodifiableList(steps);
  }
}

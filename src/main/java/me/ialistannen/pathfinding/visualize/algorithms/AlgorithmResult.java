package me.ialistannen.pathfinding.visualize.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.ialistannen.pathfinding.visualize.grid.StatefulGridCoordinate;

public class AlgorithmResult {

  private boolean successful;
  private List<List<StatefulGridCoordinate<?>>> steps;

  public AlgorithmResult(boolean successful, List<List<StatefulGridCoordinate<?>>> steps) {
    this.successful = successful;
    this.steps = new ArrayList<>(steps);
  }

  public boolean isSuccessful() {
    return successful;
  }

  public List<List<StatefulGridCoordinate<?>>> getSteps() {
    return Collections.unmodifiableList(steps);
  }
}

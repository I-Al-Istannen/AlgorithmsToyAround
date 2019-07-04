package me.ialistannen.algorithms.layout.forcedbased.forces;

import java.util.function.ToDoubleBiFunction;
import me.ialistannen.algorithms.layout.forcedbased.ForceActor;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * Plots a given distance function by moving all nodes towards the point of smallest distance,
 * stopping when they are near enough.
 */
public class PlotDistanceFunctionForce implements ForceActor {

  private Vector2D center;
  private ToDoubleBiFunction<Vector2D, Vector2D> distanceFunction;

  /**
   * Creates a new force.
   *
   * @param center the center point to gravitate to
   * @param distanceFunction the distance function to use
   */
  public PlotDistanceFunctionForce(Vector2D center,
      ToDoubleBiFunction<Vector2D, Vector2D> distanceFunction) {
    this.center = center;
    this.distanceFunction = distanceFunction;
  }

  @Override
  public void apply(Node a, Node b) {
    double force = distanceFunction.applyAsDouble(center, a.getPosition());

    // Plot distance function
    if (force < 100) {
      force = -2;
    }
    if (force > 100) {
      force = 2;
    }

    Vector2D delta = center
        .subtract(a.getPosition())
        .normalize()
        .multiply(force);

    a.setActingForce(delta);
  }
}

package me.ialistannen.algorithms.layout.forcedbased.forces;

import javafx.beans.value.ObservableValue;
import me.ialistannen.algorithms.layout.forcedbased.ForceActor;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A simple attraction to a given center point.
 */
public class BlackHoleAttractionForce implements ForceActor {

  private final ObservableValue<Vector2D> center;
  private double g;

  /**
   * Attracts all nodes to the center.
   *
   * @param center the center position
   * @param g the gravitational constant. At least by name
   */
  public BlackHoleAttractionForce(ObservableValue<Vector2D> center, double g) {
    this.center = center;
    this.g = g;
  }

  @Override
  public void apply(Node a, Node b) {
    double distanceSquared = a.getPosition().chebyshevDistanceTo(center.getValue());
    distanceSquared *= distanceSquared;

    // Inverse gravitation. Nearly discovered Anti-gravity but fell short :(
    double constant = distanceSquared / g;

    constant = Math.min(constant, 2);

    Vector2D delta = center.getValue()
        .subtract(a.getPosition())
        .normalize()
        .multiply(constant);

    a.setActingForce(a.getActingForce().add(delta));
  }
}

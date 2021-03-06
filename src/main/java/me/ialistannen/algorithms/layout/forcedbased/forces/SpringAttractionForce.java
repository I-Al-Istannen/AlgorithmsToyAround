package me.ialistannen.algorithms.layout.forcedbased.forces;

import me.ialistannen.algorithms.layout.forcedbased.ForceActor;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * Creates an attraction force based on a linear spring.
 */
public class SpringAttractionForce implements ForceActor {

  private final double springLength;
  private final double springConstant;

  /**
   * Creates a new spring attraction force.
   *
   * @param springLength the length of the spring
   * @param springConstant the spring constant
   */
  public SpringAttractionForce(double springLength, double springConstant) {
    this.springLength = springLength;
    this.springConstant = springConstant;
  }

  @Override
  public void apply(Node a, Node b) {
    // One direction is enough
    if (!a.isConnected(b) && !b.isConnected(a)) {
      return;
    }

    double distance = a.getPosition().distanceTo(b.getPosition());

    double force = springConstant * Math.max(0, distance - springLength);

    Vector2D resultingForce = b.getPosition()
        .subtract(a.getPosition())
        .normalize()
        .multiply(force);

    a.setActingForce(a.getActingForce().add(resultingForce));
  }
}

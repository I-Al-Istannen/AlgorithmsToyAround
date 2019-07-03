package me.ialistannen.algorithms.layout.forcedbased.forces;

import me.ialistannen.algorithms.layout.forcedbased.ForceActor;
import me.ialistannen.algorithms.layout.forcedbased.Node;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;

/**
 * A force based on Coulomb's Law.
 */
public class ElectricalRepulsionForce implements ForceActor {

  private final double repulsionConstant;

  /**
   * Creates a new electrical repulsion force
   *
   * @param repulsionConstant the repulsion constant
   */
  public ElectricalRepulsionForce(double repulsionConstant) {
    this.repulsionConstant = repulsionConstant;
  }

  @Override
  public void apply(Node a, Node b) {
    double distance = a.getPosition().distanceToSquared(b.getPosition());

    if (distance == 0) {
      return;
    }

    double force = repulsionConstant / distance;

    // End - start, so it is negative (i.e. repulses)
    Vector2D resultingForce = a.getPosition()
        .subtract(b.getPosition())
        .normalize()
        .multiply(force);

    a.setActingForce(a.getActingForce().add(resultingForce));
  }
}

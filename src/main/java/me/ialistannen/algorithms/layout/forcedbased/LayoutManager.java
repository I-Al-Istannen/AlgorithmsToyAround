package me.ialistannen.algorithms.layout.forcedbased;

import java.util.List;
import me.ialistannen.algorithms.layout.forcedbased.normalizing.NodePositionNormalizer;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A layout manager that lays out nodes using a force based mechanism.
 *
 * @param <T> the type of the nodes
 */
public class LayoutManager<T> implements Runnable {

  private List<Node<T>> nodes;
  private List<ForceActor> forces;
  private NodePositionNormalizer nodePositionNormalizer;
  private double dampeningFactor;

  /**
   * Creates a new layout manager.
   *
   * @param nodes the nodes to layout. Copies the reference!
   * @param forces the forces to apply. Copies the reference!
   * @param nodePositionNormalizer the normalizer for node positions
   * @param dampeningFactor the velocity dampening factor
   */
  public LayoutManager(List<Node<T>> nodes, List<ForceActor> forces,
      NodePositionNormalizer nodePositionNormalizer, double dampeningFactor) {
    this.nodes = nodes;
    this.forces = forces;
    this.nodePositionNormalizer = nodePositionNormalizer;
    this.dampeningFactor = dampeningFactor;
  }

  /**
   * Creates a new layout manager.
   *
   * Uses a {@link NodePositionNormalizer#nop()} normalizer.
   *
   * @param nodes the nodes to layout. Copies the reference!
   * @param forces the forces to apply. Copies the reference!
   * @param dampeningFactor the velocity dampening factor
   */
  public LayoutManager(List<Node<T>> nodes, List<ForceActor> forces, double dampeningFactor) {
    this(nodes, forces, NodePositionNormalizer.nop(), dampeningFactor);
  }

  @Override
  public void run() {
    applyForces();
    moveNodes();
  }

  private void moveNodes() {
    for (Node<T> node : nodes) {
      if (node.isPausePhysics()) {
        continue;
      }
      Vector2D resultPos = node.getPosition().add(node.getActingForce());
      resultPos = nodePositionNormalizer.normalize(resultPos);
      node.setPosition(resultPos);

      // dampening
      node.setActingForce(node.getActingForce().multiply(dampeningFactor));
    }
  }

  private void applyForces() {
    for (Node<T> first : nodes) {
      for (Node<T> second : nodes) {
        if (first == second) {
          continue;
        }
        forces.forEach(forceActor -> forceActor.apply(first, second));
      }
    }
  }
}

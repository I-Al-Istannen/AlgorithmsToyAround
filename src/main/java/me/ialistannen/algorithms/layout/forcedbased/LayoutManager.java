package me.ialistannen.algorithms.layout.forcedbased;

import java.util.ArrayList;
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

  /**
   * Creates a new layout manager.
   *
   * @param nodes the nodes to layout
   * @param forces the forces to apply
   * @param nodePositionNormalizer the normalizer for node positions
   */
  public LayoutManager(List<Node<T>> nodes, List<ForceActor> forces,
      NodePositionNormalizer nodePositionNormalizer) {
    this.nodes = new ArrayList<>(nodes);
    this.forces = new ArrayList<>(forces);
    this.nodePositionNormalizer = nodePositionNormalizer;
  }

  /**
   * Creates a new layout manager.
   *
   * Uses a {@link NodePositionNormalizer#nop()} normalizer.
   *
   * @param nodes the nodes to layout
   * @param forces the forces to apply
   */
  public LayoutManager(List<Node<T>> nodes, List<ForceActor> forces) {
    this(nodes, forces, NodePositionNormalizer.nop());
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
      node.setActingForce(node.getActingForce().multiply(0.1));
    }
  }

  private void applyForces() {
    for (Node<T> first : nodes) {
      for (Node<T> second : nodes) {
        if (first == second) {
          continue;
        }
        if (first.isPausePhysics() || second.isPausePhysics()) {
          continue;
        }
        forces.forEach(forceActor -> forceActor.apply(first, second));
      }
    }
  }
}

package me.ialistannen.algorithms.layout.forcedbased;

import java.util.ArrayList;
import java.util.List;
import me.ialistannen.algorithms.layout.forcedbased.normalizing.NodePositionNormalizer;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

public class LayoutManager<T> implements Runnable {

  private List<Node<T>> nodes;
  private List<ForceActor> forces;
  private NodePositionNormalizer nodePositionNormalizer;

  public LayoutManager(List<Node<T>> nodes, List<ForceActor> forces, int maxIterations,
      NodePositionNormalizer nodePositionNormalizer) {
    this.nodes = new ArrayList<>(nodes);
    this.forces = new ArrayList<>(forces);
    this.nodePositionNormalizer = nodePositionNormalizer;
  }

  @Override
  public void run() {
    applyForces();
    moveNodes();
  }

  private void moveNodes() {
    for (Node<T> node : nodes) {
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
        forces.forEach(forceActor -> forceActor.apply(first, second));
      }
    }
  }
}

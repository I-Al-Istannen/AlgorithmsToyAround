package me.ialistannen.algorithms.layout.forcedbased;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import me.ialistannen.algorithms.layout.forcedbased.normalizing.NodePositionNormalizer;

public class LayoutManager<T> implements Runnable {

  private List<Node<T>> nodes;
  private List<ForceActor> forces;
  private int maxIterations;
  private Duration delay;
  private NodePositionNormalizer nodePositionNormalizer;

  public LayoutManager(List<Node<T>> nodes, List<ForceActor> forces, int maxIterations,
      Duration delay, NodePositionNormalizer nodePositionNormalizer) {
    this.nodes = new ArrayList<>(nodes);
    this.forces = new ArrayList<>(forces);
    this.maxIterations = maxIterations;
    this.delay = delay;
    this.nodePositionNormalizer = nodePositionNormalizer;
  }

  @Override
  public void run() {
    for (int i = 0; i < maxIterations; i++) {
      sleep();

      applyForces();
      moveNodes();
    }
  }

  private void moveNodes() {
    for (Node<T> node : nodes) {
      Vector2D resultPos = node.getPosition().add(node.getActingForce());
      resultPos = nodePositionNormalizer.normalize(resultPos);
      node.setPosition(resultPos);
      node.setActingForce(node.getActingForce().multiply(0.1));
//      node.setActingForce(Vector2D.ZERO);
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

  private void sleep() {
    try {
      Thread.sleep(delay.toMillis());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

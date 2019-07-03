package me.ialistannen.algorithms.layout.forcedbased.tree;

import java.util.HashSet;
import java.util.Set;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;

/**
 * A node!
 *
 * @param <T> the type of the node's value
 */
public class Node<T> {

  private T value;
  private Vector2D actingForce;
  private Vector2D position;

  private Set<Node<T>> neighbours;
  private boolean pausePhysics;

  /**
   * Creates a new node.
   *
   * @param value the value of this node
   */
  public Node(T value) {
    this.value = value;

    this.actingForce = new Vector2D(0, 0);
    this.position = new Vector2D(0, 0);
    this.neighbours = new HashSet<>();
  }

  /**
   * Returns this node's acting force.
   *
   * @return this node's acting force
   */
  public Vector2D getActingForce() {
    return actingForce;
  }

  /**
   * Sets this node's acting force.
   *
   * @param actingForce the acting force
   */
  public void setActingForce(Vector2D actingForce) {
    this.actingForce = actingForce;
  }

  /**
   * Returns the position of this node.
   *
   * @return the position of this node
   */
  public Vector2D getPosition() {
    return position;
  }

  /**
   * Sets this node's position.
   *
   * @param position this nodes position
   */
  public void setPosition(Vector2D position) {
    this.position = position;
  }

  /**
   * Returns the value.
   *
   * @return the value
   */
  public T getValue() {
    return value;
  }

  /**
   * Returns all neighbours. Modifiable.
   *
   * @return all neighbours
   */
  public Set<Node<T>> getNeighbours() {
    return neighbours;
  }

  /**
   * Adds a birdirectional connection.
   *
   * @param other the other node
   */
  public void addConnection(Node<T> other) {
    neighbours.add(other);
    other.neighbours.add(this);
  }

  /**
   * Returns whether this node is connected with the other node.
   *
   * @param other the other node
   * @return true if they are connected
   */
  public boolean isConnected(Node other) {
    return neighbours.contains(other);
  }

  /**
   * Returns whether physics should be calculated for this node.
   *
   * @return true if physics should be calculated for this node
   */
  public boolean isPausePhysics() {
    return pausePhysics;
  }

  /**
   * Sets whether physics should be calculated for this node.
   *
   * @param pausePhysics true if physics should be calculated for this node
   */
  public void setPausePhysics(boolean pausePhysics) {
    this.pausePhysics = pausePhysics;
  }
}

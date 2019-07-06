package me.ialistannen.algorithms.layout.forcedbased.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
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

  private ObservableMap<Node<T>, Edge<T>> neighbours;
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
    this.neighbours = FXCollections.observableHashMap();
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
    if (isPausePhysics()) {
      return;
    }
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
   * Returns all neighbours. Unmodifiable.
   *
   * @return all neighbours
   */
  public Set<Node<T>> getNeighbours() {
    return Collections.unmodifiableSet(neighbours.keySet());
  }

  /**
   * Returns all edges. Unmodifiable.
   *
   * @return all neighbours
   */
  public Collection<Edge<T>> getEdges() {
    return Collections.unmodifiableCollection(neighbours.values());
  }

  /**
   * Adds a change listener that is notified when edges change.
   *
   * @param changeListener the change listener
   */
  public void registerEdgeListener(MapChangeListener<Node<T>, Edge<T>> changeListener) {
    neighbours.addListener(changeListener);
  }

  /**
   * Adds a uni-directional connection.
   *
   * @param other the other node
   * @param weight the edge weight
   */
  public void addUnidirectionalConnection(Node<T> other, double weight) {
    if (other.isConnected(this)) {
      addBidirectionalConnection(other, weight);
    } else {
      neighbours.put(other, new Edge<>(this, other, false, weight));
    }
  }

  /**
   * Adds a bi-directional connection.
   *
   * @param other the other node
   * @param weight the edge weight
   */
  public void addBidirectionalConnection(Node<T> other, double weight) {
    neighbours.put(other, new Edge<>(this, other, true, weight));
    other.neighbours.put(this, new Edge<>(other, this, true, weight));
  }

  /**
   * Removes the connection with the given node.
   *
   * If the connection was bidirectional, it is downgraded to an unidirectional one.
   *
   * @param other the other node
   */
  public void removeConnectionWith(Node<T> other) {
    Edge<T> removed = neighbours.remove(other);

    if (removed != null && removed.isBidirectional()) {
      other.addUnidirectionalConnection(this, removed.getWeight());
    }
  }

  /**
   * Alters a single edge.
   *
   * @param edge the edge to alter
   */
  public void alterEdgeWeight(Edge<T> edge, double newWeight) {
    if (!neighbours.containsKey(edge.getEnd())) {
      return;
    }

    if (edge.isBidirectional()) {
      addBidirectionalConnection(edge.getEnd(), newWeight);
    } else {
      addUnidirectionalConnection(this, newWeight);
    }
  }

  /**
   * Returns whether this node is connected with the other node.
   *
   * @param other the other node
   * @return true if they are connected
   */
  public boolean isConnected(Node other) {
    return neighbours.containsKey(other);
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

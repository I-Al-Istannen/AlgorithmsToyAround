package me.ialistannen.pathfinding.visualize.algorithms;

import java.util.Objects;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;

public class BaseNode<T extends BaseNode<T>> {

  private double distanceToStart;
  private GridCoordinate coordinate;
  private GridCoordinate target;
  private T parent;

  public BaseNode(double distanceToStart, GridCoordinate coordinate, GridCoordinate target,
      T parent) {
    this.distanceToStart = distanceToStart;
    this.coordinate = coordinate;
    this.target = target;
    this.parent = parent;
  }

  public double getDistanceToStart() {
    return distanceToStart;
  }

  public void setDistanceToStart(double distanceToStart) {
    this.distanceToStart = distanceToStart;
  }

  public GridCoordinate getCoordinate() {
    return coordinate;
  }

  public GridCoordinate getTarget() {
    return target;
  }

  public T getParent() {
    return parent;
  }

  public void setParent(T parent) {
    this.parent = parent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseNode<?> baseNode = (BaseNode<?>) o;
    return Objects.equals(coordinate, baseNode.coordinate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordinate);
  }

  @Override
  public String toString() {
    return "BaseNode{" +
        "distanceToStart=" + distanceToStart +
        ", coordinate=" + coordinate +
        '}';
  }
}

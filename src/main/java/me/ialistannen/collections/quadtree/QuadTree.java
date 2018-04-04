package me.ialistannen.collections.quadtree;

import java.util.List;

public class QuadTree {

  private QuadNode rootNode;

  public QuadTree(Rectangle boundary, int maxPerCell) {
    rootNode = new QuadNode(boundary, maxPerCell);
  }

  /**
   * Inserts a point into the tree.
   *
   * @param point the point to add
   */
  public void insert(Point point) {
    rootNode.insert(point);
  }

  /**
   * Returns all points within the boundary.
   *
   * @param boundary the boundary
   * @return all points in the boundary
   */
  public List<Point> getAllPointsWithin(Rectangle boundary) {
    return rootNode.getAllPointsWithin(boundary);
  }

  /**
   * Returns the root {@link QuadNode}.
   * <p>
   * <strong>Leaking abstraction to allow for drawing the internals.</strong>
   *
   * @return the root node
   */
  public QuadNode getRootNode() {
    return rootNode;
  }

  @Override
  public String toString() {
    return rootNode.toString();
  }

}

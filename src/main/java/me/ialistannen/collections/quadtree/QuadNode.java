package me.ialistannen.collections.quadtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A single node in the tree
 */
public class QuadNode {

  private final Rectangle boundary;
  private final int maxPerCell;
  private final Point[] points;
  private boolean split;
  private int pointCount;

  private QuadNode northEast;
  private QuadNode southEast;
  private QuadNode southWest;
  private QuadNode northWest;


  QuadNode(Rectangle boundary, int maxPerCell) {
    this.boundary = boundary;
    this.maxPerCell = maxPerCell;

    this.points = new Point[maxPerCell];
  }

  /**
   * Inserts a point into this node, subdividing when necessary.
   *
   * @param point the point to insert
   */
  public void insert(Point point) {
    if (point == null || !boundary.contains(point)) {
      return;
    }

    if (split) {
      insertDivided(point);
    } else {
      insertNotDivided(point);
    }
  }

  private void insertDivided(Point point) {
    northEast.insert(point);
    southEast.insert(point);
    southWest.insert(point);
    northWest.insert(point);
  }

  private void insertNotDivided(Point point) {
    if (pointCount < maxPerCell) {
      points[pointCount++] = point;
    } else {
      subdivide();
      insert(point);
    }
  }

  private void subdivide() {
    split = true;
    int width = boundary.width / 2;
    int height = boundary.height / 2;
    int centerX = boundary.centerX;
    int centerY = boundary.centerY;

    Rectangle northEastBoundary = new Rectangle(
        centerX + width, centerY - height, width, height
    );
    northEast = new QuadNode(northEastBoundary, maxPerCell);

    Rectangle southEastBoundary = new Rectangle(
        centerX + width, centerY + height, width, height
    );
    southEast = new QuadNode(southEastBoundary, maxPerCell);

    Rectangle southWestBoundary = new Rectangle(
        centerX - width, centerY + height, width, height
    );
    southWest = new QuadNode(southWestBoundary, maxPerCell);

    Rectangle northWestBoundary = new Rectangle(
        centerX - width, centerY - height, width, height
    );
    northWest = new QuadNode(northWestBoundary, maxPerCell);

    for (Point point : points) {
      if (point != null) {
        insert(point);
      }
    }

    // clear out points, they are in a subtree now
    Arrays.fill(points, null);
    pointCount = 0;
  }

  /**
   * Returns all points in this Node and all subnodes, which lie inside the provided boundary.
   *
   * @param boundary the boundary to get them for
   * @return all nodes in that boundary
   */
  public List<Point> getAllPointsWithin(Rectangle boundary) {
    if (!this.boundary.intersects(boundary)) {
      return Collections.emptyList();
    }
    List<Point> points = new ArrayList<>();

    for (Point point : getPoints()) {
      if (point != null && boundary.contains(point)) {
        points.add(point);
      }
    }

    if (split) {
      points.addAll(northEast.getAllPointsWithin(boundary));
      points.addAll(southEast.getAllPointsWithin(boundary));
      points.addAll(southWest.getAllPointsWithin(boundary));
      points.addAll(northWest.getAllPointsWithin(boundary));
    }

    return points;
  }

  /**
   * Returns all points in this node.
   * <p>
   * <strong>Leaking abstraction to allow for drawing the internals.</strong>
   *
   * @return all points in this node. May contain nulls
   */
  public Point[] getPoints() {
    return points;
  }

  /**
   * Returns the boundary of this node.
   * <p>
   * <strong>Leaking abstraction to allow for drawing the internals.</strong>
   *
   * @return the boundary rectangle
   */
  public Rectangle getBoundary() {
    return boundary;
  }

  /**
   * Returns all child nodes.
   * <p>
   * <strong>Leaking abstraction to allow for drawing the internals.</strong>
   *
   * @return all child nodes. May contain nulls
   */
  public QuadNode[] children() {
    return new QuadNode[]{northEast, southEast, southWest, northWest};
  }

  /**
   * The maximum amount of points per cell.
   *
   * @return the maximum amount of points in a cell
   */
  public int getMaxPerCell() {
    return maxPerCell;
  }

  @Override
  public String toString() {
    return "QuadNode{" +
        "boundary=" + boundary +
        ", maxPerCell=" + maxPerCell +
        ", split=" + split +
        ", pointCount=" + pointCount +
        ", points=" + Arrays.toString(points) +
        '}';
  }
}

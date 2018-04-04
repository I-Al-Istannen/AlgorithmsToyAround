package me.ialistannen.collections.quadtree;

import java.util.Objects;

public class Rectangle {

  public final int centerX;
  public final int centerY;
  public final int width;
  public final int height;

  public Rectangle(int centerX, int centerY, int width, int height) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.width = width;
    this.height = height;
  }

  /**
   * Checks if a point is in a given rectangle
   *
   * @param point the point to check
   * @return true if the rectangle contains the point
   */
  public boolean contains(Point point) {
    return inRange(point.x, getMinX(), getMaxX())
        && inRange(point.y, getMinY(), getMaxY());
  }

  private boolean inRange(int value, int min, int max) {
    return value >= min && value < max;
  }

  /**
   * Checks if this rectangle intersects with another.
   *
   * @param other the other Rectangle to check
   * @return true if the rectangles intersect
   */
  public boolean intersects(Rectangle other) {
    return !(
        getMaxX() < other.getMinX() || getMinX() > other.getMaxX()
            || getMaxY() < other.getMinY() || getMinY() > other.getMaxY()
    );
  }

  private int getMinY() {
    return centerY - height;
  }

  private int getMinX() {
    return centerX - width;
  }

  private int getMaxY() {
    return centerY + height;
  }

  private int getMaxX() {
    return centerX + width;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Rectangle rectangle = (Rectangle) o;
    return centerX == rectangle.centerX &&
        centerY == rectangle.centerY &&
        width == rectangle.width &&
        height == rectangle.height;
  }

  @Override
  public int hashCode() {
    return Objects.hash(centerX, centerY, width, height);
  }

  @Override
  public String toString() {
    return "Rectangle{" +
        "centerX=" + centerX +
        ", centerY=" + centerY +
        ", width=" + width +
        ", height=" + height +
        '}';
  }
}

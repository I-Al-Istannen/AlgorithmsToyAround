package me.ialistannen.algorithms.layout.forcedbased;

import java.util.Objects;
import javafx.geometry.Point2D;

/**
 * A two dimensional vector.
 */
public class Vector2D {

  public static final Vector2D ZERO = new Vector2D(0, 0);
  private double x;
  private double y;

  /**
   * Creates a new vector.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public Vector2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the x coordinate.
   *
   * @return the x coordinate
   */
  public double getX() {
    return x;
  }

  /**
   * Returns the y coordinate.
   *
   * @return the y coordinate
   */
  public double getY() {
    return y;
  }

  /**
   * Returns a new vector with the given x coordinate, keeping this vector's x coordinate.
   *
   * @param x the x coordinate
   * @return the new vector
   */
  public Vector2D withX(double x) {
    return new Vector2D(x, this.y);
  }

  /**
   * Returns a new vector with the given y coordinate, keeping this vector's x coordinate.
   *
   * @param y the y coordinate
   * @return the new vector
   */
  public Vector2D withY(double y) {
    return new Vector2D(this.x, y);
  }

  /**
   * Subtracts the given vector from this one.
   *
   * @param delta the delta vector
   * @return the resulting vector
   */
  public Vector2D subtract(Vector2D delta) {
    return add(-delta.getX(), -delta.getY());
  }

  /**
   * Subtracts the given values.
   *
   * @param x the x delta
   * @param y the y delta
   * @return the resulting vector
   */
  public Vector2D subtract(double x, double y) {
    return new Vector2D(this.x + x, this.y + y);
  }

  /**
   * Adds the given vector to this one.
   *
   * @param delta the delta vector
   * @return the resulting vector
   */
  public Vector2D add(Vector2D delta) {
    return add(delta.getX(), delta.getY());
  }
  /**
   * Adds the given values.
   *
   * @param x the x delta
   * @param y the y delta
   * @return the resulting vector
   */
  public Vector2D add(double x, double y) {
    return new Vector2D(this.x + x, this.y + y);
  }

  /**
   * Multiplies this vector by the given scalar.
   *
   * @param scalar the given scalar
   * @return the new vector
   */
  public Vector2D multiply(double scalar) {
    return new Vector2D(x * scalar, y * scalar);
  }

  /**
   * Normalizes this vector.
   *
   * @return this vector
   */
  public Vector2D normalize() {
    double length = getLength();
    if (length == 0) {
      return ZERO;
    }
    return new Vector2D(
        getX() / length,
        getY() / length
    );
  }

  /**
   * Returns the squared distance to the other vector.
   *
   * @param other the other vector
   * @return the distance
   */
  public double distanceToSquared(Vector2D other) {
    return other.subtract(this).getLengthSquared();
  }

  /**
   * Returns the distance to the other vector.
   *
   * @param other the other vector
   * @return the distance
   */
  public double distanceTo(Vector2D other) {
    return Math.sqrt(distanceToSquared(other));
  }

  /**
   * Returns the manhatten distance to the other vector.
   *
   * @param other the other vector
   * @return the distance
   */
  public double manhattenTo(Vector2D other) {
    return Math.abs(other.getX() - getX()) + Math.abs(other.getY() - getY());
  }

  /**
   * Returns the chebychev distance to the other vector, which is basically a square.
   *
   * @param other the other vector
   * @return the distance
   */
  public double chebyshevDistanceTo(Vector2D other) {
    Vector2D diff = other.subtract(this);
    return Math.max(Math.abs(diff.getX()), Math.abs(diff.getY()));
  }

  /**
   * Returns the length of this vector.
   *
   * @return the length of this vector.
   */
  public double getLength() {
    return Math.sqrt(getLengthSquared());
  }

  /**
   * Returns the length of this vector, squared.
   *
   * @return the length of this vector, squared.
   */
  public double getLengthSquared() {
    return getX() * getX() + getY() * getY();
  }

  /**
   * Converts this vector to a {@link Point2D}.
   *
   * @return the resulting point
   */
  public Point2D toPoint2D() {
    return new Point2D(getX(), getY());
  }

  /**
   * Returns the absolute value of this vector. Applies {@link Math#abs(int)} to each component.
   *
   * @return the absolute vector
   */
  public Vector2D abs() {
    if (x >= 0 && y >= 0) {
      return this;
    }
    return new Vector2D(
        Math.abs(x), Math.abs(y)
    );
  }

  @Override
  public String toString() {
    return String.format("Vector2D{x=%.2f, y=%.2f}", x, y);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vector2D vector2D = (Vector2D) o;
    return Double.compare(vector2D.x, x) == 0 &&
        Double.compare(vector2D.y, y) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}

package me.ialistannen.algorithms.layout.forcedbased;

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

  @Override
  public String toString() {
    return String.format("Vector2D{x=%.2f, y=%.2f}", x, y);
  }
}

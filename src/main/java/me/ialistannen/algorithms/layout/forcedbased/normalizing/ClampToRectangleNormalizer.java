package me.ialistannen.algorithms.layout.forcedbased.normalizing;

import me.ialistannen.algorithms.layout.forcedbased.Vector2D;

/**
 * Clamps a node to stay inside a given rectangle.
 */
public class ClampToRectangleNormalizer implements NodePositionNormalizer {

  private double minX;
  private double maxX;
  private double minY;
  private double maxY;

  /**
   * Creates a new clamp to rectangle normalizer-
   *
   * @param minX the rectangle's min x
   * @param maxX the rectangle's max x
   * @param minY the rectangle's min y
   * @param maxY the rectangle's max y
   */
  public ClampToRectangleNormalizer(double minX, double maxX, double minY, double maxY) {
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
  }

  @Override
  public Vector2D normalize(Vector2D position) {
    Vector2D resultPos = position;
    if (position.getX() < minX) {
      resultPos = resultPos.withX(minX);
    } else if (position.getX() > maxX) {
      resultPos = resultPos.withX(maxX);
    }
    if (position.getY() < minY) {
      resultPos = resultPos.withY(minY);
    } else if (position.getY() > maxY) {
      resultPos = resultPos.withY(maxY);
    }
    return resultPos;
  }
}

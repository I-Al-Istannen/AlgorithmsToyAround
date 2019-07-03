package me.ialistannen.algorithms.layout.forcedbased.normalizing;

import me.ialistannen.algorithms.layout.forcedbased.Vector2D;

public class ClampToRectangleNormalizer implements NodePositionNormalizer {

  private double minX;
  private double maxX;
  private double minY;
  private double maxY;

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

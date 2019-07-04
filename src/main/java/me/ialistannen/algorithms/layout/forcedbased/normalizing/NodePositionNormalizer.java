package me.ialistannen.algorithms.layout.forcedbased.normalizing;

import me.ialistannen.algorithms.layout.forcedbased.Vector2D;

/**
 * Normalizes the node positions by e.g. clamping them.
 */
public interface NodePositionNormalizer {

  /**
   * Normalizes a given position.
   *
   * @param position the position
   * @return the resulting position
   */
  Vector2D normalize(Vector2D position);

  /**
   * Returns a no-operation normalizer that just returns the input.
   *
   * @return a no-operation normalizer that just returns the input
   */
  static NodePositionNormalizer nop() {
    return position -> position;
  }
}

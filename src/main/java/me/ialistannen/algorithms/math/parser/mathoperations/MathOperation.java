package me.ialistannen.algorithms.math.parser.mathoperations;

/**
 * A math operation.
 *
 * <p>Either a {@link Function} or an {@link Operator}
 */
public interface MathOperation {

  /**
   * @return The Keyword for this {@link MathOperation}.
   */
  String getKeyword();

}

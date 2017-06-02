package me.ialistannen.algorithms.math.parser.mathoperations;

import java.util.List;

/**
 * A function.
 */
public interface Function extends MathOperation {

  /**
   * @return The minimum number of arguments this function needs.
   */
  int getMinArgumentCount();

  /**
   * Returns the maximum number of arguments this method can take.
   * <p>Default implementation returns {@link #getMinArgumentCount()}
   *
   * @return The maximum number of arguments this method can take.
   */
  default int getMaxArgumentCount() {
    return getMinArgumentCount();
  }

  /**
   * Evaluates the function.
   *
   * @param values The values to apply it to
   * @return The return value of the function
   */
  double evaluate(List<Double> values);

  /**
   * @return The separator separating function arguments.
   */
  static String separator() {
    return ",";
  }
}

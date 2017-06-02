package me.ialistannen.algorithms.math.parser.mathoperations;

import java.util.List;

/**
 * A function.
 */
public interface Function extends MathOperation {

  /**
   * @return The argument count for a function.
   */
  int getArgumentCount();

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

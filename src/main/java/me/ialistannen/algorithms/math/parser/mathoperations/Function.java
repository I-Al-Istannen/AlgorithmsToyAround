package me.ialistannen.algorithms.math.parser.mathoperations;

/**
 * A function.
 */
public interface Function extends MathOperation {

  /**
   * @return The argument count for a function.
   */
  int getArgumentCount();

  /**
   * @return The separator separating function arguments.
   */
  static String separator() {
    return ",";
  }
}

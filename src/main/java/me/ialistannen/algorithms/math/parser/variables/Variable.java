package me.ialistannen.algorithms.math.parser.variables;

/**
 * A Variable
 */
public interface Variable {

  /**
   * @return The variable keyword
   */
  String getKeyword();

  /**
   * @return The value for the variable
   */
  double getValue();

  /**
   * @param value The new value for the variable
   */
  void setValue(double value);
}

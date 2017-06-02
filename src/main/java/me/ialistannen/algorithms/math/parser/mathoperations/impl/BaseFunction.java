package me.ialistannen.algorithms.math.parser.mathoperations.impl;

import me.ialistannen.algorithms.math.parser.mathoperations.Function;

/**
 * Basic {@link Function}s.
 */
public enum BaseFunction implements Function {
  SINUS(1, "sin"),
  COSINUS(1, "cos"),
  TANGENS(1, "tan"),
  MAX(2, "max");

  private int argumentCount;
  private String keyword;

  /**
   * @param argumentCount The number of arguments
   * @param keyword The keyword
   */
  BaseFunction(int argumentCount, String keyword) {
    this.argumentCount = argumentCount;
    this.keyword = keyword;
  }

  @Override
  public int getArgumentCount() {
    return argumentCount;
  }

  @Override
  public String getKeyword() {
    return keyword;
  }
}

package me.ialistannen.algorithms.math.parser.variables.impl;

import me.ialistannen.algorithms.math.parser.variables.Variable;

/**
 * Contains some standard {@link Variable}s
 */
public enum BaseVariables implements Variable {
  PI("PI", Math.PI),
  E("e", Math.E);

  private String keyword;
  private double value;

  /**
   * @param keyword The keyword  of the variable
   * @param value the value of the variable
   */
  BaseVariables(String keyword, double value) {
    this.keyword = keyword;
    this.value = value;
  }

  @Override
  public String getKeyword() {
    return keyword;
  }

  @Override
  public double getValue() {
    return value;
  }

  /**
   * <b>DOES NOTHING.</b>
   * <p>Standard variables are NOT CHANGEABLE.</p>
   *
   * @param value Ignored
   */
  @Override
  public void setValue(double value) {
  }
}

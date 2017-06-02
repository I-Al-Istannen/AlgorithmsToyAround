package me.ialistannen.algorithms.math.parser.mathoperations.impl;

import java.util.function.BinaryOperator;
import me.ialistannen.algorithms.math.parser.mathoperations.Associativity;
import me.ialistannen.algorithms.math.parser.mathoperations.Operator;

/**
 * Contains several basic {@link Operator}s.
 */
public enum BaseOperator implements Operator {
  ADD(0, Associativity.LEFT, "+", (aDouble, aDouble2) -> aDouble + aDouble2),
  SUBTRACT(0, Associativity.LEFT, "-", (aDouble, aDouble2) -> aDouble - aDouble2),
  MULTIPLY(1, Associativity.LEFT, "*", (aDouble, aDouble2) -> aDouble * aDouble2),
  DIVIDE(1, Associativity.LEFT, "/", (aDouble, aDouble2) -> aDouble / aDouble2),
  POWER(2, Associativity.RIGHT, "^", Math::pow);

  private int precedence;
  private Associativity associativity;
  private String keyword;
  private BinaryOperator<Double> computer;

  /**
   * @param precedence The Precedence
   * @param associativity The {@link Associativity} of the {@link Operator}
   * @param keyword The keyword for the operator, e.g. '+' for plus
   */
  BaseOperator(int precedence, Associativity associativity, String keyword,
      BinaryOperator<Double> compute) {
    this.precedence = precedence;
    this.associativity = associativity;
    this.keyword = keyword;
    this.computer = compute;
  }

  @Override
  public int getPrecedence() {
    return precedence;
  }

  @Override
  public Associativity getAssociativity() {
    return associativity;
  }

  @Override
  public double evaluate(double... values) {
    return computer.apply(values[0], values[1]);
  }

  @Override
  public String getKeyword() {
    return keyword;
  }

}

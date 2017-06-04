package me.ialistannen.algorithms.math.parser.mathoperations.impl;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import me.ialistannen.algorithms.math.parser.mathoperations.Associativity;
import me.ialistannen.algorithms.math.parser.mathoperations.Operator;

/**
 * Contains several basic {@link Operator}s.
 */
public enum BaseOperator implements Operator {
  UNARY_PLUS(4, Associativity.RIGHT, "+", true, doubles -> doubles.get(0)),
  UNARY_MINUS(4, Associativity.RIGHT, "-", true, doubles -> -1 * doubles.get(0)),

  POWER(2, Associativity.RIGHT, "^", Math::pow),

  MULTIPLY(1, Associativity.LEFT, "*", (o1, o2) -> o1 * o2),
  DIVIDE(1, Associativity.LEFT, "/", (o1, o2) -> o1 / o2),
  MODULO(1, Associativity.LEFT, "%", (o1, o2) -> o1 % o2),

  ADD(0, Associativity.LEFT, "+", (o1, o2) -> o1 + o2),
  SUBTRACT(0, Associativity.LEFT, "-", (o1, o2) -> o1 - o2);

  private int precedence;
  private Associativity associativity;
  private String keyword;
  private boolean unary;
  private Function<List<Double>, Double> computer;

  /**
   * @param precedence The Precedence
   * @param associativity The {@link Associativity} of the {@link Operator}
   * @param keyword The keyword for the operator, e.g. '+' for plus
   * @param compute The computing function
   */
  BaseOperator(int precedence, Associativity associativity, String keyword,
      BinaryOperator<Double> compute) {
    this(precedence, associativity, keyword,
        false,
        doubles -> compute.apply(doubles.get(0), doubles.get(1))
    );
  }

  /**
   * @param precedence The Precedence
   * @param associativity The {@link Associativity} of the {@link Operator}
   * @param keyword The keyword for the operator, e.g. '+' for plus
   * @param compute The computing function
   */
  BaseOperator(int precedence, Associativity associativity, String keyword,
      boolean unary, Function<List<Double>, Double> compute) {
    this.precedence = precedence;
    this.associativity = associativity;
    this.keyword = keyword;
    this.unary = unary;
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
  public double evaluate(List<Double> values) {
    return computer.apply(values);
  }

  @Override
  public boolean isUnary() {
    return unary;
  }

  @Override
  public String getKeyword() {
    return keyword;
  }
}

package me.ialistannen.algorithms.math.parser.mathoperations.impl;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Basic {@link me.ialistannen.algorithms.math.parser.mathoperations.Function}s.
 */
public enum BaseFunction implements me.ialistannen.algorithms.math.parser.mathoperations.Function {
  SINUS(1, "sin", getOperator(Math::sin)),
  COSINUS(1, "cos", getOperator(Math::cos)),
  TANGENS(1, "tan", getOperator(Math::tan)),
  MAX(2, "max", getOperator(Math::max)),
  SUM(1, Integer.MAX_VALUE, "sum", args -> args.stream().mapToDouble(Double::doubleValue).sum());

  private int minArgumentCount;
  private int maxArgumentCount;
  private String keyword;
  private Function<List<Double>, Double> computer;

  /**
   * @param argumentCount The number of arguments. Will be the minimum and the maximum one
   * @param keyword The keyword
   * @param computer The computing function
   * @see #BaseFunction(int, int, String, Function)
   */
  BaseFunction(int argumentCount, String keyword, Function<List<Double>, Double> computer) {
    this(argumentCount, argumentCount, keyword, computer);
  }

  /**
   * @param minArgumentCount The minimum number of arguments
   * @param maxArgumentCount The maximum number of arguments
   * @param keyword The keyword
   * @param computer The computing function
   */
  BaseFunction(int minArgumentCount, int maxArgumentCount, String keyword,
      Function<List<Double>, Double> computer) {
    this.minArgumentCount = minArgumentCount;
    this.maxArgumentCount = maxArgumentCount;
    this.keyword = keyword;
    this.computer = computer;
  }

  @Override
  public double evaluate(List<Double> values) {
    return computer.apply(values);
  }

  @Override
  public int getMinArgumentCount() {
    return minArgumentCount;
  }

  @Override
  public int getMaxArgumentCount() {
    return maxArgumentCount;
  }

  @Override
  public String getKeyword() {
    return keyword;
  }

  private static Function<List<Double>, Double> getOperator(UnaryOperator<Double> operator) {
    return doubles -> operator.apply(doubles.get(0));
  }

  private static Function<List<Double>, Double> getOperator(BinaryOperator<Double> operator) {
    return doubles -> operator.apply(doubles.get(0), doubles.get(1));
  }
}

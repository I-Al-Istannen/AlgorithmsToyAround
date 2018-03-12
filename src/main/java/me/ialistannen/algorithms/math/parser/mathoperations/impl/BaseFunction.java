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
  ARC_SIN(1, "asin", getOperator(Math::asin)),
  ARC_COS(1, "acos", getOperator(Math::acos)),
  ARC_TAN(1, "atan", getOperator(Math::atan)),
  ARC_TAN_2(1, "atan2", getOperator(Math::atan2)),
  TO_DEGREES(1, "to_degree", getOperator(Math::toDegrees)),
  TO_RADIANS(1, "to_radian", getOperator(Math::toRadians)),

  MIN(2, "min", getOperator(Math::min)),
  MAX(2, "max", getOperator(Math::max)),

  // because otherwise javac things that is ambiguous
  ABSOLUTE(1, "abs", getOperator(Math::abs)),
  // because otherwise javac things that is ambiguous
  SIGNUM(1, "signum", getOperator(Math::signum)),

  EXP(1, "exp", getOperator(Math::exp)),
  LOG(1, "log", getOperator(Math::log10)),
  LN(1, "ln", getOperator(Math::log)),

  FLOOR(1, "floor", getOperator(Math::floor)),
  CEIL(1, "ceil", getOperator(Math::ceil)),
  ROUND(1, "round", getOperator(x -> (double) Math.round(x))),

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

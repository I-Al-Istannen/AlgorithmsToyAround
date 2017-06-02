package me.ialistannen.algorithms.math.parser.mathoperations;

import java.util.List;

/**
 * An Operator.
 */
public interface Operator extends MathOperation {

  /**
   * @return The {@link Operator}'s precedence.
   */
  int getPrecedence();

  /**
   * @return The {@link Operator}'s {@link Associativity}.
   */
  Associativity getAssociativity();

  /**
   * Returns true if this is an unary operator.
   *
   * @return true if this is an unary operator.
   */
  boolean isUnary();

  /**
   * @param other The other {@link Operator}
   * @return True if this one has lesser precedence
   */
  default boolean hasLesserPrecedence(Operator other) {
    return getAssociativity() == Associativity.RIGHT && getPrecedence() < other.getPrecedence()
        || getAssociativity() == Associativity.LEFT && getPrecedence() <= other.getPrecedence();
  }

  /**
   * Applies an operator to some input values.
   *
   * @param values The values passed to this Operator
   * @return The result of applying the operator to the arguments
   */
  double evaluate(List<Double> values);
}

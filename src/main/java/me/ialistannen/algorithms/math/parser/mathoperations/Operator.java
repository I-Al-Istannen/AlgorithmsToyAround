package me.ialistannen.algorithms.math.parser.mathoperations;

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
   * @param other The other {@link Operator}
   * @return True if this one has lesser precedence
   */
  default boolean hasLesserPrecedence(Operator other) {
    return getAssociativity() == Associativity.RIGHT && getPrecedence() < other.getPrecedence()
        || getAssociativity() == Associativity.LEFT && getPrecedence() <= other.getPrecedence();
  }

  double evaluate(double... values);
}

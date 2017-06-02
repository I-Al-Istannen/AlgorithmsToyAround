package me.ialistannen.algorithms.math.parser.token;

import java.util.Objects;
import me.ialistannen.algorithms.math.parser.mathoperations.Operator;
import me.ialistannen.treeviewer.Tree;

/**
 * A {@link Token} for an {@link Operator}
 */
public class OperatorToken extends BaseToken {

  private Operator operator;

  /**
   * @param tokenText The text of the token
   * @param operator The {@link Operator} this token represents
   */
  OperatorToken(String tokenText, Operator operator) {
    super(tokenText, TokenType.OPERATOR);
    this.operator = operator;
  }

  /**
   * @return The {@link Operator} this token represents
   */
  public Operator getOperator() {
    return operator;
  }

  @Override
  public double evaluate(Tree node) {
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperatorToken that = (OperatorToken) o;
    return Objects.equals(getOperator(), that.getOperator());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getOperator());
  }

  @Override
  public String toString() {
    return "OperatorToken{"
        + "operator=" + operator
        + '}';
  }
}

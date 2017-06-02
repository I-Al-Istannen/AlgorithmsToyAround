package me.ialistannen.algorithms.math.parser.token;

import java.util.Objects;
import me.ialistannen.treeviewer.Tree;

/**
 * A {@link Token} storing a concrete value.
 */
public class ValueToken extends BaseToken implements Comparable<ValueToken> {

  private double value;

  /**
   * @param tokenText The tokenText of the token
   * @param value The value of this token.
   */
  public ValueToken(String tokenText, double value) {
    super(tokenText, TokenType.NUMBER);
    this.value = value;
  }

  /**
   * @return The value
   */
  public double getValue() {
    return value;
  }

  @Override
  public double evaluate(Tree node) {
    return getValue();
  }

  @Override
  public int compareTo(ValueToken o) {
    return Double.compare(getValue(), o.getValue());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValueToken that = (ValueToken) o;
    return Double.compare(that.getValue(), getValue()) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }

  @Override
  public String toString() {
    return "ValueToken{"
        + "value=" + value
        + '}';
  }
}

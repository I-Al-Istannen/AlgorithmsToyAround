package me.ialistannen.algorithms.math.parser.token;

import java.util.Objects;
import me.ialistannen.algorithms.math.parser.mathoperations.Function;
import me.ialistannen.treeviewer.Tree;

/**
 * A {@link Token} for a function
 */
public class FunctionToken extends BaseToken {

  private Function function;

  /**
   * @param tokenText The text of the token
   * @param function The {@link Function}
   */
  FunctionToken(String tokenText, Function function) {
    super(tokenText, TokenType.FUNCTION);
    this.function = function;
  }

  /**
   * @return The {@link Function} for this token
   */
  public Function getFunction() {
    return function;
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
    FunctionToken that = (FunctionToken) o;
    return Objects.equals(getFunction(), that.getFunction());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getFunction());
  }

  @Override
  public String toString() {
    return "FunctionToken{"
        + "function=" + function
        + '}';
  }
}

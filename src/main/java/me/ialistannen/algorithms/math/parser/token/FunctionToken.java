package me.ialistannen.algorithms.math.parser.token;

import java.util.Objects;
import me.ialistannen.algorithms.math.parser.mathoperations.Function;
import me.ialistannen.treeviewer.model.Tree;

/**
 * A {@link Token} for a function
 */
public class FunctionToken extends BaseToken {

  private Function function;
  private int registeredArgumentCount = 0;

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
    return getFunction().evaluate(getChildrenValues(node));
  }

  /**
   * Increments the internal counter for arguments
   */
  public void addRegisteredArgumentCount() {
    this.registeredArgumentCount++;
  }

  /**
   * @return the internal counter for arguments
   */
  public int getRegisteredArgumentCount() {
    return registeredArgumentCount;
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

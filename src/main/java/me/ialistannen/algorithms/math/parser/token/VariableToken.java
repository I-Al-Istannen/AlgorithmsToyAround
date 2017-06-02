package me.ialistannen.algorithms.math.parser.token;

import me.ialistannen.algorithms.math.parser.variables.Variable;
import me.ialistannen.treeviewer.Tree;

/**
 * A Token for a {@link Variable}
 */
public class VariableToken extends BaseToken {

  private final Variable variable;

  /**
   * @param tokenText The text of the token
   * @param variable The {@link Variable}
   */
  VariableToken(String tokenText, Variable variable) {
    super(tokenText, TokenType.VARIABLE);
    this.variable = variable;
  }

  /**
   * @return The {@link Variable}
   */
  public Variable getVariable() {
    return variable;
  }

  @Override
  public double evaluate(Tree node) {
    return variable.getValue();
  }
}

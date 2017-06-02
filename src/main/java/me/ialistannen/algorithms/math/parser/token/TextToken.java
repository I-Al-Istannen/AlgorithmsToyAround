package me.ialistannen.algorithms.math.parser.token;

import me.ialistannen.treeviewer.Tree;

/**
 * A token that simply holds some text (e.g. Like parentheses)
 */
public class TextToken extends BaseToken {

  /**
   * @param tokenText The text of the token
   * @param tokenType The type of the {@link Token}
   */
  TextToken(String tokenText, TokenType tokenType) {
    super(tokenText, tokenType);
  }

  @Override
  public double evaluate(Tree node) {
    return 0;
  }
}

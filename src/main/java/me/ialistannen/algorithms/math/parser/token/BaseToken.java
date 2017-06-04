package me.ialistannen.algorithms.math.parser.token;

import java.util.ArrayList;
import java.util.List;
import me.ialistannen.treeviewer.model.Tree;

/**
 * A skeleton {@link Token}.
 */
public abstract class BaseToken implements Token {

  private String tokenText;
  private TokenType tokenType;

  /**
   * @param tokenText The text of the token
   * @param tokenType The type of the {@link Token}
   */
  BaseToken(String tokenText, TokenType tokenType) {
    this.tokenText = tokenText;
    this.tokenType = tokenType;
  }

  /**
   * @return The {@link Token} text
   */
  public String getTokenText() {
    return tokenText;
  }

  /**
   * @return The {@link TokenType}
   */
  @Override
  public TokenType getType() {
    return tokenType;
  }

  /**
   * Evaluates all children and returns the results.
   *
   * @param node The current node
   * @return The results of evaluating all of its children
   */
  protected List<Double> getChildrenValues(Tree node) {
    List<Double> childResults = new ArrayList<>();
    for (Tree tree : node.getChildren()) {
      childResults.add(tree.evaluate());
    }
    return childResults;
  }


  @Override
  public String toString() {
    return "BaseToken{"
        + "tokenText='" + tokenText + '\''
        + ", tokenType=" + tokenType
        + '}';
  }
}

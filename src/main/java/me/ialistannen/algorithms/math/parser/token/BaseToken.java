package me.ialistannen.algorithms.math.parser.token;

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

  @Override
  public String toString() {
    return "BaseToken{"
        + "tokenText='" + tokenText + '\''
        + ", tokenType=" + tokenType
        + '}';
  }
}

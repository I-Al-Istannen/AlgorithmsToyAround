package me.ialistannen.algorithms.math.parser.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.ialistannen.algorithms.math.parser.mathoperations.Function;
import me.ialistannen.algorithms.math.parser.mathoperations.MathOperation;
import me.ialistannen.algorithms.math.parser.mathoperations.Operator;
import me.ialistannen.algorithms.math.parser.mathoperations.impl.BaseFunction;
import me.ialistannen.algorithms.math.parser.mathoperations.impl.BaseOperator;

/**
 * Tokenize a String in usable math tokens
 */
public class Tokenizer {

  private static final Pattern NUMBER_PATTERN = Pattern.compile(
      "([+-])?\\d+(\\.\\d+)?(e([+-])?\\d+)?"
  );

  private Map<String, MathOperation> mathOperationMap;

  /**
   * Creates a new {@link Tokenizer} with the default {@link MathOperation}s.
   *
   * @see #addDefaultMathOperations()
   */
  public Tokenizer() {
    this.mathOperationMap = new HashMap<>();
    addDefaultMathOperations();
  }

  /**
   * Registers the passed {@link MathOperation}.
   *
   * @param operation The {@link MathOperation} to add
   */
  public void addMathOperation(MathOperation operation) {
    mathOperationMap.put(operation.getKeyword(), operation);
  }

  /**
   * Registers all passed {@link MathOperation}s.
   *
   * @param operation The {@link MathOperation}s to add
   * @see #addMathOperation(MathOperation)
   */
  public void addAllMathOperations(Collection<MathOperation> operation) {
    operation.forEach(this::addMathOperation);
  }

  /**
   * Adds the default {@link MathOperation}s.
   *
   * <p>Contains {@link BaseFunction} and {@link BaseOperator}.
   */
  public void addDefaultMathOperations() {
    addAllMathOperations(Arrays.asList(BaseFunction.values()));
    addAllMathOperations(Arrays.asList(BaseOperator.values()));
  }

  /**
   * Tokenizes an input string.
   *
   * @param input The input to tokenize
   * @return The tokenized input
   */
  public List<Token> tokenize(String input) {
    List<Token> tokens = new ArrayList<>();

    String currentString = input.trim().replaceAll("\\s", "");
    TokenType nextTokenType;
    while ((nextTokenType = getNextTokenType(currentString)) != TokenType.END_OF_FILE) {
      Token token;
      switch (nextTokenType) {
        case FUNCTION:
        case OPERATOR:
          Optional<MathOperation> operationOptional = readMathOperation(currentString);
          if (!operationOptional.isPresent()) {
            throw new IllegalArgumentException(
                "Error parsing an operator/function '" + currentString + "'. Probably not known?"
            );
          }
          MathOperation operation = operationOptional.get();
          if (operation instanceof Operator) {
            token = new OperatorToken(operation.getKeyword(), (Operator) operation);
          } else {
            token = new FunctionToken(operation.getKeyword(), (Function) operation);
          }

          break;
        case NUMBER:
          Optional<ValueToken> tokenOptional = readNumber(currentString);
          if (!tokenOptional.isPresent()) {
            throw new IllegalArgumentException(
                "Error parsing a number. Either no number specified or it is not a valid double: '"
                    + currentString + "'"
            );
          }
          token = tokenOptional.get();

          break;
        case PARENTHESIS:
          token = new TextToken(currentString.substring(0, 1), TokenType.PARENTHESIS);
          break;
        case FUNCTION_SEPARATOR:
          token = new TextToken(Function.separator(), TokenType.FUNCTION_SEPARATOR);
          break;
        default:
          throw new IllegalArgumentException("Unknown token type: '" + nextTokenType + "'");
      }
      currentString = currentString.substring(
          token.getTokenText().length(), currentString.length()
      );
      tokens.add(token);
    }

    return tokens;
  }

  private Optional<ValueToken> readNumber(String currentString) {
    if (currentString == null || currentString.isEmpty()) {
      return Optional.empty();
    }
    Matcher matcher = NUMBER_PATTERN.matcher(currentString);
    if (!matcher.find()) {
      return Optional.empty();
    }
    int end = matcher.end();
    try {
      String doubleString = currentString.substring(0, end);
      return Optional.of(new ValueToken(
          doubleString,
          Double.parseDouble(doubleString)
      ));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }


  private TokenType getNextTokenType(String input) {
    if (input == null || input.isEmpty()) {
      return TokenType.END_OF_FILE;
    }
    if (input.startsWith("(") || input.startsWith(")")) {
      return TokenType.PARENTHESIS;
    }
    if (input.startsWith(Function.separator())) {
      return TokenType.FUNCTION_SEPARATOR;
    }
    if (Character.isDigit(input.charAt(0))) {
      return TokenType.NUMBER;
    }
    return TokenType.OPERATOR;
  }

  /**
   * @param input The input String
   * @return The Operator, if any
   */
  private Optional<MathOperation> readMathOperation(String input) {
    MathOperation foundOne = null;
    for (int i = 1; i <= input.length(); i++) {
      String part = input.substring(0, i);
      if (mathOperationMap.containsKey(part)) {
        foundOne = mathOperationMap.get(part);
      } else if (foundOne != null) {
        // we found one before ==> probably read too much
        return Optional.of(foundOne);
      }
    }
    return Optional.empty();
  }
}

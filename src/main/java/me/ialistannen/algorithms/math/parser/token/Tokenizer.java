package me.ialistannen.algorithms.math.parser.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.ialistannen.algorithms.math.parser.mathoperations.Function;
import me.ialistannen.algorithms.math.parser.mathoperations.MathOperation;
import me.ialistannen.algorithms.math.parser.mathoperations.Operator;
import me.ialistannen.algorithms.math.parser.mathoperations.impl.BaseFunction;
import me.ialistannen.algorithms.math.parser.mathoperations.impl.BaseOperator;
import me.ialistannen.collections.CheapMultiMap;

/**
 * Tokenize a String in usable math tokens
 */
public class Tokenizer {

  private static final Pattern NUMBER_PATTERN = Pattern.compile(
      "([+-])?\\d+(\\.\\d+)?(e([+-])?\\d+)?"
  );

  private CheapMultiMap<String, MathOperation> mathOperationMap;

  /**
   * Creates a new {@link Tokenizer} with the default {@link MathOperation}s.
   *
   * @see #addDefaultMathOperations()
   */
  public Tokenizer() {
    this.mathOperationMap = new CheapMultiMap<>();
    addDefaultMathOperations();
  }

  /**
   * Registers the passed {@link MathOperation}.
   *
   * @param operation The {@link MathOperation} to add
   */
  public void addMathOperation(MathOperation operation) {
    mathOperationMap.putSingle(operation.getKeyword(), operation);
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
          Optional<MathOperation> operationOptional = readMathOperation(currentString, tokens);
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

    tokens.forEach(System.out::println);

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
  private Optional<MathOperation> readMathOperation(String input, List<Token> previousTokens) {
    MathOperation foundOne = null;
    for (int i = 1; i <= input.length(); i++) {
      String part = input.substring(0, i);
      if (mathOperationMap.containsKey(part)) {
        if (hasOperatorForKeyword(part)) {
          foundOne = getMathOperator(part, input, previousTokens);
        } else {
          foundOne = mathOperationMap.get(part).get(0);
        }
      } else if (foundOne != null) {
        // we found one before ==> probably read too much
        return Optional.of(foundOne);
      }
    }
    return Optional.empty();
  }

  private boolean hasOperatorForKeyword(String keyword) {
    return mathOperationMap.containsKey(keyword)
        && mathOperationMap.get(keyword).stream()
        .anyMatch(operation -> operation instanceof Operator);
  }

  private Operator getMathOperator(String keyword, String fullExpression, List<Token> previous) {
    boolean useUnary;
    if (previous.isEmpty()) {
      useUnary = true;
    } else {
      Token lastToken = previous.get(previous.size() - 1);
      useUnary = lastToken.getTokenText().equals("(")
          || lastToken.getType() == TokenType.OPERATOR;
    }
    if (useUnary) {
      return getUnary(keyword).orElseThrow(() -> new IllegalArgumentException(String.format(
          "Wanted an unary operator for keyword '%s', found none. Full: '%s'",
          keyword, fullExpression
      )));
    } else {
      return getBinary(keyword).orElseThrow(() -> new IllegalArgumentException(String.format(
          "Wanted a binary operator for keyword '%s', found none. Full: '%s'",
          keyword, fullExpression
      )));
    }
  }

  /**
   * @param key The keyword for the operator
   * @return The Operator's unary variant, if any
   */
  private Optional<Operator> getUnary(String key) {
    if (!mathOperationMap.containsKey(key)) {
      return Optional.empty();
    }
    return mathOperationMap.get(key).stream()
        .filter(mathOperation -> mathOperation instanceof Operator)
        .map(mathOperation -> (Operator) mathOperation)
        .filter(Operator::isUnary)
        .findFirst();
  }

  /**
   * @param key The keyword for the operator
   * @return The Operator's binary variant, if any
   */
  private Optional<Operator> getBinary(String key) {
    if (!mathOperationMap.containsKey(key)) {
      return Optional.empty();
    }
    return mathOperationMap.get(key).stream()
        .filter(mathOperation -> mathOperation instanceof Operator)
        .map(mathOperation -> (Operator) mathOperation)
        .filter(operator -> !operator.isUnary())
        .findFirst();
  }
}

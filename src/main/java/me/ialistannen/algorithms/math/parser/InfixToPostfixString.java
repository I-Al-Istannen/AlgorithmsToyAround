package me.ialistannen.algorithms.math.parser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;
import me.ialistannen.algorithms.math.parser.mathoperations.Operator;
import me.ialistannen.algorithms.math.parser.token.FunctionToken;
import me.ialistannen.algorithms.math.parser.token.OperatorToken;
import me.ialistannen.algorithms.math.parser.token.Token;
import me.ialistannen.algorithms.math.parser.token.TokenType;
import me.ialistannen.algorithms.math.parser.token.Tokenizer;

/**
 * A converter to convert Infix to postfix notation.
 */
public class InfixToPostfixString {

  private Tokenizer tokenizer = new Tokenizer();
  private Queue<Token> outputQueue;
  private Deque<Token> operatorStack;

  /**
   * @return The {@link Tokenizer} for this {@link InfixToPostfixString}.
   */
  public Tokenizer getTokenizer() {
    return tokenizer;
  }

  /**
   * Converts infix to postfix
   *
   * @param input The input in infix
   * @return The input in postfix notation
   */
  public String toPostfix(String input) {
    resetState();
    Deque<Token> tokens = new ArrayDeque<>(tokenizer.tokenize(input));
    while (!tokens.isEmpty()) {
      Token token = tokens.pollFirst();

      switch (token.getType()) {
        case NUMBER:
          outputQueue.add(token);
          break;
        case FUNCTION:
          operatorStack.addFirst(token);
          break;
        case FUNCTION_SEPARATOR:
          handleFunctionSeparator();
          break;
        case OPERATOR:
          handleOperator(token);
          break;
        case PARENTHESIS:
          handleParenthesis(token);
          break;
      }
    }

    if (!operatorStack.isEmpty() && operatorStack.peekFirst().getType() == TokenType.PARENTHESIS) {
      throw new IllegalArgumentException("Unbalanced parentheses.");
    }
    while (!operatorStack.isEmpty()) {
      outputQueue.add(operatorStack.pollFirst());
    }

    return outputQueue.stream()
        .map(Token::getTokenText)
        .collect(Collectors.joining(" "));
  }

  private void handleFunctionSeparator() {
    while (!operatorStack.isEmpty()) {
      Token first = operatorStack.peekFirst();
      if (first.getTokenText().equals("(")) {
        return;
      }
      outputQueue.add(operatorStack.pollFirst());
    }
    throw new IllegalArgumentException("Unbalanced parentheses.");
  }

  /**
   * @param token The current token
   */
  private void handleOperator(Token token) {
    Operator currentOperator = ((OperatorToken) token).getOperator();

    while (!operatorStack.isEmpty() && operatorStack.peekFirst() instanceof OperatorToken) {
      Operator top = ((OperatorToken) operatorStack.peekFirst()).getOperator();
      if (currentOperator.hasLesserPrecedence(top)) {
        outputQueue.add(operatorStack.pollFirst());
      } else {
        break;
      }
    }

    operatorStack.addFirst(token);
  }

  /**
   * @param token The current token
   */
  private void handleParenthesis(Token token) {
    if (token.getTokenText().equals("(")) {
      operatorStack.addFirst(token);
      return;
    }
    while (!operatorStack.isEmpty()) {
      Token first = operatorStack.pollFirst();
      if (first.getTokenText().equals("(")) {
        if (operatorStack.peekFirst() instanceof FunctionToken) {
          outputQueue.add(operatorStack.pollFirst());
        }
        return;
      } else {
        outputQueue.add(first);
      }
    }
    throw new IllegalArgumentException("Unbalanced parentheses.");
  }

  private void resetState() {
    outputQueue = new ArrayDeque<>();
    operatorStack = new ArrayDeque<>();
  }

  public static void main(String[] args) {
    InfixToPostfixString infixToPostfixString = new InfixToPostfixString();

    String defaultInput = "sin ( max ( 2, 3 ) / 3 * 3.1415 )";

    Scanner scanner = new Scanner(System.in);

    System.out.printf("Your input [%s]: ", defaultInput);
    String input = scanner.nextLine();

    if (input.isEmpty()) {
      input = defaultInput;
    }

    System.out.println("Input:\n" + input);
    System.out.println("\nOutput:\n" + infixToPostfixString.toPostfix(input));
  }
}

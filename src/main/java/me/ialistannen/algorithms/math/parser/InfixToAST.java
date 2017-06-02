package me.ialistannen.algorithms.math.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import me.ialistannen.algorithms.math.parser.ast.Node;
import me.ialistannen.algorithms.math.parser.mathoperations.Operator;
import me.ialistannen.algorithms.math.parser.token.FunctionToken;
import me.ialistannen.algorithms.math.parser.token.OperatorToken;
import me.ialistannen.algorithms.math.parser.token.Token;
import me.ialistannen.algorithms.math.parser.token.TokenType;
import me.ialistannen.algorithms.math.parser.token.Tokenizer;
import me.ialistannen.algorithms.math.parser.token.ValueToken;

/**
 * Converts an infix String to an AST.
 */
public class InfixToAST {

  private Tokenizer tokenizer;
  private Deque<Token> operatorStack;
  private Deque<Node> outputStack;

  /**
   * Instantiates a new converter.
   */
  public InfixToAST() {
    this.tokenizer = new Tokenizer();
  }

  public Node generateAst(String input) {
    resetState();

    Queue<Token> tokens = new ArrayDeque<>(tokenizer.tokenize(input));

    while (!tokens.isEmpty()) {
      Token token = tokens.poll();

      switch (token.getType()) {
        case NUMBER:
          addNodeToOutput(token);
          break;
        case PARENTHESIS:
          handleParenthesis(token);
          break;
        case FUNCTION_SEPARATOR:
          handleFunctionSeparator();
          break;
        case FUNCTION:
          operatorStack.addFirst(token);
          break;
        case OPERATOR:
          handleOperator(token);
          break;
      }
    }

    if (!operatorStack.isEmpty() && operatorStack.peekFirst().getType() == TokenType.PARENTHESIS) {
      throw new IllegalArgumentException("Unbalanced parenthesis.");
    }
    System.out.println(operatorStack);
    while (!operatorStack.isEmpty()) {
      addNodeToOutput(operatorStack.pollFirst());
    }

    System.out.println("Left over: " +
        outputStack.stream()
            .map(Node::getToken)
            .map(Token::getTokenText)
            .collect(Collectors.joining(" "))
    );

    return outputStack.peekFirst();
  }

  private void handleOperator(Token token) {
    if (operatorStack.isEmpty()) {
      operatorStack.addFirst(token);
      return;
    }
    Operator currentOperator = ((OperatorToken) token).getOperator();
    // instanceof to break before e.g. a parenthesis
    while (!operatorStack.isEmpty() && operatorStack.peekFirst() instanceof OperatorToken) {
      Operator top = ((OperatorToken) operatorStack.peekFirst()).getOperator();
      if (currentOperator.hasLesserPrecedence(top)) {
        addNodeToOutput(operatorStack.pollFirst());
      } else {
        break;
      }
    }

    operatorStack.addFirst(token);
  }

  private void handleFunctionSeparator() {
    while (!operatorStack.isEmpty()) {
      Token first = operatorStack.peekFirst();
      if (first.getTokenText().equals("(")) {
        return;
      }
      addNodeToOutput(operatorStack.pollFirst());
    }
    throw new IllegalArgumentException("Unbalanced parenthesis when trying to parse a function.");
  }

  private void handleParenthesis(Token token) {
    if (token.getTokenText().equals("(")) {
      operatorStack.addFirst(token);
      return;
    }

    while (!operatorStack.isEmpty()) {
      Token first = operatorStack.pollFirst();
      if (first.getTokenText().equals("(")) {
        if (operatorStack.peekFirst() instanceof FunctionToken) {
          addNodeToOutput(operatorStack.pollFirst());
        }
        return;
      }
      addNodeToOutput(first);
    }
    throw new IllegalArgumentException("Unbalanced parenthesis.");
  }

  private void addNodeToOutput(Token token) {
    if (token instanceof ValueToken) {
      outputStack.addFirst(new Node(token));
      return;
    }
    if (token instanceof FunctionToken) {
      int argumentCount = ((FunctionToken) token).getFunction().getArgumentCount();

      Node functionNode = new Node(token);
      functionNode.addChildren(pollNodesAndReverse(argumentCount));

      outputStack.addFirst(functionNode);
      return;
    }
    if (token instanceof OperatorToken) {
      int argumentCount = ((OperatorToken) token).getOperator().isUnary() ? 1 : 2;

      Node operatorNode = new Node(token);
      operatorNode.addChildren(pollNodesAndReverse(argumentCount));

      outputStack.addFirst(operatorNode);
      return;
    }
    throw new IllegalArgumentException("Unknown token: " + token);
  }

  private List<Node> pollNodesAndReverse(int count) {
    List<Node> nodes = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      nodes.add(outputStack.pollFirst());
    }
    Collections.reverse(nodes);
    return nodes;
  }

  private void resetState() {
    operatorStack = new ArrayDeque<>();
    outputStack = new ArrayDeque<>();
  }
}

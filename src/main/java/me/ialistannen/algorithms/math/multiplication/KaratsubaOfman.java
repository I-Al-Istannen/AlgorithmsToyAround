package me.ialistannen.algorithms.math.multiplication;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import me.ialistannen.algorithms.math.parser.token.Token;
import me.ialistannen.algorithms.math.parser.token.TokenType;
import me.ialistannen.treeviewer.model.Tree;
import me.ialistannen.treeviewer.view.TreePane;

public class KaratsubaOfman extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
//    Tree tree = komMultTree(1242, 3163, null);
    Tree tree = komMultTree(12345, 5678, null);
//    Tree tree = komMultTree(5356, 1313, null);

    primaryStage.setScene(new Scene(new TreePane(tree, Font.font("monospace", 20))));
    primaryStage.sizeToScene();
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  public static void main(String[] args) {
//    mult(12345, 5678);
    mult(1242, 3136);
    launch(args);
  }

  private static void mult(int a, int b) {
    int komResult = komMult(a, b);
    int real = a * b;
    System.out.println(komResult + " (Real: " + real + "): " + (komResult == real));
  }

  public static int komMult(int a, int b) {
    return komMult(a, b, 0, "R");
  }

  public static int komMult(int a, int b, int depth, String prefix) {
    // a = a1 * B^k + a0
    // b = b1 * B^k + b0
    // a * b = (a1 * B^k + a0) * (b1 * B^k + b0)
    //       = (a1 * b1) * B^2k + (a0 * b1 * B^k) + (a1 * B^k * b0) + (a0 * b0)
    //       = (a1 * b1) * B^2k + (a0 * b1 + a1 * b0) * B^k + (a0 * b0)

    int amountOfDigits = Math.max(Integer.toString(a).length(), Integer.toString(b).length());
    int k = (int) Math.ceil(amountOfDigits / 2.0);

    System.out.printf(repeat(' ', depth * 2) + prefix + ": " + "Calculating %-5d * %-5d%n", a, b);

    if (amountOfDigits == 1) {
      return a * b;
    }

    int aUpper = (int) (a / Math.pow(10, k));
    int aLower = (int) (a % Math.pow(10, k));
    int bUpper = (int) (b / Math.pow(10, k));
    int bLower = (int) (b % Math.pow(10, k));

    int lowerCombined = komMult(aLower, bLower, depth + 1, "L");
    int upperCombined = komMult(aUpper, bUpper, depth + 1, "U");

    int innerProduct =
        komMult(aUpper + aLower, bUpper + bLower, depth + 1, "E") - upperCombined - lowerCombined;

    return (int) (upperCombined * Math.pow(10, 2 * k) + innerProduct * Math.pow(10, k)
        + lowerCombined);
  }

  public static Tree komMultTree(int a, int b, KomTreeNode parent) {
    List<Tree> children = new ArrayList<>();
    KomTreeNode resultTree = new KomTreeNode(parent, children, a, b);

    int amountOfDigits = Math.max(Integer.toString(a).length(), Integer.toString(b).length());
    int k = (int) Math.ceil(amountOfDigits / 2.0);

    if (amountOfDigits == 1) {
      resultTree.setValue(a * b);
      return resultTree;
    }

    int aUpper = (int) (a / Math.pow(10, k));
    int aLower = (int) (a % Math.pow(10, k));
    int bUpper = (int) (b / Math.pow(10, k));
    int bLower = (int) (b % Math.pow(10, k));

    Tree lowerTree = komMultTree(aLower, bLower, resultTree);
    int lowerCombined = (int) lowerTree.evaluate();
    Tree upperTree = komMultTree(aUpper, bUpper, resultTree);
    int upperCombined = (int) upperTree.evaluate();

    Tree innerTree = komMultTree(aLower + aUpper, bLower + bUpper, resultTree);
    int innerProduct = (int) (innerTree.evaluate() - upperCombined - lowerCombined);

    int value = (int) (upperCombined * Math.pow(10, 2 * k) + innerProduct * Math.pow(10, k)
        + lowerCombined);

    resultTree.children.add(upperTree);
    resultTree.children.add(lowerTree);
    resultTree.children.add(innerTree);
    resultTree.setValue(value);
    return resultTree;
  }

  private static String repeat(char c, int amount) {
    return IntStream.range(0, amount)
        .mapToObj(value -> "" + c)
        .collect(joining());
  }

  private static class KomTreeNode implements Tree {

    private Tree parent;
    private List<Tree> children;
    private int value;
    private int a;
    private int b;

    public KomTreeNode(Tree parent, List<Tree> children, int a, int b) {
      this.parent = parent;
      this.children = children;
      this.a = a;
      this.b = b;
    }

    public void setValue(int value) {
      this.value = value;
    }

    /**
     * @return The parent node. May be null
     */
    @Override
    public Tree getParent() {
      return parent;
    }

    /**
     * @return The children of the tree
     */
    @Override
    public List<? extends Tree> getChildren() {
      return children.stream()
          .map(tree -> (KomTreeNode) tree)
          .filter(tree -> !tree.isTrivial())
          .collect(Collectors.toList());
    }

    private boolean isTrivial() {
      return a < 10 && b < 10;
    }

    /**
     * @return The {@link Token}
     */
    @Override
    public Token getToken() {
      return new Token() {
        @Override
        public String getTokenText() {
          String result = equationToString();

          int width = children.stream()
              .flatMapToInt(tree -> Arrays.stream(tree.getToken().getTokenText().split("\n"))
                  .mapToInt(String::length))
              .max()
              .orElse(0);
          if (width != 0) {
            result += "\n";
          }
          for (int i = 0; i < width; i++) {
            result += "-";
          }
          for (Tree child : children) {
            result += "\n" + ((KomTreeNode) child).equationToString();
          }

          return result;
        }

        @Override
        public TokenType getType() {
          return TokenType.NUMBER;
        }

        @Override
        public double evaluate(Tree node) {
          return value;
        }
      };
    }

    private String equationToString() {
      return String.format("%4d * %-4d = %4d", a, b, value);
    }

    /**
     * Evaluates this node.
     *
     * @return The value of this node.
     */
    @Override
    public double evaluate() {
      return value;
    }
  }
}

package me.ialistannen.treeviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import me.ialistannen.algorithms.math.parser.InfixToAST;
import me.ialistannen.algorithms.math.parser.token.Token;
import me.ialistannen.treeviewer.view.TreeComponent;
import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

public class ViewerMain extends Application {

  @Override
  public void start(Stage primaryStage) {
    InfixToAST infixToPostfixString = new InfixToAST();

//    String defaultInput = "sin ( max ( 2, 3 ) / 3 * 3.1415 )";
    String defaultInput = "(3 + 5) * (4 + 6) + 5";

    Scanner scanner = new Scanner(System.in);

    System.out.printf("Your input [%s]: ", defaultInput);
    String input = scanner.nextLine();

    if (input.isEmpty()) {
      input = defaultInput;
    }

    System.out.println("Input:\n" + input);
    Tree ast = infixToPostfixString.generateAst(input);
    System.out.println("\nOutput:\n" + ast);
    ast.getChildrenLevels().forEach((integer, trees) -> {
      String collect = trees.stream()
          .map(Tree::getToken)
          .map(Token::getTokenText)
          .collect(Collectors.joining(" "));
      System.out.println(integer + "\t" + collect);
    });

    AbstractTreeForTreeLayout<Tree> layout = new AbstractTreeForTreeLayout<Tree>(ast) {
      @Override
      public Tree getParent(Tree tree) {
        return tree.getParent();
      }

      @Override
      public List<Tree> getChildrenList(Tree tree) {
        return new ArrayList<>(tree.getChildren());
      }
    };

    DefaultConfiguration<Tree> defaultConfiguration = new DefaultConfiguration<>(
        20, 20
    );

    NodeExtentProvider<Tree> nodeExtentProvider = new NodeExtentProvider<Tree>() {
      @Override
      public double getWidth(Tree tree) {
        return 50;
      }

      @Override
      public double getHeight(Tree tree) {
        return 50;
      }
    };

    TreeLayout<Tree> treeLayout = new TreeLayout<>(
        layout, nodeExtentProvider, defaultConfiguration
    );

    TreeComponent treeComponent = new TreeComponent(treeLayout);

    ScrollPane pane = new ScrollPane(treeComponent);
    pane.setPadding(new Insets(20));
    pane.setMaxHeight(Double.MAX_VALUE);
    pane.setMaxWidth(Double.MAX_VALUE);

    primaryStage.setScene(new Scene(pane));

    primaryStage.setTitle(input + "\t\t" + ast.evaluate());
    primaryStage.setMaxHeight(1000);
    primaryStage.setMaxWidth(1200);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

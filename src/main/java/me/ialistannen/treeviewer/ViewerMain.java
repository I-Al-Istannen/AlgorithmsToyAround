package me.ialistannen.treeviewer;

import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import me.ialistannen.algorithms.math.parser.InfixToAST;
import me.ialistannen.treeviewer.model.Tree;
import me.ialistannen.treeviewer.view.TreePane;

public class ViewerMain extends Application {

  @Override
  public void start(Stage primaryStage) {
    InfixToAST infixToPostfixString = new InfixToAST();

    String defaultInput = "(3 + 5) * (4 + 6) + 5";

    TextInputDialog textInputDialog = new TextInputDialog(defaultInput);
    Optional<String> stringOptional = textInputDialog.showAndWait();
    if (!stringOptional.isPresent()) {
      System.exit(0);
    }

    String input = stringOptional.get();

    Tree ast = infixToPostfixString.generateAst(input);

    primaryStage.setScene(new Scene(new TreePane(ast)));

    primaryStage.setTitle(input + "\t\t" + ast.evaluate());
    primaryStage.setMaxHeight(1000);
    primaryStage.setMaxWidth(1200);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

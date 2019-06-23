package me.ialistannen.collections.heap;

import java.util.Arrays;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import me.ialistannen.treeviewer.model.Tree;
import me.ialistannen.treeviewer.view.TreePane;

/**
 * A small application that allows interactive editing of a n-child heap.
 */
public class HeapViewer extends Application {

  private NChildHeap<Integer> heap;
  private BorderPane root;
  private int childCount = 2;

  @Override
  public void start(Stage primaryStage) {
    root = new BorderPane();
    heap = new NChildHeap<>(2);

    updateTree();

    TextField numberInput = new TextField();
    numberInput.setOnAction(e -> {
      addValue(numberInput.getText());
      numberInput.setText("");
    });

    Button deleteMin = new Button("Delete min");
    deleteMin.setOnAction(event -> {
      if (!heap.isEmpty()) {
        heap.poll();
        updateTree();
      }
    });

    Button setChildCount = new Button("Set heap child count");
    setChildCount.setOnAction(event -> {
      TextInputDialog dialog = new TextInputDialog("2");
      dialog.setTitle("Choose a child count");
      dialog.setContentText("Please choose a child count!");
      dialog.showAndWait().ifPresent(s -> {
        try {
          childCount = Integer.parseInt(s);
          Integer[] underlying = heap.getUnderlying(Integer[].class);
          heap = new NChildHeap<>(childCount);
          Arrays.stream(underlying)
              .filter(Objects::nonNull)
              .forEach(heap::offer);

          updateTree();
        } catch (NumberFormatException ignored) {
        }
      });
    });

    FlowPane controlPane = new FlowPane(20, 20);
    controlPane.getChildren().addAll(numberInput, deleteMin, setChildCount);
    root.setTop(controlPane);

    primaryStage.setScene(new Scene(root));
    primaryStage.sizeToScene();
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  private void addValue(String text) {
    try {
      int value = Integer.parseInt(text);
      heap.offer(value);
      updateTree();
    } catch (NumberFormatException ignored) {
    }
  }

  private void updateTree() {
    if (!heap.isEmpty()) {
      Tree tree = HeapTree.fromHeapArray(heap.getUnderlying(Integer[].class), childCount);
      TreePane treePane = new TreePane(tree, Font.font("monospace", 20));
      root.setCenter(treePane);
      root.getScene().getWindow().sizeToScene();
    } else {
      root.setCenter(new Label("No data..."));
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

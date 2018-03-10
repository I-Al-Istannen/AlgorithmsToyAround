package me.ialistannen.pathfinding.visualize;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.DisplayedGrid;

public class TestMain extends Application {

  @Override
  public void start(Stage primaryStage) {
    int rows = 35;
    int columns = 62;
    DisplayedGrid<DefaultGridState> grid = new DisplayedGrid<>(columns, rows);

    for (int i = 0; i < rows * columns; i++) {
      int column = i / columns;
      int row = i % columns;
      grid.setCellState(row, column, DefaultGridState.EMPTY);
    }

    grid.setClickListener((column, row, state, ignored) -> {
      Node node = grid.setCellState(
          column,
          row,
          DefaultGridState.values()[(state.ordinal() + 1) % DefaultGridState.values().length]
      );
      ScaleTransition transition = new ScaleTransition(Duration.millis(200), node);
      transition.setFromX(1.2f);
      transition.setFromY(1.2f);
      transition.setToX(1);
      transition.setToY(1);

      transition.play();
    });

    grid.setPadding(new Insets(20));

    grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    primaryStage.setScene(new Scene(grid));
    primaryStage.setFullScreen(true);
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

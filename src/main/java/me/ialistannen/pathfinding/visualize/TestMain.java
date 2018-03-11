package me.ialistannen.pathfinding.visualize;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.ialistannen.pathfinding.visualize.algorithms.Algorithm;
import me.ialistannen.pathfinding.visualize.algorithms.AlgorithmGrid;
import me.ialistannen.pathfinding.visualize.algorithms.astar.AStarAlgorithm;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DefaultDistanceFunction;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;
import me.ialistannen.pathfinding.visualize.grid.StatefulGridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.interaction.PaintingGridListener;
import me.ialistannen.pathfinding.visualize.grid.node.DisplayedGrid;

public class TestMain extends Application {

  @Override
  public void start(Stage primaryStage) {
    int rows = 40;
    int columns = 40;
    AlgorithmGrid<DefaultGridState> algorithmGrid = new AlgorithmGrid<>(
        DefaultGridState.EMPTY, columns, rows
    );
    DisplayedGrid<DefaultGridState> grid = new DisplayedGrid<>(algorithmGrid);
    grid.setInteractionListener(new PaintingGridListener());

    for (int i = 0; i < rows * columns; i++) {
      int column = i / columns;
      int row = i % columns;
      algorithmGrid.setStateAt(row, column, DefaultGridState.EMPTY);
    }

    algorithmGrid.setStateAt(2, 2, DefaultGridState.START);
    algorithmGrid.setStateAt(2, 18, DefaultGridState.END);

    grid.setPadding(new Insets(20));

    grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    primaryStage.setScene(new Scene(grid));
    primaryStage.setWidth(1000);
    primaryStage.setHeight(1000);
    primaryStage.centerOnScreen();
    primaryStage.show();

    grid.requestFocus();
    grid.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.G) {
        new Thread(() -> {
          List<Direction> directions = Arrays
              .asList(Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST);
          Algorithm<DefaultGridState> algorithm = new AStarAlgorithm(
              DefaultDistanceFunction.MANHATTEN, directions);
          List<StatefulGridCoordinate<DefaultGridState>> steps = algorithm.compute(
              algorithmGrid
          )
              .getSteps();
          AtomicInteger counter = new AtomicInteger();

          Timeline timeline = new Timeline();

          KeyFrame keyFrame = new KeyFrame(Duration.millis(1), event1 -> {
            if (counter.get() >= steps.size()) {
              timeline.stop();
              return;
            }
            StatefulGridCoordinate<DefaultGridState> coordinate = steps
                .get(counter.getAndIncrement());

            if (algorithmGrid.getStateAt(coordinate.getCoordinate()) != DefaultGridState.END
                && algorithmGrid.getStateAt(coordinate.getCoordinate()) != DefaultGridState.START) {
              algorithmGrid.setStateAt(coordinate.getCoordinate(), coordinate.getState());
            }
          });

          timeline.getKeyFrames().add(keyFrame);

          timeline.setCycleCount(Animation.INDEFINITE);
          timeline.play();

        }).start();
      } else if (event.getCode() == KeyCode.R) {
        for (int width = 0; width < algorithmGrid.getWidth(); width++) {
          for (int height = 0; height < algorithmGrid.getHeight(); height++) {
            DefaultGridState state = algorithmGrid.getStateAt(width, height);
            if (state != DefaultGridState.START && state != DefaultGridState.END
                && state != DefaultGridState.WALL) {
              algorithmGrid.setStateAt(width, height, DefaultGridState.EMPTY);
            }
          }
        }
      }
    });
  }

  public static void main(String[] args) {
    launch(args);
  }
}

package me.ialistannen.pathfinding.visualize.outerui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));
    Pane pane = loader.load();

    primaryStage.setScene(new Scene(pane));
    primaryStage.setWidth(1200);
    primaryStage.setHeight(1000);
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

package me.ialistannen.pathfinding.visualize.grid;

import java.util.function.Supplier;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum DefaultGridState implements GridCellState {
  EMPTY(() -> getColoredNode(Color.WHITE)),
  WALL(() -> getColoredNode(Color.web("#808080"))),
  OPEN_SET(() -> getColoredNode(Color.web("#98fb98"))),
  EXAMINED(() -> getColoredNode(Color.web("#afeeee"))),
  START(true, false, () -> getColoredNode(Color.web("#00dd00"))),
  END(false, true, () -> getColoredNode(Color.web("#ee4400"))),
  OTHER(() -> getColoredNode(Color.web("#e5e5e5")));

  private Supplier<Node> factory;
  private boolean isEnd;
  private boolean isStart;

  DefaultGridState(Supplier<Node> factory) {
    this(false, false, factory);
  }

  DefaultGridState(boolean isStart, boolean isEnd, Supplier<Node> factory) {
    this.factory = factory;
    this.isStart = isStart;
    this.isEnd = isEnd;
  }

  @Override
  public Node getNode() {
    return factory.get();
  }

  @Override
  public boolean isEnd() {
    return isEnd;
  }

  @Override
  public boolean isStart() {
    return isStart;
  }


  private static Node getColoredNode(Paint color) {
    Region region = new Region();
    region.setBackground(new Background(new BackgroundFill(color, null, null)));
    region.setBorder(new Border(new BorderStroke(
        Color.web("#d9d9d9"),
        BorderStrokeStyle.SOLID,
        CornerRadii.EMPTY,
        BorderStroke.THIN
    )));
    return region;
  }

}

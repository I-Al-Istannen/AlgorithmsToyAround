package me.ialistannen.treeviewer.view;

import java.awt.geom.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import me.ialistannen.treeviewer.model.Tree;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

/**
 * A tree component
 */
public class TreeComponent extends Canvas {

  private static final Color LINE_COLOR = Color.ROYALBLUE;
  private static final Color TEXT_COLOR = Color.TOMATO;
  private TreeLayout<Tree> layout;

  public TreeComponent(TreeLayout<Tree> layout, Font font) {
    this.layout = layout;

    getGraphicsContext2D().setStroke(Color.BLACK);
    getGraphicsContext2D().setFont(font);

    draw();
  }

  public TreeComponent(TreeLayout<Tree> layout) {
    this(layout, Font.getDefault());
  }

  /**
   * Draws the canvas
   */
  private void draw() {
    resizeToFit();
    getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());

    for (Tree tree : layout.getNodeBounds().keySet()) {
      drawNode(tree);
      drawEdges(tree);
    }
  }

  private void resizeToFit() {
    Rectangle2D bounds = layout.getBounds();
    setWidth(bounds.getWidth());
    setHeight(bounds.getHeight());
  }

  private void drawEdges(Tree tree) {
    if (!getTree().isLeaf(tree)) {
      Rectangle2D.Double bounds = layout.getNodeBounds().get(tree);
      double x = bounds.getCenterX();
      double y = bounds.getMaxY();

      getGraphicsContext2D().setStroke(LINE_COLOR);

      for (Tree child : getChildren(tree)) {
        Rectangle2D.Double childBounds = layout.getNodeBounds().get(child);
        double childX = childBounds.getCenterX();
        double childY = childBounds.getMinY();

        getGraphicsContext2D().strokeLine(x, y, childX, childY);
      }
    }
  }

  private void drawNode(Tree tree) {
    Rectangle2D.Double bounds = layout.getNodeBounds().get(tree);
    double minX = bounds.getMinX();
    double minY = bounds.getMinY();

    getGraphicsContext2D().setStroke(LINE_COLOR);
    getGraphicsContext2D().strokeRoundRect(
        minX, minY, bounds.getWidth(), bounds.getHeight(),
        bounds.getWidth() * 20 / 100,
        bounds.getHeight() * 20 / 100
    );

    getGraphicsContext2D().setTextAlign(TextAlignment.CENTER);
    getGraphicsContext2D().setTextBaseline(VPos.CENTER);

    getGraphicsContext2D().setFill(TEXT_COLOR);
    getGraphicsContext2D().fillText(
        tree.getToken().getTokenText(),
        bounds.getCenterX(), bounds.getCenterY()
    );
  }

  private Iterable<Tree> getChildren(Tree tree) {
    return getTree().getChildren(tree);
  }

  private TreeForTreeLayout<Tree> getTree() {
    return layout.getTree();
  }
}

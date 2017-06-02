package me.ialistannen.treeviewer.view;

import java.awt.geom.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import me.ialistannen.treeviewer.Tree;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

/**
 * A tree component
 */
public class TreeComponent extends Canvas {

  private TreeLayout<Tree> layout;

  public TreeComponent(TreeLayout<Tree> layout) {
    this.layout = layout;

    setWidth(400);
    setHeight(400);
    getGraphicsContext2D().setStroke(Color.BLACK);

    draw();
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
    getGraphicsContext2D().strokeOval(
        minX, minY, bounds.getWidth(), bounds.getHeight()
    );
    getGraphicsContext2D().setTextAlign(TextAlignment.CENTER);
    getGraphicsContext2D().setTextBaseline(VPos.CENTER);
    getGraphicsContext2D().strokeText(
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

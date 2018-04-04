package me.ialistannen.collections.quadtree.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import me.ialistannen.collections.quadtree.Point;
import me.ialistannen.collections.quadtree.QuadNode;
import me.ialistannen.collections.quadtree.QuadTree;
import me.ialistannen.collections.quadtree.Rectangle;

class TreeRenderer {

  private Paint boundaryPaint;
  private Paint pointPaint;
  private Paint pointHighLightPaint;
  private Paint highlightBoundaryPaint;
  private int pointSize;

  public TreeRenderer(Paint boundaryPaint, Paint pointPaint, Paint pointHighLightPaint,
      Paint highlightBoundaryPaint, int pointSize) {
    this.boundaryPaint = boundaryPaint;
    this.pointPaint = pointPaint;
    this.pointHighLightPaint = pointHighLightPaint;
    this.highlightBoundaryPaint = highlightBoundaryPaint;
    this.pointSize = pointSize;
  }

  /**
   * Renders a {@link QuadTree} on a given {@link Canvas}. Does not clear the canvas.
   *
   * @param canvas the canvas to render it on
   * @param tree the tree to render
   * @see #render(Canvas, QuadTree, boolean)
   */
  public void render(Canvas canvas, QuadTree tree) {
    render(canvas, tree, true);
  }

  /**
   * Renders a {@link QuadTree} on a given {@link Canvas}. Does not clear the canvas.
   *
   * @param canvas the canvas to render it on
   * @param tree the tree to render
   * @param withPoints whether to also paint the points
   */
  public void render(Canvas canvas, QuadTree tree, boolean withPoints) {
    renderNode(tree.getRootNode(), canvas.getGraphicsContext2D(), withPoints);
  }

  private void renderNode(QuadNode node, GraphicsContext graphicsContext, boolean withPoints) {
    graphicsContext.setStroke(boundaryPaint);
    graphicsContext.setFill(pointPaint);
    if (node == null) {
      return;
    }

    if (withPoints) {
      for (Point point : node.getPoints()) {
        if (point != null) {
          graphicsContext.fillOval(point.x, point.y, pointSize, pointSize);
        }
      }
    }

    Rectangle boundary = node.getBoundary();
    strokeRectangle(graphicsContext, boundary);

    for (QuadNode quadNode : node.children()) {
      renderNode(quadNode, graphicsContext, withPoints);
    }
  }

  private void strokeRectangle(GraphicsContext graphicsContext, Rectangle boundary) {
    graphicsContext.strokeRect(
        boundary.centerX - boundary.width, boundary.centerY - boundary.height,
        boundary.width * 2, boundary.height * 2
    );
  }

  /**
   * Highlights the points inside the boundary.
   *
   * @param boundary the boundary
   * @param canvas the canvas to render it on
   * @param quadTree the quad tree supplying the points
   */
  public void highlightPoints(Rectangle boundary, Canvas canvas, QuadTree quadTree) {
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.setFill(pointHighLightPaint);
    graphicsContext.setStroke(highlightBoundaryPaint);

    for (Point point : quadTree.getAllPointsWithin(boundary)) {
      graphicsContext.fillOval(point.x, point.y, pointSize, pointSize);
    }

    strokeRectangle(graphicsContext, boundary);
  }
}

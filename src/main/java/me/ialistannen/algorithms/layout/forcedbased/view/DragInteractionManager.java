package me.ialistannen.algorithms.layout.forcedbased.view;

import java.util.function.BiConsumer;
import javafx.geometry.Point3D;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;

/**
 * A helper for drag interaction.
 */
class DragInteractionManager<T> {

  private DraggingState<T> state = new DraggingState<>();

  /**
   * Registers a circle for drag and dropping.
   *
   * @param circle the circle
   */
  void registerCircleDragAndDrop(NodeCircle<T> circle) {
    circle.setOnMousePressed(event -> {
      state.circle = circle;
      Point3D point3D = circle.localToParent(event.getX(), event.getY(), event.getZ());

      state.startPos = new Vector2D(
          point3D.getX(), point3D.getY()
      );
      circle.getNode().setPausePhysics(true);
    });
    circle.setOnMouseReleased(event -> {
      state.circle = null;
      circle.getNode().setPausePhysics(false);
    });
  }

  /**
   * Executes the given function if there is a node being dragged.
   *
   * @param action the action to execute
   */
  void executeIfDragging(BiConsumer<NodeCircle<T>, Vector2D> action) {
    state.executeIfDragging(action);
  }


  /**
   * A simple drag state.
   *
   * @param <T> the type of the nodes
   */
  private static class DraggingState<T> {

    private NodeCircle<T> circle;
    private Vector2D startPos;

    private DraggingState() {
    }

    /**
     * Executes the given function if there is a node being dragged.
     *
     * @param action the action to execute
     */
    void executeIfDragging(BiConsumer<NodeCircle<T>, Vector2D> action) {
      if (circle != null && startPos != null) {
        action.accept(circle, startPos);
      }
    }
  }
}

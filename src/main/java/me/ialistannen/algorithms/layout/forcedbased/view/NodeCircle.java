package me.ialistannen.algorithms.layout.forcedbased.view;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import me.ialistannen.algorithms.layout.forcedbased.Vector2D;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * A simple model to display a node.
 *
 * @param <T> the type of the stored value
 */
public class NodeCircle<T> extends GridPane {

  private static FXMLLoader loader = new FXMLLoader(
      NodeCircle.class.getResource("/fxml/nodelayout/NodeCircle.fxml")
  );

  private final Node<T> node;

  @FXML
  private Label title;

  @FXML
  private Label leftText;

  @FXML
  private Label rightText;

  @FXML
  private Separator titleSeparator;

  @FXML
  private Separator childTextSeparator;


  /**
   * Creates a new node circle for the given node.
   *
   * @param node the node to create the circle for
   */
  public NodeCircle(Node<T> node) {
    this.node = node;

    loader.setRoot(this);
    loader.setController(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    setText(Objects.toString(node.getValue()));
    setLeftText("");
    setRightText("");

    setupChildren();

    update();

    setOnDragDetected(event -> {
      if (!event.isControlDown()) {
        return;
      }
      Dragboard dragboard = startDragAndDrop(TransferMode.LINK);
      ClipboardContent content = new ClipboardContent();
      content.putString("DRAGGING");
      dragboard.setContent(content);
    });
    setOnDragOver(event -> {
      if (event.getGestureSource() == this) {
        event.acceptTransferModes(TransferMode.NONE);
        return;
      }

      if (event.getDragboard().getContent(DataFormat.PLAIN_TEXT).equals("DRAGGING")) {
        event.acceptTransferModes(TransferMode.LINK);
      } else {
        event.acceptTransferModes(TransferMode.NONE);
      }
    });
    setOnDragDropped(event -> {
      Object source = event.getGestureSource();
      if (source instanceof NodeCircle) {
        @SuppressWarnings("unchecked")
        NodeCircle<T> other = (NodeCircle<T>) source;

        if (other == this) {
          return;
        }

        if (!other.getNode().isConnected(getNode())) {
          other.getNode().addUnidirectionalConnection(getNode(), 1);
        } else {
          other.getNode().removeConnectionWith(getNode());
        }
      }
    });
  }

  private void setupChildren() {
    leftText.visibleProperty().bind(leftText.textProperty().isNotEmpty());
    rightText.visibleProperty().bind(rightText.textProperty().isNotEmpty());

    childTextSeparator.visibleProperty().bind(
        leftText.textProperty().isNotEmpty().and(rightText.textProperty().isNotEmpty())
    );
    titleSeparator.visibleProperty().bind(
        leftText.textProperty().isNotEmpty().or(rightText.textProperty().isNotEmpty())
    );

    leftText.managedProperty().bind(leftText.visibleProperty());
    rightText.managedProperty().bind(rightText.visibleProperty());
    childTextSeparator.managedProperty().bind(childTextSeparator.visibleProperty());
    titleSeparator.managedProperty().bind(titleSeparator.visibleProperty());
  }

  /**
   * Updates this circle to reflect the new node position.
   */
  public void update() {
    Vector2D position = node.getPosition();
    translateXProperty().set(position.getX());
    translateYProperty().set(position.getY());
  }

  /**
   * Returns the x coordinate of the center.
   *
   * @return the x coordinate of the center.
   */
  public double getCenterX() {
    return getTranslateX() + getWidth() / 2;
  }

  /**
   * Returns the y coordinate of the center.
   *
   * @return the y coordinate of the center.
   */
  public double getCenterY() {
    return getTranslateY() + getHeight() / 2;
  }

  /**
   * Returns the wrapped node.
   *
   * @return the wrapped node
   */
  public Node<T> getNode() {
    return node;
  }

  /**
   * Sets the displayed text of this node.
   *
   * @param text the text to display
   */
  public void setText(String text) {
    title.setText(text);
  }

  /**
   * Sets the left text of this node.
   *
   * @param text the text to display
   */
  public void setLeftText(String text) {
    leftText.setText(text);
  }

  /**
   * Sets the right text of this node.
   *
   * @param text the text to display
   */
  public void setRightText(String text) {
    rightText.setText(text);
  }

  /**
   * Highlights this node.
   *
   * @param highlight true if this node should be highlighted
   */
  public void setHighlight(boolean highlight) {
    getStyleClass().remove("highlighted-node");

    if (highlight) {
      getStyleClass().add("highlighted-node");
    }
  }
}

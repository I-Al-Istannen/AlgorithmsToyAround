package me.ialistannen.algorithms.layout.forcedbased.view.util;

import java.util.function.Consumer;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import me.ialistannen.algorithms.layout.forcedbased.util.ExceptionalFunction;

/**
 * An editable label.
 *
 * @param <T> the type of the value
 */
public class EditableLabel<T> extends AnchorPane {

  private Label label;
  private TextField textField;

  /**
   * A label that can be edited.
   *
   * @param converter the text field to value converter
   * @param resultConsumer the result consumer
   */
  public EditableLabel(ExceptionalFunction<String, T> converter, Consumer<T> resultConsumer) {
    label = new Label();
    textField = new TextField();
    textField.prefColumnCountProperty().bind(textField.textProperty().length());

    getChildren().addAll(label, textField);
    displayValue();

    setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

    setOnMouseClicked(event -> {
      if (event.getClickCount() != 2 || event.getButton() != MouseButton.PRIMARY) {
        return;
      }
      textField.setText(label.getText());

      displayInputField();
    });

    setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        displayValue();
      } else if (event.getCode() == KeyCode.ENTER && textField.isVisible()) {
        String text = textField.getText();
        try {
          resultConsumer.accept(converter.apply(text));
          displayValue();
        } catch (Exception ignored) {
          System.out.println(":(");
        }
      }
    });
  }

  private void displayInputField() {
    label.setVisible(false);
    label.setManaged(false);

    textField.setVisible(true);
    textField.setManaged(true);
    textField.requestFocus();
  }

  private void displayValue() {
    label.setVisible(true);
    label.setManaged(true);

    textField.setVisible(false);
    textField.setManaged(false);
  }

  public StringProperty textProperty() {
    return label.textProperty();
  }
}

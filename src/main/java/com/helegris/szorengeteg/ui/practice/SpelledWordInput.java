/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Timi
 */
public class SpelledWordInput extends WordInput {

    private static final int FIELD_WIDTH = 66;

    private final List<TextField> fields = new ArrayList<>();

    private final String expectedInput;

    private int numberOfFields = 14;

    public SpelledWordInput(String word, WordInputListener listener) {
        super(word, listener);
        expectedInput = word.toUpperCase();
        setFields();
    }

    private void setFields() {
        if (numberOfFields < expectedInput.length()) {
            numberOfFields = expectedInput.length();
        }

        addFields();

        if (fields.size() > 1) {
            setFirstField();
            setMiddleFields();
            setLastField();
        }
    }

    private void addFields() {
        for (int i = 0; i < numberOfFields; i++) {
            TextField field = new TextField();
            field.setPrefWidth(FIELD_WIDTH);
            field.focusedProperty().addListener(
                    (ObservableValue<? extends Boolean> observable,
                            Boolean oldValue, Boolean newValue) -> {
                        Platform.runLater(() -> {
                            if (field.isFocused() && !field.getText().isEmpty()) {
                                field.selectAll();
                            }
                        });
                    });
            fields.add(field);
            this.getChildren().add(field);
        }
    }

    private void setFirstField() {
        TextField firstField = fields.get(0);
        TextField secondField = fields.get(1);

        handleTextChange(firstField, secondField);
        handleRight(firstField, secondField);
    }

    private void setMiddleFields() {
        for (int i = 1; i < fields.size() - 1; i++) {
            TextField previousField = fields.get(i - 1);
            TextField field = fields.get(i);
            TextField nextField = fields.get(i + 1);

            handleTextChange(field, nextField);
            handleRight(field, nextField);
            handleLeft(field, previousField);
        }
    }

    private void setLastField() {
        TextField lastField = fields.get(fields.size() - 1);
        TextField oneBeforeTheLastField = fields.get(fields.size() - 2);

        handleTextChange(lastField, null);
        handleLeft(lastField, oneBeforeTheLastField);
    }

    private void handleTextChange(TextField field, Control nextControl) {
        int index = fields.indexOf(field);
        String expectedLetter = index < expectedInput.length()
                ? expectedInput.substring(index, index + 1) : "";

        field.textProperty().addListener((ov, oldValue, newValue) -> {
            if (oldValue.equals(expectedLetter)) {
                field.setText(oldValue);
            } else if (!"".equals(newValue)) {
                newValue = newValue.substring(0, 1).toUpperCase();
                if (newValue.equals(expectedLetter)) {
                    if (nextControl != null) {
                        nextControl.requestFocus();
                    } else {
                        Platform.runLater(() -> field.selectAll());
                    }
                    field.setText(newValue);
                } else {
                    field.setText("");
                }
                check();
            }
        });
    }

    private void handleRight(TextField field, Control nextControl) {
        field.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                nextControl.requestFocus();
            }
        });
    }

    private void handleLeft(TextField field, TextField previousField) {
        field.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT) {
                previousField.requestFocus();
            } else if (event.getCode() == KeyCode.BACK_SPACE
                    || event.getCode() == KeyCode.DELETE) {
                previousField.requestFocus();
            }
        });
    }

    @Override
    public void requestFocus() {
        fields.get(0).requestFocus();
    }

    @Override
    protected void check() {
        String input = "";
        input = fields.stream()
                .filter(field -> fields.indexOf(field) < expectedInput.length())
                .map(field -> field.getText())
                .reduce(input, String::concat);

        if (input.equals(expectedInput)) {
            listener.fullInput();
            disable();
        }
    }

    @Override
    public void help() {
        fields.stream().forEach(field -> field.setText(""));
        helpFirstAndLastChar();
    }

    private void helpFirstChar() {
        String firstLetter = expectedInput.substring(0, 1);
        fields.get(0).setText(firstLetter);
        fields.get(1).requestFocus();
    }

    private void helpFirstAndLastChar() {
        int index = expectedInput.length() - 1;
        if (expectedInput.length() > 2) {
            String lastLetter = expectedInput.substring(index);
            fields.get(index).setText(lastLetter);
        }
        helpFirstChar();
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                field.requestFocus();
                break;
            }
        }
        for (int i = index + 1; i < fields.size(); i++) {
            fields.get(i).setDisable(true);
        }
    }

    @Override
    public void revealWord() {
        for (int i = 0; i < expectedInput.length(); i++) {
            fields.get(i).setText(expectedInput.substring(i, i + 1));
        }
        disable();
    }

    private void disable() {
        fields.stream().forEach(field -> {
            field.setDisable(true);
            if (fields.indexOf(field) < expectedInput.length()) {
                field.setStyle("-fx-opacity: 1.0;");
            }
        });
    }

    public List<TextField> getFields() {
        return new ArrayList<>(fields);
    }
}

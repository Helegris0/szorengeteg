/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author Timi
 */
public class SpelledWordInput extends WordInput {

    private static final int FIELD_WIDTH = 32;

    private final List<TextField> fields = new ArrayList<>();

    public SpelledWordInput(String word, WordInputListener listener) {
        super(word, listener);
        setFields();
    }

    private void setFields() {
        addFields();
        addButton();

        if (fields.size() > 1) {
            setFirstField();
            setMiddleFields();
            setLastField();
        }
    }

    private void addFields() {
        for (int i = 0; i < word.length(); i++) {
            if (' ' == (word.charAt(i))) {
                Pane pane = new HBox();
                pane.setPrefWidth(FIELD_WIDTH);
                this.getChildren().add(pane);
            } else {
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
                field.setOnAction(this::check);
            }
        }
    }

    private void addButton() {
        btnCheck.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.BACK_SPACE
                    || event.getCode() == KeyCode.DELETE) {
                fields.get(fields.size() - 1).requestFocus();
            }
        });

        this.getChildren().add(btnCheck);
    }

    private void setFirstField() {
        fields.get(0).textProperty().addListener((ov, oldValue, newValue) -> {
            if (!"".equals(newValue)) {
                fields.get(1).requestFocus();
                fields.get(0).setText(newValue.substring(0, 1).toUpperCase());
            }
        });
    }

    private void setMiddleFields() {
        for (int i = 1; i < fields.size() - 1; i++) {
            TextField previousField = fields.get(i - 1);
            TextField field = fields.get(i);
            TextField nextField = fields.get(i + 1);

            field.textProperty().addListener((ov, oldValue, newValue) -> {
                if (!"".equals(newValue)) {
                    nextField.requestFocus();
                    field.setText(newValue.substring(0, 1).toUpperCase());
                } else {
                    previousField.requestFocus();
                }
            });

            handleDelete(field, previousField);
        }
    }

    private void setLastField() {
        fields.get(fields.size() - 1).textProperty().addListener((ov, oldValue, newValue) -> {
            if (!"".equals(newValue)) {
                btnCheck.requestFocus();
                fields.get(fields.size() - 1)
                        .setText(newValue.substring(0, 1).toUpperCase());
            } else {
                fields.get(fields.size() - 2).requestFocus();
            }
        });

        handleDelete(fields.get(fields.size() - 1), fields.get(fields.size() - 2));
    }

    private void handleDelete(TextField field, TextField previousField) {
        field.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.BACK_SPACE
                    || event.getCode() == KeyCode.DELETE) {
                field.setText("");
                previousField.requestFocus();
            }
        });
    }

    @Override
    protected void check(ActionEvent event) {
        String input = "";
        input = fields.stream()
                .map(field -> field.getText())
                .reduce(input, String::concat);

        if (!"".equals(input)) {
            disableButton();
            if (input.toLowerCase().equals(
                    word.toLowerCase().replaceAll(" +", ""))) {
                listener.answeredCorrectly();
            } else {
                listener.answeredIncorrectly();
            }
        }
    }

    @Override
    public void help() {
        String firstLetter = word.substring(0, 1).toUpperCase();
        fields.get(0).setText(firstLetter);

        String lastLetter = word.substring(word.length() - 1);
        fields.get(fields.size() - 1).setText(lastLetter);

        fields.get(1).requestFocus();
    }
}

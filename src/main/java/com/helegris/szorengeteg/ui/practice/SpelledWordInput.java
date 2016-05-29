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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author Timi
 */
public class SpelledWordInput extends WordInput {

    private static final int FIELD_WIDTH = 70;

    private final List<TextField> fields = new ArrayList<>();

    private final String vowels = "AÁEÉIÍOÓÖŐÜŰ";

    private boolean full;
    private int numOfTries;

    public SpelledWordInput(String word, WordInputListener listener) {
        super(word, listener);
        setFields();
    }

    private void setFields() {
        addFields();

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
            }
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
        field.textProperty().addListener((ov, oldValue, newValue) -> {
            if (" ".equals(newValue)) {
                field.setText("");
            } else if (!"".equals(newValue)) {
                if (nextControl != null) {
                    nextControl.requestFocus();
                } else {
                    Platform.runLater(() -> field.selectAll());
                }
                field.setText(newValue.substring(0, 1).toUpperCase());
            }
            if (full) {
                full = false;
                numOfTries++;
                listener.tryAgain(numOfTries);
            }
            check();
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
                field.setText("");
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
        String expected = word.toLowerCase().replaceAll(" +", "");
        String input = "";
        input = fields.stream()
                .map(field -> field.getText())
                .reduce(input, String::concat)
                .toLowerCase();

        if (input.length() == expected.length()) {
            if (input.equals(expected)) {
                listener.answeredCorrectly();
            } else {
                listener.answeredIncorrectly();
                full = true;
            }
        }
    }

    @Override
    public void help() {
        fields.stream().forEach(field -> field.setText(""));
        switch (settings.getWordHelp()) {
            case FIRST_CHAR:
                helpFirstChar();
                break;
            case FIRST_TWO_CHARS:
                helpFirstTwoChars();
                break;
            case FIRST_AND_LAST_CHAR:
                helpFirstAndLastChar();
                break;
            case VOWELS:
                helpVowels();
                break;
        }
    }

    private void helpFirstChar() {
        String firstLetter = word.substring(0, 1).toUpperCase();
        fields.get(0).setText(firstLetter);
        fields.get(1).requestFocus();
    }

    private void helpFirstTwoChars() {
        helpFirstChar();
        String secondLetter = word.substring(1, 2).toUpperCase();
        if (fields.size() > 2) {
            fields.get(1).setText(secondLetter);
            fields.get(2).requestFocus();
        }
    }

    private void helpFirstAndLastChar() {
        String lastLetter = word.substring(word.length() - 1);
        fields.get(fields.size() - 1).setText(lastLetter);
        helpFirstChar();
    }

    private void helpVowels() {
        for (int i = 0; i < fields.size(); i++) {
            String letter = word.toUpperCase().substring(i, i + 1);
            if (vowels.contains(letter)) {
                fields.get(i).setText(letter);
            }
        }
        fields.get(0).requestFocus();
    }

    @Override
    public void revealWord() {
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setText(word.substring(i, i + 1));
        }
    }

    public List<TextField> getFields() {
        return new ArrayList<>(fields);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 *
 * @author Timi
 */
public class SimpleWordInput extends WordInput {

    private final TextField field = new TextField();

    public SimpleWordInput(String word, WordInputListener listener) {
        super(word, listener);
        this.getChildren().add(field);
        field.textProperty().addListener((
                ObservableValue<? extends String> observable,
                String oldValue, String newValue) -> {
                    newValue = newValue.toUpperCase();
                    String expected = word
                    .toUpperCase()
                    .substring(0, newValue.length());

                    if (newValue.equals(expected)
                    && newValue.length() > oldValue.length()) {
                        field.setText(newValue);
                    } else {
                        field.setText(oldValue);
                    }
                    check();
                });
    }

    @Override
    public void requestFocus() {
        field.requestFocus();
    }

    @Override
    protected final void check() {
        String input = field.getText();

        if (!"".equals(input)) {
            if (input.equals(word)) {
                listener.fullInput();
                disable();
            }
        }
    }

    private void disable() {
        field.setDisable(true);
        field.setStyle("-fx-opacity: 1.0;");
    }

    @Override
    public void help() {
        switch (settings.getWordHelp()) {
            case FIRST_CHAR:
                helpFirstChars(1);
                break;
            case FIRST_TWO_CHARS:
                helpFirstChars(2);
                break;
        }
    }

    private void helpFirstChars(int endIndex) {
        field.clear();
        field.setText(word.substring(0, endIndex));
        field.requestFocus();
        field.positionCaret(endIndex);
    }

    @Override
    public void revealWord() {
        field.setText(word);
        field.setDisable(true);
        disable();
    }

    public String getFieldText() {
        return field.getText();
    }
}

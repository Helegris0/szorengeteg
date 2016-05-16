/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.messages.Messages;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 *
 * @author Timi
 */
public class SimpleWordInput extends WordInput {

    private final TextField txtInput = new TextField();

    public SimpleWordInput(String word, WordInputListener listener) {
        super(word, listener);
        this.getChildren().add(txtInput);
        txtInput.textProperty().addListener((
                ObservableValue<? extends String> observable, 
                String oldValue, String newValue) -> {
            txtInput.setText(newValue.toUpperCase());
            check();
        });
        txtInput.setTooltip(new Tooltip(Messages.msg("practice.word_input")));
    }

    @Override
    protected final void check() {
        String input = txtInput.getText();

        if (!"".equals(input)) {
            if (input.toLowerCase().equals(word.toLowerCase())) {
                listener.answeredCorrectly();
            }
        }
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
        txtInput.clear();
        txtInput.setText(word.substring(0, endIndex));
        txtInput.requestFocus();
        txtInput.positionCaret(endIndex);
    }

    public String getFieldText() {
        return txtInput.getText();
    }
}

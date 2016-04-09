/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.ui.settings.Settings;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

/**
 *
 * @author Timi
 */
public class SimpleWordInput extends WordInput {

    private final TextField txtInput = new TextField();

    public SimpleWordInput(String word, WordInputListener listener) {
        super(word, listener);
        this.getChildren().add(txtInput);
        this.getChildren().add(btnCheck);
        txtInput.setOnAction(this::check);
    }

    @Override
    protected void check(ActionEvent event) {
        String input = txtInput.getText();

        if (!"".equals(input)) {
            disableButton();
            if (input.toLowerCase().equals(word.toLowerCase())) {
                listener.answeredCorrectly();
            } else {
                listener.answeredIncorrectly();
            }
        }
    }

    @Override
    public void help() {
        switch (new Settings().getWordHelp()) {
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
}

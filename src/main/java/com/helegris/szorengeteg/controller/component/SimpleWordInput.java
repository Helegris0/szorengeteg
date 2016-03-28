/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

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
    }
}

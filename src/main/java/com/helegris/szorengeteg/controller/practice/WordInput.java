/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.practice;

import com.helegris.szorengeteg.messages.Messages;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author Timi
 */
public abstract class WordInput extends HBox {

    protected final String word;
    protected final Button btnCheck = new Button(Messages.msg("practice.check"));
    protected final WordInputListener listener;

    public WordInput(String word, WordInputListener listener) {
        this.word = word;
        this.listener = listener;
        btnCheck.setOnAction(this::check);
        btnCheck.defaultButtonProperty().bind(btnCheck.focusedProperty());
    }

    protected abstract void check(ActionEvent event);
    
    public abstract void help();

    public void disableButton() {
        btnCheck.setDisable(true);
    }
}

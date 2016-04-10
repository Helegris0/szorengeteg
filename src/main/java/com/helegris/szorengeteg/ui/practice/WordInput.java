/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import javafx.scene.layout.HBox;

/**
 *
 * @author Timi
 */
public abstract class WordInput extends HBox {

    protected final String word;
    protected final WordInputListener listener;

    public WordInput(String word, WordInputListener listener) {
        this.word = word;
        this.listener = listener;
    }

    protected abstract void check();
    
    public abstract void help();
}

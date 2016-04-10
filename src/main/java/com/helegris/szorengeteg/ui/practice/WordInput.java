/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.ui.settings.Settings;
import javafx.scene.layout.HBox;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public abstract class WordInput extends HBox {
    
    @Inject
    protected Settings settings;

    protected final String word;
    protected final WordInputListener listener;

    @SuppressWarnings("LeakingThisInConstructor")
    public WordInput(String word, WordInputListener listener) {
        this.word = word;
        this.listener = listener;
        DIUtils.injectFields(this);
    }

    protected abstract void check();
    
    public abstract void help();
}

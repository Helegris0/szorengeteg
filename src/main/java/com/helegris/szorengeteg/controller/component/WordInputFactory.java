/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.Settings;

/**
 *
 * @author Timi
 */
public class WordInputFactory {

    public WordInput getWordInput(String word, WordInputListener listener) {
        switch (new Settings().getWordInput()) {
            case ONE_FIELD:
                return new SimpleWordInput(word, listener);
            case CHAR_FIELDS:
                return new SpelledWordInput(word, listener);
            default:
                return new SpelledWordInput(word, listener);
        }
    }
}

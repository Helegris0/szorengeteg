/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Timi
 */
public class GlobalSettings {

    @SerializedName("words_per_session")
    private int wordsPerSession;

    public int getWordsPerSession() {
        return wordsPerSession;
    }

    public void setWordsPerSession(int wordsPerSession) {
        this.wordsPerSession = wordsPerSession;
    }
}

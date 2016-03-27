/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import java.util.prefs.Preferences;

/**
 *
 * @author Timi
 */
public class Settings {

    private final Preferences prefs
            = Preferences.userRoot().node(this.getClass().getName());
    private final String KEY_WORDS_PER_SESSION = "words_per_session";

    public int getWordsPerSession() {
        return prefs.getInt(KEY_WORDS_PER_SESSION, 5);
    }

    public void setWordsPerSession(int value) {
        prefs.putInt(KEY_WORDS_PER_SESSION, value);
    }
}

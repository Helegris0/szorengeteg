/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import java.util.prefs.Preferences;

/**
 *
 * @author Timi
 */
public class PositionSaver {

    private final Preferences prefs
            = Preferences.userRoot().node(this.getClass().getName());
    private static final String KEY_TOPIC_ORDINAL = "topic_ordinal";
    private static final String KEY_CARD_ORDINAL = "card_ordinal";
    private static final int TOPIC_DEF = 1;
    private static final int CARD_DEF = 0;

    public int getTopicOrdinal() {
        return prefs.getInt(KEY_TOPIC_ORDINAL, TOPIC_DEF);
    }

    public void setTopicOrdinal(int arg) {
        prefs.putInt(KEY_TOPIC_ORDINAL, arg);
    }

    public int getCardOrdinal() {
        int def = 0;
        return prefs.getInt(KEY_CARD_ORDINAL, CARD_DEF);
    }

    public void setCardOrdinal(int arg) {
        prefs.putInt(KEY_CARD_ORDINAL, arg);
    }

    public static int getTopicOrdDef() {
        return TOPIC_DEF;
    }

    public static int getCardOrdDef() {
        return CARD_DEF;
    }
}

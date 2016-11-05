/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.business.model.Topic;
import java.util.prefs.Preferences;

/**
 * Saves and loads practicing position (topic and card ordinal)
 * using Preferences API.
 * 
 * @author Timi
 */
public class PositionSaver {

    private final Preferences prefs
            = Preferences.userRoot().node(this.getClass().getName());
    private static final String KEY_TOPIC_ORDINAL = "topic_ordinal";
    private static final String KEY_CARD_ORDINAL = "card_ordinal";
    private static final int TOPIC_DEFAULT = 1;
    private static final int CARD_DEFAULT = 0;

    public int getTopicOrdinal() {
        return prefs.getInt(KEY_TOPIC_ORDINAL, TOPIC_DEFAULT);
    }

    public void setTopic(Topic topic) {
        prefs.putInt(KEY_TOPIC_ORDINAL, topic.getOrdinal());
    }

    public int getCardOrdinal() {
        return prefs.getInt(KEY_CARD_ORDINAL, CARD_DEFAULT);
    }

    public void setCardOrdinal(int arg) {
        prefs.putInt(KEY_CARD_ORDINAL, arg);
    }

    public static int getTopicOrdinalDefault() {
        return TOPIC_DEFAULT;
    }

    public static int getCardOrdinalDefault() {
        return CARD_DEFAULT;
    }
}

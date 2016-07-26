/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.messages;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Timi
 */
public class Messages {

    private static final String BASENAME = "messages.messages";
    private static final String LANGUAGE = "hu";
    private static final String COUNTRY = "HU";
    public static final ResourceBundle RESOURCE_BUNDLE
            = ResourceBundle.getBundle(BASENAME, new Locale(LANGUAGE, COUNTRY));

    public static String msg(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return missingKeyMsg(key);
        }
    }

    public static String msg(String key, Object... params) {
        try {
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
        } catch (MissingResourceException e) {
            return missingKeyMsg(key);
        }
    }

    private static String missingKeyMsg(String key) {
        return "???" + key + "???";
    }
}

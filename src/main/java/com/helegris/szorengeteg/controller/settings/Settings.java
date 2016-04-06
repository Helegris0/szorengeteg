/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

/**
 *
 * @author Timi
 */
public class Settings {

    private final Preferences prefs
            = Preferences.userRoot().node(this.getClass().getName());
    private final String KEY_WORDS_PER_SESSION = "words_per_session";
    private final String KEY_REPEAT_UNKNOWN_WORDS = "repeat_unknown_words";
    private final String KEY_WORD_INPUT = "word_input";
    private final String KEY_WORD_HELP = "word_help";

    public enum WordInput {

        ONE_FIELD("one_field"),
        CHAR_FIELDS("char_fields");

        private final String value;

        private WordInput(String value) {
            this.value = value;
        }
    }

    public enum WordHelp {

        NO_HELP("no_help", WordInput.ONE_FIELD, WordInput.CHAR_FIELDS),
        FIRST_CHAR("first_char", WordInput.ONE_FIELD, WordInput.CHAR_FIELDS),
        FIRST_TWO_CHARS("first_two_chars", WordInput.ONE_FIELD, WordInput.CHAR_FIELDS),
        FIRST_AND_LAST_CHAR("first_and_last_char", WordInput.CHAR_FIELDS),
        VOWELS("vowels", WordInput.CHAR_FIELDS);

        private final String value;
        private final List<WordInput> wordInputs = new ArrayList<>();

        private WordHelp(String value, WordInput... array) {
            this.value = value;
            wordInputs.addAll(Arrays.asList(array));
        }

        public List<WordInput> getWordInputs() {
            return wordInputs;
        }
    }

    public int getWordsPerSession() {
        int def = 5;
        return prefs.getInt(KEY_WORDS_PER_SESSION, def);
    }

    public void setWordsPerSession(int arg) {
        prefs.putInt(KEY_WORDS_PER_SESSION, arg);
    }

    public boolean isRepeatUnknownWords() {
        boolean def = false;
        return prefs.getBoolean(KEY_REPEAT_UNKNOWN_WORDS, def);
    }

    public void setRepeatUnknownWords(boolean arg) {
        prefs.putBoolean(KEY_REPEAT_UNKNOWN_WORDS, arg);
    }

    public WordInput getWordInput() {
        WordInput def = WordInput.CHAR_FIELDS;
        String setting = prefs.get(KEY_WORD_INPUT, def.value);

        for (WordInput element : WordInput.values()) {
            if (setting.equals(element.value)) {
                return element;
            }
        }
        return def;
    }

    public void setWordInput(WordInput arg) {
        prefs.put(KEY_WORD_INPUT, arg.value);
    }

    public WordHelp getWordHelp() {
        WordHelp def = WordHelp.FIRST_CHAR;
        String setting = prefs.get(KEY_WORD_HELP, def.value);

        for (WordHelp element : WordHelp.values()) {
            if (setting.equals(element.value)) {
                return element;
            }
        }
        return def;
    }

    public void setWordHelp(WordHelp arg) {
        if (arg.wordInputs.contains(getWordInput())) {
            prefs.put(KEY_WORD_HELP, arg.value);
        }
    }
}
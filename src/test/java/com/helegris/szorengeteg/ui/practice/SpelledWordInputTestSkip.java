/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.JavaFXThreadingRule;
import com.helegris.szorengeteg.ui.settings.Settings;
import java.util.List;
import javafx.scene.control.TextField;
import javax.inject.Inject;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author Timi
 */
public class SpelledWordInputTestSkip {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Inject
    private Settings settings;

    private Settings.WordInput originalInputSetting;
    private Settings.WordHelp originalHelpSetting;

    private final WordInputListener listener = () -> {
    };
    private SpelledWordInput input;

    @SuppressWarnings("LeakingThisInConstructor")
    public SpelledWordInputTestSkip() {
        DIUtils.injectFields(this);
    }

    @Before
    public void setUp() {
        originalInputSetting = settings.getWordInput();
        originalHelpSetting = settings.getWordHelp();
        settings.setWordInput(Settings.WordInput.CHAR_FIELDS);
    }

    @After
    public void tearDown() {
        settings.setWordInput(originalInputSetting);
        settings.setWordHelp(originalHelpSetting);
    }

    @Test
    public void testNumOfFields() {
        String word = "muskátli";
        input = new SpelledWordInput(word, listener);
        assertEquals(word.length(), input.getFields().size());

        word = "kakaós csiga";
        input = new SpelledWordInput(word, listener);
        assertEquals(word.length() - 1, input.getFields().size());
    }

    @Test
    public void testHelpFirstChar() {
        input = new SpelledWordInput("nagylelkű", listener);
        settings.setWordHelp(Settings.WordHelp.FIRST_CHAR);

        input.help();
        List<TextField> fields = input.getFields();

        assertEquals("N", fields.get(0).getText());
        for (int i = 1; i < fields.size(); i++) {
            assertEquals("", fields.get(i).getText());
        }
    }

    @Test
    public void testHelpFirstAndLastChar() {
        settings.setWordHelp(Settings.WordHelp.FIRST_AND_LAST_CHAR);
        input = new SpelledWordInput("piramis", listener);

        input.help();
        List<TextField> fields = input.getFields();

        assertEquals("P", fields.get(0).getText());
        for (int i = 1; i < fields.size() - 1; i++) {
            assertEquals("", fields.get(i).getText());
        }
        assertEquals("S", fields.get(fields.size() - 1).getText());
    }

    @Test
    public void testHelpVowels() {
        settings.setWordHelp(Settings.WordHelp.VOWELS);
        input = new SpelledWordInput("napsütés", listener);

        input.help();
        List<TextField> fields = input.getFields();

        assertEquals("", fields.get(0).getText());
        assertEquals("A", fields.get(1).getText());
        assertEquals("", fields.get(3).getText());
        assertEquals("Ü", fields.get(4).getText());
        assertEquals("", fields.get(fields.size() - 1).getText());
    }
}

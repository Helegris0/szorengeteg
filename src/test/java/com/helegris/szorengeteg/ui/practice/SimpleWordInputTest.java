/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.JavaFXThreadingRule;
import com.helegris.szorengeteg.ui.settings.Settings;
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
public class SimpleWordInputTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Inject
    private Settings settings;

    private Settings.WordInput originalInputSetting;
    private Settings.WordHelp originalHelpSetting;

    private final WordInputListener listener = () -> {
    };
    private SimpleWordInput input;

    @SuppressWarnings("LeakingThisInConstructor")
    public SimpleWordInputTest() {
        DIUtils.injectFields(this);
    }

    @Before
    public void setUp() {
        originalInputSetting = settings.getWordInput();
        originalHelpSetting = settings.getWordHelp();
        settings.setWordInput(Settings.WordInput.ONE_FIELD);
    }

    @After
    public void tearDown() {
        settings.setWordInput(originalInputSetting);
        settings.setWordHelp(originalHelpSetting);
    }

    @Test
    public void testHelpFirstChar() {
        settings.setWordHelp(Settings.WordHelp.FIRST_CHAR);

        input = new SimpleWordInput("csavarhúzó", listener);
        input.help();
        assertEquals("C", input.getFieldText());

        input = new SimpleWordInput("articsóka", listener);
        input.help();
        assertEquals("A", input.getFieldText());
    }

    @Test
    public void testHelpFirstTwoChars() {
        settings.setWordHelp(Settings.WordHelp.FIRST_TWO_CHARS);

        input = new SimpleWordInput("paradicsom", listener);
        input.help();
        assertEquals("PA", input.getFieldText());

        input = new SimpleWordInput("persely", listener);
        input.help();
        assertEquals("PE", input.getFieldText());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.ui.JavaFXThreadingRule;
import java.util.List;
import javafx.scene.control.TextField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author Timi
 */
public class SpelledWordInputTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private SpelledWordInput input;
    private boolean full;

    @Test
    public void testRightInput() {
        input = new SpelledWordInput("etana", () -> {});

        assertEquals("", input.getFields().get(1).getText());
        input.getFields().get(1).setText("T");
        assertEquals("T", input.getFields().get(1).getText());

        assertEquals("", input.getFields().get(3).getText());
        input.getFields().get(3).setText("N");
        assertEquals("N", input.getFields().get(3).getText());
    }

    @Test
    public void testWrongInput() {
        input = new SpelledWordInput("etana", () -> {});

        assertEquals("", input.getFields().get(0).getText());
        input.getFields().get(0).setText("K");
        assertEquals("", input.getFields().get(0).getText());

        assertEquals("", input.getFields().get(2).getText());
        input.getFields().get(2).setText("R");
        assertEquals("", input.getFields().get(2).getText());
    }

    @Test
    public void testFullInput() {
        full = false;
        input = new SpelledWordInput("ahma", () -> full = true);

        assertFalse(full);

        List<TextField> fields = input.getFields();
        fields.get(0).setText("a");
        fields.get(1).setText("h");

        assertFalse(full);

        fields.get(2).setText("m");
        fields.get(3).setText("a");

        assertTrue(full);
    }

    @Test
    public void testHelp() {
        input = new SpelledWordInput("karhu", () -> {});

        input.getFields().stream()
                .map(field -> field.getText())
                .forEach(text -> assertEquals("", text));

        input.help();

        List<TextField> fields = input.getFields();
        assertEquals("K", fields.get(0).getText());
        assertEquals("", fields.get(1).getText());
        assertEquals("", fields.get(2).getText());
        assertEquals("", fields.get(3).getText());
        assertEquals("U", fields.get(4).getText());
        assertEquals("", fields.get(5).getText());
        assertEquals("", fields.get(6).getText());
    }

    @Test
    public void testRevealWord() {
        input = new SpelledWordInput("orava", () -> {});

        input.getFields().stream()
                .map(field -> field.getText())
                .forEach(text -> assertEquals("", text));

        input.revealWord();

        List<TextField> fields = input.getFields();
        assertEquals("O", fields.get(0).getText());
        assertEquals("R", fields.get(1).getText());
        assertEquals("A", fields.get(2).getText());
        assertEquals("V", fields.get(3).getText());
        assertEquals("A", fields.get(4).getText());
        assertEquals("", fields.get(5).getText());
        assertEquals("", fields.get(6).getText());
    }

    @Test
    public void testDisableFieldsAfterRevelation() {
        input = new SpelledWordInput("susi", () -> {});

        input.getFields().stream()
                .forEach(field -> assertFalse(field.isDisabled()));

        input.revealWord();

        input.getFields().stream()
                .forEach(field -> assertTrue(field.isDisabled()));
    }

    @Test
    public void testNumberOfFields() {
        input = new SpelledWordInput("valkohäntäkauris", () -> {});

        assertEquals(16, input.getFields().size());
    }

    @Test
    public void testDefaultNumberOfFields() {
        input = new SpelledWordInput("ilves", () -> {});

        assertEquals(13, input.getFields().size());
    }
}

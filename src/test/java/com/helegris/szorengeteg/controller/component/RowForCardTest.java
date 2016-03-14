/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.JavaFXThreadingRule;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author Timi
 */
public class RowForCardTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private final String WORD = "word";
    private final String DESCRIPTION = "description";
    private Card card;
    private RowForCard row;
    private final Topic topic1 = new Topic("topic1");
    private final Topic topic2 = new Topic("topic2");

    @Before
    public void setUp() {
        List<Topic> topics = new ArrayList<>();
        topics.add(topic1);
        topics.add(topic2);
        RowForCard.refreshAllTopics(topics);

        card = new Card("word", "description");
        row = new RowForCard(x -> {
        }, card);

        row.getTxtWord().setText(WORD);
        row.getTxtDescription().setText(DESCRIPTION);
    }

    @Test
    public void testChangeTopic() {
        assertEquals(topic1, row.getUpdatedCard(topic1).getTopic());

        ComboBox comboBox = row.getCmbTopic();
        comboBox.setValue(topic2);
        assertEquals(topic2, row.getUpdatedCard().getTopic());
    }

    @Test
    public void testChangeText() {
        TextField txtWord = row.getTxtWord();
        TextField txtDescription = row.getTxtDescription();
        String otherWord1 = "another";
        String otherDescription1 = "another word";
        String otherWord2 = "yet another";
        String otherDescription2 = "yet another word";

        txtWord.setText(otherWord1);
        assertEquals(otherWord1, row.getUpdatedCard().getWord());

        txtDescription.setText(otherDescription1);
        assertEquals(otherDescription1, row.getUpdatedCard().getDescription());

        txtWord.setText(otherWord2);
        txtDescription.setText(otherDescription2);
        assertEquals(otherWord2, row.getUpdatedCard().getWord());
        assertEquals(otherDescription2, row.getUpdatedCard().getDescription());
    }

    @Test
    public void testDataValidity() {
        TextField txtWord = row.getTxtWord();
        TextField txtDescription = row.getTxtDescription();

        assertTrue(row.dataValidity());

        txtWord.setText("");
        assertFalse(row.dataValidity());
        assertTrue(row.missingData());

        txtWord.setText(WORD);
        txtDescription.setText("");
        assertFalse(row.dataValidity());
        assertTrue(row.missingData());

        txtWord.setText("");
        txtDescription.setText("");
        assertFalse(row.dataValidity());

        txtWord.setText("w");
        txtDescription.setText("d");
        assertTrue(row.dataValidity());
        assertFalse(row.missingData());
    }

}

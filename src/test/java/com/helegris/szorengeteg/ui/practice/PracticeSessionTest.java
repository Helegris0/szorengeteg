/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.business.model.Card;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyInt;
import org.mockito.Mockito;

/**
 *
 * @author Timi
 */
public class PracticeSessionTest {

    private PracticeSession session;
    private List<Card> sessionCards;

    @Before
    public void setUp() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        sessionCards = new ArrayList<>();
        sessionCards.add(new Card("word1", "desc1"));
        sessionCards.add(new Card("word2", "desc2"));

        session = new PracticeSession();

        Field field = session.getClass().getDeclaredField("sessionCards");
        field.setAccessible(true);
        field.set(session, sessionCards);

        field = session.getClass().getDeclaredField("currentCard");
        field.setAccessible(true);
        field.set(session, sessionCards.get(0));

        PositionSaver saver = Mockito.mock(PositionSaver.class);
        Mockito.doNothing().when(saver).setCardOrdinal(anyInt());
        field = session.getClass().getDeclaredField("positionSaver");
        field.setAccessible(true);
        field.set(session, saver);
    }

    @Test
    public void testJumpTo() {
        assertEquals(sessionCards.get(1), session.jumpTo(1));
        assertEquals(sessionCards.get(0), session.jumpTo(0));
    }

    @Test
    public void testNextCard() {
        assertEquals(sessionCards.get(1), session.nextCard());
    }

    @Test
    public void testGetLength() {
        assertEquals(sessionCards.size(), session.getLength());
        assertFalse(sessionCards.size() != session.getLength());
    }

    @Test
    public void testGetSessionCards() {
        assertEquals(sessionCards, session.getSessionCards());
    }

    @Test
    public void testGetCurrentCard() {
        assertEquals(sessionCards.get(0), session.getCurrentCard());
        session.nextCard();
        assertEquals(sessionCards.get(1), session.getCurrentCard());
    }

    @Test
    public void testGetIndex() {
        assertEquals(0, session.getIndex());
        session.nextCard();
        assertEquals(1, session.getIndex());
    }
}

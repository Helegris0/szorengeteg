/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.business.service.CardLoader;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.Topic;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Controller class for practicing sessions. A session currently means a
 * walkthrough of the cards in a given topic.
 *
 * @author Timi
 */
public class PracticeSession {

    @Inject
    private CardLoader cardLoader;
    @Inject
    private PositionSaver positionSaver;

    private Topic topic;

    private List<Card> sessionCards = new ArrayList<>();
    private Card currentCard;

    private int index;

    public PracticeSession() {
    }

    public PracticeSession(Topic topic) {
        this(topic, 0);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeSession(Topic topic, int startIndex) {
        this.topic = topic;
        DIUtils.injectFields(this);
        selectCards(startIndex);
    }

    /**
     * Sets the cards for the session. If there is a topic defined, it's cards
     * of the topic, otherwise it's every card.
     *
     * @param startIndex index of thhe card to be shown first
     */
    private void selectCards(int startIndex) {
        List<Card> allCards;
        if (topic == null) {
            allCards = cardLoader.loadAll();
        } else {
            allCards = cardLoader.loadByTopic(topic);
        }

        if (!allCards.isEmpty()) {
            if (topic != null) {
                allCards.sort((c1, c2) -> c1.getOrdinal() != null
                        ? c1.getOrdinal() - c2.getOrdinal() : 1);
            }
            sessionCards = allCards;

            positionSaver.setTopic(topic);
            jumpTo(startIndex);
        }
    }

    /**
     *
     * @param i index of the card to jump to
     * @return the desired card
     */
    public Card jumpTo(int i) {
        currentCard = sessionCards.get(i);
        index = i;
        positionSaver.setCardOrdinal(i);
        return currentCard;
    }

    /**
     *
     * @return next card in the session
     */
    public Card nextCard() {
        if (index < sessionCards.size() - 1) {
            return jumpTo(index + 1);
        } else {
            return null;
        }
    }

    public int getLength() {
        return sessionCards.size();
    }

    public List<Card> getSessionCards() {
        return sessionCards;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public Topic getTopic() {
        return topic;
    }

    public int getIndex() {
        return index;
    }
}

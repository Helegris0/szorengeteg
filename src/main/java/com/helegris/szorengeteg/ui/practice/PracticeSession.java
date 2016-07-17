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
        this(null);
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

    public Card jumpTo(int i) {
        currentCard = sessionCards.get(i);
        index = i;
        positionSaver.setCardOrdinal(i);
        return currentCard;
    }

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

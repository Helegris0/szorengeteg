/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.ui.settings.Settings;
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
    private Settings settings;

    private List<Card> sessionCards = new ArrayList<>();
    private Card currentCard;

    private final List<Card> correctAnswers = new ArrayList<>();
    private final List<Card> incorrectAnswers = new ArrayList<>();

    private int index;

    public PracticeSession() {
        this(null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeSession(Topic topic) {
        DIUtils.injectFields(this);
        selectCards(topic);
    }

    private void selectCards(Topic topic) {
        List<Card> allCards;
        if (topic == null) {
            allCards = cardLoader.loadAll();
        } else {
            allCards = cardLoader.loadByTopic(topic);
        }

        if (!allCards.isEmpty()) {
            if (settings.isRandomOrder()) {
                int expectedSize;

                if (settings.isAskAll()
                        || settings.getWordsPerSession() > allCards.size()) {
                    expectedSize = allCards.size();
                } else {
                    expectedSize = settings.getWordsPerSession();
                }

                while (sessionCards.size() < expectedSize) {
                    Card randomCard = allCards.get(
                            (int) (Math.random() * allCards.size()));
                    sessionCards.add(randomCard);
                    allCards.remove(randomCard);
                }
            } else {
                sessionCards = allCards;
            }

            currentCard = sessionCards.get(0);
        }
    }

    public Card nextCard() {
        if (index < sessionCards.size() - 1) {
            currentCard = sessionCards.get(index + 1);
            index++;
            return currentCard;
        } else {
            return null;
        }
    }

    public void correctAnswer() {
        correctAnswers.add(currentCard);
    }

    public void incorrectAnswer() {
        incorrectAnswers.add(currentCard);
    }

    public void repeatCard() {
        sessionCards.add(currentCard);
    }

    public List<Card> getSessionCards() {
        return sessionCards;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public List<Card> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<Card> getIncorrectAnswers() {
        return incorrectAnswers;
    }
}

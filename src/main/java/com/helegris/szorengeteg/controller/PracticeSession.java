/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.Settings;
import com.helegris.szorengeteg.model.CardLoader;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
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

    private final int sessionLength;

    private final List<Card> sessionCards = new ArrayList<>();
    private Card currentCard;

    private final List<Card> correctAnswers = new ArrayList<>();
    private final List<Card> incorrectAnswers = new ArrayList<>();

    public PracticeSession() {
        this(null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeSession(Topic topic) {
        sessionLength = new Settings().getWordsPerSession();
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
            int expectedSize;

            if (sessionLength > allCards.size()) {
                expectedSize = allCards.size();
            } else {
                expectedSize = sessionLength;
            }

            while (sessionCards.size() < expectedSize) {
                Card randomCard = allCards.get(
                        (int) (Math.random() * allCards.size()));
                if (!sessionCards.contains(randomCard)) {
                    sessionCards.add(randomCard);
                }
            }

            currentCard = sessionCards.get(0);
        }
    }

    public Card nextCard() {
        int prevIndex = sessionCards.indexOf(currentCard);
        if (prevIndex < sessionCards.size() - 1) {
            currentCard = sessionCards.get(prevIndex + 1);
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

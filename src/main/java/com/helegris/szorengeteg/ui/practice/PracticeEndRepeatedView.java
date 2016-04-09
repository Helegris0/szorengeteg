/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Card;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 *
 * @author Timi
 */
public class PracticeEndRepeatedView extends PracticeEnd {

    public static final String FXML = "fxml/practice_end_repeated.fxml";

    @FXML
    private ListView lstWords;

    private ObservableList<Card> wordList;
    private final Map<Card, Integer> occurrences = new HashMap<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeEndRepeatedView(List<Card> sessionCards) {
        setData(sessionCards);
        FXMLLoaderHelper.load(FXML, this);
    }

    private void setData(List<Card> sessionCards) {
        List<Card> temp = new ArrayList<>();

        sessionCards.stream().forEach(card -> {
            if (!temp.contains(card)) {
                temp.add(card);
                occurrences.put(card, 1);
            } else {
                occurrences.put(card, occurrences.get(card) + 1);
            }
        });

        wordList = FXCollections.observableArrayList(temp);
    }

    @Override
    protected void initialize() {
        lstWords.setItems(wordList);
        lstWords.getSelectionModel().selectedItemProperty().addListener(
                this::selectionChanged);
        super.initialize();
    }

    @Override
    protected void setSelectedCard(Card card) {
        super.setSelectedCard(card);
        int occurrence = occurrences.get(card);
        lblWord.setText(Messages.msg("practice.correct_answers") + " "
                + occurrence + Messages.msg("practice.occurrence"));
    }
}

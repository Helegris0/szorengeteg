/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.CDIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.model.CardLoader;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class WordsFormView extends CardsEditorForm {

    public static final String FXML = "fxml/words_form.fxml";

    private List<RowForCard> rowsWithCardsToModify = new ArrayList<>();
    private List<Card> cardsToDelete = new ArrayList<>();

    @Inject
    private CardLoader cardLoader;

    @SuppressWarnings("LeakingThisInConstructor")
    public WordsFormView() {
        CDIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        btnSave.setOnAction(this::saveWords);
        loadRows();
    }

    private void loadRows() {
        cardLoader.loadAll().stream().forEach(card -> {
            RowForCard row = new RowForCard(this, card);
            rowsWithCardsToModify.add(row);
            rows.add(row);
        });
    }

    @Override
    public void deleteRow(RowForCard row) {
        if (rowsWithCardsToModify.contains(row)) {
            rowsWithCardsToModify.remove(row);
            cardsToDelete.add(row.getCard());
        }
        super.deleteRow(row);
    }

    private void saveWords(ActionEvent event) {
        rowsOfCardsToCreate.stream().forEach((row) -> {
            if (row.dataValidity()) {
                try {
                    Card card = row.getUpdatedCard();
                    Topic topic = card.getTopic();
                    if (topic != null) {
                        topic.addCard(card);
                    }
                    prepareToCreate(card);
                } catch (FileNotFoundException ex) {
                    alertFileNotFound(ex, row.getImageFile());
                } catch (IOException ex) {
                    alertIOEx(ex);
                }
            }
        });

        rowsWithCardsToModify.stream().forEach((row) -> {
            if (row.dataValidity()) {
                try {
                    prepareToModify(row.getUpdatedCard());
                } catch (IOException ex) {
                    alertIOEx(ex);
                }
            }
        });

        cardsToDelete.stream().forEach((card) -> {
            Topic topic = card.getTopic();
            if (topic != null) {
                topic.removeCard(card);
                prepareToModify(topic);
            }
            prepareToDelete(card);
        });

        getTransactionDone();

        VistaNavigator.getMainView().loadContentTopics();
    }
}

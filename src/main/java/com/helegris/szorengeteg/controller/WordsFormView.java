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
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class WordsFormView extends CardsEditorForm {

    public static final String FXML = "fxml/words_form.fxml";

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
            RowForCard row = new RowForCard(this::deleteRow, card);
            rows.add(row);
        });
    }

    private void saveWords(ActionEvent event) {
        getTransactionDone();
        VistaNavigator.getMainView().loadContentTopics();
    }

    @Override
    protected void getTransactionDone() {
        if (rows.stream().anyMatch(RowForCard::missingData)) {
            throw new MissingDataException();
        }

        List<Card> cards = rows.stream()
                .filter(RowForCard::dataValidity)
                .map(row -> row.getUpdatedCard())
                .collect(Collectors.toList());
        entitySaver.saveWords(cards);
    }

}

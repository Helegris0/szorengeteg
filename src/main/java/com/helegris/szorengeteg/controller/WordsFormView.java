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
            rows.add(row);
        });
    }

    private void saveWords(ActionEvent event) {
        getTransactionDone();
        VistaNavigator.getMainView().loadContentTopics();
    }

    @Override
    protected void getTransactionDone() {
        List<Card> cards = new ArrayList<>();
        rows.stream().forEach(row -> {
            if (row.dataValidity()) {
                try {
                    cards.add(row.getUpdatedCard());
                } catch (FileNotFoundException ex) {
                    alertFileNotFound(ex, row.getImageFile());
                } catch (IOException ex) {
                    alertIOEx(ex);
                }
            }
        });
        entitySaver.saveWords(cards);
    }
}

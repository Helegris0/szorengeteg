/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.model.CardLoader;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class WordsFormView extends CardsEditorForm {

    public static final String FXML = "fxml/words_form.fxml";

    @Inject
    private CardLoader cardLoader;

    @FXML
    private TableColumn colTopic;

    @SuppressWarnings("LeakingThisInConstructor")
    public WordsFormView() {
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        colTopic.setComparator(new ComboBoxComparator());
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

    private class ComboBoxComparator implements Comparator<ComboBox<Topic>> {

        @Override
        public int compare(ComboBox o1, ComboBox o2) {
            Topic t1 = (Topic) o1.getValue();
            Topic t2 = (Topic) o2.getValue();
            if (t1 == null) {
                return -1;
            }
            if (t2 == null) {
                return 1;
            }
            return t1.getName().compareTo(t2.getName());
        }

    }

}

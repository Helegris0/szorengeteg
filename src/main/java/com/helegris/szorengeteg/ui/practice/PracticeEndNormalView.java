/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.business.model.Card;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

/**
 *
 * @author Timi
 */
public class PracticeEndNormalView extends PracticeEnd {

    public static final String FXML = "fxml/practice_end_normal.fxml";

    @FXML
    private ListView lstCorrect;
    @FXML
    private ListView lstIncorrect;

    private final ObservableList<Card> correctAnswers;
    private final ObservableList<Card> incorrectAnswers;

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeEndNormalView(List<Card> correctAnswers, List<Card> incorrectAnswers) {
        this.correctAnswers
                = FXCollections.observableArrayList(correctAnswers);
        this.incorrectAnswers
                = FXCollections.observableArrayList(incorrectAnswers);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    @Override
    protected void initialize() {
        lstCorrect.setItems(correctAnswers);
        lstIncorrect.setItems(incorrectAnswers);
        lstCorrect.getSelectionModel().selectedItemProperty().addListener(
                this::selectionChanged);
        lstIncorrect.getSelectionModel().selectedItemProperty().addListener(
                this::selectionChanged);
        lstCorrect.focusedProperty().addListener(this::focusChangedCorrect);
        lstIncorrect.focusedProperty().addListener(this::focusChangedIncorrect);
        super.initialize();
    }

    private void focusChangedCorrect(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
        if (newValue) {
            focusGained(lstCorrect);
        }
    }

    private void focusChangedIncorrect(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
        if (newValue) {
            focusGained(lstIncorrect);
        }
    }

    private void focusGained(ListView listView) {
        MultipleSelectionModel model = listView.getSelectionModel();

        if (!model.isEmpty()) {
            setSelectedCard((Card) model.getSelectedItem());
        }
    }
}

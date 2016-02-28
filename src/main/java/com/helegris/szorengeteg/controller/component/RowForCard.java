/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.controller.TopicFormView;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author Timi
 */
public class RowForCard {

    private TopicFormView topicView;
    private Card card = new Card();
    private TextField txtWord = new TextField();
    private TextField txtDescription = new TextField();
    private Button btnDelete = new Button("töröl");

    public RowForCard(TopicFormView topicView) {
        this.topicView = topicView;
        this.btnDelete.setOnAction(this::delete);
    }

    public RowForCard(TopicFormView topicView, Card card) {
        this(topicView);
        this.card = card;
        this.txtWord.setText(card.getWord());
        this.txtDescription.setText(card.getDescription());
    }

    private void delete(ActionEvent event) {
        topicView.deleteRow(this);
    }

    public boolean dataValidity() {
        return !"".equals(txtWord.getText()) && !"".equals(txtDescription.getText());
    }

    public Card getUpdatedCard() {
        card.setWord(txtDescription.getText());
        card.setDescription(txtDescription.getText());
        return card;
    }

    public Card getUpdatedCard(Topic topic) {
        getUpdatedCard();
        card.setTopic(topic);
        return card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public TextField getTxtWord() {
        return txtWord;
    }

    public void setTxtWord(TextField txtWord) {
        this.txtWord = txtWord;
    }

    public TextField getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(TextField txtDescription) {
        this.txtDescription = txtDescription;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }
}

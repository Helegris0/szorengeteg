/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.model.entity.Card;
import javafx.scene.control.Button;

/**
 *
 * @author Timi
 */
public class RowForCard {

    private Card card;
    private String word;
    private String description;
    private Button btnEdit;
    private Button btnDelete;

    public RowForCard() {
        this.btnEdit = new Button("szerkeszt");
        this.btnDelete = new Button("töröl");
    }

    public RowForCard(Card card) {
        this();
        this.card = card;
        this.word = card.getWord();
        this.description = card.getDescription();
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
        card.setWord(word);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        card.setDescription(description);
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public void setBtnEdit(Button btnEdit) {
        this.btnEdit = btnEdit;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }
}

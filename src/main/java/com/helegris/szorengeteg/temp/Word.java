/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.temp;

import javafx.scene.control.Button;

/**
 *
 * @author Timi
 */
public class Word {
    
    private String word;
    private String description;
    private Button btnEdit;
    private Button btnDelete;

    public Word(String word, String description) {
        this.word = word;
        this.description = description;
        this.btnEdit = new Button("szerkeszt");
        this.btnDelete = new Button("töröl");
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

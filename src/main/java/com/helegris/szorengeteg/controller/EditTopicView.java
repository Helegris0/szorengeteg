/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 *
 * @author Timi
 */
public class EditTopicView extends TopicFormView {
    
    private List<RowForCard> rowsWithCardsToModify = new ArrayList<>();
    private List<Card> cardsToDelete = new ArrayList<>();

    public EditTopicView(Topic topic) {
        super();
        this.topic = topic;
        txtName.setText(topic.getName());
        loadOriginalImage();
        loadOriginalRows();
    }

    private void loadOriginalImage() {
        if (topic.getImage() != null) {
            InputStream inputStream = new ByteArrayInputStream(topic.getImage());
            Image image = new Image(inputStream);
            imageView.setImage(image);
            btnDeleteImage.setVisible(true);
        }
    }

    private void loadOriginalRows() {
        topic.getCards().stream().forEach((card) -> {
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

    @Override
    protected void prepareTopic() throws IOException {
        super.prepareTopic();
        if (imageView.getImage() == null && imageFile == null) {
            topic.setImage(null);
        }
    }

    @Override
    protected void prepareAndSaveCards() {
        super.prepareAndSaveCards();
        rowsWithCardsToModify.stream().forEach((row) -> {
            if (row.dataValidity()) {
                entitySaver.modify(row.getUpdatedCard());
            }
        });
        cardsToDelete.stream().forEach((card) -> {
            topic.removeCard(card);
            entitySaver.delete(card);
        });
    }

}

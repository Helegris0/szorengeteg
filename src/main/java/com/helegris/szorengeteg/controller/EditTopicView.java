/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.ImageUtils;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            imageView.setImage(ImageUtils.loadImage(topic.getImage()));
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
                try {
                    entitySaver.modify(row.getUpdatedCard());
                } catch (FileNotFoundException ex) {
                    alertFileNotFound(ex);
                } catch (IOException ex) {
                    alertIOEx(ex);
                }
            }
        });
        cardsToDelete.stream().forEach((card) -> {
            topic.removeCard(card);
            entitySaver.delete(card);
        });
    }

}

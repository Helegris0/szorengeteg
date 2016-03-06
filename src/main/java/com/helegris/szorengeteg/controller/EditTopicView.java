/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.ImageUtils;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

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
        btnDeleteTopic.setVisible(true);
        btnDeleteTopic.setOnAction(this::deleteTopic);
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

    protected void deleteTopic(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Figyelmeztetés");
        alert.setHeaderText("Biztosan törölni szeretné a témakört?");
        alert.setContentText("A művelet nem vonható vissza.");

        ButtonType buttonTypeDelete = new ButtonType("Törlés");
        ButtonType buttonTypeCancel = new ButtonType("Mégse", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeDelete) {
            topic.getCards().stream().forEach(card -> entitySaver.delete(card));
            entitySaver.delete(topic);
            VistaNavigator.getMainView().loadContentTopics();
        } else {
            alert.close();
        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.ImageUtils;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.PersistentObject;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        alert.setTitle(Messages.msg("alert.warning"));
        alert.setHeaderText(Messages.msg("alert.sure_delete_topic"));
        alert.setContentText(Messages.msg("alert.cannot_be_undone"));

        ButtonType typeDelete
                = new ButtonType(Messages.msg("alert.delete_topic"));
        ButtonType typeDeleteWithWords
                = new ButtonType(Messages.msg("alert.delete_topic_and_words"));
        ButtonType typeCancel
                = new ButtonType(Messages.msg("alert.cancel"),
                        ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(typeDelete);
        if (!topic.getCards().isEmpty()) {
            alert.getButtonTypes().add(typeDeleteWithWords);
        }
        alert.getButtonTypes().add(typeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == typeDelete) {
            List<Card> toModify = new ArrayList<>();
            topic.getCards().stream().forEach(card -> {
                card.setTopic(null);
                toModify.add(card);
            });
            entitySaver.complexTransaction(null, toModify, Arrays.asList(topic));
            VistaNavigator.getMainView().loadContentTopics();
        } else if (result.get() == typeDeleteWithWords) {
            List<PersistentObject> toDelete = new ArrayList<>();
            topic.getCards().stream().forEach(toDelete::add);
            toDelete.add(topic);
            entitySaver.complexTransaction(null, null, toDelete);
            VistaNavigator.getMainView().loadContentTopics();
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
    protected void prepareCards() {
        super.prepareCards();
        rowsWithCardsToModify.stream().forEach((row) -> {
            if (row.dataValidity()) {
                try {
                    prepareToModify(row.getUpdatedCard());
                } catch (IOException ex) {
                    alertIOEx(ex);
                }
            }
        });
        cardsToDelete.stream().forEach((card) -> {
            topic.removeCard(card);
            prepareToModify(topic);
            prepareToDelete(card);
        });
    }

}

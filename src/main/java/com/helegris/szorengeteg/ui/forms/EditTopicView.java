/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.ui.MediaLoader;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.service.CardLoader;
import com.helegris.szorengeteg.business.model.PersistentObject;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.ui.DefaultImage;
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
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class EditTopicView extends TopicFormView {

    @Inject
    private CardLoader cardLoader;

    @SuppressWarnings("LeakingThisInConstructor")
    public EditTopicView(Topic topic) {
        super();
        this.topic = topic;
        lblOrdinal.setText(topic.getOrdinal() + ". ");
        txtName.setText(topic.getName());
        loadOriginalImage();
        loadOriginalRows();
//        btnDefaultStates.setVisible(true);
//        btnDeleteTopic.setVisible(true);
//        btnDefaultStates.setOnAction(this::defaultStates);
//        btnDeleteTopic.setOnAction(this::deleteTopic);
    }

    private void loadOriginalImage() {
        if (topic.getImage() != null) {
            imageView.setImage(new MediaLoader().loadImage(topic.getImage()));
            btnDeleteImage.setVisible(true);
        } else {
            imageView.setImage(DefaultImage.getInstance());
        }
    }

    private void loadOriginalRows() {
        loadRows(cardLoader.loadByTopic(topic));
    }

    private void defaultStates(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Kapcsolók visszaállítása"); // TODO: msg
        alert.setHeaderText("Szeretné alaphelyzetbe állítani a kapcsolókat?");
        alert.setContentText("");

        ButtonType typeYes = new ButtonType(Messages.msg("common.yes"), ButtonData.YES);
        ButtonType typeCancel = new ButtonType(Messages.msg("common.cancel"), ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(typeYes);
        alert.getButtonTypes().add(typeCancel);

        alert.setX(getScene().getWidth() / 2);
        alert.setY(100);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == typeYes) {
            rows.stream().forEach(row -> {
                row.getCard().setLastInput(false);
                row.getCard().setLastHelp(false);
                row.getCard().setLastVisual(false);
                row.getCard().setLastGaveUp(false);
                row.getCard().setLastPlayedAudio(false);
            });

            Alert alertInfo = new Alert(AlertType.INFORMATION);
            alertInfo.setTitle(Messages.msg("form.set_default"));
            alertInfo.setHeaderText(Messages.msg("alert.save_change"));
            alertInfo.showAndWait();
            alert.close();
        }
    }

    private void deleteTopic(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(Messages.msg("alert.warning"));
        alert.setHeaderText(Messages.msg("alert.sure_delete_topic"));
        alert.setContentText(Messages.msg("alert.cannot_be_undone"));

        ButtonType typeDeleteWithWords
                = new ButtonType(Messages.msg("alert.delete_topic_and_words"));
        ButtonType typeCancel
                = new ButtonType(Messages.msg("common.cancel"),
                        ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().clear();
        if (!cardLoader.loadByTopic(topic).isEmpty()) {
            alert.getButtonTypes().add(typeDeleteWithWords);
        }
        alert.getButtonTypes().add(typeCancel);

        alert.setX(getScene().getWidth() / 2);
        alert.setY(100);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == typeDeleteWithWords) {
            List<PersistentObject> toDelete = new ArrayList<>();
            cardLoader.loadByTopic(topic).stream().forEach(toDelete::add);
            toDelete.add(topic);
            entitySaver.delete(toDelete);
            VistaNavigator.getMainView().loadContentTopics();
        }
    }

    @Override
    protected void prepareTopic() throws FileNotFoundException, IOException {
        super.prepareTopic();
        if (imageView.getImage() == null && imageFile == null) {
            topic.setImage(null);
        }
    }
}

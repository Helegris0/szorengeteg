/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.ui.ImageLoader;
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
        txtName.setText(topic.getName());
        loadOriginalImage();
        loadOriginalRows();
        btnDeleteTopic.setVisible(true);
        btnDeleteTopic.setOnAction(this::deleteTopic);
    }

    private void loadOriginalImage() {
        if (topic.getImage() != null) {
            imageView.setImage(new ImageLoader().loadImage(topic.getImage()));
            btnDeleteImage.setVisible(true);
        } else {
            imageView.setImage(DefaultImage.getInstance());
        }
    }

    private void loadOriginalRows() {
        loadRows(cardLoader.loadByTopic(topic));
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
        if (!cardLoader.loadByTopic(topic).isEmpty()) {
            alert.getButtonTypes().add(typeDeleteWithWords);
        }
        alert.getButtonTypes().add(typeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == typeDelete) {
            entitySaver.deleteTopicWithoutWords(topic);
            VistaNavigator.getMainView().loadContentTopics();
        } else if (result.get() == typeDeleteWithWords) {
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

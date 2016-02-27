/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;

/**
 *
 * @author Timi
 */
public class EditTopicView extends TopicView {

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
            rows.add(new RowForCard(card));
        });
    }

    @Override
    protected void prepareTopic() throws IOException {
        super.prepareTopic();
        if (imageView.getImage() == null && imageFile == null) {
            topic.setImage(null);
        }
        entitySaver.modify(topic);
        VistaNavigator.getMainView().loadContentTopics();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.ui.MediaLoader;
import com.helegris.szorengeteg.business.service.CardLoader;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.ui.DefaultImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.inject.Inject;

/**
 * A form that conatins data of an existing topic, allowing to edit it.
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

    @Override
    protected void prepareTopic() throws FileNotFoundException, IOException {
        super.prepareTopic();
        if (imageView.getImage() == null && imageFile == null) {
            topic.setImage(null);
        }
    }
}

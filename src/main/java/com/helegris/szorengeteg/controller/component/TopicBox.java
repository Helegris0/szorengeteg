/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Timi
 */
public class TopicBox extends Pane {

    private static final String FXML = "fxml/topicbox.fxml";

    @FXML
    private ImageView imageView;
    @FXML
    private Label lblName;
    @FXML
    private ClickableLabel lblEdit;
    @FXML
    private ClickableLabel lblInfo;
    @FXML
    private Label lblNumberOfWords;

    private Topic topic;
    private Image image;
    private boolean infoVisibility;

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicBox(Topic topic) {
        this.topic = topic;
        setImage();
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    protected void initialize() {
        if (image != null) {
            imageView.setImage(image);
        }
        lblName.setText(topic.getName());
        lblEdit.setOnMouseClicked((MouseEvent event) -> {
            editTopic(event);
        });
        lblInfo.setOnMouseClicked((MouseEvent event) -> {
            toggleInfoVisibility(event);
        });
        lblNumberOfWords.setVisible(infoVisibility);
        lblNumberOfWords.setText("összes szó: " + topic.getCards().size());
    }

    protected void editTopic(MouseEvent event) {
        VistaNavigator.getMainView().loadContentEditTopic(topic);
    }

    private void toggleInfoVisibility(MouseEvent event) {
        infoVisibility = !infoVisibility;
        lblNumberOfWords.setVisible(infoVisibility);
    }

    private void setImage() {
        if (topic.getImage() != null) {
            InputStream inputStream = new ByteArrayInputStream(topic.getImage());
            image = new Image(inputStream);
        }
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}

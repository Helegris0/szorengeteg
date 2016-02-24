/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.model.entity.Topic;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author Timi
 */
public class TopicBox extends Pane {

    @FXML
    private Label lblName;

    @FXML
    private Label lblNoAllCards;

    private Topic topic;

    private static final String FXML = "/fxml/topicbox.fxml";

    public TopicBox(Topic topic) {
        this.topic = topic;
        loadFxml();
        setLabels();
    }

    private void loadFxml() {
        InputStream inputStream = this.getClass().getResourceAsStream(FXML);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(TopicBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setLabels() {
        lblName.setText(topic.getName());
        lblNoAllCards.setText("Összesen: " + topic.getCards().size());
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}

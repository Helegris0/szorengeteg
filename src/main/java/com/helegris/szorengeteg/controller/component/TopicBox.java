/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.model.entity.Topic;
import javafx.fxml.FXML;
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
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    protected void initialize() {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.controller.component.TopicBox;
import com.helegris.szorengeteg.model.TopicLoader;
import com.helegris.szorengeteg.model.entity.Topic;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javax.inject.Inject;

/**
 * FXML Controller class
 *
 * @author Timi
 */
public class TopicsController implements Initializable {

    @FXML
    private VBox vBox;

    @Inject
    private TopicLoader topicLoader;

    private List<Topic> topics;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        loadTopics();
//        createTopicBoxes();
    }

    private void loadTopics() {
        topics = topicLoader.loadAll();
    }

    private void createTopicBoxes() {
        for (Topic topic : topics) {
            vBox.getChildren().add(new TopicBox(topic));
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.CDIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.component.TopicBox;
import com.helegris.szorengeteg.model.TopicLoader;
import com.helegris.szorengeteg.model.entity.Topic;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javax.inject.Inject;

/**
 * FXML Controller class
 *
 * @author Timi
 */
public class TopicsView extends ScrollPane {

    public static final String FXML = "fxml/topics.fxml";

    @FXML
    private VBox vBox;

    @Inject
    private TopicLoader topicLoader;

    private List<Topic> topics = new ArrayList<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicsView() {
        CDIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    protected void initialize() {
        loadTopics();
        createTopicBoxes();
    }

    private void loadTopics() {
        topics = topicLoader.loadAll();
    }

    private void createTopicBoxes() {
        topics.stream().forEach((topic) -> {
            vBox.getChildren().add(new TopicBox(topic));
        });
    }

}

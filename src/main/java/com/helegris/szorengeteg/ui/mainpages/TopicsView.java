/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.mainpages;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.service.TopicLoader;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.EntitySaver;
import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.ui.practice.PositionSaver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class TopicsView extends AnchorPane {

    public static final String FXML = "fxml/topics.fxml";

    @Inject
    private TopicLoader topicLoader;
    @Inject
    private EntitySaver entitySaver;
    @Inject
    private PositionSaver positionSaver;

    @FXML
    private VBox vBox;
    @FXML
    private Button btnNewTopic;
    @FXML
    private Button btnModifyOrder;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private List<Topic> topics;
    private List<TopicBox> topicBoxes;

    private final TopicBoxMoveListener moveListener = new TopicBoxMoveListener() {

        @Override
        public void moveUp(TopicBox topicBox) {
            int index = vBox.getChildren().indexOf(topicBox);
            if (index > 0) {
                Collections.swap(topicBoxes, index, index - 1);
                setTopicBoxes();
            }
        }

        @Override
        public void moveDown(TopicBox topicBox) {
            int index = vBox.getChildren().indexOf(topicBox);
            if (index < vBox.getChildren().size() - 1) {
                Collections.swap(topicBoxes, index, index + 1);
                setTopicBoxes();
            }
        }
    };

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicsView() {
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
        Platform.runLater(() -> requestFocus());
    }

    @FXML
    private void initialize() {
        loadTopics();
        createTopicBoxes();
        btnNewTopic.setOnAction(this::newTopic);
        btnModifyOrder.setOnAction(this::allowModifyOrder);
        btnSave.setOnAction(this::saveOrder);
        btnCancel.setOnAction(this::cancel);
    }

    private void loadTopics() {
        topics = topicLoader.loadAll();
        topics.sort((t1, t2) -> t1.getOrdinal() != null ? t1.getOrdinal() - t2.getOrdinal() : 1);
    }

    private void createTopicBoxes() {
        if (topics.isEmpty()) {
            vBox.getChildren().add(new Label(Messages.msg("topics.no_topics")));
        } else {
            topicBoxes = new ArrayList<>();
            topics.stream().forEach((topic) -> {
                TopicBox topicBox = new TopicBox(topic, moveListener);
                topicBoxes.add(topicBox);
                if (topic.equals(PositionSaver.getCurrentTopic())) {
                    topicBox.highlight();
                }
            });
            setTopicBoxes();

            List<TopicBox> withoutOrdinals = topicBoxes.stream()
                    .filter(topicBox -> (topicBox.getTopic().getOrdinal() == null))
                    .collect(Collectors.toList());
            if (!withoutOrdinals.isEmpty()) {
                saveOrder();
                withoutOrdinals.stream().forEach(topicBox -> topicBox.setNameLabel());
            }
        }
    }

    private void setTopicBoxes() {
        vBox.getChildren().clear();
        topicBoxes.stream().forEach((topicBox) -> {
            vBox.getChildren().add(topicBox);
        });
    }

    private void newTopic(ActionEvent event) {
        VistaNavigator.getMainView().loadContentNewTopic();
    }

    private void allowModifyOrder(ActionEvent event) {
        orderModVisibility(true);
        topicBoxes.stream().forEach(topicBox -> topicBox.setModifyable(true));
    }

    private void saveOrder(ActionEvent event) {
        orderModVisibility(false);
        saveOrder();
    }

    private void saveOrder() {
        topicBoxes.stream().forEach(topicBox -> {
            topicBox.setModifyable(false);
            topicBox.getTopic().setOrdinal(topicBoxes.indexOf(topicBox) + 1);
            topicBox.setNameLabel();
        });
        entitySaver.saveTopics(topics);
    }

    private void cancel(ActionEvent event) {
        orderModVisibility(false);
        createTopicBoxes();
    }

    private void orderModVisibility(boolean modifying) {
        btnModifyOrder.setVisible(!modifying);
        btnSave.setVisible(modifying);
        btnCancel.setVisible(modifying);
    }
}

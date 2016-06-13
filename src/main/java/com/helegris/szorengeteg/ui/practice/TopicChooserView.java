/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.TopicLoader;
import com.helegris.szorengeteg.ui.practice.PracticeView.TopicChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class TopicChooserView extends AnchorPane {

    private static final String FXML = "fxml/topic_chooser.fxml";

    @Inject
    private TopicLoader topicLoader;

    @FXML
    private ListView listView;
    @FXML
    private Button btnChoose;
    @FXML
    private Button btnCancel;

    private ObservableList<Topic> topics;
    private final Topic currentTopic;
    private final TopicChangeListener listener;

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicChooserView(Topic currentTopic, TopicChangeListener listener) {
        this.currentTopic = currentTopic;
        this.listener = listener;
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    public void initialize() {
        topics = FXCollections.observableArrayList(topicLoader.loadAllOrdered());
        listView.setItems(topics);
        if (topics.contains(currentTopic)) {
            listView.getSelectionModel().select(currentTopic);
        }
        btnChoose.setOnAction(this::choose);
        btnCancel.setOnAction(this::cancel);
    }

    private void close() {
        ((Stage) this.getScene().getWindow()).close();
    }

    private void choose(ActionEvent event) {
        Topic chosen = (Topic) listView.getSelectionModel().getSelectedItem();
        if (chosen != null) {
            listener.changeTopic(chosen);
        }
        close();
    }

    private void cancel(ActionEvent event) {
        close();
    }
}

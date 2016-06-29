/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.mainpages;

import com.helegris.szorengeteg.ui.forms.NewTopicView;
import com.helegris.szorengeteg.ui.forms.EditTopicView;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Topic;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Timi
 */
public class MainView extends AnchorPane {

    public static final String FXML = "fxml/main.fxml";

    public static final String TOPICS_TITLE = Messages.msg("menu.topics");
    public static final String NEW_TOPIC_TITLE = Messages.msg("menu.new_topic");
    public static final String EDIT_TOPIC_TITLE = Messages.msg("menu.edit_topic");
//    public static final String WORDS_TITLE = Messages.msg("menu.words");

    @FXML
    private Menu mnTopics;
    @FXML
    private Menu mnNewTopic;
//    @FXML
//    private Menu mnWords;
    @FXML
    private Label lblTitle;
    @FXML
    private StackPane vistaHolder;

    @SuppressWarnings("LeakingThisInConstructor")
    public MainView() {
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    public void initialize() {
        lblTitle.setText(TOPICS_TITLE);
        setMenus();
    }

    private void setMenus() {
        Label label;

        label = new Label(TOPICS_TITLE);
        label.setOnMouseClicked(event -> loadContentTopics());
        initMenuLabel(mnTopics, label);

        label = new Label(NEW_TOPIC_TITLE);
        label.setOnMouseClicked(event -> loadContentNewTopic());
        initMenuLabel(mnNewTopic, label);

//        label = new Label(WORDS_TITLE);
//        label.setOnMouseClicked(event -> loadContentWords());
//        initMenuLabel(mnWords, label);
    }

    private void initMenuLabel(Menu menu, Label label) {
        Insets insets = new Insets(5, 8, 5, 8);
        label.setPadding(insets);
        menu.setGraphic(label);
    }

    private void loadContent(String title, Node node) {
        lblTitle.setText(title);
        setVista(node);
    }

    public void loadContentTopics() {
        loadContent(TOPICS_TITLE, new TopicsView());
    }

    public void loadContentNewTopic() {
        loadContent(NEW_TOPIC_TITLE, new NewTopicView());
    }

    public void loadContentEditTopic(Topic topic) {
        loadContent(EDIT_TOPIC_TITLE, new EditTopicView(topic));
    }

//    public void loadContentWords() {
//        loadContent(WORDS_TITLE, new WordsFormView());
//    }

    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }
}

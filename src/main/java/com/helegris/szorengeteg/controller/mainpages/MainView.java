/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.mainpages;

import com.helegris.szorengeteg.controller.forms.NewTopicView;
import com.helegris.szorengeteg.controller.forms.WordsFormView;
import com.helegris.szorengeteg.controller.forms.EditTopicView;
import com.helegris.szorengeteg.controller.practice.PracticeAllStartView;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.settings.SettingsView;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.model.entities.Topic;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
    public static final String PRACTICE_TITLE = Messages.msg("practice");
    public static final String NEW_TOPIC_TITLE = Messages.msg("menu.new_topic");
    public static final String EDIT_TOPIC_TITLE = Messages.msg("menu.edit_topic");
    public static final String WORDS_TITLE = Messages.msg("menu.words");
    public static final String SETTINGS_TITLE = Messages.msg("menu.settings");
    public static final String HELP_TITLE = Messages.msg("menu.help");

    @FXML
    private Menu mnTopics;
    @FXML
    private Menu mnPractice;
    @FXML
    private Menu mnNewTopic;
    @FXML
    private Menu mnWords;
    @FXML
    private Menu mnSettings;
    @FXML
    private Menu mnHelp;
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
        Map<Menu, Label> menuMap = new HashMap<>();
        Insets insets = new Insets(5, 8, 5, 8);

        label = new Label(TOPICS_TITLE);
        label.setOnMouseClicked(event -> loadContentTopics());
        menuMap.put(mnTopics, label);

        label = new Label(PRACTICE_TITLE);
        label.setOnMouseClicked(event -> loadContentPractice());
        menuMap.put(mnPractice, label);

        label = new Label(NEW_TOPIC_TITLE);
        label.setOnMouseClicked(event -> loadContentNewTopic());
        menuMap.put(mnNewTopic, label);

        label = new Label(WORDS_TITLE);
        label.setOnMouseClicked(event -> loadContentWords());
        menuMap.put(mnWords, label);

        label = new Label(SETTINGS_TITLE);
        label.setOnMouseClicked(event -> loadContentSettings());
        menuMap.put(mnSettings, label);

        label = new Label(HELP_TITLE);
        menuMap.put(mnHelp, label);

        Iterator it = menuMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Menu menu = (Menu) pair.getKey();
            label = (Label) pair.getValue();
            label.setPadding(insets);
            menu.setGraphic(label);
            it.remove();
        }
    }

    private void loadContent(String title, Node node) {
        lblTitle.setText(title);
        setVista(node);
    }

    public void loadContentTopics() {
        loadContent(TOPICS_TITLE, new TopicsView());
    }

    public void loadContentPractice() {
        loadContent(PRACTICE_TITLE, new PracticeAllStartView());
    }

    public void loadContentNewTopic() {
        loadContent(NEW_TOPIC_TITLE, new NewTopicView());
    }

    public void loadContentEditTopic(Topic topic) {
        loadContent(EDIT_TOPIC_TITLE, new EditTopicView(topic));
    }

    public void loadContentWords() {
        loadContent(WORDS_TITLE, new WordsFormView());
    }

    public void loadContentSettings() {
        loadContent(SETTINGS_TITLE, new SettingsView());
    }

    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }
}

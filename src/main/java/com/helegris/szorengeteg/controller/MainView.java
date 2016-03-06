/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.model.entity.Topic;
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

    public static final String TOPICS_TITLE = "Témakörök";
    public static final String NEW_TOPIC_TITLE = "Új témakör";
    public static final String EDIT_TOPIC_TITLE = "Témakör szerkesztése";
    public static final String WORDS_TITLE = "Szavak";
    public static final String SETTINGS_TITLE = "Beállítások";
    public static final String STATISTICS_TITLE = "Statisztika";
    public static final String HELP_TITLE = "Segítség";

    @FXML
    private Menu mnTopics;
    @FXML
    private Menu mnNewTopic;
    @FXML
    private Menu mnWords;
    @FXML
    private Menu mnSettings;
    @FXML
    private Menu mnStatistics;
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

        label = new Label(NEW_TOPIC_TITLE);
        label.setOnMouseClicked(event -> loadContentNewTopic());
        menuMap.put(mnNewTopic, label);
        
        label = new Label(WORDS_TITLE);
        label.setOnMouseClicked(event -> loadContentWords());
        menuMap.put(mnWords, label);

        label = new Label(SETTINGS_TITLE);
        menuMap.put(mnSettings, label);

        label = new Label(STATISTICS_TITLE);
        menuMap.put(mnStatistics, label);

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

    public void loadContentTopics() {
        lblTitle.setText(TOPICS_TITLE);
        setVista(new TopicsView());
    }

    public void loadContentNewTopic() {
        lblTitle.setText(NEW_TOPIC_TITLE);
        setVista(new NewTopicView());
    }

    public void loadContentEditTopic(Topic topic) {
        lblTitle.setText(EDIT_TOPIC_TITLE);
        setVista(new EditTopicView(topic));
    }

    public void loadContentWords() {
        lblTitle.setText(WORDS_TITLE);
        setVista(new WordsFormView());
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

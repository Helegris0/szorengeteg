/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.model.entity.Topic;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
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
    public static final String SETTINGS_TITLE = "Beállítások";
    public static final String STATISTICS_TITLE = "Statisztika";
    public static final String HELP_TITLE = "Segítség";

    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu mnTopics;
    @FXML
    private Menu mnNewTopic;
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

        Label lblTopics = new Label(TOPICS_TITLE);
        lblTopics.setOnMouseClicked((MouseEvent event) -> {
            loadContentTopics();
        });
        mnTopics.setGraphic(lblTopics);

        Label lblNewTopic = new Label(NEW_TOPIC_TITLE);
        lblNewTopic.setOnMouseClicked((MouseEvent event) -> {
            loadContentNewTopic();
        });
        mnNewTopic.setGraphic(lblNewTopic);

        mnSettings.setText(SETTINGS_TITLE);
        mnStatistics.setText(STATISTICS_TITLE);
        mnHelp.setText(HELP_TITLE);
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

    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import com.helegris.szorengeteg.controller.MainController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author Timi
 */
public class VistaNavigator {

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String MAIN_FXML = "/fxml/main.fxml";
    public static final String TOPICS_FXML = "/fxml/topics.fxml";
    public static final String NEW_TOPIC_FXML = "/fxml/new_topic.fxml";

    /**
     * The main application layout controller.
     */
    private static MainController mainController;

    public static MainController getMainController() {
        return mainController;
    }

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(MainController mainController) {
        VistaNavigator.mainController = mainController;
    }

    /**
     * Loads the vista specified by the fxml file into the vistaHolder pane of
     * the main application layout.
     *
     * Previously loaded vista for the same fxml file are not cached. The fxml
     * is loaded anew and a new vista node hierarchy generated every time this
     * method is invoked.
     *
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example: cache FXMLLoaders cache
     * loaded vista nodes, so they can be recalled or reused allow a user to
     * specify vista node reuse or new creation allow back and forward history
     * like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadVista(String fxml) {
        try {
            mainController.setVista((Node) FXMLLoader.load(
                    VistaNavigator.class.getResource(fxml)
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 *
 * @author Timi
 */
public class TopicBox extends Pane {

    private static final String FXML = "/fxml/topicbox.fxml";

    public TopicBox() {
        InputStream inputStream = this.getClass().getResourceAsStream(FXML);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(TopicBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

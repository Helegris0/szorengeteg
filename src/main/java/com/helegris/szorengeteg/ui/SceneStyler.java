/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 *
 * @author Timi
 */
public class SceneStyler {

    public enum Style {

        MAIN("/styles/Styles.css"),
        PRACTICE("/styles/practice.css");

        private final String path;

        private Style(String path) {
            this.path = path;
        }
    }

    public Scene createScene(Pane pane, Style style) {
        Scene scene = new Scene(pane);

        scene.getStylesheets().setAll(
                getClass().getResource(style.path).toExternalForm());

        return scene;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 *
 * @author Timi
 */
public class SceneStyler {

    private final String STYLE_PATH = "/styles/Styles.css";

    public Scene createScene(Pane pane) {
        Scene scene = new Scene(pane);

        scene.getStylesheets().setAll(
                getClass().getResource(STYLE_PATH).toExternalForm());

        return scene;
    }
}

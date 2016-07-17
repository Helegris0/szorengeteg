/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 *
 * @author Timi
 */
public class Positioner extends VBox {

    private final Button btnUp;
    private final Button btnDown;

    private static final double BUTTON_HEIGHT = 20;
    public static final String ARROW = "Arrow";
    private final double ARROW_SIZE = 4;

    public Positioner(PositionListener listener) {
        Path arrowUp = new Path();
        arrowUp.setId(ARROW);
        arrowUp.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(ARROW_SIZE, 0),
                new LineTo(0, -ARROW_SIZE), new LineTo(-ARROW_SIZE, 0));
        arrowUp.setMouseTransparent(true);

        Path arrowDown = new Path();
        arrowDown.setId(ARROW);
        arrowDown.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(ARROW_SIZE, 0),
                new LineTo(0, ARROW_SIZE), new LineTo(-ARROW_SIZE, 0));
        arrowDown.setMouseTransparent(true);

        btnUp = new Button();
        btnUp.setMinHeight(BUTTON_HEIGHT);
        btnUp.setMaxHeight(BUTTON_HEIGHT);
        btnUp.setFocusTraversable(false);
        btnUp.setOnAction(event -> listener.up());

        btnDown = new Button();
        btnDown.setMinHeight(BUTTON_HEIGHT);
        btnDown.setMaxHeight(BUTTON_HEIGHT);
        btnDown.setFocusTraversable(false);
        btnDown.setOnAction(event -> listener.down());

        StackPane upPane = new StackPane();
        upPane.getChildren().addAll(btnUp, arrowUp);
        upPane.setAlignment(Pos.CENTER);

        StackPane downPane = new StackPane();
        downPane.getChildren().addAll(btnDown, arrowDown);
        downPane.setAlignment(Pos.CENTER);

        this.getChildren().addAll(upPane, downPane);
    }
}

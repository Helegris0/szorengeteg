/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import javafx.scene.control.Label;

/**
 *
 * @author Timi
 */
public class ClickableLabel extends Label {

    public ClickableLabel() {
        super();
        setMouseEvents();
    }

    public ClickableLabel(String text) {
        super(text);
        setMouseEvents();
    }

    private void setMouseEvents() {
        this.setOnMouseEntered(e -> this.setUnderline(true));
        this.setOnMouseExited(e -> this.setUnderline(false));
    }
}

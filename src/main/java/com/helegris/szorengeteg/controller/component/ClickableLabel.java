/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Timi
 */
public class ClickableLabel extends Label {

    public ClickableLabel() {
        super();
        this.setOnMouseEntered((MouseEvent e) -> {
            this.setUnderline(true);
        });
        this.setOnMouseExited((MouseEvent e) -> {
            this.setUnderline(false);
        });
    }

}

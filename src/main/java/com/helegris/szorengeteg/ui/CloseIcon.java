/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Timi
 */
public class CloseIcon extends ImageView {

    private static final String CLOSE = "/images/close.png";
    private static final String CLOSE_HOVER = "/images/close_hover.png";

    private final Image imgClose = new Image(CLOSE);
    private final Image imgHover = new Image(CLOSE_HOVER);

    public CloseIcon() {
        this.setImage(imgClose);
        this.setOnMouseEntered(this::mouseEntered);
        this.setOnMouseExited(this::mouseExited);
    }

    private void mouseEntered(MouseEvent event) {
        this.setImage(imgHover);
    }

    private void mouseExited(MouseEvent event) {
        this.setImage(imgClose);
    }
}

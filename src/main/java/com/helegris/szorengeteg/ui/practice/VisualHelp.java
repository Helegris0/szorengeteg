/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.ui.DefaultImage;
import com.helegris.szorengeteg.ui.MediaLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Timi
 */
public class VisualHelp extends ImageView {

    private final String SHOWIMAGE_PATH = "/images/showimage.png";
    private final Image startingImage = new Image(
            this.getClass().getResourceAsStream(SHOWIMAGE_PATH));

    private Card card;
    private boolean provideHelp;
    private boolean imageShown;

    public VisualHelp() {
        this.setOnMouseClicked(this::showImage);
    }

    public void provideHelp() {
        this.setImage(startingImage);
        this.provideHelp = true;
    }

    private void showImage(MouseEvent event) {
        if (provideHelp && !imageShown) {
            if (card.getImage() != null) {
                this.setImage(new MediaLoader().loadImage(card.getImage()));
                imageShown = true;
            } else {
                this.setImage(DefaultImage.getInstance());
            }
        }
    }

    public void setCard(Card card) {
        this.card = card;
        this.provideHelp = false;
        this.imageShown = false;
        this.setImage(null);
    }
}

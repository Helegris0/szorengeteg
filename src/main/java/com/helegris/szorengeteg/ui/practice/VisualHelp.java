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

    private final String EMPTY_PATH = "/images/showimage_empty.png";

    private Card card;
    private boolean imageShown;

    public void showImage(MouseEvent event) {
        if (!imageShown) {
            if (card.getImage() != null) {
                this.setImage(new MediaLoader().loadImage(card.getImage()));
            } else {
                this.setImage(DefaultImage.getInstance());
            }
            imageShown = true;
        }
    }

    public void setCard(Card card) {
        this.card = card;
        this.imageShown = false;
        this.setImage(new Image(EMPTY_PATH));
    }

    public boolean isImageShown() {
        return imageShown;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.component.DefaultImage;
import com.helegris.szorengeteg.model.entity.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Timi
 */
public class PracticeView extends AnchorPane {

    public static final String FXML = "fxml/practice.fxml";

    private final String SHOWIMAGE_PATH = "/images/showimage.png";

    @FXML
    private ImageView imageView;
    @FXML
    private Label lblDescription;

    private final PracticeSession session;
    private Card card;
    private boolean imageShown;

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeView(PracticeSession session) {
        this.session = session;
        this.card = session.getCurrentCard();
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    public void initialize() {
        setQuestion();
        Image defImage = new Image(this.getClass().getResourceAsStream(SHOWIMAGE_PATH));
        imageView.setImage(defImage);
        imageView.setOnMouseClicked(this::showImage);
    }

    private void setQuestion() {
        lblDescription.setText(card.getDescription());
    }

    private void showImage(MouseEvent event) {
        if (!imageShown) {
            if (card.getImage() != null) {
                imageView.setImage(new ImageLoader().loadImage(card.getImage()));
                imageShown = true;
            } else {
                imageView.setImage(DefaultImage.getInstance());
            }
        }
    }

    public void nextCard() {
        card = session.nextCard();
        imageShown = false;
        setQuestion();
    }
}

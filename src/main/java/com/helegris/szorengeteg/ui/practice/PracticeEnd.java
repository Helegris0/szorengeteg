/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.ui.DefaultImage;
import com.helegris.szorengeteg.ui.ImageLoader;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Card;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class PracticeEnd extends AnchorPane {

    @FXML
    protected Label lblWord;
    @FXML
    protected TextArea txtDescription;
    @FXML
    protected ImageView imageView;

    protected final Button btnClose = new Button(Messages.msg("practice.close"));

    @FXML
    protected void initialize() {
        setRightAnchor(btnClose, 5.);
        setBottomAnchor(btnClose, 5.);
        this.getChildren().add(btnClose);
        btnClose.setOnAction(this::close);
    }

    protected void selectionChanged(ObservableValue observable,
            Object oldValue, Object newValue) {
        setSelectedCard((Card) newValue);
    }

    protected void setSelectedCard(Card card) {
        lblWord.setText(card.getWord());
        txtDescription.setText(card.getDescription());

        byte[] imageBytes = card.getImage();
        if (imageBytes != null) {
            imageView.setImage(new ImageLoader().loadImage(imageBytes));
        } else {
            imageView.setImage(DefaultImage.getInstance());
        }
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }
}

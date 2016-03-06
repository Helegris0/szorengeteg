/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.ImageUtils;
import com.helegris.szorengeteg.controller.TopicFormView;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Timi
 */
public class RowForCard {

    private TopicFormView topicView;
    private Card card = new Card();
    private ImageView imageView = new ImageView();
    private TextField txtWord = new TextField();
    private TextField txtDescription = new TextField();
    private Button btnDelete = new Button("töröl");
    private File imageFile;

    private static int imageWidth = 30;
    private static int imageHeight = 30;

    public RowForCard(TopicFormView topicView) {
        this.topicView = topicView;
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setImage(DefaultImage.getInstance());
        btnDelete.setOnAction(this::delete);
    }

    public RowForCard(TopicFormView topicView, Card card) {
        this(topicView);
        this.card = card;
        txtWord.setText(card.getWord());
        txtDescription.setText(card.getDescription());
        loadOriginalImage();
    }

    private void loadOriginalImage() {
        if (card.getImage() != null) {
            imageView.setImage(ImageUtils.loadImage(card.getImage()));
        }
    }

    private void delete(ActionEvent event) {
        topicView.deleteRow(this);
    }

    public boolean dataValidity() {
        return !"".equals(txtWord.getText())
                && !"".equals(txtDescription.getText());
    }

    public Card getUpdatedCard() throws FileNotFoundException, IOException {
        card.setWord(txtWord.getText());
        card.setDescription(txtDescription.getText());
        if (imageFile != null) {
            card.setImage(IOUtils.toByteArray(new FileInputStream(imageFile)));
        }
        return card;
    }

    public Card getUpdatedCard(Topic topic) throws FileNotFoundException, IOException {
        card.setTopic(topic);
        return getUpdatedCard();
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextField getTxtWord() {
        return txtWord;
    }

    public void setTxtWord(TextField txtWord) {
        this.txtWord = txtWord;
    }

    public TextField getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(TextField txtDescription) {
        this.txtDescription = txtDescription;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public static int getImageWidth() {
        return imageWidth;
    }

    public static void setImageWidth(int imageWidth) {
        RowForCard.imageWidth = imageWidth;
    }

    public static int getImageHeight() {
        return imageHeight;
    }

    public static void setImageHeight(int imageHeight) {
        RowForCard.imageHeight = imageHeight;
    }
}

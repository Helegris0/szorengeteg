/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.ui.ImageLoader;
import com.helegris.szorengeteg.ui.ImageNotFoundException;
import com.helegris.szorengeteg.ui.DefaultImage;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.Topic;
import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Timi
 */
public class RowForCard {

    private RowDeleteListener deleteListener;
    private Card card = new Card();
    private RowPositioner positioner;
    private ImageView imageView = new ImageView();
    private TextField txtWord = new TextField();
    private TextField txtDescription = new TextField();
    private ComboBox cmbTopic = new ComboBox();
    private Button btnDelete = new Button(Messages.msg("form.delete_row"));
    private File imageFile;
    private final ImageLoader imageLoader = new ImageLoader();

    private static int imageWidth = 40;
    private static int imageHeight = 35;
    private static ObservableList<Topic> allTopics
            = FXCollections.observableArrayList();

    public RowForCard(RowDeleteListener deleteListener, RowMoveListener moveListener) {
        this.deleteListener = deleteListener;

        positioner = new RowPositioner(new RowPositionListener() {

            @Override
            public void up() {
                moveListener.moveUp(RowForCard.this);
            }

            @Override
            public void down() {
                moveListener.moveDown(RowForCard.this);
            }
        });

        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setImage(DefaultImage.getInstance());
        cmbTopic.setItems(allTopics);
        btnDelete.setOnAction(this::delete);
    }

    public RowForCard(RowDeleteListener deleteListener, RowMoveListener moveListener, Card card) {
        this(deleteListener, moveListener);
        this.card = card;
        txtWord.setText(card.getWord());
        txtDescription.setText(card.getDescription());
        cmbTopic.setValue(card.getTopic());
        loadOriginalImage();
    }

    private void loadOriginalImage() {
        if (card.getImage() != null) {
            imageView.setImage(imageLoader.loadImage(card.getImage()));
        }
    }

    private void delete(ActionEvent event) {
        deleteListener.deleteRow(this);
    }

    public boolean dataValidity() {
        return !"".equals(txtWord.getText())
                && !"".equals(txtDescription.getText());
    }

    public boolean missingData() {
        boolean cond1 = "".equals(txtWord.getText());
        boolean cond2 = "".equals(txtDescription.getText());
        return (cond1 && !cond2) || (!cond1 && cond2);
    }

    public Card getUpdatedCard() {
        card.setWord(txtWord.getText());
        card.setDescription(txtDescription.getText());
        Topic originalTopic = card.getTopic();
        Topic newTopic = (Topic) cmbTopic.getValue();
        if (newTopic != null && !newTopic.equals(originalTopic)) {
            card.setTopic(newTopic);
        }
        if (imageFile != null) {
            card.setImage(getImage());
        }
        if (imageView.getImage() instanceof DefaultImage) {
            card.setImage(null);
        }
        return card;
    }

    private byte[] getImage() throws ImageNotFoundException {
        return imageLoader.loadImage(imageFile);
    }

    public Card getUpdatedCard(Topic topic) {
        cmbTopic.setValue(topic);
        return getUpdatedCard();
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    public static void refreshAllTopics(List<Topic> topics) {
        allTopics.clear();
        topics.stream().forEach(allTopics::add);
    }

    public void setOrdinal(Integer ordinal) {
        card.setOrdinal(ordinal);
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public RowPositioner getPositioner() {
        return positioner;
    }

    public void setPositioner(RowPositioner positioner) {
        this.positioner = positioner;
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

    public ComboBox getCmbTopic() {
        return cmbTopic;
    }

    public void setCmbTopic(ComboBox cmbTopic) {
        this.cmbTopic = cmbTopic;
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

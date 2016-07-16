/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.ui.PositionListener;
import com.helegris.szorengeteg.ui.Positioner;
import com.helegris.szorengeteg.ui.AudioIcon;
import com.helegris.szorengeteg.ui.MediaLoader;
import com.helegris.szorengeteg.ui.NotFoundException;
import com.helegris.szorengeteg.ui.DefaultImage;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.Topic;
import java.io.File;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;

/**
 *
 * @author Timi
 */
public class RowForCard {

    private RowDeleteListener deleteListener;
    private Card card = new Card();
    private CheckBox checkBox;
    private Positioner positioner;
    private ImageView imageView = new ImageView();
    private AudioIcon audioIcon = new AudioIcon();
    private TextField txtWord = new TextField();
    private TextField txtDescription = new TextField();
    private ComboBox cmbTopic = new ComboBox();
    private Button btnDelete = new Button(Messages.msg("form.delete_row"));
    private File imageFile;
    private File audioFile;
    private final MediaLoader mediaLoader = new MediaLoader();

    private static int imageWidth = 40;
    private static int imageHeight = 35;
    private static ObservableList<Topic> allTopics
            = FXCollections.observableArrayList();

    public RowForCard(RowDeleteListener deleteListener, RowMoveListener moveListener) {
        this.deleteListener = deleteListener;

        positioner = new Positioner(new PositionListener() {

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
        audioIcon.setFitWidth(imageWidth);
        audioIcon.setFitHeight(imageWidth);
        cmbTopic.setItems(allTopics);
        btnDelete.setOnAction(this::delete);
    }

    public RowForCard(RowDeleteListener deleteListener, RowMoveListener moveListener, Card card) {
        this(deleteListener, moveListener);
        this.card = card;
        audioIcon.setCard(card);
        txtWord.setText(card.getWord());
        txtDescription.setText(card.getDescription());
        cmbTopic.setValue(card.getTopic());
        loadOriginalImage();
    }

    private void loadOriginalImage() {
        if (card.getImage() != null) {
            imageView.setImage(mediaLoader.loadImage(card.getImage()));
        }
    }

    private void delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sor törlése");
        alert.setHeaderText("Biztosan törölni szeretné ezt a sort?");

        ButtonType typeYes = new ButtonType("Igen", ButtonBar.ButtonData.YES);
        ButtonType typeCancel = new ButtonType("Mégse", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(typeYes);
        alert.getButtonTypes().add(typeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == typeYes) {
            deleteListener.deleteRow(this);
        }
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
        if (audioFile != null) {
            card.setAudio(getAudio());
        }
        if (audioIcon.getAudio() == null) {
            card.setAudio(null);
        }
        return card;
    }

    public Card getUpdatedCard(Topic topic) {
        cmbTopic.setValue(topic);
        return getUpdatedCard();
    }

    private byte[] getImage() throws NotFoundException {
        return mediaLoader.loadBytes(imageFile);
    }

    private byte[] getAudio() throws NotFoundException {
        return mediaLoader.loadBytes(audioFile);
    }

    public void setAudio(Media audio) {
        audioIcon.setAudio(audio);
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    public static void refreshAllTopics(List<Topic> topics) {
        allTopics.clear();
        topics.stream().forEach(allTopics::add);
    }

    public String getWord() {
        return txtWord.getText();
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

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public Positioner getPositioner() {
        return positioner;
    }

    public void setPositioner(Positioner positioner) {
        this.positioner = positioner;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public AudioIcon getAudioIcon() {
        return audioIcon;
    }

    public void setAudioIcon(AudioIcon audioIcon) {
        this.audioIcon = audioIcon;
        audioIcon.setFitWidth(imageWidth);
        audioIcon.setFitHeight(imageWidth);
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

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
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

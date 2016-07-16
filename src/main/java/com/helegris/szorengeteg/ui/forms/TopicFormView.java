/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.ui.NotFoundException;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.ui.DefaultImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Timi
 */
public abstract class TopicFormView extends CardsEditorForm {

    public static final String FXML = "fxml/topic_form.fxml";

    @FXML
    protected TextField txtName;
    @FXML
    protected Button btnDefaultStates;
    @FXML
    protected Button btnDeleteWords;
    @FXML
    protected Button btnDeleteTopic;
    @FXML
    protected ImageView imageView;
    @FXML
    protected Button btnLoadImage;
    @FXML
    protected Button btnDeleteImage;

    protected Topic topic;
    protected File imageFile;

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicFormView() {
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        btnDefaultStates.setVisible(false);
        btnDeleteTopic.setVisible(false);
        btnLoadImage.setOnAction(this::loadImage);
        btnDeleteImage.setOnAction(this::deleteImage);
        btnDeleteWords.setOnAction(this::deleteWords);
        btnSave.setOnAction(this::submitTopic);
    }

    protected void loadImage(ActionEvent event) {
        imageFile = FileChooserHelper.getImageFile(getScene().getWindow());
        if (imageFile != null) {
            try {
                Image image = new Image(new FileInputStream(imageFile));
                imageView.setImage(image);
                btnDeleteImage.setVisible(true);
            } catch (FileNotFoundException ex) {
                alertFileNotFound(ex, imageFile);
            }
        }
    }

    protected void deleteImage(ActionEvent event) {
        btnDeleteImage.setVisible(false);
        imageFile = null;
        imageView.setImage(DefaultImage.getInstance());
    }

    private void deleteWords(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("A témakör szavainak törlése");
        alert.setHeaderText("Biztosan törölni szeretné a témakör szavait?");
        alert.setContentText("A változás mentéssel véglegesíthető.");

        ButtonType typeYes = new ButtonType("Igen", ButtonBar.ButtonData.YES);
        ButtonType typeCancel = new ButtonType("Mégse", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(typeYes);
        alert.getButtonTypes().add(typeCancel);

        alert.setX(getScene().getWidth() / 2);
        alert.setY(100);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == typeYes) {
            rows.clear();
        }
    }

    protected void submitTopic(ActionEvent event) {
        if ("".equals(txtName.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Messages.msg("alert.missing_data"));
            alert.setHeaderText(Messages.msg("alert.define_name_for_topic"));
            alert.initModality(Modality.APPLICATION_MODAL);

            alert.showAndWait();
            return;
        }
        try {
            prepareTopic();
            setOrdinals();
            getTransactionDone();
            VistaNavigator.getMainView().loadContentTopics();
        } catch (FileNotFoundException ex) {
            alertFileNotFound(ex, imageFile);
        } catch (IOException ex) {
            new ExceptionAlert(ex).showAndWait();
        } catch (MissingDataException ex) {
            alertMissingData();
        } catch (NotFoundException ex) {
            alertFileNotFound(ex, ex.getFile());
        }
    }

    protected void prepareTopic() throws FileNotFoundException, IOException {
        topic.setName(txtName.getText());
        if (imageFile != null) {
            topic.setImage(IOUtils.toByteArray(new FileInputStream(imageFile)));
        }
    }

    private void setOrdinals() {
        rows.stream().forEach(row -> row.setOrdinal(rows.indexOf(row)));
    }

    @Override
    protected void getTransactionDone() {
        if (rows.stream().anyMatch(RowForCard::missingData)) {
            throw new MissingDataException();
        }

        List<Card> cards = rows.stream()
                .filter(RowForCard::dataValidity)
                .map(row -> row.getUpdatedCard(topic))
                .collect(Collectors.toList());
        entitySaver.saveTopic(topic, cards);
    }
}

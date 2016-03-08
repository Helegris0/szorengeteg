/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
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
        btnDeleteTopic.setVisible(false);
        btnLoadImage.setOnAction(this::loadImage);
        btnDeleteImage.setOnAction(this::deleteImage);
        btnSave.setOnAction(this::submitTopic);
    }

    @FXML
    protected void loadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        FileChooser.ExtensionFilter extFilterJpg
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.jpg_files"), "*.JPG");
        FileChooser.ExtensionFilter extFilterPng
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.png_files"), "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJpg, extFilterPng);

        imageFile = fileChooser.showOpenDialog(null);

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

    @FXML
    protected void deleteImage(ActionEvent event) {
        btnDeleteImage.setVisible(false);
        imageFile = null;
        imageView.setImage(null);
    }

    @FXML
    protected void submitTopic(ActionEvent event) {
        if ("".equals(txtName.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiányos adat");
            alert.setHeaderText("Kérem, adja meg a témakör nevét");
            alert.initModality(Modality.APPLICATION_MODAL);

            alert.showAndWait();
            return;
        }
        try {
            prepareTopic();
            prepareCards();
            prepareToModify(topic);
            getTransactionDone();
            VistaNavigator.getMainView().loadContentTopics();
        } catch (FileNotFoundException ex) {
            alertFileNotFound(ex, imageFile);
        } catch (IOException ex) {
            alertIOEx(ex);
        }
    }

    protected void prepareTopic() throws IOException {
        topic.setName(txtName.getText());
        if (imageFile != null) {
            topic.setImage(IOUtils.toByteArray(new FileInputStream(imageFile)));
        }
    }

    protected void prepareCards() {
        rowsOfCardsToCreate.stream().forEach((row) -> {
            if (row.dataValidity()) {
                try {
                    Card card = row.getUpdatedCard(topic);
                    topic.addCard(card);
                    prepareToCreate(card);
                } catch (FileNotFoundException ex) {
                    alertFileNotFound(ex, row.getImageFile());
                } catch (IOException ex) {
                    alertIOEx(ex);
                }
            }
        });
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.component.DefaultImage;
import com.helegris.szorengeteg.controller.component.FileChooserHelper;
import com.helegris.szorengeteg.messages.Messages;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class ImagePopup extends AnchorPane {

    public static final String FXML = "fxml/image_popup.fxml";

    @FXML
    protected ImageView imageView;
    @FXML
    protected Button btnLoadImage;
    @FXML
    protected Button btnDeleteImage;
    @FXML
    protected Button btnOk;
    @FXML
    protected Button btnCancel;

    private Image finalImage;
    private File imageFile;
    private boolean ok;

    @SuppressWarnings("LeakingThisInConstructor")
    public ImagePopup(Image originalImage) {
        FXMLLoaderHelper.load(FXML, this);
        if (!(originalImage instanceof DefaultImage)) {
            imageView.setImage(originalImage);
            btnDeleteImage.setVisible(true);
        }
    }

    @FXML
    public void initialize() {
        btnDeleteImage.setVisible(false);
        btnLoadImage.setOnAction(this::popupLoadImage);
        btnDeleteImage.setOnAction(this::popupDeleteImage);
        btnOk.setOnAction(this::popupOk);
    }

    @FXML
    protected void popupLoadImage(ActionEvent event) {
        imageFile = FileChooserHelper.getImageFile();

        if (imageFile != null) {
            try {
                Image image = new Image(new FileInputStream(imageFile));
                imageView.setImage(image);
                btnDeleteImage.setVisible(true);
            } catch (FileNotFoundException ex) {
                alertFileNotFound(imageFile);
            }
        }
    }

    private void alertFileNotFound(File imageFile) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Messages.msg("alert.image_upload_unsuccessful"));
        alert.setHeaderText(Messages.msg("alert.image_not_available"));
        alert.setContentText(Messages.msg("alert.image_file")
                + imageFile.getAbsolutePath());
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    @FXML
    protected void popupDeleteImage(ActionEvent event) {
        btnDeleteImage.setVisible(false);
        imageView.setImage(null);
    }

    @FXML
    protected void popupOk(ActionEvent event) {
        finalImage = imageView.getImage();
        ok = true;
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }

    public Image getFinalImage() {
        return finalImage;
    }

    public File getImageFile() {
        return imageFile;
    }

    public boolean isOk() {
        return ok;
    }
}

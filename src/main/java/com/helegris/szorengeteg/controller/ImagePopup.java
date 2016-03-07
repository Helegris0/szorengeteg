/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.component.DefaultImage;
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
import javafx.stage.FileChooser;
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        FileChooser.ExtensionFilter extFilterJpg
                = new FileChooser.ExtensionFilter("JPG fájlok (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPng
                = new FileChooser.ExtensionFilter("PNG fájlok (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJpg, extFilterPng);

        imageFile = fileChooser.showOpenDialog(null);

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
        alert.setTitle("A képfeltöltés sikertelen");
        alert.setHeaderText("A megadott kép nem elérhető");
        alert.setContentText("Keresett képfájl: " + imageFile.getAbsolutePath());
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

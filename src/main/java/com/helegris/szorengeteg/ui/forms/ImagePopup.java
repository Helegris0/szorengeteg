/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.DefaultImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Timi
 */
public class ImagePopup extends FilePopup {

    public static final String FXML = "fxml/image_popup.fxml";

    @FXML
    private ImageView imageView;

    private Image finalImage;

    @SuppressWarnings("LeakingThisInConstructor")
    public ImagePopup(Image originalImage) {
        FXMLLoaderHelper.load(FXML, this);
        if (!(originalImage instanceof DefaultImage)) {
            imageView.setImage(originalImage);
            btnDelete.setVisible(true);
        }
    }

    @Override
    protected void load(ActionEvent event) {
        file = FileChooserHelper.getImageFile(getScene().getWindow());

        if (file != null) {
            try {
                Image image = new Image(new FileInputStream(file));
                imageView.setImage(image);
                btnDelete.setVisible(true);
                btnOk.requestFocus();
            } catch (FileNotFoundException ex) {
                alertFileNotFound(file);
            }
        }
    }

    @Override
    protected void delete(ActionEvent event) {
        super.delete(event);
        imageView.setImage(null);
    }

    @Override
    protected void ok(ActionEvent event) {
        finalImage = imageView.getImage();
        super.ok(event);
    }

    public Image getFinalImage() {
        return finalImage;
    }
}

package com.helegris.szorengeteg.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.helegris.szorengeteg.CDIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.model.EntitySaver;
import com.helegris.szorengeteg.model.entity.Topic;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;

/**
 * FXML Controller class
 *
 * @author Timi
 */
public class NewTopicView extends AnchorPane {
    
    public static final String FXML = "fxml/new_topic.fxml";

    @Inject
    private EntitySaver entitySaver;

    @FXML
    private TextField txtName;
    @FXML
    private ImageView imageView;
    @FXML
    private Button btnLoadImage;
    @FXML
    private Button btnDeleteImage;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBack;

    private byte[] imageBytes;

    @SuppressWarnings("LeakingThisInConstructor")
    public NewTopicView() {
        CDIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    protected void initialize() {
        btnLoadImage.setOnAction(this::loadImage);
        btnDeleteImage.setOnAction(this::deleteImage);
        btnSave.setOnAction(this::saveTopic);
        btnBack.setOnAction(this::goBack);
    }

    @FXML
    protected void loadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJpg
                = new FileChooser.ExtensionFilter("JPG fájlok (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPng
                = new FileChooser.ExtensionFilter("PNG fájlok (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJpg, extFilterPng);

        File file = fileChooser.showOpenDialog(null);

        try {
            if (file != null) {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView.setImage(image);
                imageBytes = getImageBytes(file);
                btnDeleteImage.setVisible(true);
            }
        } catch (IOException ex) {
            Logger.getLogger(NewTopicView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    protected void deleteImage(ActionEvent event) {
        btnDeleteImage.setVisible(false);
        imageBytes = null;
        imageView.setImage(null);
    }

    @FXML
    protected void saveTopic(ActionEvent event) {
        if (!"".equals(txtName.getText())) {
            Topic topic = new Topic(txtName.getText());
            if (imageBytes != null) {
                topic.setImage(imageBytes);
            }
            entitySaver.create(topic);
            VistaNavigator.getMainView().loadContentTopics();
        }
    }

    @FXML
    protected void goBack(ActionEvent event) {
        VistaNavigator.getMainView().loadContentTopics();
    }

    private byte[] getImageBytes(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        return IOUtils.toByteArray(inputStream);
    }

}

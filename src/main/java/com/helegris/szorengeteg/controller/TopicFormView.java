/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.CDIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.model.EntitySaver;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.Topic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Timi
 */
public abstract class TopicFormView extends AnchorPane {

    public static final String FXML = "fxml/topic_form.fxml";

    @Inject
    protected EntitySaver entitySaver;

    @FXML
    protected TextField txtName;
    @FXML
    protected ImageView imageView;
    @FXML
    protected Button btnLoadImage;
    @FXML
    protected Button btnDeleteImage;
    @FXML
    protected TableView<RowForCard> tableView;
    @FXML
    protected Button btnNewWord;
    @FXML
    protected Button btnSave;
    @FXML
    protected Button btnBack;

    protected Topic topic;
    protected File imageFile;
    protected ObservableList<RowForCard> rows = FXCollections.observableArrayList();
    protected List<RowForCard> rowsOfCardsToCreate = new ArrayList<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicFormView() {
        CDIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    protected void initialize() {
        btnLoadImage.setOnAction(this::loadImage);
        btnDeleteImage.setOnAction(this::deleteImage);
        btnNewWord.setOnAction(this::addRow);
        btnSave.setOnAction(this::submitTopic);
        btnBack.setOnAction(this::goBack);
        tableView.setItems(rows);
    }

    @FXML
    protected void loadImage(ActionEvent event) {
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
                alertFileNotFound(ex);
            }
        }
    }

    @FXML
    protected void addRow(ActionEvent event) {
        RowForCard row = new RowForCard(this);
        rows.add(row);
        rowsOfCardsToCreate.add(row);
    }

    public void deleteRow(RowForCard row) {
        if (rowsOfCardsToCreate.contains(row)) {
            rowsOfCardsToCreate.remove(row);
        }
        rows.remove(row);
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
            prepareAndSaveCards();
            entitySaver.modify(topic);
            VistaNavigator.getMainView().loadContentTopics();
        } catch (FileNotFoundException ex) {
            alertFileNotFound(ex);
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

    protected void prepareAndSaveCards() {
        rowsOfCardsToCreate.stream().forEach((row) -> {
            if (row.dataValidity()) {
                Card card = row.getUpdatedCard(topic);
                topic.addCard(card);
                entitySaver.create(card);
            }
        });
    }

    private void alertFileNotFound(FileNotFoundException ex) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("A képfeltöltés sikertelen");
        alert.setHeaderText("A megadott kép nem elérhető");
        alert.setContentText("Keresett képfájl: " + imageFile.getAbsolutePath());
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    private void alertIOEx(IOException ioe) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Váratlan hiba");
        alert.setHeaderText(ioe.getMessage());
        alert.initModality(Modality.APPLICATION_MODAL);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ioe.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Exception stracktrace:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait();
    }

    @FXML
    protected void goBack(ActionEvent event) {
        VistaNavigator.getMainView().loadContentTopics();
    }
}

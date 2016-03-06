/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.CDIUtils;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.DefaultImage;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.model.EntitySaver;
import com.helegris.szorengeteg.model.TopicLoader;
import java.io.File;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public abstract class CardsEditorForm extends AnchorPane {

    @Inject
    protected EntitySaver entitySaver;
    @Inject
    protected TopicLoader topicLoader;

    @FXML
    protected Button btnAddWordsFromFile;
    @FXML
    protected TableView<RowForCard> tableView;
    @FXML
    protected TableColumn colImage;
    @FXML
    protected Button btnNewWord;
    @FXML
    protected Button btnSave;
    @FXML
    protected Button btnBack;

    protected ObservableList<RowForCard> rows = FXCollections.observableArrayList();
    protected List<RowForCard> rowsOfCardsToCreate = new ArrayList<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public CardsEditorForm() {
        CDIUtils.injectFields(this);
    }

    @FXML
    protected void initialize() {
        RowForCard.refreshAlltopics(topicLoader.loadAll());
        btnAddWordsFromFile.setOnAction(this::addWordsFromFile);
        tableView.setItems(rows);
        tableView.setOnMouseClicked(this::cardImageAction);
        btnNewWord.setOnAction(this::addRow);
        btnBack.setOnAction(this::goBack);
    }

    protected void addWordsFromFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        FileChooser.ExtensionFilter extFilterTxt
                = new FileChooser.ExtensionFilter("szövegfájlok (*.txt)", "*.TXT");
        fileChooser.getExtensionFilters().add(extFilterTxt);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            FileInput fileInput = new FileInput(this, file);
            try {
                fileInput.getRows().stream().forEach(row -> {
                    rows.add(row);
                    rowsOfCardsToCreate.add(row);
                });
            } catch (FileNotFoundException ex) {
                alertFileNotFound(ex, file);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Hiba");
                alert.setHeaderText("Hibás fájl");
                alert.setContentText("Elvárt szövegszerkezet:\nszó1;leírás1\nszó2;leírás2");
                alert.showAndWait();
            }
        }
    }

    protected void cardImageAction(MouseEvent event) {
        if (!rows.isEmpty() && !tableView.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position = tableView.getSelectionModel().getSelectedCells().get(0);
            if (colImage.equals(position.getTableColumn())) {
                File cardImageFile = null;
                try {
                    int index = position.getRow();
                    RowForCard row = rows.get(index);
                    ImagePopup.setRow(row);
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource(ImagePopup.FXML));
                    stage.setScene(new Scene(root));
                    stage.setTitle(row.getTxtWord().getText() + " szó képének beállítása");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(tableView.getScene().getWindow());
                    tableView.getSelectionModel().clearSelection();

                    stage.showAndWait();
                    if (ImagePopup.isOk()) {
                        Image rowImage = ImagePopup.getFinalImage();
                        cardImageFile = ImagePopup.getImageFile();
                        if (rowImage != null && cardImageFile != null) {
                            row.setImageFile(cardImageFile);
                            row.setImage(rowImage);
                        } else {
                            row.setImage(DefaultImage.getInstance());
                        }
                    }
                } catch (FileNotFoundException ex) {
                    alertFileNotFound(ex, cardImageFile);
                } catch (IOException ex) {
                    alertIOEx(ex);
                }
            }
        }
    }

    public void deleteRow(RowForCard row) {
        if (rowsOfCardsToCreate.contains(row)) {
            rowsOfCardsToCreate.remove(row);
        }
        rows.remove(row);
    }

    protected void addRow(ActionEvent event) {
        RowForCard row = new RowForCard(this);
        rows.add(row);
        rowsOfCardsToCreate.add(row);
    }

    protected void alertFileNotFound(FileNotFoundException ex, File file) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("A fájlfeltöltés sikertelen");
        alert.setHeaderText("A megadott fájl nem elérhető");
        alert.setContentText("Keresett fájl: " + file.getAbsolutePath());
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    protected void alertIOEx(IOException ioe) {
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

    protected void goBack(ActionEvent event) {
        VistaNavigator.getMainView().loadContentTopics();
    }

}

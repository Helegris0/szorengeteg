/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.CDIUtils;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.DefaultImage;
import com.helegris.szorengeteg.controller.component.FileChooserHelper;
import com.helegris.szorengeteg.controller.component.RowForCard;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.model.EntitySaver;
import com.helegris.szorengeteg.model.TopicLoader;
import com.helegris.szorengeteg.model.entity.PersistentObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    protected ObservableList<RowForCard> rows
            = FXCollections.observableArrayList();
    protected List<RowForCard> rowsOfCardsToCreate = new ArrayList<>();

    private final Set<PersistentObject> entitiesToCreate = new HashSet<>();
    private final Set<PersistentObject> entitiesToModify = new HashSet<>();
    private final Set<PersistentObject> entitiesToDelete = new HashSet<>();

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
        File file = FileChooserHelper.getTxtFile();
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
                alert.setTitle(Messages.msg("alert.error"));
                alert.setHeaderText(Messages.msg("alert.wrong_file"));
                alert.setContentText(Messages.msg("alert.expected_text_format"));
                alert.showAndWait();
            }
        }
    }

    protected void cardImageAction(MouseEvent event) {
        if (!rows.isEmpty()
                && !tableView.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position
                    = tableView.getSelectionModel().getSelectedCells().get(0);
            if (colImage.equals(position.getTableColumn())) {
                int index = position.getRow();
                RowForCard row = rows.get(index);
                Image currentImage = row.getImageView().getImage();
                ImagePopup imagePopup = new ImagePopup(currentImage);
                Stage stage = new Stage();
                stage.setScene(new Scene(imagePopup));
                stage.setTitle(row.getTxtWord().getText() + " "
                        + Messages.msg("form.set_image_of_word"));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(tableView.getScene().getWindow());
                tableView.getSelectionModel().clearSelection();

                stage.showAndWait();
                if (imagePopup.isOk()) {
                    Image rowImage = imagePopup.getFinalImage();
                    File cardImageFile = imagePopup.getImageFile();
                    if (rowImage != null && cardImageFile != null) {
                        row.setImageFile(cardImageFile);
                        row.setImage(rowImage);
                    } else {
                        row.setImage(DefaultImage.getInstance());
                    }
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
        alert.setTitle(Messages.msg("alert.file_upload_unsuccessful"));
        alert.setHeaderText(Messages.msg("alert.file_not_available"));
        alert.setContentText(Messages.msg("alert.file") + file.getAbsolutePath());
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    protected void alertIOEx(IOException ioe) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Messages.msg("alert.unexpected_error"));
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

    public void prepareToCreate(PersistentObject obj) {
        entitiesToCreate.add(obj);
    }

    public void prepareToModify(PersistentObject obj) {
        entitiesToModify.add(obj);
    }

    public void prepareToDelete(PersistentObject obj) {
        entitiesToDelete.add(obj);
    }

    protected void getTransactionDone() {
        entitySaver.complexTransaction(entitiesToCreate,
                entitiesToModify, entitiesToDelete);
        entitiesToCreate.clear();
        entitiesToModify.clear();
        entitiesToDelete.clear();
    }

    protected void goBack(ActionEvent event) {
        VistaNavigator.getMainView().loadContentTopics();
    }

}

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
import com.helegris.szorengeteg.model.entity.Card;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TextField;
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
    protected TableColumn colWord;
    @FXML
    protected TableColumn colDescription;
    @FXML
    protected Button btnNewWord;
    @FXML
    protected Button btnSave;
    @FXML
    protected Button btnBack;

    protected ObservableList<RowForCard> rows
            = FXCollections.observableArrayList();
    protected SortedList sortedRows = new SortedList(rows);

    @SuppressWarnings("LeakingThisInConstructor")
    public CardsEditorForm() {
        CDIUtils.injectFields(this);
    }

    protected abstract void getTransactionDone();

    @FXML
    protected void initialize() {
        setTable();
        setEvents();
    }

    private void setTable() {
        RowForCard.refreshAllTopics(topicLoader.loadAll());
        tableView.setPlaceholder(new Label(Messages.msg("form.no_words")));
        tableView.setItems(sortedRows);
        sortedRows.comparatorProperty().bind(tableView.comparatorProperty());
        colWord.setComparator(new TextFieldComparator());
        colDescription.setComparator(new TextFieldComparator());
    }

    private void setEvents() {
        btnAddWordsFromFile.setOnAction(this::addWordsFromFile);
        tableView.setOnMouseClicked(this::cardImageAction);
        btnNewWord.setOnAction(this::addRow);
        btnBack.setOnAction(this::goBack);
    }

    protected void addWordsFromFile(ActionEvent event) {
        File file = FileChooserHelper.getCsvFile();
        if (file != null) {
            FileInput fileInput = new FileInput(file);
            try {
                loadRows(fileInput.getCards());
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

    protected void loadRows(List<Card> cards) {
        cards.stream().forEach(card -> {
            RowForCard row = new RowForCard(this::deleteRow, card);
            rows.add(row);
        });
    }

    protected void cardImageAction(MouseEvent event) {
        if (!rows.isEmpty()
                && !tableView.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position
                    = tableView.getSelectionModel().getSelectedCells().get(0);
            if (colImage.equals(position.getTableColumn())) {
                int index = position.getRow();
                RowForCard row = (RowForCard) sortedRows.get(index);
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
        rows.remove(row);
    }

    protected void addRow(ActionEvent event) {
        rows.add(new RowForCard(this::deleteRow));
    }

    protected void alertFileNotFound(Exception ex, File file) {
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

    protected static class MissingDataException extends RuntimeException {
    }

    protected void alertMissingData() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Messages.msg("alert.missing_data"));
        alert.setHeaderText(Messages.msg("alert.missing_word_or_description"));
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    protected void goBack(ActionEvent event) {
        VistaNavigator.getMainView().loadContentTopics();
    }

    private class TextFieldComparator implements Comparator<TextField> {

        @Override
        public int compare(TextField o1, TextField o2) {
            return o1.getText().compareTo(o2.getText());
        }

    }

}

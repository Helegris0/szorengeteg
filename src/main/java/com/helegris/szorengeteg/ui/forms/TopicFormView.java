/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.ui.NotFoundException;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.EntitySaver;
import com.helegris.szorengeteg.business.service.TopicLoader;
import com.helegris.szorengeteg.ui.DefaultImage;
import com.helegris.szorengeteg.ui.SceneStyler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Timi
 */
public abstract class TopicFormView extends AnchorPane {

    private static final String FXML = "fxml/topic_form.fxml";

    @Inject
    protected EntitySaver entitySaver;
    @Inject
    private TopicLoader topicLoader;
    @Inject
    private FileChooserHelper fileChooserHelper;

    @FXML
    private Button btnNewWord1;
    @FXML
    private Button btnNewWord2;
    @FXML
    private ComboBox cmbBulkAdd;
    @FXML
    private Button btnBulkAdd;
    @FXML
    private TableView<RowForCard> tableView;
    @FXML
    private TableColumn colImage;
    @FXML
    private TableColumn colAudio;
    @FXML
    private TableColumn colWord;
    @FXML
    private TableColumn colDescription;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBack;

    @FXML
    protected TextField txtName;
    @FXML
    private Button btnDeleteWords;
    @FXML
    protected ImageView imageView;
    @FXML
    private Button btnLoadImage;
    @FXML
    protected Button btnDeleteImage;

    protected Topic topic;
    protected File imageFile;

    protected ObservableList<RowForCard> rows
            = FXCollections.observableArrayList();
    protected SortedList sortedRows = new SortedList(rows);

    private final RowMoveListener rowMoveListener = new RowMoveListener() {

        @Override
        public void moveUp(RowForCard row) {
            int index = rows.indexOf(row);
            if (index > 0) {
                Collections.swap(rows, index, index - 1);
            }
        }

        @Override
        public void moveDown(RowForCard row) {
            int index = rows.indexOf(row);
            if (index < rows.size() - 1) {
                Collections.swap(rows, index, index + 1);
            }
        }
    };

    private final String WORD = Messages.msg("form.word");
    private final String IMAGE = Messages.msg("form.image");
    private final String AUDIO = Messages.msg("form.audio");

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicFormView() {
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    protected void initialize() {
        setTable();
        setEvents();
        cmbBulkAdd.getItems().addAll(WORD, IMAGE, AUDIO);
        cmbBulkAdd.getSelectionModel().select(0);
        btnLoadImage.setOnAction(this::loadImage);
        btnDeleteImage.setOnAction(this::deleteImage);
        btnDeleteWords.setOnAction(this::deleteWords);
        btnSave.setOnAction(this::submitTopic);
    }

    private void setTable() {
        RowForCard.refreshAllTopics(topicLoader.loadAll());
        tableView.setPlaceholder(new Label(Messages.msg("form.no_words")));
        tableView.setItems(sortedRows);
        sortedRows.comparatorProperty().bind(tableView.comparatorProperty());
        colWord.setComparator(new TextFieldComparator());
        colDescription.setComparator(new TextFieldComparator());
        Platform.runLater(() -> {
            double sum = tableView.getColumns().stream()
                    .mapToDouble(TableColumn::getWidth).sum();
            sum -= colDescription.getWidth();
            double fullWidth = tableView.getWidth();
            if (fullWidth == 0) {
                fullWidth = this.getScene().getWidth();
            }
            colDescription.setPrefWidth(fullWidth - sum - 30);
        });
    }

    private void setEvents() {
        btnBulkAdd.setOnAction(this::bulkAdd);
        tableView.setOnMouseClicked(this::tableClick);
        btnNewWord1.setOnAction(this::addRow);
        btnNewWord2.setOnAction(this::addRow);
        btnBack.setOnAction(this::goBack);
    }

    private void bulkAdd(ActionEvent event) {
        if (cmbBulkAdd.isVisible()) {
            switch (cmbBulkAdd.getSelectionModel().getSelectedIndex()) {
                case 0: {
                    Stage stage = new Stage();
                    BulkAddWordsView view = new BulkAddWordsView();
                    stage.setScene(new SceneStyler().createScene(
                            view, SceneStyler.Style.MAIN));
                    stage.setTitle(Messages.msg("form.bulk_add_something", 
                            Messages.msg("form.word_and_descripiton")));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(getScene().getWindow());
                    stage.showAndWait();
                    loadRows(view.getCards());
                }
                break;
                case 1: {
                    if (sortedRows.size() > 0) {
                        Stage stage = new Stage();
                        BulkAddImagesView view = new BulkAddImagesView(sortedRows);
                        stage.setScene(new SceneStyler().createScene(
                                view, SceneStyler.Style.MAIN));
                        stage.setTitle(Messages.msg("form.bulk_add_something", 
                            Messages.msg("form.image")));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initOwner(getScene().getWindow());
                        stage.showAndWait();

                        if (view.isOk()) {
                            view.getFiles().entrySet().stream().forEach((entry) -> {
                                ((RowForCard) sortedRows.get(entry.getKey())).setImageFile(entry.getValue());
                            });
                            view.getImages().entrySet().stream().forEach((entry) -> {
                                ((RowForCard) sortedRows.get(entry.getKey())).setImage(entry.getValue());
                            });
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(Messages.msg("alert.error"));
                        alert.setHeaderText(Messages.msg("alert.no_words_image"));
                        alert.setContentText(Messages.msg("alert.add_words"));
                        alert.showAndWait();
                    }
                }
                break;
                case 2: {
                    if (sortedRows.size() > 0) {
                        Stage stage = new Stage();
                        BulkAddAudioView view = new BulkAddAudioView(sortedRows);
                        stage.setScene(new SceneStyler().createScene(
                                view, SceneStyler.Style.MAIN));
                        stage.setTitle(Messages.msg("form.bulk_add_something", 
                            Messages.msg("form.audio")));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initOwner(getScene().getWindow());
                        stage.showAndWait();

                        if (view.isOk()) {
                            view.getFiles().entrySet().stream().forEach((entry) -> {
                                ((RowForCard) sortedRows.get(entry.getKey())).setAudioFile(entry.getValue());
                            });
                            view.getAudio().entrySet().stream().forEach((entry) -> {
                                ((RowForCard) sortedRows.get(entry.getKey())).setAudio(entry.getValue());
                            });
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(Messages.msg("alert.error"));
                        alert.setHeaderText(Messages.msg("alert.no_words_audio"));
                        alert.setContentText(Messages.msg("alert.add_words"));
                        alert.showAndWait();
                    }
                }
                break;
            }
        } else {
            btnBulkAdd.setText(Messages.msg("form.bulk_add"));
            cmbBulkAdd.setVisible(true);
            cmbBulkAdd.requestFocus();
        }
    }

    protected void loadRows(List<Card> cards) {
        cards.sort((c1, c2) -> c1.getOrdinal() != null ? c1.getOrdinal() - c2.getOrdinal() : 1);

        cards.stream().forEach(card -> {
            RowForCard row = new RowForCard(this::deleteRow, rowMoveListener, card);
            rows.add(row);
        });
    }

    private void tableClick(MouseEvent event) {
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
                stage.setScene(new SceneStyler().createScene(
                        imagePopup, SceneStyler.Style.MAIN));
                stage.setTitle(Messages.msg("form.set_image_of_word", 
                        row.getTxtWord().getText()));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(tableView.getScene().getWindow());
                stage.setResizable(false);
                tableView.getSelectionModel().clearSelection();

                stage.showAndWait();
                if (imagePopup.isOk()) {
                    Image rowImage = imagePopup.getFinalImage();
                    File cardImageFile = imagePopup.getFile();
                    if (rowImage != null && cardImageFile != null) {
                        row.setImageFile(cardImageFile);
                        row.setImage(rowImage);
                    } else if (rowImage == null) {
                        row.setImage(DefaultImage.getInstance());
                    }
                }
            } else if (colAudio.equals(position.getTableColumn())) {
                int index = position.getRow();
                RowForCard row = (RowForCard) sortedRows.get(index);
                AudioPopup audioPopup
                        = new AudioPopup(row.getAudioIcon().getAudio());
                Stage stage = new Stage();
                stage.setScene(new SceneStyler().createScene(
                        audioPopup, SceneStyler.Style.MAIN));
                stage.setTitle(Messages.msg("form.set_audio_of_word", 
                        row.getTxtWord().getText()));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(tableView.getScene().getWindow());
                stage.setResizable(false);
                tableView.getSelectionModel().clearSelection();

                stage.showAndWait();
                if (audioPopup.isOk()) {
                    File audioFile = audioPopup.getFile();
                    row.setAudioFile(audioFile);
                    row.setAudio(audioPopup.getFinalAudio());
                }
            }
        }
    }

    public void deleteRow(RowForCard row) {
        rows.remove(row);
    }

    private void addRow(ActionEvent event) {
        rows.add(new RowForCard(this::deleteRow, rowMoveListener));
        tableView.scrollTo(rows.get(rows.size() - 1));
    }

    private void alertFileNotFound(Exception ex, File file) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Messages.msg("alert.file_upload_unsuccessful"));
        alert.setHeaderText(Messages.msg("alert.file_not_available"));
        alert.setContentText(Messages.msg("alert.file") + file.getAbsolutePath());
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    private static class MissingDataException extends RuntimeException {
    }

    private void alertMissingData() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Messages.msg("alert.missing_data"));
        alert.setHeaderText(Messages.msg("alert.missing_word_or_description"));
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    private void goBack(ActionEvent event) {
        VistaNavigator.getMainView().loadContentTopics();
    }

    private class TextFieldComparator implements Comparator<TextField> {

        @Override
        public int compare(TextField o1, TextField o2) {
            return o1.getText().compareTo(o2.getText());
        }

    }

    private void loadImage(ActionEvent event) {
        imageFile = fileChooserHelper.getImageFile(getScene().getWindow());
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

    private void deleteImage(ActionEvent event) {
        btnDeleteImage.setVisible(false);
        imageFile = null;
        imageView.setImage(DefaultImage.getInstance());
    }

    private void deleteWords(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Messages.msg("alert.delete_words"));
        alert.setHeaderText(Messages.msg("alert.sure_delete_words"));
        alert.setContentText(Messages.msg("alert.save_to_confirm"));

        ButtonType typeYes = new ButtonType(Messages.msg("common.yes"), ButtonBar.ButtonData.YES);
        ButtonType typeCancel = new ButtonType(Messages.msg("common.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
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

    private void submitTopic(ActionEvent event) {
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

    private void getTransactionDone() {
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

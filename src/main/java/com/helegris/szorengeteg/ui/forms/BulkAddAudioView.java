/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.ui.AudioIcon;
import com.helegris.szorengeteg.ui.SceneStyler;
import com.helegris.szorengeteg.ui.forms.BulkAddAudioView.AudioRow;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class BulkAddAudioView extends BulkAddMediaView<AudioRow> {

    private static final String FXML = "fxml/bulk_add_audio.fxml";
    @FXML
    private TableColumn colAudio;

    private final Map<Integer, Media> audioM = new HashMap<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public BulkAddAudioView(ObservableList<RowForCard> oRows) {
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
        oRows.stream().forEach(oRow -> rows.add(
                new AudioRow(oRows.indexOf(oRow), oRow.getWord(), oRow.getAudioIcon().getAudio())));
        tableView.setItems(rows);
        rows.stream().forEach(row -> row.getCheckBox().setOnAction(event -> checkSelections()));
        checkSelections();
    }

    /**
     * Matches the choosed audio files with the selected rows.
     *
     * @param event
     */
    @Override
    protected void browse(ActionEvent event) {
        List<File> selectedFiles = fileChooserHelper.getAudioFiles(getScene().getWindow());
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            List<AudioRow> selectedRows = rows.stream()
                    .filter(row -> row.isSelected())
                    .collect(Collectors.toList());
            for (int i = 0; i < selectedRows.size() && i < selectedFiles.size(); i++) {
                AudioRow row = selectedRows.get(i);
                File file = selectedFiles.get(i);
                Media audio = new Media(file.toURI().toString());
                row.setAudio(audio);
                row.setFileName(file.getName());
                row.setSelected(false);
                files.put(row.getIndex(), file);
                audioM.put(row.getIndex(), audio);
                tableView.refresh();
            }
        }
    }

    /**
     * When clicking on the "audio" column of a row, it opens a popup with audio
     * options.
     *
     * @param event
     */
    @Override
    protected void tableClick(MouseEvent event) {
        if (!rows.isEmpty() && !tableView.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position = (TablePosition) tableView.getSelectionModel()
                    .getSelectedCells().get(0);
            if (colAudio.equals(position.getTableColumn())) {
                int index = position.getRow();
                AudioRow row = rows.get(index);
                AudioIcon audioIcon = row.getAudioIcon();
                AudioPopup audioPopup = new AudioPopup(audioIcon.getAudio());
                Stage stage = new Stage();
                stage.setScene(new SceneStyler().createScene(audioPopup, SceneStyler.Style.TOPIC_LIST));
                stage.setTitle(Messages.msg("form.set_audio_of_word", row.getWord()));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(tableView.getScene().getWindow());
                stage.setResizable(false);
                tableView.getSelectionModel().clearSelection();

                stage.showAndWait();
                if (audioPopup.isOk()) {
                    Media audio = audioPopup.getFinalAudio();
                    File file = audioPopup.getFile();
                    row.setAudio(audio);
                    audioM.put(index, audio);
                    files.put(index, file);
                    if (file != null) {
                        row.setFileName(file.getName());
                        tableView.refresh();
                    } else {
                        row.setFileName("");
                        tableView.refresh();
                    }
                }
            }
        }
    }

    public Map<Integer, Media> getAudio() {
        return audioM;
    }

    public class AudioRow extends BulkAddMediaView.Row {

        private static final int ICON_SIZE = 40;

        private AudioIcon audioIcon;
        private String fileName;

        public AudioRow(int index, String word, Media audio) {
            super(index, word);
            this.audioIcon = new AudioIcon(audio);
            this.audioIcon.setFitWidth(ICON_SIZE);
            this.audioIcon.setFitHeight(ICON_SIZE);
            getCheckBox().setSelected(audioIcon.getAudio() == null);
        }

        public AudioIcon getAudioIcon() {
            return audioIcon;
        }

        public void setAudioIcon(AudioIcon audioIcon) {
            this.audioIcon = audioIcon;
        }

        public void setAudio(Media audio) {
            audioIcon.setAudio(audio);
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}

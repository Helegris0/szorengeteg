/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.AudioIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class AudioPopup extends FilePopup {

    public static final String FXML = "fxml/audio_popup.fxml";

    @Inject
    private FileChooserHelper fileChooserHelper;

    @FXML
    private StackPane stackPane;
    @FXML
    private Label lblFileName;

    private final AudioIcon audioIcon;
    private final int iconWidth = 250;
    private final int iconHeight = 250;

    private Media finalAudio;

    @SuppressWarnings("LeakingThisInConstructor")
    public AudioPopup(AudioIcon audioIcon) {
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
        this.audioIcon = audioIcon;
        audioIcon.setFitWidth(iconWidth);
        audioIcon.setFitHeight(iconHeight);
        stackPane.getChildren().add(audioIcon);
        btnDelete.setVisible(audioIcon.getAudio() != null);
    }

    @Override
    protected void load(ActionEvent event) {
        file = fileChooserHelper.getAudioFile(getScene().getWindow());

        if (file != null) {
            audioIcon.setAudio(new Media(file.toURI().toString()));
            btnDelete.setVisible(true);
            lblFileName.setText(file.getName());
            btnOk.requestFocus();
        }
    }

    @Override
    protected void delete(ActionEvent event) {
        super.delete(event);
        audioIcon.setAudio(null);
        lblFileName.setText("");
    }

    @Override
    protected void ok(ActionEvent event) {
        finalAudio = audioIcon.getAudio();
        super.ok(event);
    }

    public AudioIcon getAudioIcon() {
        return audioIcon;
    }

    public Media getFinalAudio() {
        return finalAudio;
    }
}

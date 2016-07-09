/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.messages.Messages;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public abstract class FilePopup extends AnchorPane {

    @FXML
    protected Button btnLoad;
    @FXML
    protected Button btnDelete;
    @FXML
    protected Button btnOk;
    @FXML
    protected Button btnCancel;

    protected File file;
    protected boolean ok;

    @FXML
    public void initialize() {
        btnDelete.setVisible(false);
        btnLoad.setOnAction(this::load);
        btnDelete.setOnAction(this::delete);
        btnOk.setOnAction(this::ok);
    }

    protected abstract void load(ActionEvent event);

    protected void delete(ActionEvent event) {
        btnDelete.setVisible(false);
    }

    protected void ok(ActionEvent event) {
        ok = true;
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }

    protected void alertFileNotFound(File file) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Messages.msg("alert.file_upload_unsuccessful"));
        alert.setHeaderText(Messages.msg("alert.file_not_available"));
        alert.setContentText(Messages.msg("alert.file") + file.getAbsolutePath());
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    public File getFile() {
        return file;
    }

    public boolean isOk() {
        return ok;
    }
}

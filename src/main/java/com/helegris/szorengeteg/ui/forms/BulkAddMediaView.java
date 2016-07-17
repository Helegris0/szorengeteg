/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public abstract class BulkAddMediaView extends AnchorPane {

    @Inject
    protected FileChooserHelper fileChooserHelper;

    @FXML
    protected TableView tableView;
    @FXML
    protected Button btnBrowse;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;

    protected final Map<Integer, File> files = new HashMap<>();

    protected boolean ok;

    @FXML
    private void initialize() {
        tableView.setOnMouseClicked(this::tableClick);
        btnBrowse.setOnAction(this::browse);
        btnAdd.setOnAction(event -> {
            ok = true;
            close();
        });
        btnCancel.setOnAction(event -> close());
    }

    protected abstract void browse(ActionEvent event);

    protected abstract void tableClick(MouseEvent event);

    private void close() {
        ((Stage) getScene().getWindow()).close();
    }

    public Map<Integer, File> getFiles() {
        return files;
    }

    public boolean isOk() {
        return ok;
    }

    public abstract class Row {

        private final int index;
        private CheckBox checkBox = new CheckBox("");
        private String word;

        public Row(int index, String word) {
            this.index = index;
            this.word = word;
        }

        public int getIndex() {
            return index;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public boolean isSelected() {
            return checkBox.isSelected();
        }

        public void setSelected(boolean selected) {
            checkBox.setSelected(selected);
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }
}

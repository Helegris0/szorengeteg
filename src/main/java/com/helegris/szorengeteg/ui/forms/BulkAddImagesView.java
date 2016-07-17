/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.DefaultImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class BulkAddImagesView extends AnchorPane {

    private static final String FXML = "fxml/bulk_add_images.fxml";

    @Inject
    private FileChooserHelper fileChooserHelper;

    @FXML
    private TableView tableView;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private final ObservableList<Row> rows = FXCollections.observableArrayList();
    private final Map<Integer, File> imageFiles = new HashMap<>();
    private final Map<Integer, Image> images = new HashMap<>();

    private boolean ok;

    @SuppressWarnings("LeakingThisInConstructor")
    public BulkAddImagesView(ObservableList<RowForCard> oRows) {
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
        oRows.stream().forEach(oRow -> rows.add(
                new Row(oRows.indexOf(oRow), oRow.getWord(), oRow.getImageView().getImage())));
        tableView.setItems(rows);
        rows.stream().forEach(row -> row.getCheckBox().setOnAction(event -> checkSelections()));
        checkSelections();
    }

    @FXML
    private void initialize() {
        btnBrowse.setOnAction(this::browse);
        btnSave.setOnAction(event -> {
            ok = true;
            close();
        });
        btnCancel.setOnAction(event -> close());
    }

    private void checkSelections() {
        boolean selected = false;
        for (Row row : rows) {
            if (row.getCheckBox().isSelected()) {
                selected = true;
                break;
            }
        }
        btnBrowse.setDisable(!selected);
    }

    private void browse(ActionEvent event) {
        List<File> selectedFiles = fileChooserHelper.getImageFiles(getScene().getWindow());
        List<Row> selectedRows = rows.stream()
                .filter(row -> row.isSelected())
                .collect(Collectors.toList());
        for (int i = 0; i < selectedRows.size(); i++) {
            Row row = selectedRows.get(i);
            if (i < selectedFiles.size()) {
                try {
                    File file = selectedFiles.get(i);
                    Image image = new Image(new FileInputStream(file));
                    row.setImage(image);
                    imageFiles.put(row.getIndex(), file);
                    images.put(row.getIndex(), image);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BulkAddImagesView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            row.setSelected(false);
        }
    }

    private void close() {
        ((Stage) getScene().getWindow()).close();
    }

    public Map<Integer, File> getImageFiles() {
        return imageFiles;
    }

    public Map<Integer, Image> getImages() {
        return images;
    }

    public boolean isOk() {
        return ok;
    }

    public class Row {

        private static final int IMAGE_SIZE = 120;

        private final int index;
        private CheckBox checkBox = new CheckBox("");
        private String word;
        private ImageView imageView;

        public Row(int index, String word, Image image) {
            this.index = index;
            this.word = word;
            this.imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setFitHeight(IMAGE_SIZE);
            checkBox.setSelected(image instanceof DefaultImage);
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

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public void setImage(Image image) {
            imageView.setImage(image);
        }
    }
}

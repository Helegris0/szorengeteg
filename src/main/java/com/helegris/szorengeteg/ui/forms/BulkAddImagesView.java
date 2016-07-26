/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.ui.DefaultImage;
import com.helegris.szorengeteg.ui.SceneStyler;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class BulkAddImagesView extends BulkAddMediaView {

    private static final String FXML = "fxml/bulk_add_images.fxml";
    @FXML
    private TableColumn colImage;

    private final ObservableList<Row> rows = FXCollections.observableArrayList();
    private final Map<Integer, Image> images = new HashMap<>();

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

    @Override
    protected void browse(ActionEvent event) {
        List<File> selectedFiles = fileChooserHelper.getImageFiles(getScene().getWindow());
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            List<Row> selectedRows = rows.stream()
                    .filter(row -> row.isSelected())
                    .collect(Collectors.toList());
            for (int i = 0; i < selectedRows.size() && i < selectedFiles.size(); i++) {
                Row row = selectedRows.get(i);
                try {
                    File file = selectedFiles.get(i);
                    Image image = new Image(new FileInputStream(file));
                    row.setImage(image);
                    files.put(row.getIndex(), file);
                    images.put(row.getIndex(), image);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BulkAddImagesView.class.getName()).log(Level.SEVERE, null, ex);
                }
                row.setSelected(false);
            }
        }
    }

    @Override
    protected void tableClick(MouseEvent event) {
        if (!rows.isEmpty() && !tableView.getSelectionModel().getSelectedCells().isEmpty()) {
            TablePosition position = (TablePosition) tableView.getSelectionModel()
                    .getSelectedCells().get(0);
            if (colImage.equals(position.getTableColumn())) {
                int index = position.getRow();
                Row row = rows.get(index);
                Image currentImage = row.getImageView().getImage();
                ImagePopup imagePopup = new ImagePopup(currentImage);
                Stage stage = new Stage();
                stage.setScene(new SceneStyler().createScene(
                        imagePopup, SceneStyler.Style.MAIN));
                stage.setTitle(row.getWord() + " "
                        + Messages.msg("form.set_image_of_word"));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(tableView.getScene().getWindow());
                stage.setResizable(false);
                tableView.getSelectionModel().clearSelection();

                stage.showAndWait();
                if (imagePopup.isOk()) {
                    Image rowImage = imagePopup.getFinalImage();
                    File file = imagePopup.getFile();
                    if (rowImage != null && file != null) {
                        images.put(index, rowImage);
                        files.put(index, file);
                        row.setImage(rowImage);
                    } else if (rowImage == null) {
                        images.put(index, DefaultImage.getInstance());
                        files.remove(index);
                        row.setImage(DefaultImage.getInstance());
                    }
                }
            }
        }
    }

    public Map<Integer, Image> getImages() {
        return images;
    }

    public class Row extends BulkAddMediaView.Row {

        private static final int IMAGE_SIZE = 120;

        private ImageView imageView;

        public Row(int index, String word, Image image) {
            super(index, word);
            this.imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setFitHeight(IMAGE_SIZE);
            getCheckBox().setSelected(image instanceof DefaultImage);
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

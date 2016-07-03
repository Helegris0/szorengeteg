/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.messages.Messages;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class BulkAddView extends AnchorPane {

    private static final String FXML = "fxml/bulk_add.fxml";

    @FXML
    private Label lblDescription;
    @FXML
    private ToggleGroup grpDelimiter;
    @FXML
    private RadioButton rdColon;
    @FXML
    private RadioButton rdSemicolon;
    @FXML
    private TextArea textArea;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;

    private final Map<RadioButton, String> delimiterMap = new HashMap<>();
    private final List<Card> cards = new ArrayList<>();

    private final String description = "Ezen a felületen egyszerre több szót is hozzá lehet adni a témakörhöz."
            + " Egy sorban a SZÓ és a hozzá tartozó LEÍRÁS szerepeljen, köztük az elválasztó karakter."
            + " Az üres sorokat figyelmen kívül hagyjuk.";

    @SuppressWarnings("LeakingThisInConstructor")
    public BulkAddView() {
        FXMLLoaderHelper.load(FXML, this);
        delimiterMap.put(rdColon, ":");
        delimiterMap.put(rdSemicolon, ";");
        Platform.runLater(() -> textArea.requestFocus());
    }

    @FXML
    public void initialize() {
        lblDescription.setText(description);
        btnAdd.setOnAction(this::save);
        btnCancel.setOnAction(e -> close());
    }

    private void save(ActionEvent event) {
        RadioButton selected = (RadioButton) grpDelimiter.getSelectedToggle();
        String delimiter = delimiterMap.get(selected);
        String text = textArea.getText();

        try (Scanner scanner = new Scanner(text)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] data = line.split(delimiter);
                    if (data.length > 2) {
                        for (int i = 2; i < data.length; i++) {
                            data[1] += delimiter + data[i];
                        }
                    }
                    while (" ".equals(data[0].substring(
                            data[0].length() - 1, data[0].length()))) {
                        data[0] = data[0].substring(0, data[0].length() - 1);
                    }
                    while (" ".equals(data[1].substring(0, 1))) {
                        data[1] = data[1].substring(1, data[1].length());
                    }
                    Card card = new Card(data[0], data[1]);
                    cards.add(card);
                }
            }
            close();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Messages.msg("alert.error"));
            alert.setHeaderText("Hibás adat");
            alert.setContentText("A várt forma:\n"
                    + "szó1" + delimiter + "leírás1\nszó2" + delimiter + "leírás2");
            alert.showAndWait();
        }
    }

    private void close() {
        ((Stage) this.getScene().getWindow()).close();
    }

    public List<Card> getCards() {
        return cards;
    }
}

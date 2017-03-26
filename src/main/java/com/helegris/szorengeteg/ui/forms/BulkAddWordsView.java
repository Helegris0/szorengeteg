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
 * A pane where adding several words with descriptions is possible in a CSV-like
 * format.
 *
 * @author Timi
 */
public class BulkAddWordsView extends AnchorPane {

    private static final String FXML = "fxml/bulk_add_words.fxml";

    @FXML
    private Label lblDescription;
    @FXML
    private Label lblExample;
    @FXML
    private Label lblExample1;
    @FXML
    private Label lblExample2;
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

    private final String description = Messages.msg("form.bulk_add_words_description");

    @SuppressWarnings("LeakingThisInConstructor")
    public BulkAddWordsView() {
        FXMLLoaderHelper.load(FXML, this);
        delimiterMap.put(rdColon, ":");
        delimiterMap.put(rdSemicolon, ";");
        setExample();
        Platform.runLater(() -> textArea.requestFocus());
    }

    @FXML
    public void initialize() {
        lblDescription.setText(description);
        rdColon.setOnAction(e -> setExample());
        rdSemicolon.setOnAction(e -> setExample());
        btnAdd.setOnAction(this::add);
        btnCancel.setOnAction(e -> close());
    }

    private String getDelimiter() {
        RadioButton selected = (RadioButton) grpDelimiter.getSelectedToggle();
        return delimiterMap.get(selected);
    }

    private void setExample() {
        String delimiter = getDelimiter();
        lblExample1.setText(Messages.msg("form.bulk_add_words_example1", delimiter));
        lblExample2.setText(Messages.msg("form.bulk_add_words_example2", delimiter));
    }

    private void add(ActionEvent event) {
        String delimiter = getDelimiter();
        String text = textArea.getText();
        cards.clear();

        try (Scanner scanner = new Scanner(text)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    String[] data = line.split(delimiter);
                    if (data.length > 2) {
                        for (int i = 2; i < data.length; i++) {
                            data[1] += delimiter + data[i];
                        }
                    }
                    Card card = new Card(data[0].trim(), data[1].trim());
                    cards.add(card);
                }
            }
            close();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Messages.msg("alert.error"));
            alert.setHeaderText(Messages.msg("alert.wrong_data"));
            alert.setContentText(Messages.msg("alert.expected_format")
                    + "\n" + Messages.msg("form.word") + "1" + delimiter
                    + Messages.msg("form.description") + "1\n"
                    + Messages.msg("form.word") + "2" + delimiter
                    + Messages.msg("form.description") + "2");
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

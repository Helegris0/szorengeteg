/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.practice;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.ImageLoader;
import com.helegris.szorengeteg.controller.DefaultImage;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.model.entities.Card;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class PracticeEndView extends AnchorPane {

    public static final String FXML = "fxml/practice_end.fxml";

    @FXML
    private ListView lstCorrect;
    @FXML
    private ListView lstIncorrect;
    @FXML
    private Label lblWord;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ImageView imageView;

    private final Button btnClose = new Button(Messages.msg("practice.close"));

    private final ObservableList<Card> correctAnswers;
    private final ObservableList<Card> incorrectAnswers;

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeEndView(List<Card> correctAnswers, List<Card> incorrectAnswers) {
        this.correctAnswers
                = FXCollections.observableArrayList(correctAnswers);
        this.incorrectAnswers
                = FXCollections.observableArrayList(incorrectAnswers);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    private void initialize() {
        lstCorrect.setItems(correctAnswers);
        lstIncorrect.setItems(incorrectAnswers);
        lstCorrect.getSelectionModel().selectedItemProperty().addListener(
                this::selectionChanged);
        lstIncorrect.getSelectionModel().selectedItemProperty().addListener(
                this::selectionChanged);
        lstCorrect.focusedProperty().addListener(this::focusChangedCorrect);
        lstIncorrect.focusedProperty().addListener(this::focusChangedIncorrect);

        setRightAnchor(btnClose, 5.);
        setBottomAnchor(btnClose, 5.);
        this.getChildren().add(btnClose);
        btnClose.setOnAction(this::close);
    }

    private void selectionChanged(ObservableValue observable,
            Object oldValue, Object newValue) {
        setSelectedCard((Card) newValue);
    }

    private void focusChangedCorrect(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
        if (newValue) {
            focusGained(lstCorrect);
        }
    }

    private void focusChangedIncorrect(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
        if (newValue) {
            focusGained(lstIncorrect);
        }
    }

    private void focusGained(ListView listView) {
        MultipleSelectionModel model = listView.getSelectionModel();

        if (!model.isEmpty()) {
            setSelectedCard((Card) model.getSelectedItem());
        }
    }

    private void setSelectedCard(Card card) {
        lblWord.setText(card.getWord());
        txtDescription.setText(card.getDescription());

        byte[] imageBytes = card.getImage();
        if (imageBytes != null) {
            imageView.setImage(new ImageLoader().loadImage(imageBytes));
        } else {
            imageView.setImage(DefaultImage.getInstance());
        }
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }
}

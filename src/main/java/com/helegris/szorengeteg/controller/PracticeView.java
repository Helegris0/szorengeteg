/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.component.ClickableLabel;
import com.helegris.szorengeteg.controller.component.DefaultImage;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.model.entity.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class PracticeView extends AnchorPane {

    public static final String FXML = "fxml/practice.fxml";

    private final String SHOWIMAGE_PATH = "/images/showimage.png";
    private final Image startingImage = new Image(
            this.getClass().getResourceAsStream(SHOWIMAGE_PATH));

    @FXML
    private ImageView imageView;
    @FXML
    private Label lblDescription;
    @FXML
    private TextField txtWord;
    @FXML
    private Button btnCheck;
    @FXML
    private ClickableLabel lblDontKnow;
    @FXML
    private ClickableLabel lblHelp;
    @FXML
    private Label lblResult;
    @FXML
    private Button btnNextCard;

    private final PracticeSession session;
    private Card card;
    private boolean imageShown;

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeView(PracticeSession session) {
        this.session = session;
        this.card = session.getCurrentCard();
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    public void initialize() {
        setQuestion();
        imageView.setOnMouseClicked(this::showImage);
        btnCheck.setOnAction(this::check);
        lblDontKnow.setOnMouseClicked(this::dontKnow);
        lblHelp.setOnMouseClicked(this::help);
        btnNextCard.setOnAction(this::nextCard);
    }

    private void setQuestion() {
        txtWord.setText("");
        lblDescription.setText(card.getDescription());
        imageView.setImage(startingImage);
        checked(false);
    }

    private void showImage(MouseEvent event) {
        if (!imageShown) {
            if (card.getImage() != null) {
                imageView.setImage(new ImageLoader().loadImage(card.getImage()));
                imageShown = true;
            } else {
                imageView.setImage(DefaultImage.getInstance());
            }
        }
    }

    private void dontKnow(MouseEvent event) {
        incorrectlyAnswered();
        checked(true);
    }

    private void help(MouseEvent event) {
        String firstLetter = card.getWord().substring(0, 1);
        txtWord.setText(firstLetter);
        txtWord.positionCaret(1);
    }

    private void check(ActionEvent event) {
        if (!"".equals(txtWord.getText())) {
            boolean correctAnswer = txtWord.getText().toLowerCase()
                    .equals(card.getWord().toLowerCase());

            if (correctAnswer) {
                correctlyAnswered();
            } else {
                incorrectlyAnswered();
            }

            checked(true);
        }
    }

    private void correctlyAnswered() {
        lblResult.setText(Messages.msg("practice.correct"));
        session.correctAnswer();
    }

    private void incorrectlyAnswered() {
        lblResult.setText(Messages.msg("practice.incorrect") + " "
                + card.getWord());
        session.incorrectAnswer();
    }

    private void checked(boolean checked) {
        btnCheck.setDisable(checked);
        lblDontKnow.setVisible(!checked);
        lblHelp.setVisible(!checked);
        lblResult.setVisible(checked);
        btnNextCard.setVisible(checked);
    }

    private void nextCard(ActionEvent event) {
        card = session.nextCard();
        if (card != null) {
            imageShown = false;
            setQuestion();
        } else {
            evaluateSession();
        }
    }

    private void evaluateSession() {
        Stage stage = (Stage) btnNextCard.getScene().getWindow();
        stage.setScene(new Scene(new PracticeEndView(
                session.getCorrectAnswers(), session.getIncorrectAnswers())));
    }
}

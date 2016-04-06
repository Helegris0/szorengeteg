/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.practice;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.SceneStyler;
import com.helegris.szorengeteg.controller.settings.Settings;
import com.helegris.szorengeteg.controller.ImageLoader;
import com.helegris.szorengeteg.controller.mainpages.ClickableLabel;
import com.helegris.szorengeteg.controller.DefaultImage;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.model.entities.Card;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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
    private HBox hboxInput;
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
    private WordInput wordInput;
    private final Settings settings = new Settings();
    private final boolean helpSet
            = !Settings.WordHelp.NO_HELP.equals(settings.getWordHelp());
    private final boolean repeat = settings.isRepeatUnknownWords();
    private final WordInputListener inputListener = new WordInputListener() {

        @Override
        public void answeredCorrectly() {
            answerCorrect();
        }

        @Override
        public void answeredIncorrectly() {
            answerIncorrect();
        }
    };

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
        lblDontKnow.setOnMouseClicked(this::dontKnow);
        lblHelp.setOnMouseClicked(this::help);
        lblHelp.setVisible(helpSet);
        btnNextCard.setOnAction(this::nextCard);
        btnNextCard.defaultButtonProperty().bind(btnNextCard.focusedProperty());
    }

    private void setQuestion() {
        hboxInput.getChildren().clear();
        imageView.setImage(startingImage);
        lblDescription.setText(card.getDescription());
        wordInput = new WordInputFactory().getWordInput(card.getWord(), inputListener);
        hboxInput.getChildren().add(wordInput);
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
        wordInput.disableButton();
        answerIncorrect();
        checked(true);
    }

    private void help(MouseEvent event) {
        wordInput.help();
    }

    private void answerCorrect() {
        lblResult.setText(Messages.msg("practice.correct"));
        lblResult.setTextFill(Color.web("#009600"));
        if (!repeat) {
            session.correctAnswer();
        }
        Platform.runLater(btnNextCard::requestFocus);
        checked(true);
    }

    private void answerIncorrect() {
        lblResult.setText(Messages.msg("practice.incorrect") + " "
                + card.getWord().toUpperCase());
        lblResult.setTextFill(Color.web("#ff0000"));
        if (repeat) {
            session.repeatCard();
        } else {
            session.incorrectAnswer();
        }
        Platform.runLater(btnNextCard::requestFocus);
        checked(true);
    }

    private void checked(boolean checked) {
        lblDontKnow.setVisible(!checked);
        if (helpSet) {
            lblHelp.setVisible(!checked);
        }
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
        PracticeEnd practiceEnd;

        if (repeat) {
            practiceEnd = new PracticeEndRepeatedView(session.getSessionCards());
        } else {
            practiceEnd = new PracticeEndNormalView(session.getCorrectAnswers(),
                    session.getIncorrectAnswers());
        }

        stage.setScene(new SceneStyler().createScene(practiceEnd, SceneStyler.Style.PRACTICE));
    }
}
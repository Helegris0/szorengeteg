/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.SceneStyler;
import com.helegris.szorengeteg.ui.settings.Settings;
import com.helegris.szorengeteg.ui.ClickableLabel;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.ui.AudioIcon;
import com.helegris.szorengeteg.ui.MediaLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class PracticeView extends AnchorPane {

    public static final String FXML = "fxml/practice.fxml";

    @Inject
    private Settings settings;

    @FXML
    private ClickableLabel lblAbort;
    @FXML
    private VisualHelp visualHelp;
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
    @FXML
    private AudioIcon audioIcon;

    private final PracticeSession session;
    private Card card;
    private WordInput wordInput;
    private final boolean helpSet;
    private final boolean repeat;
    private final WordInputListener inputListener = new WordInputListener() {

        @Override
        public void answeredCorrectly() {
            PracticeView.this.answerCorrect();
        }

        @Override
        public void answeredIncorrectly() {
            PracticeView.this.answerIncorrect();
        }

        @Override
        public void tryAgain(int numOfTries) {
            PracticeView.this.tryAgain(numOfTries);
        }
    };
    private boolean checked;

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeView(PracticeSession session) {
        this.session = session;
        this.card = session.getCurrentCard();
        DIUtils.injectFields(this);
        helpSet = !Settings.WordHelp.NO_HELP.equals(settings.getWordHelp());
        repeat = settings.isRepeatUnknownWords();
        FXMLLoaderHelper.load(FXML, this);
        visualHelp.setCard(card);
    }

    @FXML
    public void initialize() {
        setQuestion();
        lblAbort.setOnMouseClicked(this::abort);
        lblDontKnow.setOnMouseClicked(this::dontKnow);
        lblHelp.setOnMouseClicked(this::help);
        btnNextCard.setOnAction(this::nextCard);
        btnNextCard.defaultButtonProperty().bind(btnNextCard.focusedProperty());
        audioIcon.setOnMouseClicked(this::playAudio);
    }

    private void setQuestion() {
        hboxInput.getChildren().clear();
        lblDescription.setText(card.getDescription());
        wordInput = new WordInputFactory().getWordInput(card.getWord(), inputListener);
        hboxInput.getChildren().add(wordInput);
        audioIcon.setCard(card);
        checked(false);
    }

    private void dontKnow(MouseEvent event) {
        showExpectedWord();
        checked(true);
    }

    private void help(MouseEvent event) {
        wordInput.help();
    }

    private void answerCorrect() {
        lblResult.setText(Messages.msg("practice.correct"));
        lblResult.setTextFill(Color.web("#009600"));
        if (!repeat && !checked) {
            session.correctAnswer();
        }
        Platform.runLater(btnNextCard::requestFocus);
        checked(true);
    }

    private void answerIncorrect() {
        lblResult.setText(Messages.msg("practice.incorrect"));
        lblResult.setTextFill(Color.web("#0000ff"));
        lblResult.setVisible(true);
    }

    private void tryAgain(int numOfTries) {
        lblResult.setVisible(false);
        switch (numOfTries) {
            case 1:
                if (helpSet) {
                    lblHelp.setVisible(true);
                }
                break;
            case 3:
                visualHelp.provideHelp();
                break;
            case 5:
                lblDontKnow.setVisible(true);
                break;
        }
    }

    private void showExpectedWord() {
        lblResult.setText(Messages.msg("practice.expected") + " "
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
        this.checked = checked;
        lblDontKnow.setVisible(false);
        lblHelp.setVisible(false);
        lblResult.setVisible(checked);
        btnNextCard.setVisible(checked);
        audioIcon.setVisible(checked);
    }

    private void playAudio(MouseEvent event) {
        if (card.getAudio() != null) {
            try {
                MediaPlayer player = new MediaPlayer(new MediaLoader().loadAudio(card.getAudio()));
                player.play();
            } catch (IOException ex) {
                Logger.getLogger(PracticeView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void nextCard(ActionEvent event) {
        card = session.nextCard();
        if (card != null) {
            setQuestion();
            visualHelp.setCard(card);
        } else {
//            evaluateSession();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(Messages.msg("practice.end"));
            alert.setHeaderText(Messages.msg("practice.end"));
            alert.setContentText(Messages.msg("practice.end_content"));
            alert.showAndWait();

            ((Stage) this.getScene().getWindow()).close();
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
        stage.setMaximized(true);
    }

    private void abort(MouseEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }
}

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
import com.helegris.szorengeteg.business.service.EntitySaver;
import com.helegris.szorengeteg.ui.AudioIcon;
import com.helegris.szorengeteg.ui.CloseIcon;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class PracticeView extends AnchorPane implements WordInputListener, Runnable {

    public static final String FXML = "fxml/practice.fxml";

    private static final String CLOSE_ICON_PATH = "/images/close.png";

    @Inject
    private Settings settings;
    @Inject
    private EntitySaver entitySaver;

    @FXML
    private CloseIcon closeIcon;
    @FXML
    private VisualHelp visualHelp;
    @FXML
    private Label lblDescription;
    @FXML
    private HBox hboxInput;
    @FXML
    private ImageView imgInput;
    @FXML
    private Label lblInput;
    @FXML
    private ClickableLabel lblHelp;
    @FXML
    private ImageView imgHelp;
    @FXML
    private ClickableLabel lblDontKnow;
    @FXML
    private Label lblResult;
    @FXML
    private Button btnNextCard;
    @FXML
    private AudioIcon audioIcon;
//    @FXML
//    private RadioButton rdLastHelp;
//    @FXML
//    private RadioButton rdLastVisual;
//    @FXML
//    private RadioButton rdLastGaveUp;

    private final PracticeSession session;
    private Card card;
    private WordInput wordInput;
    private final boolean helpSet;
    private final boolean playAudio;
    private final boolean repeat;
    private final long sleepTime;
    private Thread thread;
    private boolean nowHelp;
    private boolean nowGaveUp;
    private boolean checked;

    private final Image IMG_RIGHT_GRAY = new Image("/images/triangle_left_gray.png");

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeView(PracticeSession session) {
        this.session = session;
        this.card = session.getCurrentCard();
        DIUtils.injectFields(this);
        helpSet = !Settings.WordHelp.NO_HELP.equals(settings.getWordHelp());
        playAudio = settings.isPlayAudio();
        repeat = settings.isRepeatUnknownWords();
        sleepTime = settings.getHelpSeconds() * 1000;
        FXMLLoaderHelper.load(FXML, this);
        visualHelp.setCard(card);
    }

    @FXML
    public void initialize() {
        setQuestion();
        closeIcon.setImage(new Image(CLOSE_ICON_PATH));
        closeIcon.setOnMouseClicked(this::abort);
        lblInput.setOnMouseClicked(this::showInput);
        lblDontKnow.setOnMouseClicked(this::dontKnow);
        lblHelp.setOnMouseClicked(this::help);
        btnNextCard.setOnAction(this::nextCard);
        btnNextCard.defaultButtonProperty().bind(btnNextCard.focusedProperty());
        audioIcon.setOnMouseClicked(this::playAudio);
        imgInput.setImage(IMG_RIGHT_GRAY);
        imgHelp.setImage(IMG_RIGHT_GRAY);
    }

    private void setQuestion() {
        hboxInput.getChildren().clear();
        lblDescription.setText(card.getDescription());
        wordInput = new WordInputFactory().getWordInput(card.getWord(), this);
        wordInput.setVisible(false);
        lblInput.setDisable(false);
        hboxInput.getChildren().add(wordInput);
        audioIcon.setCard(card);
//        rdLastHelp.setSelected(card.isLastHelp());
//        rdLastVisual.setSelected(card.isLastVisual());
//        rdLastGaveUp.setSelected(card.isLastGaveUp());
        nowHelp = false;
        nowGaveUp = false;
        checked(false);
        thread = new Thread(this);
        thread.start();
    }

    private void showInput(MouseEvent event) {
        wordInput.setVisible(true);
        lblInput.setDisable(true);
    }

    private void dontKnow(MouseEvent event) {
        showExpectedWord();
        nowGaveUp = true;
        checked(true);
    }

    private void help(MouseEvent event) {
        wordInput.help();
        nowHelp = true;
    }

    @Override
    public void answeredCorrectly() {
        lblResult.setText(Messages.msg("practice.correct"));
        lblResult.setTextFill(Color.web("#009600"));
        if (!repeat && !checked) {
            session.correctAnswer();
        }
        Platform.runLater(btnNextCard::requestFocus);
        checked(true);
    }

    @Override
    public void answeredIncorrectly() {
        lblResult.setText(Messages.msg("practice.incorrect"));
        lblResult.setTextFill(Color.web("#0000ff"));
        lblResult.setVisible(true);
    }

    @Override
    public void tryAgain(int numOfTries) {
        lblResult.setVisible(false);
        switch (numOfTries) {
            case 1:
                if (helpSet) {
                    lblHelp.setDisable(false);
                }
                break;
            case 3:
                if (!visualHelp.isImageShown()) {
                    visualHelp.provideHelp();
                }
                break;
            case 5:
                lblDontKnow.setDisable(false);
                break;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(sleepTime);
            if (helpSet && !checked) {
                lblHelp.setDisable(false);
            }
            Thread.sleep(sleepTime);
            if (!visualHelp.isImageShown() && !checked) {
                visualHelp.provideHelp();
            }
            Thread.sleep(sleepTime);
            if (!checked) {
                lblDontKnow.setDisable(false);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PracticeView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showExpectedWord() {
//        lblResult.setText(Messages.msg("practice.expected") + " "
//                + card.getWord().toUpperCase());
//        lblResult.setTextFill(Color.web("#ff0000"));
        wordInput.revealWord();
        lblResult.setText("");
        if (repeat) {
            session.repeatCard();
        } else {
            session.incorrectAnswer();
        }
        Platform.runLater(btnNextCard::requestFocus);
    }

    private void checked(boolean checked) {
        this.checked = checked;
        lblDontKnow.setDisable(true);
        lblHelp.setDisable(true);
        lblResult.setVisible(checked);
        btnNextCard.setVisible(checked);
        audioIcon.setVisible(checked);
        if (checked) {
            card.setLastHelp(nowHelp);
            card.setLastVisual(visualHelp.isImageShown());
            card.setLastGaveUp(nowGaveUp);
            entitySaver.saveCard(card);
            if (playAudio) {
                playAudio(null);
            }
        }
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

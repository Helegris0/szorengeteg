/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.settings.Settings;
import com.helegris.szorengeteg.ui.ClickableLabel;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.service.EntitySaver;
import com.helegris.szorengeteg.ui.CloseIcon;
import com.helegris.szorengeteg.ui.MediaLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private ImageView imgVisual;
    @FXML
    private ClickableLabel lblVisual;
    @FXML
    private HBox hboxInput;
    @FXML
    private ImageView imgInput;
    @FXML
    private ClickableLabel lblInput;
    @FXML
    private ImageView imgHelp;
    @FXML
    private ClickableLabel lblHelp;
    @FXML
    private ImageView imgGiveUp;
    @FXML
    private ClickableLabel lblGiveUp;
    @FXML
    private ImageView imgPlayAudio;
    @FXML
    private ClickableLabel lblPlayAudio;
    @FXML
    private Label lblResult;
    @FXML
    private Label lblLast;
    @FXML
    private ImageView imgNext;
    @FXML
    private ClickableLabel lblNext;
    @FXML
    private ImageView imgPrev;
    @FXML
    private ClickableLabel lblPrev;
    @FXML
    private ClickableLabel lblJump;
    @FXML
    private ImageView imgJump;
    @FXML
    private TextField txtJump;
    @FXML
    private Label lblSum;

    private PracticeControl pcInput;
    private PracticeControl pcHelp;
    private PracticeControl pcVisual;
    private PracticeControl pcGiveUp;
    private PracticeControl pcPlayAudio;
    private PracticeControl pcNext;
    private PracticeControl pcPrev;
    private PracticeControl pcJump;

    private final PracticeSession session;
    private Card card;
    private WordInput wordInput;
    private final boolean helpSet;
    private final boolean playAudio;
    private final long sleepTime;
    private Thread thread;
    private boolean nowHelp;
    private boolean nowGaveUp;
    private boolean checked;

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeView(PracticeSession session) {
        this.session = session;
        this.card = session.getCurrentCard();
        DIUtils.injectFields(this);
        helpSet = !Settings.WordHelp.NO_HELP.equals(settings.getWordHelp());
        playAudio = settings.isPlayAudio();
        sleepTime = settings.getHelpSeconds() * 1000;
        FXMLLoaderHelper.load(FXML, this);
        visualHelp.setCard(card);
    }

    @FXML
    public void initialize() {
        pcInput = new PracticeControl(
                PracticeControl.Direction.LEFT, imgInput, lblInput,
                this::showInput, true, false);
        pcHelp = new PracticeControl(
                PracticeControl.Direction.LEFT, imgHelp, lblHelp,
                this::help, false, card.isLastHelp());
        pcVisual = new PracticeControl(
                PracticeControl.Direction.UP, imgVisual, lblVisual,
                this::showImage, false, card.isLastVisual());
        pcGiveUp = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgGiveUp, lblGiveUp,
                this::giveUp, false, card.isLastGaveUp());
        pcPlayAudio = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgPlayAudio, lblPlayAudio,
                this::playAudio, false, false);
        pcNext = new PracticeControl(
                PracticeControl.Direction.DOWN, imgNext, lblNext,
                this::nextCard, true, false);
        pcPrev = new PracticeControl(
                PracticeControl.Direction.UP, imgPrev, lblPrev,
                this::prevCard, false, false);
        pcJump = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgJump, lblJump,
                this::jump, true, false);
        setQuestion();
        closeIcon.setImage(new Image(CLOSE_ICON_PATH));
        closeIcon.setOnMouseClicked(this::abort);
        lblSum.setText("/" + session.getLength());
        txtJump.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                jump();
            }
        });
        this.setOnKeyPressed((KeyEvent event) -> {
            if (checked && event.getCode().equals(KeyCode.ENTER)) {
                nextCard();
            }
        });
    }

    private void setQuestion() {
        lblDescription.setText(card.getDescription());
        hboxInput.getChildren().clear();
        wordInput = new WordInputFactory().getWordInput(card.getWord(), this);
        wordInput.setVisible(false);
        hboxInput.getChildren().add(wordInput);
        pcHelp.updateSpecial(card.isLastHelp());
        pcVisual.updateSpecial(card.isLastVisual());
        pcGiveUp.updateSpecial(card.isLastGaveUp());
        pcInput.setEnabled(true);
        pcHelp.setEnabled(false);
        pcPlayAudio.setEnabled(false);
        pcPrev.setEnabled(session.getIndex() > 0);
        txtJump.setDisable(true);
        txtJump.setText(Integer.toString(session.getIndex() + 1));
        if (session.getIndex() == session.getLength() - 1) {
            lblNext.setText(Messages.msg("practice.end"));
            lblLast.setVisible(true);
        } else {
            lblNext.setText(Messages.msg("practice.next"));
            lblLast.setVisible(false);
        }
        nowHelp = false;
        nowGaveUp = false;
        checked(false);
    }

    private void showInput() {
        wordInput.setVisible(true);
        wordInput.requestFocus();
        pcInput.setEnabled(false);
        thread = new Thread(this);
        thread.start();
    }

    private void showImage() {
        visualHelp.showImage(null);
        pcVisual.setEnabled(false);
    }

    private void giveUp() {
        showExpectedWord();
        nowGaveUp = true;
        checked(true);
    }

    private void help() {
        wordInput.help();
        nowHelp = true;
    }

    @Override
    public void answeredCorrectly() {
        lblResult.setText(Messages.msg("practice.correct"));
        lblResult.setTextFill(Color.web("#009600"));
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
        numOfTries += 1;
        numOfTries /= 2;
        helpStep(numOfTries);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(sleepTime);
            helpStep(1);
            Thread.sleep(sleepTime);
            helpStep(2);
            Thread.sleep(sleepTime);
            helpStep(3);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void helpStep(int step) {
        switch (step) {
            case 1:
                if (helpSet && !checked) {
                    pcHelp.setEnabled(true);
                }
                break;
            case 2:
                if (!visualHelp.isImageShown() && !checked) {
                    pcVisual.setEnabled(true);
                }
                break;
            case 3:
                if (!checked) {
                    pcGiveUp.setEnabled(true);
                }
                break;
        }
    }

    private void showExpectedWord() {
        wordInput.revealWord();
        lblResult.setText("");
    }

    private void checked(boolean checked) {
        this.checked = checked;
        if (thread != null) {
            thread.interrupt();
        }
        pcGiveUp.setEnabled(false);
        pcHelp.setEnabled(false);
        pcVisual.setEnabled(false);
        if (checked && card.getAudio() != null) {
            pcPlayAudio.setEnabled(true);
        }
        lblResult.setVisible(checked);
        if (checked) {
            card.setLastHelp(nowHelp);
            card.setLastVisual(visualHelp.isImageShown());
            card.setLastGaveUp(nowGaveUp);
            entitySaver.saveCard(card);
            if (playAudio) {
                playAudio();
            }
            lblNext.setStyle("-fx-font-weight: bold");
            lblNext.setFocusTraversable(true);
            lblNext.requestFocus();
        } else {
            lblNext.setStyle("-fx-font-weight: regular");
        }
    }

    private void playAudio() {
        if (card.getAudio() != null) {
            try {
                MediaPlayer player = new MediaPlayer(new MediaLoader().loadAudio(card.getAudio()));
                player.play();
            } catch (IOException ex) {
                Logger.getLogger(PracticeView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void nextCard() {
        card = session.nextCard();
        if (card != null) {
            setQuestion();
            visualHelp.setCard(card);
        } else {
            abort(null);
        }
    }

    private void prevCard() {
        Card prev = session.prevCard();
        if (prev != null) {
            card = prev;
            setQuestion();
            visualHelp.setCard(card);
        }
    }

    private void jump() {
        if (txtJump.isDisable()) {
            txtJump.setDisable(false);
            txtJump.requestFocus();
        } else {
            try {
                int number = Integer.parseInt(txtJump.getText());
                if (number != session.getIndex() + 1
                        && 0 < number && number <= session.getLength()) {
                    card = session.jumpTo(number - 1);
                    setQuestion();
                    visualHelp.setCard(card);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                txtJump.setText(Integer.toString(session.getIndex() + 1));
            }
        }
    }

    private void abort(MouseEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }
}

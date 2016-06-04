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
import com.helegris.szorengeteg.ui.AudioIcon;
import com.helegris.szorengeteg.ui.CloseIcon;
import com.helegris.szorengeteg.ui.MediaLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class PracticeView extends AnchorPane implements WordInputListener {

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
    private ImageView audioIcon;
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

    private PracticeControl pcInput;
    private PracticeControl pcHelp;
    private PracticeControl pcVisual;
    private PracticeControl pcGiveUp;
    private PracticeControl pcPlayAudio;
    private PracticeControl pcNext;
    private PracticeControl pcPrev;

    private final PracticeSession session;
    private Card card;
    private WordInput wordInput;
    private final boolean helpSet;
    private final boolean playAudio;
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
        FXMLLoaderHelper.load(FXML, this);
        visualHelp.setCard(card);
    }

    @FXML
    public void initialize() {
        pcInput = new PracticeControl(
                PracticeControl.Direction.LEFT, imgInput, lblInput,
                this::showInput);
        pcHelp = new PracticeControl(
                PracticeControl.Direction.LEFT, imgHelp, lblHelp,
                this::help);
        pcVisual = new PracticeControl(
                PracticeControl.Direction.UP, imgVisual, lblVisual,
                this::showImage);
        pcGiveUp = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgGiveUp, lblGiveUp,
                this::giveUp);
        pcPlayAudio = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgPlayAudio, lblPlayAudio,
                this::playAudio);
        pcNext = new PracticeControl(
                PracticeControl.Direction.DOWN, imgNext, lblNext,
                this::nextCard);
        pcPrev = new PracticeControl(
                PracticeControl.Direction.UP, imgPrev, lblPrev,
                this::prevCard);
        audioIcon.setImage(new Image(AudioIcon.AUDIO));
        audioIcon.setOnMouseClicked((MouseEvent event) -> playAudio());
        setQuestion();
        closeIcon.setImage(new Image(CLOSE_ICON_PATH));
        closeIcon.setOnMouseClicked(this::abort);
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

        pcInput.setEnabled(true);
        pcInput.resetNow();
        pcHelp.setEnabled(false);
        pcHelp.setUsedLast(card.isLastHelp());
        pcVisual.setEnabled(true);
        pcVisual.setUsedLast(card.isLastVisual());
        pcGiveUp.setEnabled(false);
        pcGiveUp.setUsedLast(card.isLastGaveUp());
        pcPlayAudio.resetNow();
        pcPlayAudio.setVisible(false);
        audioIcon.setVisible(false);
        pcPrev.setVisible(session.getIndex() > 0);

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
        pcInput.useAndDisable();
        pcHelp.setEnabled(true);
        pcGiveUp.setEnabled(true);
    }

    private void showImage() {
        visualHelp.showImage(null);
        pcVisual.useAndDisable();
    }

    private void giveUp() {
        showExpectedWord();
        nowGaveUp = true;
        checked(true);
        pcGiveUp.useAndDisable();
    }

    private void help() {
        if (helpSet) {
            wordInput.help();
            nowHelp = true;
            pcHelp.useAndDisable();
        }
    }

    @Override
    public void fullInput() {
        checked(true);
    }

    private void showExpectedWord() {
        wordInput.revealWord();
    }

    private void checked(boolean checked) {
        this.checked = checked;
        if (checked) {
            card.setLastHelp(nowHelp);
            card.setLastVisual(visualHelp.isImageShown());
            card.setLastGaveUp(nowGaveUp);
            entitySaver.saveCard(card);

            pcHelp.setEnabled(false);
            pcGiveUp.setEnabled(false);

            if (card.getAudio() != null) {
                pcPlayAudio.setVisible(true);
                audioIcon.setVisible(true);
                if (playAudio) {
                    playAudio();
                }
            }

            lblNext.setStyle("-fx-font-weight: bold");
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
        pcPlayAudio.use();
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

    private void abort(MouseEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }
}

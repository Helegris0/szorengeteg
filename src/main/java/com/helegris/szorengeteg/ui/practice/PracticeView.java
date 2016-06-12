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
    private ImageView imgDefault;
    @FXML
    private ClickableLabel lblDefault;
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
    @FXML
    private Label lblOrdinal;

    private PracticeControl pcDefault;
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
        pcDefault = new PracticeControl(
                PracticeControl.Direction.DOWN, imgDefault, lblDefault,
                this::defaultMode);
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

        setBoldness(lblDefault, true);
        pcDefault.setEnabled(true);
        pcDefault.setUsed(false);
        pcInput.setEnabled(false);
        pcInput.setUsed(false);
        pcHelp.setEnabled(false);
        pcHelp.setUsed(card.isLastHelp());
        pcVisual.setEnabled(false);
        pcVisual.setUsed(card.isLastVisual());
        pcGiveUp.setEnabled(false);
        pcGiveUp.setUsed(card.isLastGaveUp());
        pcPlayAudio.setUsed(false);
        pcPlayAudio.setEnabled(false);
        pcPrev.setEnabled(session.getIndex() > 0);

        lblOrdinal.setText(
                String.format("%s. témakör (%s) %s. szava (%s szóból)",
                        card.getTopic().getOrdinal(),
                        card.getTopic().getName(),
                        session.getIndex() + 1,
                        session.getLength()));
        if (session.getIndex() == session.getLength() - 1) {
            lblLast.setVisible(true);
        } else {
            lblLast.setVisible(false);
        }
        checked(false);
    }

    private void defaultMode() {
        setBoldness(lblDefault, false);
        pcDefault.setEnabled(false);
        pcDefault.setUsed(true);

        pcInput.setUsed(false);
        pcHelp.setUsed(false);
        pcVisual.setUsed(false);
        pcGiveUp.setUsed(false);
        pcPlayAudio.setUsed(false);

        setBoldness(lblInput, true);
        pcInput.setEnabled(true);
        pcVisual.setEnabled(true);
    }

    private void showInput() {
        setBoldness(lblInput, false);
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
        pcGiveUp.useAndDisable();
        showExpectedWord();
        checked(true);
    }

    private void help() {
        if (helpSet) {
            wordInput.help();
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
            card.setLastHelp(pcHelp.isUsed());
            card.setLastVisual(pcVisual.isUsed());
            card.setLastGaveUp(pcGiveUp.isUsed());
            entitySaver.saveCard(card);

            pcHelp.setEnabled(false);
            pcGiveUp.setEnabled(false);

            if (card.getAudio() != null) {
                pcPlayAudio.setEnabled(true);
                if (playAudio) {
                    playAudio();
                }
            }

            setBoldness(lblNext, true);
        } else {
            setBoldness(lblNext, false);
        }
    }

    private void setBoldness(Label label, boolean bold) {
        label.setStyle(bold ? "-fx-font-weight: bold"
                : "-fx-font-weight: regular;-fx-opacity: 1.0;");
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
        pcPlayAudio.setUsed(true);
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

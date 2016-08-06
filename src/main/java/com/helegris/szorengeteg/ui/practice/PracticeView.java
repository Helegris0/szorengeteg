/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.MainApp;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.EntitySaver;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.ui.AudioIcon;
import com.helegris.szorengeteg.ui.HelpControl;
import com.helegris.szorengeteg.ui.MediaLoader;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javax.inject.Inject;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Timi
 */
public class PracticeView extends AnchorPane implements WordInputListener {

    private static final String FXML = "fxml/practice.fxml";

    @Inject
    private EntitySaver entitySaver;

    @FXML
    private VisualHelp visualHelp;
    @FXML
    private Label lblDescription;
    @FXML
    private ImageView imgVisual;
    @FXML
    private Label lblVisual;
    @FXML
    private ImageView imgDefault;
    @FXML
    private Label lblDefault;
    @FXML
    private HBox hboxInput;
    @FXML
    private ImageView imgInput;
    @FXML
    private Label lblInput;
    @FXML
    private ImageView imgHelp;
    @FXML
    private Label lblHelp;
    @FXML
    private ImageView imgGiveUp;
    @FXML
    private Label lblGiveUp;
    @FXML
    private ImageView imgPlayAudio;
    @FXML
    private Label lblPlayAudio;
    @FXML
    private ImageView audioIcon;
    @FXML
    private ImageView imgNext;
    @FXML
    private Label lblNext;
    @FXML
    private ImageView imgChooseTopic;
    @FXML
    private Label lblChooseTopic;
    @FXML
    private TextField txtTopicOrdinal;
    @FXML
    private TextField txtWordOrdinal;
    @FXML
    private ImageView imgJump;
    @FXML
    private ImageView imgQuit;
    @FXML
    private Label lblQuit;
    @FXML
    private HelpControl helpControl;

    private PracticeControl pcDefault;
    private PracticeControl pcInput;
    private PracticeControl pcHelp;
    private PracticeControl pcVisual;
    private PracticeControl pcGiveUp;
    private PracticeControl pcPlayAudio;
    private PracticeControl pcNext;
    private PracticeControl pcChooseTopic;
    private PracticeControl pcJump;
    private PracticeControl pcQuit;

    private PracticeSession session;
    private Card card;
    private WordInput wordInput;
    private boolean checked;
    private final PopOver popInvalidJump = new PopOver();

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeView(PracticeSession session) {
        setSession(session);
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    private void setSession(PracticeSession session) {
        this.session = session;
        this.card = session.getCurrentCard();
    }

    @FXML
    private void initialize() {
        pcDefault = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgDefault, lblDefault,
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
                this::playAudioClicked);
        pcNext = new PracticeControl(
                PracticeControl.Direction.DOWN, imgNext, lblNext,
                this::nextCard);
        pcChooseTopic = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgChooseTopic, lblChooseTopic,
                this::switchToTopics);
        pcQuit = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgQuit, lblQuit,
                this::abort);
        audioIcon.setImage(new Image(AudioIcon.AUDIO));
        imgJump.setImage(new Image("/images/triangle_right_unused.png"));
        imgJump.setOnMouseClicked(event -> jump());
        setQuestion();
        this.setOnKeyPressed((KeyEvent event) -> {
            if (checked && event.getCode().equals(KeyCode.ENTER)) {
                nextCard();
            }
        });
        txtTopicOrdinal.setOnMouseClicked(event -> txtTopicOrdinal.selectAll());
        txtWordOrdinal.setOnMouseClicked(event -> txtWordOrdinal.selectAll());
        helpControl.setContentType(HelpControl.ContentType.PRACTICE);
    }

    private void setQuestion() {
        visualHelp.setCard(card);
        lblDescription.setText(card.getDescription());
        hboxInput.getChildren().clear();
        wordInput = new SpelledWordInput(card.getWord(), this);
        wordInput.setAlignment(Pos.CENTER);
        wordInput.setVisible(false);
        hboxInput.getChildren().add(wordInput);

        if (card.isLastInput()) {
            setBoldness(lblDefault, true);
            pcDefault.setEnabled(true);
            pcInput.setUsed(card.isLastInput());
            pcHelp.setUsed(card.isLastHelp());
            pcVisual.setUsed(card.isLastVisual());
            pcGiveUp.setUsed(card.isLastGaveUp());
            pcPlayAudio.setUsed(card.isLastPlayedAudio());
        } else {
            defaultMode();
        }
        pcInput.setEnabled(!card.isLastInput());
        pcHelp.setEnabled(false);
        pcVisual.setEnabled(false);
        pcGiveUp.setEnabled(false);
        pcPlayAudio.setEnabled(false);

        txtTopicOrdinal.setText(Integer.toString(card.getTopic().getOrdinal()));
        txtWordOrdinal.setText(Integer.toString(session.getIndex() + 1));
        checked(false);
    }

    private void defaultMode() {
        setBoldness(lblDefault, false);
        pcDefault.setEnabled(false);

        pcInput.setUsed(false);
        pcHelp.setUsed(false);
        pcVisual.setUsed(false);
        pcGiveUp.setUsed(false);
        pcPlayAudio.setUsed(false);

        setBoldness(lblInput, true);
        pcInput.setEnabled(true);
    }

    private void showInput() {
        setBoldness(lblInput, false);
        wordInput.setVisible(true);
        wordInput.requestFocus();
        pcInput.useAndDisable();
        pcHelp.setEnabled(true);
        setBoldness(lblHelp, true);
    }

    private void showImage() {
        setBoldness(lblVisual, false);
        visualHelp.showImage(null);
        pcVisual.useAndDisable();
        if (!checked) {
            setBoldness(lblGiveUp, true);
            pcGiveUp.setEnabled(true);
        }
    }

    private void giveUp() {
        pcGiveUp.useAndDisable();
        showExpectedWord();
        checked(true);
    }

    private void help() {
        setBoldness(lblHelp, false);
        wordInput.help();
        pcHelp.useAndDisable();
        setBoldness(lblVisual, true);
        pcVisual.setEnabled(true);
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
            card.setLastInput(pcInput.isUsed());
            card.setLastHelp(pcHelp.isUsed());
            card.setLastVisual(pcVisual.isUsed());
            card.setLastGaveUp(pcGiveUp.isUsed());
            card.setLastPlayedAudio(pcPlayAudio.isUsed());
            entitySaver.saveCard(card);

            pcHelp.setEnabled(false);
            pcVisual.setEnabled(false);
            pcGiveUp.setEnabled(false);

            if (card.getAudio() != null) {
                pcPlayAudio.setEnabled(true);
                setBoldness(lblPlayAudio, true);
            }
            setBoldness(lblNext, true);

            setBoldness(lblHelp, false);
            setBoldness(lblVisual, false);
            setBoldness(lblGiveUp, false);
        } else {
            setBoldness(lblNext, false);
        }
        Platform.runLater(() -> requestFocus());
    }

    private void setBoldness(Label label, boolean bold) {
        label.setStyle(bold ? "-fx-font-weight: bold;"
                : "-fx-font-weight: regular;-fx-opacity: 1.0;");
    }

    private void playAudioClicked() {
        playAudio();
        if (!pcPlayAudio.isUsed()) {
            pcPlayAudio.setUsed(true);
            card.setLastPlayedAudio(true);
            entitySaver.saveCard(card);
            setBoldness(lblPlayAudio, false);
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
        if (checked) {
            Card c = session.nextCard();
            if (c != null) {
                card = c;
                setQuestion();
                visualHelp.setCard(card);
            } else {
                nextTopic();
            }
        }
    }

    private void nextTopic() {
        setSession(new PracticeSession(
                new NextTopicFinder().getNextTopic(
                        session.getTopic())));
        setQuestion();
    }

    private void jump() {
        int origTopicOrd = card.getTopic().getOrdinal();
        int origWordOrd = session.getIndex() + 1;

        try {
            int newTopicOrd = Integer.parseInt(txtTopicOrdinal.getText());
            int newWordOrd = Integer.parseInt(txtWordOrdinal.getText());

            if (origTopicOrd == newTopicOrd) {
                if (0 < newWordOrd && newWordOrd <= session.getLength()) {
                    card = session.jumpTo(newWordOrd - 1);
                    setQuestion();
                } else {
                    throw new Exception(Messages.msg("practice.unsuccessful_jump", session.getLength()));
                }
            } else {
                SessionJump jump = new SessionJump(newTopicOrd, newWordOrd);
                if (jump.isValid()) {
                    session = new PracticeSession(jump.getTopic(), newWordOrd - 1);
                    card = session.getCurrentCard();
                    setQuestion();
                } else {
                    if (jump.getTopic() != null) {
                        throw new Exception(Messages.msg("practice.unsuccessful_jump", session.getLength()));
                    } else {
                        throw new Exception(Messages.msg("practice.no_such_topic"));
                    }
                }
            }
        } catch (NumberFormatException ex) {
            txtTopicOrdinal.setText(Integer.toString(origTopicOrd));
            txtWordOrdinal.setText(Integer.toString(origWordOrd));
        } catch (Exception ex) {
            txtTopicOrdinal.setText(Integer.toString(origTopicOrd));
            txtWordOrdinal.setText(Integer.toString(origWordOrd));
            popInvalidJump.setContentNode(new Label(ex.getMessage()));
            popInvalidJump.show(imgJump);
        }
    }

    private void abort() {
        ButtonType yes = new ButtonType(Messages.msg("common.yes"), ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType(Messages.msg("common.cancel"), ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", yes, no);
        alert.setTitle(Messages.msg("common.quit"));
        alert.setHeaderText(Messages.msg("common.sure_quit"));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        } else {
            alert.close();
        }
    }

    private void switchToTopics() {
        MainApp.getInstance().setEditorScene();
    }

    public interface TopicChangeListener {

        public void changeTopic(Topic topic);
    }
}

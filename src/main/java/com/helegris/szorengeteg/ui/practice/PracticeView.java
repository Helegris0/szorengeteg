/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.MainApp;
import com.helegris.szorengeteg.ui.settings.Settings;
import com.helegris.szorengeteg.ui.ClickableLabel;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.business.service.EntitySaver;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.ui.AudioIcon;
import com.helegris.szorengeteg.ui.MediaLoader;
import com.helegris.szorengeteg.ui.SceneStyler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.inject.Inject;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Timi
 */
public class PracticeView extends AnchorPane implements WordInputListener {

    public static final String FXML = "fxml/practice.fxml";

    @Inject
    private Settings settings;
    @Inject
    private EntitySaver entitySaver;

    @FXML
    private ImageView helpIcon;
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
    private ImageView imgNext;
    @FXML
    private ClickableLabel lblNext;
    @FXML
    private ImageView imgPrev;
    @FXML
    private ClickableLabel lblPrev;
    @FXML
    private Label lblTopicOrdinal;
    @FXML
    private Label lblWordOrdinal;
    @FXML
    private ImageView imgChooseTopic;
    @FXML
    private ClickableLabel lblChooseTopic;
    @FXML
    private ImageView imgQuit;
    @FXML
    private ClickableLabel lblQuit;

    private PracticeControl pcDefault;
    private PracticeControl pcInput;
    private PracticeControl pcHelp;
    private PracticeControl pcVisual;
    private PracticeControl pcGiveUp;
    private PracticeControl pcPlayAudio;
    private PracticeControl pcNext;
    private PracticeControl pcPrev;
    private PracticeControl pcChooseTopic;
    private PracticeControl pcQuit;

    private PracticeSession session;
    private Card card;
    private WordInput wordInput;
    private final boolean helpSet;
    private final boolean playAudio;
    private boolean checked;
    private final PopOver popLast = new PopOver(popOverPane());

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeView(PracticeSession session) {
        setSession(session);
        DIUtils.injectFields(this);
        helpSet = !Settings.WordHelp.NO_HELP.equals(settings.getWordHelp());
        playAudio = settings.isPlayAudio();
        FXMLLoaderHelper.load(FXML, this);
    }

    private void setSession(PracticeSession session) {
        this.session = session;
        this.card = session.getCurrentCard();
    }

    @FXML
    public void initialize() {
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
        pcPrev = new PracticeControl(
                PracticeControl.Direction.UP, imgPrev, lblPrev,
                this::prevCard);
        pcChooseTopic = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgChooseTopic, lblChooseTopic,
                this::switchToTopics);
        pcQuit = new PracticeControl(
                PracticeControl.Direction.RIGHT, imgQuit, lblQuit,
                this::abort);
        audioIcon.setImage(new Image(AudioIcon.AUDIO));
        setQuestion();
        this.setOnKeyPressed((KeyEvent event) -> {
            if (checked && event.getCode().equals(KeyCode.ENTER)) {
                nextCard();
            }
        });
        helpIcon.setImage(new Image("/images/help.png"));
        helpIcon.setOnMouseClicked(event -> {
            Stage helpStage = new Stage();
            helpStage.setScene(new SceneStyler().createScene(
                    new HelpView(), SceneStyler.Style.MAIN));
            helpStage.setTitle("Segítség");
            Stage thisStage = (Stage) PracticeView.this.getScene().getWindow();
            helpStage.initOwner(thisStage);
            helpStage.show();
        });
    }

    private void setQuestion() {
        visualHelp.setCard(card);
        lblDescription.setText(card.getDescription() + " ①");
        hboxInput.getChildren().clear();
        wordInput = new WordInputFactory().getWordInput(card.getWord(), this);
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
        pcPrev.setEnabled(session.getIndex() > 0);

        lblTopicOrdinal.setText(" " + card.getTopic().getOrdinal() + ". ");
        lblWordOrdinal.setText(" " + (session.getIndex() + 1) + ". ");
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
        pcVisual.setEnabled(true);
    }

    private void showInput() {
        setBoldness(lblInput, false);
        wordInput.setVisible(true);
        wordInput.requestFocus();
        pcInput.useAndDisable();
        pcHelp.setEnabled(true);
        pcVisual.setEnabled(true);
        pcGiveUp.setEnabled(true);
        if (helpSet) {
            setBoldness(lblHelp, true);
        } else if (!pcVisual.isUsed()) {
            setBoldness(lblVisual, true);
        }
    }

    private void showImage() {
        setBoldness(lblVisual, false);
        visualHelp.showImage(null);
        pcVisual.useAndDisable();
        if (!pcGiveUp.isUsed()) {
            setBoldness(lblGiveUp, true);
        }
    }

    private void giveUp() {
        pcGiveUp.useAndDisable();
        showExpectedWord();
        checked(true);
    }

    private void help() {
        if (helpSet) {
            setBoldness(lblHelp, false);
            wordInput.help();
            pcHelp.useAndDisable();
            if (!pcVisual.isUsed()) {
                setBoldness(lblVisual, true);
            }
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
            card.setLastInput(pcInput.isUsed());
            card.setLastHelp(pcHelp.isUsed());
            card.setLastVisual(pcVisual.isUsed());
            card.setLastGaveUp(pcGiveUp.isUsed());
            card.setLastPlayedAudio(pcPlayAudio.isUsed());
            entitySaver.saveCard(card);

            pcHelp.setEnabled(false);
            pcGiveUp.setEnabled(false);

            if (card.getAudio() != null) {
                pcPlayAudio.setEnabled(true);
                if (playAudio) {
                    playAudio();
                } else {
                    setBoldness(lblPlayAudio, true);
                }
            } else {
                setBoldness(lblNext, true);
            }

            setBoldness(lblHelp, false);
            setBoldness(lblVisual, false);
            setBoldness(lblGiveUp, false);
        } else {
            setBoldness(lblNext, false);
        }
    }

    private void setBoldness(Label label, boolean bold) {
        label.setStyle(bold ? "-fx-font-weight: bold"
                : "-fx-font-weight: regular;-fx-opacity: 1.0;");
    }

    private void playAudioClicked() {
        playAudio();
        if (!pcPlayAudio.isUsed()) {
            pcPlayAudio.setUsed(true);
            card.setLastPlayedAudio(true);
            entitySaver.saveCard(card);
            setBoldness(lblPlayAudio, false);
            setBoldness(lblNext, true);
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
        Card c = session.nextCard();
        if (c != null) {
            card = c;
            setQuestion();
            visualHelp.setCard(card);
        } else {
            popLast.show(lblNext);
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

    private void nextTopic() {
        setSession(new PracticeSession(
                new NextTopicFinder().getNextTopic(
                        session.getTopic())));
        setQuestion();
    }

    private void chooseTopic() {
        Stage chooserStage = new Stage();
        chooserStage.setScene(new SceneStyler().createScene(
                new TopicChooserView(
                        session.getTopic(), topic -> {
                            setSession(new PracticeSession(topic));
                            setQuestion();
                        }), SceneStyler.Style.PRACTICE));
        chooserStage.initModality(Modality.APPLICATION_MODAL);
        chooserStage.setTitle(Messages.msg("practice.choose_topic"));
        Stage thisStage = (Stage) this.getScene().getWindow();
        chooserStage.initOwner(thisStage);
        chooserStage.showAndWait();
    }

    private void abort() {
        popLast.hide(Duration.millis(0));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
    }

    private AnchorPane popOverPane() {
        VBox vBox = new VBox();
        AnchorPane anchorPane = new AnchorPane(vBox);
        vBox.setMaxWidth(600);
        AnchorPane.setTopAnchor(vBox, 5.);
        AnchorPane.setRightAnchor(vBox, 5.);
        AnchorPane.setBottomAnchor(vBox, 5.);
        AnchorPane.setLeftAnchor(vBox, 5.);

        Label label = new Label(Messages.msg("practice.last"));
        label.setWrapText(true);
//        Button btnNextTopic = new Button(Messages.msg("practice.next_topic"));
//        btnNextTopic.setOnAction(event -> {
//            nextTopic();
//            popLast.hide();
//        });
//        Button btnOtherTopic = new Button(Messages.msg("practice.other_topic"));
//        btnOtherTopic.setOnAction(event -> chooseTopic());
//        Button btnQuit = new Button(Messages.msg("practice.quit"));
//        btnQuit.setOnAction(event -> abort());

        vBox.getChildren().add(label);
//        vBox.getChildren().add(new HBox(btnNextTopic, btnOtherTopic, btnQuit));

        return anchorPane;
    }

    private void switchToTopics() {
        MainApp.getInstance().setEditorScene();
    }

    public interface TopicChangeListener {

        public void changeTopic(Topic topic);
    }
}

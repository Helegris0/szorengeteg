/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.mainpages;

import com.helegris.szorengeteg.ui.ClickableLabel;
import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.DefaultImage;
import com.helegris.szorengeteg.ui.MediaLoader;
import com.helegris.szorengeteg.ui.SceneStyler;
import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.service.CardLoader;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.ui.PositionListener;
import com.helegris.szorengeteg.ui.Positioner;
import com.helegris.szorengeteg.ui.practice.PracticeSession;
import com.helegris.szorengeteg.ui.practice.PracticeView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class TopicBox extends Pane {

    private static final String FXML = "fxml/topicbox.fxml";

    @Inject
    private CardLoader cardLoader;

    @FXML
    private Pane positionerPane;
    @FXML
    private ImageView imageView;
    @FXML
    private ClickableLabel lblName;
    @FXML
    private ClickableLabel lblEdit;
    @FXML
    private Label lblNumberOfWords;

    private final Positioner positioner;

    private final Topic topic;
    private int numOfCards;

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicBox(Topic topic, TopicBoxMoveListener moveListener) {
        this.topic = topic;
        positioner = new Positioner(new PositionListener() {

            @Override
            public void up() {
                moveListener.moveUp(TopicBox.this);
            }

            @Override
            public void down() {
                moveListener.moveDown(TopicBox.this);
            }
        });
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    protected void initialize() {
        setImage();
        setModifyable(false);
        setNameLabel();
        positionerPane.getChildren().add(positioner);
        numOfCards = cardLoader.loadByTopic(topic).size();
        lblEdit.setOnMouseClicked(this::editTopic);
        lblNumberOfWords.setText(numOfCards + " "
                + Messages.msg("topicbox.words"));
        imageView.setOnMouseClicked(this::startPracticeSession);
        lblName.setOnMouseClicked(this::startPracticeSession);
    }

    protected void editTopic(MouseEvent event) {
        VistaNavigator.getMainView().loadContentEditTopic(topic);
    }

    private void startPracticeSession(MouseEvent event) {
        if (numOfCards > 0) {
            Stage pStage = new Stage();
            pStage.setScene(new SceneStyler().createScene(
                    new PracticeView(new PracticeSession(topic)),
                    SceneStyler.Style.PRACTICE));
            pStage.setTitle(topic.getName() + " " + Messages.msg("practice.title"));
            pStage.initModality(Modality.APPLICATION_MODAL);
            pStage.setMaximized(true);
            Stage thisStage = (Stage) this.getScene().getWindow();
            pStage.initOwner(thisStage);
            thisStage.hide();
            pStage.showAndWait();
            thisStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Messages.msg("topicbox.alert.title"));
            alert.setHeaderText(topic.getName() + " "
                    + Messages.msg("topicbox.alert.header"));
            alert.setContentText(Messages.msg("topicbox.alert.content"));
            alert.showAndWait();
        }
    }

    public void setNameLabel() {
        if (topic.getOrdinal() != null) {
            lblName.setText(topic.getOrdinal() + ". " + topic.getName());
        }
    }

    private void setImage() {
        Image image;

        if (topic.getImage() != null) {
            image = new MediaLoader().loadImage(topic.getImage());
        } else {
            image = DefaultImage.getInstance();
        }

        imageView.setImage(image);
    }

    public void setModifyable(boolean modifyable) {
        positioner.setVisible(modifyable);
    }

    public Topic getTopic() {
        return topic;
    }
}

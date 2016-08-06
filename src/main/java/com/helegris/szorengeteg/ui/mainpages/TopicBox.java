/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.mainpages;

import com.helegris.szorengeteg.ui.ClickableLabel;
import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.MainApp;
import com.helegris.szorengeteg.ui.DefaultImage;
import com.helegris.szorengeteg.ui.MediaLoader;
import com.helegris.szorengeteg.ui.VistaNavigator;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.business.service.CardLoader;
import com.helegris.szorengeteg.business.model.Topic;
import com.helegris.szorengeteg.ui.PositionListener;
import com.helegris.szorengeteg.ui.Positioner;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javax.inject.Inject;

/**
 *
 * @author Timi
 */
public class TopicBox extends HBox {

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

    private final TopicClickListener clickListener;
    private final Positioner positioner;

    private final Topic topic;
    private int numOfCards;

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicBox(Topic topic, TopicClickListener clickListener, TopicBoxMoveListener moveListener) {
        this.topic = topic;
        this.clickListener = clickListener;
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
        lblEdit.setOnMouseClicked(this::editClick);
        lblNumberOfWords.setText(numOfCards + " "
                + Messages.msg("topicbox.words"));
        imageView.setOnMouseClicked(this::topicClick);
        lblName.setOnMouseClicked(this::topicClick);
    }

    protected void editClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            VistaNavigator.getMainView().loadContentEditTopic(topic);
        }
    }

    private void topicClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 1) {
                clickListener.clicked(this);
            } else if (event.getClickCount() == 2) {
                if (numOfCards > 0) {
                    MainApp.getInstance().setPracticeScene(topic);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Messages.msg("topicbox.alert.title"));
                    alert.setHeaderText(topic.getName() + " "
                            + Messages.msg("topicbox.alert.header"));
                    alert.setContentText(Messages.msg("topicbox.alert.content"));
                    alert.showAndWait();
                }
            }
        }
    }

    public void highlight(boolean highlight) {
        lblName.setStyle(highlight
                ? "-fx-font-weight: bold;-fx-font-size: 18pt;"
                + "-fx-background-color:rgba(85, 255, 68,0.7);"
                : "-fx-font-weight: bold;-fx-font-size: 18pt;");
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

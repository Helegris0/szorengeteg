/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.mainpages;

import com.helegris.szorengeteg.DIUtils;
import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.SceneStyler;
import com.helegris.szorengeteg.controller.VistaNavigator;
import com.helegris.szorengeteg.messages.Messages;
import com.helegris.szorengeteg.model.CardLoader;
import com.helegris.szorengeteg.model.entities.Topic;
import com.helegris.szorengeteg.controller.practice.PracticeSession;
import com.helegris.szorengeteg.controller.practice.PracticeView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private ImageView imageView;
    @FXML
    private Label lblName;
    @FXML
    private ClickableLabel lblEdit;
    @FXML
    private ClickableLabel lblInfo;
    @FXML
    private Label lblNumberOfWords;
    @FXML
    private Button btnPractice;

    private final Topic topic;
    private Image image;
    private boolean infoVisibility;

    @SuppressWarnings("LeakingThisInConstructor")
    public TopicBox(Topic topic) {
        this.topic = topic;
        setImage();
        DIUtils.injectFields(this);
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    protected void initialize() {
        if (image != null) {
            imageView.setImage(image);
        }
        lblName.setText(topic.getName());
        lblEdit.setOnMouseClicked(this::editTopic);
        lblInfo.setOnMouseClicked(this::toggleInfoVisibility);
        lblNumberOfWords.setVisible(infoVisibility);
        lblNumberOfWords.setText(Messages.msg("topicbox.allwords")
                + cardLoader.loadByTopic(topic).size());
        btnPractice.setOnAction(this::startPracticeSession);
    }

    protected void editTopic(MouseEvent event) {
        VistaNavigator.getMainView().loadContentEditTopic(topic);
    }

    private void toggleInfoVisibility(MouseEvent event) {
        infoVisibility = !infoVisibility;
        lblNumberOfWords.setVisible(infoVisibility);
    }

    private void startPracticeSession(ActionEvent event) {
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
    }

    private void setImage() {
        if (topic.getImage() != null) {
            InputStream inputStream = new ByteArrayInputStream(topic.getImage());
            image = new Image(inputStream);
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.practice;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.controller.SceneStyler;
import com.helegris.szorengeteg.messages.Messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class PracticeAllStartView extends StackPane {

    public static final String FXML = "fxml/practice_all_start.fxml";

    @FXML
    private Button btnPractice;

    @SuppressWarnings("LeakingThisInConstructor")
    public PracticeAllStartView() {
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    private void initialize() {
        btnPractice.setOnAction(this::startPractice);
    }

    private void startPractice(ActionEvent event) {
        Stage stage = new Stage();
        stage.setScene(new SceneStyler().createScene(
                new PracticeView(new PracticeSession())));
        stage.setTitle(Messages.msg("practice"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btnPractice.getScene().getWindow());
        stage.setResizable(false);
        stage.showAndWait();
    }
}

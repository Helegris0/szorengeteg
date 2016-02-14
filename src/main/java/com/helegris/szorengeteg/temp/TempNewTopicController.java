/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.temp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;

/**
 *
 * @author Timi
 */
public class TempNewTopicController implements Initializable {

    @FXML
    private TableView<Word> tableView;
    @FXML
    private Menu newTopic;
    private final ObservableList<Word> data =
            FXCollections.observableArrayList(
                    new Word("ebédel", "ebédet fogyaszt, eszik valamit"),
                    new Word("falánk", "sokat, mohón és válogatás nélkül evő, ilyen természetű"),
                    new Word("citrom", "sárga héjú, savanyú déli gyümölcs"),
                    new Word("rántotta", "fölvert és forró zsiradékban kisütött tojás"),
                    new Word("cukor", "cukorrépából v. cukornádból gyártott, fehér, kristályos édesítőszer"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setContent();
    }

    private void setContent() {
        tableView.setItems(data);
    }

}

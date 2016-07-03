/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Timi
 */
public class HelpView extends AnchorPane {

    private static final String FXML = "fxml/help.fxml";

    private static final String GUIDE_PATH = "src/main/resources/text/user_guide_utf8.txt";
    private static final String CHARSET = "utf-8";

    @FXML
    private TextArea txtContent;
    @FXML
    private Button btnClose;

    @SuppressWarnings("LeakingThisInConstructor")
    public HelpView() {
        FXMLLoaderHelper.load(FXML, this);
        try {
            String content = FileUtils.readFileToString(new File(GUIDE_PATH), CHARSET);
            txtContent.setText(content);
            setSize();
        } catch (IOException ex) {
            Logger.getLogger(HelpView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void initialize() {
        btnClose.setOnAction(event -> ((Stage) this.getScene().getWindow()).close());
    }

    private void setSize() {
        Platform.runLater(() -> {
            this.prefWidthProperty().bind(this.getScene().widthProperty());
            txtContent.prefWidthProperty().bind(this.prefWidthProperty());
            this.prefHeightProperty().bind(this.getScene().heightProperty());
            txtContent.prefHeightProperty()
                    .bind(this.prefHeightProperty()
                            .subtract(btnClose.heightProperty()));
        });
    }
}

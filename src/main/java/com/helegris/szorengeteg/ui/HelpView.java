/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.HelpControl.ContentType;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;

/**
 * A pane containing a user guide text.
 *
 * @author Timi
 */
public class HelpView extends AnchorPane {

    private static final String FXML = "fxml/help.fxml";

    private static final String PRACTICE_GUIDE_PATH
            = "/text/user_guide_practice.txt";
    private static final String EDITOR_GUIDE_PATH
            = "/text/user_guide_editor.txt";
    private static final String CHARSET = "utf-8";

    @FXML
    private TextArea txtContent;
    @FXML
    private Button btnClose;

    @SuppressWarnings("LeakingThisInConstructor")
    public HelpView(ContentType type) {
        FXMLLoaderHelper.load(FXML, this);
        String path = null;
        switch (type) {
            case PRACTICE:
                path = PRACTICE_GUIDE_PATH;
                break;
            case TOPIC_LIST:
                path = EDITOR_GUIDE_PATH;
                break;
        }
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            String content = IOUtils.toString(inputStream, CHARSET);
            txtContent.setText(content);
        } catch (IOException ex) {
            Logger.getLogger(HelpView.class.getName()).log(Level.SEVERE, null, ex);
        }
        setSize();
    }

    @FXML
    private void initialize() {
        btnClose.setOnAction(event -> ((Stage) this.getScene().getWindow()).close());
    }

    /**
     * Binds pane size to content size.
     */
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

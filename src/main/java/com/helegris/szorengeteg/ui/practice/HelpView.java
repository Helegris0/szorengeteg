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
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Timi
 */
public class HelpView extends AnchorPane {

    private static final String FXML = "fxml/help.fxml";

    @FXML
    private TextArea txtContent;

    @SuppressWarnings("LeakingThisInConstructor")
    public HelpView() {
        FXMLLoaderHelper.load(FXML, this);
        try {
            String content = FileUtils.readFileToString(new File("src/main/resources/text/user_guide_utf8.txt"), "utf-8");
            txtContent.setText(content);
        } catch (IOException ex) {
            Logger.getLogger(HelpView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void initalize() {
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.messages.Messages;
import java.io.File;
import javafx.stage.FileChooser;

/**
 *
 * @author Timi
 */
public class FileChooserHelper {

    private static final String DEFAULT_PATH = System.getProperty("user.home");
    private static String imageDirectory = DEFAULT_PATH;
    private static String txtDirectory = DEFAULT_PATH;

    public static File getImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(imageDirectory));

        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.images"), 
                        "*.JPG", "*.PNG", "*.GIF");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageDirectory = file.getParent();
        }
        return file;
    }

    public static File getCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(txtDirectory));

        FileChooser.ExtensionFilter extFilterTxt
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.txt_files"), "*.TXT");
        FileChooser.ExtensionFilter extFilterCsv
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.csv_files"), "*.CSV");
        fileChooser.getExtensionFilters().addAll(extFilterTxt, extFilterCsv);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            txtDirectory = file.getParent();
        }
        return file;
    }
}

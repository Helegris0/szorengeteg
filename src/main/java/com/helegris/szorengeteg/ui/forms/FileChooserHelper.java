/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.messages.Messages;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author Timi
 */
public class FileChooserHelper {

    private static final String DEFAULT_PATH = System.getProperty("user.home");
    private static String imageDirectory = DEFAULT_PATH;
    private static String audioDirectory = DEFAULT_PATH;
    private static String txtDirectory = DEFAULT_PATH;

    public static File getImageFile(Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(imageDirectory));

        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.images"), 
                        "*.jpg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(ownerWindow);
        if (file != null) {
            imageDirectory = file.getParent();
        }
        return file;
    }

    public static File getAudioFile(Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(audioDirectory));

        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.audio_files"), 
                        "*.mp3", "*.wav", "*.aac");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(ownerWindow);
        if (file != null) {
            audioDirectory = file.getParent();
        }
        return file;
    }

    public static File getCsvFile(Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(txtDirectory));

        FileChooser.ExtensionFilter extFilterTxt
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.txt_files"), "*.txt");
        FileChooser.ExtensionFilter extFilterCsv
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.csv_files"), "*.csv");
        fileChooser.getExtensionFilters().addAll(extFilterTxt, extFilterCsv);

        File file = fileChooser.showOpenDialog(ownerWindow);
        if (file != null) {
            txtDirectory = file.getParent();
        }
        return file;
    }
}

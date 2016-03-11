/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import com.helegris.szorengeteg.messages.Messages;
import java.io.File;
import javafx.stage.FileChooser;

/**
 *
 * @author Timi
 */
public class FileChooserHelper {

    private static final String defaultPath = System.getProperty("user.home");
    private static String imageDirectory = defaultPath;
    private static String txtDirectory = defaultPath;

    public static File getImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(imageDirectory));

        FileChooser.ExtensionFilter extFilterJpg
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.jpg_files"), "*.JPG");
        FileChooser.ExtensionFilter extFilterPng
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.png_files"), "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJpg, extFilterPng);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageDirectory = file.getParent();
        }
        return file;
    }

    public static File getTxtFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(txtDirectory));

        FileChooser.ExtensionFilter extFilterTxt
                = new FileChooser.ExtensionFilter(
                        Messages.msg("open_dialog.txt_files"), "*.TXT");
        fileChooser.getExtensionFilters().add(extFilterTxt);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            txtDirectory = file.getParent();
        }
        return file;
    }
}

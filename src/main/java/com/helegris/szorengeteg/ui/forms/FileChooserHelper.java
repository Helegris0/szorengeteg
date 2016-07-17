/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.forms;

import com.helegris.szorengeteg.messages.Messages;
import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author Timi
 */
public class FileChooserHelper {

    private static final FileChooser.ExtensionFilter IMAGE_EXT
            = new FileChooser.ExtensionFilter(Messages.msg("open_dialog.images"),
                    "*.jpg", "*.png", "*.gif");
    private static final FileChooser.ExtensionFilter AUDIO_EXT
            = new FileChooser.ExtensionFilter(Messages.msg("open_dialog.audio_files"),
                    "*.mp3", "*.wav", "*.aac");
    private static final String DEFAULT_PATH = System.getProperty("user.home");
    private static String imageDirectory = DEFAULT_PATH;
    private static String audioDirectory = DEFAULT_PATH;

    public File getImageFile(Window ownerWindow) {
        File file = getFileChooser(imageDirectory, IMAGE_EXT)
                .showOpenDialog(ownerWindow);
        if (file != null) {
            imageDirectory = file.getParent();
        }
        return file;
    }

    public List<File> getImageFiles(Window ownerWindow) {
        List<File> files = getFileChooser(imageDirectory, IMAGE_EXT)
                .showOpenMultipleDialog(ownerWindow);
        if (files != null && !files.isEmpty()) {
            imageDirectory = files.get(0).getParent();
        }
        return files;
    }

    public File getAudioFile(Window ownerWindow) {
        File file = getFileChooser(audioDirectory, AUDIO_EXT)
                .showOpenDialog(ownerWindow);
        if (file != null) {
            audioDirectory = file.getParent();
        }
        return file;
    }

    public List<File> getAudioFiles(Window ownerWindow) {
        List<File> files = getFileChooser(audioDirectory, AUDIO_EXT)
                .showOpenMultipleDialog(ownerWindow);
        if (files != null && !files.isEmpty()) {
            audioDirectory = files.get(0).getParent();
        }
        return files;
    }

    private FileChooser getFileChooser(String directory, 
            FileChooser.ExtensionFilter extensionFilter) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(directory));
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser;
    }
}

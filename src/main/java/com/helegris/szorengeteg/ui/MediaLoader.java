/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Timi
 */
public class MediaLoader {

    /**
     * Creates JavaFX image from a byte array.
     *
     * @param imageBytes
     * @return image
     */
    public Image loadImage(byte[] imageBytes) {
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        Image image = new Image(inputStream);
        return image;
    }

    /**
     * Creates playable JavaFX audio from a byte array. To achieve this, it
     * creates and deletes a temporary file.
     *
     * @param audioBytes
     * @return audio
     * @throws IOException
     */
    public Media loadAudio(byte[] audioBytes) throws IOException {
        File tempFile = File.createTempFile("audio", null);
        tempFile.deleteOnExit();
        IOUtils.write(audioBytes, new FileOutputStream(tempFile));
        Media media = new Media(tempFile.toURI().toString());
        return media;
    }

    /**
     * Turns a file into a btye array.
     *
     * @param file
     * @return byte array
     * @throws NotFoundException exception containing file information
     */
    public byte[] loadBytes(File file) throws NotFoundException {
        try {
            return IOUtils.toByteArray(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new NotFoundException(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

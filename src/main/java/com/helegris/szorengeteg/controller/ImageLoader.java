/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Timi
 */
public class ImageLoader {

    public Image loadImage(byte[] imageBytes) {
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        Image image = new Image(inputStream);
        return image;
    }

    public byte[] loadImage(File imageFile) throws ImageNotFoundException {
        try {
            return IOUtils.toByteArray(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            throw new ImageNotFoundException(imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

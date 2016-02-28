/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.scene.image.Image;

/**
 *
 * @author Timi
 */
public class ImageUtils {

    public static Image loadImage(byte[] imageBytes) {
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        Image image = new Image(inputStream);
        return image;
    }
}

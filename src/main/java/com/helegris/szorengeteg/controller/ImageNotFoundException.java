package com.helegris.szorengeteg.controller;

import java.io.File;

/**
 *
 * @author Timi
 */
public class ImageNotFoundException extends RuntimeException {

    private final File imageFile;

    public ImageNotFoundException(File imageFile) {
        this.imageFile = imageFile;
    }

    public File getImageFile() {
        return imageFile;
    }
}

package com.helegris.szorengeteg.ui;

import java.io.File;

/**
 *
 * @author Timi
 */
public class NotFoundException extends RuntimeException {

    private final File file;

    public NotFoundException(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}

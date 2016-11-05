package com.helegris.szorengeteg.ui;

import java.io.File;

/**
 * A custom exception to be used when a file is not found. It stores the file
 * path.
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

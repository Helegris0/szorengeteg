/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import com.helegris.szorengeteg.messages.Messages;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author Timi
 */
public class FXMLLoaderHelper {

    private static FXMLLoader FXML_LOADER = new FXMLLoader();

    /**
     * Loads an fxml layout and sets an object as its root and controller.
     *
     * @param fxml fxml file path
     * @param dest controller
     */
    public static void load(String fxml, Object dest) {
        InputStream fxmlInputStream = getFxmlInputStream(fxml);

        FXML_LOADER.setResources(Messages.RESOURCE_BUNDLE);
        FXML_LOADER.setRoot(dest);
        FXML_LOADER.setController(dest);

        try {
            FXML_LOADER.load(fxmlInputStream);
        } catch (IOException ex) {
            throw new FXMLLoadException(ex);
        }
    }

    private static InputStream getFxmlInputStream(String fxml)
            throws FXMLLoadException {
        final InputStream fxmlInputStream = getResource(fxml);
        if (fxmlInputStream == null) {
            throw new FXMLLoadException("FXML '" + fxml + "' not found");
        }
        return fxmlInputStream;
    }

    private static InputStream getResource(String fxml) {
        return FXMLLoaderHelper.class.getClassLoader().getResourceAsStream(fxml);
    }

    private static class FXMLLoadException extends RuntimeException {

        public FXMLLoadException() {
        }

        public FXMLLoadException(String message) {
            super(message);
        }

        public FXMLLoadException(String message, Throwable cause) {
            super(message, cause);
        }

        public FXMLLoadException(Throwable cause) {
            super(cause);
        }
    }
}

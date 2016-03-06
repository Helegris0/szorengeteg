/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.component;

import java.io.InputStream;
import javafx.scene.image.Image;

/**
 *
 * @author Timi
 */
public class DefaultImage extends Image {

    private static final String PATH = "/images/default.jpg";

    private static final DefaultImage INSTANCE = new DefaultImage(
            new PathFinder().getInputStream()
    );

    private DefaultImage(InputStream is) {
        super(is);
    }

    public static DefaultImage getInstance() {
        return INSTANCE;
    }

    private static class PathFinder {

        private InputStream getInputStream() {
            return this.getClass().getResourceAsStream(PATH);
        }
    }

}
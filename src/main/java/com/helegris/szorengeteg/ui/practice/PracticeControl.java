/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Icon with label indicating action.
 *
 * @author Timi
 */
public final class PracticeControl {

    private static final String PATH_CORE = "/images/triangle";
    private static final String USED_PATH = "_used";
    private static final String UNUSED_PATH = "_unused";
    private static final String EXT = ".png";

    private static final int IMG_SIZE = 38;

    private final ImageView imageView;
    private final Label label;

    private final Image imageUsed;
    private final Image imageUnused;

    private final Runnable function;

    private boolean used;
    private boolean enabled;

    public PracticeControl(Direction direction, ImageView imageView,
            Label label, Runnable function) {
        this.imageView = imageView;
        imageView.setFitWidth(IMG_SIZE);
        imageView.setFitHeight(IMG_SIZE);

        this.label = label;

        imageUsed = new Image(PATH_CORE + direction.path + USED_PATH + EXT);
        imageUnused = new Image(PATH_CORE + direction.path + UNUSED_PATH + EXT);

        this.function = function;
        imageView.setOnMouseClicked(this::action);

        setUsed(false);
        setEnabled(true);
    }

    /**
     * Four directions in which the triangle can point. Contains a fragment of
     * the corresponding image's file name.
     */
    public enum Direction {

        UP("_up"),
        RIGHT("_right"),
        DOWN("_down"),
        LEFT("_left");

        private final String path;

        private Direction(String path) {
            this.path = path;
        }
    }

    /**
     * If enabled, runs the function. Called when the icon is clicked.
     *
     * @param event
     */
    private void action(MouseEvent event) {
        if (enabled && function != null) {
            function.run();
        }
    }

    public void useAndDisable() {
        setUsed(true);
        setEnabled(false);
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
        imageView.setImage(used ? imageUsed : imageUnused);
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (label != null) {
            this.label.setDisable(!enabled);
            if (!enabled) {
                label.setStyle("-fx-font-weight: regular;-fx-opacity: 1.0;");
            }
        }
    }
}

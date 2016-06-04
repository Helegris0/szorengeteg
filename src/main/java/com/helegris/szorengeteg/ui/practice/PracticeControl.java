/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.practice;

import com.helegris.szorengeteg.ui.ClickableLabel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Timi
 */
public class PracticeControl {

    private static final String PATH_CORE = "/images/triangle";
    private static final String DISABLED = "_disabled";
    private static final String EXT = ".png";

    private final ImageView imageView;
    private final ClickableLabel label;

    private final Image image;
    private final Image imageLast;
    private final Image imageNow;
    private final Image imageDisabled;
    private final Image imageLastDisabled;
    private final Image imageNowDisabled;

    private final Runnable function;

    private boolean usedLast;
    private boolean usedNow;
    private boolean enabled;

    public PracticeControl(Direction direction, ImageView imageView,
            ClickableLabel label, Runnable function) {
        this.imageView = imageView;
        this.label = label;

        String pathCore = PATH_CORE + direction.path;

        String coreStandard = pathCore + Color.GRAY.path;
        image = new Image(coreStandard + EXT);
        imageDisabled = new Image(coreStandard + DISABLED + EXT);

        String coreNow = pathCore + Color.BLUE.path;
        imageNow = new Image(coreNow + EXT);
        imageNowDisabled = new Image(coreNow + DISABLED + EXT);

        String coreLast = pathCore + Color.RED.path;
        imageLast = new Image(coreLast + EXT);
        imageLastDisabled = new Image(coreLast + DISABLED + EXT);

        imageView.setOnMouseEntered(e -> {
            if (enabled) {
                label.setUnderline(true);
            }
        });
        imageView.setOnMouseExited(e -> label.setUnderline(false));

        this.function = function;
        imageView.setOnMouseClicked(this::action);
        label.setOnMouseClicked(this::action);
        label.setStyle("-fx-opacity: 1.0;");

        setEnabled(true);
    }

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

    private enum Color {

        GRAY("_gray"),
        BLUE("_blue"),
        RED("_red");

        private final String path;

        private Color(String path) {
            this.path = path;
        }
    }

    private void action(MouseEvent event) {
        if (enabled && function != null) {
            function.run();
        }
    }

    private void updateImage() {
        if (usedNow) {
            imageView.setImage(enabled ? imageNow : imageNowDisabled);
        } else if (usedLast) {
            imageView.setImage(enabled ? imageLast : imageLastDisabled);
        } else {
            imageView.setImage(enabled ? image : imageDisabled);
        }
    }

    public void use() {
        usedNow = true;
        updateImage();
    }

    public void useAndDisable() {
        usedNow = true;
        setEnabled(false);
    }

    public void resetNow() {
        usedNow = false;
        updateImage();
    }

    public void setUsedLast(boolean usedLast) {
        this.usedLast = usedLast;
        resetNow();
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.label.setDisable(!enabled);
        updateImage();
    }

    public void setVisible(boolean visible) {
        imageView.setVisible(visible);
        label.setVisible(visible);
    }
}

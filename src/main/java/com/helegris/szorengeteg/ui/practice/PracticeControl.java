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

    private static final Image L_GRAY = new Image("/images/triangle_left_gray.png");
    private static final Image R_GRAY = new Image("/images/triangle_right_gray.png");
    private static final Image L_DIS = new Image("/images/triangle_left_disabled.png");
    private static final Image R_DIS = new Image("/images/triangle_right_disabled.png");

    private static final int IMAGE_SIZE = 50;

    private final ImageView imageView;
    private final ClickableLabel label;

    private final Image imgEnabled;
    private final Image imgDisabled;

    private final Runnable function;
    private boolean enabled;

    public PracticeControl(Direction direction, ImageView imageView,
            ClickableLabel label, Runnable function, boolean enable) {
        switch (direction) {
            case LEFT:
                imgEnabled = L_GRAY;
                imgDisabled = L_DIS;
                break;
            case RIGHT:
                imgEnabled = R_GRAY;
                imgDisabled = R_DIS;
                break;
            default:
                imgEnabled = R_GRAY;
                imgDisabled = R_DIS;
        }

        this.imageView = imageView;
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);

        this.label = label;

        this.function = function;
        imageView.setOnMouseClicked(this::action);
        label.setOnMouseClicked(this::action);

        setEnabled(enable);
    }

    public enum Direction {

        LEFT,
        RIGHT
    }

    private void action(MouseEvent event) {
        if (enabled && function != null) {
            function.run();
        }
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        Image image = enabled ? imgEnabled : imgDisabled;
        imageView.setImage(image);
        label.setDisable(!enabled);
    }
}

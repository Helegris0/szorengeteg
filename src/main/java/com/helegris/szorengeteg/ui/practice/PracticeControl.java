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

    private static final Image U_GRAY = new Image("/images/triangle_up_gray.png");
    private static final Image U_RED = new Image("/images/triangle_up_red.png");
    private static final Image U_GRAY_DIS = new Image("/images/triangle_up_gray_disabled.png");
    private static final Image U_RED_DIS = new Image("/images/triangle_up_red_disabled.png");
    private static final Image R_GRAY = new Image("/images/triangle_right_gray.png");
    private static final Image R_RED = new Image("/images/triangle_right_red.png");
    private static final Image R_GRAY_DIS = new Image("/images/triangle_right_gray_disabled.png");
    private static final Image R_RED_DIS = new Image("/images/triangle_right_red_disabled.png");
    private static final Image D_GRAY = new Image("/images/triangle_down_gray.png");
    private static final Image D_RED = new Image("/images/triangle_down_red.png");
    private static final Image D_GRAY_DIS = new Image("/images/triangle_down_gray_disabled.png");
    private static final Image D_RED_DIS = new Image("/images/triangle_down_red_disabled.png");
    private static final Image L_GRAY = new Image("/images/triangle_left_gray.png");
    private static final Image L_RED = new Image("/images/triangle_left_red.png");
    private static final Image L_GRAY_DIS = new Image("/images/triangle_left_gray_disabled.png");
    private static final Image L_RED_DIS = new Image("/images/triangle_left_red_disabled.png");

    private final ImageView imageView;
    private final ClickableLabel label;

    private final Image image;
    private final Image imageUsed;
    private final Image imageDisabled;
    private final Image imageUsedDisabled;

    private final Runnable function;

    private boolean used;
    private boolean enabled;

    public PracticeControl(Direction direction, ImageView imageView,
            ClickableLabel label, Runnable function) {
        this.imageView = imageView;
        this.label = label;

        switch (direction) {
            case UP:
                image = U_GRAY;
                imageUsed = U_RED;
                imageDisabled = U_GRAY_DIS;
                imageUsedDisabled = U_RED_DIS;
                break;
            case RIGHT:
                image = R_GRAY;
                imageUsed = R_RED;
                imageDisabled = R_GRAY_DIS;
                imageUsedDisabled = R_RED_DIS;
                break;
            case DOWN:
                image = D_GRAY;
                imageUsed = D_RED;
                imageDisabled = D_GRAY_DIS;
                imageUsedDisabled = D_RED_DIS;
                break;
            case LEFT:
                image = L_GRAY;
                imageUsed = L_RED;
                imageDisabled = L_GRAY_DIS;
                imageUsedDisabled = L_RED_DIS;
                break;
            default:
                image = R_GRAY;
                imageUsed = R_RED;
                imageDisabled = R_GRAY_DIS;
                imageUsedDisabled = R_RED_DIS;
        }

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

        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    private void action(MouseEvent event) {
        if (enabled && function != null) {
            function.run();
        }
    }

    private void updateImage() {
        if (enabled) {
            imageView.setImage(used ? imageUsed : image);
        } else {
            imageView.setImage(used ? imageUsedDisabled : imageDisabled);
        }
    }

    public void use() {
        used = true;
        setEnabled(false);
    }

    public void setUsed(boolean used) {
        this.used = used;
        updateImage();
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

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

    private Image imgEnabled;
    private Image imgDisabled;

    private final Direction direction;
    private final Runnable function;
    private boolean enabled;

    public PracticeControl(Direction direction, ImageView imageView,
            ClickableLabel label, Runnable function, boolean enable, boolean special) {
        this.direction = direction;
        updateSpecial(special);

        this.imageView = imageView;
        this.label = label;

        imageView.setOnMouseEntered(e -> {
            if (enabled) {
                label.setUnderline(true);
            }
        });
        imageView.setOnMouseExited(e -> {
            if (enabled) {
                label.setUnderline(false);
            }
        });

        this.function = function;
        imageView.setOnMouseClicked(this::action);
        label.setOnMouseClicked(this::action);

        setEnabled(enable);
    }

    public void updateSpecial(boolean special) {
        if (special) {
            switch (direction) {
                case UP:
                    imgEnabled = U_RED;
                    imgDisabled = U_RED_DIS;
                    break;
                case RIGHT:
                    imgEnabled = R_RED;
                    imgDisabled = R_RED_DIS;
                    break;
                case DOWN:
                    imgEnabled = D_RED;
                    imgDisabled = D_RED_DIS;
                    break;
                case LEFT:
                    imgEnabled = L_RED;
                    imgDisabled = L_RED_DIS;
                    break;
                default:
                    imgEnabled = R_RED;
                    imgDisabled = R_RED_DIS;
            }
        } else {
            switch (direction) {
                case UP:
                    imgEnabled = U_GRAY;
                    imgDisabled = U_GRAY_DIS;
                    break;
                case RIGHT:
                    imgEnabled = R_GRAY;
                    imgDisabled = R_GRAY_DIS;
                    break;
                case DOWN:
                    imgEnabled = D_GRAY;
                    imgDisabled = D_GRAY_DIS;
                    break;
                case LEFT:
                    imgEnabled = L_GRAY;
                    imgDisabled = L_GRAY_DIS;
                    break;
                default:
                    imgEnabled = R_GRAY;
                    imgDisabled = R_GRAY_DIS;
            }
        }
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

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        Image image = enabled ? imgEnabled : imgDisabled;
        imageView.setImage(image);
        label.setDisable(!enabled);
    }
}

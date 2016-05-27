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
import javafx.scene.layout.HBox;

/**
 *
 * @author Timi
 */
public class PracticeLabel extends HBox {

    private static final Image L_GRAY = new Image("/images/triangle_left_gray.png");
    private static final Image R_GRAY = new Image("/images/triangle_right_gray.png");
    private static final Image L_DIS = new Image("/images/triangle_left_disabled.png");
    private static final Image R_DIS = new Image("/images/triangle_right_gray.png");

    private static final int IMAGE_SIZE = 50;

    private final ImageView imageView = new ImageView();
    private final ClickableLabel label = new ClickableLabel();

    private Image imgEnabled;
    private Image imgDisabled;

    private Runnable function;
    private boolean enabled;

    public PracticeLabel() {
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);
    }

    public void setUp(Direction direction, String text, boolean enable) {
        label.setText(text);
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
        setEnabled(enable);
    }

    public void setUp(Direction direction, String text) {
        setUp(direction, text, false);
    }

    public enum Direction {

        LEFT,
        RIGHT
    }

    public void setAction(Runnable function) {
        this.function = function;
        imageView.setOnMouseClicked(this::action);
        label.setOnMouseClicked(this::action);
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

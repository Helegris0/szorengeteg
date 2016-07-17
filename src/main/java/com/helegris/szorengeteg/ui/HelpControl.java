/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import com.helegris.szorengeteg.messages.Messages;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Timi
 */
public class HelpControl extends HBox {

    private static final String IMAGE_PATH = "/images/help.png";
    private static final int IMAGE_SIZE = 64;

    private final ClickableLabel label;
    private final ImageView imageView;

    private ContentType contentType;

    private boolean asd;

    public HelpControl() {
        super();
        this.label = new ClickableLabel("Súgó");
        this.label.setStyle("fx-font-size: 23pt;");
        this.imageView = new ImageView(IMAGE_PATH);
        this.imageView.setFitWidth(IMAGE_SIZE);
        this.imageView.setFitHeight(IMAGE_SIZE);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(label);
        this.getChildren().add(imageView);
        this.setAlignment(Pos.CENTER);
        setEvents();
    }

    private void setEvents() {
        label.setOnMouseClicked(this::action);
        imageView.setOnMouseClicked(this::action);
        imageView.setOnMouseEntered(e -> label.setUnderline(true));
        imageView.setOnMouseExited(e -> label.setUnderline(false));
    }

    public enum ContentType {

        PRACTICE,
        EDITOR
    }

    private void action(MouseEvent event) {
        if (!asd) {
            Stage helpStage = new Stage();
        HelpView helpView = new HelpView();
        helpStage.setScene(new SceneStyler().createScene(
                helpView, SceneStyler.Style.MAIN));
        helpStage.setTitle(Messages.msg("common.help"));
        helpStage.showAndWait();
        }
    }

    public void setAsd(boolean asd) {
        this.asd = asd;
    }
}

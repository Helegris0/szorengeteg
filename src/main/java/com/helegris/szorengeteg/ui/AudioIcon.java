/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import com.helegris.szorengeteg.business.model.Card;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Timi
 */
public final class AudioIcon extends ImageView {

    public static final String AUDIO = "/images/audio.png";
    public static final String NO_AUDIO = "/images/no_audio.png";

    private final Image imgDark = new Image(AUDIO);
    private final Image imgLight = new Image(NO_AUDIO);

    private Image actualImage;
    private Media audio;

    public AudioIcon() {
        setAudio(null);
        this.setOnMouseEntered(this::mouseEntered);
        this.setOnMouseExited(this::mouseExited);
        this.setOnMouseClicked(this::mouseClicked);
    }

    public AudioIcon(Media audio) {
        this();
        setAudio(audio);
    }

    private void mouseEntered(MouseEvent event) {
        this.setImage(imgLight);
    }

    private void mouseExited(MouseEvent event) {
        this.setImage(actualImage);
    }

    private void mouseClicked(MouseEvent event) {
        if (audio != null) {
            MediaPlayer player = new MediaPlayer(audio);
            player.play();
        }
    }

    public final void setCard(Card card) {
        try {
            byte[] audioBytes = card.getAudio();
            if (audioBytes != null) {
                setAudio(new MediaLoader().loadAudio(audioBytes));
            } else {
                setAudio(null);
            }
        } catch (IOException ex) {
            Logger.getLogger(AudioIcon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Media getAudio() {
        return audio;
    }

    public void setAudio(Media audio) {
        this.audio = audio;
        if (audio != null) {
            actualImage = imgDark;
        } else {
            actualImage = imgLight;
        }
        this.setImage(actualImage);
    }
}

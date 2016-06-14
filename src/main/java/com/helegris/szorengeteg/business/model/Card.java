/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.business.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Timi
 */
@Entity
@Table(name = "CARDS")
public class Card extends PersistentObject {

    @NotNull
    @Column(name = "word")
    private String word;

    @NotNull
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "ordinal")
    private Integer ordinal;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Lob
    @Column(name = "audio")
    private byte[] audio;

    @Column(name = "last_input")
    private boolean lastInput;

    @Column(name = "last_help")
    private boolean lastHelp;

    @Column(name = "last_visual")
    private boolean lastVisual;

    @Column(name = "last_gave_up")
    private boolean lastGaveUp;

    public Card() {
    }

    public Card(String word, String description) {
        this.word = word;
        this.description = description;
    }

    @Override
    public String toString() {
        return word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    public boolean isLastInput() {
        return lastInput;
    }

    public void setLastInput(boolean lastInput) {
        this.lastInput = lastInput;
    }

    public boolean isLastHelp() {
        return lastHelp;
    }

    public void setLastHelp(boolean lastHelp) {
        this.lastHelp = lastHelp;
    }

    public boolean isLastVisual() {
        return lastVisual;
    }

    public void setLastVisual(boolean lastVisual) {
        this.lastVisual = lastVisual;
    }

    public boolean isLastGaveUp() {
        return lastGaveUp;
    }

    public void setLastGaveUp(boolean lastGaveUp) {
        this.lastGaveUp = lastGaveUp;
    }
}

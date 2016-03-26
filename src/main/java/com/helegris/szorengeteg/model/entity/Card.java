/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "factor")
    private double factor;

    @Column(name = "last_time")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastTime;

    @Column(name = "due_time")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueTime;

    @Column(name = "rep_interval")
    private int repInterval;

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public int getRepInterval() {
        return repInterval;
    }

    public void setRepInterval(int repInterval) {
        this.repInterval = repInterval;
    }

}

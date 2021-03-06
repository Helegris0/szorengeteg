/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.business.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Timi
 */
@Entity
@Table(name = "TOPICS")
public class Topic extends PersistentObject {

    @NotNull
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "ordinal")
    private Integer ordinal;

    public Topic() {
    }

    public Topic(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ordinal + ". " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }
}

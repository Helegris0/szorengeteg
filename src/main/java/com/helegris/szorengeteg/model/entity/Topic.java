/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.model.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "topic")
    private List<Card> cards = new ArrayList<>();

    @Override
    public String toString() {
        return "Topic{" + "id=" + id + ", name=" + name + '}';
    }

    public void addCard(Card card) {
        if (!cards.contains(card)) {
            cards.add(card);
            card.setTopic(this);
        }
    }

    public void removeCard(Card card) {
        if (cards.contains(card)) {
            cards.remove(card);
        }
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}

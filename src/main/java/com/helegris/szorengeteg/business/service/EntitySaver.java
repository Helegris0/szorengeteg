/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.business.service;

import com.google.inject.persist.Transactional;
import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.PersistentObject;
import com.helegris.szorengeteg.business.model.Topic;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * A class for CRUD and persistence.
 *
 * @author Timi
 */
public class EntitySaver {

    @Inject
    private EntityManager em;
    @Inject
    private CardLoader cardLoader;

    @Transactional
    public void createTopic(Topic topic, List<Card> cards) {
        cards.stream().forEach(this::save);
        em.persist(topic);
    }

    /**
     * Saves a topic and its list of cards. If there are cards in the database
     * that don't have equivalents in the list, it deletes them.
     *
     * @param topic
     * @param cards
     */
    @Transactional
    public void saveTopic(Topic topic, List<Card> cards) {
        cards.stream().forEach(this::save);
        deleteIf(cardLoader.loadByTopic(topic), card -> !cards.contains(card));
        em.merge(topic);
    }

    @Transactional
    public void saveTopics(List<Topic> topics) {
        topics.stream().forEach(this::save);
    }

    @Transactional
    public void saveCard(Card card) {
        save(card);
    }

    @Transactional
    public void saveCards(List<Card> cards) {
        cards.stream().forEach(this::save);
    }

    /**
     * Deletes all the elements of a collection.
     *
     * @param entities
     */
    @Transactional
    public void delete(Collection<? extends PersistentObject> entities) {
        entities.stream().forEach(em::remove);
    }

    /**
     * Creates a record or updates it if it already exists.
     *
     * @param entity
     */
    private void save(PersistentObject entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    /**
     * Deletes a collection's elements that meet a given condition.
     *
     * @param <T> type of elements
     * @param entities collection to iterate through
     * @param filter condition
     */
    private <T extends PersistentObject> void deleteIf(Collection<T> entities,
            Predicate<T> filter) {
        entities.stream().filter(filter).forEach(em::remove);
    }
}

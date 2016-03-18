/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.model;

import com.google.inject.persist.Transactional;
import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.PersistentObject;
import com.helegris.szorengeteg.model.entity.Topic;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
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

    @Transactional
    public void saveTopic(Topic topic, List<Card> cards) {
        cards.stream().forEach(this::save);
        delete(cardLoader.loadByTopic(topic), card -> !cards.contains(card));
        em.merge(topic);
    }

    @Transactional
    public void saveWords(List<Card> cards) {
        cards.stream().forEach(this::save);
        delete(cardLoader.loadAll(), card -> !cards.contains(card));
    }

    @Transactional
    public void delete(Collection<? extends PersistentObject> entities) {
        entities.stream().forEach(em::remove);
    }

    @Transactional
    public void deleteTopicWithoutWords(Topic topic) {
        cardLoader.loadByTopic(topic).stream().forEach(card -> {
            card.setTopic(null);
            em.merge(card);
        });
        em.remove(topic);
    }

    private void save(PersistentObject entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    private <T extends PersistentObject> void delete(Collection<T> entities,
            Predicate<T> filter) {
        entities.stream().filter(filter).forEach(em::remove);
    }

}

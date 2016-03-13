/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.model;

import com.helegris.szorengeteg.model.entity.Card;
import com.helegris.szorengeteg.model.entity.PersistentObject;
import com.helegris.szorengeteg.model.entity.Topic;
import com.helegris.szorengeteg.transaction.Transactional;
import java.util.Collection;
import java.util.List;
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

    private void createOrUpdate(Collection<? extends PersistentObject> entities) {
        entities.stream().forEach(entity -> {
            if (entity.getId() == null) {
                em.persist(entity);
            } else {
                em.merge(entity);
            }
        });
    }

    private void compareAndDelete(
            Collection<? extends PersistentObject> entitiesOriginal,
            Collection<? extends PersistentObject> entitesNew) {
        entitiesOriginal.stream().forEach(entity -> {
            if (!entitesNew.contains(entity)) {
                em.remove(entity);
            }
        });
    }

    @Transactional
    public void delete(Collection<? extends PersistentObject> entities) {
        entities.stream().forEach(em::remove);
    }

    @Transactional
    public void deleteTopicButSaveItsWords(Topic topic) {
        cardLoader.loadByTopic(topic).stream().forEach(card -> {
            card.setTopic(null);
            em.merge(card);
        });
        em.remove(topic);
    }

    @Transactional
    public void saveTopic(Topic topic, List<Card> cards) {
        createOrUpdate(cards);
        compareAndDelete(cardLoader.loadByTopic(topic), cards);
    }

    @Transactional
    public void saveWords(List<Card> cards) {
        createOrUpdate(cards);
        compareAndDelete(cardLoader.loadAll(), cards);
    }

}

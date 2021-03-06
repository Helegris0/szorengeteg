/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.business.service;

import com.helegris.szorengeteg.business.model.Card;
import com.helegris.szorengeteg.business.model.Topic;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Timi
 */
public class CardLoader extends EntityLoader<Card> {

    @Override
    protected Class<Card> getEntityClass() {
        return Card.class;
    }

    /**
     * Loads all cards from the database that are in a given topic.
     *
     * @param topic
     * @return card list
     */
    public List<Card> loadByTopic(Topic topic) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Card> cq = cb.createQuery(getEntityClass());
        Root<Card> card = cq.from(getEntityClass());
        cq.select(card).where(cb.equal(card.get("topic"), topic));
        TypedQuery<Card> q = em.createQuery(cq);
        return q.getResultList();
    }
}

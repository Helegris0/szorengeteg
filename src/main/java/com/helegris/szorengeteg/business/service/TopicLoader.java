/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.business.service;

import com.helegris.szorengeteg.business.model.Topic;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Timi
 */
public class TopicLoader extends EntityLoader<Topic> {

    @Override
    protected Class<Topic> getEntityClass() {
        return Topic.class;
    }

    public List<Topic> loadAllOrdered() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Topic> cq = cb.createQuery(getEntityClass());
        Root<Topic> topic = cq.from(getEntityClass());
        cq.select(topic);
        cq.orderBy(cb.asc(topic.get("ordinal")));
        TypedQuery<Topic> q = em.createQuery(cq);
        return q.getResultList();
    }

    public Topic loadByOrdinal(int ordinal) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Topic> cq = cb.createQuery(getEntityClass());
        Root<Topic> topic = cq.from(getEntityClass());
        cq.where(cb.equal(topic.get("ordinal"), ordinal));
        cq.select(topic);
        TypedQuery<Topic> q = em.createQuery(cq);
        try {
            Topic result = q.getSingleResult();
            return result;
        } catch (NoResultException ex) {
            return null;
        }
    }
}

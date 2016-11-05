/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.business.service;

import com.helegris.szorengeteg.business.model.PersistentObject;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Timi
 * @param <T> type of entity to be loaded
 */
public abstract class EntityLoader<T extends PersistentObject>
        implements Serializable {

    @Inject
    protected EntityManager em;

    /**
     * Loads all instances of a type stored in database.
     *
     * @return result list
     */
    public List<T> loadAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> entity = cq.from(getEntityClass());
        cq.select(entity);
        TypedQuery<T> q = em.createQuery(cq);
        return q.getResultList();
    }

    /**
     *
     * @return the type of T
     */
    protected abstract Class<T> getEntityClass();
}

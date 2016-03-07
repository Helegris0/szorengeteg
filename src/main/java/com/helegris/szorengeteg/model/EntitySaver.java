/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.model;

import com.helegris.szorengeteg.model.entity.PersistentObject;
import com.helegris.szorengeteg.transaction.Transactional;
import java.util.Collection;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Timi
 */
@Transactional
public class EntitySaver {

    @Inject
    private EntityManager em;

    public void create(PersistentObject obj) {
        em.persist(obj);
    }

    public void modify(PersistentObject obj) {
        em.merge(obj);
    }

    public void delete(PersistentObject obj) {
        em.remove(obj);
    }

    public void complexTransaction(Collection<? extends PersistentObject> toCreate,
            Collection<? extends PersistentObject> toModify,
            Collection<? extends PersistentObject> toDelete) {
        if (toCreate != null) {
            toCreate.stream().forEach(em::persist);
        }
        if (toModify != null) {
            toModify.stream().forEach(em::merge);
        }
        if (toDelete != null) {
            toDelete.stream().forEach(em::remove);
        }
    }
}

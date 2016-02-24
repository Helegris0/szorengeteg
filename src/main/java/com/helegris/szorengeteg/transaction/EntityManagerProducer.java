/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.jboss.weld.environment.se.events.ContainerInitialized;

/**
 *
 * @author Timi
 */
@ApplicationScoped
public class EntityManagerProducer {

    private EntityManagerFactory emf;

    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_STORE = new ThreadLocal<>();

    public void init(@Observes ContainerInitialized containerInitialized) {
        emf = Persistence.createEntityManagerFactory("default");
    }

    @Produces
    public EntityManager getEntityManager() {
        EntityManager em = ENTITY_MANAGER_STORE.get();
        if (em == null) {
            em = emf.createEntityManager();
            ENTITY_MANAGER_STORE.set(em);
        }
        return em;
    }

    public void closeEntityManager(@Disposes EntityManager entityManager) {
        ENTITY_MANAGER_STORE.remove();
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

}

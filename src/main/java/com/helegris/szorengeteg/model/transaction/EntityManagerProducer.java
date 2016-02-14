/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.model.transaction;

import java.util.Stack;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.jboss.weld.environment.se.events.ContainerInitialized;

/**
 *
 * @author Timi
 */
@ApplicationScoped
public class EntityManagerProducer {

    private EntityManagerFactory emf;

    private final ThreadLocal<Stack<EntityManager>> emStackThreadLocal = new ThreadLocal<>();

    public void init(@Observes ContainerInitialized containerInitialized) {
        emf = Persistence.createEntityManagerFactory("default");
    }

    @Produces
    @PersistenceContext
    public EntityManager createEntityManager() {
        final Stack<EntityManager> entityManagerStack = emStackThreadLocal.get();
        if (entityManagerStack == null || entityManagerStack.isEmpty()) {
            return createAndRegister();
        }
        return entityManagerStack.peek();
    }

    private EntityManager createAndRegister() {
        Stack<EntityManager> entityManagerStack = emStackThreadLocal.get();
        if (entityManagerStack == null) {
            entityManagerStack = new Stack<>();
            emStackThreadLocal.set(entityManagerStack);
        }

        final EntityManager entityManager = emf.createEntityManager();
        entityManagerStack.push(entityManager);
        return entityManager;
    }

    public void removeEntityManager(@Disposes EntityManager entityManager) {
        final Stack<EntityManager> entityManagerStack = emStackThreadLocal.get();
        if (entityManagerStack == null || entityManagerStack.isEmpty()) {
            throw new IllegalStateException("Removing of entity manager failed. Your entity manager was not found.");
        }

        if (entityManagerStack.peek() != entityManager) {
            throw new IllegalStateException("Removing of entity manager failed. Your entity manager was not found.");
        }
        entityManagerStack.pop();

        entityManager.close();
    }
}

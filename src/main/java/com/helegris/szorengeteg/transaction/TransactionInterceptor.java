/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.transaction;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Timi
 */
@Transactional
@Interceptor
public class TransactionInterceptor {

    private static final Logger LOGGER = Logger.getLogger(TransactionInterceptor.class.getName());

    @Inject
    private EntityManager em;

    @AroundInvoke
    public Object manageTransaction(final InvocationContext ctx) throws Exception {
        if (em.getTransaction().isActive()) {
            return ctx.proceed();
        }

        final EntityTransaction transaction = em.getTransaction();
        Object returnValue = null;
        try {
            transaction.begin();
            returnValue = ctx.proceed();
            transaction.commit();
        } catch (Exception e) {
            try {
                if (transaction.isActive()) {
                    transaction.rollback();
                    LOGGER.log(Level.FINE, "Rolled back transaction");
                }
            } catch (Exception roolbackException) {
                LOGGER.log(Level.WARNING, "Rollback of transaction failed -> {0}", roolbackException);
            }
            throw e;
        }

        return returnValue;
    }
}

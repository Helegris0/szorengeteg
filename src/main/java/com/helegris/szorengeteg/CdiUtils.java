/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 *
 * @author Timi
 */
public class CdiUtils {

    public static <T> T injectFields(T instance) {
        if (instance == null) {
            return null;
        }

        BeanManager beanManager = ApplicationContainer.getInstance().getBeanManager();

        CreationalContext<T> creationalContext = beanManager.createCreationalContext(null);

        AnnotatedType<T> annotatedType = beanManager.createAnnotatedType((Class<T>) instance.getClass());
        InjectionTarget<T> injectionTarget = beanManager.createInjectionTarget(annotatedType);
        injectionTarget.inject(instance, creationalContext);
        return instance;
    }

}

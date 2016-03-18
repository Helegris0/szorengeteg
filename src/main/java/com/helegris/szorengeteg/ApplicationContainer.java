/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

/**
 *
 * @author Timi
 */
public class ApplicationContainer {

    private static class ApplicationContainerLoader {

        private static final ApplicationContainer INSTANCE = new ApplicationContainer();
    }

    private final Injector injector;

    private ApplicationContainer() {
        if (ApplicationContainerLoader.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }

        injector = Guice.createInjector(new ApplicationInjector());
        injector.getInstance(PersistService.class).start();
    }

    public static ApplicationContainer getInstance() {
        return ApplicationContainerLoader.INSTANCE;
    }

    public <T> T getBean(Class<T> type) {
        return injector.getInstance(type);
    }

    public <T> void injectFields(T instance) {
        injector.injectMembers(instance);
    }
}

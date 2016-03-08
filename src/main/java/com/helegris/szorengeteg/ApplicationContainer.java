/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import javax.enterprise.inject.spi.BeanManager;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 *
 * @author Timi
 */
public class ApplicationContainer {

    private static class ApplicationContainerLoader {

        private static final ApplicationContainer INSTANCE
                = new ApplicationContainer();
    }

    private final WeldContainer container;

    private ApplicationContainer() {
        if (ApplicationContainerLoader.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }

        this.container = new Weld().initialize();
    }

    public static ApplicationContainer getInstance() {
        return ApplicationContainerLoader.INSTANCE;
    }

    public <T> T getBean(Class<T> type) {
        return container.instance().select(type).get();
    }

    public BeanManager getBeanManager() {
        return container.getBeanManager();
    }

}

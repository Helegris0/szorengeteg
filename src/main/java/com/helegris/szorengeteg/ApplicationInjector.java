package com.helegris.szorengeteg;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 *
 * @author Timi
 */
public class ApplicationInjector extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("default"));
    }

}

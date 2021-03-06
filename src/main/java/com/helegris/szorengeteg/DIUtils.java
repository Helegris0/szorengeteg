/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

/**
 *
 * @author Timi
 */
public class DIUtils {

    /**
     * Injects every field marked with @Inject annotation in a given instance of
     * any class.
     *
     * @param <T>
     * @param instance
     */
    public static <T> void injectFields(T instance) {
        if (instance == null) {
            return;
        }
        ApplicationContainer.getInstance().injectFields(instance);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui;

import com.helegris.szorengeteg.ui.topiclist.MainView;

/**
 *
 * @author Timi
 */
public class VistaNavigator {

    private static MainView mainView;

    public static MainView getMainView() {
        return mainView;
    }

    public static void setMainView(MainView mainView) {
        VistaNavigator.mainView = mainView;
    }
}

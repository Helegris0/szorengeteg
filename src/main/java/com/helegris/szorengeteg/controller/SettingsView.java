/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.GlobalSettings;
import com.helegris.szorengeteg.GlobalSettingsStore;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.NumberSpinner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Timi
 */
public class SettingsView extends AnchorPane {

    public static final String FXML = "fxml/settings.fxml";

    @FXML
    private NumberSpinner nmbWorsPerSession;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBack;

    private GlobalSettings settings;
    private final GlobalSettingsStore store = new GlobalSettingsStore();

    @SuppressWarnings("LeakingThisInConstructor")
    public SettingsView() {
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    private void initialize() {
        settings = store.load();
        nmbWorsPerSession.setValue(settings.getWordsPerSession());

        btnSave.setOnAction(this::saveSettings);
        btnBack.setOnAction(this::goBack);
    }

    private void saveSettings(ActionEvent event) {
        settings.setWordsPerSession(nmbWorsPerSession.getValue());
        store.store(settings);
        VistaNavigator.getMainView().loadContentTopics();
    }

    private void goBack(ActionEvent event) {
        VistaNavigator.getMainView().loadContentTopics();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.Settings;
import com.helegris.szorengeteg.VistaNavigator;
import com.helegris.szorengeteg.controller.component.NumberSpinner;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
    private ToggleGroup grpWordInput;
    @FXML
    private RadioButton rdOneField;
    @FXML
    private RadioButton rdCharFields;
    @FXML
    private ToggleGroup grpWordHelp;
    @FXML
    private RadioButton rdNoHelp;
    @FXML
    private RadioButton rdFirstChar;
    @FXML
    private RadioButton rdFirstTwoChars;
    @FXML
    private RadioButton rdFirstAndLastChar;
    @FXML
    private RadioButton rdVowels;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBack;

    private final Settings settings = new Settings();
    private final Map<RadioButton, Settings.WordInput> wordInputRadios
            = new HashMap<>();
    private final Map<RadioButton, Settings.WordHelp> wordHelpRadios
            = new HashMap<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public SettingsView() {
        FXMLLoaderHelper.load(FXML, this);
    }

    @FXML
    private void initialize() {
        nmbWorsPerSession.setValue(settings.getWordsPerSession());

        for (int i = 0; i < grpWordInput.getToggles().size(); i++) {
            RadioButton radioButton = (RadioButton) grpWordInput.getToggles().get(i);
            Settings.WordInput wordInput = Settings.WordInput.values()[i];
            wordInputRadios.put(radioButton, wordInput);

            if (wordInput.equals(settings.getWordInput())) {
                radioButton.setSelected(true);
            }
        }

        for (int i = 0; i < grpWordHelp.getToggles().size(); i++) {
            RadioButton radioButton = (RadioButton) grpWordHelp.getToggles().get(i);
            Settings.WordHelp wordHelp = Settings.WordHelp.values()[i];
            wordHelpRadios.put(radioButton, wordHelp);

            if (wordHelp.equals(settings.getWordHelp())) {
                radioButton.setSelected(true);
            }
        }

//        wordInputRadios.put(Settings.WordInput.ONE_FIELD, rdOneField);
//        wordInputRadios.put(Settings.WordInput.CHAR_FIELDS, rdCharFields);
//
//        wordHelpRadios.put(Settings.WordHelp.NO_HELP, rdNoHelp);
//        wordHelpRadios.put(Settings.WordHelp.FIRST_CHAR, rdFirstChar);
//        wordHelpRadios.put(Settings.WordHelp.FIRST_TWO_CHARS, rdFirstTwoChars);
//        wordHelpRadios.put(Settings.WordHelp.FIRST_AND_LAST_CHAR, rdFirstAndLastChar);
//        wordHelpRadios.put(Settings.WordHelp.VOWELS, rdVowels);
        btnSave.setOnAction(this::saveSettings);
        btnBack.setOnAction(this::goBack);
    }

    private void saveSettings(ActionEvent event) {
        settings.setWordsPerSession(nmbWorsPerSession.getValue());

        RadioButton selectedInput = (RadioButton) grpWordInput.getSelectedToggle();
        settings.setWordInput(wordInputRadios.get(selectedInput));

        RadioButton selectedHelp = (RadioButton) grpWordHelp.getSelectedToggle();
        settings.setWordHelp(wordHelpRadios.get(selectedHelp));

        VistaNavigator.getMainView().loadContentTopics();
    }

    private void goBack(ActionEvent event) {
        VistaNavigator.getMainView().loadContentTopics();
    }
}

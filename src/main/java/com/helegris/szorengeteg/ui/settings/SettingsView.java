/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.ui.settings;

import com.helegris.szorengeteg.FXMLLoaderHelper;
import com.helegris.szorengeteg.ui.VistaNavigator;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
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
    private CheckBox chkRepeat;
    @FXML
    private ToggleGroup grpWordInput;
    @FXML
    private ToggleGroup grpWordHelp;
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
        fillMaps();
        setDependency();
        setDefaultValues();
        btnSave.setOnAction(this::saveSettings);
        btnBack.setOnAction(this::goBack);
    }

    private void fillMaps() {
        for (int i = 0; i < grpWordInput.getToggles().size(); i++) {
            RadioButton radioButton = (RadioButton) grpWordInput.getToggles().get(i);
            Settings.WordInput wordInput = Settings.WordInput.values()[i];
            wordInputRadios.put(radioButton, wordInput);
        }

        for (int i = 0; i < grpWordHelp.getToggles().size(); i++) {
            RadioButton radioButton = (RadioButton) grpWordHelp.getToggles().get(i);
            Settings.WordHelp wordHelp = Settings.WordHelp.values()[i];
            wordHelpRadios.put(radioButton, wordHelp);
        }
    }

    private void setDependency() {
        grpWordInput.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
                    RadioButton selectedInputRd = (RadioButton) grpWordInput.getSelectedToggle();
                    Settings.WordInput selectedInput = wordInputRadios.get(selectedInputRd);

                    if (selectedInputRd != null) {
                        wordHelpRadios.forEach((rd, help) -> {
                            if (help.getWordInputs().contains(selectedInput)) {
                                rd.setVisible(true);
                            } else {
                                rd.setVisible(false);

                                RadioButton selectedHelpRd = (RadioButton) grpWordHelp.getSelectedToggle();
                                if (rd.equals(selectedHelpRd)) {
                                    rd.setSelected(false);
                                }
                            }
                        });
                    }

                    RadioButton selectedHelpRd = (RadioButton) grpWordHelp.getSelectedToggle();
                    if (selectedHelpRd == null) {
                        grpWordHelp.getToggles().get(0).setSelected(true);
                    }
                });
    }

    private void setDefaultValues() {
        nmbWorsPerSession.setValue(settings.getWordsPerSession());

        chkRepeat.setSelected(settings.isRepeatUnknownWords());

        grpWordInput.getToggles().stream().forEach(toggle -> {
            RadioButton radioButton = (RadioButton) toggle;
            Settings.WordInput wordInput = wordInputRadios.get(radioButton);

            if (wordInput.equals(settings.getWordInput())) {
                radioButton.setSelected(true);
            }
        });

        grpWordHelp.getToggles().stream().forEach(toggle -> {
            RadioButton radioButton = (RadioButton) toggle;
            Settings.WordHelp wordHelp = wordHelpRadios.get(radioButton);

            if (wordHelp.equals(settings.getWordHelp())) {
                radioButton.setSelected(true);
            }
        });
    }

    private void saveSettings(ActionEvent event) {
        settings.setWordsPerSession(nmbWorsPerSession.getValue());

        settings.setRepeatUnknownWords(chkRepeat.isSelected());

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

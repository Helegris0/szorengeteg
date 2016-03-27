/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.helegris.szorengeteg.controller.FormAlert;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Timi
 */
public class GlobalSettingsStore {

    private final String PATH = "src/main/resources/json/settings.json";
    private final String CHARSET = "UTF-8";

    private final int WORDS_PER_SESSION = 5;

    public void store(GlobalSettings settings) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(settings);

        File file = new File(PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                new FormAlert().exceptionAlert(ex);
            }
        }

        try (PrintWriter out = new PrintWriter(PATH, CHARSET)) {
            out.println(json);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            new FormAlert().exceptionAlert(ex);
        }
    }

    public GlobalSettings load() {
        File file = new File(PATH);
        if (file.exists()) {
            try {
                String json = FileUtils.readFileToString(file);
                Gson gson = new Gson();
                return gson.fromJson(json, GlobalSettings.class);
            } catch (IOException ex) {
                new FormAlert().exceptionAlert(ex);
            }
        }
        return defaultSettings();
    }

    private GlobalSettings defaultSettings() {
        GlobalSettings settings = new GlobalSettings();
        settings.setWordsPerSession(WORDS_PER_SESSION);
        return settings;
    }
}

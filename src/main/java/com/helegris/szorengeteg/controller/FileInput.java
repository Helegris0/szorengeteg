/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.model.entity.Card;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Timi
 */
public class FileInput {

    private final String CHARSET = "UTF-8";
    private final String DELIMITER = ";";
    private final File file;

    public FileInput(File file) {
        this.file = file;
    }

    public List<Card> getCards() throws FileNotFoundException {
        List<Card> cards = new ArrayList<>();

        try (Scanner scanner = new Scanner(file, CHARSET)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] data = line.split(DELIMITER);
                    Card card = new Card(data[0], data[1]);
                    cards.add(card);
                }
            }
        }
        return cards;
    }

}

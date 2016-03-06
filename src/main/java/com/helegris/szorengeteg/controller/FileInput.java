/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller;

import com.helegris.szorengeteg.controller.component.RowForCard;
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
    private final CardsEditorForm form;
    private final File file;

    public FileInput(CardsEditorForm form, File file) {
        this.form = form;
        this.file = file;
    }

    public List<RowForCard> getRows() throws FileNotFoundException {
        List<RowForCard> rows = new ArrayList<>();

        try (Scanner scanner = new Scanner(file, CHARSET)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] data = line.split(";");
                    Card card = new Card();
                    card.setWord(data[0]);
                    card.setDescription(data[1]);
                    rows.add(new RowForCard(form, card));
                }
            }
        }
        return rows;
    }

}

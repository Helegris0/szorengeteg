/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.helegris.szorengeteg.controller.forms;

import com.helegris.szorengeteg.model.entities.Card;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Timi
 */
public class FileInputTest {

    private File file;
    private FileInput fileInput;
    private final List<Card> cards = new ArrayList<>();

    @Before
    public void setUp() {
        cards.add(new Card("január", "az év első hónapja"));
        cards.add(new Card("számol", "számokkal műveleteket végez"));
        cards.add(new Card("járda", "az utcán ezen járunk"));
        cards.add(new Card("nagy", "a kicsi ellentéte"));
    }

    @Test
    public void testFromTxt() throws FileNotFoundException, URISyntaxException {
        file = new File(this.getClass().getResource("demo01.txt").toURI());
        fileInput = new FileInput(file);
        assertEquals(cards, fileInput.getCards());
    }

    @Test
    public void testFromCsv() throws FileNotFoundException, URISyntaxException {
        file = new File(this.getClass().getResource("demo01.csv").toURI());
        fileInput = new FileInput(file);
        assertEquals(cards, fileInput.getCards());
    }

}

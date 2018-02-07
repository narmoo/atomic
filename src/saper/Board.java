/**
 * Created by Maciek on 06.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static saper.Saper.PROBABILITY;
import static saper.Saper.Y_SMALL;

public class Board extends JFrame {
    private JPanel buttonPanel;
    private Field[][] field;

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;


    public Board() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Tworzy tablicę przycisków z polami
     * @param x liczba rzędów pól
     * @param y liczba szeregów pól
     */
    public void createFields(int x, int y) {
        int xLosowa;
        int yLosowa;

        // Tworzy przyciski
        buttonPanel = new JPanel();
        field = new Field[x][y];

        buttonPanel.setLayout(new GridLayout(x, y));
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                field[j][i] = new Field();
                buttonPanel.add(field[j][i]);
                field[j][i].setToolTipText("P: " + j + " " + i);
            }
        }

        // Przypisuje bomby
        int z = 0;
        while (z < (int) (PROBABILITY * x * y)) {
            xLosowa = (int) (Math.random() * x);
            yLosowa = (int) (Math.random() * y);
//            System.out.println("pos = " + xLosowa + " " + yLosowa);
            if (!field[xLosowa][yLosowa].getWithBomb()) {
                field[xLosowa][yLosowa].setWithBomb(true);
                z++;
//                System.out.println("Dodano: " + z);

                // Tymczasowe!
                field[xLosowa][yLosowa].setText("B");
            }
        }

        // Ustawia ilość bomb w pobliżu
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                System.out.println("pos: " + j + " " + i);
                if(!field[j][i].getWithBomb()) {
                    // Na górze
                    if (i > 0 && field[j][i - 1].getWithBomb()) {
                        field[j][i].increaseNeighbours();
                    }

                    // Na dole
                    if (i < y - 1 && field[j][i + 1].getWithBomb()) {
                        field[j][i].increaseNeighbours();
                    }

                    // Po lewej
                    if (j > 0 && field[j - 1][i].getWithBomb()) {
                        field[j][i].increaseNeighbours();
                    }

                    // Po prawej
                    if (j < x - 1 && field[j + 1][i].getWithBomb()) {
                        field[j][i].increaseNeighbours();
                    }

                    // Po lewej góra
                    if (j > 0 && i > 0 && field[j - 1][i - 1].getWithBomb()) {
                        field[j][i].increaseNeighbours();
                    }

                    // Po lewej dół
                    if (j > 0 && i < y - 1 && field[j - 1][i + 1].getWithBomb()) {
                        field[j][i].increaseNeighbours();
                    }

                    // Po prawej góra
                    if (j < x - 1 && i > 0 && field[j + 1][i - 1].getWithBomb()) {
                        field[j][i].increaseNeighbours();
                    }

                    // Po prawej dół
                    if (j < x - 1 && i < y - 1 && field[j + 1][i + 1].getWithBomb()) {
                        field[j][i].increaseNeighbours();
                    }
                }
            }
        }


        add(buttonPanel);
    }
}

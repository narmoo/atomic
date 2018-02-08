/**
 * Created by Maciek on 06.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static saper.Saper.PROBABILITY;

public class Board extends JFrame {
    private JPanel buttonPanel;
    private Field[][] field;
    private int sizeX;
    private int sizeY;

    private int clearFields;
    private int unrevealedCount;

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

        int bombsNumber = (int) (PROBABILITY * x * y);

        clearFields = x * y - bombsNumber;
        unrevealedCount = clearFields;

        sizeX = x;
        sizeY = y;

        // Tworzy przyciski
        buttonPanel = new JPanel();
        field = new Field[x][y];

        buttonPanel.setLayout(new GridLayout(x, y));
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                field[j][i] = new Field();
                buttonPanel.add(field[j][i]);
                field[j][i].setToolTipText("P: " + j + " " + i);

                field[j][i].addActionListener(new FieldAction(j, i));
            }
        }

        // Przypisuje bomby
        int z = 0;
        while (z < (int) (bombsNumber)) {
            xLosowa = (int) (Math.random() * x);
            yLosowa = (int) (Math.random() * y);
            if (!field[xLosowa][yLosowa].getWithBomb()) {
                field[xLosowa][yLosowa].setWithBomb(true);
                z++;

            }
        }

        // Ustawia ilość bomb w pobliżu
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
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

    private class FieldAction implements ActionListener {
        int posX;
        int posY;

        public FieldAction (int x, int y) {
            posX = x;
            posY = y;
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            testField(posX, posY);
        }
    }

    /**
     * Sprawdza po kliknięciu, czy jest bomba i wykonuje odpowiednią akcję.
     * @param x Pozycja x testowanego pola
     * @param y POzycja y testowanego pola
     */
    public void testField (int x, int y) {
        if (field[x][y].getWithBomb()) {
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    if(field[j][i].getWithBomb()) {
                        field[j][i].displayContent();
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "GAME OVER");
            System.exit(0);
        } else {
            field[x][y].displayContent();
            unrevealedCount--;
            System.out.println("unrevealedCount = " + unrevealedCount);
            if (unrevealedCount == 0) {
                JOptionPane.showMessageDialog(this, "WIN!");
            }
        }
    }
}

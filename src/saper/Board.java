/**
 * Created by Maciek on 06.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static saper.Saper.PROBABILITY;


public class Board extends JFrame {
    private Field[][] field;
    private JTextField toRevealTxt;
    private int sizeX;
    private int sizeY;

    private int flaggedCount;
    private int unrevealedCount;
    private int bombsNumber;

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;


    private Board() {
        flaggedCount = 0;
//        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Tworzy tablicę przycisków z polami
     * @param columns liczba rzędów pól
     * @param rows liczba szeregów pól
     */
    private void createFields(int columns, int rows) {
        int xLosowa;
        int yLosowa;

        bombsNumber = (int) (PROBABILITY * columns * rows);

        unrevealedCount = columns * rows - bombsNumber;

        sizeX = columns;
        sizeY = rows;

        // Tworzy panele
        JPanel controlPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        toRevealTxt = new JTextField("", 3);
        toRevealTxt.setEditable(false);
        controlPanel.add(new JLabel("Bomb zostalo:"));
        controlPanel.add(toRevealTxt);

        add(controlPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        updateCounter(0);
        setVisible(true);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT + controlPanel.getHeight());

        // Tworzy przyciski
        field = new Field[columns][rows];

        buttonPanel.setLayout(new GridLayout(columns, rows));
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                field[x][y] = new Field(x, y);
                buttonPanel.add(field[x][y]);
                field[x][y].setToolTipText("P: " + x + " " + y);
                field[x][y].addActionListener(new FieldAction(x, y));
                field[x][y].addMouseListener(new FieldMouseAction(field[x][y]));
            }
        }

        // Przypisuje bomby
        int z = 0;
        while (z < bombsNumber) {
            xLosowa = (int) (Math.random() * columns);
            yLosowa = (int) (Math.random() * rows);
            if (!field[xLosowa][yLosowa].getWithBomb()) {
                field[xLosowa][yLosowa].setWithBomb(true);
                z++;

            }
        }

        // Ustawia ilość bomb w pobliżu
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if(!field[x][y].getWithBomb()) {
                    // Na górze
                    if (y > 0 && field[x][y - 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                    }
                    // Na dole
                    if (y < rows - 1 && field[x][y + 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                    }
                    // Po lewej
                    if (x > 0 && field[x - 1][y].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                    }
                    // Po prawej
                    if (x < columns - 1 && field[x + 1][y].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                    }
                    // Po lewej góra
                    if (x > 0 && y > 0 && field[x - 1][y - 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                    }
                    // Po lewej dół
                    if (x > 0 && y < rows - 1 && field[x - 1][y + 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                    }
                    // Po prawej góra
                    if (x < columns - 1 && y > 0 && field[x + 1][y - 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                    }
                    // Po prawej dół
                    if (x < columns - 1 && y < rows - 1 && field[x + 1][y + 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                    }
                }
            }
        }
    }

    private class FieldAction implements ActionListener {
        int posX;
        int posY;

        private FieldAction (int x, int y) {
            posX = x;
            posY = y;
        }

        @Override
        public void actionPerformed (ActionEvent e) {
            if (!field[posX][posY].getFlag()) {
                testField(posX, posY);
            }
        }
    }

    private class FieldMouseAction extends MouseAdapter {
        Field object;

        FieldMouseAction (Field object) {
            this.object = object;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) {
                object.setFlag(!object.getFlag());
                if (object.getFlag()) {
                    flaggedCount++;
                    updateCounter(0);
                } else {
                    flaggedCount--;
                    updateCounter(0);
                }

            }
        }
    }


    /**
     * Sprawdza po kliknięciu, czy jest bomba i wykonuje odpowiednią akcję.
     * @param posX Pozycja x testowanego pola
     * @param posY POzycja y testowanego pola
     */
    private void testField (int posX, int posY) {
        // Przegrana
        if (field[posX][posY].getWithBomb()) {
            for (int y = 0; y < sizeY; y++) {
                for (int x = 0; x < sizeX; x++) {
                    if(field[x][y].getWithBomb()) {
                        field[x][y].displayContent();
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "GAME OVER");
            System.exit(0);
        } else {
            field[posX][posY].displayContent();
            if (field[posX][posY].getNeighbours() == 0) {
                findZeroes(posX, posY);
            }
            updateCounter(-1);
            if (unrevealedCount == 0) {
                JOptionPane.showMessageDialog(this, "WIN!");
            }
        }

    }

    private void updateCounter(int amount) {
        unrevealedCount += amount;
        System.out.println("unrevealedCount = " + unrevealedCount);
//        flaggedCount -= amount;
        toRevealTxt.setText(Integer.toString(bombsNumber - flaggedCount));
    }

//    public void updateFlaggedCounter (int amount) {
//        flaggedCount += amount;
//    }

    private void findZeroes(int posX, int posY) {
        // Dwie listy - aktualna i następna
        ArrayList<Field> workingField = new ArrayList<>();
        ArrayList<Field> nextWorkingField = new ArrayList<>();

        // Dodaje pierwsze pole - to które zostało kliknięte
        workingField.add(field[posX][posY]);

        // Skanuje pola dookoła. Pomijając pole odkryte wcześniej odkrywa wszystkie inne.
        while (!workingField.isEmpty()) {
            for (Field f:workingField) {
                int x = f.getPosX();
                int y = f.getPosY();
                for (Direction direction : Direction.returnAll()) {
                    int newX = x;
                    int newY = y;
                    switch (direction) {
                        // W prawo
                        case RIGHT:
                            newX++;
                            break;
                        // W lewo
                        case LEFt:
                            newX--;
                            break;
                        // W górę
                        case UP:
                            newY--;
                            break;
                        // W dół
                        case DOWN:
                            newY++;
                            break;
                        // W górę lewo
                        case UP_LEFT:
                            newY--;
                            newX--;
                            break;
                        // W górę prawo
                        case UP_RIGHT:
                            newY--;
                            newX++;
                            break;
                        // W dół lewo
                        case DOWN_LEFT:
                            newY++;
                            newX--;
                            break;
                        // W dół prawo
                        case DOWN_RIGHT:
                            newY++;
                            newX++;
                            break;
                    }
                    if (newX >= 0 && newY >= 0 && newX < sizeX  && newY < sizeY && field[newX][newY].isEnabled()) {
                        // Zerowe pola są dodawane do następnej listy
                        if (field[newX][newY].getNeighbours() == 0) {
                            nextWorkingField.add(field[newX][newY]);
                        }
                        field[newX][newY].displayContent();
                        updateCounter(-1);
                    }
                }
            }

            // Następna lista staje się aktualną listą.
            workingField.clear();
            workingField.addAll(nextWorkingField);
            nextWorkingField.clear();
        }
    }

    enum Direction {
        LEFt, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT;

        public static Direction[] returnAll() {
            return new Direction[]{LEFt, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT};
        }
    }

    static Board generate(int sizeX, int sizeY) {
        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(infos[1].getClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Board frame = new Board();
        frame.setTitle("Board");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Centrowanie okienka
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getWidth();
        frame.setLocation(screenWidth / 2 - frameWidth / 2, screenHeight / 2 - frameHeight / 2);
        frame.createFields(sizeX, sizeY);
        return frame;
    }

}

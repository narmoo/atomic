/**
 * Created by Maciek on 06.02.2018
 */

package saper;

import java.awt.*;
import java.util.ArrayList;

import static saper.Saper.PROBABILITY;


public class Board {
    private Field[][] field;
    private int sizeX;
    private int sizeY;

    private int flaggedCount;
    private int unrevealedCount;
    private int bombsNumber;

    private BoardPanel buttonPanel;

    static final int DEFAULT_WIDTH = 400;
    static final int DEFAULT_HEIGHT = 400;


    Board(int rows, int columns) {
        flaggedCount = 0;
        EventQueue.invokeLater(() -> createFields(rows, columns));
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

        buttonPanel = new BoardPanel();
        buttonPanel.createBoard(sizeX, sizeY);
        buttonPanel.updateCounter(bombsNumber);

        // Tworzy przyciski
        field = new Field[columns][rows];


        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                field[x][y] = new Field(x, y, this);
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
            buttonPanel.doGameOver();
            System.exit(0);
        } else {
            field[posX][posY].displayContent();
            if (field[posX][posY].getNeighbours() == 0) {
                findZeroes(posX, posY);
            }
            updateCounter(-1);

            // Wygrana
            if (unrevealedCount == 0) {
                buttonPanel.doWin();
                System.exit(0);
            }
        }

    }

    private void updateCounter(int amount) {
        unrevealedCount += amount;
    }

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
                    if (newX >= 0 && newY >= 0 && newX < sizeX  && newY < sizeY && !field[newX][newY].isClicked()) {
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

    public void clickField(int posX, int posY) {
        if (!field[posX][posY].getFlag()) {
            testField(posX, posY);
        }
    }

    public void altClickField(int posX, int posY) {
        Field f = field[posX][posY];
        f.setFlag(!f.getFlag());
        if (f.getFlag()) {
            flaggedCount++;
        } else {
            flaggedCount--;
        }
        buttonPanel.updateCounter(bombsNumber - flaggedCount);
    }

    public void add(FieldBtn button) {
        buttonPanel.addBtn(button);
    }

    enum Direction {
        LEFt, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT;
        public static Direction[] returnAll() {
            return new Direction[]{LEFt, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT};
        }
    }
}

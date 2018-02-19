/**
 * Created by Maciek on 07.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.*;

public class Field extends JButton {
    private boolean withBomb;
    private int neighbours;
    private boolean flag;
    private int posX;
    private int posY;

    Field(int x, int y) {
        super();
        posX = x;
        posY = y;
        flag = false;
    }

    public void setFlag (boolean newFlag) {
        flag = newFlag;
        if (flag) {
            setText("F");
        } else {
            setText("");
        }
    }

    public boolean getFlag() {
        return flag;
    }

    public boolean getWithBomb() {
        return withBomb;
    }

    public void setWithBomb(boolean b) {
        withBomb = b;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public void increaseNeighbours() {
        neighbours++;
    }

    public void displayContent(){
        if (this.getWithBomb()) {
            this.setText("B");
        } else {
            if (neighbours > 0) this.setText(Integer.toString(neighbours));
        }
        // Na chwilę zmienia kolor tekstu
        this.setForeground(this.getBackground());
        new Timer(100, e -> this.setForeground(Color.BLACK)).start();
        this.setEnabled(false);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}

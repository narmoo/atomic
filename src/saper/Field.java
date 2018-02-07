/**
 * Created by Maciek on 07.02.2018
 */

package saper;

import javax.swing.*;

public class Field extends JButton {
    private boolean withBomb;
    private int neighbours;

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
        this.setText(Integer.toString(neighbours));
    }
}

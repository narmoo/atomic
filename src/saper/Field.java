/**
 * Created by Maciek on 07.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Field extends JButton {
    private boolean withBomb;
    private int neighbours;
    private boolean flag;

    public Field() {
        super();

        flag = false;

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) {
                    if (flag) {
                        Field.super.setText("");
                        flag = false;
                    }
                    else {
                        Field.super.setText("F");
                        flag = true;
                    }
                }
            }

        });
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

    public void displayContent() {
        if (this.getWithBomb()) {
            this.setText("B");
        } else {
            this.setText(Integer.toString(neighbours));
        }
        this.setEnabled(false);
    }


}

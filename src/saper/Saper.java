/**
 * Created by Maciek on 06.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.*;

public class Saper {
    public static final int X_SMALL = 8;
    public static final int Y_SMALL = 8;
    public static final int X_MEDIUM = 16;
    public static final int Y_MEDIUM = 16;
    public static final int X_LARGE = 32;
    public static final int Y_LARGE = 32;

    public static final double PROBABILITY = 0.3;

    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            Board frame = new Board();
            frame.setTitle("Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            int screenHeight = screenSize.height;
            int screenWidth = screenSize.width;
            int frameWidth = frame.getWidth();
            int frameHeight = frame.getWidth();

            frame.setLocation(screenWidth / 2 - frameWidth / 2, screenHeight / 2 - frameHeight / 2);

            frame.createFields(X_SMALL, Y_SMALL);
        });
    }

}

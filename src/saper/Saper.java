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

    static final double PROBABILITY = 0.2;

    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> Board.generate(X_SMALL, Y_SMALL));
    }

}

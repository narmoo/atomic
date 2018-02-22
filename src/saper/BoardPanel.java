/**
 * Created by Maciek on 22.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.*;

import static saper.Board.DEFAULT_HEIGHT;
import static saper.Board.DEFAULT_WIDTH;

public class BoardPanel extends JFrame {

    private JTextField toRevealTxt;
    private JPanel buttonPanel;

    public void createBoard(int columns, int rows) {
        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(infos[1].getClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Board");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        // Tworzy panele
        JPanel controlPanel = new JPanel();
        buttonPanel = new JPanel();
        toRevealTxt = new JTextField("", 3);
        toRevealTxt.setEditable(false);
        controlPanel.add(new JLabel("Bomb zostalo:"));
        controlPanel.add(toRevealTxt);

        add(controlPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT + controlPanel.getHeight());

        buttonPanel.setLayout(new GridLayout(columns, rows));

        // Centrowanie okienka
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        int frameWidth = getWidth();
        int frameHeight = getWidth();
        setLocation(screenWidth / 2 - frameWidth / 2, screenHeight / 2 - frameHeight / 2);
    }

    public void doGameOver() {
        JOptionPane.showMessageDialog(this, "GAME OVER!");
    }


    public void doWin() {
        JOptionPane.showMessageDialog(this, "WIN!");
    }

    public void updateCounter(int number) {
        toRevealTxt.setText(Integer.toString(number));
    }

    public void addBtn(FieldBtn button) {
        buttonPanel.add(button);
    }
}

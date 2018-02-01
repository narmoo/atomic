/**
 * Created by Maciek on 25.01.2018
 */

package testclasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


public class Calculator {
    private JPanel calcPanel;
    private JTextField resultsTxt;
    private JButton clearBtn;
    private JButton signBtn;
    private JButton percentBtn;
    private JButton divideBtn;
    private JButton sevenBtn;
    private JButton eightBtn;
    private JButton nineBtn;
    private JButton multiplyBtn;
    private JButton fourBtn;
    private JButton fiveBtn;
    private JButton sixBtn;
    private JButton minusBtn;
    private JButton zeroBtn;
    private JButton digitBtn;
    private JButton equalBtn;
    private JButton oneBtn;
    private JButton twoBtn;
    private JButton threeBtn;
    private JButton addBtn;
    private JButton mcBtn;
    private JButton mrBtn;
    private JButton mPlusBtn;
    private JButton mMinusBtn;

    private Double leftOperand;
    private Double rightOperand;
    private Operation calcOperation;
    private boolean toErase;
    private Double memory;

    private enum MemoryAction {MC, MR, M_PLUS, M_MINUS}


    public Calculator() {

        sevenBtn.addActionListener(new NumberBtnClicked(sevenBtn.getText()));
        eightBtn.addActionListener(new NumberBtnClicked(eightBtn.getText()));
        nineBtn.addActionListener(new NumberBtnClicked(nineBtn.getText()));
        fourBtn.addActionListener(new NumberBtnClicked(fourBtn.getText()));
        fiveBtn.addActionListener(new NumberBtnClicked(fiveBtn.getText()));
        sixBtn.addActionListener(new NumberBtnClicked(sixBtn.getText()));
        oneBtn.addActionListener(new NumberBtnClicked(oneBtn.getText()));
        twoBtn.addActionListener(new NumberBtnClicked(twoBtn.getText()));
        threeBtn.addActionListener(new NumberBtnClicked(threeBtn.getText()));
        zeroBtn.addActionListener(new NumberBtnClicked(zeroBtn.getText()));

        percentBtn.addActionListener(new OperationBtnClicked(Operation.PERCENTAGE));
        multiplyBtn.addActionListener(new OperationBtnClicked(Operation.MULTIPLICATION));
        divideBtn.addActionListener(new OperationBtnClicked(Operation.DIVISION));
        minusBtn.addActionListener(new OperationBtnClicked(Operation.SUBTRACTION));
        addBtn.addActionListener(new OperationBtnClicked(Operation.ADDITION));
        equalBtn.addActionListener(new EqualBtnClicked());
        clearBtn.addActionListener(new ClearBtnClicked());
        signBtn.addActionListener(new SignBtnClicked());
        digitBtn.addActionListener(new DigitBtnClicked());
        mcBtn.addActionListener(new MemoryBtnClicked(MemoryAction.MC));
        mrBtn.addActionListener(new MemoryBtnClicked(MemoryAction.MR));
        mPlusBtn.addActionListener(new MemoryBtnClicked(MemoryAction.M_PLUS));
        mMinusBtn.addActionListener(new MemoryBtnClicked(MemoryAction.M_MINUS));

        toErase = true;
        memory = 0.0;
    }

    private abstract class SolvingBtnClicked implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (leftOperand != null) {
                rightOperand = Double.valueOf(resultsTxt.getText());
                Double output = calcOperation.getOperator().applyAsDouble(leftOperand, rightOperand);
                resultsTxt.setText(output % 1 == 0 ? String.valueOf(output.intValue()) : String.valueOf(output));
                leftOperand = null;
                rightOperand = null;
                toErase = true;
            }
        }
    }

    private class NumberBtnClicked implements ActionListener {

        private String value;

        public NumberBtnClicked(String value) {
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (toErase) {
                resultsTxt.setText("");
                toErase = false;
            }

            resultsTxt.setText(resultsTxt.getText() + value);
        }
    }

    private class OperationBtnClicked extends SolvingBtnClicked {

        private Operation operation;

        public OperationBtnClicked(Operation operation) {
            this.operation = operation;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(leftOperand != null) super.actionPerformed(e);
            calcOperation = operation;
            leftOperand = Double.valueOf(resultsTxt.getText());
            toErase = true;
        }
    }

    private class ClearBtnClicked implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            resultsTxt.setText("");
            leftOperand = null;
            rightOperand = null;
        }
    }

    private class DigitBtnClicked implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            resultsTxt.setText(resultsTxt.getText() + ".");

        }
    }

    private class EqualBtnClicked extends SolvingBtnClicked {}

    private class SignBtnClicked implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            resultsTxt.setText("-" + resultsTxt.getText());
        }
    }

    private class MemoryBtnClicked extends SolvingBtnClicked {
        private MemoryAction action;

        public MemoryBtnClicked (MemoryAction a) {
            this.action = a;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (this.action) {
                case MC:
                    memory = 0.0;
                    mcBtn.setEnabled(false);
                    mrBtn.setEnabled(false);
                    break;
                case MR:
                    resultsTxt.setText(memory % 1 == 0 ? String.valueOf(memory.intValue()) : String.valueOf(memory));
                    break;
                case M_PLUS:
                    memory += Double.valueOf(resultsTxt.getText());
                    mcBtn.setEnabled(true);
                    mrBtn.setEnabled(true);
                    break;
                case M_MINUS:
                    memory -= Double.valueOf(resultsTxt.getText());
                    mcBtn.setEnabled(true);
                    mrBtn.setEnabled(true);
                    break;
            }
        }
    }

    private void processKeys(){
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
                new KeyEventDispatcher()  {
                    public boolean dispatchKeyEvent(KeyEvent e){
                        if(e.getID() == KeyEvent.KEY_PRESSED){
                            System.out.println(e.getKeyCode());
                            /*handleKeyPress(e.getKeyCode());
                            if(areThingsInPlace() && !dialogShown){
                                dialogShown = true;
                                JOptionPane.showMessageDialog(null,"Congratulations!!! YOU WIN!!");
                                System.exit(1);
                            }*/
                        }
                        return false;
                    }
                });
    }

    public static void main(String[] args) {

        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(infos[1].getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new Calculator().calcPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getWidth();

        frame.setLocation(screenWidth / 2 - frameWidth / 2, screenHeight / 2 - frameHeight / 2);
//        frame.setLocationByPlatform(true);
    }
}

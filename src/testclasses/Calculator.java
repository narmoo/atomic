/**
 * Created by Maciek on 25.01.2018
 */

package testclasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;

public class Calculator {
    private JPanel calcPanel;
    private JTextField resultsTxt;
    private JButton oneBtn;
    private JButton twoBtn;
    private JButton threeBtn;
    private JButton fourBtn;
    private JButton fiveBtn;
    private JButton sixBtn;
    private JButton sevenBtn;
    private JButton eightBtn;
    private JButton nineBtn;
    private JButton clearBtn;
    private JButton signBtn;
    private JButton percentBtn;
    private JButton divideBtn;
    private JButton addBtn;
    private JButton minusBtn;
    private JButton multiplyBtn;
    private JButton zeroBtn;
    private JButton digitBtn;
    private JButton equalBtn;
    private JButton mcBtn;
    private JButton mrBtn;
    private JButton mPlusBtn;
    private JButton mMinusBtn;

    private Double leftOperand;
    private Double rightOperand;
    private Operation calcOperation;
    private boolean toErase;
    private Double memory;

    private Color btnColor;

    private JButton activeBtn;

    private enum MemoryAction {MC, MR, M_PLUS, M_MINUS}

    public Calculator() {

        String[] buttonsNames = {"oneBtn", "twoBtn", "threeBtn", "fourBtn", "fiveBtn", "sixBtn",
                "sevenBtn", "eightBtn", "nineBtn", "zeroBtn", "clearBtn", "signBtn", "digitBtn",
                "equalBtn", "addBtn", "percentBtn", "divideBtn", "multiplyBtn", "minusBtn", "mcBtn",
                "mrBtn", "mPlusBtn", "mMinusBtn"};

        String[] buttonsKeyStrokes = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "ESCAPE",
                "control MINUS", "PERIOD", "ENTER", "shift EQUALS", "shift 5", "SLASH", "X",
                "MINUS", "C", "R", "M", "N"};

        JButton[] buttonsObjects =  {oneBtn, twoBtn, threeBtn, fourBtn, fiveBtn, sixBtn,
                sevenBtn, eightBtn, nineBtn, zeroBtn, clearBtn, signBtn, digitBtn,
                equalBtn, addBtn, percentBtn, divideBtn, multiplyBtn, minusBtn, mcBtn,
                mrBtn, mPlusBtn, mMinusBtn};

        InputMap imap = calcPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap amap = calcPanel.getActionMap();

        for (int i = 0; i < buttonsNames.length ; i++) {
            imap.put(KeyStroke.getKeyStroke(buttonsKeyStrokes[i]), buttonsNames[i]);
            amap.put(buttonsNames[i], new SimulateClick(buttonsObjects[i]));
        }

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

        btnColor = addBtn.getBackground();
    }

    public void setBtnActive (Operation operation) {

        switch (operation) {
            case ADDITION: activeBtn = addBtn; break;
            case DIVISION: activeBtn = divideBtn; break;
            case MULTIPLICATION: activeBtn = multiplyBtn; break;
            case SUBTRACTION: activeBtn = minusBtn; break;
            case PERCENTAGE: activeBtn = percentBtn; break;
        }

        activeBtn.setBackground(Color.CYAN);
    }

    public void setBtnInactive () {
        if (activeBtn != null) activeBtn.setBackground(btnColor);
    }

    private class NumberBtnClicked extends AbstractAction {

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

    private class OperationBtnClicked extends EqualBtnClicked {

        private Operation operation;

        public OperationBtnClicked(Operation operation) {
            this.operation = operation;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if(leftOperand != null) super.actionPerformed(e);
            calcOperation = operation;
            try {
                leftOperand = Double.valueOf(resultsTxt.getText());
                toErase = true;
                setBtnActive(operation);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private class ClearBtnClicked extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            resultsTxt.setText("");
            leftOperand = null;
            rightOperand = null;
            setBtnInactive();
        }
    }

    private class DigitBtnClicked extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            resultsTxt.setText(resultsTxt.getText() + ".");

        }
    }

    private class EqualBtnClicked extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            setBtnInactive();

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

    private class SignBtnClicked extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (Double.valueOf(resultsTxt.getText()) > 0){
                resultsTxt.setText("-" + resultsTxt.getText());
            } else {
                resultsTxt.setText(resultsTxt.getText().substring(1));
            }
            }
        }

    private class MemoryBtnClicked extends EqualBtnClicked {

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

    private class SimulateClick extends  AbstractAction {
        private JButton button;

        public  SimulateClick (JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            button.doClick();
        }
    }
}

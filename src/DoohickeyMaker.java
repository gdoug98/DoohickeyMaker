import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;

public class DoohickeyMaker
{
    private final int MAX_DOOHICKEY_VALUE = 10000;
    private final int MIN_DOOHICKEY_VALUE = 0;

    private JButton createButton;
    private JTextField txtName;
//    private JSpinner spnValue = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    private JLabel lblValue;
    private JCheckBox chkDeliver;
    private JLabel lblDeliver;
    private JLabel lblName;
    private JLabel lblCondition;
    private JComboBox cmbCondition;
    private JPanel pnlWindow;
    private JRadioButton rbSetup1;
    private JRadioButton rbSetup2;
    private JRadioButton rbSetup3;
    private JLabel lblSetup;
    private JFormattedTextField txtValue;
    private JButton btnValueDown;
    private JButton btnValueUp;
    private JPanel pnlValueContainer;

    private Doohickey _original;
    private DoohickeyUI _parent;
//    private ArrayList<Doohickey> _doohickeys;
    private int _doohickeyValue = 0;

//    public DoohickeyMaker()
//    {
//        registerConditionOptions();
//        createButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFrame src = (JFrame)e.getSource();
//                int spinnerValue;
//                try
//                {
//                    txtValue.commitEdit();
//                    spinnerValue = (Integer)txtValue.getValue();
//                }
//                catch(ParseException ex)
//                {
//                    System.out.println("Parse error occurred while reading doohickey value.");
//                    spinnerValue = -1;
//                }
//                if(_original == null)
//                {
//
//                    Doohickey newOne = new Doohickey(txtName.getText(), spinnerValue, chkDeliver.isSelected(), ConditionEnum.valueOf(cmbCondition.getSelectedItem().toString()), getSelectedPlan(), true);
//                    _parent.addDoohickey(newOne);
//                }
//                else
//                {
//                    // update doohickey properties here
//                    spinnerValue = (Integer)txtValue.getValue();
//                    _original = _original.name(txtName.getText()).value(spinnerValue).deliver(chkDeliver.isSelected()).condition(ConditionEnum.valueOf(cmbCondition.getSelectedItem().toString())).plan(getSelectedPlan()).setnew(false);
//                }
//                JOptionPane.showMessageDialog(src, "Doohickey details added successfully.");
//                src.dispatchEvent(new WindowEvent(src, WindowEvent.WINDOW_CLOSING)); // close window once this is done
//            }
//        });
//    }

    public DoohickeyMaker(int originalInx, DoohickeyUI frm)
    {
        _parent = frm;
        _original = frm.getDoohickey(originalInx);

        registerConditionOptions();
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame src = (JFrame)SwingUtilities.windowForComponent((Component)e.getSource());

                if(_original == null)
                {
                    Doohickey newOne = new Doohickey(txtName.getText(), _doohickeyValue, chkDeliver.isSelected(), ConditionEnum.valueOf(cmbCondition.getSelectedItem().toString()), getSelectedPlan(), true);
                    _parent.addDoohickey(newOne);
                }
                else
                {
                    // update doohickey properties here
                    _original = _original.name(txtName.getText()).value(_doohickeyValue).deliver(chkDeliver.isSelected()).condition(ConditionEnum.valueOf(cmbCondition.getSelectedItem().toString())).plan(getSelectedPlan()).setnew(false);
                }
                JOptionPane.showMessageDialog(src, "Doohickey details added successfully.");
                src.dispatchEvent(new WindowEvent(src, WindowEvent.WINDOW_CLOSING)); // close window once this is done
            }
        });
        btnValueDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _doohickeyValue = Math.max(MIN_DOOHICKEY_VALUE, --_doohickeyValue);
                txtValue.setValue(_doohickeyValue);
            }
        });
        btnValueUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _doohickeyValue = Math.min(MAX_DOOHICKEY_VALUE, ++_doohickeyValue);
                txtValue.setValue(_doohickeyValue);
            }
        });

        txtValue.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                String text = evt.getNewValue() != null ? evt.getNewValue().toString() : "";
                try
                {
                    _doohickeyValue = Integer.valueOf(text);
                    System.out.println("Doohickey value now " + _doohickeyValue);
                }
                catch(NumberFormatException ex)
                {
                    System.out.println("Not a number, boss.");
                }
            }
        });
        JFrame frame = new JFrame("DoohickeyMaker");
        frame.setContentPane(pnlWindow);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("DoohickeyMaker");
//        frame.setContentPane(new DoohickeyMaker().pnlWindow);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }

    private void registerConditionOptions()
    {
        for(ConditionEnum c : ConditionEnum.values())
        {
            cmbCondition.addItem(c.name());
        }
    }

    private short getSelectedPlan()
    {
        if(rbSetup1.isSelected())
        {
            return 1;
        }
        else if(rbSetup2.isSelected())
        {
            return 2;
        }
        else if(rbSetup3.isSelected())
        {
            return 3;
        }
        else
        {
            return -1;
        }
    }

//    private void createUIComponents() {
//        // TODO: place custom component creation code here
//
//    }
}

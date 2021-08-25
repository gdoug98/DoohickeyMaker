import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.*;
import java.util.List;

public class DoohickeyUI
{
    private final String FILE_PATH = "C:\\Users\\gd689057\\IdeaProjects\\AProj\\Data\\Doohickeys.csv";

    private JTable tblDoohickey;
    private JPanel pnlMain;
    private JButton btnNewDoohickey;
    private JButton btnUpdate;
    private JScrollPane pnlTableContainer;
    private ArrayList<Doohickey> _doohickeys;
    private ArrayList<Doohickey> _newDoohickeys;

    public DoohickeyUI()
    {
        _doohickeys = initialiseDoohickeyList();
        _newDoohickeys = new ArrayList<>();
        DoohickeyUI frm = this; // so I can pass a reference to the action listener.
        btnNewDoohickey.setPreferredSize(new Dimension(200, 35));
        btnNewDoohickey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                Doohickey doo = _doohickeys.size() > 0 ? _doohickeys.get(0) : null;
                DoohickeyMaker window = new DoohickeyMaker(-1, frm);
            }
        });
        btnUpdate.setPreferredSize(new Dimension(200, 120));
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String acc = "";
                for(Doohickey doo : _newDoohickeys)
                {
                    acc += doo.WriteCsv();
                }
                FileIOManager instance = FileIOManager.getInstance();
                try
                {
                    instance.writeToFile(acc.toCharArray());
                    reloadModel();
                }
                catch(IOException ex)
                {
                    System.out.print("Something fucked this way comes.");
                }
            }
        });
        DoohickeyDataModel model = new DoohickeyDataModel();
        model.addRange(_doohickeys);
        tblDoohickey = new JTable(model);
        for(int i = 0; i < model.getColumnCount(); i++)
        {
            tblDoohickey.setDefaultRenderer(tblDoohickey.getColumnClass(i), new DoohickeyCellRenderer());
        }
        tblDoohickey.setPreferredSize(new Dimension(350, 350));
        pnlTableContainer.setViewportView(tblDoohickey);
    }

    public static void main(String[] args)
    {
        DoohickeyUI doo = new DoohickeyUI();
        JFrame mainFrame = new JFrame("Doohickey Test");
        mainFrame.setContentPane(doo.pnlMain);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public Doohickey getDoohickey(int inx)
    {
        if(0 > inx | inx > _doohickeys.size())
        {
            return null;
        }
        return _doohickeys.get(inx);
    }

    public void addDoohickey(Doohickey doo)
    {
        if(doo == null)
        {
            return;
        }
        _newDoohickeys.add(doo);
        DoohickeyDataModel model = (DoohickeyDataModel)tblDoohickey.getModel();
        model.add(doo);
//        reloadModel();
    }

    public void updateDooHickey(Doohickey doo)
    {
        DoohickeyDataModel model = (DoohickeyDataModel)tblDoohickey.getModel();
        model.add(doo);
    }

    private void reloadModel()
    {
        DoohickeyDataModel model = new DoohickeyDataModel();
        _newDoohickeys.clear();
        _doohickeys = initialiseDoohickeyList();
        model.addRange(_doohickeys);
        tblDoohickey.setModel(model);
    }

//    private void createUIComponents()
//    {
//        // TODO: place custom component creation code here
//
//    }

    private ArrayList<Doohickey> initialiseDoohickeyList()
    {
        ArrayList<Doohickey> ret = new ArrayList<>();
        // ^[a-zA-Z]\b+,\d+,true|false,NEW|LIKE_NEW|REFURBISHED|USED,\d{1}$ old regex
        Pattern patt = Pattern.compile("(([a-zA-Z\\x20]+),(\\d+),(true|false),([a-zA-Z_]+),(\\d{1}))");
        FileIOManager instance = FileIOManager.getInstance();
        instance.setPath(FILE_PATH);
        try
        {
            System.out.print("Loading doohickey data...");
            String csv = instance.readFromFile();
            Matcher m = patt.matcher(csv);
            while(m.find())
            {
                ret.add(new Doohickey(csv.substring(m.start(0), m.end(0))));
            }
            System.out.print("Doohickey data loaded successfully.");
        }
        catch(IOException ex)
        {
            System.out.print("Failed to load doohickey data.");
        }
        return ret;
    }
}

class DoohickeyDataModel extends AbstractTableModel
{
    final String[] COLUMNS = { "Name", "Value", "Home Delivery?", "Condition", "Setup Plan" };
    final Class[] CLASSES = { String.class, Integer.class, Boolean.class, ConditionEnum.class, String.class };
    final float[] widths = { 0.3f, 0.15f, 0.1f, 0.25f, 0.2f };

    final Vector data = new Vector();
    private HashMap<Integer, String> _planmap;

    public DoohickeyDataModel()
    {
        createPlanMap();
    }

    public void add(Doohickey d)
    {
        data.addElement(d);
        int len = data.size() - 1;
        fireTableRowsInserted(len, len);
    }

    public void addRange(List<Doohickey> doohickeys)
    {
        int oldLen = data.size();
        for(Doohickey d : doohickeys)
        {
            data.addElement(d);
        }
        fireTableRowsInserted(oldLen, data.size() - 1);
    }

    @Override
    public int getColumnCount()
    {
        return COLUMNS.length;
    }

    @Override
    public int getRowCount()
    {
        return data.size();
    }

    @Override
    public String getColumnName(int column)
    {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Doohickey doo = (Doohickey)getAtRow(rowIndex);
        switch(columnIndex)
        {
            case 0:
                return doo.name();
            case 1:
                return doo.value();
            case 2:
                return doo.deliver();
            case 3:
                return doo.condition();
            case 4:
                return _planmap.get(doo.plan());
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Doohickey doo = (Doohickey)getAtRow(rowIndex);
        switch(columnIndex)
        {
            case 0:
                data.setElementAt(doo.name((String)aValue), rowIndex);
                break;
            case 1:
                data.setElementAt(doo.value((int)aValue), rowIndex);
                break;
            case 2:
                data.setElementAt(doo.deliver((boolean)aValue), rowIndex);
                break;
            case 3:
                data.setElementAt(doo.condition(ConditionEnum.valueOf((String)aValue)), rowIndex);
                break;
            case 4:
                data.setElementAt(doo.plan((short)aValue), rowIndex);
                break;
            default:
                break;
        }
    }

    public Object getAtRow(int row)
    {
        return data.elementAt(row);
    }

    public boolean isCellEditable(int row, int col) { return false; }

    private void createPlanMap()
    {
        _planmap = new HashMap<>();
        _planmap.put(1, "Setup on delivery");
        _planmap.put(2, "Setup after arrival");
        _planmap.put(3, "Manual setup");
    }
}

class DoohickeyCellRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        DoohickeyDataModel ddm = (DoohickeyDataModel)table.getModel();
        Doohickey doo = (Doohickey)ddm.getAtRow(row);
        if(doo.getNew())
        {
            setBackground(Color.GREEN);
        }
        else
        {
            setBackground(Color.WHITE);
        }
        setPreferredSize(new Dimension((int)(ddm.widths[column] * table.getPreferredSize().width), table.getRowHeight())); // make column widths a percentage of the table's preferred size
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}

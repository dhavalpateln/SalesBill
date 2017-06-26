/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesbill;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Generates Help Dialog
 * @author Dhaval
 */
public class HelpDialog extends JDialog {
    
    private JTextField searchBar;
    private JRadioButton filterRadioButton;
    private JRadioButton findRadioButton;
    private JComboBox<String> searchColumnComboBox;
    private HelpTable table;
    private int searchColumn = 0;
    private String[][] returnData;
    private String[] returnDataHeadings;
    
    private HashMap<String, String> map = null;
    
    /**
     * Generates Help Dialog. Use this if you have raw Data
     * @param frame pass the instance of frame
     * @param data data of the table
     * @param headings headings of the table
     */
    public HelpDialog(JFrame frame, Object[][] data, Object[] headings) {
        super(frame, "Help", true);  
        returnDataHeadings = new String[headings.length];
        for(int i = 0; i < headings.length; i++) {
            returnDataHeadings[i] = headings[i].toString();
        }
        returnData = new String[data.length][headings.length];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < headings.length; j++) {
                returnData[i][j] = data[i][j].toString();
            }
        }
        setTableData(data, headings);
        createGUI();
    }
    
    /**
     * Generates Help Dialog. Use this if the columns you want to display are same as columns you want to return in result
     * @param frame pass the instance of frame
     * @param columns pass the columns to display in the HelpDialog. 
     *                This should be same as what you will write in Query between SELECT and FROM. 
     *                The return result will be same as columns
     * <br>i.e. SELECT visibleColumn FROM table
     * @param appendQuery pass the query starting from FROM. i.e. If query is SELECT * FROM table WHERE condition, pass "FROM table WHERE condition"
     * @param columnNo default column selected for searching. Starting index is 0<br>
     * <b> Example : </b><br>
     * {@code HelpDialog dialog = new HelpDialog(frame,"*","FROM table",1);}
     */
    public HelpDialog(JFrame frame, String columns, String appendQuery, int columnNo) {
        super(frame, "Help", true);
        DataDbHelper db = DataDbHelper.getInstance();
        String query = generateQuery(columns, appendQuery);
        ResultSet result = db.executeQuery(query);
        searchColumn = columnNo;
        if(result == null) {
            JOptionPane.showMessageDialog(null, "No Data Extracted");
        }
        else {
            try {
                ResultSetMetaData metaData = result.getMetaData();
                
                String headings[] = new String[metaData.getColumnCount()];
                for(int i = 1; i <= headings.length; i++) {
                    headings[i-1] = metaData.getColumnLabel(i);
                }
                result.last();
                int rows = result.getRow();
                result.beforeFirst();
                String data[][] = new String[rows][headings.length];
                for(int i = 0; result.next(); i++) {
                    for(int j = 0; j < headings.length; j++) {
                        data[i][j] = result.getString(j+1);
                    }
                }
                returnData = data;
                returnDataHeadings = headings;
                setTableData(data, headings);
                createGUI();
            } catch (SQLException ex) {
                Logger.getLogger(HelpDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Generates Help Dialog. Use this if there are some hidden columns or you have different requirements to 
     * show visible columns and values of columns to return
     * @param frame pass the instance of frame
     * @param visibleColumn pass the columns to display in the HelpDialog. This should be same as what you will write in Query between SELECT and FROM.
     * <br>i.e. SELECT visibleColumn FROM table
     * @param returnColumn pass the columns to return in the HelpDialog. This should be same as what you will write in Query between SELECT and FROM.
     * <br>i.e. SELECT visibleColumn FROM table
     * @param appendQuery pass the query starting from FROM. i.e. If query is SELECT * FROM table WHERE condition, pass "FROM table WHERE condition"
     * @param columnNo default column selected for searching. Starting index is 0<br>
     * <b> Example : </b><br>
     * {@code HelpDialog dialog = new HelpDialog(frame,"*","FROM table",1);}
     */
    public HelpDialog(JFrame frame, String visibleColumn, String returnColumn, String appendQuery, int columnNo) {
        
        //Clean this code
        super(frame, "Help", true);
        DataDbHelper db = DataDbHelper.getInstance();
        String visibleQuery = generateQuery(visibleColumn, appendQuery);
        String returnQuery = generateQuery(returnColumn, appendQuery);
        ResultSet result = db.executeQuery(visibleQuery);
        searchColumn = columnNo;
        if(result == null) {
            JOptionPane.showMessageDialog(null, "No Data Extracted");
        }
        else {
            try {
                ResultSetMetaData metaData = result.getMetaData();
                
                String headings[] = new String[metaData.getColumnCount()];
                for(int i = 1; i <= headings.length; i++) {
                    headings[i-1] = metaData.getColumnLabel(i);
                }
                result.last();
                int rows = result.getRow();
                result.beforeFirst();
                String data[][] = new String[rows][headings.length];
                for(int i = 0; result.next(); i++) {
                    for(int j = 0; j < headings.length; j++) {
                        data[i][j] = result.getString(j+1);
                    }
                }
                ResultSet reresult = db.executeQuery(returnQuery);
                ResultSetMetaData ewmetaData = reresult.getMetaData();
                
                returnDataHeadings = new String[ewmetaData.getColumnCount()];
                for(int i = 1; i <= returnDataHeadings.length; i++) {
                    returnDataHeadings[i-1] = ewmetaData.getColumnLabel(i);
                }
                reresult.last();
                int ewrows = reresult.getRow();
                reresult.beforeFirst();
                returnData = new String[ewrows][returnDataHeadings.length];
                for(int i = 0; reresult.next(); i++) {
                    for(int j = 0; j < returnDataHeadings.length; j++) {
                        returnData[i][j] = reresult.getString(j+1);
                    }
                }
                setTableData(data, headings);
                createGUI();
            } catch (SQLException ex) {
                Logger.getLogger(HelpDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    private void createGUI() {
        
        setSize(800, 600);
        searchBar = new JTextField();  
        filterRadioButton = new JRadioButton("Filter");
        findRadioButton = new JRadioButton("Find");
        ButtonGroup group = new ButtonGroup();
        group.add(findRadioButton);
        group.add(filterRadioButton);
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                table.filter(searchColumn);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                table.filter(searchColumn);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                table.filter(searchColumn);
            }
        });
        
        searchColumnComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                searchColumn = searchColumnComboBox.getSelectedIndex();
                table.filter(searchColumn);
            }
        });
        
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                table.filter(searchColumn);
            }
        };
        filterRadioButton.addActionListener(radioListener);
        findRadioButton.addActionListener(radioListener);
        
        searchColumnComboBox.setSelectedIndex(searchColumn);
        filterRadioButton.setSelected(true);
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout(5, 5));
        panel.add(searchColumnComboBox, BorderLayout.WEST);
        panel.add(searchBar, BorderLayout.CENTER);
        JPanel searchOptionPanel = new JPanel();
        searchOptionPanel.setLayout(new FlowLayout());
        searchOptionPanel.add(filterRadioButton);
        searchOptionPanel.add(findRadioButton);
        panel.add(searchOptionPanel,BorderLayout.EAST);
        add(panel, BorderLayout.NORTH);
        
        searchBar.requestFocus();
        setVisible(true);
    }
    
    private void setTableData(Object[][] data, Object[] headings) {
        String head[] = new String[headings.length];
        for(int i = 0; i < headings.length; i++) {
            head[i] = (String) headings[i];
        }
        searchColumnComboBox = new JComboBox<>(head);
        table = new HelpTable(this, data, headings);
    }
    
    private String generateQuery(String columns, String appendQuery) {
        String query = "";
        query = "SELECT "+columns+" "+appendQuery;
        return query;
    }
    
    /**
     * Get the result of the selected column
     * @return returns a HashMap with keys as name of the return columns. 
     * <br>If the user has closed HelpDialog. i.e. User have not selected any option, this will return null
     * <b> Example :</b>
     * <pre>
     * {@code 
     *      HashMap<String, String> result = dialog.getResult();
     *      if(result == null) {
     *          // User has cancelled HelpDialog
     *      }
     *      else {
     *          // User has successfully completed operation.
     *          // Do your operation here
     *      }
     * }
     * </pre>
     */
    public HashMap<String, String> getResult() {
        return map;
    }
    
    class HelpTable extends JTable {
        
        private TableRowSorter<DefaultTableModel> sorter;
        private JPopupMenu popUpMenu;
        private int popUpClick = 0;
        private Object[][] data;
        
        HelpTable(HelpDialog dialog, Object[][] data, Object[] headings) {
            DefaultTableModel model = new DefaultTableModel(data, headings) {
                @Override
                public boolean isCellEditable(int i, int i1) {
                    return false;
                }                
            };
            this.data = data;
            sorter = new TableRowSorter<>(model);
            setModel(model);
            setRowSorter(sorter);
            
            popUpMenu = new JPopupMenu();
            JMenuItem sum = new JMenuItem("Sum");
            JMenuItem avg = new JMenuItem("Average");
            popUpMenu.add(sum);
            popUpMenu.add(avg);
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    if(me.getClickCount() == 2) {
                        map = new HashMap<>();
                        JTable table = (JTable) me.getSource();
                        //int row = table.rowAtPoint(me.getPoint());
                        int row=table.getSelectedRow();
                        if (table.getRowSorter()!=null) {
                            row = table.getRowSorter().convertRowIndexToModel(row);
                        }
                        for(int i = 0; i < returnDataHeadings.length; i++) {
                            map.put(returnDataHeadings[i], returnData[row][i]);
                        }
                        dialog.setVisible(false);
                    }
                }

                @Override
                public void mousePressed(MouseEvent me) {
                    showPopUp(me);
                }

                @Override
                public void mouseReleased(MouseEvent me) {
                    showPopUp(me);
                }

                @Override
                public void mouseEntered(MouseEvent me) {
                }

                @Override
                public void mouseExited(MouseEvent me) {
                }
                
                private void showPopUp(MouseEvent e) {
                    if(e.isPopupTrigger()) {
                        JTable table =(JTable) e.getSource();
                        Point p = e.getPoint();
                        popUpClick = table.columnAtPoint(p);
                        popUpMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            
            sum.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    try{
                        JOptionPane.showMessageDialog(null, "Sum is "+getSum());
                    }catch(Exception e) {
                        JOptionPane.showMessageDialog(null, "Cannot Find Sum");
                    }
                }
            });
            
            avg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    try{
                        JOptionPane.showMessageDialog(null, "Average is " + (getSum()/data.length));
                    }catch(Exception e) {
                        JOptionPane.showMessageDialog(null, "Cannot Find Average");
                    }
                }
            });
            
        }
        
        private double getSum() {
            double s = 0;
            for(int i = 0; i < data.length; i++) {
                String temp = data[i][popUpClick].toString();
                double num = Double.valueOf(temp);
                s = s + num;
            }
            return s;
        }
        
        public void filter(int column) {
            RowFilter<DefaultTableModel, Object> rf = null;
            try {
                if(filterRadioButton.isSelected()) {
                    rf = RowFilter.regexFilter(searchBar.getText(), column);
                }
                else {
                    rf = RowFilter.regexFilter("^" + searchBar.getText(), column);
                }
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
            sorter.setRowFilter(rf);
        }
        
    }
}

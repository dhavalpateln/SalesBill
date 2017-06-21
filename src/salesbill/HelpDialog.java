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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Dhaval
 */
public class HelpDialog extends JDialog {
    
    private JTextField searchBar;
    private JComboBox<String> searchColumnComboBox;
    private HelpTable table;
    private int searchColumn = 0;
    private int filterType = 0;
    
    private static final int FILTER = 0;
    private static final int FIND = 1;
    
    public HelpDialog(JFrame frame, Object[][] data, Object[] headings) {
        super(frame, "Help", true);
        setSize(500, 500);
        
        setTableData(data, headings);
        searchBar = new JTextField();        
        createGUI();
    }
    
    private void createGUI() {
        
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
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(searchColumnComboBox, BorderLayout.WEST);
        panel.add(searchBar, BorderLayout.CENTER);
        
        add(panel, BorderLayout.NORTH);
    }
    
    private void setTableData(Object[][] data, Object[] headings) {
        String head[] = new String[headings.length];
        for(int i = 0; i < headings.length; i++) {
            head[i] = (String) headings[i];
        }
        searchColumnComboBox = new JComboBox<>(head);
        table = new HelpTable(data, headings);
    }
    
    class HelpTable extends JTable {
        
        private TableRowSorter<DefaultTableModel> sorter;
        private JPopupMenu popUpMenu;
        private int popUpClick = 0;
        private Object[][] data;
        
        HelpTable(Object[][] data, Object[] headings) {
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
                if(filterType == FILTER) {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesbill;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
    private HelpTable table;
    private int totalColumns = 0;
    
    public HelpDialog(JFrame frame, Object[][] data, Object[] headings) {
        super(frame, "Help", true);
        setSize(500, 500);
        
        totalColumns = headings.length;
        table = new HelpTable(data, headings);
        searchBar = new JTextField();
        
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                table.filter();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                table.filter();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                table.filter();
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(searchBar, BorderLayout.NORTH);
    }
    class HelpTable extends JTable {
        
        private TableRowSorter<DefaultTableModel> sorter;
        
        HelpTable(Object[][] data, Object[] headings) {
            DefaultTableModel model = new DefaultTableModel(data, headings);
            sorter = new TableRowSorter<>(model);
            setModel(model);
            setRowSorter(sorter);
        }
        
        public void filter() {
            RowFilter<DefaultTableModel, Object> rf = null;
            int c[] = new int[totalColumns];
            for(int i = 0; i < c.length; i++) {
                c[i] = i;
            }
            try {
                rf = RowFilter.regexFilter(searchBar.getText(), c);
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
            sorter.setRowFilter(rf);
        }
        
    }
}

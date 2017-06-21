/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesbill;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Dhaval
 */
public class driver {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Test");
        JButton button = new JButton("Click");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Object[][] data = { 
                                    {"asd","zxc","qwert",2},
                                    {"tybt","qcqeas","plmfk",7},
                                    {10,"ascce","6yhnv",11},
                                    {"czxcasd","zxadwxac","qwert10",10}};
                Object[] heading = {"first","second","third","fourth"};
                HelpDialog dialog = new HelpDialog(frame,data,heading);
                dialog.setVisible(true);
            }
        });
        
        frame.add(button);
        
        frame.setVisible(true);
    }
}

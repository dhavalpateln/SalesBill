/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesbill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dhaval
 */
public class DataDbHelper {
    
    private static DataDbHelper db = null;
    private Connection conn;
    private Statement st;
    
    private DataDbHelper() {
        connect();
    }
    
    public static DataDbHelper getInstance() {
        if(db == null) {
            db = new DataDbHelper();
        }
        return db;
    }
    
    private void connect() {
        String url = "jdbc:mysql://localhost:3306/";
	String dbName = "jarvis";
	String driver = "com.mysql.jdbc.Driver";
	String userName = "root"; 
	String password = "";

        try {
            Class.forName(driver); //Register JDBC Driver
            conn = DriverManager.getConnection(url+dbName,userName,password); //create a connection object
            System.out.println("Connection Established");
            st = conn.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet executeQuery(String query) {
        try {
            return st.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(DataDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesbill;

/**
 *
 * @author Dhaval
 */
public class DataDbHelper {
    
    private static DataDbHelper db = null;
    
    
    
    private DataDbHelper() {
        connect();
    }
    
    public DataDbHelper getInstance() {
        if(db == null) {
            db = new DataDbHelper();
        }
        return db;
    }
    
    private void connect() {
        
    }
    
}

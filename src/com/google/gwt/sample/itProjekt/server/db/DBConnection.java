package com.google.gwt.sample.itProjekt.server.db;


import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;


public class DBConnection {

   
     // TODO: Notiz zu Urheberschaft
    private static Connection con = null;

   
   
    public static Connection connection() {
       
        if (con == null) {
            
            try {
                if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                    
                    Class.forName("com.mysql.jdbc.GoogleDriver");           
                    con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:europe-west1:itprojektdb/itpdb2?user=root&password=root");
                    System.out.println("Verbindung zur google Datenbank hergestellt");
                    
                } else {
                	
                	Class.forName("com.mysql.jdbc.Driver");
                	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_schema", "root", "itprojekt");
                    System.out.println("lokale Verbindung hergestellt");               
                }
                
                
            } catch (Exception e) {
                con = null;
                e.printStackTrace();
                System.out.println("Runtime Problem incoming");
                throw new RuntimeException(e.getMessage());
                
            }
        }

   
        return con;
    }

}

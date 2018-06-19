package com.google.gwt.sample.itProjekt.server.db;


import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;


public class DBConnection {

   
     
    private static Connection con = null;

   
   
    public static Connection connection() {
       
        if (con == null) {
            
            try {
                if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                    
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                    //das ist die meiner Meinung nach richtigste
                    //con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:europe-west1:itprojektdb/itpdb2?user=root&password=root");
					//so hats Thies
					//con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:itprojektdb/itpdb2?user=root&password=root");
					// notfalls wenn alle Stricke reissen
					//con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:itprojektdb/itpdb2","root","root");
					//Joshi probiers mal so... will den Fehlercode sehen
                   // con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:itpdb2","root","root");
                    con = DriverManager.getConnection("jdbc:google:rdbms://it-projekt-gruppe-10-203610:europe-west1:itprojektdb/itpdb2","root","root");
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

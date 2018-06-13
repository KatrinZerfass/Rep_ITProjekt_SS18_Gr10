package com.google.gwt.sample.itProjekt.server.db;


import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.client.Window;


public class DBConnection {

   
     
    private static Connection con = null;

   
   
    public static Connection connection() {
       
        if (con == null) {
            
            try {
                if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                    
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                    Window.alert("gleich wirds spannend");
                  //  con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:itprojektdb/itpdb2?user=root&password=root");
                    con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:europe-west1:itprojektdb/itpdb2?user=root&password=root");
                    Window.alert("lief wohl durch hier");
                } else {
                	Class.forName("com.mysql.jdbc.Driver");
                	Window.alert("gleich wirds spannend lokal");
                	con = DriverManager.getConnection("jdbc:mysql://35.233.24.130/itpdb2", "root", "root");
                   Window.alert("da lief was schief");
                   
                }
                
                
            } catch (Exception e) {
                con = null;
                e.printStackTrace();
                Window.alert("Runtime Problem incoming");
                throw new RuntimeException(e.getMessage());
                
            }
        }

   
        return con;
    }

}


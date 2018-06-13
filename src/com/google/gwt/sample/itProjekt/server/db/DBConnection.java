package com.google.gwt.sample.itProjekt.server.db;


import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.client.Window;


public class DBConnection {

   
     
    private static Connection con = null;

   
    private static String googleUrl = "jdbc:google:mysql://it-projekt-gruppe-10-203610:europe-west1:itprojectdb/itpdb2?user=root&password=root";
    private static String googleUrl2 = "jdbc:mysql://it-projekt-gruppe-10-203610:europe-west1:itprojektdb/itpdb2";
    private static String googleUrl3 = "jdbc:google:mysql://$it-projekt-gruppe-10-203610:europe-west1:itprojektdb/$itpdb2?user=$root&amp;password=$root";
   
    
    //private static String localUrl = "jdbc:mysql://127.0.0.1:3306/bankproject?user=demo&password=demo";
    private static String localUrl2 = "jdbc:mysql://35.233.24.130/it-projekt-gruppe-10-203610:europe-west1:itprojectdb/itpdb2?user=root&password=root";
    
    public static Connection connection() {
       
        if (con == null) {
            String url = null;
            try {
                if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                    
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                    con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:itprojektdb/itpdb2?user=root&password=root");
                    Window.alert("lief wohl durch hier");
                } else {
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


package com.google.gwt.sample.itProjekt.server.db;


import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;


public class DBConnection {

   
     
    private static Connection con = null;

   
    private static String googleUrl = "jdbc:google:mysql://it-projekt-gruppe-10-203610:europe-west1:itprojectdb/itpdb?user=root&password=root";
    private static String googleUrl2 = "jdbc:mysql://it-projekt-gruppe-10-203610:europe-west1:itprojektdb/itpdb";
    private static String localUrl = "jdbc:mysql://127.0.0.1:3306/bankproject?user=demo&password=demo";

    
    public static Connection connection() {
       
        if (con == null) {
            String url = null;
            try {
                if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                    
                    Class.forName("com.mysql.jdbc.GoogleDriver");
                    url = googleUrl;
                } else {
                   
                    Class.forName("com.mysql.jdbc.Driver");
                    url = localUrl;
                }
                
                con = DriverManager.getConnection(url);
            } catch (Exception e) {
                con = null;
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }

   
        return con;
    }

}


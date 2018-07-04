package com.google.gwt.sample.itProjekt.server.db;


import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;

/**
 * Verwalten einer Verbindung zur Datenbank.
 * <p>
 * <b>Vorteil:</b> Sehr einfacher Verbindungsaufbau zur Datenbank.
 * <p>
 * <b>Nachteil:</b> Durch die Singleton-Eigenschaft der Klasse kann nur auf eine
 * fest vorgegebene Datenbank zugegriffen werden.
 * <p>
 * In der Praxis kommen die meisten Anwendungen mit einer einzigen Datenbank
 * aus. Eine flexiblere Variante für mehrere gleichzeitige
 * Datenbank-Verbindungen wäre sicherlich leistungsfähiger. Dies würde
 * allerdings den Rahmen dieses Projekts sprengen bzw. die Software unnötig
 * verkomplizieren, da dies für diesen Anwendungsfall nicht erforderlich ist.
 * 
 * @author Thies
 */
public class DBConnection {

   
	/**
     * Die Klasse DBConnection wird nur einmal instantiiert. Man spricht hierbei
     * von einem sogenannten <b>Singleton</b>.
     * <p>
     * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
     * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
     * speichert die einzige Instanz dieser Klasse.
     * 
     */
    private static Connection con = null;

    /**
     * Diese statische Methode kann aufgrufen werden durch
     * <code>DBConnection.connection()</code>. Sie stellt die
     * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine
     * einzige Instanz von <code>DBConnection</code> existiert.
     * <p>
     * 
     * <b>Fazit:</b> DBConnection sollte nicht mittels <code>new</code>
     * instantiiert werden, sondern stets durch Aufruf dieser statischen
     * Methode.
     * <p>
     * 
     * <b>Nachteil:</b> Bei Zusammenbruch der Verbindung zur Datenbank - dies
     * kann z.B. durch ein unbeabsichtigtes Herunterfahren der Datenbank
     * ausgelöst werden - wird keine neue Verbindung aufgebaut, so dass die in
     * einem solchen Fall die gesamte Software neu zu starten ist. In einer
     * robusten Lösung würde man hier die Klasse dahingehend modifizieren, dass
     * bei einer nicht mehr funktionsfähigen Verbindung stets versucht würde,
     * eine neue Verbindung aufzubauen. Dies würde allerdings ebenfalls den
     * Rahmen dieses Projekts sprengen.
     * 
     * @return DAS <code>DBConncetion</code>-Objekt.
     * @see con
     */
   
    public static Connection connection() {
       
        if (con == null) {
            
            try {
                if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                	 // Load the class that provides the new
                    // "jdbc:google:mysql://" prefix.
                	// Google Driver
                    Class.forName("com.mysql.jdbc.GoogleDriver"); 
                    //dieser Link baut die DB Verbindung zu Google auf anhand des Instanznamen, des Usernames und des Passwords -> unsicher weil Password hard gecodet
                    con = DriverManager.getConnection("jdbc:google:mysql://it-projekt-gruppe-10-203610:europe-west1:itprojektdb/itpdb2?user=root&password=root");
                    System.out.println("Verbindung zur Google Datenbank hergestellt");
                    
                } else {
                	// Load the class that provides the new
                    // "jdbc:mysql://" prefix. for the local database
                	// Local MySQL Driver
                	Class.forName("com.mysql.jdbc.Driver");
                	//dieser Link baut die DB Verbindung zu einer lokalen Datenbank auf anhand des Instanznamen, des Usernames und des Passwords -> Erleichtert das Testen
                	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_schema", "root", "itprojekt");
                    System.out.println("Lokale Verbindung hergestellt");               
                }
                
                
            } catch (Exception e) {
                con = null;
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
                
            }
        }

        // Zurückgegeben der Verbindung
        return con;
    }

}

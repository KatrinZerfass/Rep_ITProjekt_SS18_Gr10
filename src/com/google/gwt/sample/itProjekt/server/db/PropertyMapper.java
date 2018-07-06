package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;


public class PropertyMapper {

	/**
	 * Konstruktor für den PropertyMapper (Singleton)
	 * static weil Singleton. Einzige Instanz dieser Klasse
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	private static PropertyMapper  propertymapper = null;
	
	/**
	 * Falls noch kein ProperyMapper existiert erstellt er ein neuen PropertyMapper und gibt ihn zurück
	 * 
	 * @return erstmalig erstellter PropertyMapper
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	public static PropertyMapper propertyMapper() {
		if (propertymapper == null){
			propertymapper = new PropertyMapper();
		}
		return propertymapper;
		}


/**
 * Findet Property durch eine P_ID und speichert die dazugehörigen Werte (P_ID und type) in einem Property Objekt ab und gibt dieses wieder
 * 
 * @param property übergebenes Property Objekt mit Attribut P_ID
 * @return Ein vollständiges Property Objekt
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */
public Property findByID(Property property){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT P_ID, type, C_ID FROM T_Property WHERE P_ID ="+ property.getId() + " ORDER BY P_ID");
		if (rs.next()){
			Property p = new Property();
			p.setId(rs.getInt("P_ID"));
			p.setType(rs.getString("type"));
			p.setContactID(rs.getInt("C_ID"));
			return p;	
		}
	}
	catch (SQLException e2){
		e2.printStackTrace();
		return null;
	}
	return null;
}

/**
 * Gibt alle Property Objekte zurück welche mit P_ID, type und C_ID befüllt sind
 * Hierfür holen wir die Attribute aus der T_Property Tabelle und speichern diese in einem Property Objekt ab und fügen diese dem Vector hinzu
 * Am Ende geben wir diesen Vector zurück
 * 
 * @return Ein Vector voller Property Objekte welche befüllt sind
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */

public Vector<Property> findAll(){
Connection con = DBConnection.connection();
Vector<Property> result = new Vector<Property>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT P_ID, type, C_ID FROM T_Property ORDER BY P_ID");
			
			while (rs.next()){
				Property p = new Property();
				p.setId(rs.getInt("P_ID"));
				p.setType(rs.getString("type"));
				p.setContactID(rs.getInt("C_ID"));
				
				result.addElement(p);
			}		
		}catch(SQLException e2){
			e2.printStackTrace();
		}
		return result;
	}

/**
 * Findet alle P_ID, type und C_ID wo die C_ID der ID des übergebenen Objekte entspricht
 * Befüllt das Property Objekt mit den Attributen und fügt dieses Objekt dem Vector hinzu
 * Gibt ein Vector voller Property Objekte zurück
 *
 * @param contact übergebenes Contact Objekt mit Attribut C_ID
 * @return Ein Vector voller Property Objekte welche befüllt sind
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */

public Vector<Property> findAllByCID(Contact contact){
Connection con = DBConnection.connection();
Vector<Property> result = new Vector<Property>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT P_ID, type, C_ID FROM T_Property WHERE C_ID=" 
			+ contact.getId()
			+" ORDER BY P_ID");
			
			while (rs.next()){
				Property p = new Property();
				p.setId(rs.getInt("P_ID"));
				p.setType(rs.getString("type"));
				p.setContactID(rs.getInt("C_ID"));
				
				result.addElement(p);
			}		
		}catch(SQLException e2){
			e2.printStackTrace();
		}
		return result;
	}

/**
 * Gibt alle Proeprty Objekte zurück die mit den Default Werten befüllt sind
 * Hierfür sind alle Default Property Werte mit der C_ID von 20000000 verknüpft
 * Alle Attribute die zu dieser C_ID von 20000000 gehören holen wir aus der T_Property Tabelle (P_ID, type und C_ID)
 * und speichern diese in einem Property Objekt ab und fügen diese dem Vector hinzu
 * Am Ende geben wir diesen Vector zurück
 * 
 * @return Ein Vector voller Property Objekte welche befüllt sind
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */

public Vector<Property> findAllDefault(){
Connection con = DBConnection.connection();
Vector<Property> result = new Vector<Property>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT P_ID, type, C_ID FROM T_Property WHERE C_ID=20000000 ORDER BY P_ID");
			
			while (rs.next()){
				Property p = new Property();
				p.setId(rs.getInt("P_ID"));
				p.setType(rs.getString("type"));
				p.setContactID(rs.getInt("C_ID"));
				
				result.addElement(p);
			}		
		}catch(SQLException e2){
			e2.printStackTrace();
		}
		return result;
	}

/**
 * Sucht nach der höchsten P_ID um diese um eins zu erhöhen und als neue P_ID zu nutzen
 * Befüllt T_Property mit P_ID, type und C_ID
 * Eine Property wird zum Schluss zurückgegeben
 * 
 * @param property übergebenes Property Objekt mit Attributen P_ID und type 
 * @param contact übergebenes Contact Objekt mit Attribut C_ID
 * @return Ein vollständiges Property Objekt
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */


public Property insert(Property property, Contact contact){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT MAX(P_ID) AS maxpid FROM T_Property");
		if (rs.next()){
			
			property.setId(rs.getInt("maxpid")+1);
			Statement stmt2 = con.createStatement();
			stmt2.executeUpdate("INSERT INTO T_Property (P_ID, type, C_ID)"
			+ " VALUES ("
			+ property.getId() 
			+ ", '" 
			+ property.getType() 
			+ "', " 
			+ contact.getId() 
			+ ")") ;
					
			return property;	
			
		}
	}
	catch (SQLException e2){
		e2.printStackTrace();
		return property;
	}
	return property;}

/**
 * Update von Veränderungen falls sich type ändert
 * Gibt ein Property zurück
 * 
 * @param property übergebenes Property Objekt mit Attributen P_ID und type
 * @return Ein vollständiges Property Objekt
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */

public Property update(Property property){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		stmt.executeUpdate("UPDATE T_Property SET type ='"
		+ property.getType()
		+ "', "
		+ "WHERE P_ID =" + property.getId());
	}

catch (SQLException e2){
	e2.printStackTrace();
	return property;
}
return property;}

/**
 * Entfernt alles aus T_Property wo die P_ID der ID des übergebenen Objekts entspricht
 * 
 * @param property übergebenes Property Objekt mit Attribut P_ID
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */

public void delete (Property property){
	Connection con = DBConnection.connection();
				
				try{
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate("DELETE FROM T_Property WHERE P_ID =" + property.getId());
					
				}
			
			catch (SQLException e2){
				e2.printStackTrace();
				}
			}

/**
 * Findet alle P_ID, type und C_ID in welcher der type dem type des übergebenen Objektes entspricht
 * Befüllt Property Objekte mit den Attributen und gibt einen damit gefüllten Vektor wieder
 * 
 * @param property übergebenes Property Objekt mit Attribut type
 * @return Ein Vektor voller vollständiger Property Objekt
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */
public Vector<Property> findByTypeAndCID(Property property, Contact contact){
	Connection con = DBConnection.connection();
	Vector <Property> result = new Vector <Property>();
	Property p = new Property();
	
	try{
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT P_ID, type, C_ID FROM T_Property WHERE type ='"+ property.getType() + "' AND C_ID="+contact.getId()+ "ORDER BY P_ID");
		while (rs.next()){
			
			p.setId(rs.getInt("P_ID"));
			p.setType(rs.getString("type"));
			p.setContactID(rs.getInt("C_ID"));
			result.addElement(p);	
		}
		
	}
	catch (SQLException e2){
		e2.printStackTrace();
		return result;
	}
	return result;
}
/**
 * Findet alle P_ID, type und C_ID in welcher der type dem type des übergebenen Objektes entspricht
 * Befüllt Property Objekte mit den Attributen und gibt einen damit gefüllten Vektor wieder
 * 
 * @param property übergebenes Property Objekt mit Attribut type
 * @return Ein Vektor voller vollständiger Property Objekt
 * 
 * @author Egor Krämer
 * @author Robert Mattheis
 */
public Vector<Property> findByType(Property property){
	Connection con = DBConnection.connection();
	Vector <Property> result = new Vector <Property>();
	Property p = new Property();
	
	try{
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT P_ID, type, C_ID FROM T_Property WHERE type ='"+ property.getType() + "' ORDER BY P_ID");
		while (rs.next()){
			
			p.setId(rs.getInt("P_ID"));
			p.setType(rs.getString("type"));
			p.setContactID(rs.getInt("C_ID"));
			result.addElement(p);	
		}
		
	}
	catch (SQLException e2){
		e2.printStackTrace();
		return result;
	}
	return result;
}
}
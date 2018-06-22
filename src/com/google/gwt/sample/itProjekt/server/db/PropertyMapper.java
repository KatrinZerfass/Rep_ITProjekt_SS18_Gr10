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

	/** Konstruktor f�r den PropertyMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static PropertyMapper  propertymapper = null;
	
	/**
	 * PropertyMapper.
	 *
	 *Falls noch kein ProperyMapper existiert erstellt er ein neuen PropertyMapper und gibt ihn zur�ck
	 * 
	 */
	public static PropertyMapper propertyMapper() {
		if (propertymapper == null){
			propertymapper = new PropertyMapper();
		}
		return propertymapper;
		}


/**
 * FindByID.
 *
 *Findet Property durch eine P_ID und speichert die dazugeh�rigen Werte (P_ID und type) in einem Property Objekt ab und gibt dieses wieder
 *
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
 * FindAll.
 *
 * Gibt alle Property Objekte zur�ck welche mit P_ID, type und C_ID bef�llt sind
 * Hierf�r holen wir die Attribute aus der T_Property Tabelle und speichern diese in einem Property Objekt ab und f�gen diese dem Vector hinzu
 * Am Ende geben wir diesen Vector zur�ck
 *
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
 * FindAllByCID.
 * 
 * Findet alle P_ID, type und C_ID wo die C_ID der ID des �bergebenen Objekte entspricht
 * Bef�llt das Property Objekt mit den Attributen und f�gt dieses Objekt dem Vector hinzu
 * Gibt ein Vector voller Property Objekte zur�ck
 *
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
 * FindAllDefault.
 *
 * Gibt alle Proeprty Objekte zur�ck die mit den Default Werten bef�llt sind
 * Hierf�r sind alle Default Property Werte mit der C_ID von 20000000 verkn�pft
 * Alle Attribute die zu dieser C_ID von 20000000 geh�ren holen wir aus der T_Property Tabelle (P_ID, type und C_ID)
 * und speichern diese in einem Property Objekt ab und f�gen diese dem Vector hinzu
 * Am Ende geben wir diesen Vector zur�ck
 *
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
 * Insert.
 *
 * Sucht nach der h�chsten P_ID um diese um eins zu erh�hen und als neue P_ID zu nutzen
 * Bef�llt T_Property mit P_ID, type und C_ID
 * Eine Property wird zum Schluss zur�ckgegeben
 *
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
 * Update.
 *
 * Update von Ver�nderungen falls sich type �ndert
 * Gibt ein Property zur�ck
 * 
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
 * Delete.
 *
 * Entfernt alles aus T_Property wo die P_ID der ID des �bergebenen Objekts entspricht
 * 
 */

public void delete (Property property){
	Connection con = DBConnection.connection();
				
				try{
					
					Statement stmt = con.createStatement();
					if(property.getContactID() != 20000000){
						
					stmt.executeUpdate("DELETE FROM T_Property WHERE P_ID =" + property.getId());
					
					}
				}
			
			catch (SQLException e2){
				e2.printStackTrace();
				}
			}
public Property findByType(Property property){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT P_ID, type, C_ID FROM T_Property WHERE type ='"+ property.getType() + "' AND C_ID=20000000 ORDER BY P_ID");
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
}
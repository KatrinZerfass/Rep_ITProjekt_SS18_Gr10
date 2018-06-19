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

	/** Konstruktor für den PropertyMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static PropertyMapper  propertymapper = null;
	
	/**
	 * PropertyMapper.
	 *
	 *Falls noch kein ProperyMapper existiert erstellt er ein neuen PropertyMapper und gibt ihn zurück
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
 *Findet Property durch eine P_ID und speichert die dazugehörigen Werte (P_ID und type) in einem Property Objekt ab und gibt dieses wieder
 *
 */
public Property findByID(Property property){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT P_ID, type FROM T_Property WHERE P_ID ="+ property.getId() + " ORDER BY P_ID");
		if (rs.next()){
			Property p = new Property();
			p.setId(rs.getInt("P_ID"));
			p.setType(rs.getString("type"));
			return p;	
		}
	}
	catch (SQLException e2){
		e2.printStackTrace();
		return null;
	}
	return null;
}
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
public Vector<Property> findAllByCID(Contact c){
Connection con = DBConnection.connection();
Vector<Property> result = new Vector<Property>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT P_ID, type, C_ID FROM T_Property WHERE C_ID=" 
			+ c.getId()
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
public Property insert(Property p, Contact c){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT MAX(P_ID) AS maxpid FROM T_Property");
		if (rs.next()){
			
			p.setId(rs.getInt("maxpid")+1);
			Statement stmt2 = con.createStatement();
			stmt2.executeUpdate("INSERT INTO T_Property (P_ID, type, C_ID)"
			+ " VALUES ("
			+ p.getId() 
			+ ", '" 
			+ p.getType() 
			+ "', " 
			+ c.getId() 
			+ ")") ;
					
			return p;	
			
		}
	}
	catch (SQLException e2){
		e2.printStackTrace();
		return p;
	}
	return p;}
public Property update(Property p){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		stmt.executeUpdate("UPDATE T_Property SET type ='"
		+ p.getType()
		+ "', "
		+ "WHERE P_ID =" + p.getId());
	}

catch (SQLException e2){
	e2.printStackTrace();
	return p;
}
return p;}
public void delete (Property p){
	Connection con = DBConnection.connection();
				
				try{
					
					Statement stmt = con.createStatement();
					stmt.executeUpdate("DELETE FROM T_Property WHERE P_ID =" + p.getId());
				}
			
			catch (SQLException e2){
				e2.printStackTrace();
				}
			}
}
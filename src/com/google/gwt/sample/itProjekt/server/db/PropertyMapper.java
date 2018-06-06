package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.sample.itProjekt.shared.bo.Property;

/**
 * The Class PropertyMapper.
 */
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
 * Find by ID.
 *
 * @param pr the pr
 * @return the property
 */
public Property findByID(Property pr){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT P_ID, type FROM T_Property WHERE P_ID ="+ pr.getId() + " ORDER BY P_ID");
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

}
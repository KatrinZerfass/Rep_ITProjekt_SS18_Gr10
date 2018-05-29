package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.sample.itProjekt.shared.bo.Property;

public class PropertyMapper {
private static PropertyMapper propertymapper = null;
	
	public static PropertyMapper propertyMapper() {
		if (propertymapper == null){
			propertymapper = new PropertyMapper();
		}
		return propertymapper;
		}


public Property findByID(Property pr){
	Connection con = DBConnection.connection();
	
	try{
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT p_id, type From T_Property where p_id ="+ pr.getId() + " order by P_ID");
		if (rs.next()){
			Property p = new Property();
			p.setId(rs.getInt("p_id"));
			p.setType(rs.getString("type"));
			return p;	
		}
	}
	catch (SQLException e2){
		e2.printStackTrace();
		return null;
	}
	return null;
}}
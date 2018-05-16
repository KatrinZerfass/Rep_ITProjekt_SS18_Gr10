package com.google.gwt.sample.itProjekt.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.sql.Timestamp;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.User;

public class UserMapper {
	
	private static UserMapper usermapper = null;
	
	protected UserMapper UserMapper() {
		if (usermapper == null){
			usermapper = new UserMapper();
		}
		
		return usermapper;
		}
	
	public User findByID(int uid){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select u_id, firstname, lastname, username from T_User where u_id ="+ uid + " order by U_ID");
			
			if (rs.next()){
				User u = new User();
				u.setId(rs.getInt("u_id"));
				u.setFirstname(rs.getString("firstname"));
				u.setLastname(rs.getString("lastname"));
				u.setUsername("username");
				
				return u;	
			}
		}
		
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	public User findByUserName(String username){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select u_id, username, firstname, lastname from T_User where username ="+ username + " order by U_ID");
			if (rs.next()){
				User u = new User();
				u.setId(rs.getInt("u_id"));
				u.setFirstname(rs.getString("firstname"));
				u.setLastname(rs.getString("lastname"));
				u.setUsername(rs.getString("username"));
	
				return u;	
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	
	
	

}



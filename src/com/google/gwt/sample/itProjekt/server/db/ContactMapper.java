package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.User;

public class ContactMapper {
	
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static ContactMapper  contactmapper = null;
	
	public static ContactMapper contactMapper() {
		if (contactmapper == null){
			contactmapper = new ContactMapper();
		}
		return contactmapper;
		}
	public Contact findByID(Contact contact){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID From T_Contact where C_ID ="+ contact.getId() + " order by C_ID");
			if (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				//c.setParticipant(rs.getInt("U_ID"));
				return c;	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	public Vector<Contact> findAll(){
Connection con = DBConnection.connection();
Vector<Contact> result = new Vector<Contact>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID From T_Contact order by C_ID");
			
			while (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				//c.setParticipant(rs.getInt("U_ID"));
				result.addElement(c);
			}		
		}catch(SQLException e2){
			e2.printStackTrace();
		}
		return result;
	}
	
	public Vector<Contact> findByName(Contact contact){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID From T_Contact where firstName ='"+ contact.getFirstname()+ "' AND lastName='" + contact.getLastname() + "' order by C_ID");
			while (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				//c.setParticipant(rs.getInt("U_ID"));
				result.addElement(c);	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return result;
		}
		return result;
	}
	public Vector<Contact> findAllByUID(User user){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID From T_Contact where U_ID=" + user.getId()+ " order by C_ID");
					
					while (rs.next()){
						Contact c = new Contact();
						c.setId(rs.getInt("C_ID"));
						c.setFirstname(rs.getString("firstName"));
						c.setLastname(rs.getString("lastName"));
						c.setSex(rs.getString("gender"));
						//c.setParticipant(rs.getInt("U_ID"));
						result.addElement(c);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	
	public Contact insert(Contact c, User u){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(C_ID) AS maxcid From T_Contact");
			if (rs.next()){
				
				c.setId(rs.getInt("maxcid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Contact (C_ID, firstName, lastName, gender, create_date, mod_date, U_ID)"
				+ " VALUES ("
				+ c.getId() 
				+ ", '" 
				+ c.getFirstname() 
				+ "', '" 
				+ c.getLastname() 
				+ "', '" 
				+ c.getSex() 
				+ "', " 
				+ new Timestamp(System.currentTimeMillis())  
				+ ", "
				+ new Timestamp(System.currentTimeMillis()) 
				+ ", " 
				+ u.getId()
				+ ")") ;
						
				return c;	
				// Timestamp testen...
				// placeholder f�r sowas wie: c.getOWner.getID())
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return c;
		}
		return c;}
	
		public Contact update(Contact c){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_Contact SET firstName ='"+c.getFirstname()+"', lastName ='" + c.getLastname()+ "', gender=" + c.getSex() +"', mod_date="
						+ new java.sql.Timestamp(System.currentTimeMillis()) + ", U_ID=" + 123 + " Where C_ID =" + c.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return c;
		}
		return c;}
		
		public void delete (Contact c){
Connection con = DBConnection.connection();
			
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Contact WHERE C_ID =" +c.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
		}
		
	
	
	}




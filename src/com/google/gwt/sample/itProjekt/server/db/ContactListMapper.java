package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;

public class ContactListMapper {
	
private static ContactListMapper  contactlistmapper = null;
	
	public static ContactListMapper contactListMapper() {
		if (contactlistmapper == null){
			contactlistmapper = new ContactListMapper();
		}
		return contactlistmapper;
		}
	
	public ContactList findByID(ContactList cl){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CL_ID, listname, U_ID FROM T_ContactList WHERE CL_ID =" + cl.getId() + " ORDER BY CL_ID");
			if (rs.next()){
				ContactList c = new ContactList();
				c.setId(rs.getInt("CL_ID"));
				c.setName((rs.getString("listname")));
				
				
				return c;	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	
	public Vector<ContactList> findAll(){
		Connection con = DBConnection.connection();
		Vector<ContactList> result = new Vector<ContactList>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT CL_ID, listname, U_ID FROM T_ContactList ORDER BY CL_ID");
					
					while (rs.next()){
						ContactList c = new ContactList();
						c.setId(rs.getInt("CL_ID"));
						c.setName(rs.getString("listname"));
						result.addElement(c);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	public Vector <ContactList> findByName(String name){
		Connection con = DBConnection.connection();
		Vector<ContactList> result = new Vector<ContactList>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CL_ID, listname, U_ID FROM T_ContactList WHERE listname ='"+ name + "' ORDER BY CL_ID");
			while (rs.next()){
				ContactList c = new ContactList();
				c.setId(rs.getInt("CL_ID"));
				c.setName(rs.getString("listname"));
				
				result.addElement(c);	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return result;
		}
		return result;
	}
	public Vector <ContactList> findAllByUID(User u){
		Connection con = DBConnection.connection();
		Vector<ContactList> result = new Vector<ContactList>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CL_ID, listname, U_ID FROM T_ContactList WHERE U_ID=" + u.getId() + " ORDER BY CL_ID");
			while (rs.next()){
				ContactList c = new ContactList();
				c.setId(rs.getInt("CL_ID"));
				c.setName(rs.getString("listname"));
				result.addElement(c);	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return result;
		}
		return result;
	}
	public ContactList insert(ContactList c, User u){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(CL_ID) AS maxclid FROM T_ContactList");
			if (rs.next()){
				
				c.setId(rs.getInt("maxclid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_ContactList (CL_ID, listname, U_ID)"
				+ " VALUES ("
				+ c.getId() 
				+ ", '" 
				+ c.getName() 
				+ "', "
				+ u.getId() 
				+ ")") ;
						
				return c;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return c;
		}
		return c;}
	
		public ContactList update(ContactList c){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_ContactList SET listname ='" + c.getName() + "' " + "WHERE CL_ID =" + c.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return c;
		}
		return c;}
		
		public void delete (ContactList c){
Connection con = DBConnection.connection();
//TODO: Add Permissions
			
		try{
	
				Statement stmt2 = con.createStatement();
					stmt2.executeUpdate("DELETE FROM T_Permission_Contactslist WHERE CL_ID =" + c.getId());
			}

			catch (SQLException e2){
				e2.printStackTrace();

				}	
		try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_ContactsList WHERE CL_ID =" + c.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}}
		
		public Vector <Contact> getAllContacts(ContactList cl){
			Connection con = DBConnection.connection();
			
			Vector<Contact> result = new Vector<Contact>();
			
			try{
				Statement stmt = con.createStatement();
				Statement stmt2 = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID FROM T_Contact_Contactlist WHERE CL_ID =" + cl.getId() + " ORDER BY C_ID");

				
				
				while (rs.next()){
										
					ResultSet rs2 = stmt2.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID FROM T_Contact WHERE C_ID =" + rs.getInt("C_ID") + " ORDER BY C_ID");
					Contact c = new Contact();
					c.setId(rs2.getInt("C_ID"));
					c.setFirstname(rs2.getString("firstName"));
					c.setLastname(rs2.getString("lastName"));
					c.setSex(rs2.getString("gender"));
					result.addElement(c);
				}
				
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return result;
			}
			return result;
		}	
		public ContactList addContact(ContactList cl, Contact c){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO T_Contact_ContactList (CL_ID, C_ID)"
				+ " VALUES ('"
				+ cl.getId() 
				+ "', '" 
				+ c.getId() 
				+ "')") ;
						
				return cl;	
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return cl;
			}
			}
		public ContactList removeContact(ContactList cl, Contact c){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Contact_ContactList WHERE CL_ID="
				+ cl.getId() 
				+ " AND C_ID=" 
				+ c.getId());
						
				return cl;	
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return cl;
			}
			}
		
}

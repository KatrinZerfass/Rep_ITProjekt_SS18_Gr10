package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;

public class ContactListMapper {
	
private static ContactListMapper contactlistmapper = null;
	
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
			ResultSet rs = stmt.executeQuery("SELECT cl_id, listname, U_ID From T_ContactList where cl_id ="+ cl.getId() + " order by CL_ID");
			if (rs.next()){
				ContactList c = new ContactList();
				c.setId(rs.getInt("cl_id"));
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
	
	public ContactList findByID(int cl){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT cl_id, listname, U_ID From T_ContactList where cl_id ="+ cl + " order by CL_ID");
			if (rs.next()){
				ContactList c = new ContactList();
				c.setId(rs.getInt("cl_id"));
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
					ResultSet rs = stmt.executeQuery("SELECT cl_id, listname, U_ID From T_ContactList order by CL_ID");
					
					while (rs.next()){
						ContactList c = new ContactList();
						c.setId(rs.getInt("cl_id"));
						c.setName(rs.getString("listname"));
						// Besitzer soll als User Objekt weitergegeben werden? warum nicht einfach als ID int?
						//c.setParticipant(rs.getInt("U_ID"));
						result.addElement(c);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	public Vector <ContactList> findByName(ContactList cl){
		Connection con = DBConnection.connection();
		Vector<ContactList> result = new Vector<ContactList>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT cl_id, listname, U_ID From T_Contactlist where listname ="+ cl.getName()+ " order by Cl_ID");
			while (rs.next()){
				ContactList c = new ContactList();
				c.setId(rs.getInt("cl_id"));
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
			ResultSet rs = stmt.executeQuery("SELECT cl_id, listname, U_ID From T_Contactlist where u_id ="+ u.getId()+ " order by Cl_ID");
			while (rs.next()){
				ContactList c = new ContactList();
				c.setId(rs.getInt("cl_id"));
				c.setName(rs.getString("listname"));
				// Besitzer soll als User Objekt weitergegeben werden? warum nicht einfach als ID int?
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
	public ContactList insert(ContactList c, User u){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(cl_id) AS maxclid From T_Contactlist");
			if (rs.next()){
				
				c.setId(rs.getInt("maxclid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_ContactList (cl_id, listname, u_id)"
				+ " VALUES ('"
				+ c.getId() 
				+ "', '" 
				+ c.getName() 
				+ "', '" 
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
				stmt.executeUpdate("UPDATE T_CONTACTList SET listname ='"+c.getName()+", u_id=" + 123+ " Where cl_id =" + c.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return c;
		}
		return c;}
		
		public void delete (ContactList c){
Connection con = DBConnection.connection();
			
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_CONTACTLIST WHERE CL_ID =" +c.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}}
		// TODO: ADDCONTACTS, REMOVECONTACTS, getAllContacts
		
}

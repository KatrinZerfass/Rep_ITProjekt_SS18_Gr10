package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.User;

/**
 * The Class ContactMapper.
 */
public class ContactMapper {
	
	/** Konstruktor für den ContactMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static ContactMapper  contactmapper = null;
	
	/**
	 * ContactMapper.
	 *
	 *Falls noch kein ContactMapper existiert erstellt er ein neuen ContactMapper und gibt ihn zurück
	 * 
	 */
	public static ContactMapper contactMapper() {
		if (contactmapper == null){
			contactmapper = new ContactMapper();
		}
		return contactmapper;
		}
	
	/**
	 * Find by ID.
	 *
	 * @param contact the contact
	 * @return the contact
	 */
	public Contact findByID(Contact contact){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID FROM T_Contact WHERE C_ID =" + contact.getId() + " ORDER BY C_ID");
			if (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				return c;	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	/**
	 * Find all.
	 *
	 * @return the vector
	 */
	public Vector<Contact> findAll(){
Connection con = DBConnection.connection();
Vector<Contact> result = new Vector<Contact>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID FROM T_Contact ORDER BY C_ID");
			
			while (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				result.addElement(c);
			}		
		}catch(SQLException e2){
			e2.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Find by name.
	 *
	 * @param contact the contact
	 * @return the vector
	 */
	public Vector<Contact> findByName(Contact contact){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID FROM T_Contact WHERE firstName ='" + contact.getFirstname() + "' AND lastName='" + contact.getLastname() + "' ORDER BY C_ID");
			while (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				result.addElement(c);	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return result;
		}
		return result;
	}
	
	/**
	 * Find all by UID.
	 *
	 * @param user the user
	 * @return the vector
	 */
	public Vector<Contact> findAllByUID(User user){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID FROM T_Contact WHERE U_ID=" + user.getId()+ " ORDER BY C_ID");
					
					while (rs.next()){
						Contact c = new Contact();
						c.setId(rs.getInt("C_ID"));
						c.setFirstname(rs.getString("firstName"));
						c.setLastname(rs.getString("lastName"));
						c.setSex(rs.getString("gender"));
						c.setOwner(rs.getInt("U_ID"));
						result.addElement(c);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	
	/**
	 * Insert.
	 *
	 * @param c the c
	 * @param u the u
	 * @return the contact
	 */
	public Contact insert(Contact c, User u){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(C_ID) AS maxcid FROM T_Contact");
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
				// TODO: Timestamp testen...
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return c;
		}
		return c;}
	
	
	
	
		/**
		 * Update.
		 *
		 * @param c the c
		 * @return the contact
		 */
		public Contact update(Contact c){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_Contact SET firstName ='" 
				+ c.getFirstname()
				+ "', " 
				+ "lastName ='" 
				+ c.getLastname()
				+ "', " 
				+ "gender='" 
				+ c.getSex() 
				+ "', " 
				+ "mod_date="
				+ new java.sql.Timestamp(System.currentTimeMillis()) 
				+ " WHERE C_ID =" + c.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return c;
		}
		return c;}
		
		/**
		 * Delete.
		 *
		 * @param c the c
		 */
		public void delete (Contact c){
Connection con = DBConnection.connection();

			try{
	
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("DELETE FROM T_Permission_Contact WHERE C_ID =" +c.getId());
			}

			catch (SQLException e2){
				e2.printStackTrace();

			}
			
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Contact WHERE C_ID =" +c.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
			
		}
		
	
	
	}




package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.user.client.Window;

/**
 * The Class ContactMapper.
 */
public class ContactMapper {
	
	/** Konstruktor f�r den ContactMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static ContactMapper  contactmapper = null;
	
	/**
	 * ContactMapper.
	 *
	 *Falls noch kein ContactMapper existiert erstellt er ein neuen ContactMapper und gibt ihn zur�ck
	 * 
	 */
	public static ContactMapper contactMapper() {
		if (contactmapper == null){
			contactmapper = new ContactMapper();
		}
		return contactmapper;
		}
	
	/**
	 * FindByID.
	 *
	 * Findet Contacts durch eine C_ID und speichert die dazugeh�rigen Werte (C_ID, firstName, lastName, gender und U_ID) in einem COntact Objekt ab und gibt dieses wieder
	 * 
	 */
	public Contact findByID(Contact contact){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date FROM T_Contact WHERE C_ID =" + contact.getId() + " ORDER BY C_ID");
			if (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				c.setCreationDate(rs.getTimestamp("create_date"));
				c.setModificationDate(rs.getTimestamp("mod_date"));
				return c;	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
//			return null;
		}
		return null;
	}
	
	
	/**
	 * FindAll.
	 *
	 *Gibt alle Contact Objekte zur�ck welche mit C_ID, firstName, lastName, geder und U_ID bef�llt sind
	 *Hierf�r holen wir C_ID, firstName, lastName, geder und U_ID aus der T_Contact Tabelle und speichern diese in einem Contact Objekt ab und f�gen diese dem Vector hinzu
	 *Diesen Vector bef�llt mit Contacts geben wir zur�ck
	 *
	 */
	public Vector<Contact> findAll(){
Connection con = DBConnection.connection();
Vector<Contact> result = new Vector<Contact>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date FROM T_Contact ORDER BY C_ID");
			
			while (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				c.setCreationDate(rs.getTimestamp("create_date"));
				c.setModificationDate(rs.getTimestamp("mod_date"));
				result.addElement(c);
			}		
		}catch(SQLException e2){
			e2.printStackTrace();
		}
		return result;
	}
	
	/**
	 * FindByName.
	 *
	 * Findet Contacts durch einen Namen und speichert die dazugeh�rigen Werte (C_ID, firstName, lastName, gender und U_ID) in einem Contact Objekt ab
	 * und Speichert dieses Objekt im Vector ab und gibt diesen wieder
	 * 
	 */
	public Vector<Contact> findByName(Contact contact){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date FROM T_Contact WHERE firstName ='" + contact.getFirstname() + "' AND lastName='" + contact.getLastname() + "' ORDER BY C_ID");
			while (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				c.setCreationDate(rs.getTimestamp("create_date"));
				c.setModificationDate(rs.getTimestamp("mod_date"));
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
	 * FindAllByUID.
	 *
	 * Findet alle Contacts die ein User erstellt hat durch seine U_ID
	 * Alle Values werden aus T_Contact ausgelesen und in einem Contact Objekt gespeichert und einem Vector hinzugef�gt und zur�ckgegeben
	 * Gibt ein Vector voller Contact Objekte zur�ck welche ein User erstellt hat
	 * 
	 */
	public Vector<Contact> findAllByUID(User user){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
				
				try{
					Statement stmt = con.createStatement();
					
					ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date FROM T_Contact WHERE U_ID=" + user.getId()+ " ORDER BY C_ID");
						while (rs.next()){
						
						Contact c = new Contact();
						c.setId(rs.getInt("C_ID"));
						c.setFirstname(rs.getString("firstName"));
						c.setLastname(rs.getString("lastName"));
						c.setSex(rs.getString("gender"));
						c.setOwner(rs.getInt("U_ID"));
						c.setCreationDate(rs.getTimestamp("create_date"));
						c.setModificationDate(rs.getTimestamp("mod_date"));
						
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
	 *Sucht nach der h�chsten C_ID um diese um eins zu erh�hen und als neue C_ID zu nutzen
	 *Bef�llt T_Contact mit C_ID, firstName, lastName, gender, create_date, mod_date, U_ID
	 *Ein Contact wird zur�ckgegeben
	 */
	public Contact insert(Contact contact, User user){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(C_ID) AS maxcid FROM T_Contact");
			if (rs.next()){
				
				contact.setId(rs.getInt("maxcid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Contact (C_ID, firstName, lastName, gender, create_date, mod_date, U_ID)"
				+ " VALUES ('"
				+ contact.getId() 
				+ "', '" 
				+ contact.getFirstname() 
				+ "', '" 
				+ contact.getLastname() 
				+ "', '" 
				+ contact.getSex() 
				+ "', '" 
				+ new Timestamp(System.currentTimeMillis())  
				+ "', '"
				+ new Timestamp(System.currentTimeMillis()) 
				+ "', '" 
				+ user.getId()
				+ "')") ;
						
				return contact;	
				// TODO: Timestamp testen...
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return contact;
		}
		return contact;}
	
	
	
	
		/**
		 * Update.
		 *
		 * Update von Ver�nderungen falls sich die firstName, lastName und/oder gender �ndert
		 * Falls Updates vorgenommen werden wird ein neuer mod_date gesetzt
		 * Gibt ein Contact zur�ck
		 * 
		 */
		public Contact update(Contact contact){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_Contact SET firstName ='" 
				+ contact.getFirstname()
				+ "', " 
				+ "lastName ='" 
				+ contact.getLastname()
				+ "', " 
				+ "gender='" 
				+ contact.getSex() 
				+ "', " 
				+ "mod_date="
				+ new java.sql.Timestamp(System.currentTimeMillis()) 
				+ " WHERE C_ID =" + contact.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return contact;
		}
		return contact;}
		
		/**
		 * Delete.
		 *
		 * Entfernt alles aus T_Permission_Contact wo die C_ID der ID des �bergebenen Objekts entspricht
		 * Damit l�sen wir die Teilhaberschaft an einem Contact auf
		 * der n�chste Schritt entfernt alles aus T_Contact wo die C_ID der ID des �bergebenen Objekts entspricht
		 * 
		 */
		public void delete (Contact contact){
Connection con = DBConnection.connection();

			try{
	
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("DELETE FROM T_Permission_Contact WHERE C_ID =" +contact.getId());
			}

			catch (SQLException e2){
				e2.printStackTrace();

			}
			
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Values WHERE C_ID =" +contact.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
			
			try{
				
				Statement stmt3 = con.createStatement();
				stmt3.executeUpdate("DELETE FROM T_Property WHERE C_ID =" +contact.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Contact_Contactlist WHERE C_ID =" +contact.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
				
			
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Contact WHERE C_ID =" +contact.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
			
			

			
		}
		
		public Vector<Contact> findAllByName(String name){
			Connection con = DBConnection.connection();
			Vector<Contact> result = new Vector<Contact>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date FROM T_Contact WHERE firstName LIKE'%" +name+ "%' OR lastName LIKE'%" + name+ "%' ORDER BY C_ID");
				while (rs.next()){
					Contact c = new Contact();
					c.setId(rs.getInt("C_ID"));
					c.setFirstname(rs.getString("firstName"));
					c.setLastname(rs.getString("lastName"));
					c.setSex(rs.getString("gender"));
					c.setOwner(rs.getInt("U_ID"));
					c.setCreationDate(rs.getTimestamp("create_date"));
					c.setModificationDate(rs.getTimestamp("mod_date"));
					result.addElement(c);	
				}
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return result;
			}
			return result;
		}
		
	
	
	}




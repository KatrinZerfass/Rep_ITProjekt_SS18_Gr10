package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.user.client.Window;


public class ContactMapper {
	
	/**
	* Konstruktor für den ContactMapper (Singleton) 
	* static weil Singleton. Einzige Instanz dieser Klasse
	* 
	* @author Egor Krämer
	* @author Robert Mattheis
	*/
	private static ContactMapper  contactmapper = null;
	
	/**
	 * Falls noch kein ContactMapper existiert wird ein neuen ContactMapper erstellt und gibt ihn zurück
	 * 
	 * @return erstmalig erstellter ContactMapper
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	public static ContactMapper contactMapper() {
		if (contactmapper == null){
			contactmapper = new ContactMapper();
		}
		return contactmapper;
		}
	
	/**
		 * Gibt alle Contact Objekte zurück welche mit C_ID, firstName, lastName, gender und U_ID befüllt sind
		 * Hierfür holen wir C_ID, firstName, lastName, geder und U_ID aus der T_Contact Tabelle und speichern diese in einem Contact Objekt ab und fügen diese dem Vector hinzu
		 * Diesen Vector befüllt mit Contacts geben wir zurück
		 * 
		 * @return Ein Vector voller Contact Objekte welche befüllt sind
		 * 
		 * @author Egor Krämer
		 * @author Robert Mattheis
		 */
		public Vector<Contact> findAll(){
	Connection con = DBConnection.connection();
	Vector<Contact> result = new Vector<Contact>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date, isUser FROM T_Contact ORDER BY firstName");
				
				while (rs.next()){
					Contact c = new Contact();
					c.setId(rs.getInt("C_ID"));
					c.setFirstname(rs.getString("firstName"));
					c.setLastname(rs.getString("lastName"));
					c.setSex(rs.getString("gender"));
					c.setOwner(rs.getInt("U_ID"));
					c.setCreationDate(rs.getTimestamp("create_date"));
					c.setModificationDate(rs.getTimestamp("mod_date"));
					c.setIsUser(rs.getBoolean("isUser"));
					result.addElement(c);
				}		
			}catch(SQLException e2){
				e2.printStackTrace();
			}
			return result;
		}

	/**
		 * Gibt alle Contact Objekte zurück welche mit C_ID, firstName, lastName, gender und U_ID befüllt sind und den IsUser flag auf true haben.
		 * Hierfür holen wir C_ID, firstName, lastName, geder und U_ID aus der T_Contact Tabelle und speichern diese in einem Contact Objekt ab und fügen diese dem Vector hinzu
		 * Diesen Vector befüllt mit Contacts geben wir zurück.
		 * 
		 * @return Ein Vector voller Contact Objekte welche befüllt sind
		 * 
		 * @author Egor Krämer
		 * @author Robert Mattheis
		 */
		public Vector<Contact> findAllUserContacts(){
	Connection con = DBConnection.connection();
	Vector<Contact> result = new Vector<Contact>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date, isUser FROM T_Contact WHERE isUser = 1 ORDER BY firstName");
				
				while (rs.next()){
					Contact c = new Contact();
					c.setId(rs.getInt("C_ID"));
					c.setFirstname(rs.getString("firstName"));
					c.setLastname(rs.getString("lastName"));
					c.setSex(rs.getString("gender"));
					c.setOwner(rs.getInt("U_ID"));
					c.setCreationDate(rs.getTimestamp("create_date"));
					c.setModificationDate(rs.getTimestamp("mod_date"));
					c.setIsUser(rs.getBoolean("isUser"));
					result.addElement(c);
				}		
			}catch(SQLException e2){
				e2.printStackTrace();
			}
			return result;
		}

	/**
	 * Findet Contacts durch eine C_ID und speichert die dazugehörigen Werte (C_ID, firstName, lastName, gender und U_ID) in einem COntact Objekt ab und gibt dieses wieder
	 * 
	 * @param contact übergebenes Contact Objekt mit Attribut C_ID
	 * @return Ein vollständiges Contact Objekt
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	public Contact findByID(Contact contact){
		Connection con = DBConnection.connection();
		Contact c = new Contact();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date, isUser FROM T_Contact WHERE C_ID =" + contact.getId() + " ORDER BY C_ID");
			if (rs.next()){
				
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				c.setCreationDate(rs.getTimestamp("create_date"));
				c.setModificationDate(rs.getTimestamp("mod_date"));
				c.setIsUser(rs.getBoolean("isUser"));
				return c;	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return c;
		}
		return c;
	}
	/**
	 * Findet Contacts durch einen Namen und speichert die dazugehörigen Werte (C_ID, firstName, lastName, gender und U_ID) in einem Contact Objekt ab
	 * und Speichert dieses Objekt im Vector ab und gibt diesen wieder
	 * 
	 * @param contact übergebenes Contact Objekt mit Attributen firstName und lastName
	 * @return Ein Vector voller Contact Objekte welche befüllt sind
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	public Vector<Contact> findByName(Contact contact){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date, isUser FROM T_Contact WHERE firstName ='" + contact.getFirstname() + "' AND lastName='" + contact.getLastname() + "' ORDER BY firstName");
			while (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				c.setCreationDate(rs.getTimestamp("create_date"));
				c.setModificationDate(rs.getTimestamp("mod_date"));
				c.setIsUser(rs.getBoolean("isUser"));
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
	 * Findet alle Contacts die ein User erstellt hat durch seine U_ID
	 * Alle Values werden aus T_Contact ausgelesen und in einem Contact Objekt gespeichert und einem Vector hinzugefügt und zurückgegeben
	 * Gibt ein Vector voller Contact Objekte zurück welche ein User erstellt hat
	 * 
	 * @param user übergebenes User Objekt mit Attribut U_ID
	 * @return Ein Vector voller Contact Objekte welche befüllt sind
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	public Vector<Contact> findAllByUID(User user){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
				
				try{
					Statement stmt = con.createStatement();
					
					ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date, isUser FROM T_Contact WHERE U_ID=" + user.getId()+ " ORDER BY firstName");
						while (rs.next()){
						
						Contact c = new Contact();
						c.setId(rs.getInt("C_ID"));
						c.setFirstname(rs.getString("firstName"));
						c.setLastname(rs.getString("lastName"));
						c.setSex(rs.getString("gender"));
						c.setOwner(rs.getInt("U_ID"));
						c.setCreationDate(rs.getTimestamp("create_date"));
						c.setModificationDate(rs.getTimestamp("mod_date"));
						c.setIsUser(rs.getBoolean("isUser"));
						result.addElement(c);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}

	/**
	 * Findet alle C_ID, firstName, lastName, gender, U_ID, create_date und mod_date wo der firstName oder lastName so ähnlich ist wie der eingegebene String ist (name)
	 * Befüllt das Contact Objekt mit den Attributen und fügt dieses Objekt dem Vector hinzu
	 * Gibt ein Vector voller Contact Objekte zurück
	 * 
	 * @param name übergebener String name
	 * @return Ein Vector voller Contact Objekte welche befüllt sind
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	
	public Vector<Contact> findAllByName(String name){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT C_ID, firstName, lastName, gender, U_ID, create_date, mod_date, isUser FROM T_Contact WHERE firstName LIKE'%" +name+ "%' OR lastName LIKE'%" + name+ "%' ORDER BY firstName");
			while (rs.next()){
				Contact c = new Contact();
				c.setId(rs.getInt("C_ID"));
				c.setFirstname(rs.getString("firstName"));
				c.setLastname(rs.getString("lastName"));
				c.setSex(rs.getString("gender"));
				c.setOwner(rs.getInt("U_ID"));
				c.setCreationDate(rs.getTimestamp("create_date"));
				c.setModificationDate(rs.getTimestamp("mod_date"));
				c.setIsUser(rs.getBoolean("isUser"));
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
	 * Sucht nach der höchsten C_ID um diese um eins zu erhöhen und als neue C_ID zu nutzen
	 * Befüllt T_Contact mit C_ID, firstName, lastName, gender, create_date, mod_date, U_ID
	 * Ein Contact wird zurückgegeben
	 *
	 * @param contact übergebenes Contact Objekt mit Attributen C_ID, firstName, lastName und gender
	 * @param user übergebenes User Objekt mit Attribut U_ID
	 * @return Ein vollständiges Contact Objekt
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	
	public Contact insert(Contact contact, User user){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			
			Timestamp ts = new Timestamp(System.currentTimeMillis()+ 7200000);
			
			String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts);
			
			ResultSet rs = stmt.executeQuery("SELECT MAX(C_ID) AS maxcid FROM T_Contact");
			if (rs.next()){
				
				contact.setId(rs.getInt("maxcid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Contact (C_ID, firstName, lastName, gender, create_date, mod_date, U_ID, isUser)"
				+ " VALUES ('"
				+ contact.getId() 
				+ "', '" 
				+ contact.getFirstname() 
				+ "', '" 
				+ contact.getLastname() 
				+ "', '" 
				+ contact.getSex() 
				+ "', '" 
				+ s
				+ "', '"
				+ s
				+ "', " 
				+ user.getId()
				+", "
				+ contact.getIsUser()
				+ ")") ;
						
				return contact;	
				
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return contact;
		}
		return contact;}

	/**
		 * Update von Veränderungen falls sich die firstName, lastName und/oder gender ändert
		 * Falls Updates vorgenommen werden wird ein neuer mod_date gesetzt
		 * Gibt ein Contact zurück
		 * 
		 * @param contact übergebenes Contact Objekt mit Attributen firstName, lastName, gender und C_ID
		 * @return Ein vollständiges Contact Objekt
		 * 
		 * @author Egor Krämer
		 * @author Robert Mattheis
		 */
		
		public Contact update(Contact contact){
			Connection con = DBConnection.connection();
			
			try{
				Timestamp ts = new Timestamp(System.currentTimeMillis()+ 7200000);
				
				
				String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts);
				
				
				
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
				+ "mod_date ='" 
				+ s
				+ "' WHERE C_ID =" + contact.getId());
				
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return contact;
		}
		return contact;}
		
		/**
		 * Entfernt alles aus T_Contact wo die C_ID der ID des übergebenen Objekts entspricht
		 * 
		 * @param contact übergebenes Contact Objekt mit Attribut C_ID
		 * 
		 * @author Egor Krämer
		 * @author Robert Mattheis
		 */
		public void delete (Contact contact){
Connection con = DBConnection.connection();

	
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Contact WHERE C_ID =" +contact.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
			
		}
		
	
	
	}




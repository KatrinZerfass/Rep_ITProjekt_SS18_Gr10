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

/**
 * The Class ContactMapper.
 */
public class ContactMapper {
	
	/** Konstruktor für den ContactMapper (Singleton) 
	* static weil Singleton. Einzige Instanz dieser Klasse
	*/
	private static ContactMapper  contactmapper = null;
	
	/**
	 * ContactMapper.
	 *
	 * Falls noch kein ContactMapper existiert wird ein neuen ContactMapper erstellt und gibt ihn zurück
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
	 * Findet Contacts durch eine C_ID und speichert die dazugehörigen Werte (C_ID, firstName, lastName, gender und U_ID) in einem COntact Objekt ab und gibt dieses wieder
	 * 
	 * @param contact übergebenes Contact Objekt mit Attribut C_ID
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
	 * Gibt alle Contact Objekte zurück welche mit C_ID, firstName, lastName, geder und U_ID befüllt sind
	 * Hierfür holen wir C_ID, firstName, lastName, geder und U_ID aus der T_Contact Tabelle und speichern diese in einem Contact Objekt ab und fügen diese dem Vector hinzu
	 * Diesen Vector befüllt mit Contacts geben wir zurück
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
	 * Findet Contacts durch einen Namen und speichert die dazugehörigen Werte (C_ID, firstName, lastName, gender und U_ID) in einem Contact Objekt ab
	 * und Speichert dieses Objekt im Vector ab und gibt diesen wieder
	 * 
	 * @param contact übergebenes Contact Objekt mit Attributen firstName und lastName
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
	 * Alle Values werden aus T_Contact ausgelesen und in einem Contact Objekt gespeichert und einem Vector hinzugefügt und zurückgegeben
	 * Gibt ein Vector voller Contact Objekte zurück welche ein User erstellt hat
	 * 
	 * @param user übergebenes User Objekt mit Attribut U_ID
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
	 * Sucht nach der höchsten C_ID um diese um eins zu erhöhen und als neue C_ID zu nutzen
	 * Befüllt T_Contact mit C_ID, firstName, lastName, gender, create_date, mod_date, U_ID
	 * Ein Contact wird zurückgegeben
	 *
	 * @param contact übergebenes Contact Objekt mit Attributen C_ID, firstName, lastName und gender
	 * @param user übergebenes User Objekt mit Attribut U_ID
	 */
	public Contact insert(Contact contact, User user){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			String s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(ts);
			
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
				+ "CURRENT_TIMESTAMP"
				+ "', '"
				+ "CURRENT_TIMESTAMP"
				+ "', " 
				+ user.getId()
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
		 * Update.
		 *
		 * Update von Veränderungen falls sich die firstName, lastName und/oder gender ändert
		 * Falls Updates vorgenommen werden wird ein neuer mod_date gesetzt
		 * Gibt ein Contact zurück
		 * 
		 * @param contact übergebenes Contact Objekt mit Attributen firstName, lastName, gender und C_ID
		 */
		public Contact update(Contact contact){
			Connection con = DBConnection.connection();
			
			try{
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				System.out.println(ts.toString());
				String s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(ts);
				System.out.println(s.toString());
				System.out.println(s);
				
				System.out.println("startet update");
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_Contact SET firstName ='" 
				+ contact.getFirstname()
				+ "', " 
				+ "lastName ='" 
				+ contact.getLastname()
				+ "', " 
				+ "gender='" 
				+ contact.getSex() 
				+ "'"
				+ "mod_date =" 
				+ "CURRENT_TIMESTAMP"
				+ " WHERE C_ID =" + contact.getId());
				System.out.println("update complete");
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			System.out.println("catch blok");
			return contact;
		}
		return contact;}
		
		/**
		 * Delete.
		 *
		 * Entfernt alles aus T_Permission_Contact wo die C_ID der ID des übergebenen Objekts entspricht
		 * Entfernt alles aus T_Value wo die C_ID der ID des übergebenen Objekts entspricht
		 * Entfernt alles aus T_Property wo die C_ID der ID des übergebenen Objekts entspricht
		 * Entfernt alles aus T_Property wo die C_ID der ID des übergebenen Objekts entspricht
		 * Entfernt alles aus T_Contact_Contactlist wo die C_ID der ID des übergebenen Objekts entspricht
		 * Damit lösen wir die Teilhaberschaft an einem Contact auf
		 * der nächste Schritt entfernt alles aus T_Contact wo die C_ID der ID des übergebenen Objekts entspricht
		 * 
		 * @param contact übergebenes Contact Objekt mit Attribut C_ID
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
				stmt.executeUpdate("DELETE FROM T_Value WHERE C_ID =" +contact.getId());
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
		
		/**
		 * findAllByName.
		 *
		 * Findet alle C_ID, firstName, lastName, gender, U_ID, create_date und mod_date wo der firstName oder lastName so ähnlich ist wie der eingegebene String ist (name)
		 * Befüllt das Contact Objekt mit den Attributen und fügt dieses Objekt dem Vector hinzu
		 * Gibt ein Vector voller Contact Objekte zurück
		 * 
		 * @param name übergebener String name
		 */
		
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




package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.User;

/**
 * The Class ContactListMapper.
 */
public class ContactListMapper {
	
	/** Konstruktor für den ContactListMapper (Singleton) 
	* static weil Singleton. Einzige Instanz dieser Klasse
	*/
	private static ContactListMapper  contactlistmapper = null;
	
	/**
	 * ContactListMapper.
	 *
	 * Falls noch kein ContactListMapper existiert erstellt er ein neuen ContactListMapper und gibt ihn zurück
	 * 
	 */
	public static ContactListMapper contactListMapper() {
		if (contactlistmapper == null){
			contactlistmapper = new ContactListMapper();
		}
		return contactlistmapper;
		}
	
	/**
	 * FindByID.
	 *
	 * Findet ContactList durch eine CL_ID und speichert die dazugehörigen Werte (CL_ID, listname und U_ID) in einem ContactList Objekt ab und gibt dieses wieder
	 * 
	 * @param contactlist übergebenes ContactList Objekt mit Attribut CL_ID
	 */
	public ContactList findByID(ContactList contactlist){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CL_ID, listname, U_ID FROM T_ContactList WHERE CL_ID =" + contactlist.getId() + " ORDER BY CL_ID");
			if (rs.next()){
				ContactList cl = new ContactList();
				cl.setId(rs.getInt("CL_ID"));
				cl.setName((rs.getString("listname")));
				cl.setOwner(rs.getInt("U_ID"));
				
				
				return cl;	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	
	/**
	 * FindAll.
	 *
	 * Gibt alle ContactList Objekte zurück welche mit CL_ID, listname und U_ID befüllt sind
	 * Hierfür holen wir CL_ID, listname und U_ID aus der T_ContactList Tabelle und speichern diese in einem ContactList Objekt ab und fügen diese dem Vector hinzu
	 * Am Ende geben wir diesen Vector zurück
	 *
	 */
	public Vector<ContactList> findAll(){
		Connection con = DBConnection.connection();
		Vector<ContactList> result = new Vector<ContactList>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT CL_ID, listname, U_ID FROM T_ContactList ORDER BY CL_ID");
					
					while (rs.next()){
						ContactList cl = new ContactList();
						cl.setId(rs.getInt("CL_ID"));
						cl.setName(rs.getString("listname"));
						cl.setOwner(rs.getInt("U_ID"));
						result.addElement(cl);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	
	/**
	 * FindByName.
	 *
	 * Findet ContactLists durch einen Namen und speichert die dazugehörigen Werte (CL_ID, listname und U_ID) in einem ContactList Objekt ab
	 * und Speichert dieses Objekt im Vector ab und gibt diesen wieder
	 * 
	 * @param name übergebener String listname
	 */
	public Vector <ContactList> findByName(String name){
		Connection con = DBConnection.connection();
		Vector<ContactList> result = new Vector<ContactList>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CL_ID, listname, U_ID FROM T_ContactList WHERE listname ='"+ name + "' ORDER BY CL_ID");
			while (rs.next()){
				ContactList cl = new ContactList();
				cl.setId(rs.getInt("CL_ID"));
				cl.setName(rs.getString("listname"));
				cl.setOwner(rs.getInt("U_ID"));
				result.addElement(cl);	
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
	 * Findet alle ContactLists die ein User erstellt hat durch seine U_ID
	 * Alle Values werden aus T_ContactList ausgelesen und in einem ContactList Objekt gespeichert und einem Vector hinzugefügt und zurückgegeben
	 * Gibt ein Vector voller ContactList Objekte zurück welche ein User erstellt hat
	 * 
	 * @param user übergebenes User Objekt mit Attribut U_ID
	 */
	public Vector <ContactList> findAllByUID(User user){
		Connection con = DBConnection.connection();
		Vector<ContactList> result = new Vector<ContactList>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CL_ID, listname, U_ID FROM T_ContactList WHERE U_ID=" + user.getId() + " ORDER BY CL_ID");
			while (rs.next()){
				ContactList cl = new ContactList();
				cl.setId(rs.getInt("CL_ID"));
				cl.setName(rs.getString("listname"));
				cl.setOwner(rs.getInt("U_ID"));
				result.addElement(cl);	
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return result;
		}
		return result;
	}
	
	/**
	 * FindAllByCID.
	 * 
	 * Findet alle CL_ID wo die C_ID der ID der übergebenen Objekte entspricht
	 * Befüllt das ContactList Objekt mit den Attributen und fügt dieses Objekt dem Vector hinzu
	 * Gibt ein Vector voller ContactList Objekte zurück
	 *
	 * @param contact übergebenes Contact Objekt mit Attributen C_ID
	 */
	public Vector <ContactList> findAllByCID(Contact contact){
		Connection con = DBConnection.connection();
		Vector<ContactList> result = new Vector<ContactList>();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT DISTINCT CL_ID FROM T_Contact_Contactlist WHERE C_ID=" + contact.getId() + " ORDER BY CL_ID");
			while (rs.next()){
				ContactList cl = new ContactList();
				cl.setId(rs.getInt("CL_ID"));
								
				result.addElement(ContactListMapper.contactListMapper().findByID(cl));
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return result;
		}
		return result;
	}
	
	/**
	 * Insert.
	 *
	 * Sucht nach der höchsten CL_ID um diese um eins zu erhöhen und als neue CL_ID zu nutzen
	 * Befüllt T_ContactList mit CL_ID, listname und U_ID
	 * Ein ContactList wird zurückgegeben
	 *
	 * @param contactlist übergebenes ContactList Objekt mit Attributen CL_ID und listname
	 * @param user übergebenes User Objekt mit Attribut U_ID
	 */
	public ContactList insert(ContactList contactlist, User user){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(CL_ID) AS maxclid FROM T_ContactList");
			if (rs.next()){
				
				contactlist.setId(rs.getInt("maxclid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_ContactList (CL_ID, listname, U_ID)"
				+ " VALUES ("
				+ contactlist.getId() 
				+ ", '" 
				+ contactlist.getName() 
				+ "', "
				+ user.getId() 
				+ ")") ;
						
				return contactlist;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return contactlist;
		}
		return contactlist;}
	
		/**
		 * Update.
		 *
		 * Update von Veränderungen falls sich der listname ändert
		 * Gibt ein ContactList zurück
		 * 
		 * @param contactlist übergebenes ContactList Objekt mit Attributen listname und CL_ID
		 */
		public ContactList update(ContactList contactlist){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_ContactList SET listname ='" + contactlist.getName() + "' " + "WHERE CL_ID =" + contactlist.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return contactlist;
		}
		return contactlist;}
		
		/**
		 * Delete.
		 *
		 * Entfernt alles aus T_Permission_Contactlist wo die CL_ID der ID des übergebenen Objekts entspricht
		 * Entfernt alles aus T_Permission_Contact wo die C_ID der ID des übergebenen Objekts entspricht
		 * Damit lösen wir die Teilhaberschaft an einem ContactList auf
		 * der nächste Schritt entfernt alles aus T_ContactList wo die CL_ID der ID des übergebenen Objekts entspricht
		 * 
		 * @param contactlist übergebenes ContactList Objekt mit Attribut CL_ID
		 */
		public void delete (ContactList contactlist){
Connection con = DBConnection.connection();

			
		try{
	
				Statement stmt2 = con.createStatement();
					stmt2.executeUpdate("DELETE FROM T_Permission_Contactlist WHERE CL_ID =" + contactlist.getId());
			}

			catch (SQLException e2){
				e2.printStackTrace();

				}
		try{
			
						
				Vector <Contact> c = ContactListMapper.contactListMapper().getAllContacts(contactlist);
				
				if(c.size()>0){
				for(Contact c1: c){	
			Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("DELETE FROM T_Permission_Contact WHERE C_ID =" + c1.getId());
		}}}

		catch (SQLException e2){
			e2.printStackTrace();

			}	
		try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_ContactList WHERE CL_ID =" + contactlist.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}}
		
		/**
		 * GetAllContacts.
		 *
		 * Befüllt den Vector mit Contacts die in einer ContactList enthalten sind
		 * Hierfür durchsuchen wir die T_Contact_ContactList Tabelle nach C_ID wo die CL_ID der ID des übergebenen Objektes entspricht
		 * Diese C_ID nutzen wir um die C_ID, firstName, lastName, gender und U_ID aus der T_Contact zu holen wo die C_ID der ID des ResultSets entspricht (Die C_ID welche wir aus T_Contact_Contactlist erhalten haben)
		 * Die Werte aus der T_Contact speichern wir in einem Contact Objekt ab und geben den Vector zurück
		 * 
		 * @param contactlist übergebenes ContactList Objekt mit Attribut CL_ID
		 */
		public Vector <Contact> getAllContacts(ContactList contactlist){
			Connection con = DBConnection.connection();
			Vector<Contact> result = new Vector<Contact>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID FROM T_Contact_Contactlist WHERE CL_ID =" + contactlist.getId() + " ORDER BY C_ID");			
			
				
				while (rs.next()){

					Contact c2=new Contact();
					c2.setId(rs.getInt("C_ID"));
					result.addElement(ContactMapper.contactMapper().findByID(c2));					
										
				}				
				
			}
			catch (SQLException e2){
				System.out.println("läuft nicht");
				e2.printStackTrace();
				return result;
			}
			return result;
		}	
		
		/**
		 * AddContact.
		 *
		 * Fügt der ContactList einen Contact hinzu
		 * Hierfür fügen wir der T_Contact_ContactList die CL_ID und C_ID hinzu und geben die ContactList zurück
		 * 
		 * @param contactlist übergebenes ContactList Objekt mit Attribut CL_ID
		 * @param contact übergebenes Contact Objekt mit Attributen C_ID
		 */
		public ContactList addContact(ContactList contactlist, Contact contact){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("INSERT INTO T_Contact_Contactlist (CL_ID, C_ID)"
				+ " VALUES ("
				+ contactlist.getId() 
				+ ", " 
				+ contact.getId() 
				+ ")") ;
						
				return contactlist;	
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return contactlist;
			}
			}
		
		/**
		 * RemoveContact.
		 * 
		 * Entfernt einen Contact aus der ContactList
		 * Hierfür löschen wir den Eintag aus T_Contact_ContactList wo die CL_ID der CL_ID des übergebenen Objektes entspricht
		 * 
		 * @param contactlist übergebenes ContactList Objekt mit Attribut CL_ID
		 * @param contact übergebenes Contact Objekt mit Attribut C_ID
		 */
		public ContactList removeContact(ContactList contactlist, Contact contact){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Contact_Contactlist WHERE CL_ID="
				+ contactlist.getId() 
				+ " AND C_ID=" 
				+ contact.getId());
						
				return contactlist;	
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return contactlist;
			}
			}
		
}

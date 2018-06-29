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
 * The Class PermissionMapper.
 */
public class PermissionMapper {

	/** Konstruktor f�r den PermissionMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static PermissionMapper  permissionmapper = null;
	
	/**
	 * PermissionMapper.
	 *
	 * Falls noch kein PermissionMapper existiert erstellt er ein neuen PermissionMapper und gibt ihn zur�ck
	 * 
	 */
	public static PermissionMapper permissionMapper() {
		if (permissionmapper == null){
			permissionmapper = new PermissionMapper();
		}
		return permissionmapper;
		}
	
	/**
	 * FindAll.
	 *
	 * Gibt alle Permission Objekte zur�ck welche mit U_ID und C_ID bef�llt sind
	 * Hierf�r holen wir U_ID und C_ID aus der T_Permission_Contact Tabelle und setzten als permissionID ein zusammengesetzten Key aus U_ID und C_ID
	 * F�r das setzten des Participant holen wir, durch das aufrufen der findByID im UserMapper, die U_ID
	 * und speichern diese in einem Permission Objekt ab
	 * zudem setzen wir die C_ID in einem neuen Contact Objekt und ein Shareableobject durch das aufrufen der findByID Methode im ContactMapper 
	 * Das selbe f�hren wir f�r ContactList durch und f�gen das Permission Objekt dem Vector hinzu
	 * 
	 * 
	 */
	public Vector<Permission> findAll(){
		Connection con = DBConnection.connection();
		Vector<Permission> result = new Vector<Permission>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT U_ID, C_ID, srcU_ID FROM T_Permission_Contact ORDER BY U_ID");
					
					while (rs.next()){
						Permission p = new Permission();
						p.setId(rs.getInt("U_ID") + rs.getInt("C_ID") +rs.getInt("srcU_ID"));
						p.setParticipantID(rs.getInt("U_ID"));
						p.setSourceUserID(rs.getInt("srcU_ID"));
						p.setShareableObjectID(rs.getInt("C_ID"));
												
						result.addElement(p);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT U_ID, CL_ID, srcU_ID FROM T_Permission_Contactlist ORDER BY U_ID");
					
					while (rs.next()){
						Permission p = new Permission();
						p.setId(rs.getInt("U_ID") + rs.getInt("CL_ID")+rs.getInt("srcU_ID"));
						p.setParticipantID(rs.getInt("U_ID"));
						p.setSourceUserID(rs.getInt("srcU_ID"));
						p.setShareableObjectID(rs.getInt("CL_ID"));
						result.addElement(p);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				
				}
				return result;
			}
	
/**
 * Update.
 *
 * Update von Ver�nderungen falls sich die Shareableobject �ndert
 * Falls die ID unter 30000000 liegt wird ein Contact geupdated, falls die ID �ber 30000000 liegt werden ContactLists geupdated
 * Gibt ein permission zur�ck
 */
public Permission update(Permission permission){
		
		Connection con = DBConnection.connection();
		
		if(permission.getShareableObjectID() < 30000000) {
		
		try{
			Statement stmt1 = con.createStatement();
			stmt1.executeUpdate("UPDATE T_Permission_Contact SET C_ID=" + permission.getShareableObjectID() + " WHERE U_ID =" + permission.getParticipantID());
						}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return permission;
				}
		
		return permission;
			}
		
		if(permission.getShareableObjectID() >= 30000000) {
			
			try{
			Statement stmt2 = con.createStatement();
			stmt2.executeUpdate("UPDATE T_Permission_Contactlist SET CL_ID=" + permission.getShareableObjectID()+ " WHERE U_ID =" + permission.getParticipantID());
					}
	
			catch (SQLException e2){
				e2.printStackTrace();
				return permission;
				}
			return permission;
			}
		return permission;
	}
	
	/**
	 * Delete.
	 *
	 * falls die ID des �bergebenen Shareableobjects Objekts unter 30000000 liegt wird aus der T_Permission_Contact alles entfernt
	 * wo die U_ID und C_ID der ID des Participants und des Shareableobjects entspricht
	 * 
	 * falls die ID des �bergebenen Shareableobjects Objekts �ber 30000000 liegt wird aus der T_Permission_Contactlist alles entfernt
	 * wo die U_ID und C_ID der ID des Participants und des Shareableobjects entspricht
	 */
	public void delete (Permission permission){
		
		Connection con = DBConnection.connection();
					
		if(permission.getShareableObjectID() < 30000000)
			
					try{
						Statement stmt1 = con.createStatement();
						stmt1.executeUpdate("DELETE FROM T_Permission_Contact WHERE C_ID =" + permission.getShareableObjectID()+ " AND U_ID=" + permission.getParticipantID());
					}
				
				catch (SQLException e2){
					e2.printStackTrace();
				}
		
		if(permission.getShareableObjectID()>= 30000000) {
			
					try{
					Statement stmt2 = con.createStatement();
					stmt2.executeUpdate("DELETE FROM T_Permission_Contactlist WHERE CL_ID =" + permission.getShareableObjectID()+ " AND U_ID ="+ permission.getParticipantID());
				}
			
				catch (SQLException e2){
					e2.printStackTrace();
			}
		}
	}
	
	/**
	 * DeleteAllByCLID.
	 *
	 * Entfernt alle Eintr�ge aus T_Permission_Contactlist falls eine ContactList gel�scht wird
	 * Hierf�r wird die T_Permission_Contactlist nach der CL_ID durchsucht wo sie der ID der ContactList entspricht welches wir �bergeben bekommen haben
	 * 
	 */
	public void deleteAllByCLID (ContactList contactlist){
		
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM T_Permission_Contactlist WHERE CL_ID =" + contactlist.getId());
		}
	
	catch (SQLException e2){
		e2.printStackTrace();
	}
}
	
	
	/**
	 * ShareContact.
	 *
	 * Bef�llt die T_Permission_Contact mit einer zusammengesetzten ID aus C_ID und U_ID 
	 * und bef�llt die Tabelle mit der C_ID aus dem Shareableobject und der U_ID aus Participant
	 * und gibt die permission zur�ck
	 * 
	 */
	public Permission shareContact(Permission permission){
		
		
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
				
				
				stmt.executeUpdate("INSERT INTO T_Permission_Contact (C_ID, U_ID, srcU_ID)"
						+ " VALUES ("
						+ permission.getShareableObjectID()
						+ ", " 
						+ permission.getParticipantID()
						+", "
						+ permission.getSourceUserID()
						
						+ ")") ;
						
				return permission;	
				
				}
		catch (SQLException e2){
			e2.printStackTrace();
			return permission;
			}
		}
	
	
	/**
	 * Share contact list.
	 *
	 * Bef�llt die T_Permission_Contactlist mit einer zusammengesetzten ID aus CL_ID und U_ID 
	 * und bef�llt die Tabelle mit der CL_ID aus dem Shareableobject und der U_ID aus Participant
	 * und gibt die permission zur�ck
	 * 
	 */
	public Permission shareContactList(Permission permission){
		
		
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			
				
			stmt.executeUpdate("INSERT INTO T_Permission_Contactlist (CL_ID, U_ID, srcU_ID)"
						+ " VALUES ("
						+ permission.getShareableObjectID()
						+ ", " 
						+ permission.getParticipantID()
						+", "
						+ permission.getSourceUserID()
					
						+ ")") ;
			
			ContactList cl= new ContactList();
			cl.setId(permission.getParticipantID());	
				cl = ContactListMapper.contactListMapper().findByID(cl);
				Vector <Contact> c = ContactListMapper.contactListMapper().getAllContacts(cl);
				for(Contact c1: c){
					Permission p = new Permission();
					p.setParticipantID(permission.getParticipantID());
					p.setSourceUserID(permission.getSourceUserID());
					p.setShareableObjectID(c1.getId());
					shareContact(p);
				}
			
			return permission;	
				
				}
		catch (SQLException e2){
			e2.printStackTrace();
			return permission;
			}
		}
	

/**
 * GetAllContactsByUID.
 * 
 * Sucht nach allen Contacts die einem User zur Verf�gung stehen
 * Hierf�r suchen wir nach allen U_ID die der ID des User Objektes entsprechen
 * Die gefundenen C_ID werden in einem Contact Objekt abgespeichert 
 * und durch den Aufruf der findByID im ContactMapper vollst�ndig bef�llt und dem Vector hinzugef�gt
 * Zum Schluss geben wir den Vector zur�ck
 * 
 */
public Vector<Contact> getAllContactsByUID(User user){
	
	Connection con = DBConnection.connection();
	Vector<Contact> result = new Vector<Contact>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID From T_Permission_Contact WHERE U_ID=" + user.getId()+ " ORDER BY C_ID");
				
				while (rs.next()){
					Contact c = new Contact();
					c.setId(rs.getInt("C_ID"));
									
					result.addElement(ContactMapper.contactMapper().findByID(c));
				}		
			}catch(SQLException e2){
				e2.printStackTrace();
				return result;
			}
			return result;
		}

public Vector<Contact> getAllContactsBySrcUID(User user){
	
	Connection con = DBConnection.connection();
	Vector<Contact> result = new Vector<Contact>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID From T_Permission_Contact WHERE srcU_ID=" + user.getId()+ " ORDER BY C_ID");
				
				while (rs.next()){
					Contact c = new Contact();
					c.setId(rs.getInt("C_ID"));
									
					result.addElement(ContactMapper.contactMapper().findByID(c));
				}		
			}catch(SQLException e2){
				e2.printStackTrace();
				return result;
			}
			return result;
		}



/**
 * Gets the all contact lists by UID.
 *
 * Sucht nach allen ContactLists die einem User zur Verf�gung stehen
 * Hierf�r suchen wir nach allen U_ID die der ID des User Objektes entsprechen
 * Die gefundenen CL_ID werden in einem ContactList Objekt abgespeichert 
 * und durch den Aufruf der findByID im ContactListMapper vollst�ndig bef�llt und dem Vector hinzugef�gt
 * Zum Schluss geben wir den Vector zur�ck
 */
public Vector<ContactList> getAllContactListsByUID(User user){
	
	Connection con = DBConnection.connection();
	Vector<ContactList> result = new Vector<ContactList>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT CL_ID From T_Permission_Contactlist WHERE U_ID=" + user.getId()+ " ORDER BY CL_ID");
				
				while (rs.next()){
					ContactList cl = new ContactList();
					cl.setId(rs.getInt("CL_ID"));
									
					result.addElement(ContactListMapper.contactListMapper().findByID(cl));
				}		
			}catch(SQLException e2){
				e2.printStackTrace();
			}
			return result;
		}
	
		
}


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

	/** Konstruktor für den PermissionMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static PermissionMapper  permissionmapper = null;
	
	/**
	 * PermissionMapper.
	 *
	 * Falls noch kein PermissionMapper existiert erstellt er ein neuen PermissionMapper und gibt ihn zurück
	 * 
	 */
	public static PermissionMapper permissionMapper() {
		if (permissionmapper == null){
			permissionmapper = new PermissionMapper();
		}
		return permissionmapper;
		}
	
	/**
	 * Find all.
	 *
	 * @return the vector
	 */
	public Vector<Permission> findAll(){
		Connection con = DBConnection.connection();
		Vector<Permission> result = new Vector<Permission>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT U_ID, C_ID FROM T_Permission_Contact ORDER BY U_ID");
					
					while (rs.next()){
						Permission p = new Permission();
						p.setId(rs.getInt("U_ID") + rs.getInt("C_ID"));
						p.setParticipant(UserMapper.userMapper().findByID(rs.getInt("U_ID")));
						Contact c = new Contact();
						c.setId(rs.getInt("C_ID"));
						p.setShareableobject(ContactMapper.contactMapper().findByID(c));
						
						result.addElement(p);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT U_ID, CL_ID FROM T_Permission_Contactlist ORDER BY U_ID");
					
					while (rs.next()){
						Permission p = new Permission();
						p.setId(rs.getInt("U_ID") + rs.getInt("CL_ID"));
						p.setParticipant(UserMapper.userMapper().findByID(rs.getInt("U_ID")));
						ContactList cl = new ContactList();
						cl.setId(rs.getInt("CL_ID"));
						p.setShareableobject(ContactListMapper.contactListMapper().findByID(cl));
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
 * @param permission the permission
 * @return the permission
 */
public Permission update(Permission permission){
		
		Connection con = DBConnection.connection();
		
		if(permission.getShareableobject().getId()< 30000000) {
		
		try{
			Statement stmt1 = con.createStatement();
			stmt1.executeUpdate("UPDATE T_Permission_Contact SET C_ID=" + permission.getShareableobject().getId() + " WHERE U_ID =" + permission.getParticipant().getId());
						}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return permission;
				}
		
		return permission;
			}
		
		if(permission.getShareableobject().getId() >= 30000000) {
			
			try{
			Statement stmt2 = con.createStatement();
			stmt2.executeUpdate("UPDATE T_Permission_Contactlist SET CL_ID=" + permission.getShareableobject().getId()+ " WHERE U_ID =" + permission.getParticipant().getId());
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
	 * @param permission the permission
	 */
	public void delete (Permission permission){
		
		Connection con = DBConnection.connection();
					
		if(permission.getShareableobject().getId()< 30000000)
			
					try{
						Statement stmt1 = con.createStatement();
						stmt1.executeUpdate("DELETE FROM T_Permission_Contact WHERE C_ID =" + permission.getShareableobject().getId()+ " AND U_ID=" + permission.getParticipant().getId());
					}
				
				catch (SQLException e2){
					e2.printStackTrace();
				}
		
		if(permission.getShareableobject().getId()>= 30000000) {
			
					try{
					Statement stmt2 = con.createStatement();
					stmt2.executeUpdate("DELETE FROM T_Permission_Contactlist WHERE CL_ID =" + permission.getShareableobject().getId()+ " AND U_ID ="+ permission.getParticipant().getId());
				}
			
				catch (SQLException e2){
					e2.printStackTrace();
			}
		}
	}
	
	/**
	 * Delete all by CLID.
	 *
	 * @param contactlist the contactlist
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
	 * Share contact.
	 *
	 * @param permission the permission
	 * @return the permission
	 */
	public Permission shareContact(Permission permission){
		
		
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
				
				permission.setId(permission.getShareableobject().getId() + permission.getParticipant().getId());
				
				stmt.executeUpdate("INSERT INTO T_Permission_Contact (C_ID, U_ID)"
						+ " VALUES ("
						+ permission.getShareableobject().getId()
						+ ", " 
						+ permission.getParticipant().getId()
						+ ", "
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
	 * @param permission the permission
	 * @return the permission
	 */
	public Permission shareContactList(Permission permission){
		
		
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
				
			permission.setId(permission.getShareableobject().getId() + permission.getParticipant().getId());
				
			stmt.executeUpdate("INSERT INTO T_Permission_Contactlist (CL_ID, U_ID)"
						+ " VALUES ("
						+ permission.getShareableobject().getId()
						+ ", " 
						+ permission.getParticipant().getId()
						+ ", "
						+ ")") ;
						
			return permission;	
				
				}
		catch (SQLException e2){
			e2.printStackTrace();
			return permission;
			}
		}
	

/**
 * Gets the all contacts by UID.
 *
 * @param user the user
 * @return the all contacts by UID
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
			}
			return result;
		}

/**
 * Gets the all contact lists by UID.
 *
 * @param user the user
 * @return the all contact lists by UID
 */
public Vector<ContactList> getAllContactListsByUID(User user){
	
	Connection con = DBConnection.connection();
	Vector<ContactList> result = new Vector<ContactList>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT DISTINCT CL_ID From T_Permission_Contact WHERE U_ID=" + user.getId()+ " ORDER BY CL_ID");
				
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


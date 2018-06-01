package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.bo.User;

public class PermissionMapper {
private static PermissionMapper  permissionmapper = null;
	
	public static PermissionMapper permissionMapper() {
		if (permissionmapper == null){
			permissionmapper = new PermissionMapper();
		}
		return permissionmapper;
		}
	public Vector<Permission> findAll(){
		Connection con = DBConnection.connection();
		Vector<Permission> result = new Vector<Permission>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT U_ID, C_ID FROM T_Permission_Contact ORDER BY U_ID");
					
					while (rs.next()){
						Permission p = new Permission();
						p.setId(rs.getInt("U_ID")+rs.getInt("C_ID"));
						p.setParticipant(UserMapper.userMapper().findByID(rs.getInt("U_ID")));
						Contact c = new Contact();
						c.setId(rs.getInt("C_ID"));
						p.setShareableobject(ContactMapper.contactMapper().findByID(c));
						//TODO: isShared if einf�gen
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
						p.setId(rs.getInt("U_ID")+rs.getInt("CL_ID"));
						p.setParticipant(UserMapper.userMapper().findByID(rs.getInt("U_ID")));
						ContactList cl = new ContactList();
						cl.setId(rs.getInt("cl_id"));
						p.setShareableobject(ContactListMapper.contactListMapper().findByID(cl));
						//TODO: isShared if einf�gen
						result.addElement(p);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				
				}
				return result;
			}
	
public Permission update(Permission permission){
		
		Connection con = DBConnection.connection();
		
		if(permission.getShareableobject().getId()<= 50000000) {
		
		try{
			Statement stmt1 = con.createStatement();
			stmt1.executeUpdate("UPDATE T_Permission_Contact SET C_ID=" + permission.getShareableobject().getId()+ " WHERE U_ID =" + permission.getParticipant().getId());
						}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return permission;
				}
		
		return permission;
			}
		
		if(permission.getShareableobject().getId()> 40000000) {
			
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
	
	public void delete (Permission permission){
		
		Connection con = DBConnection.connection();
					
		if(permission.getShareableobject().getId()<= 50000000)
			
					try{
						Statement stmt1 = con.createStatement();
						stmt1.executeUpdate("DELETE FROM T_Permission_Contact" + " WHERE C_ID = " + permission.getShareableobject().getId()+ "AND U_ID= " + permission.getParticipant().getId());
					}
				
				catch (SQLException e2){
					e2.printStackTrace();
				}
		
		if(permission.getShareableobject().getId()> 40000000) {
			
					try{
					Statement stmt2 = con.createStatement();
					stmt2.executeUpdate("DELETE FROM T_Permission_Contactlist" + " WHERE CL_ID = " + permission.getShareableobject().getId()+ "AND U_ID = "+ permission.getParticipant().getId());
				}
			
				catch (SQLException e2){
					e2.printStackTrace();
			}
		}
	}
	
	public void deleteAllByCLID (ContactList contactlist){
		
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM T_Contact_Contactlist WHERE CL_ID =" +contactlist.getId());
		}
	
	catch (SQLException e2){
		e2.printStackTrace();
	}
}
	
	// We need to have a additional ID to identify the C_ID 50000000
	
	public Permission insertContact(Permission permission){
		
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(C_ID) AS maxcid FROM T_Permission_Contact");
			
			if (rs.next()){
				
				permission.setId(rs.getInt("maxcid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Permission_Contact (C_ID, U_ID)"
						+ " VALUES ('"
						+ permission.getShareableobject().getId()
						+ "', '" 
						+ permission.getParticipant().getId()
						+ "', "
						+ ")") ;
						
				return permission;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return permission;
		}
		return permission;}
	
	// We need to have a additional ID to identify the CL_ID
	
public Permission insertContactList(Permission permission){
		
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(CL_ID) AS maxclid FROM T_Permission_Contactlist");
			
			if (rs.next()){
				
				permission.setId(rs.getInt("maxclid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Permission_Contactlist (CL_ID, U_ID)"
						+ " VALUES ('"
						+ permission.getShareableobject().getId()
						+ "', '" 
						+ permission.getParticipant().getId()
						+ "', "
						+ ")") ;
						
				return permission;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return permission;
		}
		return permission;
		}
	

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


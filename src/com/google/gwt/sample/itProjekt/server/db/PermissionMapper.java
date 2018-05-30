package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.Value;

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
		
		if(permission.getShareableobject().getId()> 30000000) {
		
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
		
		if(permission.getShareableobject().getId()<= 30000000) {
			
			try{
			Statement stmt2 = con.createStatement();
			stmt2.executeUpdate("UPDATE T_Permission_ContactList SET CL_ID=" + permission.getShareableobject().getId()+ " WHERE U_ID =" + permission.getParticipant().getId());
					}
	
			catch (SQLException e2){
				e2.printStackTrace();
				return permission;
				}
			return permission;
			}
		return permission;
	}
	
	
	
	
	
	
	
	
		
}


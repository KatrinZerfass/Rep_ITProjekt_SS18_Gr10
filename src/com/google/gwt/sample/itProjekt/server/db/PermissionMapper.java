package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;

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
					ResultSet rs = stmt.executeQuery("SELECT u_id, c_ID From T_Permission_Contact order by u_ID");
					
					while (rs.next()){
						Permission p = new Permission();
						p.setId(rs.getInt("u_id")+rs.getInt("c_id"));
						p.setParticipant(UserMapper.userMapper().findByID(rs.getInt("u_id")));
						Contact c = new Contact();
						c.setId(rs.getInt("c_id"));
						p.setShareableobject(ContactMapper.contactMapper().findByID(c));
						//TODO: isShared if einf�gen
						result.addElement(p);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT u_id, cl_ID From T_Permission_Contactlist order by u_ID");
					
					while (rs.next()){
						Permission p = new Permission();
						p.setId(rs.getInt("u_id")+rs.getInt("cl_id"));
						p.setParticipant(UserMapper.userMapper().findByID(rs.getInt("u_id")));
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
	
	
	
	
	
}

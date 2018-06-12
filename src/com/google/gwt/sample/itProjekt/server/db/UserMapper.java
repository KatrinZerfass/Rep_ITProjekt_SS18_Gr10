package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.User;


public class UserMapper {
	
	/** Konstruktor f�r den UserMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static UserMapper  usermapper = null;
	
	/**
	 * UserMapper.
	 *
	 *Falls noch kein UserMapper existiert erstellt er ein neuen UserMapper und gibt ihn zur�ck
	 * 
	 */
	public static UserMapper userMapper() {
		if (usermapper == null){
			usermapper = new UserMapper();
		}
		
		return usermapper;
		}
	
	/**
	 * FindByID.
	 * 
	 * Findet User durch eine U_ID und speichert die dazugeh�rigen Werte (U_ID und email) in einem User Objekt ab und gibt dieses wieder
	 * 
	 */
	public User findByID(int uid){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT U_ID, eMail FROM T_User WHERE U_ID ="+ uid + " ORDER BY U_ID");
			
			if (rs.next()){
				User u = new User();
				u.setId(rs.getInt("U_ID"));
				u.setEmail(rs.getString("eMail"));
				
				return u;	
			}
		}
		
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	/**
	 * FindByEMail.
	 *
	 * Findet User durch eine EMail und speichert die dazugeh�rigen Werte (U_ID und eMail) in einem User Objekt ab 
	 * und speichert dieses Objekt im Vector ab und gibt diesen wieder
	 *  
	 */
	public User findByEMail(String email){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT U_ID, eMail FROM T_User WHERE eMail ='"+ email + "' ORDER BY U_ID");
			if (rs.next()){
				User u = new User();
				u.setId(rs.getInt("U_ID"));
				u.setEmail(rs.getString("eMail"));
	
				return u;	
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	/**
	 * FindAll.
	 *
	 *Gibt alle User Objekte zur�ck welche mit U_ID und eMail bef�llt sind
	 *Hierf�r holen wir U_ID und eMail aus der T_User Tabelle und speichern diese in einem User Objekt ab und f�gen diese dem Vector hinzu
	 *Diesen Vector bef�llt mit User geben wir zur�ck
	 *
	 */
	public Vector<User> findAll(){
		Connection con = DBConnection.connection();
		Vector<User> result = new Vector<User>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT U_ID, eMail FROM T_User ORDER BY U_ID");
					
					while (rs.next()){
						User u = new User();
						u.setId(rs.getInt("U_ID"));
						u.setEmail(rs.getString("eMail"));
						result.addElement(u);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}	
	
	/**
	 * Insert.
	 *
	 *Sucht nach der h�chsten U_ID um diese um eins zu erh�hen und als neue U_ID zu nutzen
	 *Bef�llt T_User mit U_ID und eMail
	 *Ein value wird zur�ckgegeben
	 */
	public User insert(User user){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(U_ID) AS maxuid FROM T_User");
			if (rs.next()){
				
				user.setId(rs.getInt("maxuid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_User (U_ID, eMail)"
				+ " VALUES ("
				+ user.getId() 
				+ ", '" 
				+ user.getEmail()
				+ "')") ;
						
				return user;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return user;
		}
		return user;}
	
	/**
	 * Update.
	 *
	 * Update von Ver�nderungen falls sich die eMail �ndert
	 * Gibt ein User zur�ck
	 */
	public User update(User user){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE T_User SET eMail ='"
			+ user.getEmail()
			+ "', "
			+ "WHERE U_ID =" + user.getId());
		}
	
	catch (SQLException e2){
		e2.printStackTrace();
		return user;
	}
	return user;}
	
	/**
	 * Delete.
	 *
	 * Entfernt alles aus T_User wo die U_ID der ID des �bergebenen Objekts entspricht
	 *
	 */
	public void delete (User user){
		Connection con = DBConnection.connection();
					
					try{
						
						Statement stmt = con.createStatement();
						stmt.executeUpdate("DELETE FROM T_User WHERE U_ID =" + user.getId());
					}
				
				catch (SQLException e2){
					e2.printStackTrace();
					}
				}

	
}



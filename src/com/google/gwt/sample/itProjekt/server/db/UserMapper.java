package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.User;

/**
 * The Class UserMapper.
 */
public class UserMapper {
	
	/** Konstruktor für den UserMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static UserMapper  usermapper = null;
	
	/**
	 * UserMapper.
	 *
	 *Falls noch kein UserMapper existiert erstellt er ein neuen UserMapper und gibt ihn zurück
	 * 
	 */
	public static UserMapper userMapper() {
		if (usermapper == null){
			usermapper = new UserMapper();
		}
		
		return usermapper;
		}
	
	/**
	 * Find by ID.
	 *
	 * @param uid the uid
	 * @return the user
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
	 * Find by E mail.
	 *
	 * @param email the email
	 * @return the user
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
	 * Find all.
	 *
	 * @return the vector
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
	 * @param u the u
	 * @return the user
	 */
	public User insert(User u){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(U_ID) AS maxuid FROM T_User");
			if (rs.next()){
				
				u.setId(rs.getInt("maxuid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_User (U_ID, eMail)"
				+ " VALUES ("
				+ u.getId() 
				+ ", '" 
				+ u.getEmail()
				+ "')") ;
						
				return u;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return u;
		}
		return u;}
	
	/**
	 * Update.
	 *
	 * @param u the u
	 * @return the user
	 */
	public User update(User u){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("UPDATE T_User SET eMail ='"
			+ u.getEmail()
			+ "', "
			+ "WHERE U_ID =" + u.getId());
		}
	
	catch (SQLException e2){
		e2.printStackTrace();
		return u;
	}
	return u;}
	
	/**
	 * Delete.
	 *
	 * @param u the u
	 */
	public void delete (User u){
		Connection con = DBConnection.connection();
					
					try{
						
						Statement stmt = con.createStatement();
						stmt.executeUpdate("DELETE FROM T_User WHERE U_ID =" + u.getId());
					}
				
				catch (SQLException e2){
					e2.printStackTrace();
					}
				}

	
}



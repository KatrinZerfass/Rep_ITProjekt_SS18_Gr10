package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.User;


public class UserMapper {
	
	/**
	 * Konstruktor für den UserMapper (Singleton)
	 * static weil Singleton. Einzige Instanz dieser Klasse
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	private static UserMapper  usermapper = null;
	
	/**
	 * Falls noch kein UserMapper existiert erstellt er ein neuen UserMapper und gibt ihn zurück
	 * 
	 * @return erstmalig erstellter UserMapper
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	public static UserMapper userMapper() {
		if (usermapper == null){
			usermapper = new UserMapper();
		}
		
		return usermapper;
		}
	
	/**
	 * Gibt alle User Objekte zurück welche mit U_ID und eMail befüllt sind
	 * Hierfür holen wir U_ID und eMail aus der T_User Tabelle und speichern diese in einem User Objekt ab und fügen diese dem Vector hinzu
	 * Diesen Vector befüllt mit User geben wir zurück
	 * 
	 * @return Ein Vector voller User Objekte welche befüllt sind
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
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
	 * Findet User durch eine U_ID und speichert die dazugehörigen Werte (U_ID und email) in einem User Objekt ab und gibt dieses wieder
	 * 
	 * @param uid übergebener Integer der U_ID
	 * @return Ein vollständiges User Objekt
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
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
	 * Findet User durch eine EMail und speichert die dazugehörigen Werte (U_ID und eMail) in einem User Objekt ab 
	 * und speichert dieses Objekt im Vector ab und gibt diesen wieder
	 * 
	 * @param email übergebener String der eMail
	 * @return Ein vollständiges User Objekt
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
	 */
	public User findByEMail(String email){
		Connection con = DBConnection.connection();
		User u = new User();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT U_ID, eMail FROM T_User WHERE eMail ='"+ email + "' ORDER BY U_ID");
			if (rs.next()){
				
				u.setId(rs.getInt("U_ID"));
				u.setEmail(rs.getString("eMail"));
	
				return u;	
			}
		}
		catch (SQLException e){
			e.printStackTrace();
		return u;
		}
		
		return u;
	}
	
	/**
	 * Sucht nach der höchsten U_ID um diese um eins zu erhöhen und als neue U_ID zu nutzen
	 * Befüllt T_User mit U_ID und eMail
	 * Ein value wird zurückgegeben
	 *
	 * @param user übergebenes User Objekt mit Attributen U_ID und eMail
	 * @return Ein vollständiges User Objekt
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
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
	 * Update von Veränderungen falls sich die eMail ändert
	 * Gibt ein User zurück
	 * 
	 * @param user übergebenes User Objekt mit Attributen U_ID und eMail
	 * @return Ein vollständiges User Objekt
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
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
	 * Entfernt alles aus T_User wo die U_ID der ID des übergebenen Objekts entspricht
	 * 
	 * @param user übergebenes User Objekt mit Attribut U_ID
	 * 
	 * @author Egor Krämer
	 * @author Robert Mattheis
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



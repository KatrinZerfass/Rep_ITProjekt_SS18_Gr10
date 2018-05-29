package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.bo.Property;

public class ValueMapper {
private static ValueMapper valuemapper = null;
	
	public static ValueMapper valueMapper() {
		if (valuemapper == null){
			valuemapper = new ValueMapper();
		}
		return valuemapper;
		}
	public Vector<Value> findAllByValue(Value value){
		Connection con = DBConnection.connection();
		Vector<Value> result = new Vector<Value>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT v_id, value From T_Value where value=" + value.getContent()+ " order by V_ID");
					
					while (rs.next()){
						Value v = new Value();
						v.setId(rs.getInt("v_id"));
						v.setContent(rs.getString("value"));
						
						
						result.addElement(v);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	public Vector<Contact> findAllContactsByValue(Value value){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT DISTINCT c_id From T_Value where value=" + value.getContent()+ " order by C_ID");
					
					while (rs.next()){
						Contact c = new Contact();
						c.setId(rs.getInt("c_id"));
										
						result.addElement(ContactMapper.contactMapper().findByID(c));
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	public Vector<Value> findAll(){
		Connection con = DBConnection.connection();
		Vector<Value> result = new Vector<Value>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT v_id, value From T_Value order by v_ID");
					
					while (rs.next()){
						Value v = new Value();
						v.setId(rs.getInt("v_id"));
						v.setContent(rs.getString("value"));
						
						result.addElement(v);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	public Value insert(Value v, Contact c, Property p){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(v_id) AS maxvid From T_Value");
			if (rs.next()){
				
				v.setId(rs.getInt("maxvid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Value (v_id, p_id, value, c_id, isShared)"
				+ " VALUES ('"
				+ v.getId() 
				+ "', '" 
				+ p.getId() 
				+ "', '" 
				+ v.getContent() 
				+ "', '" 
				+ c.getId() 
				+ "', '" 
				+ true 
				+ ")") ;
						
				return v;	
				// isShared auch übergeben
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return v;
		}
		return v;}
	public Value insert(Value v, Contact c, Property p, boolean isShared){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(v_id) AS maxvid From T_Value");
			if (rs.next()){
				
				v.setId(rs.getInt("maxvid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Value (v_id, p_id, value, c_id, isShared)"
				+ " VALUES ('"
				+ v.getId() 
				+ "', '" 
				+ p.getId() 
				+ "', '" 
				+ v.getContent() 
				+ "', '" 
				+ c.getId() 
				+ "', '" 
				+ isShared
				+ "')") ;
						
				return v;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return v;
		}
		return v;}
	
		public Value update(Value v, Contact c, Property p, boolean s){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_Value SET p_id ='"+p.getId()+"', value ='" + v.getContent()+ "', c_id=" + c.getId() +"', isShared="
						+ s);
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return v;
		}
		return v;}
		
		public void delete (Value v){
Connection con = DBConnection.connection();
			
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_VALUE WHERE V_ID =" +v.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
}
		public Vector<Contact> getAllContactsByPID(Property property){
			Connection con = DBConnection.connection();
			Vector<Contact> result = new Vector<Contact>();
					
					try{
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT DISTINCT c_id From T_Value where p_id=" + property.getId()+ " order by C_ID");
						
						while (rs.next()){
							Contact c = new Contact();
							c.setId(rs.getInt("c_id"));
											
							result.addElement(ContactMapper.contactMapper().findByID(c));
						}		
					}catch(SQLException e2){
						e2.printStackTrace();
					}
					return result;
				}
		
		public Vector <Property> getAllPropertiesByCID (Contact contact){
			Connection con = DBConnection.connection();
			Vector <Property> result=new Vector <Property>();
			
			try{
				Statement stmt = con.createStatement();
				Statement stmt2 = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT p_id FROM T_Value WHERE c_id ="+ contact.getId()+ " ORDER BY C_ID");

				while (rs.next()){
					ResultSet rs2 = stmt2.executeQuery ("SELECT p_id, type FROM T_Property WHERE p_id =" + rs.getInt("P_ID")+ " ORDER BY C_ID");
					Property p = new Property();
					p.setId(rs2.getInt("v_id"));
					p.setType(rs2.getString("Type"));
	
					result.addElement(p);
				}
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return result;
			}
			return result;
		}	
		
		
		
		
		public Vector <Value> getAllValueByCID (Contact contact){
			Connection con = DBConnection.connection();
			Vector <Value> result = new Vector <Value>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT v_id, value, c_id FROM T_Value WHERE c_id ="+ contact.getId()+ " ORDER BY V_ID");

				while (rs.next()){
					Value v = new Value();
					v.setId(rs.getInt("v_id"));
					v.setContent(rs.getString("value"));
	
					result.addElement(v);
				}
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return result;
			}
			return result;
			
}
		
	
}

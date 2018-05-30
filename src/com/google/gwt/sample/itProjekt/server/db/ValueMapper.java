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
	
	public static ValueMapper  valueMapper() {
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
					ResultSet rs = stmt.executeQuery("SELECT V_ID, value From T_Value where value=" + value.getContent()+ " order by V_ID");
					
					while (rs.next()){
						Value v = new Value();
						v.setId(rs.getInt("V_ID"));
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
					ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID From T_Value where value=" + value.getContent()+ " order by C_ID");
					
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
	public Vector<Value> findAll(){
		Connection con = DBConnection.connection();
		Vector<Value> result = new Vector<Value>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT V_ID, value From T_Value order by V_ID");
					
					while (rs.next()){
						Value v = new Value();
						v.setId(rs.getInt("V_ID"));
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
			ResultSet rs = stmt.executeQuery("SELECT MAX(V_ID) AS maxvid From T_Value");
			if (rs.next()){
				
				v.setId(rs.getInt("maxvid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Value (V_ID, P_ID, value, C_ID, isShared)"
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
			ResultSet rs = stmt.executeQuery("SELECT MAX(V_ID) AS maxvid From T_Value");
			if (rs.next()){
				
				v.setId(rs.getInt("maxvid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Value (V_ID, P_ID, value, C_ID, isShared)"
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
				stmt.executeUpdate("UPDATE T_Value SET P_ID ='"+p.getId()+"', value ='" + v.getContent()+ "', C_ID=" + c.getId() +"', isShared="
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
				stmt.executeUpdate("DELETE FROM T_Value WHERE V_ID =" +v.getId());
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
						ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID From T_Value where P_ID=" + property.getId()+ " order by C_ID");
						
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
		
		public Vector <Property> getAllPropertiesByCID (Contact contact){
			Connection con = DBConnection.connection();
			Vector <Property> result=new Vector <Property>();
			
			try{
				Statement stmt = con.createStatement();
				Statement stmt2 = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT P_ID FROM T_Value WHERE C_ID ="+ contact.getId()+ " ORDER BY C_ID");

				while (rs.next()){
					ResultSet rs2 = stmt2.executeQuery ("SELECT P_ID, type FROM T_Property WHERE P_ID =" + rs.getInt("P_ID")+ " ORDER BY C_ID");
					Property p = new Property();
					p.setId(rs2.getInt("V_ID"));
					p.setType(rs2.getString("type"));
	
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
				ResultSet rs = stmt.executeQuery("SELECT V_ID, value, C_ID FROM T_Value WHERE C_ID ="+ contact.getId()+ " ORDER BY V_ID");

				while (rs.next()){
					Value v = new Value();
					v.setId(rs.getInt("V_ID"));
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

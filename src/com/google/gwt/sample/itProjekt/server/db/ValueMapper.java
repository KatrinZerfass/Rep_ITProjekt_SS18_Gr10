package com.google.gwt.sample.itProjekt.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.bo.Property;


public class ValueMapper {

	/** Konstruktor für den ValueMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static ValueMapper valuemapper = null;
	
	/**
	 * ValueMapper.
	 *
	 *Falls noch kein ValueMapper existiert erstellt er ein neuen ValueMapper und gibt ihn zurück
	 * 
	 */
	public static ValueMapper  valueMapper() {
		if (valuemapper == null){
			valuemapper = new ValueMapper();
		}
		return valuemapper;
		}
	
	/**
	 * FindAllByValue.
	 * 
	 * Findet alle V_ID durch ein value welches als Filterkriterium dient
	 * Befüllt ein Value Objekt mit V_ID und value und fügt dieses Objekt dem Vector hinzu
	 * Gibt ein Vector voller Value Objekte zurück
	 *
	 */
	public Vector<Value> findAllByValue(Value value){
		Connection con = DBConnection.connection();
		Vector<Value> result = new Vector<Value>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT DISTINCT V_ID, value FROM T_Value WHERE value='" + value.getContent()+ "' ORDER BY V_ID");
					
					while (rs.next()){
						Value v = new Value();
						v.setId(rs.getInt("V_ID"));
						v.setContent(rs.getString("value"));
						v.setPropertyid(rs.getInt("P_ID"));
						
						
						result.addElement(v);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	
	/**
	 * FindAllContactsByValue.
	 *
	 * Findet alle C_ID durch ein value welches als Filterkriterium dient
	 * Mit dem Contect Objekt welches C_ID beinhaltet wird durch findByID im ContactMapper das Contact Objekt vollständig befüllt und fügt diesen dem Vector hinzu
	 * Gibt ein Vector voller Contact Objekte zurück welche eine bestimmte value besitzen
	 *
	 */
	public Vector<Contact> findAllContactsByValue(Value value){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID FROM T_Value WHERE value='" + value.getContent()+ "' ORDER BY C_ID");
					
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
	 * FindAll.
	 *
	 *Gibt alle Value Objekte zurück welche mit V_ID und value befüllt sind
	 *Hierfür holen wir V_ID und value aus der T_Value Tabelle und speichern diese in einem Value Objekt ab und fügen diese dem Vector hinzu
	 *
	 */
	public Vector<Value> findAll(){
		Connection con = DBConnection.connection();
		Vector<Value> result = new Vector<Value>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT V_ID, value FROM T_Value ORDER BY V_ID");
					
					while (rs.next()){
						Value v = new Value();
						v.setId(rs.getInt("V_ID"));
						v.setContent(rs.getString("value"));
						v.setPropertyid(rs.getInt("U_ID"));
						
						result.addElement(v);
					}		
				}catch(SQLException e2){
					e2.printStackTrace();
				}
				return result;
			}
	
	/**
	 * Insert.
	 *
	 *Sucht nach der hochsten V_ID um diese um eins zu erhöhen und als neue V_ID zu nutzen
	 *Befüllt T_Value mit V_ID, P_ID, value, C_ID und isShared, welcher standardmässig auf True gesetzt ist, also als geteilt
	 *Ein value wird zurückgegeben
	 *
	 */
	public Value insert(Value value, Contact contact, Property property){
		//Mit Default isShared=true
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(V_ID) AS maxvid FROM T_Value");
			if (rs.next()){
				
				value.setId(rs.getInt("maxvid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Value (V_ID, P_ID, value, C_ID, isShared)"
				+ " VALUES ("
				+ value.getId() 
				+ ", " 
				+ property.getId() 
				+ ", '" 
				+ value.getContent() 
				+ "', " 
				+ contact.getId() 
				+ ", " 
				//Default isShared Flag ist auf true gesetzt
				+ true 
				+ ")") ;
						
				return value;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return value;
		}
		return value;}
	
	/**
	 * Insert.
	 *
	 *Sucht nach der hochsten V_ID um diese um eins zu erhöhen und als neue V_ID zu nutzen
	 *Befüllt T_Value mit V_ID, P_ID, value, C_ID und isShared
	 *Mit dem isShared legen wir fest ob die value True oder False ist bzw. ob es geteilt ist oder nicht
	 *Eine value wird zum Schluss zurückgegeben
	 *
	 */
	public Value insert(Value value, Contact contact, Property property, boolean isShared){
		Connection con = DBConnection.connection();
		
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(V_ID) AS maxvid FROM T_Value");
			if (rs.next()){
				
				value.setId(rs.getInt("maxvid")+1);
				Statement stmt2 = con.createStatement();
				stmt2.executeUpdate("INSERT INTO T_Value (V_ID, P_ID, value, C_ID, isShared)"
				+ " VALUES ("
				+ value.getId() 
				+ ", " 
				+ property.getId() 
				+ ", '" 
				+ value.getContent() 
				+ "', " 
				+ contact.getId() 
				+ ", " 
				+ isShared
				+ ")") ;
						
				return value;	
				
			}
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return value;
		}
		return value;}
	
		/**
		 * Update.
		 *
		 *Update von Veränderungen falls sich value, P_ID, C_ID oder isShared ändert
		 *Gibt ein value zurück
		 * 
		 */
		public Value update(Value value, Contact contact, Property property, boolean isShared){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_Value SET P_ID ="+property.getId()+", value ='" + value.getContent()+ "', C_ID=" + contact.getId() +", isShared="
						+ isShared);
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			return value;
		}
		return value;}
		
		/**
		 * Delete.
		 *
		 *Entfernt alles aus T_Value wo die V_ID der ID des übergebenen Objekts entspricht
		 * 
		 */
		public void delete (Value value){
Connection con = DBConnection.connection();
			
			try{
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate("DELETE FROM T_Value WHERE V_ID =" +value.getId());
			}
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
}
		
		/**
		 * getAllContactsByPID.
		 *
		 *Befüllt ein Vector mit Contacts welche eine bestimmte Property haben und gibt diesen Vactor zurück
		 *Hierfür suchen wir nach C_ID in der T_Value Tabelle wo die P_ID der ID des übergebenen Objekts entspricht
		 *Mit der C_ID befüllen wir ein Contact Objekt mit der Methode findByID und fügen diesen dem Vector hinzu
		 *
		 *
		 */
		public Vector<Contact> getAllContactsByPID(Property property){
			//TODO: Brauchen wir diese Methode?
			Connection con = DBConnection.connection();
			Vector<Contact> result = new Vector<Contact>();
					
					try{
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID FROM T_Value WHERE P_ID=" + property.getId()+ " ORDER BY C_ID");
						
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
		 * getAllPropertiesByCID.
		 *
		 *Befüllt ein Vector mit Property welche eine bestimmter Contact hat und gibt diesen Vactor zurück
		 *Hierfür suchen wir nach P_ID in der T_Value Tabelle wo die C_ID der ID des übergebenen Objekts entspricht
		 *Mit dieser P_ID werden alle type und P_ID aus T_Property geholt und in einem neuen Property Objekt gespeichert und den Vector hinzugefügt
		 *
		 */
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
					p.setId(rs2.getInt("P_ID"));
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
		
		
		
		
		/**
		 * getAllValueByCID.
		 *
		 *Befüllt ein Vector mit Value welche eine bestimmter Contact hat und gibt diesen Vactor zurück
		 *Hierfür suchen wir nach V_ID und value in der T_Value Tabelle wo die C_ID der ID des übergebenen Objekts entspricht
		 *Wir befüllen diese Daten in ein Value Objekt welches wir dem Vector hinzufügen
		 *
		 */
		public Vector <Value> getAllValueByCID (Contact contact){
			Connection con = DBConnection.connection();
			Vector <Value> result = new Vector <Value>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT V_ID, value, P_ID FROM T_Value WHERE C_ID ="+ contact.getId()+ " ORDER BY C_ID");

				while (rs.next()){
					Value v = new Value();
					v.setId(rs.getInt("V_ID"));
					v.setContent(rs.getString("value"));
					v.setPropertyid(rs.getInt("P_ID"));
	
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

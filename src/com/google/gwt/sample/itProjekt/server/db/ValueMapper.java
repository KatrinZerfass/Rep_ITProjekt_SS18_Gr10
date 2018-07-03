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

	/** Konstruktor für den ValueMapper (Singleton) 
	 * static weil Singleton. Einzige Instanz dieser Klasse
	 */
	private static ValueMapper valuemapper = null;
	
	/**
	 * ValueMapper.
	 *
	 * Falls noch kein ValueMapper existiert erstellt er ein neuen ValueMapper und gibt ihn zurück
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
	 * @param value übergebenes Value Objekt mit Attribut value
	 */
	public Vector<Value> findAllByValue(Value value){
		Connection con = DBConnection.connection();
		Vector<Value> result = new Vector<Value>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT DISTINCT V_ID, value, P_ID, isShared FROM T_Value WHERE value LIKE'%" + value.getContent()+ "%' ORDER BY V_ID");
					
					while (rs.next()){
						Value v = new Value();
						v.setId(rs.getInt("V_ID"));
						v.setContent(rs.getString("value"));
						v.setPropertyid(rs.getInt("P_ID"));
						v.setIsShared(rs.getBoolean("isShared"));
						
						
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
	 * Mit dem Contact Objekt welches C_ID beinhaltet wird durch findByID im ContactMapper das Contact Objekt vollständig befüllt und fügt diesen dem Vector hinzu
	 * Gibt ein Vector voller Contact Objekte zurück welche eine bestimmte value besitzen
	 *
	 * @param value übergebenes Value Objekt mit Attribut value
	 */
	public Vector<Contact> findAllContactsByValue(Value value){
		Connection con = DBConnection.connection();
		Vector<Contact> result = new Vector<Contact>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT DISTINCT C_ID FROM T_Value WHERE value LIKE '%" + value.getContent()+ "%' ORDER BY C_ID");
					
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
	 * Gibt alle Value Objekte zurück welche mit V_ID und value befüllt sind
	 * Hierfür holen wir V_ID und value aus der T_Value Tabelle und speichern diese in einem Value Objekt ab und fügen diese dem Vector hinzu
	 * Am Ende geben wir diesen Vector zurück
	 *
	 */
	public Vector<Value> findAll(){
		Connection con = DBConnection.connection();
		Vector<Value> result = new Vector<Value>();
				
				try{
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT V_ID, value, P_ID, isShared FROM T_Value ORDER BY V_ID");
					
					while (rs.next()){
						Value v = new Value();
						v.setId(rs.getInt("V_ID"));
						v.setContent(rs.getString("value"));
						v.setPropertyid(rs.getInt("P_ID"));
						v.setIsShared(rs.getBoolean("isShared"));
						
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
	 * Sucht nach der höchsten V_ID um diese um eins zu erhöhen und als neue V_ID zu nutzen
	 * Befüllt T_Value mit V_ID, P_ID, value, C_ID und isShared
	 * Mit dem isShared legen wir fest ob die value True oder False ist bzw. ob es geteilt ist oder nicht
	 * Eine value wird zum Schluss zurückgegeben
	 *
	 * @param value übergebenes Value Objekt mit Attributen V_ID, value und isShared
	 * @param contact übergebenes Contact Objekt mit Attribut C_ID
	 * @param property übergebenes Property Objekt mit Attribut P_ID
	 */
	public Value insert(Value value, Contact contact, Property property){
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
				+ value.getIsShared()
				+ ")") ;
				
				Contact c2 = new Contact();
				c2 = ContactMapper.contactMapper().findByID(contact);
				ContactMapper.contactMapper().update(contact);
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
		 * Update von Veränderungen falls sich value, P_ID, C_ID oder isShared ändert
		 * Gibt ein value zurück
		 * 
		 * @param value übergebenes Value Objekt mit Attributen V_ID, value und isShared
		 * @param contact übergebenes Contact Objekt mit Attribut C_ID
		 * @param property übergebenes Property Objekt mit Attribut P_ID
		 */
		public Value update(Value value, Contact contact, Property property){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_Value SET P_ID ="+property.getId()+", value ='" + value.getContent()+ "', C_ID=" + contact.getId() +", isShared="
						+ value.getIsShared() + " WHERE V_ID=" + value.getId());
				System.out.println("1. kommt so weit");
				Contact c2 = new Contact();
				System.out.println("die ID des übergebenen contact "+contact);
				c2 = ContactMapper.contactMapper().findByID(contact);
				System.out.println(c2.toString());
				System.out.println("die ID des gesetzten contactes "+c2.getId());
				ContactMapper.contactMapper().update(c2);
				System.out.println("durchgelaufen");
			}
			
		
		catch (SQLException e2){
			e2.printStackTrace();
			return value;
		}
		return value;}
		
		/**
		 * Delete.
		 *
		 * Entfernt alles aus T_Value wo die V_ID der ID des übergebenen Objekts entspricht
		 * Falls das Value Objekt das letzte Objekt war welches der P_ID zugewiesen war,
		 * wird auch die dazugehörige Property gelöscht mit dem aufruf der delete Methode im PropertyMapper
		 * 
		 * @param value übergebenes Value Objekt mit Attribut V_ID
		 */
		public void delete (Value value){
			Connection con = DBConnection.connection();
			
			try{
				Contact c2 = new Contact();
				c2 = ContactMapper.contactMapper().findByID(findContactByVID(value));
				ContactMapper.contactMapper().update(c2);
				
				Statement stmt = con.createStatement();
				
				stmt.executeUpdate("DELETE FROM T_Value WHERE V_ID =" + value.getId());
				
				Property p = new Property();
				Contact c = new Contact();
				
				p.setId(value.getPropertyid());
				c.setId(ValueMapper.valueMapper().findContactByVID(value).getId());
				
				
				if(ValueMapper.valueMapper().findAllByPID(p,c).size() < 1 && ValueMapper.valueMapper().findContactByVID(value).getId() != 20000000 ){
					
					PropertyMapper.propertyMapper().delete(p);
				}
				
				
			}
			
			
		
		catch (SQLException e2){
			e2.printStackTrace();
			
		}
}
		/**
		 * findAllContactsByVID.
		 *
		 * Gibt ein Contact welche eine bestimmte V_ID habt zurück
		 * Hierfür suchen wir nach C_ID in der T_Value Tabelle wo die V_ID der ID des übergebenen Objekts entspricht
		 * Mit der C_ID befüllen wir ein Contact Objekt mit der Methode findByID und geben ihn zurück
		 *
		 * @param value übergebenes Value Objekt mit Attribut V_ID
		 */
		
		public Contact findContactByVID(Value value){
			
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs= stmt.executeQuery("SELECT C_ID FROM T_Value WHERE V_ID=" + value.getId() + " ORDER BY V_ID");
				
				if(rs.next()){
					Contact c = new Contact();
					c.setId(rs.getInt("C_ID"));
					
					return c;	
				}
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return null;
			}
			return null;
		}
		
		/**
		 * getAllContactsByPID.
		 *
		 * Befüllt ein Vector mit Contacts welche eine bestimmte Property haben und gibt diesen Vector zurück
		 * Hierfür suchen wir nach C_ID in der T_Value Tabelle wo die P_ID der ID des übergebenen Objekts entspricht
		 * Mit der C_ID befüllen wir ein Contact Objekt mit der Methode findByID und fügen diesen dem Vector hinzu
		 *
		 * @param property übergebenes Property Objekt mit Attribut P_ID
		 */
		public Vector<Contact> getAllContactsByPID(Property property){

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
//						return result;
					}
					return result;
				}
		
		
		/**
		 * getAllValueByCID.
		 *
		 * Befüllt ein Vector mit Value welche eine bestimmter Contact hat und gibt diesen Vector zurück
		 * Hierfür suchen wir nach V_ID, value, P_ID und isShared in der T_Value Tabelle wo die C_ID der ID des übergebenen Objekts entspricht
		 * Wir befüllen diese Daten in ein Value Objekt welches wir dem Vector hinzufügen
		 * 
		 * @param contact übergebenes Contact Objekt mit Attribut C_ID
		 */
		public Vector <Value> getAllValueByCID (Contact contact){
			Connection con = DBConnection.connection();
			Vector <Value> result = new Vector <Value>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT V_ID, value, P_ID, isShared FROM T_Value WHERE C_ID ="+ contact.getId()+ " ORDER BY P_ID");

				while (rs.next()){
					Value v = new Value();
					v.setId(rs.getInt("V_ID"));
					v.setContent(rs.getString("value"));
					v.setPropertyid(rs.getInt("P_ID"));
					v.setIsShared(rs.getBoolean("isShared"));
	
					result.addElement(v);
				}
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return result;
			}
			return result;
			
}
		
		/**
		 * getAllSharedValueByCID.
		 *
		 * Befüllt ein Vector mit Value welche eine bestimmter Contact hat und geteilt ist und gibt diesen Vactor zurück
		 * Hierfür suchen wir nach V_ID, value, P_ID und isShared in der T_Value Tabelle wo die C_ID der ID des übergebenen Objekts entspricht und isShared 1 (TRUE) entspricht
		 * Wir befüllen diese Daten in ein Value Objekt welches wir dem Vector hinzufügen
		 * 
		 * @param contact übergebenes Contact Objekt mit Attributen C_ID und isShared
		 */
		
		public Vector <Value> getAllSharedValueByCID (Contact contact){
			Connection con = DBConnection.connection();
			Vector <Value> result = new Vector <Value>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT V_ID, value, P_ID, isShared FROM T_Value WHERE C_ID ="+ contact.getId()+ " AND  isShared=" + 1 +  " ORDER BY C_ID");

				while (rs.next()){
					Value v = new Value();
					v.setId(rs.getInt("V_ID"));
					v.setContent(rs.getString("value"));
					v.setPropertyid(rs.getInt("P_ID"));
					v.setIsShared(rs.getBoolean("isShared"));

					result.addElement(v);
				}
				
			}
			catch (SQLException e2){
				e2.printStackTrace();
				return result;
			}
			return result;
			
		}
		
		/**
		 * FindAllByPID.
		 * 
		 * Findet alle V_ID, value, P_ID und isShared wo die P_ID und die C_ID der ID der beiden übergebenen Objekte entspricht
		 * Befüllt das Value Objekt mit den Attributen und fügt dieses Objekt dem Vector hinzu
		 * Gibt ein Vector voller Value Objekte zurück
		 * 
		 * @param property übergebenes Property Objekt mit Attribut P_ID
		 * @param contact übergebenes Contact Objekt mit Attribut C_ID
		 */
		
		public Vector<Value> findAllByPID(Property property, Contact contact){
			Connection con = DBConnection.connection();
			Vector<Value> result = new Vector<Value>();
					
					try{
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT DISTINCT V_ID, value, P_ID, isShared FROM T_Value WHERE P_ID=" + property.getId()+ " AND C_ID=" + contact.getId() + " ORDER BY V_ID");
						
						while (rs.next()){
							Value v = new Value();
							v.setId(rs.getInt("V_ID"));
							v.setContent(rs.getString("value"));
							v.setPropertyid(rs.getInt("P_ID"));
							v.setIsShared(rs.getBoolean("isShared"));
							
							
							result.addElement(v);
						}		
					}catch(SQLException e2){
						e2.printStackTrace();
					}
					return result;
				}
		
	
}

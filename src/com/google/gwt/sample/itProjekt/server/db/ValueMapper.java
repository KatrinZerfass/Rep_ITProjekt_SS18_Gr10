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

	/** Konstruktor f�r den ValueMapper (Singleton) */
	//static weil Singleton. Einzige Instanz dieser Klasse
	private static ValueMapper valuemapper = null;
	
	/**
	 * ValueMapper.
	 *
	 *Falls noch kein ValueMapper existiert erstellt er ein neuen ValueMapper und gibt ihn zur�ck
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
	 * Bef�llt ein Value Objekt mit V_ID und value und f�gt dieses Objekt dem Vector hinzu
	 * Gibt ein Vector voller Value Objekte zur�ck
	 *
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
	 * Mit dem Contact Objekt welches C_ID beinhaltet wird durch findByID im ContactMapper das Contact Objekt vollst�ndig bef�llt und f�gt diesen dem Vector hinzu
	 * Gibt ein Vector voller Contact Objekte zur�ck welche eine bestimmte value besitzen
	 *
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
	 *Gibt alle Value Objekte zur�ck welche mit V_ID und value bef�llt sind
	 *Hierf�r holen wir V_ID und value aus der T_Value Tabelle und speichern diese in einem Value Objekt ab und f�gen diese dem Vector hinzu
	 *Am Ende geben wir diesen Vector zur�ck
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
	 *Sucht nach der h�chsten V_ID um diese um eins zu erh�hen und als neue V_ID zu nutzen
	 *Bef�llt T_Value mit V_ID, P_ID, value, C_ID und isShared
	 *Mit dem isShared legen wir fest ob die value True oder False ist bzw. ob es geteilt ist oder nicht
	 *Eine value wird zum Schluss zur�ckgegeben
	 *
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
						
				return value;	
				
			}
			Contact c2 = new Contact();
			c2 = ContactMapper.contactMapper().findByID(contact);
			ContactMapper.contactMapper().update(c2);
		}
		catch (SQLException e2){
			e2.printStackTrace();
			return value;
		}
		return value;}
	
		/**
		 * Update.
		 *
		 *Update von Ver�nderungen falls sich value, P_ID, C_ID oder isShared �ndert
		 *Gibt ein value zur�ck
		 * 
		 */
		public Value update(Value value, Contact contact, Property property){
			Connection con = DBConnection.connection();
			
			try{
				Statement stmt = con.createStatement();
				stmt.executeUpdate("UPDATE T_Value SET P_ID ="+property.getId()+", value ='" + value.getContent()+ "', C_ID=" + contact.getId() +", isShared="
						+ value.getIsShared());
				Contact c2 = new Contact();
				c2 = ContactMapper.contactMapper().findByID(contact);
				ContactMapper.contactMapper().update(c2);
			}
			
		
		catch (SQLException e2){
			e2.printStackTrace();
			return value;
		}
		return value;}
		
		/**
		 * Delete.
		 *
		 *Entfernt alles aus T_Value wo die V_ID der ID des �bergebenen Objekts entspricht
		 * 
		 */
		//TODO kommentar erg�nzen
		public void delete (Value value){
			Connection con = DBConnection.connection();
			
			try{
				
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
		 *Bef�llt ein Vector mit Contacts welche eine bestimmte Property haben und gibt diesen Vactor zur�ck
		 *Hierf�r suchen wir nach C_ID in der T_Value Tabelle wo die P_ID der ID des �bergebenen Objekts entspricht
		 *Mit der C_ID bef�llen wir ein Contact Objekt mit der Methode findByID und f�gen diesen dem Vector hinzu
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
						return result;
					}
					return result;
				}
		
		/**
		 * getAllPropertiesByCID.
		 *
		 *Bef�llt ein Vector mit Property welche eine bestimmter Contact hat und gibt diesen Vactor zur�ck
		 *Hierf�r suchen wir nach P_ID in der T_Value Tabelle wo die C_ID der ID des �bergebenen Objekts entspricht
		 *Mit dieser P_ID werden alle type und P_ID aus T_Property geholt und in einem neuen Property Objekt gespeichert und den Vector hinzugef�gt
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
		 * Bef�llt ein Vector mit Value welche eine bestimmter Contact hat und gibt diesen Vactor zur�ck
		 * Hierf�r suchen wir nach V_ID, value, P_ID und isShared in der T_Value Tabelle wo die C_ID der ID des �bergebenen Objekts entspricht
		 * Wir bef�llen diese Daten in ein Value Objekt welches wir dem Vector hinzuf�gen
		 *
		 */
		public Vector <Value> getAllValueByCID (Contact contact){
			Connection con = DBConnection.connection();
			Vector <Value> result = new Vector <Value>();
			
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT V_ID, value, P_ID, isShared FROM T_Value WHERE C_ID ="+ contact.getId()+ " ORDER BY C_ID");

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
		 * Bef�llt ein Vector mit Value welche eine bestimmter Contact hat und geteilt ist und gibt diesen Vactor zur�ck
		 * Hierf�r suchen wir nach V_ID, value, P_ID und isShared in der T_Value Tabelle wo die C_ID der ID des �bergebenen Objekts entspricht und isShared 1 (TRUE) entspricht
		 * Wir bef�llen diese Daten in ein Value Objekt welches wir dem Vector hinzuf�gen
		 *
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
		 * Findet alle V_ID, value, P_ID und isShared wo die P_ID und die C_ID der ID der beiden �bergebenen Objekte entspricht
		 * Bef�llt das Value Objekt mit den Attributen und f�gt dieses Objekt dem Vector hinzu
		 * Gibt ein Vector voller Value Objekte zur�ck
		 *
		 */
		
		public Vector<Value> findAllByPID(Property p, Contact c){
			Connection con = DBConnection.connection();
			Vector<Value> result = new Vector<Value>();
					
					try{
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT DISTINCT V_ID, value, P_ID, isShared FROM T_Value WHERE P_ID=" + p.getId()+ " AND C_ID=" + c.getId() + " ORDER BY V_ID");
						
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

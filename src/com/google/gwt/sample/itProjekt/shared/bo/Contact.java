package com.google.gwt.sample.itProjekt.shared.bo;

import java.sql.Timestamp;

/**
 * Die Klasse Contact, Datenstruktur für das Kontakt Business Objekt.
 */

public class Contact extends BusinessObject {

	
	private static final long serialVersionUID = 1L;

	/** Der Vorname des Kontakts. */
	private String firstname;
	
	/** Der Nachname des Kontakts. */
	private String lastname;
	
	/** Das Geschlecht des Kontakts. */
	private String sex;
	
	/** Der Besitzer des Kontakts. */
	private int owner;
	
	/** Der Kontakt ist ein Nutzer. */
	private boolean isUser = false;
	
	/** Erstellungsdatum des Kontakts. */
	private Timestamp creationdate;
		
	/** Modifikationsdatum des Kontakts. */
	private Timestamp modificationdate;
	
	/**
	 * Auslesen des Vornamens. 
	 * 
	 * @return String, welcher den Vornamen des Kontakts repräsentiert. 
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Setzen des Vornamens. 
	 * 
	 * @param firstname String, welcher den Vornamen des Kontakts repräsentiert. 
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * Auslesen des Nachnamens. 
	 * 
	 * @return String, welcher den Nachnamen des Kontakts repräsentiert.  
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 *Setzen des Nachnamens.
	 *
	 * @param lastname String, welcher den Nachnamen des Kontakts repräsentiert. 
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * Auslesen des Geschlechts.
	 * 
	 * @return String, welcher das Geschlecht des Kontatkts repräsentiert.  
	 */
	public String getSex() {
		return sex;
	}
	
	/**
	 * Setzen des Geschlechts. 
	 * 
	 * @param sex String, welcher das Geschlecht des Kontatkts repräsentiert.
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	/**
	 *Auslesen des Owners. 
	 *
	 * @return Integer, welcher den Eigentümer des Kontakts repräsentiert. 
	 */

	public int getOwner() {
		return owner;
	}
	
	/**
	 *Setzen des Owners. 
	 *
	 *@param owner Integer, welcher den Eigentümer des Kontakts repräsentiert.
	 */

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	/**
	 * Handelt es sich um den User Kontakt (Kontakt der den User repräsentiert), wird true zurückgegeben.
	 * Auslesen des IsUser Zustands. 
	 * 
	 * @return Boolean, welcher den IsUser Zustand repräsentiert. 
	 */
	
	public boolean getIsUser() {
		return isUser;
	}
	
	/**
	 * Setzen des IsUser Zustands. 
	 * 
	 * @param isUser Boolean, welcher den IsUser Zustand repräsentiert. 
	 */

	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}
	
	/**
	 *Auslesen des Erstellungsdatums. 
	 *
	 *@return Timestamp, welcher das Erstellungsdatum des Kontakts repräsentiert.
	 */
	
	public Timestamp getCreationDate(){
		return creationdate;
	}
	
	/**
	 *Setzen des Erstellungsdatums. 
	 *
	 *@param creationdate Timestamp, welcher das Erstellungsdatum des Kontakts repräsentiert. 
	 */
	
	public void setCreationDate(Timestamp creationdate){
		this.creationdate = creationdate;
		
	}
	
	/**
	 *Auslesen des Modifikationsdatums. 
	 *
	 *@return Timestamp, welcher das Modifiktaionsdatum des Kontakts respräsentiert.
	 */
		
	public Timestamp getModificationDate(){
		return modificationdate;
	}
	
	/**
	 *Setzen des Modifikationsdatums. 
	 *
	 *@param modificationdate Timestamp, welcher das Modifiktaionsdatum des Kontakts respräsentiert. 
	 */
	
	public void setModificationDate(Timestamp modificationdate){
		this.modificationdate = modificationdate;
	}

	
}

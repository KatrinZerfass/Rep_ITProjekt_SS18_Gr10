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
	
	private boolean isUser = false;
	
	/** Erstellungsdatum des Kontakts. */
	private Timestamp creationdate;
		
	/** Modifikationsdatum des Kontakts. */
	private Timestamp modificationdate;
	
	/**
	 * Auslesen des Vornamens.  
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Setzen des Vornamens.   
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * Auslesen des Nachnamens. 
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 *Setzen des Nachnamens. 
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * Auslesen des Geschlechts. 
	 */
	public String getSex() {
		return sex;
	}
	
	/**
	 * Setzen des Geschlechts. 
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	/**
	 *Auslesen des Owners.  
	 */

	public int getOwner() {
		return owner;
	}
	
	/**
	 *Setzen des Owners. 
	 */

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	/**
	 *Setzen des Erstellungsdatums. 
	 */
	
	public void setCreationDate(Timestamp creationdate){
		this.creationdate = creationdate;
		
	}
	
	/**
	 *Auslesen des Erstellungsdatums. 
	 */
	
	public Timestamp getCreationDate(){
		return creationdate;
	}
	
	/**
	 *Setzen des Modifikationsdatums. 
	 */
	
	public void setModificationDate(Timestamp modificationdate){
		this.modificationdate = modificationdate;
	}
	
	/**
	 *Auslesen des Modifikationsdatums. 
	 */
		
	public Timestamp getModificationDate(){
		return modificationdate;
	}

	public boolean getIsUser() {
		return isUser;
	}

	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}
	
	public Contact configureIsUser(boolean isUser) {
		this.isUser = isUser;
		return this;
	}
}

package com.google.gwt.sample.itProjekt.shared.bo;

import java.sql.Timestamp;


/**
 * Die Klasse Contact, Datenstruktur f�r das Kontakt Business Objekt.
 * @author JanNoller
 * 
 */
public class Contact extends BusinessObject {

	/** Der Vorname des Kontakts. */
	private String firstname;
	
	/** Der Nachname des Kontakts. */
	private String lastname;
	
	/** Das Geschlecht des Kontakts. */
	private String sex;
	
	/** Der Besitzer des Kontakts. */
	private int owner;
	
	/** Erstellungsdatum des Kontakts. */
	//private Timestamp creationdate;
	private Timestamp creationdate;
	
	
	
	
	/** Modifikationsdatum des Kontakts. */
	//private Timestamp modificationdate;
	private Timestamp modificationdate;
	
	/**
	 * TODO vielleicht Konstruktor(en)?

	
	public Contact() {}
	public Contact(String newfirstname, String newlastname, String newsex) {
		this.setFirstname(newfirstname);
		this.setLastname(newlastname);
		this.setSex(newsex);
	}
	public Contact(int newid, String newfirstname, String newlastname, String newsex) {
		super(newid);
		this.setFirstname(newfirstname);
		this.setLastname(newlastname);
		this.setSex(newsex);
	}
	
	 */
	
	/**
	 * Getter f�r den Vornamen.
	 *
	 * @return der Vorname
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Setter f�r Vorname.
	 *
	 * @param firstname der neue Vorname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * Getter f�r Nachnamen.
	 *
	 * @return der Nachname
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * Setter f�r Nachname.
	 *
	 * @param lastname der neue Nachname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * Getter f�r Geschlecht.
	 *
	 * @return das Geschlecht
	 */
	public String getSex() {
		return sex;
	}
	
	/**
	 * Setter f�r Geschlecht.
	 *
	 * @param sex das neue Geschlecht
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	public void setCreationDate(Timestamp creationdate){
		this.creationdate = creationdate;
		
	}
	
	public Timestamp getCreationDate(){
		return creationdate;
	}
	
	public void setModificationDate(Timestamp modificationdate){
		this.modificationdate = modificationdate;
	}
	
	public Timestamp getModificationDate(){
		return modificationdate;
	}
}

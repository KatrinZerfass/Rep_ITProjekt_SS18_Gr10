package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse Contact, Datenstruktur für das Kontakt Business Objekt.
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
	
	/**
	 * Getter für den Vornamen.
	 *
	 * @return der Vorname
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Setter für Vorname.
	 *
	 * @param firstname der neue Vorname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * Getter für Nachnamen.
	 *
	 * @return der Nachname
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * Setter für Nachname.
	 *
	 * @param lastname der neue Nachname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * Getter für Geschlecht.
	 *
	 * @return das Geschlecht
	 */
	public String getSex() {
		return sex;
	}
	
	/**
	 * Setter für Geschlecht.
	 *
	 * @param sex das neue Geschlecht
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
}

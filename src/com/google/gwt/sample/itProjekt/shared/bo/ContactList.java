package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse ContactList, Datenstruktur für das Kontaktlisten Business Objekt.
 */
public class ContactList extends BusinessObject {
	
	private static final long serialVersionUID = 1L;

	/** Der Name der Kontaktliste. */
	private String name;
	
	/** Der Eigentümer der Kontaktliste */
	private int owner;

	/**
	 * Auslesen des Namens. 
	 */
	public String getName() {
		return name;
	}

	/**
	 *Setzen des Namens. 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/** 
	 * Auslesen des Eigentürmers. 
	 */

	public int getOwner() {
		return owner;
	}
	
	/**
	 *Setzen des Eigentümers. 
	 */

	public void setOwner(int owner) {
		this.owner = owner;
	}
}

package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse ContactList, Datenstruktur für das Kontaktlisten Business Objekt.
 * @author JanNoller
 * 
 */
public class ContactList extends BusinessObject {
	
	
	private static final long serialVersionUID = 1L;

	/** Der Name der Kontaktliste. */
	private String name;
	
	/** Der Eigentümer der Kontaktliste */
	private int owner;
	
	/** Der Flag zur identifikation des eigenen Kontaktes */
	private boolean myContactsFlag = false; 

	/**
	 * Getter für den Namen.
	 *
	 * @return der Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter für den Namen.
	 *
	 * @param name der neue Name
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
	 * Setzen des Eigentümers. 
	 */

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	/**
	 * Getter für den ContactFlag.
	 *
	 * @return True or False 
	 */

	public boolean getMyContactsFlag() {
		return myContactsFlag;
	}

	/**
	 * Setzen des Flags. 
	 */
	
	public void setMyContactsFlag(boolean myContactsFlag) {
		this.myContactsFlag = myContactsFlag;
	}
}

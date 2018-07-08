package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse Property, Datenstruktur für die Eigenschaften eines Kontakts.
 */
public class Property extends BusinessObject {

	private static final long serialVersionUID = 1L;

	/** Die Art der Eigenschaft (Email-Adresse, Telefonnummer, etc.). */
	private String type;
	
	/** Der Kontakt, der die Eigenschaften besitzt. */
	private int contactID;

	/**
	 * Auslesen der Eigenschaftsart.
	 *
	 * @return Eigenschaftsart
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setzen der Eigenschaftsart. 
	 * 
	 * @param type Eigenschaftsart
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/** 
	 * Auslesen des Kontakts. 
	 * 
	 * @return Kontakt ID
	 */

	public int getContactID() {
		return contactID;
	}

	/** 
	 * Setzen des Kontakts. 
	 * 
	 * @param contactID Kontakt ID
	 */
	
	public void setContactID(int contactID) {
		this.contactID = contactID;
	}
}

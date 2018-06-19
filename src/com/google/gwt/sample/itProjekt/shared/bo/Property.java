package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse Property, Datenstruktur f�r die Eigenschaften eines Kontakts.
 * @author JanNoller
 * 
 */
public class Property extends BusinessObject {

	/** Die Art der Eigenschaft (Email-Adresse, Telefonnummer, etc.). */
	private String type;
	
	private int contactID;

	/**
	 * Getter f�r die Eigenschaftsart.
	 *
	 * @return die Eigenschaftsart
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter f�r die Eigenschaftsart.
	 *
	 * @param type die neue Eigenschaftsart
	 */
	public void setType(String type) {
		this.type = type;
	}

	public int getContactID() {
		return contactID;
	}

	public void setContactID(int contactID) {
		this.contactID = contactID;
	}
}

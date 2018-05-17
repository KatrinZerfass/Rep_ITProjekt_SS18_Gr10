package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse Property, Datenstruktur für die Eigenschaften eines Kontakts.
 * @author JanNoller
 * 
 */
public class Property extends BusinessObject {

	/** Die Art der Eigenschaft (Email-Adresse, Telefonnummer, etc.). */
	private String type;

	/**
	 * Getter für die Eigenschaftsart.
	 *
	 * @return die Eigenschaftsart
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter für die Eigenschaftsart.
	 *
	 * @param type die neue Eigenschaftsart
	 */
	public void setType(String type) {
		this.type = type;
	}
}

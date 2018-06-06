package com.google.gwt.sample.itProjekt.shared.bo;

// TODO: Auto-generated Javadoc
/**
 * Die Klasse Value, Datenstruktur für die Ausprägungen der Eigenschaften eines Kontakts.
 * @author JanNoller
 * .
 */
public class Value extends BusinessObject {

	/** Der Inhalt einer Ausprägung. */
	private String content;
	
	private int propertyid;

	/**
	 * Getter für die Ausprägung.
	 *
	 * @return der Inhalt
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Setter für Ausprägung.
	 *
	 * @param content der neue Inhalt
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public int getPropertyid() {
		return propertyid;
	}

	public void setPropertyid(int propertyid) {
		this.propertyid = propertyid;
	}
}

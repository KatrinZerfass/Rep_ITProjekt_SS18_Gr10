package com.google.gwt.sample.itProjekt.shared.bo;

/**
 * Die Klasse Value, Datenstruktur für die Ausprägungen der Eigenschaften eines Kontakts.
 */
public class Value extends BusinessObject {

	private static final long serialVersionUID = 1L;

	/** Der Inhalt einer Ausprägung. */
	private String content;
	
	/** Bei isShared = true wird die Ausprägung des Kontakts mitgeteilt.
	 * 	Bei isShared = false wird die Ausprägung nicht mitgeteilt. 
	 * */
	private boolean isShared = true;
	
	/**Die zugehörige Eigenschaft der Ausprägung */
	private int propertyid;

	/**
	 * Auslesen der Ausprägung.
	 */
	public String getContent() {
		return content;
	}

	/**
	 *Setzen der Ausprägung. 
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Auslesen der Eigenschaft. 
	 */

	public int getPropertyid() {
		return propertyid;
	}
	
	/** 
	 * Setzen der Eigenschaft. 
	 */

	public void setPropertyid(int propertyid) {
		this.propertyid = propertyid;
	}
	
	/**
	 *Auslesen des IsShared Werts. 
	 */

	public boolean getIsShared() {
		return isShared;
	}

	/**
	 * Setzen des IsSharedWerts.
	 */
	public void setIsShared(boolean isShared) {
		this.isShared = isShared;
	}
}

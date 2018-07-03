package com.google.gwt.sample.itProjekt.shared.bo;

// TODO: Auto-generated Javadoc
/**
 * Die Klasse ContactList, Datenstruktur f�r das Kontaktlisten Business Objekt.
 * @author JanNoller
 * 
 */
public class ContactList extends BusinessObject {
	
	/** Der Name der Kontaktliste. */
	private String name;
	
	private int owner;
	
	private boolean myContactsFlag = false; 

	/**
	 * Getter f�r den Namen.
	 *
	 * @return der Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter f�r den Namen.
	 *
	 * @param name der neue Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public boolean getMyContactsFlag() {
		return myContactsFlag;
	}

	public void setMyContactsFlag(boolean myContactsFlag) {
		this.myContactsFlag = myContactsFlag;
	}
}

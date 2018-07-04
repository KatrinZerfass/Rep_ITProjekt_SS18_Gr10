package com.google.gwt.sample.itProjekt.shared.bo;

/** 
 *Die Klasse User reräsentiert die Nutzer eines Systems. Jeder Nutzer besitzt eine 
 *eindeutige E-Mail Adresse. 
 */

public class User extends BusinessObject {
	
	private static final long serialVersionUID = 1L;
	
	/** Eine eindeutige E-Mail, mit der jeder Nutzer des Systems identifiziert werden kann. */ 
	private String email = null;

	/**Auslesen der E-Mail-Adresse. */ 
	
	public String getEmail() {
		return email;
	}
	
	/**Setzen der E-Mail-Adresse */

	public void setEmail(String email) {
		this.email = email;
	}
}

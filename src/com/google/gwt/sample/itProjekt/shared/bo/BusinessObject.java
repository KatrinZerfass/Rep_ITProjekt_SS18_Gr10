package com.google.gwt.sample.itProjekt.shared.bo;

import java.io.Serializable;

/**
 * Basisklasse für alles.
 * @author jnnll
 *
 */

public abstract class BusinessObject implements Serializable {
	
	/**
	 * Eindeutige Identifikationsnummer einer Instanz dieser Klasse und Möglichkeit zur Indetifizierung, 
	 * welche Rolle dieses Objekt in der DB hat.
	 */
	
	private int id;
	
	/**
	 * Setter für die ID.
	 */
	
	public void setId (int new_id) {
		this.id = new_id;
	}
	
	/**
	 * Getter für die ID
	 * @return id
	 */
	
	public int getId() {
		return this.id;
	}
	
	/**
	 * Überprüft die inhaltliche Gleicheit zweier BusinessObject Objekte (die ID).
	 */
	
	public boolean equals(Object object) {
		if (object != null && object instanceof BusinessObject) {
		      BusinessObject bo = (BusinessObject) object;
		      try {
		        if (bo.getId() == this.id)
		          return true;
		    }
		
			catch (IllegalArgumentException e) {
				return false;  
			}
		}
		return false;
	}
	
	/**
	 * Einfache ausgabe des Klassennamens einer BusinessObject Instanz und der ID:
	 */
	
	public String toString() {
		return this.getClass().getName() + "ID:" + this.getId();
	}
	
}

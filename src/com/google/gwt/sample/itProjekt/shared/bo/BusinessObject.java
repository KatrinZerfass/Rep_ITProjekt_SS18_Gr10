package com.google.gwt.sample.itProjekt.shared.bo;

import java.io.Serializable;

/**
 * Basisklasse für alle Business Objekte.
 * @author JanNoller
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
	 *
	 * @param new_id die neue ID des BO
	 */
	
	public void setId (int new_id) {
		this.id = new_id;
	}
	
	/**
	 * Getter für die ID.
	 *
	 * @return id
	 */
	
	public int getId() {
		return this.id;
	}
	
	/**
	 * Überprüft die inhaltliche Gleicheit zweier BusinessObject Objekte (die ID).
	 *
	 * @param object das zu vergleichende Objekt
	 * @return true, wenn inhaltlich gleich
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
	 * Einfache Ausgabe des Klassennamens einer BusinessObject Instanz und der ID des Business Objekts.
	 *
	 * @return der string oben beschriebene String
	 */
	
	public String toString() {
		return this.getClass().getName() + "ID:" + this.getId();
	}
	
}

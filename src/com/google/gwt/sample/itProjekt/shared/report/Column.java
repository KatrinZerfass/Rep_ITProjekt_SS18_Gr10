package com.google.gwt.sample.itProjekt.shared.report;

/**
 * Die Klasse Column, welche Teil der Datenstruktur für den Report ist. 
 * @see Row
 *
 */

import java.io.Serializable;



public class Column implements Serializable {
	
	/** Die Konstante serialVersionUID, welche als Versionsnummer bei der Serialisierung dient. */
	private static final long serialVersionUID=1L;
	
	/** der String "value" */
	private String value;
	
	/**
	 * Die Konstruktoren der Klasse Column, welche ermöglichen, 
	 * dass ein Column-Objekt mit einem String instanziiert kann. 
	 * Trotzdem kann eine Instanz der Column Klasse auch ohne String instanziiert werden. 
	 * Dieser kann anschließend mit setValue gesetzt werden. 
	 *
	 * @param v the v
	 */
	public Column(String v) {
		this.value=v;
	}
	public Column() {
		
	}
	/**
	 * Setzen der Variable value.
	 *
	 * @param v the new value
	 */
	public void setValue(String v) {
		this.value=v;
	}
	
	/**
	 * Auslesen der Variable value
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/** 
	 * Umwandeln des Column-Onjekts in einen String.
	 * @return den Wert als String
	 */
@Override 
	public String toString() {
		return this.value;
	}
}

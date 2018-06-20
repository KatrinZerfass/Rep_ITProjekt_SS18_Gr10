package com.google.gwt.sample.itProjekt.shared.report;

/**
 * Die Klasse Column, Datenstruktur für Report. 
 * @see Row
 * @author Anna-MariaGmeiner
 */

import java.io.Serializable;



public class Column implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID=1L;
	
	/** der String "value" */
	private String value;
	
	/**
	 * Instantiates a new column.
	 *
	 * @param v the v
	 */
	public Column(String v) {
		this.value=v;
	}
	public Column() {
		
	}
	/**
	 * Setter für die Variable value.
	 *
	 * @param v the new value
	 */
	public void setValue(String v) {
		this.value=v;
	}
	
	/**
	 * Getter für die Variable value
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/** 
	 * wandelt das value Objekt in einen String um. 
	 * @return den Wert als String
	 */
@Override 
	public String toString() {
		return this.value;
	}
}

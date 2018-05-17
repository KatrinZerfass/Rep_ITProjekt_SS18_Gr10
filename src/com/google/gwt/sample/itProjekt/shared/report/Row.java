package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

// 
/**
 *  Die Klasse Row, Datenstruktur für Report Objekt.
 *  @author Anna-MariaGmeiner
 *  @see Column 
 */
public class Row implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID=1L;
	
	/** The columns. */
	private Vector<Column> columns;
	
	/**
	 * Hinzufügen einer Spalte.
	 *
	 * @param c the c
	 */
	public void addColumn(Column c) {
		this.columns.addElement(c);
	}
	
	/**
	 * Entfernen einer Spalte.
	 *
	 * @param c the c
	 */
	public void removeColumn(Column c) {
		this.columns.removeElement(c);
	}
	
	/**
	 * Getter für die Spalten.
	 *
	 * @return the columns
	 */
	public Vector<Column> getColumns(){
		return this.columns;
	}
	
	/**
	 * Getter für die Anzahl der Spalten.
	 *
	 * @return Anzahl columns
	 */
	public int getNumColumns() {
		return this.columns.size();
	}
	
	/**
	 * Getter für bestimmte Spalte mit index i.
	 *
	 * @param i the i
	 * @return the column at
	 */
	public Column getColumnAt(int i) {
		return this.columns.elementAt(i);
	}
}

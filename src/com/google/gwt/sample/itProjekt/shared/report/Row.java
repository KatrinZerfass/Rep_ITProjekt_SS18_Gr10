package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

// 
/**
 *  Die Klasse Row, Datenstruktur f�r Report Objekt.
 *  @author Anna-MariaGmeiner
 *  @see Column 
 */
public class Row implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID=1L;
	
	/** The columns. */
	private Vector<Column> columns=new Vector<Column>();
	public Row(){};
	/**
	 * Hinzuf�gen einer Spalte.
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
	 * Getter f�r die Spalten.
	 *
	 * @return the columns
	 */
	public Vector<Column> getColumns(){
		return this.columns;
	}
	
	/**
	 * Getter f�r die Anzahl der Spalten.
	 *
	 * @return Anzahl columns
	 */
	public int getNumColumns() {
		return this.columns.size();
	}
	
	/**
	 * Getter f�r bestimmte Spalte mit index i.
	 *
	 * @param i the i
	 * @return the column at
	 */
	public Column getColumnAt(int i) {
		return this.columns.elementAt(i);
	}
}

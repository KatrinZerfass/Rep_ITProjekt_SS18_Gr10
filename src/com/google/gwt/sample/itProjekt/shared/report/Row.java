package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

/**
 * 	Die Klasse Row, welche Teil der Datenstruktur für das Report Objekt ist. 
 *  Diese wird benötigt um die Spalten in einer Zeile zu speichern. 
 *  Anschließend können dann die Zeilen zum Report hinzugefügt werden
 * 	Zeile einer Tabelle eines <code>SimpleReport</code>-Objekts. <code>Row</code>
 * 	-Objekte implementieren das <code>Serializable</code>-Interface und können
 * 	daher als Kopie z.B. vom Server an den Client übertragen werden.
 * 
 * @see SimpleReport
 * @see Column
 * @author Thies
 */
public class Row implements Serializable{
	
	/** Die Konstante serialVersionUID, welche als Versionsnummer bei der Serialisierung dient. */
	private static final long serialVersionUID=1L;
	
	/** Der Vektor, welche alle Spalten (columns) einer Reihe beinhaltet. */
	private Vector<Column> columns=new Vector<Column>();
	public Row(){};
	/**
	 * Hinzufügen einer Spalte zur Zeile .
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
	 * Auslesen der Spalten.
	 *
	 * @return the columns
	 */
	public Vector<Column> getColumns(){
		return this.columns;
	}
	
	/**
	 * Auslesen der Anzahl der Spalten.
	 *
	 * @return Anzahl columns
	 */
	public int getNumColumns() {
		return this.columns.size();
	}
	
	/**
	 * Auslesen einer bestimmten Spalte mit dem Index i.
	 *
	 * @param i the i
	 * @return the column at
	 */
	public Column getColumnAt(int i) {
		return this.columns.elementAt(i);
	}
}

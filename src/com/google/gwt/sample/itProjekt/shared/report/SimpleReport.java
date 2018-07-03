package com.google.gwt.sample.itProjekt.shared.report;

import java.util.Vector;


/**
 * Die abstrakte Report Klasse, welche als Basis für die einzelnen statischen Reports ist.
 * @author Thies
 */
public abstract class SimpleReport extends Report {
	
	/** Die Konstante serialVersionUID, welche als Versionsnummer bei der Serialisierung dient. */
	private static final long serialVersionUID=1L;
	
	/** Die Variable table mit allen enthaltenen Reihen. */
	private Vector<Row> table = new Vector<Row>();
	
	/**
	 * Hinzufügen von Zeilen zum Report.
	 *
	 * @param r the r
	 */
	public void addRow(Row r) {
		table.addElement(r);
	}
	
	/**
	 * Entfernen von Zeilen zum Report.
	 *
	 * @param r the r
	 */
	public void removeRow(Row r) {
		this.table.removeElement(r);
	}
	
	/**
	 * Auslesen der Tabelle.
	 *
	 * @return the rows
	 */
	public Vector<Row> getRows(){
		return this.table;
	}
}
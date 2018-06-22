package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

public abstract class SimpleReport extends Report implements Serializable{
	private static final long serialVersionUID = 1L;
	private Vector<Row> table = new Vector<Row>();
	public void addRow(Row r) {
		table.addElement(r);
	}
	
	/**
	 * Entfernen von Spalten zum Report.
	 *
	 * @param r the r
	 */
	public void removeRow(Row r) {
		this.table.removeElement(r);
	}
	
	/**
	 * Getter  der Tabelle.
	 *
	 * @return the rows
	 */
	public Vector<Row> getRows(){
		return this.table;
	}
}

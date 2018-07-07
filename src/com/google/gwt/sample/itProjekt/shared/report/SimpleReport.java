package com.google.gwt.sample.itProjekt.shared.report;

import java.util.Vector;

/**
 * Die abstrakte Report Klasse, welche als Basis für die einzelnen statischen Reports ist.
 * <p>
 * Ein einfacher Report, der neben den Informationen der Superklasse <code>
 * Report</code> eine Tabelle mit "Positionsdaten" aufweist. Die Tabelle greift
 * auf zwei Hilfsklassen namens <code>Row</code> und <code>Column</code> zurück.
 * </p>
 * <p>
 * Die Positionsdaten sind vergleichbar mit der Liste der Bestellpositionen
 * eines Bestellscheins. Dort werden in eine Tabelle zeilenweise Eintragung z.B.
 * bzgl. Artikelnummer, Artikelbezeichnung, Menge, Preis vorgenommen.
 * </p>
 * 
 * @see Row
 * @see Column
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
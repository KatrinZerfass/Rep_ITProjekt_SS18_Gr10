package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

/**
 * Ein zusammengesetzter Report. Dieser Report kann aus einer Menge von 
 * Teil-Reports (vgl. Attribut <code>subReports</code>) bestehen.
 * 
 * @author Rathke, Thies 
 */
public abstract class CompositeReport extends Report implements Serializable {	
	
	/** Die Konstante serialVersionUID, welche als Versionsnummer bei der Serialisierung dient. */
	private static final long serialVersionUID=1L;
	
	private Vector<Report> subReports = new Vector<Report>();

	/**
	 * Hinzufügen eines Teil-Reports.
	 * @param r der hinzuzufügende Teil-Report.
	 */
	public void addSubReport(Report r) {
		this.subReports.addElement(r);
	}

	/**
	 * Entfernen eines Teil-Reports.
	 * @param r der zu entfernende Teil-Report.
	 */
	public void removeSubReport(Report r) {
		this.subReports.removeElement(r);
	}

	/**
	 * Auslesen der Anzahl von Teil-Reports.
	 * @return int Anzahl der Teil-Reports.
	 */
	public int getNumSubReports() {
		return this.subReports.size();
	}

	/**
	 * Auslesen eines einzelnen Teil-Reports.
	 * @param i Position des Teilreports. Bei n Elementen läuft der Index i von 0
	 * bis n-1.
	 * 
	 * @return Position des Teil-Reports.
	 */	
	public Report getSubReportAt(int i) {
		return this.subReports.elementAt(i);
	}
	
}
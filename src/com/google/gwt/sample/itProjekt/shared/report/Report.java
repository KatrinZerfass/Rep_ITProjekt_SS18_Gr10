package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * <p>
 * Die abstrakte Report Klasse, welche als Basis für die einzelnen statischen Reports ist.
 *  Reports sind als <code>Serializable</code>
 * deklariert, damit sie von dem Server an den Client gesendet werden können.
 * Der Zugriff auf Reports erfolgt also nach deren Bereitstellung lokal auf dem
 * Client.
 * </p>
 * <p>
 * Ein Report besitzt eine Reihe von Standardelementen. Sie werden mittels
 * Attributen modelliert und dort dokumentiert.
 * </p>
 * 
 * @see Report
 * @author Thies
 */
public abstract class Report implements Serializable {
	
	/** Die Konstante serialVersionUID, welche als Versionsnummer bei der Serialisierung dient. */
	private static final long serialVersionUID=1L;
	
	/** Der String title. */
	private String title;
	
	/** Das Erstellungsdatum created. */
	private Date created;
	
	/** Der Paragraph mit den Kopfdaten. */
	private Paragraph headerData;
	
	/** Die Variable table mit allen enthaltenen Reihen. */
	private Vector<Row> table = new Vector<Row>();
	
	/** Der Paragraph der den Inhalt enthält. */
	private Paragraph content;
	
	/**
	 * Setzen des Inhalts.
	 *
	 * @param c the new content
	 */
	public void setContent(Paragraph c) {
		this.content=c;
	}
	
	/**
	 * Auslesen des Inhalts.
	 *
	 * @return the content
	 */
	public Paragraph getContent() {
		return this.content;
	}
	
	/**
	 * Setzen der Kopfdaten header data.
	 *
	 * @param headerData the new header data
	 */
	public void setHeaderData(Paragraph headerData) {
		this.headerData=headerData;
	}
	
	/**
	 * Auslesen der Kopfdaten header data.
	 *
	 * @return the header data
	 */
	public Paragraph getHeaderData() {
		return this.headerData;
	}
	
	/**
	 * Setzen des String title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title=title;
	}
	
	/**
	 * Auslesen des Titles title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Setzen des Erstellungsdatums.
	 *
	 * @param created the new created
	 */
	public void setCreated(Date created) {
		this.created=created;
	}
	
	/**
	 * Auslesen des Erstellungsdatums.
	 *
	 * @return the created
	 */
	public Date getCreated() {
		return this.created;
	}
	
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
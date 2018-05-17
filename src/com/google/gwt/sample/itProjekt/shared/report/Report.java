/*
 * 
 */

package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * Die abstrakte Report Klasse, welche als Basis für die einzelnen statischen Reports ist.
 * @author Anna-MariaGmeiner
 */
public abstract class Report implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID=1L;
	
	/** Der String title. */
	private String title;
	
	/** Das Erstellungsdatum created. */
	private Date created;
	
	/** Der Paragraph mit den header data. */
	private Paragraph headerData;
	
	/** Die Variable "table" mit allen enthaltenen Reihen. */
	private Vector<Row> table;
	
	/** The content. */
	private Paragraph content;
	
	/**
	 * Sets the content.
	 *
	 * @param c the new content
	 */
	public void setContent(Paragraph c) {
		this.content=c;
	}
	
	/**
	 * Getter für den Inhalt.
	 *
	 * @return the content
	 */
	public Paragraph getContent() {
		return this.content;
	}
	
	/**
	 * Setter für die Kopfdaten header data.
	 *
	 * @param headerData the new header data
	 */
	public void setHeaderData(Paragraph headerData) {
		this.headerData=headerData;
	}
	
	/**
	 * Getter für die Kopfdaten header data.
	 *
	 * @return the header data
	 */
	public Paragraph getHeaderData() {
		return this.headerData;
	}
	
	/**
	 * Setter des String title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title=title;
	}
	
	/**
	 * Getter von title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Setter des Erstellungsdatums.
	 *
	 * @param created the new created
	 */
	public void setCreated(Date created) {
		this.created=created;
	}
	
	/**
	 * Getter des Erstellungsdatums.
	 *
	 * @return the created
	 */
	public Date getCreated() {
		return this.created;
	}
	
	/**
	 * Hinzufügen von Spalten zum Report.
	 *
	 * @param r the r
	 */
	public void addRow(Row r) {
		this.table.addElement(r);
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

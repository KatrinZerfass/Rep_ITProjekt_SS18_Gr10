package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;


// 
/**
 * Die Klasse SimpleParagraph, welche reinen Text enth�lt.
 * @author Anna-MariaGmeiner
 */
public class SimpleParagraph extends Paragraph implements Serializable {
	
	/** Die Konstante serialVersionUID, welche als Versionsnummer bei der Serialisierung dient. */
	private static final long serialVersionUID=1L;
	
	/** Der String text. */
	private String text; 
	
	/**
 	 * Die Konstruktoren der Klasse SimpleParagraph, welche ermöglichen, 
	 * dass ein SimpleParagraph-Objekt mit einem String instanziiert kann. 
	 * Trotzdem kann eine Instanz der SimpleParagraph Klasse auch ohne String instanziiert werden. 
	 * Dieser kann anschließend mit setText gesetzt werden.	*
	 * @param text the text
	 */
	public SimpleParagraph(String text) {
		this.text=text;
	}
	public SimpleParagraph() {
		
	}
	
	/**
	 * Setzen der Variable text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text=text;
	}
	
	/**
	 * Auslesen der Variable text.
	 *
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/** 
	 * Umwandeln des SimpleParagraph-Objekts in einen String.
	 * @return den Wert als String
	 */
@Override
	public String toString() {
		return this.text;
	}
}

package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;


// 
/**
 * Die Klasse SimpleParagraph, welche reinen Text enthält.
 * @author Anna-MariaGmeiner
 */
public class SimpleParagraph extends Paragraph implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID=1L;
	
	/** Der String text. */
	private String text; 
	
	/**
	 * Konstruktor für den SimpleParagraph. 
	 *
	 * @param text the text
	 */
	public SimpleParagraph(String text) {
		this.text=text;
	}
	public SimpleParagraph() {
		
	}
	
	/**
	 * Setter für den String text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text=text;
	}
	
	/**
	 * Getter für den String text.
	 *
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}


@Override
	public String toString() {
		return this.text;
	}
}

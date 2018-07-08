package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

// 
/**
 * Diese Klasse stellt eine Menge einzelner Absätze (
 * <code>SimpleParagraph</code>-Objekte) dar. Diese werden als Unterabschnitte
 * in einem <code>Vector</code> abgelegt verwaltet.
 * @see SimpleParagraph
 * @author Thies
 */   

public class CompositeParagraph extends Paragraph implements Serializable {
	
	/** Die Konstante serialVersionUID, welche als Versionsnummer bei der Serialisierung dient. */
	private static final long serialVersionUID= 1L;
	
	/** Der Vector subParagraph, der nur SimpleParagraphs enth�lt . */
	private Vector<SimpleParagraph> subParagraph =new Vector<SimpleParagraph>();
	
	/**
	 * Hinzufügen eines SimpleParagraphs zu subParagraph.
	 *
	 * @param s the s
	 */
	public void addSubParagraph(SimpleParagraph s) {
		this.subParagraph.addElement(s);
	}
	
	/**
	 * Entfernen eines SimpleParagraph aus dem Vector subParagraph.
	 *
	 * @param s the s
	 */
	public void removeSubParagraph(SimpleParagraph s) {
		this.subParagraph.removeElement(s);
	}
	
	/**
	 * Auslesen von subParagraph.
	 *
	 * @return the sub paragraphs
	 */
	public Vector<SimpleParagraph> getSubParagraphs(){
		return this.subParagraph;
	}
	
	/**
	 * Auslesen  Anzahl der SubParagraphen
	 *
	 * @return Anzahl der SubParagraphen
	 */
	public int getNumParagraphs() {
		return this.subParagraph.size();
	}
	
	/**
	 * Auslesen eines SimpleParagraphs an bestimmten Index des Vectors subParagraph. 
	 * @param i Index des Vectors
	 * @return SimpleParagraph
	 */
	public SimpleParagraph getParagraphAt(int i) {
		return this.subParagraph.elementAt(i);
	}

@Override
	public String toString() {
	/**
	 * Anlegen eines Stringbuffers für Umwandeln der SubParagrahen in Simple Paragraphen.
	 */
		StringBuffer result = new StringBuffer();

	    /**
	    * Schleife um auf alle subParagraphs zuzugreifen und diese zum Stringbuffer hinzuzufügen
	    */
		for (int i = 0; i < this.subParagraph.size(); i++) {
	      SimpleParagraph p = this.subParagraph.elementAt(i);
	      result.append(p.toString() + "\n");
	    }

	    /**
	     * Gibt Result als String zurück.
	     **/
	    return result.toString();
	  }	
}

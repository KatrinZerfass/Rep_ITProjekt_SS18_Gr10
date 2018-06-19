package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

// 
/**
 * @author Anna-MariaGmeiner
 * Die Klasse CompositeParagraph, welche aus mehreren SimpleParagraphs besteht
 * @see SimpleParagraph
 */
public class CompositeParagraph extends Paragraph implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID= 1L;
	
	/** Der Vector subParagraph, der nur SimpleParagraphs enth�lt . */
	private Vector<SimpleParagraph> subParagraph =new Vector<SimpleParagraph>();
	
	/**
	 * Hinzuf�gen eines SimpleParagraphs zu subParagraph.
	 *
	 * @param s the s
	 */
	public void addSubParagraph(SimpleParagraph s) {
		this.subParagraph.addElement(s);
	}
	
	/**
	 * Entfernt SimpleParagraph aus subParagraph.
	 *
	 * @param s the s
	 */
	public void removeSubParagraph(SimpleParagraph s) {
		this.subParagraph.removeElement(s);
	}
	
	/**
	 * Getter von subParagraph.
	 *
	 * @return the sub paragraphs
	 */
	public Vector<SimpleParagraph> getSubParagraphs(){
		return this.subParagraph;
	}
	
	/**
	 * Getter Anzahl der SubParagraphen
	 *
	 * @return Anzahl der SubParagraphen
	 */
	public int getNumParagraphs() {
		return this.subParagraph.size();
	}
	
	public SimpleParagraph getParagraphAt(int i) {
		return this.subParagraph.elementAt(i);
	}

@Override
	public String toString() {
	/**
	 * Anlegen einesStringbuffers f�r Umwandeln der SubParagrahen in Simple Paragraphen.
	 */
		StringBuffer result = new StringBuffer();

	    // Schleife �ber alle Unterabschnitte
	    for (int i = 0; i < this.subParagraph.size(); i++) {
	      SimpleParagraph p = this.subParagraph.elementAt(i);

	      /*
	       * den jew. Unterabschnitt in einen String wandeln und an den Buffer h�ngen.
	       */
	      result.append(p.toString() + "\n");
	    }

	    /*
	     * Gibt Result als String zur�ck.
	     */
	    return result.toString();
	  }	
}

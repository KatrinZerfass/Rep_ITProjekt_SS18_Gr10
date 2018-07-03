package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

/**
 * Ein ReportWriter, der Reports mittels HTML formatiert. Das im Zielformat
 * vorliegende Ergebnis wird in der Variable reportText abgelegt und kann nach
 * Aufruf der entsprechenden Prozessierungsmethode mit getReportText()
 * ausgelesen werden.
 * 
 * @author Thies
 */

public class HTMLReportWriter implements Serializable{

	/** Die Konstante serialVersionUID, welche als Versionsnummer bei der Serialisierung dient. */
	private static final long serialVersionUID=1L;
	
	/** Der String zum Reporttext. */
	private String reportText= "";
	
	/**
	 * Zurücksetzen des String Reporttext.
	 */
	public void resetReportText() {
		this.reportText="";
	}
	
	/**
	 * Die Methode p2HTML, welche einen Paragraphen, egal ob Simple oder CompositeParagraph, 
	 * in einen String umwandelt und diesen zurückgibt.
	 *
	 * @param p the p
	 * @return the string
	 */
	public String p2HTML(Paragraph p) {
	    if (p instanceof CompositeParagraph) {
	      return this.p2HTML((CompositeParagraph) p);
	    }
	    else {
	      return this.p2HTML((SimpleParagraph) p);
	    }
	  }
	
	/**
	 * P 2 HTML.
	 *
	 * @param p the p
	 * @return the string
	 */
	public String p2HTML(CompositeParagraph p) {
		StringBuffer resultParagraph=new StringBuffer();
		
		for (int i=0; i < p.getNumParagraphs(); i++) {
			resultParagraph.append("<p>" + p.getParagraphAt(i) + "</p>");
		}
		
		return resultParagraph.toString();
	}
	
	/**
	 * P 2 HTML.
	 *
	 * @param p the p
	 * @return the string
	 */
	public String p2HTML(SimpleParagraph p) {
		return "<p>" + p.toString() + "</p>";
	}
		
	/* (non-Javadoc)
	 * @see com.google.gwt.sample.itProjekt.shared.report.ReportWriter#process(com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport)
	 */
	public void process(AllContactsOfUserReport r) {
		this.resetReportText();
		
		StringBuffer result=new StringBuffer();
		
		result.append("<H1>" + r.getTitle() + "</H1>");
		result.append("<table class=\"infotable\"><tr>");
		result.append("<td valign=\"top\"><b>" + p2HTML(r.getHeaderData()) + "</b></td>");
		result.append("<tr></tr><td>" + r.getCreated().toString()+ "</td></tr><tr></tr></table>");
		
		Vector<Row> rows=r.getRows();
		result.append("<table class=\"reporttable\">");
		
		for (int i=0; i < rows.size();i++) {
			Row row=rows.elementAt(i);
			result.append("<tr>");
			for(int k=0; k<row.getNumColumns();k++) {
				if (i==0) {
					result.append("<td class=\"columnhead\">" + row.getColumnAt(k)
		              + "</td>");
				}
				else {
					if(i>1) {
						result.append("<td class=\"reporttd\" valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
					else {
						result.append("<td class=\"reporttd\" valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
				}
			}
			result.append("</tr>");
		}
		result.append("</table>");
	    this.reportText = result.toString();
	  }
	
	

	public void process(AllSharedContactsOfUserReport r) {
		this.resetReportText();
		
		StringBuffer result=new StringBuffer();
		
		result.append("<H1>" + r.getTitle() + "</H1>");
		result.append("<table class=\"infotable\"><tr>");
		result.append("<td valign=\"top\"><b>" + p2HTML(r.getHeaderData()) + "</b></td>");
		result.append("<tr></tr><td>" + r.getCreated().toString()+ "</td></tr></table>");
		
		Vector<Row> rows=r.getRows();
		result.append("<table class=\"reporttable\">");
		
		for (int i=0; i<rows.size();i++) {
			Row row=rows.elementAt(i);
			result.append("<tr>");
			for(int k=0; k<row.getNumColumns();k++) {
				if (i==0) {
					result.append("<td class=\"columnhead\">" + row.getColumnAt(k)
		              + "</td>");
				}
				else {
					if(i>1) {
						result.append("<td class=\"reporttd\" valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
					else {
						result.append("<td class=\"reporttd\"  valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
				}
			}
			result.append("</tr>");
		}
		result.append("</table>");
	    this.reportText = result.toString();
	}
	
	
	public void process(AllContactsWithValueReport r) {
		this.resetReportText();
		
		StringBuffer result=new StringBuffer();
		
		result.append("<H1>" + r.getTitle() + "</H1>");
		result.append("<table class=\"infotable\"><tr>");
		result.append("<td valign=\"top\"><b>" + p2HTML(r.getHeaderData()) + "</b></td>");
		result.append("<tr></tr><td>" + r.getCreated().toString()+ "</td></tr></table>");
		
		Vector<Row> rows=r.getRows();
		result.append("<table class=\"reporttable\">");
		
		for (int i=0; i<rows.size();i++) {
			Row row=rows.elementAt(i);
			result.append("<tr>");
			for(int k=0; k<row.getNumColumns();k++) {
				if (i==0) {
					result.append("<td  class= \"columnhead\">" + row.getColumnAt(k)
		              + "</td>");
				}
				else {
					if(i>1) {
						result.append("<td class=\"reporttd\" valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
					else {
						result.append("<td class=\"reporttd\" valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
				}
			}
			result.append("</tr>");
		}
		result.append("</table>");
	    this.reportText = result.toString();
	}
	
	public void process(AllContactsWithPropertyReport r) {
		this.resetReportText();
		
		StringBuffer result=new StringBuffer();
		
		result.append("<H1>" + r.getTitle() + "</H1>");
		result.append("<table class=\"infotable\"><tr>");
		result.append("<td valign=\"top\"><b>" + p2HTML(r.getHeaderData()) + "</b></td>");
		result.append("<tr></tr><td>" + r.getCreated().toString()+ "</td></tr></table>");
		
		Vector<Row> rows=r.getRows();
		result.append("<table class=\"reporttable\">");
		
		for (int i=0; i<rows.size();i++) {
			Row row=rows.elementAt(i);
			result.append("<tr>");
			for(int k=0; k<row.getNumColumns();k++) {
				if (i==0) {
					result.append("<td  class=\"columnhead\">" + row.getColumnAt(k)
		              + "</td>");
				}
				else {
					if(i>1) {
						result.append("<td class=\"reporttd\" valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
					else {
						result.append("<td class=\"reporttd\" valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
				}
			}
			result.append("</tr>");
		}
		result.append("</table>");
	    this.reportText = result.toString();
	}
	
	/**
	 * Auslesen the report text.
	 *
	 * @return the report text
	 */
	public String getReportText() {
		return "<html><head><title>Report</title></head><body>" + this.reportText + "</body></html>";
	}
	


}

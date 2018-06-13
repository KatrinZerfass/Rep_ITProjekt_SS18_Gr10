package com.google.gwt.sample.itProjekt.shared.report;

import java.util.Vector;

public class HTMLReportWriter extends ReportWriter{

	private String reportText= "";
	
	public void resetReportText() {
		this.reportText="";
	}
	
	public String p2HTML(Paragraph p) {
	    if (p instanceof CompositeParagraph) {
	      return this.p2HTML((CompositeParagraph) p);
	    }
	    else {
	      return this.p2HTML((SimpleParagraph) p);
	    }
	  }
	
	public String p2HTML(CompositeParagraph p) {
		StringBuffer resultParagraph=new StringBuffer();
		
		for (int i=0; i < p.getNumParagraphs(); i++) {
			resultParagraph.append("<p>" + p.getParagraphAt(i) + "</p>");
		}
		
		return resultParagraph.toString();
	}
	
	public String p2HTML(SimpleParagraph p) {
		return "<p>" + p.toString() + "</p>";
	}
		
	@Override
	public void process(AllContactsOfUserReport r) {
		// TODO Auto-generated method stub
		this.resetReportText();
		
		StringBuffer result=new StringBuffer();
		
		result.append("H1>" + r.getTitle() + "</H1>");
		result.append("<table><tr>");
		result.append("<td valign=\"top\"><b>" + p2HTML(r.getHeaderData()) + "</b></td>");
		result.append("<tr><tr><td></td><td>" + r.getCreated().toString()+ "</td></tr></table>");
		
		Vector<Row> rows=r.getRows();
		result.append("<table>");
		
		for (int i=0; i<rows.size();i++) {
			Row row=rows.elementAt(i);
			result.append("tr>");
			for(int k=0; k<row.getNumColumns();k++) {
				if (i==0) {
					result.append("<td style=\"background:#222222;font-weight:bold\">" + row.getColumnAt(k)
		              + "</td>");
				}
				else {
					if(i>1) {
						result.append("<td valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
					else {
						result.append("<td valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
				}
			}
			result.append("</tr>");
		}
		result.append("</table>");
	    this.reportText = result.toString();
	  }
	
	

	@Override
	public void process(AllSharedContactsOfUserReport r) {
		this.resetReportText();
		
		StringBuffer result=new StringBuffer();
		
		result.append("H1>" + r.getTitle() + "</H1>");
		result.append("<table><tr>");
		result.append("<td valign=\"top\"><b>" + p2HTML(r.getHeaderData()) + "</b></td>");
		result.append("<tr><tr><td></td><td>" + r.getCreated().toString()+ "</td></tr></table>");
		
		Vector<Row> rows=r.getRows();
		result.append("<table>");
		
		for (int i=0; i<rows.size();i++) {
			Row row=rows.elementAt(i);
			result.append("tr>");
			for(int k=0; k<row.getNumColumns();k++) {
				if (i==0) {
					result.append("<td>" + row.getColumnAt(k)
		              + "</td>");
				}
				else {
					if(i>1) {
						result.append("<td valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
					else {
						result.append("<td valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
				}
			}
			result.append("</tr>");
		}
		result.append("</table>");
	    this.reportText = result.toString();
	}
	

	@Override
	public void process(AllContactsWithValueReport r) {
		this.resetReportText();
		
		StringBuffer result=new StringBuffer();
		
		result.append("H1>" + r.getTitle() + "</H1>");
		result.append("<table><tr>");
		result.append("<td valign=\"top\"><b>" + p2HTML(r.getHeaderData()) + "</b></td>");
		result.append("<tr><tr><td></td><td>" + r.getCreated().toString()+ "</td></tr></table>");
		
		Vector<Row> rows=r.getRows();
		result.append("<table>");
		
		for (int i=0; i<rows.size();i++) {
			Row row=rows.elementAt(i);
			result.append("tr>");
			for(int k=0; k<row.getNumColumns();k++) {
				if (i==0) {
					result.append("<td>" + row.getColumnAt(k)
		              + "</td>");
				}
				else {
					if(i>1) {
						result.append("<td valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
					else {
						result.append("<td valign=\"top\">" + row.getColumnAt(k)+ "</td>");
					}
				}
			}
			result.append("</tr>");
		}
		result.append("</table>");
	    this.reportText = result.toString();
	}
	
	
	public String getReportText() {
		return "<html><head><title>Report</title></head><body>" + this.reportText + "</body></html>";
	}
	


}

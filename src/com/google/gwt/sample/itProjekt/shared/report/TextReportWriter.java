package com.google.gwt.sample.itProjekt.shared.report;

import java.util.Vector;
/**
 * die Klasse TextReportWriter, die für die textuelle Ausgabe des 
 * Reports zuständig ist./**
 * @author Anna-MariaGmeiner
 */
public class TextReportWriter extends ReportWriter{
	
	/** Die Variable String report text. */
	private String reportText="";
	
	
	/**
	 * Reset report text.
	 */
	public void resetReportText() {
		this.reportText="";
	}
	
	/**
	 * Getter für den header.
	 *
	 * @param r the r
	 * @return the header
	 */
/**	public String getHeader(Report r) {
		return r.getHeaderData().toString();
	}
**/
	
	/* Override of the abstract methods  AlContactsOfUserReport, AllContactsReport, AllSharedContactsOfUserReport & AllContactsWithValueReport 
	 * 	 * @see com.google.gwt.sample.itProjekt.shared.report.ReportWriter#process(com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport)
	 */
	@Override
	public void process(AllContactsOfUserReport r) {
		this.resetReportText();
		
		StringBuffer result= new StringBuffer();
		result.append("Report: "+ r.getTitle()+ "\n\n");
		result.append(r.getHeaderData() + "\n");
		result.append("Erstellt am: " + r.getCreated().toString()+ "\n\n");
		Vector <Row> rows=r.getRows();
		
		for (Row row: rows) {
			for (int  i=0; i<row.getNumColumns(); i++) {
				result.append(row.getColumnAt(i) + "\t; \t");
			}
			result.append("\n");
		}
		this.reportText=result.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.sample.itProjekt.shared.report.ReportWriter#process(com.google.gwt.sample.itProjekt.shared.report.AllContactsReport)
	 */
	@Override
	public void process(AllContactsReport r) {
		this.reportText="";
		
		StringBuffer result=new StringBuffer();
		
		result.append(" Report: "+ r.getTitle() +"\n\n");
		
		if(r.getHeaderData()!=null) {
			result.append(r.getHeaderData()+ "\n");
		}
		
		Vector <Row> rows=r.getRows();
		result.append("Erstellt am: " + r.getCreated().toString()+"\n\n");
		for (Row row:rows) {
			for (int i=0; i< row.getNumColumns();i++){
				result.append(row.getColumnAt(i) + "\t\t");
			}
			result.append("\n");
		}
		this.reportText=result.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.sample.itProjekt.shared.report.ReportWriter#process(com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport)
	 */
	@Override
	public void process(AllSharedContactsOfUserReport r) {
		this.reportText="";
		
		StringBuffer result=new StringBuffer();
		
		result.append(" Report: "+ r.getTitle() +"\n\n");
		
		if(r.getHeaderData()!=null) {
			result.append(r.getHeaderData()+ "\n");
		}
		
		Vector <Row> rows=r.getRows();
		result.append("Erstellt am: " + r.getCreated().toString()+"\n\n");
		for (Row row:rows) {
			for (int i=0; i< row.getNumColumns();i++){
				result.append(row.getColumnAt(i) + "\t\t");
			}
			result.append("\n");
		}
		this.reportText=result.toString();
	}
	
/* (non-Javadoc)
 * @see com.google.gwt.sample.itProjekt.shared.report.ReportWriter#process(com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport)
 */
@Override
	public void process(AllContactsWithValueReport r) {
		this.reportText="";
		
		StringBuffer result=new StringBuffer();
		
		result.append(" Report: "+ r.getTitle() +"\n\n");
		
		if(r.getHeaderData()!=null) {
			result.append(r.getHeaderData()+ "\n");
		}
		
		Vector <Row> rows=r.getRows();
		result.append("Erstellt am: " + r.getCreated().toString()+"\n\n");
		for (Row row:rows) {
			for (int i=0; i< row.getNumColumns();i++){
				result.append(row.getColumnAt(i) + "\t\t");
			}
			result.append("\n");
		}
		this.reportText=result.toString();
	}
	
	/**
	 * Gets the report text.
	 *
	 * @return the report text
	 */
	public String getReportText() {
		return "<html><head><title>Report</title></head><body>" + this.reportText + "</body></html>";
		
	}
}

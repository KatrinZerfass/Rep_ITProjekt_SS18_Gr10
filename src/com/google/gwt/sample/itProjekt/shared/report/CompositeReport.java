package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

public class CompositeReport extends Report implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Vector<Report> subReports = new Vector<Report>();
	
	public void addSubReport(Report r) {
		this.subReports.addElement(r);
	}
	
	public void removeSubReport(Report r){
		this.subReports.removeElement(r);
	}
	
	public int getSizeSubReport(){
		return this.subReports.size();
	}
	
	public Report getSubReportAt(int i){
		return this.subReports.elementAt(i);
	}
	
	
}

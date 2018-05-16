package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Vector;

public class Row implements Serializable{
	private static final long serialVersionUID=1L;
	
	private Vector<Column> columns;
	
	public void addColumn(Column c) {
		this.columns.addElement(c);
	}
	
	public void removeColumn(Column c) {
		this.columns.removeElement(c);
	}
	
	public Vector<Column> getColumns(){
		return this.columns;
	}
	
	public int getNumColumns() {
		return this.columns.size();
	}
	
	public Column getColumnAt(int i) {
		return this.columns.elementAt(i);
	}
}

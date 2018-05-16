package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public abstract class Report implements Serializable {
	private static final long serialVersionUID=1L;
	
	String title;
	Date created;
	Paragraph headerData;
	Vector<Row> table;
}

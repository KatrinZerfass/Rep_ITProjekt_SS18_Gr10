package com.google.gwt.sample.itProjekt.shared.report;

import java.io.Serializable;

public class Column implements Serializable {
	private static final long serialVersionUID=1L;
	
	private String value;
	
	public void setValue(String v) {
		this.value=v;
	}
}

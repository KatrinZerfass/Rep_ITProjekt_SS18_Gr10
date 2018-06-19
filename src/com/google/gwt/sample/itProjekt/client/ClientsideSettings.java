package com.google.gwt.sample.itProjekt.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.sample.itProjekt.shared.EditorAdministration;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.ReportGenerator;
import com.google.gwt.sample.itProjekt.shared.ReportGeneratorAsync;
import com.google.gwt.sample.itProjekt.shared.bo.User;



public class ClientsideSettings {
	
	
	
	private static EditorAdministrationAsync editorAdministration = null;
	private static ReportGeneratorAsync reportGenerator = null;
	private static User user = null;
	

	
	
	public static EditorAdministrationAsync getEditorAdministration() {
	   
	    if (editorAdministration == null) {
	     editorAdministration = GWT.create(EditorAdministration.class);
	    }
	    return editorAdministration;
	  }
	
	
	
	public static ReportGeneratorAsync getReportGenerator() {
	    if (reportGenerator == null) {
	      reportGenerator = GWT.create(ReportGenerator.class);
	      reportGenerator.init(new AsyncCallback<Void>() {
	    	  @Override
	    	  public void onFailure(Throwable caught) {
	    	  }
	    	  @Override
	    	  public void onSuccess(Void result) {
	    	  }
	      });
	    }
	    return reportGenerator;
	  }

	public static void setUser(User u) {
		user = u;
	}
	
	public static User getUser() {
		return user;	
		
	}
}

package com.google.gwt.sample.itProjekt.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.itProjekt.shared.EditorAdministration;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.ReportGenerator;
import com.google.gwt.sample.itProjekt.shared.ReportGeneratorAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.sample.itProjekt.shared.bo.User;

/**
 * Klasse mit Diensten, die für alle Client-seitigen Klassen
 * relevant sind. Wird ergänzt durch die Klasse {@link ClientsideFunctions}
 * 
 * @author JanNoller
 */
public class ClientsideSettings {
	
	
	
	/** Remote Service Proxy zur Verbindungsaufnahme mit dem Server-seitgen 
	 * Dienst namens editorAdministration. */
	private static EditorAdministrationAsync editorAdministration = null;
	
	/** Remote Service Proxy zur Verbindungsaufnahme mit dem Server-seitgen 
	 * Dienst namens reportGenerator. */
	private static ReportGeneratorAsync reportGenerator = null;
	
	/** Client-seitiges Nutzer-Object, repräsentiert den angemeldeten Nutzer. */
	private static User user = null;
	

	
	
	/**
	 * Anlegen und Auslesen der applikationsweit eindeutigen EditorAdministration. Diese
     * Methode erstellt die EditorAdministration, sofern sie noch nicht existiert. Bei
     * wiederholtem Aufruf dieser Methode wird das bereits zuvor angelegte Objekt zurückgegeben.
	 *
	 * @return eindeutige Instanz des Typs EditorAdministrationAsync
	 */
	public static EditorAdministrationAsync getEditorAdministration() {
	   
	    if (editorAdministration == null) {
	     editorAdministration = GWT.create(EditorAdministration.class);
	    }
	    return editorAdministration;
	  }
	
	
	
	/**
	 * Anlegen und Auslesen des applikationsweit eindeutigen ReportGenerators. Diese
     * Methode erstellt den ReportGenerator, sofern dieser noch nicht existiert. Bei
     * wiederholtem Aufruf dieser Methode wird das bereits zuvor angelegte Objekt zurückgegeben.
	 *
	 * @return eindeutige Instanz des Typs ReportGeneratorAsync
	 */
	public static ReportGeneratorAsync getReportGenerator() {
	    if (reportGenerator == null) {
	      reportGenerator = GWT.create(ReportGenerator.class);
	      reportGenerator.init(new AsyncCallback<Void>() {
	    	  public void onFailure(Throwable t) {
	    		  System.out.println(t.getMessage());
	    	  }
	    	
	    	  public void onSuccess(Void result) {
	    	  }
	      });
	    }
	    return reportGenerator;
	  }

	/**
	 * Setter für das Nutzer-Objekt.
	 *
	 * @param u neuer Nutzer
	 */
	public static void setUser(User u) {
		user = u;
	}
	
	/**
	 * Getter für das Nutzer-Objekt.
	 *
	 * @return Nutzer-Object
	 */
	public static User getUser() {
		return user;	
		
	}
}

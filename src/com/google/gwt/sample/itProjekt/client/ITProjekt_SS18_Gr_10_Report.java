package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.sample.itProjekt.shared.LoginServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/*
 *  Entry-Point-Klasse des Report Generators.
 *  Der Reportgenerator besteht aus einem Navigationsteil, einer Kontaktliste und
 *  einer Detailansicht  
 */

public class ITProjekt_SS18_Gr_10_Report implements EntryPoint {
	
	//Relevante Attribute für LoginService
	
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	
	VerticalPanel mainPanel = new VerticalPanel ();
	HorizontalPanel addPanel = new HorizontalPanel();
	
	
	/*
	 * Die notwendigen Buttons für den Navigationsteil 
	 */
	
	Button allContactsOfUser = new Button("Suchen");
	Button allContacts = new Button("Alle Kontakte");
	Button allContactsWithValue = new Button ("Suchen");
	Button allSharedContactsOfUser = new Button ("Suchen");
	
	
	
	
	public void onModuleLoad() {
		
		
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	    public void onFailure(Throwable error) {
	     }

	     public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	       if(loginInfo.isLoggedIn()) {
	         loadApplication();
	      } else {
	          loadLogin();
	    }
	    }
	    });
	  }
		
		
	
	 public void loadApplication() {
		  
			signOutLink.setHref(loginInfo.getLogoutUrl());
			
			addPanel.add(allContactsOfUser);
			addPanel.add(allContacts);
			addPanel.add(allContactsWithValue);
			addPanel.add(allSharedContactsOfUser);
			
			
			mainPanel.add(addPanel);
			RootPanel.get("report").add(mainPanel);
			  
		  }
	 
	 
	 private void loadLogin() {
		  
		    signInLink.setHref(loginInfo.getLoginUrl());
		    loginPanel.add(loginLabel);
		    loginPanel.add(signInLink);
		    RootPanel.get("loginRepo").add(loginPanel);
		  }
	
	

}

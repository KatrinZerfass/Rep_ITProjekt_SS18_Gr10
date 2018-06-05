package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.sample.itProjekt.shared.CommonSettings;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.sample.itProjekt.shared.LoginServiceAsync;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;





/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ITProjekt_SS18_Gr_10 implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

//	private LoginInfo loginInfo = null;
//	private VerticalPanel loginPanel = new VerticalPanel();
//	private Label loginLabel = new Label(
//	      "Please sign in to your Google Account to access the application.");
//	private Anchor signInLink = new Anchor("Sign In");
//	private Anchor signOutLink = new Anchor("Sign Out");


	
//	EditorAdministrationAsync editorAdministration = null;

	  
	  public void onModuleLoad() {
		  
		  
		 /* 
		  * Login Status des Benutzers wird geprüft. (Wird erst zu einem späteren Zeitpunkt implementiert) 
		  */
	    
//		LoginServiceAsync loginService = GWT.create(LoginService.class);
//	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
//	    public void onFailure(Throwable error) {
//	     }
//
//	     public void onSuccess(LoginInfo result) {
//	        loginInfo = result;
//	       if(loginInfo.isLoggedIn()) {
//	         loadApplication();
//	      } else {
//	          loadLogin();
//	    }
//	    }
//	    });
//	  }
//	  
	  
	  
	  /*
	   * Diese Methode wird aufgerufen wenn der Benutzer eingeloggt ist. Sie beinhaltet 
	   * die eigentliche Applikation.  
	   */
	  
//	  public void loadApplication() {
		
//		signOutLink.setHref(loginInfo.getLogoutUrl());
		
		ContactForm cf = new ContactForm();
		ContactListContactTreeViewModel clctvm = new ContactListContactTreeViewModel();	
		
		
		clctvm.setContactForm(cf);
		cf.setClctvm(clctvm);
		
		//test Kontakt
		Contact testContact = new Contact();
		testContact.setFirstname("Hans");
		testContact.setLastname("Müller");
		testContact.setId(0001);
		testContact.setSex("m");
		
		
		//test Kontaktliste
		ContactList testContactList = new ContactList();
		testContactList.setId(1000);
		testContactList.setName("Meine Freunde");
		
		//test Zusammensetzung
//		editorAdministration.addContactToContactList(testContactList, testContact, new AsyncCallback<ContactList>() {
//			public void onFailure{
//				
//			}
//			public void onSuccess{
//				
//			}
//		});
		
		cf.setSelected(testContact);
		clctvm.addContactList(testContactList);
		clctvm.addContactOfContactList(testContactList, testContact);
		
		
		VerticalPanel contactPanel = new VerticalPanel();
		contactPanel.add(cf);
		RootPanel.get("Details").add(contactPanel);
		
	
	
		
		

		
		CellBrowser.Builder<String> builder = new CellBrowser.Builder<>(clctvm, "Root");	
		CellBrowser cellBrowser = builder.build(); //<--
	
		Window.alert("created cellbrowser");
		cellBrowser.setHeight("630");
		cellBrowser.setWidth("200");
	
	
		
		RootPanel.get("Navigator").add(cellBrowser);
		Window.alert("hey");
		  
	  }
	  
	  /* 
	   * Das Login Panel wird aufgerufen wenn der Benutzer nicht eingeloggt ist. 
	   */

//	  private void loadLogin() {
//		  
//	    signInLink.setHref(loginInfo.getLoginUrl());
//	    loginPanel.add(loginLabel);
//	    loginPanel.add(signInLink);
//	    RootPanel.get("Details").add(loginPanel);
//	  }
//	  
}	 

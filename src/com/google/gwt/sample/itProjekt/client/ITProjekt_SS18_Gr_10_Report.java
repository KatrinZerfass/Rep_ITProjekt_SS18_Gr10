package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.sample.itProjekt.shared.LoginServiceAsync;
import com.google.gwt.sample.itProjekt.shared.ReportGeneratorAsync;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.HTMLReportWriter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/*
 *  Entry-Point-Klasse des Report Generators.
 *  Der Reportgenerator besteht aus einem Navigationsteil, einer Kontaktliste und
 *  einer Detailansicht  
 */

public class ITProjekt_SS18_Gr_10_Report implements EntryPoint {
	
	//Relevante Attribute für LoginService
	private ReportGeneratorAsync reportGenerator=null;
	
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	
	VerticalPanel mainPanel = new VerticalPanel ();
	VerticalPanel logout=new VerticalPanel();
	HorizontalPanel addPanel = new HorizontalPanel();
	
	
	
	/*
	 * Die notwendigen Buttons für den Navigationsteil 
	 */
	Label userLabel = new Label("User: ");
	TextBox userTextBox = new TextBox();
	Button allContactsOfUserButton = new Button("Alle Kontakte eines Nutzers");
	Button allSharedContactsOfUserButton = new Button("Alle geteilten Kontakte eines Nutzers");
	Button allContactsWithValueButton = new Button("Kontakte mit bestimmter Ausprägung");
	
	public void onModuleLoad() {
		
		if(reportGenerator ==null) {
			reportGenerator=ClientsideSettings.getReportGenerator();
			
		}
	
		
			
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
						
			allContactsOfUserButton.addClickHandler(new ClickHandler() {
		         @Override
		         public void onClick(ClickEvent event) {
					 ClientsideSettings.getEditorAdministration().getUserInformation(userTextBox.getText(), new AsyncCallback<User>() {
	        			 public void onFailure(Throwable caught) {
	        				 
	        			 }
	    				 public void onSuccess(User result) {
	     					 reportGenerator.generateAllContactsOfUserReport(result, new AsyncCallback<AllContactsOfUserReport>() {
	    						 public void onFailure(Throwable caught) {
	    							 
	    						 }
	    						 public void onSuccess(AllContactsOfUserReport result) {
	    							 if (result != null) {
	    								 HTMLReportWriter writer=new HTMLReportWriter();
	    								 writer.process(result);
	    								 RootPanel.get("reporttext").clear();
	    								 RootPanel.get("reporttext").add(new HTML(writer.getReportText()));	
	    							 }
	    						}
	    					 });
	        			}
		        	 });
		         }	 
			});
			
			allSharedContactsOfUserButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					 ClientsideSettings.getEditorAdministration().getUserInformation(userTextBox.getText(), new AsyncCallback<User>() {
	        			 public void onFailure(Throwable caught) {
	        				 
	        			 }
	    				 public void onSuccess(User result) {
		   					 reportGenerator.generateAllSharedContactsOfUserReport(result, new AsyncCallback<AllSharedContactsOfUserReport>() {
		   						 public void onFailure(Throwable caught) {
		   						 }
		   						 public void onSuccess(AllSharedContactsOfUserReport result) {
		   						 }
		   					 });
	   				 	}
					});
				}
			});
			
			allContactsWithValueButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Value v = new Value();
					v.setContent(userTextBox.getText());
					 reportGenerator.generateAllContactsWithValueReport(v, new AsyncCallback<AllContactsWithValueReport>() {
						 public void onFailure(Throwable caught) {
							 
						 }
						 public void onSuccess(AllContactsWithValueReport result) {
							 HTMLReportWriter writer=new HTMLReportWriter();
							 writer.process(result);
							 RootPanel.get("reporttext").clear();
							 RootPanel.get("reporttext").add(new HTML(writer.getReportText()));
						 }
					 });
	   			}
			});
			
			addPanel.add(userLabel);
			addPanel.add(userTextBox);
			addPanel.add(allContactsOfUserButton);
			addPanel.add(allContactsWithValueButton);
			addPanel.add(allSharedContactsOfUserButton);
			
			logout.add(signOutLink);
			mainPanel.add(addPanel);
			RootPanel.get("report").add(logout);
			RootPanel.get("report").add(mainPanel);
			  
		  }
	 
	 private void loadLogin() {
		  
		    signInLink.setHref(loginInfo.getLoginUrl());
		    loginPanel.add(loginLabel);
		    loginPanel.add(signInLink);
		    RootPanel.get("loginRepo").add(loginPanel);
		  }
}
package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.sample.itProjekt.shared.LoginServiceAsync;
import com.google.gwt.sample.itProjekt.shared.ReportGeneratorAsync;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithPropertyReport;
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
import com.google.gwt.user.client.Window;

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
	HorizontalPanel reportbuttonPanel=new HorizontalPanel();
	HorizontalPanel searchPanel=new HorizontalPanel();
	HorizontalPanel descriptionPanel = new HorizontalPanel();
	
	HorizontalPanel addPanel = new HorizontalPanel();
	
	private User user = null;
	private ReportGeneratorAsync reportGenerator=null;
	
	
	/*
	 * Die notwendigen Buttons für den Navigationsteil 
	 */
	Label searchheading = new Label("Hier können Sie Ihre Kontakte gefiltert nach Eigenschaft oder Ausprägung ausgeben.");
	Label searchLabel = new Label("Suche: ");
	TextBox searchInput = new TextBox();
	Button allContactsOfUserButton = new Button("Alle Kontakte eines Nutzers");
	Button allSharedContactsOfUserButton = new Button("Alle geteilten Kontakte eines Nutzers");
	Button allContactsWithValueButton = new Button("Kontakte mit bestimmter Ausprägung");
	Button allContactsWithPropertyButton = new Button("Kontakte mit bestimmter Eigenschaft");

	public void onModuleLoad() {
		
		reportGenerator=ClientsideSettings.getReportGenerator();
		RootPanel.get("reporttext").setVisible(false);
		searchheading.addStyleName("searchheading");
		signOutLink.addStyleName("signout");
		signInLink.addStyleName("reportbutton");

		searchLabel.addStyleName("searchlabel");
		reportbuttonPanel.addStyleName("top-buttons");
		allContactsOfUserButton.addStyleName("reportbutton");
		allSharedContactsOfUserButton.addStyleName("reportbutton");
		allContactsWithValueButton.addStyleName("reportbutton");
		allContactsWithPropertyButton.addStyleName("reportbutton");
		searchInput.addStyleName("inputReport");

		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login("https://it-projekt-gruppe-10-203610.appspot.com/ITProjekt_SS18_Gr_10_Report.html", new AsyncCallback<LoginInfo>() {
	    	public void onFailure(Throwable error) {
	    	}
	    	public void onSuccess(LoginInfo result) {
	    		loginInfo = result;
	    		if(loginInfo.isLoggedIn()) {
	    			loadUserInformation();
	    		} else {
	    			loadLogin();
	    		}
	    	}
	    });
	  }
		
	public void loadUserInformation() {
    	
		if(reportGenerator == null){
			 reportGenerator = ClientsideSettings.getReportGenerator();
		 }
		
		reportGenerator.getUserInformation(loginInfo.getEmailAddress(), new AsyncCallback<User>() {
			
	    	public void onFailure(Throwable caught) {
	    		Window.alert("AsyncCallback fehlgeschlagen");			
			}

			public void onSuccess(User result) {
				ClientsideSettings.setUser(result);
				user = result;
				loadApplication();
				
			}
			   		
	    });
  	}
	
	 public void loadApplication() { 
	  
			signOutLink.setHref(loginInfo.getLogoutUrl());
						
			allContactsOfUserButton.addClickHandler(new ClickHandler() {
		         @Override
		         public void onClick(ClickEvent event){
		        	 reportGenerator.generateAllContactsOfUserReport(user, new AsyncCallback<AllContactsOfUserReport>() {
	    						 public void onFailure(Throwable caught) {
//	   				 				    RootPanel.get("reporttext").setVisible(false);	
//	   				 				    Label errornote=new Label("Es Existieren leider keine Kontakte");
//	   				 				    errornote.addStyleName("errornote");
//	   				 				    mainPanel.add(errornote);
	    							 
	    						 }
	    						 public void onSuccess(AllContactsOfUserReport result) {
	    							 if (result != null) {
	    								 RootPanel.get("reporttext").setVisible(true);
	    								 HTMLReportWriter writer=new HTMLReportWriter();
	    								 writer.process(result);
	    								 RootPanel.get("reporttext").clear();
	    								 RootPanel.get("reporttext").add(new HTML(writer.getReportText()));
	    							 }
	    						}
	    					 });
	        			}
		        	 }
		        	
					 );
		        	 
		    
	 
			
			allSharedContactsOfUserButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(searchInput.getText() != ""){
						User sharedUser=new User();
						sharedUser.setEmail(searchInput.getText());
						reportGenerator.generateAllSharedContactsOfUserReport(user, sharedUser, new AsyncCallback<AllSharedContactsOfUserReport>() {
				 				    	public void onFailure(Throwable caught) {
//		   				 				    RootPanel.get("reporttext").setVisible(false);	
//		   				 				    Label errornote=new Label("Es Existieren leider keine geteilten Kontakte mit dem eingegebenen Nutzer");
//		   				 				    errornote.addStyleName("errornote");
//		   				 				    mainPanel.add(errornote);
			   						 }
			   						 public void onSuccess(AllSharedContactsOfUserReport result) {
			 		 				    RootPanel.get("reporttext").setVisible(true);
		   								if(result!=null){
			   	   	    						 HTMLReportWriter writer=new HTMLReportWriter();
		   	    								 writer.process(result);
		   	    								 RootPanel.get("reporttext").clear();
		   	    								 RootPanel.get("reporttext").add(new HTML(writer.getReportText()));	
		   	    							 }	
		   								 }
		   					 });
						}
						else{
							Window.alert("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.");
							}
	   				 	}
					});
				

			
			allContactsWithValueButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(searchInput.getText() != ""){
					Value v = new Value();
					v.setContent(searchInput.getText());
					 reportGenerator.generateAllContactsWithValueReport(user, v, new AsyncCallback<AllContactsWithValueReport>() {
						 public void onFailure(Throwable caught) {
						 }
						 public void onSuccess(AllContactsWithValueReport result) {
								if (result!=null) {
				 				    RootPanel.get("reporttext").setVisible(true);
				 				    HTMLReportWriter writer=new HTMLReportWriter();
				 				    writer.process(result);
				 				    RootPanel.get("reporttext").clear();
				 				    RootPanel.get("reporttext").add(new HTML(writer.getReportText()));
									 
								}
						 }
					 });
					}
				else{
					Window.alert("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.");
					
				}}});
			
			allContactsWithPropertyButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(searchInput.getText() != ""){
					Property p = new Property();
					p.setType(searchInput.getText());
					 reportGenerator.generateAllContactsWithPropertyReport(user, p, new AsyncCallback<AllContactsWithPropertyReport>() {
						 public void onFailure(Throwable caught) {
							 System.out.println("springt in onFailure");
							 RootPanel.get("reporttext").setVisible(false);
							 Window.alert("Es wurde kein Kontakt mit der angegebenen Eigenschaft gefunden");
						 }
						 public void onSuccess(AllContactsWithPropertyReport result) {
								if (result !=null) {
				 				    RootPanel.get("reporttext").setVisible(true);
				 				    HTMLReportWriter writer=new HTMLReportWriter();
								 	writer.process(result);
									RootPanel.get("reporttext").clear();
									RootPanel.get("reporttext").add(new HTML(writer.getReportText()));
									}
						 }
					 });
	   			}else{
	   				Window.alert("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.");
	   			}}
			});
			
			
			descriptionPanel.add(searchheading);
			addPanel.add(searchLabel);
			addPanel.add(searchInput);
			reportbuttonPanel.add(allContactsOfUserButton);
			addPanel.add(allSharedContactsOfUserButton);
			addPanel.add(allContactsWithValueButton);
			addPanel.add(allContactsWithPropertyButton);
			
			RootPanel.get("signout").add(signOutLink);
			
			RootPanel.get("report").add(reportbuttonPanel);
			mainPanel.add(searchPanel);
			mainPanel.add(descriptionPanel);
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
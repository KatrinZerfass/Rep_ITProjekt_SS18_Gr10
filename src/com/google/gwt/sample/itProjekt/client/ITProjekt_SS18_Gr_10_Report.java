package com.google.gwt.sample.itProjekt.client;

import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
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
	VerticalPanel selectionPanel = new VerticalPanel ();
	HorizontalPanel selectionHPanel= new HorizontalPanel();
	
	
	HorizontalPanel reportbuttonPanel=new HorizontalPanel();
	HorizontalPanel searchPanel=new HorizontalPanel();
	HorizontalPanel descriptionPanel = new HorizontalPanel();
	HorizontalPanel propertyPanel=new HorizontalPanel();
	HorizontalPanel valuePanel=new HorizontalPanel();
	HorizontalPanel participantPanel = new HorizontalPanel();
	
	private User user = null;
	private ReportGeneratorAsync reportGenerator=null;
	private TextSuggest sb=null;
	
	/*
	 * Die notwendigen Buttons für den Navigationsteil 
	 */
	Label searchheading = new Label("Hier können Sie sich einen Report über Ihre Kontakte ausgeben lassen. "
			+ "Dabei können Sie Ihren Report zusätzlich nach Eigenschaften der Kontakte (z.B. Arbeitsplatz) oder bestimmten Ausprägungen der Kontakte (z.B. Hochschule der Medien) filtern. "
			+ "Außerdem können Sie nach Eingabe eines Nutzers, die Kontakte sehen, die Sie mit diesem geteilt haben.");
	Label participantLabel = new Label("Nutzer: ");
	Label valueLabel = new Label("Ausprägung: ");
	Label propertyLabel = new Label("Eigenschaft: ");


	TextBox propertyInput = new TextBox();
	TextBox valueInput = new TextBox();
	ListBox propertylistbox = new ListBox();
	ListBox reportlistbox = new ListBox();
	Button getReportButton = new Button("Zum Report");
	Button allContactsOfUserButton = new Button("Report ausgeben");
	Button allSharedContactsOfUserButton = new Button("Report ausgeben");
	Button allContactsWithValueButton = new Button("Report ausgeben");
	Button allContactsWithPropertyButton = new Button("Report ausgeben");

	public class TextSuggest{

		private SuggestBox sb;
        private MultiWordSuggestOracle oracle;
        public TextSuggest(MultiWordSuggestOracle inputOracle) {
    		
    		setOracle(inputOracle);
    						
    		reportGenerator.getAllUserSuggestions(user, new AsyncCallback<Vector<String>>() {
    			public void onFailure(Throwable arg0) {
    				Window.alert("Fehler beim holen aller User in der InputDialogBox");
    			}
    			@Override
    			public void onSuccess(Vector<String> arg0) {
					for(String s : arg0) {
						getOracle().add(s);
					}
    				setSuggestBox(new SuggestBox(getOracle()));		
    			}
    		});
    	}		

		public SuggestBox getSuggestBox() {
			return sb;
		}

		public void setSuggestBox(SuggestBox sb) {
			this.sb = sb;
		}

		public MultiWordSuggestOracle getOracle() {
			return oracle;
		}

		public void setOracle(MultiWordSuggestOracle oracle) {
			this.oracle = oracle;
		}
	}

			
	
	public void onModuleLoad() {
		
		reportGenerator=ClientsideSettings.getReportGenerator();
		RootPanel.get("reporttext").setVisible(false);
		
		/**
		 * Instanziierung der SuggestBox für die Eingabe der Email.
		**/
		sb=new TextSuggest(new MultiWordSuggestOracle());

		/**Layout**/		
		searchheading.addStyleName("searchheading");
		signOutLink.addStyleName("signout");
		signInLink.addStyleName("reportbutton");		
		propertyLabel.addStyleName("searchlabel");
		valueLabel.addStyleName("searchlabel");
		participantLabel.addStyleName("searchlabel");
		reportbuttonPanel.addStyleName("top-buttons");
		valuePanel.addStyleName("panel");
		propertyPanel.addStyleName("panel");
		participantPanel.addStyleName("panel");
		getReportButton.addStyleName("reportbutton");
		allContactsOfUserButton.addStyleName("reportbutton");
		allSharedContactsOfUserButton.addStyleName("reportbutton");
		allContactsWithValueButton.addStyleName("reportbutton");
		allContactsWithPropertyButton.addStyleName("reportbutton");
		propertyInput.addStyleName("sonstigeinput");
		valueInput.addStyleName("reportSuggestBox");
		
		/**
		 * Auslesen der vordefinierten Eigenschaften aus der Datenbank, um diese zur ListBox hinzuzufügen.
		**/
		
		reportGenerator.getAllPredefinedPropertiesOfReport(new AsyncCallback<Vector<Property>>(){
			public void onFailure(Throwable t) {
				Window.alert("Auslesen aller vordefinierten Eigenschaften fehlgeschlagen");
			}
			public void onSuccess(Vector<Property> properties) {
				for (Property p : properties) {
					propertylistbox.addItem(p.getType());
				}
				propertylistbox.addItem("eigene Eigenschaftsart");	
			}
		});
		
		propertylistbox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
            	int size=propertylistbox.getItemCount();
            	int Item = propertylistbox.getSelectedIndex();
            	
            	if(Item==(size-1)){
            		propertyPanel.remove(allContactsWithPropertyButton);
            		propertyPanel.add(propertyInput);
            		propertyPanel.add(allContactsWithPropertyButton);
                	}
            	else{
            		propertyPanel.remove(propertyInput);
            	}
            	}});
            	           	
        reportlistbox.addItem("Alle meine Kontakte");
        reportlistbox.addItem("Alle mit einem Nutzer geteilten Kontakte");
        reportlistbox.addItem("Kontakte mit bestimmter Ausprägung");
        reportlistbox.addItem("Kontakte mit bestimmter Eigenschaft");

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
		
	/**
	 * Auslesen der Nutzerinformationen und abspeichern der Informationen in die Variable user.
	**/
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
	
	/**
	 * Laden der Applikation.
	**/
	 public void loadApplication() { 
	  		 
			signOutLink.setHref(loginInfo.getLogoutUrl());
	        
			
			/**
			 * Hinzufügen eines Clickhandlers um den passenden Report auszuwählen.
			**/
			getReportButton.addClickHandler(new ClickHandler() {
		         public void onClick(ClickEvent event){
		        	 	RootPanel.get("reporttext").setVisible(false);
		       			switch (reportlistbox.getSelectedIndex()){
		        			case 0:
		        				mainPanel.remove(valuePanel);
		        				mainPanel.remove(propertyPanel);
		        				mainPanel.remove(participantPanel);
		        				reportGenerator.generateAllContactsOfUserReport(user, new AsyncCallback<AllContactsOfUserReport>() {
		    						 public void onFailure(Throwable caught) {
		    							  RootPanel.get("reporttext").setVisible(false);	
		   				 				  Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");
		    						 }
		    						 public void onSuccess(AllContactsOfUserReport result) {
		    							    							 
		    							 if (result != null) {
		    								 RootPanel.get("reporttext").setVisible(true);
		    								 HTMLReportWriter writer=new HTMLReportWriter();	    								 
		    								 writer.process(result);
		    								 RootPanel.get("reporttext").clear();
		    								 RootPanel.get("reporttext").add(new HTML(writer.getReportText()));
		    						}}});
		        				break;
		        			case 1:
		        				mainPanel.remove(valuePanel);
		        				mainPanel.remove(propertyPanel);
		        				mainPanel.add(participantPanel);
		        				break;
		        			case 2: 
		         				mainPanel.remove(propertyPanel);
		        				mainPanel.remove(participantPanel);
		        				mainPanel.add(valuePanel);
		        				break;
		        			case 3:
		         				mainPanel.remove(valuePanel);
		        				mainPanel.remove(participantPanel);
		        				mainPanel.add(propertyPanel);
		        				break;
		        			}
		        		 }
		              });		
			/**
			 * Hinzufügen eines Clickhandlers um den Report für alle Kontakte des Nutzers zu generieren.
			**/
			allContactsOfUserButton.addClickHandler(new ClickHandler() {
		         @Override
		         public void onClick(ClickEvent event){
		        	 reportGenerator.generateAllContactsOfUserReport(user, new AsyncCallback<AllContactsOfUserReport>() {
	    						 public void onFailure(Throwable caught) {
	    							  RootPanel.get("reporttext").setVisible(false);	
	   				 				  Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");
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
		        	 });
			/**
			 * Hinzufügen eines Clickhandlers um den Report für "alle geteilten Kontakte mit einem Nutzers" zu generieren.
			**/    	 
		    allSharedContactsOfUserButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(sb.getSuggestBox().getText() != ""){
						String[] split = sb.getSuggestBox().getText().split(" - ");
						String userEmail = split[1].substring(0, split[1].length());
						User sharedUser=new User();
						sharedUser.setEmail(userEmail);
						reportGenerator.generateAllSharedContactsOfUserReport(user, sharedUser, new AsyncCallback<AllSharedContactsOfUserReport>() {
				 				    	public void onFailure(Throwable caught) {
		   				 				    RootPanel.get("reporttext").setVisible(false);	
		   				 				    Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");

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
							final ClientsideFunctions.popUpBox emptyTextbox = new ClientsideFunctions.popUpBox("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.", new ClientsideFunctions.OkButton());
							emptyTextbox.getOkButton().addCloseDBClickHandler(emptyTextbox);
							}
	   				 	}
					});
		    /**
			 * Hinzufügen eines Clickhandlers um den Report für "alle Kontakte mit einer bestimmten Ausprägung" zu generieren.
			**/  
			allContactsWithValueButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(valueInput.getText() != ""){
					Value v = new Value();
					v.setContent(valueInput.getText());
					 reportGenerator.generateAllContactsWithValueReport(user, v, new AsyncCallback<AllContactsWithValueReport>() {
						 public void onFailure(Throwable caught) {
							RootPanel.get("reporttext").setVisible(false);	
				 			Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");
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
					final ClientsideFunctions.popUpBox emptyTextbox = new ClientsideFunctions.popUpBox("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.", new ClientsideFunctions.OkButton());
					emptyTextbox.getOkButton().addCloseDBClickHandler(emptyTextbox);
				}}});
			
			/**
			 * Hinzufügen eines Clickhandlers um den Report für "alle Kontakte mit einer bestimmten Eigenschaft" zu generieren.
			**/  
			
			allContactsWithPropertyButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(!propertylistbox.getSelectedItemText().equals("eigene Eigenschaftsart")){
					Property p = new Property();
					p.setType(propertylistbox.getSelectedItemText());
					 reportGenerator.generateAllContactsWithPropertyReport(user, p, new AsyncCallback<AllContactsWithPropertyReport>() {
						 public void onFailure(Throwable caught) {
							  RootPanel.get("reporttext").setVisible(false);	
				 			  Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");
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
	   				if(propertyInput.getText() != ""){
	   				Property p = new Property();
					p.setType(propertyInput.getText());
					reportGenerator.generateAllContactsWithPropertyReport(user, p, new AsyncCallback<AllContactsWithPropertyReport>() {
						 public void onFailure(Throwable caught) {
							
							 RootPanel.get("reporttext").setVisible(false);
							 Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");
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
	   				final ClientsideFunctions.popUpBox emptyTextbox = new ClientsideFunctions.popUpBox("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.", new ClientsideFunctions.OkButton());
					emptyTextbox.getOkButton().addCloseDBClickHandler(emptyTextbox);
	   				}
				}}
			});
			
			/**
			 * Befüllen des Hauptpanels  
			**/  
			
			descriptionPanel.add(searchheading);
			sb.getSuggestBox().addStyleName("reportSuggestBox");
			reportbuttonPanel.add(allContactsOfUserButton);
			participantPanel.add(participantLabel);
			participantPanel.add(sb.getSuggestBox());
			participantPanel.add(allSharedContactsOfUserButton);
			propertyPanel.add(propertyLabel);
			propertyPanel.add(propertylistbox);
			propertyPanel.add(allContactsWithPropertyButton);
			valuePanel.add(valueLabel);
			valuePanel.add(valueInput);
			valuePanel.add(allContactsWithValueButton);

			RootPanel.get("signout").add(signOutLink);
			selectionHPanel.add(reportlistbox);
			selectionHPanel.add(getReportButton);
			selectionPanel.add(descriptionPanel);
			selectionPanel.add(selectionHPanel);
			mainPanel.add(selectionPanel);
			RootPanel.get("report").add(mainPanel);
	   			
			}  
	 	/**
		 * Laden des Logins und befüllen des LoginPanels.
		**/  
		
	private void loadLogin() {
		    signInLink.setHref(loginInfo.getLoginUrl());
		    loginPanel.add(loginLabel);
		    loginPanel.add(signInLink);
		    RootPanel.get("loginRepo").add(loginPanel);
		  }
}
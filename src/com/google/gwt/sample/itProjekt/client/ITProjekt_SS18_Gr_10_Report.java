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


/**
 *  Entry-Point-Klasse des Report Generators.
 *  Der Reportgenerator besteht aus einem Navigationsteil, einer Kontaktliste und
 *  einer Detailansicht 
 */
public class ITProjekt_SS18_Gr_10_Report implements EntryPoint {
	
	/**Relevante Attribute für LoginService*/
	
	/** Das logininfo objekt. */
	private LoginInfo loginInfo = null;
	
	/** Das VerticalPanel login panel. */
	private VerticalPanel loginPanel = new VerticalPanel();
	
	/** Das login label. */
	private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the application.");
	
	/** Der signIn link. */
	private Anchor signInLink = new Anchor("Sign In");
	
	/** Der sign out link. */
	private Anchor signOutLink = new Anchor("Sign Out");
	
	
	/** Das Hauptpanel "mainpanel". */
	VerticalPanel mainPanel = new VerticalPanel ();
	
	/** Der Reportbutton panel. */
	HorizontalPanel reportbuttonPanel=new HorizontalPanel();
	
	/** Ein weiteres Panel für die Suche. */
	HorizontalPanel searchPanel=new HorizontalPanel();
	
	/** Das weitere Panel für die Beschreibung der Suchmöglichkeiten. */
	HorizontalPanel descriptionPanel = new HorizontalPanel();
	
	/** Das Panel für die Suche nach bestimmten Eigenschaften. */
	HorizontalPanel propertyPanel=new HorizontalPanel();
	
	/** Das Panel für die Ausprägungen und die geteilten Nutzer. */
	HorizontalPanel addPanel = new HorizontalPanel();
	
	/** Das user objekt. */
	private User user = null;
	
	/** Die asynchrone ReportGenerator Klasse. */
	private ReportGeneratorAsync reportGenerator=null;
	
	/** Die SuggestBox für die Email eingabe. */
	private TextSuggest sb=null;
	
	/** Das Label für die Beschreibung der Suchmöglichkeiten */

	Label searchheading = new Label("Hier können Sie Ihre Kontakte gefiltert nach Eigenschaft oder Ausprägung ausgeben.");
	
	/** Das Label für das Suchfeld. */
	Label searchLabel = new Label("Suche: ");
	
	/** Das Eingabefeld für die Eigenschaften, falls sonstiges ausgewählt wird. */
	TextBox propertyInput = new TextBox();
	
	/** Die Auswahlbox der vordefinierten Eigenschaften. */
	ListBox propertylistbox = new ListBox();
	/**
	 * Die notwendigen Buttons für den Navigationsteil 
	 **/
	
	/** Der Button, für den Report von allen Kontakten des angemeldeten Nutzers. */
	Button allContactsOfUserButton = new Button("Alle Kontakte eines Nutzers");
	
	/** Der Button, für den Report von allen geteilten Kontakten des Nutzers mit einem bestimmten anderen Kontakt. */
	Button allSharedContactsOfUserButton = new Button("Alle geteilten Kontakte eines Nutzers");
	
	/** Der Button, für den Report von allen Kontakten des Nutzers mit einer bestimmten Ausprägung. */
	Button allContactsWithValueButton = new Button("Kontakte mit bestimmter Ausprägung");
	
	/** Der Button, für den Report von allen Kontakten des Nutzers, mit einer bestimmten Eigenschaft. */
	Button allContactsWithPropertyButton = new Button("Kontakte mit bestimmter Eigenschaft");

	/**
	 * Die innere Klasse TextSuggest, dient dazu, dass man alle angemeldeten Nutzer 
	 * in einer SuggestBox vorgeschlagen bekommt.
	 */
	public class TextSuggest{

		/** Die Deklaration der SuggestBox. */
		private SuggestBox sb;
        
        /** Die Deklaration vom MultiWordSuggestOracle oracle. */
        private MultiWordSuggestOracle oracle;
        
        /**
         * Konstruktor für die TextSuggest Klasse.
         *
         * @param inputOracle the input oracle
         */
        public TextSuggest(MultiWordSuggestOracle inputOracle) {
    		
    		setOracle(inputOracle);
    						
    		reportGenerator.getAllUsers(new AsyncCallback<Vector<User>>() {
    			public void onFailure(Throwable arg0) {
    				Window.alert("Fehler beim holen aller User in der InputDialogBox");
    			}
    			@Override
    			public void onSuccess(Vector<User> arg0) {
    				
    				for(User loopUser : arg0) {
    					if (!loopUser.equals(user)) {
    						getOracle().add(loopUser.getEmail());
    					}
    				}
    				setSuggestBox(new SuggestBox(getOracle()));
    			
    			}
    		});
    	}		

		/**
		 * Getter der Suggest box.
		 *
		 * @return the suggest box
		 */
		public SuggestBox getSuggestBox() {
			return sb;
		}

		/**
		 * Setter der Suggest box.
		 *
		 * @param sb the new suggest box
		 */
		public void setSuggestBox(SuggestBox sb) {
			this.sb = sb;
		}

		/**
		 * Getter für die MultiWordSuggestOracle.
		 *
		 * @return the oracle
		 */
		public MultiWordSuggestOracle getOracle() {
			return oracle;
		}

		/**
		 * Setter für die MultiWordSuggestOracle.
		 *
		 * @param oracle the new oracle
		 */
		public void setOracle(MultiWordSuggestOracle oracle) {
			this.oracle = oracle;
		}
	}

			
	
	/**
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 * Die onModuleLoad() Methode. Hier werden alle Styles, der ChangeHandler 
	 * und der LoginService hinzugefügt.   
	**/
	public void onModuleLoad() {
		
		reportGenerator=ClientsideSettings.getReportGenerator();
		RootPanel.get("reporttext").setVisible(false);
		/**
		 * zusätzliche Klassennamen, die für das stylesheet benötigt werden.
		**/
		
		/**
		 * Instanziierung der SuggestBox für die Eingabe der Email.
		**/
		sb=new TextSuggest(new MultiWordSuggestOracle());
		
		searchheading.addStyleName("searchheading");
		signOutLink.addStyleName("signout");
		signInLink.addStyleName("reportbutton");
		sb.getSuggestBox().addStyleName("reportSuggestBox");

		searchLabel.addStyleName("searchlabel");
		reportbuttonPanel.addStyleName("top-buttons");
		allContactsOfUserButton.addStyleName("reportbutton");
		allSharedContactsOfUserButton.addStyleName("reportbutton");
		allContactsWithValueButton.addStyleName("reportbutton");
		allContactsWithPropertyButton.addStyleName("reportbutton");
		propertyInput.addStyleName("sonstigeinput");
		
		
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
				propertylistbox.addItem("Sonstiges");	
			}
		});
		
		/**
		 * Hinzufügen eines ChangeHandler, um eine weitere TextBox darzustellen, 
		 * falls die Eigenschaft Sonstiges ausgewählt wird.
		**/
		
		propertylistbox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
            	int size=propertylistbox.getItemCount();
            	int Item = propertylistbox.getSelectedIndex();
            	
            	if(Item==(size-1)){
            		propertyPanel.add(propertyInput);
                	}
        }});
		
		/**
		 * Hinzufügen des LoginServices
		**/
		
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
	 * Laden der Nutzerinformationen und das Speichern der Nutzerinformationen in einer Variable.
	 */
	
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
 	 * Laden der Anwendung. In dieser Methode werden alle ClickHandler der Buttons instanziiert. 
 	 * Außerdem wird das RootPanel befüllt.  
 	 */
 	public void loadApplication() { 
	  
	 		 /**
	 	 	 * Hinzufügen des SignOut Links. 
	 	 	 */
			
 			signOutLink.setHref(loginInfo.getLogoutUrl());
			
			/**
	 	 	 * Hinzufügen ClickHandlers für den AllContactsOfUser Button. Hier wird der Report mittels des
	 	 	 * HTML ReportWriters erstellt. 
	 	 	 */			
			
 			allContactsOfUserButton.addClickHandler(new ClickHandler() {
		         @Override
		         public void onClick(ClickEvent event){
		        	 reportGenerator.generateAllContactsOfUserReport(user, new AsyncCallback<AllContactsOfUserReport>() {
	    						 public void onFailure(Throwable caught) {
	   				 				    RootPanel.get("reporttext").setVisible(false);	
	   				 				    Label errornote=new Label("Es ist leider ein Fehler aufgetreten, versuchen Sie es später noch einmal.");
	   				 				    errornote.addStyleName("errornote");
	   				 				    mainPanel.add(errornote);
	    							 
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
		        	 
		    
 			/**
	 	 	 * Hinzufügen ClickHandlers für den allSharedContactsOfUserButton Button. Hier wird der Report mittels des
	 	 	 * HTML ReportWriters erstellt und der Inhalt der SuggestBox ausgelesen. 
	 	 	 */	
			
			allSharedContactsOfUserButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(sb.getSuggestBox().getText() != ""){
						User sharedUser=new User();
						sharedUser.setEmail(sb.getSuggestBox().getText());
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
				
			/**
	 	 	 * Hinzufügen ClickHandlers für den allContactsWithValueButton Button. Hier wird der Report mittels des
	 	 	 * HTML ReportWriters erstellt und der Inhalt der SuggestBox ausgelesen. 
	 	 	 */	
			
			allContactsWithValueButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(sb.getSuggestBox().getText() != ""){
					Value v = new Value();
					v.setContent(sb.getSuggestBox().getText());
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
			
			/**
	 	 	 * Hinzufügen ClickHandlers für den allContactsWithPropertyButton Button. Hier wird der Report mittels des
	 	 	 * HTML ReportWriters erstellt und die ausgewählte Eigenschaft aus der ListBox bzw. aus der TextBox ausgelesen. 
	 	 	 */	
			
			allContactsWithPropertyButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(propertylistbox.getSelectedItemText() != "Sonstiges"){
					Property p = new Property();
					p.setType(propertylistbox.getSelectedItemText());
					 reportGenerator.generateAllContactsWithPropertyReport(user, p, new AsyncCallback<AllContactsWithPropertyReport>() {
						 public void onFailure(Throwable caught) {
							
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
	   				if(propertyInput.getText() != ""){
	   				Property p = new Property();
					p.setType(propertyInput.getText());
					 reportGenerator.generateAllContactsWithPropertyReport(user, p, new AsyncCallback<AllContactsWithPropertyReport>() {
						 public void onFailure(Throwable caught) {
							
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
	   			}
			});
			
			/**
	 	 	 * Hinzufügen der einzelnen Panels zum Hauptpanel und anschließendes Hinzufügen des Hauptpanels zum RootPanel.
	 	 	 */	
			
			descriptionPanel.add(searchheading);
			addPanel.add(searchLabel);
			addPanel.add(sb.getSuggestBox());
			reportbuttonPanel.add(allContactsOfUserButton);
			addPanel.add(allSharedContactsOfUserButton);
			addPanel.add(allContactsWithValueButton);
			propertyPanel.add(propertylistbox);
			propertyPanel.add(allContactsWithPropertyButton);
			
			RootPanel.get("signout").add(signOutLink);
			
			RootPanel.get("report").add(reportbuttonPanel);
			mainPanel.add(searchPanel);
			mainPanel.add(descriptionPanel);
			mainPanel.add(addPanel);
			mainPanel.add(propertyPanel);
			RootPanel.get("report").add(mainPanel); 
		  }
	 	
	/**
	 * Load Login Methode, welches die LogIninfo aufruft und diese zum loginPanel hinzufügt. 
	 * Anschließend wird dieses zum RootPanel, damit dies auf der HTML Seite dargestellt wird.
	 */	
	private void loadLogin() {
		  
		    signInLink.setHref(loginInfo.getLoginUrl());
		    loginPanel.add(loginLabel);
		    loginPanel.add(signInLink);
		    RootPanel.get("loginRepo").add(loginPanel);
		  } 
}
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
 *  
 *  @author Anna-MariaGmeiner
 */

public class ITProjekt_SS18_Gr_10_Report implements EntryPoint {
	
	/** Die Instanzenvariablen, die mit dem Login-Service zusammenhängen. */
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
		
	/** Die Variablen, die die Panels instanziieren. */

	VerticalPanel mainPanel = new VerticalPanel ();
	VerticalPanel selectionPanel = new VerticalPanel ();
	HorizontalPanel selectionHPanel= new HorizontalPanel();
		
	HorizontalPanel reportbuttonPanel=new HorizontalPanel();
	HorizontalPanel searchPanel=new HorizontalPanel();
	HorizontalPanel descriptionPanel = new HorizontalPanel();
	HorizontalPanel propertyPanel=new HorizontalPanel();
	HorizontalPanel valuePanel=new HorizontalPanel();
	HorizontalPanel participantPanel = new HorizontalPanel();
	
	/** Die Instanzenvariablen, die für das Generieren der Reports benötigt werden. */
	private User user = null;
	private ReportGeneratorAsync reportGenerator=null;
	private TextSuggest sb=null;
	
	/** Die notwendigen Labels, Buttons und Drop-Down Menüs für den Navigationsteil. */
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

	
			
	/**
	 * Die Methode onModuelLoad() wird beim Starten der Anwendung aufgerufen.
	 */
	public void onModuleLoad() {
		
		/*
		 * Login Status des Benutzers wird geprüft. 
		 */
		signInLink.addStyleName("reportbutton");
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login("https://it-projekt-gruppe-10-203610.appspot.com/ITProjekt_SS18_Gr_10_Report.html", new AsyncCallback<LoginInfo>() {
	    	public void onFailure(Throwable error) {
	    	}
	    	public void onSuccess(LoginInfo result) {
	    		loginInfo = result;
	    		if(loginInfo.isLoggedIn()) {
	    			//ist der Benutzer mit seinem Google Account im Browser eingeloggt, wird die Methode loadUserInformatino() aufgerufen
	    			loadUserInformation();
	    		} else {
	    			//ist der Benutzer nicht eingeloggt, so wird er auf die LoginSeite weitergeleitet 
	    			loadLogin();
	    		}
	    	}
	    });
	  }
	
		
	/**
	 * Die Methode loadUserInformation wird aufgerufen, wenn der Nutzer mit seinem Google Account bereits im Browser eingeloggt ist.
	 * Sie prüft, ob der Nutzer bereits dem System bekannt ist.
	 * Wenn ja, wird die Applikation geladen.
	 * Wenn nein, erscheint eine Maske, in der sich der Benutzer bei erstmaligem Aufruf der Applikation registrieren kann. 
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
				//Der Nutzer konnte in der Datenbank gefunden werden und ist somit bereits bestehender Nutzer der Applikation
				ClientsideSettings.setUser(result);
				user = result;
				
				//eine Instanz der inneren Klasse TextSuggest (s.u.) wird erstellt 
				sb=new TextSuggest(new MultiWordSuggestOracle());
			}
			   		
	    });
  	}
	
	
	/**
	 * Die Methode loadApplication beinhaltet das Laden der Applikation.
	**/
	 public void loadApplication() { 

			signOutLink.setHref(loginInfo.getLogoutUrl());
								

			//Hinzufügen von StyleNames		
			searchheading.addStyleName("searchheading");
			signOutLink.addStyleName("signout");					
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
			
			/**
			 * Hinzufügen eines ChangeHandlers zur propertylistbox, damit das zusätzliche Eingabefeld erscheint; 
			 * falls nach einer eigen erstellten Eigenschaftsart gesucht werden möchte.
			***/
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
	           
			//Befüllen der ListBox "reportlistbox" mit dden verschiedenen Report-Möglichkeiten.
	        reportlistbox.addItem("Alle meine Kontakte");
	        reportlistbox.addItem("Alle mit einem Nutzer geteilten Kontakte");
	        reportlistbox.addItem("Kontakte mit bestimmter Ausprägung");
	        reportlistbox.addItem("Kontakte mit bestimmter Eigenschaft");

			
			
			
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
					
			selectionHPanel.add(reportlistbox);
			selectionHPanel.add(getReportButton);
			selectionPanel.add(descriptionPanel);
			selectionPanel.add(selectionHPanel);
			
			mainPanel.add(selectionPanel);
			
		 	RootPanel.get("reporttext").setVisible(false);
			RootPanel.get("signout").add(signOutLink);
			RootPanel.get("report").add(mainPanel);
			

			
			
			/**
			 * Hinzufügen eines Clickhandlers, um den passenden Report auszuwählen. Dabei werden je nach ausgewähltem Report 
			 * die zusätzlichen Eingabefelder zum Panel hinzugefügt.
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
	    							 	/**
	    								 * Der Report von allen Kontakten konnte erstellt werden und wird nun mittels des
	    								 * HTMLReportWriters in HTML Format umgewandelt .
	    								**/    							 
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
						// Auslesen der SuggestBox und Formatierung des Strings, sodass er lediglich die Email beinhaltet.
						String[] split = sb.getSuggestBox().getText().split(" - ");
						String userEmail = split[1].substring(0, split[1].length());
						// Erstellen eines User Objektes und setzen der Email durch den eingegebenen Nutzer.
						User sharedUser=new User();
						sharedUser.setEmail(userEmail);
						reportGenerator.generateAllSharedContactsOfUserReport(user, sharedUser, new AsyncCallback<AllSharedContactsOfUserReport>() {
				 				    	public void onFailure(Throwable caught) {
		   				 				    RootPanel.get("reporttext").setVisible(false);	
		   				 				    Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");

			   						 }
			   						 public void onSuccess(AllSharedContactsOfUserReport result) {
			   						 	/**
		    								 * Der Report von allen Kontakten konnte erstellt werden und wird nun mittels des
		    								 * HTMLReportWriters in HTML Format umgewandelt .
	    								**/  
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
							/**
							 *  Die Suchleiste ist leer. Deshalb wird dem User mitgeteilt, 
							 *  dass er zuerst einen Suchbegriff eingeben muss, um den Report zu generieren
							**/ 
							final ClientsideFunctions.popUpBox emptyTextbox = new ClientsideFunctions.popUpBox("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.", new ClientsideFunctions.OkButton());
							emptyTextbox.getOkButton().addCloseDBClickHandler(emptyTextbox);
							}
	   				 	}
					});
		    
		    
		    /**
			 * Hinzufügen eines Clickhandlers um den Report für "alle Kontakte mit einer bestimmten Ausprägung" zu generieren.
			 * Dabei wird das zugehörige Eingabefeld ausgelesen, um anschließend den Report zu generieren.
			**/  
			allContactsWithValueButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(valueInput.getText() != ""){
					// Erstellen eines Value Objektes und setzen des Inhalts durch die eingegebene Ausprägung.
					Value v = new Value();
					v.setContent(valueInput.getText());
					 reportGenerator.generateAllContactsWithValueReport(user, v, new AsyncCallback<AllContactsWithValueReport>() {
						 public void onFailure(Throwable caught) {
							RootPanel.get("reporttext").setVisible(false);	
				 			Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");
						 }
						 public void onSuccess(AllContactsWithValueReport result) {
							 	/**
								 * Der Report von allen Kontakten konnte erstellt werden und wird nun mittels des
								 * HTMLReportWriters in HTML Format umgewandelt .
								**/  
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
					/**
					 *  Die Suchleiste ist leer. Deshalb wird dem User mitgeteilt, 
					 *  dass er zuerst einen Suchbegriff eingeben muss, um den Report zu generieren
					**/ 
					final ClientsideFunctions.popUpBox emptyTextbox = new ClientsideFunctions.popUpBox("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.", new ClientsideFunctions.OkButton());
					emptyTextbox.getOkButton().addCloseDBClickHandler(emptyTextbox);
				}}});
			
			
			/**
			 * Hinzufügen eines Clickhandlers um den Report für "alle Kontakte mit einer bestimmten Eigenschaft" zu generieren.
			**/  
			
			allContactsWithPropertyButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
				 	/**
					 * Die gewählte Eigenschaftsart wird abgefragt und ist in diesem Fall 
					 * eine bereits vordefinierte Eigenschaft. Deshalb wird hier der ausgewählte Text der Listbox ausgelesen, um den zugehörigen Report zu generieren. 
					**/  
					if(!propertylistbox.getSelectedItemText().equals("eigene Eigenschaftsart")){
						// Erstellen eines Property Objektes und setzen des Typs durch die ausgewählte Eigenschaft.
						Property p = new Property();
					p.setType(propertylistbox.getSelectedItemText());
					 reportGenerator.generateAllContactsWithPropertyReport(user, p, new AsyncCallback<AllContactsWithPropertyReport>() {
						 public void onFailure(Throwable caught) {
							  RootPanel.get("reporttext").setVisible(false);	
				 			  Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");
						 }
						 public void onSuccess(AllContactsWithPropertyReport result) {
							 	/**
								 * Der Report von allen Kontakten konnte erstellt werden und wird nun mittels des
								 * HTMLReportWriters in HTML Format umgewandelt .
								**/  	
							 if (result !=null) {
				 				    RootPanel.get("reporttext").setVisible(true);
				 				    HTMLReportWriter writer=new HTMLReportWriter();
								 	writer.process(result);
									RootPanel.get("reporttext").clear();
									RootPanel.get("reporttext").add(new HTML(writer.getReportText()));
									}
						 }
					 });
	   			}/**
					 * Es wurde eine eigen erstellte Eigenschaft abgefragt, weshalb hier nicht der Inhalt der ListBox ausgelesen wird, 
					 * sondern das zugehörige Eingabefeld. Deshalb wird der Report mit dem eingegebenen Text generiert und nicht . 
					**/ 
					else{
	   				if(propertyInput.getText() != ""){
						// Erstellen eines Property Objektes und setzen des Typs durch die eingegebene Eigenschaft.
	   				Property p = new Property();
					p.setType(propertyInput.getText());
					reportGenerator.generateAllContactsWithPropertyReport(user, p, new AsyncCallback<AllContactsWithPropertyReport>() {
						 public void onFailure(Throwable caught) {
							 RootPanel.get("reporttext").setVisible(false);
							 Window.alert("Es ist leider ein Fehler aufgetreten. Der Report konnte nicht erstellt werden.");
						 }
						 public void onSuccess(AllContactsWithPropertyReport result) {
							 	/**
								 * Der Report von allen Kontakten konnte erstellt werden und wird nun mittels des
								 * HTMLReportWriters in HTML Format umgewandelt .
								**/  	
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
	   				/**
					 *  Die Suchleiste ist leer. Deshalb wird dem User mitgeteilt, 
					 *  dass er zuerst einen Suchbegriff eingeben muss, um den Report zu generieren
					**/ 
	   				final ClientsideFunctions.popUpBox emptyTextbox = new ClientsideFunctions.popUpBox("Suchleiste ist leer. Bitte füllen Sie einen Suchbegriff in das Suchfeld ein.", new ClientsideFunctions.OkButton());
					emptyTextbox.getOkButton().addCloseDBClickHandler(emptyTextbox);
	   				}
				}}
			});
	   			
		}  
	 
		/**
		 *  Erstellen einer SuggestBox, welche alle Nutzer enthält.
		**/ 
	 public class TextSuggest{
			/**
			 *  Deklaration der benötigten Variablen
			**/ 
			private SuggestBox sb;
	        private MultiWordSuggestOracle oracle;
	        public TextSuggest(MultiWordSuggestOracle inputOracle) {
	    		
	    		setOracle(inputOracle);
	    		/**
				 *  Auslesen der User Suggestions und Hinzufügen zum Oracle Objekt.
				**/ 				
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
	    				loadApplication();
	    			}
	    		});
	    	}		
	        /**
			 *  Auslesen der SuggestBox.
			**/ 	
			public SuggestBox getSuggestBox() {
				return sb;
			}
			/**
			 *  Setzen der SuggestBox.
			**/ 	
			public void setSuggestBox(SuggestBox sb) {
				this.sb = sb;
			}
			/**
			 *  Auslesen des Oracles.
			**/ 	
			public MultiWordSuggestOracle getOracle() {
				return oracle;
			}
			/**
			 *  Setzen des Oracles.
			**/ 	
			public void setOracle(MultiWordSuggestOracle oracle) {
				this.oracle = oracle;
			}
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
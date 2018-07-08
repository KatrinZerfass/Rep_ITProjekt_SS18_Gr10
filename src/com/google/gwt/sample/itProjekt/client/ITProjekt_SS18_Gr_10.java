package com.google.gwt.sample.itProjekt.client;


import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.sample.itProjekt.shared.LoginServiceAsync;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die Entry-Point-Klasse für den Editor. Sie erbt vom Interface "EntryPoint" und implementiert die Methode "onModuleLoad()"
 * 
 * @author KatrinZerfass & JoshuaHill & JanNoller
 * 
 */
public class ITProjekt_SS18_Gr_10 implements EntryPoint {
	
	/**
	 * Diese Nachricht wird angezeigt, wenn der Client keine Verbindung zum Server aufbauen 
	 * kann.  
	 */
	
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	
	/** Die Instanzenvariablen, die mit dem Login-Service zusammenhängen. */
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
	      "Bitte loggen Sie sich mit ihrem Google-Account ein, um die Anwendung zu nutzen.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private Label signedInUser = null;
	
	
	User user = null;
	EditorAdministrationAsync editorAdministration = null;
	
	/**Instanzen der anderen GUI-Klassen, welche in der Entry-Point-Klasse erstellt und verknüpft werden*/
	ContactForm cf = null;
	ContactListForm clf = null;
	ContactListContactTreeViewModel clctvm = new ContactListContactTreeViewModel();
	
	/** Die Default-Kontaktliste MyContactsContactList mccl. */
	ContactList mccl = new ContactList();
	
	/** Die DialogBox, die bei erstmaliger Registrierung des Nutzers erscheint */
	ClientsideFunctions.InputDialogBox createAccountBox = null;


	
	
	  
	/**
	 * Die Methode onModuelLoad() wird beim Starten der Anwendung aufgerufen.
	 */
	public void onModuleLoad() {
		  
		  
		 /* 
		  * Login Status des Benutzers wird geprüft. 
		  */
	    signInLink.addStyleName("signin");
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login("https://it-projekt-gruppe-10-203610.appspot.com/ITProjekt_SS18_Gr_10.html", new AsyncCallback<LoginInfo>() {
		    public void onFailure(Throwable error) {
		    }
	
		    public void onSuccess(LoginInfo result) {
		    	loginInfo = result;
		    	if(loginInfo.isLoggedIn()) {
		    		//ist der Benutzer mit seinem Google Account im Browser eingeloggt, wird die Methode loadUserInformatino() aufgerufen
		    		loadUserInformation();
		    		
		    	}
		    	else {
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
	    	
		if(editorAdministration == null) {
			editorAdministration = ClientsideSettings.getEditorAdministration();
	    }

	    
	    editorAdministration.isUserKnown(loginInfo.getEmailAddress(), new AsyncCallback<Boolean>() {
			
	    	public void onFailure(Throwable caught) {
	    		Window.alert("AsyncCallback fehlgeschlagen: isUserKnown");			
			}

			public void onSuccess(Boolean result) {
				if (result) {
					//Der Nutzer konnte in der Datenbank gefunden werden und ist somit bereits bestehender Nutzer der Applikation
					editorAdministration.getUserByEmail(loginInfo.getEmailAddress(), new AsyncCallback<User>() {
						public void onFailure(Throwable arg0) {
							Window.alert("AsyncCallback fehlgeschlagen: getUser");
						}
						public void onSuccess(User arg0) {
							//das zurückkommende Nutzer-Objekt wird in den ClientsideSettings hinterlegt und in einer Instanzenvariable gespeichert.
							ClientsideSettings.setUser(arg0);
							user = arg0;
							//da der Nutzer bereits bekannt ist, wird für ihn im Folgenden die Applikation geladen
							loadApplication();
						}
					});
				}
				
				else {
					/*Wenn kein Nutzer mit dieser e-Mail in der Datenbank gefunden wurde, wird die DialogBox für die erstmalige Registrierung 
					 * aufgebaut. In diese muss der Nutzer seinen Vor- und Nachnamen eintragen und sein Geschlecht auswählen. */
					createAccountBox = new ClientsideFunctions.InputDialogBox(loginInfo.getEmailAddress());
					
					createAccountBox.getOKButton().addClickHandler(new ClickHandler() {
						
						public void onClick(ClickEvent arg0) {
							if(ClientsideFunctions.checkName(createAccountBox.getMultiUseTextBox().getText()) && ClientsideFunctions.checkName(createAccountBox.getNameTextBox().getText())) {
								//wenn für Vor- und Nachname gültige Werte eingetragen wurden, wird ein Kontakt-Objekt erstellt, welches den Nutzer verkörpert
								editorAdministration.createUserContact(createAccountBox.getMultiUseTextBox().getText(), createAccountBox.getNameTextBox().getText(), createAccountBox.getListBox().getSelectedItemText(), loginInfo.getEmailAddress(), new AsyncCallback<User>() {
									public void onFailure(Throwable arg0) {
										Window.alert("AsyncCallback fehlgeschlagen: createContact");
										createAccountBox.hide();
									}
									public void onSuccess(User arg0) {
										if(arg0 == null) {
											Window.alert("arg0 = null");
										}
										//nach erfolgreichen Anlegen des Nutzer-Kontakts wird der neue Nutzer im System willkommen geheißen
										final ClientsideFunctions.popUpBox welcome = new ClientsideFunctions.popUpBox("Herzlich Willkommen!", new ClientsideFunctions.OkButton());
										welcome.getOkButton().addCloseDBClickHandler(welcome);
										createAccountBox.hide();
										//das zurückkommende Nutzer-Objekt wird in den ClientsideSettings hinterlegt und in einer Instanzenvariable gespeichert.
										ClientsideSettings.setUser(arg0);
										user = arg0;
										//danach wird für den neu registrierten Nutzer ebenfalls die Applikation geladen
										loadApplication();
									}
								});
							}
							else {
								createAccountBox.hide();
							}
						}
					});
				}
			}
	    });
  	}
  	
  	
		
  	 /**
     * Die Methode loadApplication() wird aufgerufen, wenn der Benutzer eingeloggt und dem System als Nutzer-Objekt bekannt ist. 
     * Sie beinhaltet die eigentliche Applikation, welche im Folgenden aufgebaut wird.  
     */
  	
  	public void loadApplication(){
  		 		
		/*
		 * Das loginPanel wird aufgebaut
		 */
	    VerticalPanel loginPanel = new VerticalPanel();
	    
	    /*
	     * Der signOutLink wird dem loginPanel hinzugefügt
	     */
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    signOutLink.addStyleName("signout");
		signInLink.addStyleName("reportbutton");
		
		
		loginPanel.add(signOutLink);
		
		
		/*
		 * Die Information über den aktuell angemeldeten Nutzer wird ebenfalls dem loginPanel hinzugefügt
		 */
	    signedInUser = new Label();
	    signedInUser.addStyleName("signedInUser");
	    	    
	    editorAdministration.getFullNameOfUser(user, new AsyncCallback<String>(){
	    	public void onFailure(Throwable t) {
	    		
	    	}
	    	public void onSuccess(String result) {
	    		
	    		signedInUser.setText("Angemeldet als: " +result);
	    	}
	    });
	    
		loginPanel.add(signedInUser);
		
		
		//das loginPanel wird dem div mit der id "Login" hinzugefügt
		RootPanel.get("Login").add(loginPanel);
		
		
		
		/*
		 * Instanzen von ContactForm und ContactListForm werden erstellt und je einem eigenen div hinzugefügt
		 */
		cf = new ContactForm();
		RootPanel.get("ContactForm").add(cf);
		
		clf = new ContactListForm();
		RootPanel.get("Contactlist").add(clf);
	
		
		/*
		 * buttonsPanel und newPropertyPanel werden erstellt und je einem eigenen div hinzugefügt
		 */
		VerticalPanel buttonsPanel = cf.getButtonsPanel();
		RootPanel.get("ButtonsPanel").add(buttonsPanel);
		
		HorizontalPanel newPropertyPanel = cf.getNewPropertyPanel();
		RootPanel.get("PropertyPanel").add(newPropertyPanel);		
			
		
		/*
		 * TreeViewModel, ContactForm und ContactListForm werden miteinander verbunden.
		 */
		clctvm.setContactForm(cf);
		cf.setClctvm(clctvm);
		clf.setClctvm(clctvm);
		
		
		/*
		 * Die default-Kontaktliste "Meine Kontakte" wird erstellt und dem TreeVieModel hinzugefügt.
		 */
		mccl.setName("Meine Kontakte");
		mccl.setOwner(user.getId());
		mccl.setId(1000);
		mccl.setMyContactsFlag(true);
		
		clctvm.setMyContactsContactList(mccl);
	

		/*
		 * Eine Instanz eines CellBrowsers wird erstellt und dem div "Navigator" hinzugefügt
		 */
		CellBrowser.Builder<String> builder = new CellBrowser.Builder<>(clctvm, "Root");	
		CellBrowser cellBrowser = builder.build(); 
		cellBrowser.setHeight("100%");
		cellBrowser.setWidth("100%");
		cellBrowser.setAnimationEnabled(true);
		cellBrowser.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);		
		
		RootPanel.get("Navigator").add(cellBrowser);
		//beim Aufruf der Applikation wird die Default-Liste "Meine Kontakte" automatisch geöffnet
		cellBrowser.getRootTreeNode().setChildOpen(0, true);

	  }
	  
	 
  	
	/**
  	 * Die Methode loadLogin() wird aufgerufen wenn der Benutzer nicht eingeloggt ist. 
  	 */
  	private void loadLogin() {
		  
		signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get("Login").add(loginPanel);
	}	
	
}	 

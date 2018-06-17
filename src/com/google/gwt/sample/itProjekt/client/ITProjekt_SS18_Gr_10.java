package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.sample.itProjekt.client.ContactForm.EmailDialogBox;
import com.google.gwt.sample.itProjekt.shared.CommonSettings;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.sample.itProjekt.shared.LoginServiceAsync;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.User;

import java.util.Vector;

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
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die Entry-point-Klasse für den Editor
 */
public class ITProjekt_SS18_Gr_10 implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	EditorAdministrationAsync editorAdministration = null;
	
	/* Die Instanzenvariablen, die mit dem Login-Service zusammenhängen. */
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	/** Das referenzierte ContactListContactTreeViewModel-Objekt */
	ContactListContactTreeViewModel clctvm = new ContactListContactTreeViewModel();
	
	/** Die Default-Kontaktliste MyContactsContactList mccl. */
	ContactList mccl = new ContactList();


	
	
	/**
	 * Die innere Klasse InputDialogBox.
	 * ?? was macht sie ??
	 * 
	 * @author JanNoller
	 */
	public class InputDialogBox extends DialogBox{
		
		private String input;
		
		Label label;
		
        private TextBox tb = new TextBox();
		
		/**
		 * Der Konstruktor von InputDialogBox
		 * ?? was macht er ??
		 */
		public InputDialogBox() {
			setText("Eingabe");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
			Button ok = new Button("OK");
	        ok.addClickHandler(new ClickHandler() {
	        	public void onClick(ClickEvent event) {
	        		input = tb.getText();
	        		
	            	InputDialogBox.this.hide();
	            }
	        });
			
			VerticalPanel panel = new VerticalPanel();
			
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(label);
	        panel.add(tb);
	        panel.add(ok);

	        setWidget(panel);
		}
		
		/**
		 * Getter von input.
		 *
		 * @return den Input
		 */
		public String getInput() {
			return this.input;
		}
		
		/**
		 * Setter von input.
		 *
		 * @param input der Input
		 */
		public void setInput(String input) {
			this.input = input;
		}
		
		/**
		 * Setter des Labels.
		 *
		 * @param labelText der Text des Labels.
		 */
		public void setLabel (String labelText) {
			this.label = new Label(labelText);
		}
		
		/**
		 * Gets the label.
		 *
		 * @return the label
		 */
		public Label getlabel () {
			return this.label;
		}
	}

	  
	/**
	 * Die Methode onModuelLoad() wird beim Starten der Anwendung aufgerufen
	 */
	public void onModuleLoad() {
		  
		  
		 /* 
		  * Login Status des Benutzers wird geprüft. (Wird erst zu einem späteren Zeitpunkt implementiert) 
		  */
	    
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	    public void onFailure(Throwable error) {
	    }

	    public void onSuccess(LoginInfo result) {
	    	loginInfo = result;
	    	if(loginInfo.isLoggedIn()) {
	    		loadApplication();
	    	}
	    	else {
	    		loadLogin();
	    	}
	    }
	    });
	}
	  
	  
	  
  /*
   * Die Methode loadApplication() wird aufgerufen, wenn der Benutzer eingeloggt ist. Sie beinhaltet 
   * die eigentliche Applikation.  
   */
  	public void loadApplication() {
	    	
		if(editorAdministration == null) {
			editorAdministration = ClientsideSettings.getEditorAdministration();
	    }
	    
	    
	    // Anlegen des User Objekts 
	    
	    editorAdministration.getUserInformation(loginInfo.getEmailAddress(), new AsyncCallback<User>() {
			
	    	public void onFailure(Throwable caught) {
				editorAdministration.createUser(loginInfo.getEmailAddress(), new AsyncCallback<User>() {
    				public void onFailure(Throwable caught) {
					}
    				public void onSuccess(User result) {
    					editorAdministration.setUser(result, new AsyncCallback<Void>() {
    						public void onFailure(Throwable caught) {
    						}
    						public void onSuccess(Void result) {
    						}
    			 
				        });
			        }
    		    });
				
			}

			
			public void onSuccess(User result) {
				editorAdministration.setUser(result, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
					}
					public void onSuccess(Void result) {
					}
			
		        });
			}    		
	    });
	    
		
		
		
		/*
		 * Im Folgenden wird das GUI aufgebaut
		 */
	    
	    signOutLink.setHref(loginInfo.getLogoutUrl());
		RootPanel.get("Login").add(signOutLink);
		
		/*
		 * Das Div "ContactForm" beinhaltet eine Instanz von ContactForm
		 */
		ContactForm cf = new ContactForm();
		VerticalPanel contactPanel = new VerticalPanel();
		contactPanel.add(cf);
		RootPanel.get("ContactForm").add(contactPanel);	
		
		
		/*
		 * Das Div "Contactlist" links unter der Navigation beinhaltet die Buttons für Kontaktlisten und das Suchfeld.
		 */
		HorizontalPanel clButtonsAndSearchPanel = new HorizontalPanel();
		clButtonsAndSearchPanel.setWidth("100%");
				
		//Die Buttons für Kontaktlisten
		VerticalPanel contactListButtonsPanel = new VerticalPanel();
		contactListButtonsPanel.setStyleName("buttonPanel");
		
		Button newContactListButton = new Button("Neue Kontaktliste anlegen");
		Button deleteContactListButton = new Button("Kontaktliste löschen");
		Button shareContactListButton = new Button("Kontaktliste teilen");
		
		contactListButtonsPanel.add(shareContactListButton);
		contactListButtonsPanel.add(deleteContactListButton);
		contactListButtonsPanel.add(newContactListButton);
		
		newContactListButton.addClickHandler(new NewContactListClickHandler());
		deleteContactListButton.addClickHandler(new DeleteContactListClickHandler());
		shareContactListButton.addClickHandler(new ShareContactListClickHandler());
		
		clButtonsAndSearchPanel.add(contactListButtonsPanel);
	
		
		//Das Suchfeld
		VerticalPanel searchPanel = new VerticalPanel();
		
		Label searchLabel = new Label();
		searchLabel.setText("Durchsuchen Sie Ihre Kontaktlisten nach bestimmten Ausprägungen: ");
		searchLabel.setWidth("240px");
		searchPanel.add(searchLabel);
		
		HorizontalPanel searchBox = new HorizontalPanel();
		TextBox searchTextBox = new TextBox();
		PushButton searchButton = new PushButton();
		Image searchButtonImg = new Image("searchButton.png");
		searchButtonImg.setPixelSize(17, 17);
		searchButton.getUpFace().setImage(searchButtonImg);
		
		searchBox.add(searchTextBox);
		searchBox.add(searchButton);
		
		searchPanel.add(searchBox);
		
		clButtonsAndSearchPanel.add(searchPanel);
		clButtonsAndSearchPanel.setCellHorizontalAlignment(searchPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		RootPanel.get("Contactlist").add(clButtonsAndSearchPanel);
		
		
		/*
		 * TreeViewModel und ContactForm werden miteinander verbunden.
		 */
		clctvm.setContactForm(cf);
		cf.setClctvm(clctvm);
		
		/*
		 * Die default-Kontaktliste "Meine Kontakte" wird erstellt.
		 */
		mccl.setName("Meine Kontakte");
//		mccl.setOwner();
//		mccl.setId();
		clctvm.setMyContactsContactList(mccl);

		/*
		 * Das div "Navigator" beinhaltet eine Instanz eines CellBrowswers
		 */
		CellBrowser.Builder<String> builder = new CellBrowser.Builder<>(clctvm, "Root");	
		CellBrowser cellBrowser = builder.build(); 
		cellBrowser.setHeight("100%");
		cellBrowser.setWidth("100%");
		cellBrowser.setAnimationEnabled(true);
		cellBrowser.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		Window.alert("3. created cellbrowser");
		
		RootPanel.get("Navigator").add(cellBrowser);
		
		Window.alert("6. finished loadApplication");
		  
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
	 
  	
	/**
	 * Die innere Klasse NewContactListClickHandler. 
	 * Eine Instanz von ihr wird beim Klick auf den newContactListButton aufgerufen.
	 * 
	 * @author JanNoller
	 */
	private class NewContactListClickHandler implements ClickHandler {
		
		InputDialogBox inputDB;
		
		
		public void onClick(ClickEvent event) {
			inputDB = new InputDialogBox();
			inputDB.show();
			inputDB.setLabel("Bitte geben Sie den Namen der neuen Kontaktliste an.");
			editorAdministration.createContactList(inputDB.getInput(), new AsyncCallback<ContactList>() {
				public void onFailure(Throwable arg0) {
					Window.alert("Fehler beim Erstellen der Kontaktliste!");
				}
				public void onSuccess(ContactList arg0) {
					Window.alert("Kontaktliste erfolgreich erstellt.");
					clctvm.addContactList(arg0);
				}
			});
		}
	}
	
	/**
	 * Die innere Klasse DeleteContactListClickHandler. 
	 * Eine Instanz von ihr wird beim Klick auf den deleteContactListButton aufgerufen.
	 * 
	 * @author JanNoller
	 */
	private class DeleteContactListClickHandler implements ClickHandler {

		
		public void onClick(ClickEvent event) {
			editorAdministration.deleteContactList(clctvm.getSelectedContactList(), new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable arg0) {
					Window.alert("Fehler beim löschen der Kontaktliste!");
				}
				@Override
				public void onSuccess(Void arg0) {
					Window.alert("Kontaktliste erfolgreich gelöscht.");	
				}
			});
		}
	}
	
	/**
	 * Die innere Klasse ShareContactListClickHandler. 
	 * Eine Instanz von ihr wird beim Klick auf den ShareContactListButton aufgerufen.
	 * 
	 * @author JanNoller
	 */
	private class ShareContactListClickHandler implements ClickHandler {
	
		InputDialogBox inputDB;
		
		public void onClick(ClickEvent event) {
			inputDB = new InputDialogBox();
			inputDB.setLabel("Bitte geben Sie die Email-Adresse des Nutzers ein mit dem Sie die Kontaktliste teilen möchten.");
			inputDB.show();
			
			editorAdministration.shareContactList(clctvm.getSelectedContactList(), inputDB.getInput(), new AsyncCallback<Permission>() {
				@Override
				public void onFailure(Throwable arg0) {
					Window.alert("Fehler beim Teilen der Kontaktliste!");
				}
				@Override
				public void onSuccess(Permission arg0) {
					Window.alert("Kontaktliste erfolgreich geteilt.");
				}
			});
		}
	}
}	 

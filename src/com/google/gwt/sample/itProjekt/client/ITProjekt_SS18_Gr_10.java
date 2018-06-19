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
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ITProjekt_SS18_Gr_10 implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	User user = null;
	
	ContactListContactTreeViewModel clctvm = new ContactListContactTreeViewModel();

	EditorAdministrationAsync editorAdministration = null;
	
	
	public class InputDialogBox extends DialogBox{
		
		private String input;
		
		Label label;
		
        private TextBox eingabe = new TextBox();
		
		public InputDialogBox() {
			setText("Eingabe");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
			Button ok = new Button("OK");
	        ok.addClickHandler(new ClickHandler() {
	        	public void onClick(ClickEvent event) {
	        		input = eingabe.getText();
	        		
	            	InputDialogBox.this.hide();
	            }
	        });
			
			VerticalPanel panel = new VerticalPanel();
			
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(label);
	        panel.add(eingabe);
	        panel.add(ok);

	        setWidget(panel);
		}
		
		public String getInput() {
			return this.input;
		}
		
		public void setInput(String input) {
			this.input = input;
		}
		
		public void setLabel (String labelText) {
			this.label = new Label(labelText);
		}
		
		public Label getlabel () {
			return this.label;
		}
	}

	  
	public void onModuleLoad() {
		  
		  
		 /* 
		  * Login Status des Benutzers wird geprüft. 
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
	   * Diese Methode wird aufgerufen wenn der Benutzer eingeloggt ist. Sie beinhaltet 
	   * die eigentliche Applikation.  
	   */
	  
	public void loadApplication() {
	    	
		if(editorAdministration == null) {
			editorAdministration = ClientsideSettings.getEditorAdministration();
	    }
	    
	    
	    //Anlegen des User Objekts 
	    
	    editorAdministration.getUserInformation(loginInfo.getEmailAddress(), new AsyncCallback<User>() {
			
	    	public void onFailure(Throwable caught) {
	    		Window.alert("AsyncCallback fehlgeschlagen");			
			}

			public void onSuccess(User result) {
				
				Window.alert("User Objekt wurde übergeben");
				user = result;
				
			}
			   		
	    });

	    		   		    		    			
	      
		signOutLink.setHref(loginInfo.getLogoutUrl());
		RootPanel.get("Login").add(signOutLink);
		
		ContactForm cf = new ContactForm();
		VerticalPanel contactPanel = new VerticalPanel();
		contactPanel.add(cf);
		RootPanel.get("ContactForm").add(contactPanel);	
		
		HorizontalPanel clButtonsAndSearchPanel = new HorizontalPanel();
		
		VerticalPanel contactListButtonsPanel = new VerticalPanel();
		contactListButtonsPanel.setStyleName("buttonPanel");
		
		Button newContactList = new Button("Neue Kontaktliste anlegen");
		Button deleteContactList = new Button("Kontaktliste löschen");
		Button shareContactList = new Button("Kontaktliste teilen");
		
		newContactList.addClickHandler(new newContactListClickHandler());
		deleteContactList.addClickHandler(new deleteContactListClickHandler());
		shareContactList.addClickHandler(new shareContactListClickHandler());
		
		contactListButtonsPanel.add(shareContactList);
		contactListButtonsPanel.add(deleteContactList);
		contactListButtonsPanel.add(newContactList);
		
		clButtonsAndSearchPanel.add(contactListButtonsPanel);
		
		
		RootPanel.get("Contactlist").add(clButtonsAndSearchPanel);
		
		VerticalPanel searchPanel = new VerticalPanel();
		
		
		Label searchLabel = new Label();
		searchLabel.setText("Suchfeld für Kontakte: ");
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
		
		
		clctvm.setContactForm(cf);
		cf.setClctvm(clctvm);
		
		//test Kontakt
		Contact testContact = new Contact();
		testContact.setFirstname("Hans");
		testContact.setLastname("Müller");
		testContact.setId(0001);
		testContact.setSex("w");
		
		
		//test Kontaktliste
		ContactList testContactList = new ContactList();
		testContactList.setId(1000);
		testContactList.setName("Meine Freunde");
		
		
		clctvm.setSelectedContactList(testContactList);
		// Check: "2. Meine Freunde als selectedContactList des clctvm
		
		
	
		
		CellBrowser.Builder<String> builder = new CellBrowser.Builder<>(clctvm, "Root");	
		CellBrowser cellBrowser = builder.build(); 
		cellBrowser.setHeight("100%");
		cellBrowser.setWidth("100%");
		cellBrowser.setAnimationEnabled(true);
		cellBrowser.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		Window.alert("3. created cellbrowser");
		
		clctvm.addContactList(testContactList);
		// "4. Meine Freunde im contactListDataProvider
		
		clctvm.addContactOfContactList(testContactList, testContact);
		// "5. contactDataProvider enthält nicht die CL als Key 
		
		
		//braucht man nicht mehr
	//	clctvm.setSelectedContact(testContact);
		//Check: Einfügen von Hans Müller
			
		
		
		
	
		
		RootPanel.get("Navigator").add(cellBrowser);
		RootPanel.get("login").add(signOutLink);
		Window.alert("6. finished onModuleLoad");
		  
	  }
	  
	  /* 
	   * Das Login Panel wird aufgerufen wenn der Benutzer nicht eingeloggt ist. 
	   */

	private void loadLogin() {
		  
		signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get("Login").add(loginPanel);
	}
	  
	private class newContactListClickHandler implements ClickHandler {
		
		InputDialogBox input;
		
		public void onClick(ClickEvent event) {
			input = new InputDialogBox();
			input.setLabel("Bitte geben Sie den Namen der neuen Kontaktliste an.");
			editorAdministration.createContactList(input.getInput(), new AsyncCallback<ContactList>() {
				public void onFailure(Throwable arg0) {
					Window.alert("Fehler beim erstellen der Kontaktliste!");
				}
				public void onSuccess(ContactList arg0) {
					Window.alert("Kontaktliste erfolgreich erstellt.");
					clctvm.addContactList(arg0);
				}
			});
		}
	}
	
	private class deleteContactListClickHandler implements ClickHandler {

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
	
	private class shareContactListClickHandler implements ClickHandler {
		
		InputDialogBox input;
		
		public void onClick(ClickEvent event) {
			input = new InputDialogBox();
			input.setLabel("Bitte geben Sie die Email-Adresse des Nutzers ein mit dem Sie die Kontaktliste teilen möchten.");
			editorAdministration.shareContactList(clctvm.getSelectedContactList(), input.getInput(), new AsyncCallback<Permission>() {
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

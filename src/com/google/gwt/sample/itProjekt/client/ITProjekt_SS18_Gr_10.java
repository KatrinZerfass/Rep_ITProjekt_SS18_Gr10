package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.sample.itProjekt.client.ContactForm.CloseButton;
import com.google.gwt.sample.itProjekt.client.ContactForm.EmailDialogBox;
import com.google.gwt.sample.itProjekt.shared.CommonSettings;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.sample.itProjekt.shared.LoginServiceAsync;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;

import java.util.Vector;

import org.apache.tools.ant.taskdefs.Sync.MyCopy;

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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
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

	
	/* Die Instanzenvariablen, die mit dem Login-Service zusammenhängen. */
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
	      "Please sign in to your Google Account to access the application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	
	User user = null;
	EditorAdministrationAsync editorAdministration = null;
	ContactForm cf = null;
	
	
	// aus der loadApplication kopiert
	VerticalPanel searchPanel = new VerticalPanel();
	Label searchLabel = new Label();
	HorizontalPanel searchBox = new HorizontalPanel();
	TextBox searchTextBox = new TextBox();
	PushButton searchButton = new PushButton();
	Image searchButtonImg = new Image("searchButton.png");
	
	/** Das referenzierte ContactListContactTreeViewModel-Objekt */

	ContactListContactTreeViewModel clctvm = new ContactListContactTreeViewModel();

	
	/** Die Default-Kontaktliste MyContactsContactList mccl. */
	ContactList mccl = new ContactList();
	
	public class CloseButton extends Button{
		InputDialogBox db;
		
		public CloseButton(InputDialogBox db) {
			this.db = db;
			this.addClickHandler(new CloseDBClickHandler(db)); 
			this.setText("X");
			this.addStyleName("closebutton");
		}
		
		private class CloseDBClickHandler implements ClickHandler{
			InputDialogBox db;
	
			
			public CloseDBClickHandler(InputDialogBox db) {
				this.db=db;
			}
			
			public void onClick(ClickEvent event) {
				db.hide();
			}
			
		}
		
	}
	
	/**
	 * Die innere Klasse InputDialogBox.
	 * ?? was macht sie ??
	 * 
	 * @author JanNoller
	 */
	public class InputDialogBox extends DialogBox{
		
		private String input;
		
		Label dialogBoxLabel = new Label();
		
        private TextBox tb;
        private SuggestBox sb;
        private MultiWordSuggestOracle oracle;
        
        Button ok = new Button("OK");
		
		/**
		 * Der Konstruktor von InputDialogBox
		 * ?? was macht er ??
		 */
		public InputDialogBox(TextBox inputtb) {
			
			setTextBox(inputtb);
			
			Window.alert("InputDialogBox instanziert");
			
			setText("Eingabe");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
	        
			VerticalPanel panel = new VerticalPanel();
			CloseButton close= new CloseButton(this);
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			panel.add(close);
			panel.add(tb);
	        panel.add(ok);

	        setWidget(panel);
	        
	        show();
	    }
		
		public InputDialogBox(MultiWordSuggestOracle inputOracle) {
			
			setOracle(inputOracle);
			
			setdialogBoxLabel("Bitte geben Sie die Email-Adresse des Nutzers ein mit dem Sie die Kontaktliste teilen möchten.");
			
			editorAdministration.getAllUsers(new AsyncCallback<Vector<User>>() {
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
					
					setText("Eingabe");
					setAnimationEnabled(true);
					setGlassEnabled(true);
					
					VerticalPanel panel = new VerticalPanel();

			        panel.setHeight("100");
			        panel.setWidth("300");
			        panel.setSpacing(10);
			        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			        panel.add(dialogBoxLabel);
					panel.add(getSuggestBox());
			        panel.add(ok);
			        
			        setWidget(panel);
			        
			        show();
				}
			});
		}
		
		public Button getOKButton() {
			return this.ok;
		}
		
		public void setOKButton(Button b) {
			this.ok = b;
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
		 * Gets the label.
		 *
		 * @return the label
		 */
		public Label getdialogBoxLabel () {
			return this.dialogBoxLabel;
		}
		
		/**
		 * Setter des Labels.
		 *
		 * @param labelText der Text des Labels.
		 */
		public void setdialogBoxLabel (String labelText) {
			this.dialogBoxLabel.setText(labelText);
		}
		
		public TextBox getTextBox() {
			return this.tb;
		}
		
		public void setTextBox(TextBox tb) {
			this.tb = tb;
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

	  
	/**
	 * Die Methode onModuelLoad() wird beim Starten der Anwendung aufgerufen
	 */
	public void onModuleLoad() {
		  
		  
		 /* 
		  * Login Status des Benutzers wird geprüft. 
		  */
	    
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login("https://it-projekt-gruppe-10-203610.appspot.com/ITProjekt_SS18_Gr_10.html", new AsyncCallback<LoginInfo>() {
	    public void onFailure(Throwable error) {
	    }

	    public void onSuccess(LoginInfo result) {
	    	loginInfo = result;
	    	if(loginInfo.isLoggedIn()) {
	    		loadUserInformation();
	    		
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
  	public void loadUserInformation() {
	    	
		if(editorAdministration == null) {
			editorAdministration = ClientsideSettings.getEditorAdministration();
	    }
		
	    
	    
	    // Anlegen des User Objekts & Abspeichern in einer lokalen Variabel
	    
	    editorAdministration.getUserInformation(loginInfo.getEmailAddress(), new AsyncCallback<User>() {
			
	    	public void onFailure(Throwable caught) {
	    		Window.alert("AsyncCallback fehlgeschlagen");			
			}

			public void onSuccess(User result) {
				ClientsideSettings.setUser(result);
				user = result;
				loadApplication();
				
			}

			/*
			   * Die Methode loadApplication() wird aufgerufen, wenn der Benutzer eingeloggt ist. Sie beinhaltet 
			   * die eigentliche Applikation.  
			   */
			  	public void loadUserInformation() {
				    	
					if(editorAdministration == null) {
						editorAdministration = ClientsideSettings.getEditorAdministration();
				    }
					
				    
				    
				    // Anlegen des User Objekts & Abspeichern in einer lokalen Variabel
				    
				    editorAdministration.getUserInformation(loginInfo.getEmailAddress(), new AsyncCallback<User>() {
						
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
			   		
	    });
  	}	    				
		
  	
  	public void loadApplication(){
		/*
		 * Im Folgenden wird das GUI aufgebaut
		 */
	    
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    signOutLink.addStyleName("signout");
		signInLink.addStyleName("reportbutton");
		
		RootPanel.get("Login").add(signOutLink);
		
		/*
		 * Das Div "ContactForm" beinhaltet eine Instanz von ContactForm
		 */
		cf = new ContactForm();
		RootPanel.get("ContactForm").add(cf);
	
		
		VerticalPanel buttonsPanel = cf.getButtonsPanel();
		RootPanel.get("ButtonsPanel").add(buttonsPanel);
		
		HorizontalPanel newPropertyPanel = cf.getNewPropertyPanel();
		RootPanel.get("PropertyPanel").add(newPropertyPanel);
		
		/*
		 * Das Div "Contactlist" links unter der Navigation beinhaltet die Buttons für Kontaktlisten und das Suchfeld.
		 */
		HorizontalPanel clButtonsAndSearchPanel = new HorizontalPanel();
		clButtonsAndSearchPanel.setWidth("100%");
				
		//Die Buttons für Kontaktlisten
		VerticalPanel contactListButtonsPanel = new VerticalPanel();
		contactListButtonsPanel.setStyleName("contactListButtonPanel");
		
		Button newContactListButton = new Button("Neue Kontaktliste");
		newContactListButton.addStyleName("buttonPanel");
		Button deleteContactListButton = new Button("Kontaktliste löschen");
		deleteContactListButton.addStyleName("buttonPanel");
		Button shareContactListButton = new Button("Kontaktliste teilen");
		shareContactListButton.addStyleName("buttonPanel");
		
		contactListButtonsPanel.add(shareContactListButton);
		contactListButtonsPanel.add(deleteContactListButton);
		contactListButtonsPanel.add(newContactListButton);
		
		newContactListButton.addClickHandler(new NewContactListClickHandler());
		deleteContactListButton.addClickHandler(new DeleteContactListClickHandler());
		shareContactListButton.addClickHandler(new ShareContactListClickHandler());
		
		clButtonsAndSearchPanel.add(contactListButtonsPanel);
	
		VerticalPanel searchPanel = new VerticalPanel();
		Label searchLabel = new Label();
		HorizontalPanel searchBox = new HorizontalPanel();
		TextBox searchTextBox = new TextBox();
		PushButton searchButton = new PushButton();
		Image searchButtonImg = new Image("searchButton.png");
		
		//Das Suchfeld
		
		searchButton.addClickHandler(new SearchButtonClickHandler(searchTextBox));
		searchLabel.setText("Durchsuchen Sie die ausgewählte Kontaktliste nach bestimmten Kontakten oder Ausprägungen");
		searchLabel.addStyleName("label_search");
		searchLabel.setWidth("210px");
		searchPanel.add(searchLabel);
		
		
		searchButtonImg.addStyleName("editorSearch");
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
		mccl.setOwner(user.getId());
		mccl.setId(5);
		
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
		
		
		RootPanel.get("Navigator").add(cellBrowser);
		

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
			inputDB = new InputDialogBox(new TextBox());
			inputDB.setdialogBoxLabel("Bitte geben Sie den Namen der neuen Kontaktliste an.");
			inputDB.show();
			
			inputDB.getOKButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					Window.alert("user-email: " + user.getEmail());
					editorAdministration.createContactList(inputDB.getTextBox().getText(), user, new AsyncCallback<ContactList>() {
						public void onFailure(Throwable arg0) {
							Window.alert("Fehler beim Erstellen der Kontaktliste!");
							inputDB.hide();
						}
						public void onSuccess(ContactList result) {
							Window.alert("Kontaktliste erfolgreich erstellt.");
							clctvm.addContactList(result);
							inputDB.hide();
						}
					});
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
			
			if(clctvm.getSelectedContactList() == clctvm.getMyContactsContactList()) {
				Window.alert("Sie können die Liste all Ihrer Kontakte nicht löschen!");
			}
			else {
				if(clctvm.getSelectedContactList().getOwner() == user.getId()) {
					editorAdministration.deleteContactList(clctvm.getSelectedContactList(), new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable arg0) {
							Window.alert("Fehler beim löschen der Kontaktliste!");
						}
						@Override
						public void onSuccess(Void arg0) {
							Window.alert(clctvm.getSelectedContactList().getName());
							Window.alert("Kontaktliste erfolgreich gelöscht.");	
							clctvm.removeContactList(clctvm.getSelectedContactList());
							clctvm.setSelectedContactList(clctvm.getMyContactsContactList());
						}
					});
					
					
				}
				else {
					editorAdministration.deletePermission(user, clctvm.getSelectedContactList(), new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable arg0) {
							Window.alert("Fehler beim entfernen der Kontaktliste!");
						}
						@Override
						public void onSuccess(Void arg0) {
							Window.alert("Kontaktliste erfolgreich entfernt.");
						}
					});
				}
			}
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
		
//		User shareUser = new User();
		
		public void onClick(ClickEvent event) {
			if(clctvm.getSelectedContactList() == clctvm.getMyContactsContactList()) {
				Window.alert("Sie können die Liste all Ihrer Kontakte nicht teilen!");
			}
			else {
				if(clctvm.getSelectedContactList() == null) {
					Window.alert("Keine Kontaktliste ausgewählt");
				}else {
					inputDB = new InputDialogBox(new MultiWordSuggestOracle());
					inputDB.getOKButton().addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							if(inputDB.getSuggestBox().getText()== "") {
								Window.alert("Fehler beim Teilen der Kontaktliste!");
							}else {
								Window.alert(inputDB.getSuggestBox().getText());
								
								editorAdministration.shareContactList(user, inputDB.getSuggestBox().getText(), clctvm.getSelectedContactList(), new AsyncCallback<Permission>() {
			
									public void onFailure(Throwable arg0) {
										Window.alert("Fehler beim Teilen der Kontaktliste!");
										inputDB.hide();
									}
									public void onSuccess(Permission arg0) {
										Window.alert("Kontaktliste erfolgreich geteilt.");
										inputDB.hide();
									}
								});
							}
						}
					});
				}
			}
//			editorAdministration.getAllUsers(new AsyncCallback<Vector<User>>() {
//				@Override
//				public void onFailure(Throwable arg0) {
//					Window.alert("Fehler beim holen der User aus der Datenbank im ShareContactListClickHandler!");
//				}
//				@Override
//				public void onSuccess(Vector<User> arg0) {
//					allUsers = arg0;
//				}
//			});
//			
//			inputDB.getOKButton().addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent arg0) {
//					
//					editorAdministration.getUser(inputDB.getTextBox().getText(), new AsyncCallback<User>() {
//						@Override
//						public void onFailure(Throwable arg0) {
//							Window.alert("Fehler beim holen des Users für das teilen der Kontaktliste!");
//						}
//						@Override
//						public void onSuccess(User arg0) {
//							shareUser = arg0;
//							Window.alert("Email: shareUser " + shareUser.getEmail());
//							Window.alert(inputDB.getTextBox().getText());
//						}
//					});
//					
//					if(allUsers.contains(shareUser) && shareUser != user) {
//						editorAdministration.shareContactList(user, shareUser, clctvm.getSelectedContactList(), new AsyncCallback<Permission>() {
//							@Override
//							public void onFailure(Throwable arg0) {
//								Window.alert("Fehler beim Teilen der Kontaktliste!");
//								inputDB.hide();
//							}
//							@Override
//							public void onSuccess(Permission arg0) {
//								Window.alert("Kontaktliste erfolgreich geteilt.");
//								inputDB.hide();
//							}
//						});
//					}
//					else {
//						Window.alert("Ungültiger Benutzer!");
//						inputDB.hide();
//					}
//				}
//			});
		}
	}
	
	/**
	 * Die innere Klasse SearchButtonClickHandler. 
	 * Eine Instanz von ihr wird beim Klick auf den searchButton aufgerufen.
	 * 
	 * @author JanNoller
	 */
	private class SearchButtonClickHandler implements ClickHandler {
		
		TextBox searchTextBox = null;						
		public SearchButtonClickHandler(TextBox sTB) {
			searchTextBox = sTB;
		}
					
		@Override
		public void onClick(ClickEvent arg0) {
			
			ContactList selectedContactList = clctvm.getSelectedContactList();		
			
			editorAdministration.getContactsOfNameSearchResult(user, searchTextBox.getText(), selectedContactList,  new AsyncCallback<Vector<Contact>>() {
				@Override
				public void onFailure(Throwable arg0) {
					Window.alert("Fehler beim Füllen des allContactsOfUser Vectors!");
				}
				@Override
				public void onSuccess(Vector<Contact> arg0){
					if(arg0.size() != 0) {
						clctvm.addNameResults();
						clctvm.addContactOfSearchResultList(clctvm.getNameResultsCL(), arg0);
					}else{
						clctvm.deleteNameResults();
					}
					
				}
				
			});	
			
			editorAdministration.getContactsOfValueSearchResult(user, searchTextBox.getText(), selectedContactList, new AsyncCallback<Vector<Contact>>() {
				
				public void onFailure(Throwable arg0) {
					Window.alert("Fehler beim Füllen des allContactsOfUser Vectors!");
				}
				@Override
				public void onSuccess(Vector<Contact> arg0){
					if(arg0.size() != 0){
						clctvm.addValueResults();
						clctvm.addContactOfSearchResultList(clctvm.getValueResultsCL(), arg0);
					}else{
						clctvm.deleteValueResults();
					}
				}
				
			});	
					
					
//					editorAdministration.getAllContactsWithName(searchTextBox.getText(), new AsyncCallback<Vector<Contact>>() {
//						@Override
//						public void onFailure(Throwable arg0) {
//							Window.alert("Fehler beim Suchen der Kontakte nach Namen im cMapper!");
//						}
//						@Override
//						public void onSuccess(Vector<Contact> arg0) {
//							nameResults = arg0;
//							
//							editorAdministration.getAllContactsWithValue(searchTextBox.getText(), new AsyncCallback<Vector<Contact>>() {
//								@Override
//								public void onFailure(Throwable arg0) {
//									Window.alert("Fehler beim Suchen der Kontakte nach Value im vMapper!");	
//								}
//								@Override
//								public void onSuccess(Vector<Contact> arg0) {
//									valueResults = arg0;
//									
//									if (nameResults.size() > 0) {
//										clctvm.addNameResults();
//										for (Contact c : nameResults) {
//											if (allContactsOfUser.contains(c)) {
//												finalNameResult.add(c);
//											}
//										}
//										
//										clctvm.addContactOfSearchResultList(clctvm.getNameResultsCL(), finalNameResult);
//									}
////									else {
////										clctvm.removeContactList(clctvm.getNameResultsCL());
////									}
//									if (valueResults.size() > 0) {
//										clctvm.addValueResults();
//										for (Contact c : valueResults) {
//											if (allContactsOfUser.contains(c)) {
//											finalValueResult.add(c);
//											}
//										}
//										clctvm.addContactOfSearchResultList(clctvm.getValueResultsCL(), finalValueResult);
//									}
//									else {
//										clctvm.removeContactList(clctvm.getValueResultsCL());
//									}
//									if (nameResults.size() == 0 && valueResults.size() == 0) {
//										Window.alert("Kein Suchergebnis!");
//										clctvm.removeContactList(clctvm.getNameResultsCL());
//										clctvm.removeContactList(clctvm.getValueResultsCL());
//									}
//								}
//							});
//						}
//					});
//				}
//			});
			
//TODO fliegt raus wenn triple callback tut			
			
//			editorAdministration.getAllContactsWithName(searchTextBox.getText(), new AsyncCallback<Vector<Contact>>() {
//				@Override
//				public void onFailure(Throwable arg0) {
//					Window.alert("Fehler beim Suchen der Kontakte nach Namen im cMapper!");
//				}
//				@Override
//				public void onSuccess(Vector<Contact> arg0) {
//					nameResults = arg0;
//				}
//			});
			
//			editorAdministration.getAllContactsWithValue(searchTextBox.getText(), new AsyncCallback<Vector<Contact>>() {
//				@Override
//				public void onFailure(Throwable arg0) {
//					Window.alert("Fehler beim Suchen der Kontakte nach Value im vMapper!");	
//				}
//				@Override
//				public void onSuccess(Vector<Contact> arg0) {
//					valueResults = arg0;
//				}
//			});
			
//			if (nameResults.size() > 0) {
//				clctvm.addNameResults();
//				for (Contact c : nameResults) {
//					if (allContactsOfUser.contains(c)) {
//						clctvm.addContactOfContactList(clctvm.getNameResultsCL(), c);
//					}
//				}
//			}
//			else {
//				clctvm.removeContactList(clctvm.getNameResultsCL());
//			}
//			if (valueResults.size() > 0) {
//				clctvm.addValueResults();
//				for (Contact c : valueResults) {
//					if (allContactsOfUser.contains(c)) {
//						clctvm.addContactOfContactList(clctvm.getValueResultsCL(), c);
//					}
//				}
//			}
//			else {
//				clctvm.removeContactList(clctvm.getValueResultsCL());
//			}
//			if (nameResults.size() == 0 && valueResults.size() == 0) {
//				Window.alert("Kein Suchergebnis!");
//				clctvm.removeContactList(clctvm.getNameResultsCL());
//				clctvm.removeContactList(clctvm.getValueResultsCL());
//			}
		}
	}
}	 

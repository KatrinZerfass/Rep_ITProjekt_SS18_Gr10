package com.google.gwt.sample.itProjekt.client;


import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.sample.itProjekt.shared.LoginServiceAsync;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;

import java.util.Vector;

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
	      "Bitte loggen Sie sich mit ihrem Google-Account ein, um die Anwendung zu nutzen.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private Label signedInUserName = null;
	
	
	User user = null;
	EditorAdministrationAsync editorAdministration = null;
	ContactForm cf = null;
	ContactListForm clf = null;
	ContactListContactTreeViewModel clctvm = new ContactListContactTreeViewModel();
	ClientsideFunctions.InputDialogBox createAccountBox = null;

	/** Die Default-Kontaktliste MyContactsContactList mccl. */
	ContactList mccl = new ContactList();
	
	
	  
	/**
	 * Die Methode onModuelLoad() wird beim Starten der Anwendung aufgerufen
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
	    
	    editorAdministration.isUserKnown(loginInfo.getEmailAddress(), new AsyncCallback<Boolean>() {
			
	    	public void onFailure(Throwable caught) {
	    		Window.alert("AsyncCallback fehlgeschlagen: isUserKnown");			
			}

			public void onSuccess(Boolean result) {
				if (result) {
					editorAdministration.getUserByEmail(loginInfo.getEmailAddress(), new AsyncCallback<User>() {
						public void onFailure(Throwable arg0) {
							Window.alert("AsyncCallback fehlgeschlagen: getUser");
						}
						public void onSuccess(User arg0) {
							ClientsideSettings.setUser(arg0);
							user = arg0;
							loadApplication();
						}
					});
				}
				
				else {
					createAccountBox = new ClientsideFunctions.InputDialogBox(loginInfo.getEmailAddress());
					
					createAccountBox.getOKButton().addClickHandler(new ClickHandler() {
						
						public void onClick(ClickEvent arg0) {
							if(ClientsideFunctions.checkName(createAccountBox.getMultiUseTextBox().getText()) && ClientsideFunctions.checkName(createAccountBox.getNameTextBox().getText())) {
								editorAdministration.createUserContact(createAccountBox.getMultiUseTextBox().getText(), createAccountBox.getNameTextBox().getText(), createAccountBox.getSexListBox().getSelectedItemText(), loginInfo.getEmailAddress(), new AsyncCallback<User>() {
									public void onFailure(Throwable arg0) {
										Window.alert("AsyncCallback fehlgeschlagen: createContact");
										createAccountBox.hide();
									}
									public void onSuccess(User arg0) {
										if(arg0 == null) {
											Window.alert("arg0 = null");
										}
										final ClientsideFunctions.popUpBox welcome = new ClientsideFunctions.popUpBox("Herzlich Willkommen!", new ClientsideFunctions.OkButton());
										welcome.getOkButton().addCloseDBClickHandler(welcome);
										createAccountBox.hide();
										ClientsideSettings.setUser(arg0);
										user = arg0;
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
		
  	
  	public void loadApplication(){
  		
  		//Window.alert(loginInfo.getNickname());
  		
		/*
		 * Im Folgenden wird das GUI aufgebaut
		 */
	    VerticalPanel loginPanel = new VerticalPanel();
	    HorizontalPanel signedInUserPanel = new HorizontalPanel();
	    Label signedInUserLabel = new Label("Angemeldet als:      ");
	    signedInUserPanel.add(signedInUserLabel);
	    signedInUserName = new Label();
	    
	    editorAdministration.getFullNameOfUser(user, new AsyncCallback<String>(){
	    	public void onFailure(Throwable t) {
	    		
	    	}
	    	public void onSuccess(String result) {
	    		signedInUserName.setText("  " + result);
	    	
	    		
	    	}
	    });
	    
	    signedInUserPanel.add(signedInUserName);
	    
	    
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    signOutLink.addStyleName("signout");
		signInLink.addStyleName("reportbutton");
		signedInUserPanel.addStyleName("signedInUser");
		
		loginPanel.add(signOutLink);
		loginPanel.add(signedInUserPanel);
		
		
		RootPanel.get("Login").add(loginPanel);
		
		/*
		 * Das Div "ContactForm" beinhaltet eine Instanz von ContactForm
		 */
		cf = new ContactForm();
		RootPanel.get("ContactForm").add(cf);
		
		clf = new ContactListForm();
		RootPanel.get("Contactlist").add(clf);
	
		
		VerticalPanel buttonsPanel = cf.getButtonsPanel();
		RootPanel.get("ButtonsPanel").add(buttonsPanel);
		
		HorizontalPanel newPropertyPanel = cf.getNewPropertyPanel();
		RootPanel.get("PropertyPanel").add(newPropertyPanel);
		
	
		
		
		/*
		 * TreeViewModel und ContactForm werden miteinander verbunden.
		 */
		clctvm.setContactForm(cf);
		cf.setClctvm(clctvm);
		clf.setClctvm(clctvm);
		
		/*
		 * Die default-Kontaktliste "Meine Kontakte" wird erstellt.
		 */
		mccl.setName("Meine Kontakte");
		mccl.setOwner(user.getId());
		mccl.setId(5);
		
		mccl.setMyContactsFlag(true);
		
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
//		}
//	}
	
	
					
					
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
//		}
//	}
	
	
	
}	 

package com.google.gwt.sample.itProjekt.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactListForm extends VerticalPanel{
	
		EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	
		ContactListContactTreeViewModel clctvm = null;
		
		User user = new User();
	
		VerticalPanel searchPanel = new VerticalPanel();
		Label searchLabel = new Label();
		HorizontalPanel searchBox = new HorizontalPanel();
		TextBox searchTextBox = new TextBox();
		PushButton searchButton = new PushButton();
		Image searchButtonImg = new Image("searchButton.png");
		
		
	public void onLoad() {
			
			super.onLoad();
			
			user = ClientsideSettings.getUser();
			/*
			 * Das Div "Contactlist" links unter der Navigation beinhaltet die Buttons für Kontaktlisten und das Suchfeld.
			 */
			HorizontalPanel contactListPanel = new HorizontalPanel();
			contactListPanel.setWidth("100%");
			
			this.add(contactListPanel);
					
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
			
			contactListPanel.add(contactListButtonsPanel);
	
			
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
			
			contactListPanel.add(searchPanel);
			contactListPanel.setCellHorizontalAlignment(searchPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		}
	
	

//		public class CloseButton extends Button{
//			InputDialogBox db;
//			
//			public CloseButton(InputDialogBox db) {
//				this.db = db;
//				this.addClickHandler(new CloseDBClickHandler(db)); 
//				this.setText("Abbrechen");
//				this.addStyleName("closebutton");
//			}
//			
//			private class CloseDBClickHandler implements ClickHandler{
//				InputDialogBox db;
//		
//				
//				public CloseDBClickHandler(InputDialogBox db) {
//					this.db=db;
//				}
//				
//				public void onClick(ClickEvent event) {
//					db.hide();
//				}
//				
//			}
//			
//		}
		
		
		
		/**
		 * Die innere Klasse InputDialogBox.
		 * ?? was macht sie ??
		 * 
		 * @author JanNoller
		 */
//		public class InputDialogBox extends DialogBox{
//			
//			private String input;
//			
//			Label dialogBoxLabel = new Label();
//			
//	        private TextBox tb;
//	        private SuggestBox sb;
//	        private MultiWordSuggestOracle oracle;
//			CloseButton close=new CloseButton(this);
//
//	        Button ok = new Button("OK");
//			
//			/**
//			 * Der Konstruktor von InputDialogBox
//			 * ?? was macht er ??
//			 */
//			public InputDialogBox(TextBox inputtb) {
//				
//				setTextBox(inputtb);
//		        ok.addStyleName("okbutton");
//		        close.addStyleName("closebutton");
//
//				Window.alert("InputDialogBox instanziert");
//				
//				setText("Eingabe");
//				setAnimationEnabled(true);
//				setGlassEnabled(true);
//				
//		        
//				VerticalPanel panel = new VerticalPanel();
//		        panel.setHeight("100");
//		        panel.setWidth("300");
//		        panel.setSpacing(10);
//		        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//		        panel.add(dialogBoxLabel);
//				HorizontalPanel hpanel=new HorizontalPanel();
//				hpanel.add(close);
//		        hpanel.add(ok);
//		        panel.add(tb);
//		        panel.add(hpanel);
//
//		        setWidget(panel);
//		        
//		        show();
//		    }
//			
//			public InputDialogBox(MultiWordSuggestOracle inputOracle) {
//				
//				setOracle(inputOracle);
//		        ok.addStyleName("okbutton");
//		        close.addStyleName("closebutton");
//
//				setdialogBoxLabel("Bitte geben Sie die Email-Adresse des Nutzers ein mit dem Sie die Kontaktliste teilen möchten.");
//				
//				editorAdministration.getAllUsers(new AsyncCallback<Vector<User>>() {
//					public void onFailure(Throwable arg0) {
//						Window.alert("Fehler beim holen aller User in der InputDialogBox");
//					}
//					@Override
//					public void onSuccess(Vector<User> arg0) {
//						
//						for(User loopUser : arg0) {
//							if (!loopUser.equals(user)) {
//								getOracle().add(loopUser.getEmail());
//							}
//						}
//						setSuggestBox(new SuggestBox(getOracle()));
//						
//						setText("Eingabe");
//						setAnimationEnabled(true);
//						setGlassEnabled(true);
//						
//						VerticalPanel panel = new VerticalPanel();
//
//				        panel.setHeight("100");
//				        panel.setWidth("300");
//				        panel.setSpacing(10);
//				        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//				        panel.add(dialogBoxLabel);
//						panel.add(getSuggestBox());
//						HorizontalPanel hpanel= new HorizontalPanel();
//				        hpanel.add(close);
//				        hpanel.add(ok);
//				        panel.add(hpanel);
//				        setWidget(panel);
//				        
//				        show();
//					}
//				});
//			}
//			
//			public Button getOKButton() {
//				return this.ok;
//			}
//			
//			public void setOKButton(Button b) {
//				this.ok = b;
//			}
//			
//			/**
//			 * Getter von input.
//			 *
//			 * @return den Input
//			 */
//			public String getInput() {
//				return this.input;
//			}
//			
//			/**
//			 * Setter von input.
//			 *
//			 * @param input der Input
//			 */
//			public void setInput(String input) {
//				this.input = input;
//			}
//
//			/**
//			 * Gets the label.
//			 *
//			 * @return the label
//			 */
//			public Label getdialogBoxLabel () {
//				return this.dialogBoxLabel;
//			}
//			
//			/**
//			 * Setter des Labels.
//			 *
//			 * @param labelText der Text des Labels.
//			 */
//			public void setdialogBoxLabel (String labelText) {
//				this.dialogBoxLabel.setText(labelText);
//			}
//			
//			public TextBox getTextBox() {
//				return this.tb;
//			}
//			
//			public void setTextBox(TextBox tb) {
//				this.tb = tb;
//			}
//
//			public SuggestBox getSuggestBox() {
//				return sb;
//			}
//
//			public void setSuggestBox(SuggestBox sb) {
//				this.sb = sb;
//			}
//
//			public MultiWordSuggestOracle getOracle() {
//				return oracle;
//			}
//
//			public void setOracle(MultiWordSuggestOracle oracle) {
//				this.oracle = oracle;
//			}
//		}
	
		
	
		
		/**
		 * Die innere Klasse NewContactListClickHandler. 
		 * Eine Instanz von ihr wird beim Klick auf den newContactListButton aufgerufen.
		 * 
		 * @author JanNoller
		 */
		private class NewContactListClickHandler implements ClickHandler {
			
			ClientsideFunctions.InputDialogBox inputDB;
			
			
			public void onClick(ClickEvent event) {
				inputDB = new ClientsideFunctions.InputDialogBox(new TextBox());
				inputDB.setdialogBoxLabel("Bitte geben Sie den Namen der neuen Kontaktliste an.");
				inputDB.show();
				
				inputDB.getOKButton().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent arg0) {

						editorAdministration.createContactList(inputDB.getMultiUseTextBox().getText(), user, new AsyncCallback<ContactList>() {
							public void onFailure(Throwable arg0) {
								Window.alert("Fehler beim Erstellen der Kontaktliste!");
								inputDB.hide();
							}
							public void onSuccess(final ContactList result) {
								
								inputDB.hide();
								
								final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontaktliste erfolgreich erstellt!", new ClientsideFunctions.OkButton());
								success.getOkButton().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent click) {
										clctvm.deleteNameResults();
										clctvm.deleteValueResults();
										clctvm.addContactList(result);	
										success.hide();
									}
								});
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
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Sie können die Liste all Ihrer Kontakte nicht löschen.", new ClientsideFunctions.OkButton());
					failed.getOkButton().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent arg0) {
							failed.hide();
						}
					});
				}
				else {
				
					final ClientsideFunctions.popUpBox safety = new ClientsideFunctions.popUpBox("Sind Sie sicher dass Sie die Kontaktliste löschen möchten?", new ClientsideFunctions.OkButton(), new ClientsideFunctions.CloseButton());
					safety.getCloseButton().addCloseDBClickHandler(safety);
					safety.getOkButton().addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent arg0) {
							safety.hide();
							editorAdministration.deleteContactList(clctvm.getSelectedContactList(), ClientsideFunctions.isOwner(clctvm.getSelectedContactList(), user), user, new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable arg0) {
									Window.alert("Fehler beim löschen der Kontaktliste!");
								}
								@Override
								public void onSuccess(Void arg0) {

									final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontaktliste erfolgreich gelöscht.", new ClientsideFunctions.OkButton());
									success.getOkButton().addClickHandler(new ClickHandler() {
										
										@Override
										public void onClick(ClickEvent arg0) {
											clctvm.removeContactList(clctvm.getSelectedContactList());
											clctvm.setSelectedContactList(clctvm.getMyContactsContactList());
											success.hide();
										}
									});
								}
							});
						}
					});
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
		
			ClientsideFunctions.InputDialogBox inputDB = null;
			
//			User shareUser = new User();
			
			public void onClick(ClickEvent event) {
				if(clctvm.getSelectedContactList() == clctvm.getMyContactsContactList()) {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Sie können die Liste all Ihrer Kontakte nicht teilen.", new ClientsideFunctions.OkButton());
					failed.getOkButton().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent arg0) {
							failed.hide();
						}
					});
				}
				else {
					inputDB = new ClientsideFunctions.InputDialogBox(new MultiWordSuggestOracle(), "Bitte geben Sie die Email-Adresse des Nutzers ein mit dem Sie die Kontaktliste teilen möchten.");
					inputDB.getOKButton().addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							if(inputDB.getSuggestBox().getText()== "") {
								inputDB.hide();
								
								final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Fehler bei Teilen der Kontaktliste weil kein Nutzer ausgewählt wurde.", new ClientsideFunctions.OkButton());
								failed.getOkButton().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent arg0) {
										failed.hide();
									}
								});
							}else {	
								inputDB.hide();
								
								String[] split = inputDB.getSuggestBox().getText().split(" - ");
								String userEmail = split[1].substring(0, split[1].length());
								
								editorAdministration.shareContactList(user, userEmail, clctvm.getSelectedContactList(), new AsyncCallback<Permission>() {
			
									public void onFailure(Throwable arg0) {
										Window.alert("Weil onFailure");
									}
									public void onSuccess(Permission arg0) {
										if(arg0 != null) {

											final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontaktliste erfolgreich geteilt!", new ClientsideFunctions.OkButton());
											success.getOkButton().addClickHandler(new ClickHandler() {
												@Override
												public void onClick(ClickEvent arg0) {
													success.hide();
												}
											});
										}
										else if(arg0 == null) {
											inputDB.hide();
											final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Nutzer ist der Urheber der Kontaktliste", new ClientsideFunctions.OkButton());
											failed.getOkButton().addClickHandler(new ClickHandler() {
												@Override
												public void onClick(ClickEvent arg0) {
													failed.hide();
												}
											});
										}
									}
								});
							}
						}
					});
				}
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
					ContactList selectedContactList; 	
					boolean foundResult = false;
					
					public SearchButtonClickHandler(TextBox sTB) {
						searchTextBox = sTB;
					}
								
					@Override
					public void onClick(ClickEvent arg0) {
						foundResult = false; 
						selectedContactList = clctvm.getSelectedContactList();
						
						editorAdministration.getContactsOfNameSearchResult(user, searchTextBox.getText(), selectedContactList,   new AsyncCallback<Vector<Contact>>() {
							@Override
							public void onFailure(Throwable arg0) {
								Window.alert("Fehler beim Füllen des allContactsOfUser Vectors!");
							}
							@Override
							public void onSuccess(final Vector<Contact> arg0){
								if(arg0.size() != 0) {
									foundResult = true;
//									final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Suche erfolgreich!", new ClientsideFunctions.OkButton());
//									success.getOkButton().addClickHandler(new ClickHandler() {
//										@Override
//										public void onClick(ClickEvent click) {
											clctvm.addNameResults();
											clctvm.addContactOfSearchResultList(clctvm.getNameResultsCL(), arg0);
//											success.hide();
//										}
//									});
								}else{
//									final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Kein Suchergebnis.", new ClientsideFunctions.OkButton());
//									failed.getOkButton().addClickHandler(new ClickHandler() {
//										@Override
//										public void onClick(ClickEvent arg0) {
											clctvm.deleteNameResults();
//											failed.hide();
//										}
//									});
								}
								
								editorAdministration.getContactsOfValueSearchResult(user, searchTextBox.getText(), selectedContactList, new AsyncCallback<Vector<Contact>>() {
									
									public void onFailure(Throwable arg0) {
										Window.alert("Fehler beim Füllen des allContactsOfUser Vectors!");
									}
									@Override
									public void onSuccess(final Vector<Contact> arg0){
										if(arg0.size() != 0){
											foundResult = true;
											
											final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Suche erfolgreich!", new ClientsideFunctions.OkButton());
											success.getOkButton().addClickHandler(new ClickHandler() {
												
												@Override
												public void onClick(ClickEvent click) {
													clctvm.addValueResults();
													clctvm.addContactOfSearchResultList(clctvm.getValueResultsCL(), arg0);
													success.hide();
												}
											});
										}else{
											if(foundResult) {
												final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Suche erfolgreich!", new ClientsideFunctions.OkButton());
												success.getOkButton().addClickHandler(new ClickHandler() {
													
													@Override
													public void onClick(ClickEvent click) {
//														clctvm.addValueResults();
//														clctvm.addContactOfSearchResultList(clctvm.getValueResultsCL(), arg0);
														success.hide();
													}
												});
											}else {
												final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Kein Suchergebnis.", new ClientsideFunctions.OkButton());
												failed.getOkButton().addClickHandler(new ClickHandler() {
													@Override
													public void onClick(ClickEvent arg0) {
														clctvm.deleteValueResults();
														failed.hide();
													}
												});
											}
										}
									}
								});	
							}
						});	
						
						
					}
				}
				
		/**
		 * Setzt das referenzierte TreeViewModel
		 *
		 * @param ContactListContactTreeViewModel das referenzierte TreeViewModel
		 */
		public void setClctvm(ContactListContactTreeViewModel clctvm) {
			this.clctvm= clctvm;
			
		}

		
		
		/**
		 * Die Methode compareUser() vergleicht den aktuell angemeldeten Nutzer mit dem Eigentümer des Kontakts.
		 * 
		 * @return true= Eigentümer oder false= Teilhaber
		 * @author JanNoller
		 */		
	
				
//		public boolean compareUser () {
//			
//			if (user.getId() == clctvm.getSelectedContact().getOwner()) {
//				return true;
//			}
//			else {
//				return false;
//			}
//		}

				
}
		
		


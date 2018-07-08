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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die Klasse ContactListForm dient der Darstellung der Buttons, welche zum Teilen, Löschen und Anlegen von Kontaktlisten benötigt werden. 
 * Außerdem repräsentiert die Klasse ein Suchfeld, in dem nach Kontakten und Ausprägungen in einer Kontaktliste gesucht werden können.
 * 
 * @author KatrinZerfass, JoshuaHill, JanNoller 
 */

public class ContactListForm extends VerticalPanel{
	
		EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
		
		/** Das referenzierte TreeViewModel */
		ContactListContactTreeViewModel clctvm = null;
		
		User user = new User();
	
		/** Instanzenvariablen, welche zur Darstellung des Suchfeldes benötigt werden */
		VerticalPanel searchPanel = new VerticalPanel();
		Label searchLabel = new Label();
		HorizontalPanel searchBox = new HorizontalPanel();
		TextBox searchTextBox = new TextBox();
		PushButton searchButton = new PushButton();
		Image searchButtonImg = new Image("searchButton.png");
		
	/**
	 * Die Methode <code>onLoad()</code> wird in der EntryPoint-Klasse aufgerufen, um im GUI eine Instanz von ContactListForm zu erzeugen.
	 *
	 * @author KatrinZerfass
	 */	
	public void onLoad() {
			
			super.onLoad();
			
			HorizontalPanel contactListPanel = new HorizontalPanel();
			contactListPanel.setWidth("100%");
			this.add(contactListPanel);
			
			/* 
			 * Der angemeldete Nutzer wird abgefragt und als Instanzenvariable gespeichert.
			 */
			user = ClientsideSettings.getUser();
			
			
					
			/*
			 * Die Buttons für Kontaktlisten werden erstellt und es wird ihnen jeweils ein ClickHandler hinzugefügt
			 */
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
	
			
			/*
			 * Die Elemente für das Suchfeld wurden aufgebaut
			 */
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

	
		
		/**
		 * Die innere Klasse NewContactListClickHandler. 
		 * Eine Instanz von ihr wird beim Klick auf den Button "Neue Kontaktliste" aufgerufen.
		 * 
		 * @author JanNoller 
		 */
		private class NewContactListClickHandler implements ClickHandler {
			
			ClientsideFunctions.InputDialogBox inputDB;
			
			
			public void onClick(ClickEvent event) {
				//DialogBox in die man den Namen einer neuen Kontaktliste eintragen kann 
				inputDB = new ClientsideFunctions.InputDialogBox(new TextBox());
				inputDB.setdialogBoxLabel("Bitte geben Sie den Namen der neuen Kontaktliste an.");
				inputDB.show();
				
				inputDB.getOKButton().addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent arg0) {
						//bestätigt der Nutzer seine Eingabe mit "Ok", wir die Kontaktliste erstellt
						editorAdministration.createContactList(inputDB.getMultiUseTextBox().getText(), user, new AsyncCallback<ContactList>() {
							public void onFailure(Throwable arg0) {
								Window.alert("Fehler beim Erstellen der Kontaktliste!");
								inputDB.hide();
							}
							
							public void onSuccess(final ContactList result) {
								
								inputDB.hide();
								//Bestätigungsmeldung, dass die Kontaktliste erfolgreich angelegt wurde
								final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontaktliste erfolgreich erstellt!", new ClientsideFunctions.OkButton());
								success.getOkButton().addClickHandler(new ClickHandler() {
									
									public void onClick(ClickEvent click) {
										//falls die Suchergebnis-Kontaktlisten noch angezeigt werden, werden diese entfernt
										clctvm.deleteNameResults();
										clctvm.deleteValueResults();
										//die neue Kontaktliste wird dem TreeViewModel hinzugefügt
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
		 * Eine Instanz von ihr wird beim Klick auf den Button "Kontaktliste löschen" aufgerufen.
		 * 
		 * @author JanNoller
		 */
		private class DeleteContactListClickHandler implements ClickHandler {
			
			public void onClick(ClickEvent event) {
				
				if(clctvm.getSelectedContactList() == clctvm.getMyContactsContactList()) {
					//Fehlermeldung, falls es sich bei der ausgewählten Kontaktliste um die Default-Liste "Meine Kontakte" handelt
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Sie können die Liste all Ihrer Kontakte nicht löschen.", new ClientsideFunctions.OkButton());
					failed.getOkButton().addClickHandler(new ClickHandler() {
						
						public void onClick(ClickEvent arg0) {
							failed.hide();
						}
					});
				}
				else {
					//der Benutzer wird gefragt, ob er sich wirklich sicher ist, dass er die Kontaktliste löschen möchte
					final ClientsideFunctions.popUpBox safety = new ClientsideFunctions.popUpBox("Sind Sie sicher, dass Sie die Kontaktliste löschen möchten?", new ClientsideFunctions.OkButton(), new ClientsideFunctions.CloseButton());
					safety.getCloseButton().addCloseDBClickHandler(safety);
					
					safety.getOkButton().addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent arg0) {
							safety.hide();
							//bestätigt der Nutzer mit OK, wird die Kontaktliste gelöscht
							editorAdministration.deleteContactList(clctvm.getSelectedContactList(), ClientsideFunctions.isOwner(clctvm.getSelectedContactList(), user), user, new AsyncCallback<Void>() {
								
								public void onFailure(Throwable arg0) {
									Window.alert("Fehler beim löschen der Kontaktliste!");
								}
							
								public void onSuccess(Void arg0) {
									//Bestätigungsmeldung, dass die Kontaktliste gelöscht wurde
									final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontaktliste erfolgreich gelöscht.", new ClientsideFunctions.OkButton());
									success.getOkButton().addClickHandler(new ClickHandler() {
									
										public void onClick(ClickEvent arg0) {
											//die Kontaktliste wird auch aus dem TreeViewModel entfernt 
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
		 * Eine Instanz von ihr wird beim Klick auf den Button "Kontaktliste teilen" aufgerufen.
		 * 
		 * @author JanNoller
		 */
		private class ShareContactListClickHandler implements ClickHandler {
		
			ClientsideFunctions.InputDialogBox inputDB = null;
			
			public void onClick(ClickEvent event) {
				if(clctvm.getSelectedContactList() == clctvm.getMyContactsContactList()) {
					//Fehlermeldung, falls es sich bei der ausgewählten Kontaktliste um die Default-Liste "Meine Kontakte" handelt
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Sie können die Liste all Ihrer Kontakte nicht teilen.", new ClientsideFunctions.OkButton());
					failed.getOkButton().addClickHandler(new ClickHandler() {
						
						public void onClick(ClickEvent arg0) {
							failed.hide();
						}
					});
				}
				else {
					//DialogBox, in die der Nutzer eintragen soll, mit wem er die Kontaktliste teilen möchte
					inputDB = new ClientsideFunctions.InputDialogBox(new MultiWordSuggestOracle(), "Bitte geben Sie den Nutzer ein, mit dem Sie die Kontaktliste teilen möchten.");
					inputDB.getOKButton().addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							
							if(inputDB.getSuggestBox().getText()== "") {
								inputDB.hide();
								//Fehlermeldung, falls nichts eingetragen wurde und trotzdem der "Ok"-Button geklickt wurde
								final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Fehler bei Teilen der Kontaktliste weil kein Nutzer ausgewählt wurde.", new ClientsideFunctions.OkButton());
								failed.getOkButton().addClickHandler(new ClickHandler() {
									
									public void onClick(ClickEvent arg0) {
										failed.hide();
									}
								});
							}else {	
								inputDB.hide();
								/*
								 * Einträge der Suggestbox bestehen aus Klarnamen und e-mail-Adressen der Nutzer.
								 * Im folgenden wird die e-Mail-Adresse vom restlichen String getrennt, um diese im Serveraufruf
								 * shareContactList() als Argument zu übergeben.
								 */
								String[] split = inputDB.getSuggestBox().getText().split(" - ");
								String userEmail = split[1].substring(0, split[1].length());
								
								editorAdministration.shareContactList(user, userEmail, clctvm.getSelectedContactList(), new AsyncCallback<Permission>() {
			
									public void onFailure(Throwable arg0) {
										Window.alert("Weil onFailure");
									}
									public void onSuccess(Permission arg0) {
										if(arg0 != null) {
											//Bestätigungsmeldung, dass die Kontaktliste erfolgreich geteilt wurde
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
											//Meldung, dass es sich bei dem Nutzer, an den man teilen wollte, um den Eigentümer der Kontaktliste handelt
											final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Nutzer ist der Urheber der Kontaktliste", new ClientsideFunctions.OkButton());
											failed.getOkButton().addClickHandler(new ClickHandler() {
												
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
				 * @author JoshuaHill & JanNoller & KatrinZerfass
				 */
				private class SearchButtonClickHandler implements ClickHandler {
					
					TextBox searchTextBox = null;
					ContactList selectedContactList; 
					//bool'scher Wert, der angibt, ob Ergebnisse gefunden wurden 
					boolean foundResult = false;
					
					public SearchButtonClickHandler(TextBox sTB) {
						//die searchTextBox, in die der Nutzer seinen Suchbegriff eingetragen hat
						searchTextBox = sTB;
						
					}
								
				
					public void onClick(ClickEvent arg0) {
						foundResult = false;
						
						selectedContactList = clctvm.getSelectedContactList();
						/*
						 * Zuerst wird überprüft, ob der eingegebene Suchbegriff in den Namen von Kontakten gefunden wurde 
						 */
						editorAdministration.getContactsOfNameSearchResult(user, searchTextBox.getText(), selectedContactList,   new AsyncCallback<Vector<Contact>>() {
							
							public void onFailure(Throwable arg0) {
								Window.alert("Fehler beim Füllen des allContactsOfUser Vectors!");
							}
						
							public void onSuccess(final Vector<Contact> arg0){
								if(arg0.size() != 0) {
									foundResult = true;
									/*
									 * Wurden Ergebnisse gefunden, wird die virtuelle Kontaktliste mit den Suchergebnissen dem TreeViewModel hinzugefügt
									 * und mit den ensprechenden Kontakten befüllt. 
									 */
									clctvm.addNameResults();
									clctvm.addContactOfSearchResultList(clctvm.getNameResultsCL(), arg0);

								}else{
									clctvm.deleteNameResults();

								}
								
								/*
								 * Anschließend wird überpürft, ob auch Suchergebnisse in den Eigenschaften von Kontakten gefunden werden
								 */
								editorAdministration.getContactsOfValueSearchResult(user, searchTextBox.getText(), selectedContactList, new AsyncCallback<Vector<Contact>>() {
									
									public void onFailure(Throwable arg0) {
										Window.alert("Fehler beim Füllen des allContactsOfUser Vectors!");
									}
									
									public void onSuccess(final Vector<Contact> arg0){
										if(arg0.size() != 0){
											foundResult = true;
											//wurden Ergebnisse gefunden, erscheint eine Erfolgsmeldung
											final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Suche erfolgreich!", new ClientsideFunctions.OkButton());
											success.getOkButton().addClickHandler(new ClickHandler() {
												
												public void onClick(ClickEvent click) {
													//dem TreeViewModel wird die virtuelle Kontaktliste hinzugefügt und diese mit den entsprechenden Kontakte befüllt
													clctvm.addValueResults();
													clctvm.addContactOfSearchResultList(clctvm.getValueResultsCL(), arg0);
													success.hide();
												}
											});
											
										}else{
											if(foundResult) {
												 //Wurden in den Eigenschaften keine Ergebnisse gefunden, aber davor in den Namen, so erscheint auch die Erfolgsmeldung
												final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Suche erfolgreich!", new ClientsideFunctions.OkButton());
												success.getOkButton().addClickHandler(new ClickHandler() {
													
													public void onClick(ClickEvent click) {
														success.hide();
													}
												});
											}else {
												//wenn foundResult nach beiden Suchen noch "false" ist, kommt eine Meldung, dass keine Suchergebnisse gefunden wurden 
												final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Kein Suchergebnis.", new ClientsideFunctions.OkButton());
												failed.getOkButton().addClickHandler(new ClickHandler() {
													
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
		 * @param clctvm das referenzierte TreeViewModel
		 */
		public void setClctvm(ContactListContactTreeViewModel clctvm) {
			this.clctvm= clctvm;
			
		}

				
}
		
		


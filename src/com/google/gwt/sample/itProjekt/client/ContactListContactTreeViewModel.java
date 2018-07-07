
package com.google.gwt.sample.itProjekt.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.bo.BusinessObject;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;


import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Die ContactListContactTreeViewModel. 
 * Sie dient der Verwaltung von Kontaktlisten und den dazugehörigen Kontakten und stellt die Basis für den CellBrowser dar.
 * 
 * @author KatrinZerfass & JoshuaHill
 */
public class ContactListContactTreeViewModel implements TreeViewModel{
	
	private EditorAdministrationAsync editorAdministration = null;
	private User user = null;
	
	private ContactForm contactForm =null;
	
	/** Selektierte Kontakte oder Kontaktlisten im CellBrowser werden in selectedContactList oder selectedContact gespeichert*/

	private ContactList selectedContactList = null;
	private Contact selectedContact = null;
	
	/** Default-Kontaktliste. Taucht bei jedem Benutzer auf und beinhaltet alle seine Kontakte.*/
	private ContactList myContactsContactList = new ContactList(); 
	
	/** Im contactListDataProvider befindet sich eine Liste aller Kontaktlisten */
	private ListDataProvider<ContactList> contactListDataProvider = null;
	
	/** Der contactDataProvider vom Typ Map verbindet je eine Kontaktliste mit einem ListDataProvider von Kontakten.  */
	private Map<ContactList, ListDataProvider<Contact>> contactDataProviders = null;
	
	/** nameRestultsCL und valueResultsCL repräsentieren die 'virtuellen' Kontaktlisten der Suchergebnisse. */
	private ContactList nameResultsCL = new ContactList();
	private ContactList valueResultsCL = new ContactList();
	
	/** Die Ergebnisse der Suchanfrage werden in nameResults und valueResults gespeichert. */
	private Vector<Contact> nameResults = null;
	private Vector<Contact> valueResults = null;

	/**
	 * Die innere Klasse BusinessObjectKeyProvider
	 * Sie weißt jedem BusinessObject eine eindeutige id zu.
	 */
	private class BusinessObjectKeyProvider implements
	ProvidesKey<BusinessObject> {
	
	@Override
	public Integer getKey(BusinessObject bo) {
		if (bo == null) {
			return null;
		}
		if (bo instanceof ContactList) {
			return new Integer(bo.getId());
		} else {
			return new Integer(-bo.getId());
		}
	}
	};

	/** Eine Instanz der inneren Klasse BusinessObjectKeyProvider */
	private BusinessObjectKeyProvider boKeyProvider = null;
	
	/** Das SelectionModel */
	private SingleSelectionModel<BusinessObject> selectionModel = null;
	
	
	/**
	* Innere Klasse für die Reaktion auf Selektionsereignisse. Als Folge einer Baumknotenauswahl wird je 
	* nach Typ des Business-Objekts die "selectedContactList" bzw. der "selectedContact" gesetzt.
	*/
	private class SelectionChangeEventHandler implements SelectionChangeEvent.Handler {
	
	@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			BusinessObject selection = selectionModel.getSelectedObject();
			if (selection instanceof ContactList) {
				setSelectedContactList((ContactList) selection);
			} else if (selection instanceof Contact) {
				setSelectedContact((Contact) selection);
			}
		}
	}

		
	/**
	 * Konstruktor von ContactListContactTreeViewModel.
	 */
	public ContactListContactTreeViewModel() {
		editorAdministration= ClientsideSettings.getEditorAdministration();
		user = ClientsideSettings.getUser();
		boKeyProvider = new BusinessObjectKeyProvider();
		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		contactDataProviders = new HashMap<ContactList, ListDataProvider<Contact>>();
			
	}

	/** Getter und Setter */ 
	
	/**
	 * Setzen der Kontaktformulars
	 * 
	 * @param Kontaktformular
	 */
	
	public void setContactForm(ContactForm cf) {
		contactForm = cf;
	}
	
	
	/**
	 * Auslesen der selektierten Kontaktliste.
	 * 
	 * @return selektierte Kontaktliste  
	 */
	
	public ContactList getSelectedContactList() {
		return selectedContactList;
	}
	
	/**
	 * Kontaktliste als selektierte Kontaktliste setzen. 
	 * 
	 * @param die zu selektierende Kontaktliste
	 */

	
	public void setSelectedContactList (ContactList cl) {
		selectedContactList = cl;
		selectedContact = null;
		contactForm.setSelected(null);
		
	}
	
	/**
	 * Auslesen des selektierten Kontakts.
	 * 
	 * @return selektierter Kontakt
	 */
	
	public Contact getSelectedContact() {
		return selectedContact;
	}
	
	/**
	 * Kontakt als selektierten Kontakt setzen. 
	 * 
	 * @param der zu setzende Kontakt
	 */

	public void setSelectedContact(Contact c) {
		selectedContact = c;
		contactForm.setSelected(selectedContact);
		
	}
	
	/**
	 * Auslesen der 'Meine Kontaktliste'. 
	 * 
	 * @return meine Kontaktliste
	 */
	
	public ContactList getMyContactsContactList() {
		return this.myContactsContactList;
	}
	
    /**
     * Kontaktliste als 'Meine Kontaktliste' setzen. 
     * 
     * @param die zu setzende Kontaktliste
     */

	public void setMyContactsContactList(ContactList cl) {
		myContactsContactList = cl;

		this.setSelectedContactList(myContactsContactList);
	}
		
	
	/**
	 * Auslesen der virtuellen Kontaktliste 'Suchergebnis im Namen'
	 * 
	 * @return nameResultsCL
	 */
	
	public ContactList getNameResultsCL() {
		return nameResultsCL;
	}

	/**
	 * Setzen der virtuellen Kontaktliste 'Suchergebnis im Namen'
	 * @param nameResultsCL
	 */

	public void setNameResultsCL(ContactList nameResultsCL) {
		this.nameResultsCL = nameResultsCL;
	}

	/**
	 * Auslesen der virtuellen Kontaktliste 'Suchergebnis in den Eigenschaften'
	 * @return valueResultsCL
	 */

	public ContactList getValueResultsCL() {
		return valueResultsCL;
	}
	
	/**
	 * Setzen der virtuellen Kontaktliste 'Suchergebnis in den Eigenschaften'
	 * @param valueResultsCL
	 */
	
	public void setValueResultsCL(ContactList valueResultsCL) {
		this.valueResultsCL = valueResultsCL;
	}
	

	/**
	 * GUI Funktionalitäten
	 */
	
	
	/**
	 * Eine neu erstellte Kontaktliste wird dem contactListDataProvider hinzugefügt und selektiert.
	 * 
	 * @param die neu erstellte Kontaktliste
	 */
	public void addContactList(ContactList cl) {
		contactListDataProvider.getList().add(cl);
		selectionModel.setSelected(cl, true);
				
	}
	
	/**
	 * Beim Löschen der Kontaktliste wird die selektierte Liste aus dem contactListDataProvider entfernt und die 
	 * Verknüpfung zwischen der Kontaktliste und dem ListDataProvider aufgelöst.
	 *  
	 * @param cl die selektierte Kontaktliste
	 */
	
	public void removeContactList(ContactList cl) {
		if (contactListDataProvider.getList().contains(cl) && cl != null && !cl.equals(myContactsContactList)) {
			contactListDataProvider.getList().remove(cl);
			contactDataProviders.remove(cl);
		}
	}
	
	/**
	 * Ein bereits bestehender Kontakt wird einer Kontaktliste hinzugefügt. 
	 * @param cl Kontaktliste, zu welcher der Kontakt hinzugefügt wird  
	 * @param c	 der ausgewählte Kontakt
	 */
	
	
	public void addContactOfContactList(ContactList cl, Contact c) {
		if (!contactDataProviders.containsKey(cl)) {
			Window.alert("Fehler beim addContactofcontactlist");
			return;
		}
		ListDataProvider<Contact> contactsProvider = contactDataProviders.get(cl);
		if (!contactsProvider.getList().contains(c)) {
			contactsProvider.getList().add(c);
		}
		selectionModel.setSelected(c, true);
		
	}
	
	/**
	 * Ein selektierter Kontakt wird aus der selektierten Kontakliste gelöscht. 
	 * @param cl die selektierte Kontaktliste
	 * @param contact der selektierte Kontakt 
	 */
	
	public void removeContactOfContactList(ContactList cl, Contact contact) {
		if (!contactDataProviders.containsKey(cl)) {
			return;
		}			
		contactDataProviders.get(cl).getList().remove(contact);
		selectionModel.setSelected(cl, true);	
		
	}
	
	/**
	 * Das Kontaktobjekt im Cellbrowser wird nach Veränderungen in den Eigenschaften aktualisiert. 
	 * 
	 * @param contact der veränderte Kontakt 
	 */
	
	public void updateContact(Contact contact){							
		editorAdministration.getContactByID(contact.getId(), new AsyncCallback<Contact>() {			
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Contact result) {
				List<Contact> contactList = contactDataProviders.get(getSelectedContactList()).getList();
				for (int i=0; i<contactList.size(); i++) {
					if (result.getId() == contactList.get(i).getId()) {
						contactList.set(i, result);
						break;
					}
				}			
			}	
		});
	}
	
	
	/**
	 * Anlegen einer virtuellen Kontaktliste für die Suchergebnisse im Namen.
	 */		
	
	public void addNameResults () {			
		deleteNameResults();
		nameResultsCL = new ContactList();
		nameResultsCL.setId(0);
		nameResultsCL.setName("Suchergebnis im Namen");
		
	}
	
	/**
	 * Anlegen einer virtuellen Kontaktliste für die Suchergebnisse in den Eigenschaften.
	 */	
	
	public void addValueResults () {	
		deleteValueResults();		
		valueResultsCL = new ContactList();
		valueResultsCL.setId(1);
		valueResultsCL.setName("Suchergebnis in den Eigenschaften");
		
	}
	
	
	/**
	 * Hinzufügen von Kontakten in die virtuellen Kontaktlisten "Suchergebnis in den Eigenschaften" und 
	 * Suchergebnis im Namen".
	 * 
	 * @param cl die betroffene virtuelle Kontaktliste
	 * @param contacts die Kontakte der Suchergebnisse 
	 */
	
	
	public void addContactOfSearchResultList(ContactList cl, Vector<Contact> contacts) {
		if(cl == nameResultsCL) {
			nameResults = contacts;
			contactListDataProvider.getList().add(cl);			
		}
		if(cl == valueResultsCL) {
			valueResults = contacts;
			contactListDataProvider.getList().add(cl);
		}	
	}
	
	
	/**
	 * Löschen der virtuellen Kontaktliste "Suchergebnis im Namen".
	 */
	
	public void deleteNameResults () {
		contactListDataProvider.getList().remove(nameResultsCL);
	}
	
	/**
	 * Löschen der virtuellen Kontaktliste "Suchergebnis in den Eigenschaften".
	 */
	
	public void deleteValueResults () {
		contactListDataProvider.getList().remove(valueResultsCL);
	}
	
	

	/** 
	 * Die Methode getNodeInfo gibt für jeden Knoten im TreeViewModel dessen Kinder wieder 
	 */
	public <T> NodeInfo<?> getNodeInfo(T value) {
				
		user = ClientsideSettings.getUser();
				
		/*
		 * Beim Laden des CellBrowsers wird ein neuer ListDataProvider erzeugt und mit den 
		 * Kontaktlisten des Nutzers befüllt. 
		 */
		if(value.equals("Root")) {
			contactListDataProvider = new ListDataProvider<ContactList>();
			contactListDataProvider.getList().add(myContactsContactList);
				
			editorAdministration.getAllContactListsOfUser(user.getEmail(), new AsyncCallback<Vector<ContactList>>(){
				public void onFailure(Throwable t) {
					
				}
				
				public void onSuccess(Vector<ContactList> contactLists) {
					for (ContactList cl : contactLists) {
						contactListDataProvider.getList().add(cl);
				
					}
				}
			});
			
			return new DefaultNodeInfo<ContactList>(contactListDataProvider,
				new ContactListCell(), selectionModel, null);
			
		
		
		
		/*
		 * Wenn der Nutzer eine Kontaktliste auswählt, wird die Methode 'getNodeInfo' 
		 * aufgerufen und folgende Abfragen werden geprüft. 
		 */
				
		}else if(value instanceof ContactList) {
			final ListDataProvider<Contact> contactsProvider = new ListDataProvider<Contact>();
			contactDataProviders.put((ContactList) value, contactsProvider);
			
			
			/*
			 * Bei der angeklickten Kontaktliste handelt es sich um die "Meine Kontakte" Liste.
			 */
			
			if((ContactList) value == myContactsContactList) {
				
				//Löschen der virtuellen Kontaktlisten.  
				deleteNameResults();
				deleteValueResults();
				
				editorAdministration.getAllContactsOfActiveUser(user, new AsyncCallback<Vector<Contact>>() {
					public void onFailure (Throwable t) {
						
					}
					
					public void onSuccess (Vector<Contact> contacts) {
						for (Contact c : contacts) {
							contactsProvider.getList().add(c);							
						}
					}
				});
								
				return new DefaultNodeInfo<Contact>(contactsProvider,new ContactCell(), selectionModel, null);
				
						
				
			/* 
			 * Bei der angeklickten Kontaktliste handelt es sich um eine vom Nutzer erstellte Kontaktliste. 
			 */	
				
			}else if(user.getId() == ((ContactList) value).getOwner())  {
				
				//Löschen der virtuellen Kontaktlisten.
				deleteNameResults();
				deleteValueResults();
				
				editorAdministration.getAllContactsOfContactlistForUser((ContactList) value, user, new AsyncCallback<Vector<Contact>>() {
					public void onFailure(Throwable t) {
							Window.alert("Kontakte der Kontaktliste auslesen fehlgeschlagen");
					}
		
					public void onSuccess(Vector<Contact> contacts) {
						for (Contact c : contacts) {
								contactsProvider.getList().add(c);
						}
						contactListDataProvider.refresh();
					}
				});
					return new DefaultNodeInfo<Contact>(contactsProvider,
							new ContactCell(), selectionModel, null);
				
			
			/* 
			 * Bei der angeklickten Kontaktliste handelt es sich um eine mit dem Nutzer geteilte Kontaktliste. 
			 */
					
			}else if(user.getId() != ((ContactList) value).getOwner() && 
					(ContactList) value != nameResultsCL && (ContactList) value != valueResultsCL) {
				
				    //Löschen der virtuellen Kontaktlisten.
					deleteNameResults();
					deleteValueResults();
					
					editorAdministration.getAllSharedContactsOfContactList((ContactList) value, user, new AsyncCallback<Vector<Contact>>() {
						public void onFailure(Throwable t) {
								}
						public void onSuccess(Vector<Contact> contacts) {
							for (Contact c : contacts) {
								contactsProvider.getList().add(c);
							}
						}
					});
					
						return new DefaultNodeInfo<Contact>(contactsProvider,
								new ContactCell(), selectionModel, null);
				
					
			/*
			 * Bei der angeklickten Kontaktliste handelt es sich um die virtuelle Kontaktliste "Suchergebnis im Namen".
			 */
				    
			}else if((ContactList) value == nameResultsCL){
					
					for (Contact c : nameResults){
						contactsProvider.getList().add(c);
					}
					return new DefaultNodeInfo<Contact>(contactsProvider,
							new ContactCell(), selectionModel, null);
			
					
			/*
			 * Bei der angeklickten Kontaktliste handelt es sich um die virtuelle Kontaktliste "Suchergebnis im Namen".
			 */				
			
			}else if((ContactList) value == valueResultsCL){
			
					for (Contact c : valueResults){
						contactsProvider.getList().add(c);
					}
					return new DefaultNodeInfo<Contact>(contactsProvider,
							new ContactCell(), selectionModel, null);
			}				
		}
	return null;	
	}
	
	/**
	 * Es wird geprüft, ob es sich bei dem übergebenen Objekt um ein Kontakt Objekt handelt. 
	 *  
	 * @param das zu untersuchende Objekt
	 * @return boolean 
	 */

	public boolean isLeaf(Object value) {
		return (value instanceof Contact);
	}


	



}

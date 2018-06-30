
package com.google.gwt.sample.itProjekt.client;

import java.util.HashMap;
import java.util.Map;

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
import java.util.Vector;


/**
 * Die ContactListContactTreeViewModel. 
 * Sie dient der Verwaltung von Kontaktlisten und den dazugehörigen Kontakten und stellt die Basis für den CellBrowser dar.
 * 
 * @author KatrinZerfass
 */
public class ContactListContactTreeViewModel implements TreeViewModel{
	
	private EditorAdministrationAsync editorAdministration = null;
	private User user = null;
	
	private ContactForm contactForm =null;

	private ContactList selectedContactList = null;
	private Contact selectedContact = null;
	
	/** Default-Kontaktliste. Taucht bei jedem Benutzer auf und beinhaltet alle seine Kontakte.*/
	private ContactList myContactsContactList = new ContactList(); 
	
	/** Im contactListDataProvider befindet sich eine Liste aller Kontaktlisten */
	private ListDataProvider<ContactList> contactListDataProvider = null;
	
	/** Der contactDataProvider vom Typ Map verbindet je eine Kontaktliste mit einem ListDataProvider von Kontakten.  */
	private Map<ContactList, ListDataProvider<Contact>> contactDataProviders = null;
	
	private ContactList nameResultsCL = new ContactList();
	private ContactList valueResultsCL = new ContactList();
	private Vector<Contact> nameResults = null;
	private Vector<Contact> valueResults = null;

	/**
	 * Die innere Klasse BusinessObjectKeyProvider
	 * Sie weißt jedem BusinessObject eine eindeutige id zu.
	 * ?? Für was brauchen wir die ?? 
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
	 * Die vorher deklarierten 
	 */
	public ContactListContactTreeViewModel() {
		editorAdministration= ClientsideSettings.getEditorAdministration();
		user = ClientsideSettings.getUser();
		boKeyProvider = new BusinessObjectKeyProvider();
		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEventHandler());
		contactDataProviders = new HashMap<ContactList, ListDataProvider<Contact>>();
		
		
	}

	
	/* 
	 * Getter und Setter für ContactForm, selectedContactList, selectedContact und myContactContactList
	 */
	public void setContactForm(ContactForm cf) {
		contactForm = cf;
	}

	
	public ContactList getSelectedContactList() {
		return selectedContactList;
	}
	
	public void setSelectedContactList (ContactList cl) {
		selectedContactList = cl;
		selectedContact = null;
		contactForm.setSelected(null);
		
	}
	
	
	public Contact getSelectedContact() {
		return selectedContact;
	}

	public void setSelectedContact(Contact c) {
		selectedContact = c;
		contactForm.setSelected(selectedContact);
		
	//muss dan auch die dazugehörige Kontaktliste ausgewählt werden?! --> siehe R&T
	}

	
	public ContactList getMyContactsContactList() {
		return this.myContactsContactList;
	}

	public void setMyContactsContactList(ContactList cl) {
		myContactsContactList = cl;

		this.setSelectedContactList(myContactsContactList);
	
	}
	

	/*
	 * GUI Funktionalitäten
	 */
	
	
	/**
	 * Wir eine neue Kontaktliste erstellt, wird diese dem ListDataProvider hinzugefügt und selektiert.
	 * @param cl die neue Kontaktliste
	 */
	public void addContactList(ContactList cl) {
		contactListDataProvider.getList().add(cl);
		
		//nachfolgende Zeile steht nirgends bei R&T ?!
		//contactDataProviders.put(cl, new ListDataProvider<Contact>());
		
		selectionModel.setSelected(cl, true);
			
		
	}
	
	
	public void removeContactList(ContactList cl) {
		if (contactListDataProvider.getList().contains(cl) && cl != null && !cl.equals(myContactsContactList)) {
			contactListDataProvider.getList().remove(cl);
			contactDataProviders.remove(cl);
		}
	}
	
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
	
	
	public void addContactOfSearchResultList(ContactList cl, Vector<Contact> contacts) {
		if(cl == nameResultsCL) {
			nameResults = contacts;
			contactListDataProvider.getList().add(cl);
			//selectionModel.setSelected(contactListDataProvider.getList().get(2), true);
		}
		if(cl == valueResultsCL) {
			valueResults = contacts;
			contactListDataProvider.getList().add(cl);
			selectionModel.setSelected(cl, true);
		}
		
	}
	
	
	public void removeContactOfContactList(ContactList cl, Contact c) {
		if (!contactDataProviders.containsKey(cl)) {
			return;
		}
		contactDataProviders.get(cl).getList().remove(c);
		selectionModel.setSelected(cl, true);
	}
	
	public void addNameResults () {
		
		
		contactListDataProvider.getList().remove(nameResultsCL);
			
		nameResultsCL = new ContactList();
		// nameResultsCL.setId(1);
		nameResultsCL.setName("Suchergebnis im Namen");
		
	}
	
	public void addValueResults () {
		
		contactListDataProvider.getList().remove(valueResultsCL);
		
		valueResultsCL = new ContactList();
		valueResultsCL.setId(2);
		valueResultsCL.setName("Suchergebnis in den Eigenschaften");
		
	}
	
	

	/** 
	 * Die Methode getNodeInfo gibt für jeden Knoten im TreeViewModel dessen Kinder wieder 
	 */
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		user = ClientsideSettings.getUser();
		
		if(value.equals("Root")) {
			//evtl. hier Abfrage, ob der Provider = null ist?
			contactListDataProvider = new ListDataProvider<ContactList>();
			
			contactListDataProvider.getList().add(myContactsContactList);
		
			
			// User-Parameter muss den aktuell angemeldeten User zurückgeben
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
			
		
		}else if(value instanceof ContactList) {
			final ListDataProvider<Contact> contactsProvider = new ListDataProvider<Contact>();
			contactDataProviders.put((ContactList) value, contactsProvider);
			/*
			 * Bei der angeklickten Kontaktliste handelt es sich um die default myContactsContactList 
			 */
	
			
			if((ContactList) value == myContactsContactList) {
				editorAdministration.getAllContactsOfActiveUser(user, new AsyncCallback<Vector<Contact>>() {
					public void onFailure (Throwable t) {
						
					}
					
					public void onSuccess (Vector<Contact> contacts) {
						for (Contact c : contacts) {
							contactsProvider.getList().add(c);
						}
					}
				});
				return new DefaultNodeInfo<Contact>(contactsProvider,
						new ContactCell(), selectionModel, null);
				
			/* 
			 * Der Nutzer ist Eigentümer der Kontakliste 
			 */	
				
			}else if(user.getId() == ((ContactList) value).getOwner())  {
				editorAdministration.getAllContactsOf((ContactList) value, new AsyncCallback<Vector<Contact>>() {
					public void onFailure(Throwable t) {
							Window.alert("Kontakte der Kontaktliste auslesen fehlgeschlagen");
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
			 * Ergebnis der Kontaktsuche
			 */
		
		    
			}else if((ContactList) value == nameResultsCL){
					
					for (Contact c : nameResults){
						contactsProvider.getList().add(c);
					}
					return new DefaultNodeInfo<Contact>(contactsProvider,
							new ContactCell(), selectionModel, null);
					
				
			
			}else if((ContactList) value == valueResultsCL){
			
					for (Contact c : valueResults){
						contactsProvider.getList().add(c);
					}
					return new DefaultNodeInfo<Contact>(contactsProvider,
							new ContactCell(), selectionModel, null);
				
			  /*
		       * Der Nutzer ist nur Teilhaber der Kontaktliste 
			   */
								
			}else {
					Window.alert("geteilte Kontakliste");
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
			}									
								
		}
		return null;
	}
	

	

	public boolean isLeaf(Object value) {
		return (value instanceof Contact);
	}


	public ContactList getNameResultsCL() {
		return nameResultsCL;
	}


	public void setNameResultsCL(ContactList nameResultsCL) {
		this.nameResultsCL = nameResultsCL;
	}


	public ContactList getValueResultsCL() {
		return valueResultsCL;
	}


	public void setValueResultsCL(ContactList valueResultsCL) {
		this.valueResultsCL = valueResultsCL;
	}

	
	


}

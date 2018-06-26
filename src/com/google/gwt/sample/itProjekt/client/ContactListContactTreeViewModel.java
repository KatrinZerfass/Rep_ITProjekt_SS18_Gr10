
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
		//Check
		Window.alert("2. " + selectedContactList.getName() + " als selectedContactList des clctvm");
	}
	
	
	public Contact getSelectedContact() {
		return selectedContact;
	}

	public void setSelectedContact(Contact c) {
		selectedContact = c;
		contactForm.setSelected(c);
		
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
		//Check
		Window.alert("4. " + contactListDataProvider.getList().get(1).getName() + " im contactListDataProvider");
		
		
	}
	
	
	public void removeContactList(ContactList cl) {
		if (contactListDataProvider.getList().contains(cl) && cl != null && !cl.equals(myContactsContactList)) {
			contactListDataProvider.getList().remove(cl);
			contactDataProviders.remove(cl);
		}
	}
	
	
	public void addContactOfContactList(ContactList cl, Contact c) {
		if (!contactDataProviders.containsKey(cl)) {
			return;
		}
		ListDataProvider<Contact> contactsProvider = contactDataProviders.get(cl);
		if (!contactsProvider.getList().contains(c)) {
			contactsProvider.getList().add(c);
		}
		selectionModel.setSelected(c, true);
		
	}
	
	
	public void removeContactOfContactList(ContactList cl, Contact c) {
		if (!contactDataProviders.containsKey(cl)) {
			return;
		}
		contactDataProviders.get(cl).getList().remove(c);
		selectionModel.setSelected(cl, true);
	}
	
	

	/** 
	 * Die Methode getNodeInfo gibt für jeden Knoten im TreeViewModel dessen Kinder wieder 
	 */
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if(value.equals("Root")) {
			//evtl. hier Abfrage, ob der Provider = null ist?
			contactListDataProvider = new ListDataProvider<ContactList>();
			Window.alert("Geht in NodeInfo rein");
			
			contactListDataProvider.getList().add(myContactsContactList);
			
			// User-Parameter muss den aktuell angemeldeten User zurückgeben
			editorAdministration.getAllContactListsOfUser(ClientsideSettings.getUser().getEmail(), new AsyncCallback<Vector<ContactList>>(){
				public void onFailure(Throwable t) {
					
				}
				
				public void onSuccess(Vector<ContactList> contactLists) {
					for (ContactList cl : contactLists) {
						contactListDataProvider.getList().add(cl);
						Window.alert("Alle Kontaktlisten des Nutzers ausgelesen");
						
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
			Window.alert("erkennt Kontakliste");
			Window.alert(((Integer)((ContactList) value).getOwner()).toString());
			
			if((ContactList) value == myContactsContactList) {
				Window.alert("meine Kontakte");
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
			 * Bei der angeklickten Kontaktliste handelt es sich um eine manuell erstellte Kontaktliste
			 */
			}else {
				Window.alert("springt in else");
				/*
				 * Der Nutzer ist Eigentümer der Kontaktliste
				 */
				if(user.getId() == ((ContactList) value).getOwner()) {
					Window.alert("meine Kontaktliste");
					
					editorAdministration.getAllContactsOf((ContactList) value, new AsyncCallback<Vector<Contact>>() {
							public void onFailure(Throwable t) {
								Window.alert("Kontakte der Kontaktliste auslesen fehlgeschlagen");
							}
		
							
							public void onSuccess(Vector<Contact> contacts) {
								for (Contact c : contacts) {
									contactsProvider.getList().add(c);
									Window.alert("Kontakte der Kontaktliste auslesen erfolgreich");
								}
							}
						});
		
				/*
				 * Der Nutzer ist nur Teilhaber der Kontaktliste 
				 */
				}
				else {
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
				}
				
						
			}
			Window.alert("läuft if else durch");
			Window.alert(contactsProvider.getList().get(0).getFirstname());
			return new DefaultNodeInfo<Contact>(contactsProvider,
			new ContactCell(), selectionModel, null);	
			
		}
		return null;
	}
	

	

	public boolean isLeaf(Object value) {
		return (value instanceof Contact);
	}

	
	


}

package com.google.gwt.sample.itProjekt.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.bo.BusinessObject;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Vector;





public class ContactListContactTreeViewModel implements TreeViewModel{
	
	
	private ContactForm contactForm =null;
	
	private ContactList selectedContactList = null;
	private Contact selectedContact = null;
	
	private EditorAdministrationAsync editorAdministration = null;

	private ListDataProvider<ContactList> contactListDataProvider = null;
	
	
	
	//verbindet eine Liste von Kontakten mit einer Kontaktliste
	private Map<ContactList, ListDataProvider<Contact>> contactDataProviders = null;

	//wei�t jedem BO im Tree eine id zu
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

	private BusinessObjectKeyProvider boKeyProvider = null;
	private SingleSelectionModel<BusinessObject> selectionModel = null;
	
	/**
	* Nested Class für die Reaktion auf Selektionsereignisse. Als Folge einer
	* Baumknotenauswahl wird je nach Typ des Business-Objekts der
	* "selectedCustomer" bzw. das "selectedAccount" gesetzt.
	*/
	private class SelectionChangeEventHandler implements
		SelectionChangeEvent.Handler {
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

	
	//Konstruktor
	public ContactListContactTreeViewModel() {
		editorAdministration= ClientsideSettings.getEditorAdministration();
		boKeyProvider = new BusinessObjectKeyProvider();
		selectionModel = new SingleSelectionModel<BusinessObject>(boKeyProvider);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEventHandler());
		contactDataProviders = new HashMap<ContactList, ListDataProvider<Contact>>();
		
		
	}

	//getter und setter
	public void setContactForm(ContactForm cf) {
		contactForm = cf;
	}

	ContactList getSelectedContactList() {
		return selectedContactList;
	}

	void setSelectedContactList (ContactList cl) {
		selectedContactList = cl;
		selectedContact = null;
		contactForm.setSelected(null);
	}

	
	Contact getSelectedContact() {
		return selectedContact;
	}


	void setSelectedContact(Contact c) {
		selectedContact = c;
		contactForm.setSelected(c);
	}

	

	//GUI Funktionen
	
	
	void addContactList(ContactList cl) {
		contactListDataProvider.getList().add(cl);
		selectionModel.setSelected(cl, true);
	}
	
	void removeContactList(ContactList cl) {
		contactListDataProvider.getList().remove(cl);
		contactDataProviders.remove(cl);
	}
	
	void addContactOfContactList(ContactList cl, Contact c) {
		if (!contactDataProviders.containsKey(cl)) {
			return;
		}
		ListDataProvider<Contact> contactsProvider = contactDataProviders.get(cl);
		if (!contactsProvider.getList().contains(c)) {
			contactsProvider.getList().add(c);
		}
		selectionModel.setSelected(c, true);
	}
	
	void removeContactOfContactList(ContactList cl, Contact c) {
		if (!contactDataProviders.containsKey(cl)) {
			return;
		}
		contactDataProviders.get(cl).getList().remove(c);
		selectionModel.setSelected(cl, true);
	}
	
	

	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if(value.equals("Root")) {
			contactListDataProvider = new ListDataProvider<ContactList>();
			
			// User-Parameter muss den aktuell angemeldeten User zurückgeben
			editorAdministration.getAllContactListsOf(new User(), new AsyncCallback<Vector<ContactList>>(){
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
		
		}if(value instanceof ContactList) {
			// Erzeugen eines ListDataproviders für Account-Daten
			final ListDataProvider<Contact> contactsProvider = new ListDataProvider<Contact>();
			contactDataProviders.put((ContactList) value, contactsProvider);
	
			editorAdministration.getAllContactsOf((ContactList) value,
					new AsyncCallback<Vector<Contact>>() {
						@Override
						public void onFailure(Throwable t) {
						}
	
						@Override
						public void onSuccess(Vector<Contact> contacts) {
							for (Contact c : contacts) {
								contactsProvider.getList().add(c);
							}
						}
					});
	
			// Return a node info that pairs the data with a cell.
					return new DefaultNodeInfo<Contact>(contactsProvider,
							new ContactCell(), selectionModel, null);
		}
		return null;
	}
	

	
	public boolean isLeaf(Object value) {
		return (value instanceof Contact);
	}

	
	


}

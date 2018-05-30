package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EditorAdministrationAsync {

	void addContactToContactList(ContactList contactlist, Contact contact, AsyncCallback<ContactList> callback);

	void createContact(User user, String firstname, String lastname, String sex, AsyncCallback<Contact> callback);

	void createContactList(String name, AsyncCallback<ContactList> callback);

	void createUser(String firstname, String lastname, String sex, String email, AsyncCallback<User> callback);

	void createValue(Contact contact, Property property, String content, AsyncCallback<Contact> callback);

	void deleteContact(int id, AsyncCallback<Void> callback);

	void deleteContactList(ContactList contactlist, AsyncCallback<Void> callback);

	void deleteValue(Value value, AsyncCallback<Void> callback);

	void editContact(int id, String firstname, String lastname, String sex, AsyncCallback<Contact> callback);

	void editContactList(int id, String name, AsyncCallback<ContactList> callback);

	void editValue(Contact contact, Property property, Value value, String content, boolean isshared,
			AsyncCallback<Contact> callback);

	void getAllContactListsOf(User user, AsyncCallback<Vector<ContactList>> callback);

	void getAllContactsOf(ContactList contactlist, AsyncCallback<Vector<Contact>> callback);

	void getAllContactsWith(Value value, AsyncCallback<Vector<Contact>> callback);

	void getAllOwnedContactsOf(User user, AsyncCallback<Vector<Contact>> callback);

	void getAllSharedContactsWith(User user, AsyncCallback<Vector<Contact>> callback);

	void getContact(int id, AsyncCallback<Contact> callback);

	void init(AsyncCallback<Void> callback);

	void removeContactFromContactList(ContactList contactlist, Contact contact, AsyncCallback<ContactList> callback);

	void shareContact(Contact contact, User user, AsyncCallback<Permission> callback);

	void shareContactList(ContactList contactlist, User user, AsyncCallback<Permission> callback);

}

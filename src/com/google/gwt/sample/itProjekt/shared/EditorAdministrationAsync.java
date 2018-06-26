package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.BusinessObject;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EditorAdministrationAsync {

	void addContactToContactList(ContactList contactlist, Contact contact, AsyncCallback<ContactList> callback);

	void createContact(String firstname, String lastname, String sex, User user, AsyncCallback<Contact> callback);

	void createContactList(String name, User user, AsyncCallback<ContactList> callback);

	void createUser(String email, AsyncCallback<User> callback);

	void createValue(Contact contact, int propertyid, String content, AsyncCallback<Value> callback);

	void deleteContact(int id, AsyncCallback<Void> callback);

	void deleteContactList(ContactList contactlist, AsyncCallback<Void> callback);

	void deleteValue(Value value, AsyncCallback<Void> callback);

	void editContact(int id, String firstname, String lastname, String sex, AsyncCallback<Contact> callback);

	void editContactList(int id, String name, AsyncCallback<ContactList> callback);

	void editValue(Contact contact, int propertyId, Value value, String content, boolean isshared,
			AsyncCallback<Value> callback);

	void getAllContactListsOf(String email, AsyncCallback<Vector<ContactList>> callback);

	void getAllContactsOf(ContactList contactlist, User user, AsyncCallback<Vector<Contact>> callback);

	void getAllContactsOfActiveUser(User user, AsyncCallback<Vector<Contact>> callback);

	void getAllContactsWith(Value value, AsyncCallback<Vector<Contact>> callback);

	void getAllOwnedContactsOf(String email, AsyncCallback<Vector<Contact>> callback);

	void getAllSharedContactsWith(String email, AsyncCallback<Vector<Contact>> callback);

	void getContact(int id, AsyncCallback<Contact> callback);

	void getUserInformation(String email, AsyncCallback<User> callback);

	void init(AsyncCallback<Void> callback);

	void removeContactFromContactList(ContactList contactlist, Contact contact, AsyncCallback<ContactList> callback);

	void shareContact(Contact contact, String email, AsyncCallback<Permission> callback);

	void shareContactList(ContactList contactlist, String email, AsyncCallback<Permission> callback);

	void getAllContactListsOfActiveUser(User user, AsyncCallback<Vector<ContactList>> callback);

	void getAllValuesOf(Contact contact, AsyncCallback<Vector<Value>> callback);

	void getAllContactListsWith(Contact contact, AsyncCallback<Vector<ContactList>> callback);

	void deletePermission(User user, BusinessObject bo, AsyncCallback<Void> callback);

	void getAllContactsBy(String content, AsyncCallback<Vector<Contact>> callback);

	void getAllContactsWith(String name, AsyncCallback<Vector<Contact>> callback);

	void getPropertyOfValue(Value value, AsyncCallback<Property> callback);

	void getAllSharedValuesOfContact(Contact contact, AsyncCallback<Vector<Value>> callback);

	void getAllPredefinedPropertiesOf(AsyncCallback<Vector<Property>> callback);

	void createProperty(Contact contact, String type, AsyncCallback<Property> callback);

	void getAllUsers(AsyncCallback<Vector<User>> callback);

	void getPropertyByType(String type, AsyncCallback<Property> callback);

	void getAllSharedContactsOfContactList(ContactList contactlist, User user, AsyncCallback<Vector<Contact>> callback);
}

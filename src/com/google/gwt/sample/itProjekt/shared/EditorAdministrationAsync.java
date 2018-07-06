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

	void deleteContact(Contact contact, boolean owner, User user, AsyncCallback<Void> callback);

	void deleteContactList(ContactList contactlist, boolean owner, User user, AsyncCallback<Void> callback);

	void deleteValue(Value value, AsyncCallback<Void> callback);

	void editContact(int id, String firstname, String lastname, String sex, AsyncCallback<Contact> callback);

	void editContactList(int id, String name, AsyncCallback<ContactList> callback);

	void editValue(Contact contact, int propertyId, Value value, String content, boolean isshared,
			AsyncCallback<Value> callback);

	void getAllContactListsOfUser(String email, AsyncCallback<Vector<ContactList>> callback);

	void getAllContactsOfContactList(ContactList contactlist, AsyncCallback<Vector<Contact>> callback);

	void getAllContactsOfActiveUser(User user, AsyncCallback<Vector<Contact>> callback);

	void getAllOwnedContactsOfUser(String email, AsyncCallback<Vector<Contact>> callback);

	void getAllSharedContactsWithUser(String email, AsyncCallback<Vector<Contact>> callback);

	void getContactByID(int id, AsyncCallback<Contact> callback);

	void isUserKnown(String email, AsyncCallback<Boolean> callback);

	void init(AsyncCallback<Void> callback);

	void removeContactFromContactList(ContactList contactlist, Contact contact, AsyncCallback<ContactList> callback);

	void shareContact(User sourceUser, String shareUserEmail, Contact shareContact, AsyncCallback<Permission> callback);

	void shareContactList(User sourceUser, String shareUserEmail, ContactList shareContactList,
			AsyncCallback<Permission> callback);

	void getAllOwnedContactListsOfActiveUser(User user, AsyncCallback<Vector<ContactList>> callback);

	void getAllValuesOfContact(Contact contact, AsyncCallback<Vector<Value>> callback);

	void getAllContactListsWithContact(Contact contact, AsyncCallback<Vector<ContactList>> callback);

	void deletePermission(User user, BusinessObject bo, AsyncCallback<Void> callback);

	void getAllContactsWithValue(String content, AsyncCallback<Vector<Contact>> callback);

	void getAllContactsWithName(String name, AsyncCallback<Vector<Contact>> callback);

	void getPropertyOfValue(Value value, AsyncCallback<Property> callback);

	void getAllSharedValuesOfContact(Contact contact, AsyncCallback<Vector<Value>> callback);

	void getAllPredefinedPropertiesOf(AsyncCallback<Vector<Property>> callback);

	void createProperty(Contact contact, String type, AsyncCallback<Property> callback);

	void getAllUsers(AsyncCallback<Vector<User>> callback);

	void getPropertyByType(String type, Contact contact, AsyncCallback<Property> callback);

	void getAllSharedContactsOfContactList(ContactList contactlist, User user, AsyncCallback<Vector<Contact>> callback);

	void getUserByEmail(String email, AsyncCallback<User> callback);

	void getAllContactsOfUserWithValue(User user, Value value, AsyncCallback<Vector<Contact>> callback);

	void getContactsOfUserWithProperty(User user, Property Property, AsyncCallback<Vector<Contact>> callback);

	void getContactsOfNameSearchResult(User user, String textBox, ContactList selectedContactList,
			AsyncCallback<Vector<Contact>> callback);

	void getContactsOfValueSearchResult(User user, String textBox, ContactList selectedContactList,
			AsyncCallback<Vector<Contact>> callback);

	void getAllSharedContactsOfUserWithOtherUser(User source, String receiver, AsyncCallback<Vector<Contact>> callback);

	void getUserByID(int ID, AsyncCallback<User> callback);

	void getSourceToSharedContact(Contact contact, User receivingUser, AsyncCallback<User> callback);

	void getAllPermissions(AsyncCallback<Vector<Permission>> callback);

	void getAllContactsOfContactlistForUser(ContactList contactlist, User user,
			AsyncCallback<Vector<Contact>> callback);

	void getOwnerOfContact(Contact c, AsyncCallback<User> callback);

	void createAddress(String street, String housenumber, String zip, String city, Contact contact,
			AsyncCallback<Value> callback);

	void createUserContact(String firstname, String lastname, String sex, String email,
			AsyncCallback<User> callback);

	void getAllParticipantsOfContact(Contact contact, AsyncCallback<Vector<User>> callback);

	void getAllUserSuggestions(User activeUser, AsyncCallback<Vector<String>> callback);

	void getFullNamesOfUsers(Vector<User> user, AsyncCallback<Vector<String>> callback);

	void getContactsOfUserWithDefaultProperty(User user, Property property, AsyncCallback<Vector<Contact>> callback);

	void editProperty(Property property, AsyncCallback<Property> callback);

	void deleteProperty(Property property, AsyncCallback<Void> callback);

	void deleteUser(User u, AsyncCallback<Void> callback);

	void editUser(User user, AsyncCallback<User> callback);
}


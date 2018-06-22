package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;
import com.google.gwt.sample.itProjekt.shared.bo.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("editoradministration")
public interface EditorAdministration extends RemoteService{
	
	public void init() throws IllegalArgumentException;
	
	public User getUserInformation (String email) throws IllegalArgumentException;
	
	public User createUser(String email) throws IllegalArgumentException;
	
	public Vector<Contact> getAllContactsOfActiveUser(User user) throws IllegalArgumentException;
	
	public Vector<ContactList> getAllContactListsOfActiveUser(User user) throws IllegalArgumentException;
	
	public Vector<ContactList> getAllContactListsOf(String email) throws IllegalArgumentException;
	
	public Vector<Contact> getAllOwnedContactsOf(String email) throws IllegalArgumentException;
	
	public Vector<Contact> getAllSharedContactsWith(String email) throws IllegalArgumentException;
	
	public Vector<Contact> getAllContactsOf(ContactList contactlist, User user) throws IllegalArgumentException;
	
	public Vector<Contact> getAllContactsWith(Value value) throws IllegalArgumentException;
	
	public Vector<ContactList> getAllContactListsWith(Contact contact) throws IllegalArgumentException;
	
	public Contact getContact(int id) throws IllegalArgumentException;
	
	public Contact createContact(String firstname, String lastname, String sex, User user) throws IllegalArgumentException;
	
	public Contact editContact(int id, String firstname, String lastname, String sex) throws IllegalArgumentException;
	
	public Permission shareContact(Contact contact, String email) throws IllegalArgumentException;
	
	public void deleteContact(int id) throws IllegalArgumentException;
	
	public ContactList createContactList(String name, User user) throws IllegalArgumentException;
	
	public ContactList editContactList(int id, String name) throws IllegalArgumentException;
	
	public ContactList addContactToContactList(ContactList contactlist, Contact contact) throws IllegalArgumentException;
	
	public Permission shareContactList(ContactList contactlist, String email) throws IllegalArgumentException;
	
	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact) throws IllegalArgumentException;
	
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException;
	
	public Value createValue(Contact contact, int propertyid, String content) throws IllegalArgumentException;
	
	public Vector<Value> getAllValuesOf(Contact contact) throws IllegalArgumentException;
	
	Value editValue(Contact contact, int propertyId, Value value, String content, boolean isshared);
	
	public void deleteValue(Value value) throws IllegalArgumentException;
	
	public void deletePermission(User user, BusinessObject bo) throws IllegalArgumentException;
	
	public Vector<Contact> getAllContactsBy(String content) throws IllegalArgumentException;
	
	public Vector<Contact> getAllContactsWith(String name) throws IllegalArgumentException;
	
	public Property getPropertyOfValue(Value value) throws IllegalArgumentException;
	
	public Vector<Value> getAllSharedValuesOfContact(Contact contact) throws IllegalArgumentException;
	
	public Vector<Property> getAllPredefinedPropertiesOf() throws IllegalArgumentException;
	
	public Property createProperty(Contact contact, String type) throws IllegalArgumentException;
	
	public Property getPropertyByType(String type) throws IllegalArgumentException;
	
	public Vector<User> getAllUsers() throws IllegalArgumentException;
}

package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;
import com.google.gwt.sample.itProjekt.shared.bo.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("editoradministration")
public interface EditorAdministration extends RemoteService{
	
	public void init() throws IllegalArgumentException;
	
	public User getUser() throws IllegalArgumentException;
	
	public void setUser(User u) throws IllegalArgumentException;
	
	public User getUserInformation (String email) throws IllegalArgumentException;
	
	public User createUser(String email) throws IllegalArgumentException;
	
	public Vector<Contact> getAllContactsOfActiveUser() throws IllegalArgumentException;
	
	public Vector<ContactList> getAllContactListsOfActiveUser() throws IllegalArgumentException;
	
	public Vector<ContactList> getAllContactListsOf(String email) throws IllegalArgumentException;
	
	public Vector<Contact> getAllOwnedContactsOf(String email) throws IllegalArgumentException;
	
	public Vector<Contact> getAllSharedContactsWith(String email) throws IllegalArgumentException;
	
	public Vector<Contact> getAllContactsOf(ContactList contactlist) throws IllegalArgumentException;
	
	public Vector<Contact> getAllContactsWith(Value value) throws IllegalArgumentException;
	
	public Contact getContact(int id) throws IllegalArgumentException;
	
	public Contact createContact(String firstname, String lastname, String sex) throws IllegalArgumentException;
	
	public Contact editContact(int id, String firstname, String lastname, String sex) throws IllegalArgumentException;
	
	public Permission shareContact(Contact contact, String email) throws IllegalArgumentException;
	
	public void deleteContact(int id) throws IllegalArgumentException;
	
	public ContactList createContactList(String name) throws IllegalArgumentException;
	
	public ContactList editContactList(int id, String name) throws IllegalArgumentException;
	
	public ContactList addContactToContactList(ContactList contactlist, Contact contact) throws IllegalArgumentException;
	
	public Permission shareContactList(ContactList contactlist, User user) throws IllegalArgumentException;
	
	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact) throws IllegalArgumentException;
	
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException;
	
	public Value createValue(Contact contact, Property property, String content) throws IllegalArgumentException;
	
	public Value editValue(Contact contact, Property property, Value value, String content, boolean isshared) throws IllegalArgumentException;
	
	public void deleteValue(Value value) throws IllegalArgumentException;
	
}

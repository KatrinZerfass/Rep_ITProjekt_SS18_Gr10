package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;
import com.google.gwt.sample.itProjekt.shared.bo.*;

public interface EditorAdministration extends RemoteService{
	
	public void init() throws IllegalArgumentException;
	
	public Vector<ContactList> showAllContactListsOf(User user) throws IllegalArgumentException;
	
	public Vector<Contact> showAllContactsOf(User user) throws IllegalArgumentException;
	
	public Vector<Contact> showAllContactsOf(ContactList contactlist) throws IllegalArgumentException;
	
	public Contact createContact(String firstname, String lastname, String sex) throws IllegalArgumentException;
	
	public Contact editContact(String firstname, String lastname, String sex) throws IllegalArgumentException;
	
	public Permission shareContact(Contact contact, User user) throws IllegalArgumentException;
	
	public void deleteContact(Contact contact) throws IllegalArgumentException;
	
	public ContactList createContactList(String name) throws IllegalArgumentException;
	
	public ContactList editContactList(String name) throws IllegalArgumentException;
	
	public ContactList addContactToContactList(ContactList contactlist, Contact contact) throws IllegalArgumentException;
	
	public Permission shareContactList(ContactList contactlist, User user) throws IllegalArgumentException;
	
	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact) throws IllegalArgumentException;
	
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException;
	
	public Contact createValue(Contact contact, Property property, String content) throws IllegalArgumentException;
	
	public Contact editValue(Contact contact, Property property, Value value, String content) throws IllegalArgumentException;
	
	public void deleteValue(Value value) throws IllegalArgumentException;
}

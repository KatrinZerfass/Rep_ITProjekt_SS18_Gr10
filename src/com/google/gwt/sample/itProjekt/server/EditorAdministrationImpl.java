package com.google.gwt.sample.itProjekt.server;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.*;
import com.google.gwt.sample.itProjekt.shared.bo.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.sample.itProjekt.server.db.*;

public class EditorAdministrationImpl extends RemoteServiceServlet implements EditorAdministration{
	
	/**
	 * 
	 */

	private ContactListMapper clMapper;
	private ContactMapper cMapper;
	private PropertyMapper pMapper;
	private UserMapper uMapper;
	private ValueMapper vMapper;

	@Override
	public void init() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		this.clMapper = ContactListMapper.contactListMapper();
		this.cMapper = ContactMapper.contactMapper();
		this.pMapper = PropertyMapper.propertyMapper();
		this.uMapper = UserMapper.userMapper();
		
	}

	@Override
	public Vector<ContactList> showAllContactListsOf(User user) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Contact> showAllContactsOf(User user) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Contact> showAllContactsOf(ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact createContact(String firstname, String lastname, String sex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact editContact(String firstname, String lastname, String sex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Permission shareContact(Contact contact, User user) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteContact(Contact contact) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContactList createContactList(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContactList editContactList(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContactList addContactToContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Permission shareContactList(ContactList contactlist, User user) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Contact createValue(Contact contact, Property property, String content) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact editValue(Contact contact, Property property, Value value, String content)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteValue(Value value) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}

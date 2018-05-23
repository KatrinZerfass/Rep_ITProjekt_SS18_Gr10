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
		Vector<Contact> resultcontacts = new Vector<Contact>();
		resultcontacts = cMapper.findAllByUID(user.getId());
		return resultcontacts;
	}

	@Override
	public Vector<Contact> showAllContactsOf(ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Contact showContact(int id) {
		return cMapper.findByID(id);
	}

	@Override
	public Contact createContact(String firstname, String lastname, String sex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Contact newcontact = new Contact();
		newcontact.setFirstname(firstname);
		newcontact.setLastname(lastname);
		newcontact.setSex(sex);
		
		return cMapper.insert(newcontact);
	}

	@Override
	public Contact editContact(int id, String firstname, String lastname, String sex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Contact changedcontact = new Contact();
		changedcontact.setFirstname(firstname);
		changedcontact.setLastname(lastname);
		changedcontact.setSex(sex);
		changedcontact.setId(id);

		return cMapper.update(changedcontact);
	}

	@Override
	public Permission shareContact(Contact contact, User user) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Permission newpermission = new Permission();
		
		// TODO insert Permission
		return null;
	}

	@Override
	public void deleteContact(int id) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Contact deletedcontact = new Contact();
		deletedcontact.setId(id);
		
		cMapper.delete(deletedcontact);
	}

	@Override
	public ContactList createContactList(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		ContactList newcontactlist = new ContactList();
		newcontactlist.setName(name);
		
		// TODO insert
		return null;
	}

	@Override
	public ContactList editContactList(int id, String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		ContactList changedcontactlist = new ContactList();
		changedcontactlist.setId(id);
		changedcontactlist.setName(name);
		
		// TODO update
		return null;
	}

	@Override
	public ContactList addContactToContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		// TODO addContact
		return null;
	}

	@Override
	public Permission shareContactList(ContactList contactlist, User user) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		// TODO insert Permission
		return null;
	}

	@Override
	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		// TODO removeContact
		return null;
	}

	@Override
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		// TODO delete
	}

	@Override
	public Contact createValue(Contact contact, Property property, String content) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		// TODO insert
		return null;
	}

	@Override
	public Contact editValue(Contact contact, Property property, Value value, String content, boolean isshared)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteValue(Value value) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}

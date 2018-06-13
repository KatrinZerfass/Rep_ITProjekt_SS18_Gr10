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

	private User user = null;
	
	private ContactListMapper clMapper;
	private ContactMapper cMapper;
	private PropertyMapper pMapper;
	private PermissionMapper pmMapper;
	private UserMapper uMapper;
	private ValueMapper vMapper;

	@Override
	public void init() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		this.clMapper = ContactListMapper.contactListMapper();
		this.cMapper = ContactMapper.contactMapper();
		this.pMapper = PropertyMapper.propertyMapper();
		this.pmMapper = PermissionMapper.permissionMapper();
		this.uMapper = UserMapper.userMapper();
		this.vMapper = ValueMapper.valueMapper();
		
	}
	
	@Override
	public User getUser() throws IllegalArgumentException {

		return this.user;
	}

	@Override
	public void setUser(User u) throws IllegalArgumentException {
		
		this.user = u;
	}

	public User getUserInformation (String email) throws IllegalArgumentException{
		
		return uMapper.findByEMail(email);
	}
	
	@Override
	public User createUser(String email)
			throws IllegalArgumentException {

		User newuser = new User();
		newuser.setEmail(email);
		
		return uMapper.insert(newuser);
	}
	
	public Vector<Contact> getAllContactsOfActiveUser() throws IllegalArgumentException {
		
		Vector<Contact> result = cMapper.findAllByUID(this.user);
		result.addAll(pmMapper.getAllContactsByUID(this.user));
		
		return result;
	}
	
	public Vector<ContactList> getAllContactListsOfActiveUser() throws IllegalArgumentException {
		
		return clMapper.findAllByUID(this.user);
	}

	@Override
	public Vector<ContactList> getAllContactListsOf(String email) throws IllegalArgumentException {

		return clMapper.findAllByUID(uMapper.findByEMail(email));
	}

	@Override
	public Vector<Contact> getAllOwnedContactsOf(String email) throws IllegalArgumentException {

		Vector<Contact> resultcontacts = new Vector<Contact>();
		resultcontacts = cMapper.findAllByUID(uMapper.findByEMail(email));
		return resultcontacts;
	}

	@Override
	public Vector<Contact> getAllSharedContactsWith(String email) throws IllegalArgumentException {

		return pmMapper.getAllContactsByUID(uMapper.findByEMail(email));
	}

	@Override
	public Vector<Contact> getAllContactsOf(ContactList contactlist) throws IllegalArgumentException {

		return clMapper.getAllContacts(contactlist);
	}

	@Override
	public Vector<Contact> getAllContactsWith(Value value) throws IllegalArgumentException {
		
		return vMapper.findAllContactsByValue(value);
	}

	@Override
	public Contact getContact(int id) throws IllegalArgumentException {

		Contact contact = new Contact();
		contact.setId(id);
		return cMapper.findByID(contact);
	}


	@Override
	public Contact createContact(String firstname, String lastname, String sex) throws IllegalArgumentException {

		Contact newcontact = new Contact();
		newcontact.setFirstname(firstname);
		newcontact.setLastname(lastname);
		newcontact.setSex(sex);
		
		return cMapper.insert(newcontact, this.user);
	}

	@Override
	public Contact editContact(int id, String firstname, String lastname, String sex) throws IllegalArgumentException {
		
		Contact changedcontact = new Contact();
		changedcontact.setFirstname(firstname);
		changedcontact.setLastname(lastname);
		changedcontact.setSex(sex);
		changedcontact.setId(id);

		return cMapper.update(changedcontact);
	}

	@Override
	public Permission shareContact(Contact contact, String email) throws IllegalArgumentException {
		
		Permission newpermission = new Permission();
		newpermission.setParticipant(uMapper.findByEMail(email));
		newpermission.setIsowner(false);
		newpermission.setShareableobject(contact);
		
		return pmMapper.shareContact(newpermission);
	}

	@Override
	public void deleteContact(int id) throws IllegalArgumentException {
		
		Contact deletedcontact = new Contact();
		deletedcontact.setId(id);
		
		cMapper.delete(deletedcontact);
	}

	@Override
	public ContactList createContactList(String name) throws IllegalArgumentException {

		ContactList newcontactlist = new ContactList();
		newcontactlist.setName(name);
		
		return clMapper.insert(newcontactlist, this.user);
	}

	@Override
	public ContactList editContactList(int id, String name) throws IllegalArgumentException {

		ContactList changedcontactlist = new ContactList();
		changedcontactlist.setId(id);
		changedcontactlist.setName(name);
		
		return clMapper.update(changedcontactlist);
	}

	@Override
	public ContactList addContactToContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {
		
		return clMapper.addContact(contactlist, contact);
	}

	@Override
	public Permission shareContactList(ContactList contactlist, String email) throws IllegalArgumentException {
		
		Permission newpermission = new Permission();
		User user = new User();
		user = getUserInformation(email);
		newpermission.setParticipant(user);
		newpermission.setIsowner(false);
		newpermission.setShareableobject(contactlist);
		
		return pmMapper.shareContactList(newpermission);
	}

	@Override
	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {
		
		return clMapper.removeContact(contactlist, contact);
	}

	@Override
	public void deleteContactList(ContactList contactlist) throws IllegalArgumentException {
		
		clMapper.delete(contactlist);
	}

	@Override
	public Value createValue(Contact contact, Property property, String content) throws IllegalArgumentException {
		
		Value newvalue = new Value();
		newvalue.setContent(content);
		
		return vMapper.insert(newvalue, contact, property);
	}

	@Override
	public Value editValue(Contact contact, int propertyId, Value value, String content, boolean isshared)
			throws IllegalArgumentException {
		
		Value newvalue = new Value();
		newvalue = value;
		newvalue.setIsShared(isshared);
		newvalue.setContent(content);
		Property property = new Property();
		property.setId(propertyId);
		
		return vMapper.update(newvalue, contact, property);
	}

	@Override
	public void deleteValue(Value value) throws IllegalArgumentException {
		
		vMapper.delete(value);
	}

	@Override
	public Vector<Value> getAllValuesOf(Contact contact) throws IllegalArgumentException {
		
		return vMapper.getAllValueByCID(contact);
	}

	@Override
	public Vector<ContactList> getAllContactListsWith(Contact contact) throws IllegalArgumentException {
		return clMapper.findAllByCID(contact);
	}
}

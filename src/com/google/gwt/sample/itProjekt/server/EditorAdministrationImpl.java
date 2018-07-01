package com.google.gwt.sample.itProjekt.server;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.*;
import com.google.gwt.sample.itProjekt.shared.bo.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.sample.itProjekt.server.db.*;

@SuppressWarnings("serial")
public class EditorAdministrationImpl extends RemoteServiceServlet implements EditorAdministration{
	
	/**
	 * 
	 */
	
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
	

	public User getUserInformation (String email) throws IllegalArgumentException{
	
		//Wenn der User noch nicht in der Datenbank existiert, wird ein neuer User angelegt. 
		if(uMapper.findByEMail(email) == null){
			return createUser(email);	
		}
		else{
			return uMapper.findByEMail(email);
		}
	}
	
	public User getUser(String email) {
		return uMapper.findByEMail(email);
	}
	
	@Override
	public User getUserByID(int ID) throws IllegalArgumentException {

		return uMapper.findByID(ID);
	}
	
	@Override
	public User createUser(String email)
			throws IllegalArgumentException {

		User newuser = new User();
		newuser.setEmail(email);
		
		return uMapper.insert(newuser);
	}
	
	public Vector<Contact> getAllContactsOfActiveUser(User user) throws IllegalArgumentException {
		
		Vector<Contact> result = cMapper.findAllByUID(user);
		Vector<Contact> sharedContacts = pmMapper.getAllContactsByUID(user);
		
		for (Contact c : sharedContacts) {
			result.add(c);
		}
		
		return result;
	}
	
	public Vector<ContactList> getAllOwnedContactListsOfActiveUser(User user) throws IllegalArgumentException {
		
		return clMapper.findAllByUID(user);
	}

	@Override
	public Vector<ContactList> getAllContactListsOfUser(String email) throws IllegalArgumentException {
		
		Vector<ContactList> result = new Vector<ContactList>();
		
		result = clMapper.findAllByUID(uMapper.findByEMail(email));
		result.addAll(pmMapper.getAllContactListsByUID(uMapper.findByEMail(email)));

		return result;
	}

	@Override
	public Vector<Contact> getAllOwnedContactsOf(String email) throws IllegalArgumentException {

		Vector<Contact> resultcontacts = new Vector<Contact>();
		resultcontacts = cMapper.findAllByUID(uMapper.findByEMail(email));
		return resultcontacts;
	}
	
	@Override
	public Vector<Contact> getAllSharedContactsOfUserWithOtherUser(User source, String receiver) throws IllegalArgumentException {
		
		Vector<Contact> result = new Vector<Contact>();
		Vector<Contact> sourceContacts = pmMapper.getAllContactsBySrcUID(source);
		Vector<Contact> receiverContacts = pmMapper.getAllContactsByUID(uMapper.findByEMail(receiver));
				
		for (Contact c : sourceContacts) {
			if (receiverContacts.contains(c)) {
				result.add(c);
			}
		}
		
		return result;
	}

	@Override
	public Vector<Contact> getAllSharedContactsWith(String email) throws IllegalArgumentException {

		return pmMapper.getAllContactsByUID(uMapper.findByEMail(email));
	}
	
	
	public Vector<Contact> getAllContactsOfContactlistForUser(ContactList contactlist, User user) throws IllegalArgumentException {
		
		Vector<Contact> allContactsOfActiveUser = getAllContactsOfActiveUser(user);
		Vector<Contact> allContactsOfContactList = getAllContactsOf(contactlist);
		
		Vector<Contact> result = new Vector<Contact>();
		
		for(Contact c : allContactsOfContactList){
			if(allContactsOfActiveUser.contains(c)){
				result.add(c);
			}
		}
		return result;		
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
	public Contact createContact(String firstname, String lastname, String sex, User user) throws IllegalArgumentException {

		Contact newcontact = new Contact();
		newcontact.setFirstname(firstname);
		newcontact.setLastname(lastname);
		newcontact.setSex(sex);
		newcontact.setOwner(user.getId());
		
		return cMapper.insert(newcontact, user);
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
	public Permission shareContact(User sourceUser, String shareUserEmail, Contact shareContact) throws IllegalArgumentException {
		
		if(uMapper.findByEMail(shareUserEmail).getId() != shareContact.getOwner()) {
			Permission newpermission = new Permission();
			newpermission.setSourceUserID(sourceUser.getId());
			newpermission.setParticipantID(uMapper.findByEMail(shareUserEmail).getId());
			newpermission.setShareableObjectID(shareContact.getId());
			newpermission.setIsowner(false);
		
			return pmMapper.shareContact(newpermission);
		}
		else {
			return null;
		}
	}

	@Override
	public void deleteContact(Contact contact, boolean owner, User user) throws IllegalArgumentException {
				
		if(owner == true){
			cMapper.delete(contact);
		
		}else{
			deletePermission(user, contact);
		}
		
	}

	@Override
	public ContactList createContactList(String name, User user) throws IllegalArgumentException {

		ContactList newcontactlist = new ContactList();
		newcontactlist.setName(name);
		newcontactlist.setOwner(user.getId());
		
		return clMapper.insert(newcontactlist, user);
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
	public Permission shareContactList(User sourceUser, String shareUserEmail, ContactList shareContactList) throws IllegalArgumentException {
		

		Permission newCLpermission = new Permission();
		newCLpermission.setSourceUserID(sourceUser.getId());
		newCLpermission.setParticipantID(uMapper.findByEMail(shareUserEmail).getId());
		newCLpermission.setShareableObjectID(shareContactList.getId());
		newCLpermission.setIsowner(false);
		
		return pmMapper.shareContactList(newCLpermission);
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
	public Value createValue(Contact contact, int propertyid, String content) throws IllegalArgumentException {
		
		Value newvalue = new Value();
		newvalue.setContent(content);
		Property property = new Property();
		property.setId(propertyid);
		
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


	@Override
	public void deletePermission(User user, BusinessObject bo) throws IllegalArgumentException {
		
		Permission permission = new Permission();
		permission.setParticipantID(user.getId());
		permission.setShareableObjectID(bo.getId());
		
		pmMapper.delete(permission);
	}


	@Override
	public Vector<Contact> getAllContactsWithValue(String content) throws IllegalArgumentException {
		
		Value value = new Value();
		value.setContent(content);
		
		return vMapper.findAllContactsByValue(value);
	}


	@Override
	public Vector<Contact> getAllContactsWithName(String name) throws IllegalArgumentException {
	
		return cMapper.findAllByName(name);
	}


	@Override
	public Property getPropertyOfValue(Value value) throws IllegalArgumentException {
		
		Property property = new Property();
		property.setId(value.getPropertyid());
		
		return pMapper.findByID(property);
	}


	@Override
	public Vector<Value> getAllSharedValuesOfContact(Contact contact) throws IllegalArgumentException {
		
		return vMapper.getAllSharedValueByCID(contact);
	}


	@Override
	public Vector<Property> getAllPredefinedPropertiesOf() throws IllegalArgumentException {
		
		return pMapper.findAllDefault();
	}


	@Override
	public Property createProperty(Contact contact, String type) throws IllegalArgumentException {
		
		Property newProperty = new Property();
		newProperty.setType(type);
		
		return pMapper.insert(newProperty, contact);
	}


	@Override
	public Property getPropertyByType(String type) throws IllegalArgumentException {
		
		Property property = new Property();
		property.setType(type);
		
		return pMapper.findByType(property);
	}


	@Override
	public Vector<User> getAllUsers() throws IllegalArgumentException {

		return uMapper.findAll();
	}


	@Override
	public Vector<Contact> getAllSharedContactsOfContactList(ContactList contactlist, User user)
			throws IllegalArgumentException {
		Vector<Contact> allContactsInCL = getAllContactsOf(contactlist);
		Vector<Contact> allContactsWithPermission = pmMapper.getAllContactsByUID(user);
		Vector<Contact> result = new Vector<Contact>();
		
		for (Contact loop : allContactsInCL) {
			if (allContactsWithPermission.contains(loop)) {
				result.add(loop);
			}
		}
		
		return result;
	}
	
	@Override
	public Vector<Contact> getAllContactsOfUserWithValue(User user, Value value) throws IllegalArgumentException{
		Vector<Contact> allContactsOfUser= new Vector<Contact>();
		
		allContactsOfUser = getAllContactsOfActiveUser(user);
		
		Vector<Contact> allContactsWithValue=vMapper.findAllContactsByValue(value);
		Vector<Contact> result=new Vector<Contact>();
		
		for (Contact c :allContactsOfUser){
			if(allContactsWithValue.contains(c)){
				result.add(c);
			}
		}
		return result;
	}
	
	@Override
	public Vector<Contact> getContactsOfUserWithProperty(User user, Property property) throws IllegalArgumentException{
		Vector<Contact> allContactsOfUser= new Vector<Contact>();
		allContactsOfUser = getAllContactsOfActiveUser(user);
		Property p=new Property();
		p=pMapper.findByType(property);
		
		Vector<Contact> allContactsWithProperty=vMapper.getAllContactsByPID(p);
		Vector<Contact> result=new Vector<Contact>();
		
		for (Contact c :allContactsOfUser){
			if(allContactsWithProperty.contains(c)){
				result.add(c);
			}
		}
		return result;
	}
	
	public Vector<Contact> getContactsOfNameSearchResult(User user, String textBox, ContactList selectedContactList) throws IllegalArgumentException{
		
		Vector<Contact> allContactsOfUser = new Vector<Contact>();
		Vector<Contact> allContactsOfList = new Vector<Contact>();
		Vector<Contact> nameResults = new Vector<Contact>();
		
		Vector<Contact> result = new Vector<Contact>();
		
		allContactsOfUser = getAllContactsOfActiveUser(user);
		allContactsOfList = getAllContactsOf(selectedContactList);
		nameResults = getAllContactsWithName(textBox);
		
			if(selectedContactList.getId() == 5) {
				for (Contact c : nameResults) {
					if (allContactsOfUser.contains(c)) {
						result.add(c);
				    }
				}
			}else{
				for (Contact c : nameResults) {
					if (allContactsOfList.contains(c)) {
						result.add(c);
				    }
			    }	
			}
		
		return result;
		
	}
	
	public Vector<Contact> getContactsOfValueSearchResult(User user, String textBox, ContactList selectedContactList) throws IllegalArgumentException{
		
		Vector<Contact> allContactsOfUser = new Vector<Contact>();
		Vector<Contact> allContactsOfList = new Vector<Contact>();
		Vector<Contact> valueResults = new Vector<Contact>();
		
		Vector<Contact> result = new Vector<Contact>();
		
		allContactsOfUser = getAllContactsOfActiveUser(user);
		allContactsOfList = getAllContactsOf(selectedContactList); 
		valueResults = getAllContactsWithValue(textBox);
		
		if(selectedContactList.getId() == 5) {
			for (Contact c : valueResults) {
				if (allContactsOfUser.contains(c)) {
					result.add(c);
			    }
			}
		}else{
			for (Contact c : valueResults) {
				if (allContactsOfList.contains(c)) {
					result.add(c);
			    }
		    }	
		}
		return result;
	}

	public User getSourceToSharedContact(Contact contact, User receivingUser) throws IllegalArgumentException {
		
		return pmMapper.getSourceUserByUIDAndCID(receivingUser, contact);
	}


	@Override
	public Vector<Permission> getAllPermissions() throws IllegalArgumentException {

		return pmMapper.findAll();
	}
	
	@Override
	public User getOwnerOfContact (Contact c) throws IllegalArgumentException {
		User user= new User();
		user=uMapper.findByID(c.getOwner());
		if(user!=null){
			return user;
		}
		else{
			return null;
		}
	}
	
	public Value createAddress(String street, String housenumber, String zip, String city, Contact contact) {
		
		createValue(contact, 7, housenumber);
		createValue(contact, 8, zip);
		createValue(contact, 9, city);
		
		return createValue(contact, 6, street);
	}
}

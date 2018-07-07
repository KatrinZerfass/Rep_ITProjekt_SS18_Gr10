package com.google.gwt.sample.itProjekt.server;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.*;
import com.google.gwt.sample.itProjekt.shared.bo.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.sample.itProjekt.server.db.*;

// TODO: Auto-generated Javadoc
/**
 * The Class EditorAdministrationImpl.
 */
@SuppressWarnings("serial")
public class EditorAdministrationImpl extends RemoteServiceServlet implements EditorAdministration{
	
	
	/** Referenz auf den NutzerMapper, der Nutzerobjekte mit der Datenbank abgleicht. */
	private UserMapper uMapper;
	
	/** Referenz auf den KontaktMapper, der Kontaktobjekte mit der Datenbank abgleicht. */
	private ContactMapper cMapper;
	
	/** Referenz auf den KontaktlistenMapper, der Kontaktlistenobjekte mit der Datenbank abgleicht. */
	private ContactListMapper clMapper;
	
	/** Referenz auf den EigenschaftenMapper, der Eigenschaftsobjekte mit der Datenbank abgleicht. */
	private PropertyMapper pMapper;
	
	/** Referenz auf den AusprägungsMapper, der Ausprägungsobjekte mit der Datenbank abgleicht. */
	private ValueMapper vMapper;
	
	/** Referenz auf den TeilhaberschaftsMapper, der Teilhaberschaftsobjekte mit der Datenbank abgleicht. */
	private PermissionMapper pmMapper;

	/*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Initialisierung
	   * ***************************************************************************
	   */
	
	/**
	 * No Argument Konstruktor
	 *
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public EditorAdministrationImpl() throws IllegalArgumentException {
		// TODO Auto-generated constructor stub
	}
	
	public void init() throws IllegalArgumentException {
		
		this.clMapper = ContactListMapper.contactListMapper();
		this.cMapper = ContactMapper.contactMapper();
		this.pMapper = PropertyMapper.propertyMapper();
		this.pmMapper = PermissionMapper.permissionMapper();
		this.uMapper = UserMapper.userMapper();
		this.vMapper = ValueMapper.valueMapper();
		
	}
	
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Initialisierung
	   * ***************************************************************************
	   */

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden für User-Objekte
	   * create, edit, delete und get(ter) für User-Objekte, sowie alle Methoden, 
	   * welche User-Objekte oder Vektoren von User-Objekten zurück geben 
	   * oder den User direkt betreffen.
	   * ***************************************************************************
	   */
	
	@Override
	public User createUser(String email) throws IllegalArgumentException {

		User newuser = new User();
		newuser.setEmail(email);
		
		return uMapper.insert(newuser);
	}
	
	public User editUser(User user) throws IllegalArgumentException{
		return uMapper.update(user);
	}
	
	public void deleteUser(User user) throws IllegalArgumentException{
		uMapper.delete(user);
	}
	
	public User getUserByEmail(String email) {
		return uMapper.findByEMail(email);
	}
	
	public User getUserByID(int ID) throws IllegalArgumentException {

		return uMapper.findByID(ID);
	}

	public User createUserContact(String firstname, String lastname, String sexList, String email)
			throws IllegalArgumentException {
		
		User newUser = null;
		newUser = createUser(email);
		
		Contact newContact = new Contact();
		newContact.setFirstname(firstname);
		newContact.setLastname(lastname);
		String sex = "o";
		switch(sexList) {
			case "männlich": 
				sex = "m";
				break;
			case "weiblich": 
				sex = "f";
				break;
			case "Sonstiges": 
				sex = "o";
				break;
		}
		newContact.setSex(sex);
		newContact.setIsUser(true);
		
		cMapper.insert(newContact, newUser);
		
		return newUser;
	}
	
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

	public User getSourceToSharedContact(Contact contact, User receivingUser) throws IllegalArgumentException {
		
		return pmMapper.getSourceUserByUIDAndCID(receivingUser, contact);
	}
	
	public Vector<User> getAllUsers() throws IllegalArgumentException {

		return uMapper.findAll();
	}
	  
	public Vector<User> getAllParticipantsOfContact(Contact contact) throws IllegalArgumentException {
		
		return pmMapper.findAllParticipantsByCID(contact);
	}
	
	public Vector<User> getAllParticipantsOfContactList(ContactList contactlist) throws IllegalArgumentException {
		
		return pmMapper.findAllParticipantsByCLID(contactlist);
	}

	public boolean isUserKnown (String email) throws IllegalArgumentException{
	
		//Wenn der User noch nicht in der Datenbank existiert, wird ein neuer User angelegt. 
		if(getUserByEmail(email).getEmail() == null){
			return false;	
		}
		else{
			return true;
		}
	}
	
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden für User-Objekte
	   * ***************************************************************************
	   */

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden für Kontakt-Objekte
	   * create, edit, delete und get(ter) für Kontakt-Objekte, sowie alle Methoden, 
	   * welche Kontakt-Objekte oder Vektoren von Kontakt-Objekten zurück geben.
	   * ***************************************************************************
	   */
	
	public Contact createContact(String firstname, String lastname, String sex, User user) throws IllegalArgumentException {

		Contact newcontact = new Contact();
		newcontact.setFirstname(firstname);
		newcontact.setLastname(lastname);
		newcontact.setSex(sex);
		newcontact.setOwner(user.getId());
		
		return cMapper.insert(newcontact, user);
	}
	
	public Contact editContact(int id, String firstname, String lastname, String sex) throws IllegalArgumentException {
		
		Contact changedcontact = new Contact();
		changedcontact.setFirstname(firstname);
		changedcontact.setLastname(lastname);
		changedcontact.setSex(sex);
		changedcontact.setId(id);

		return cMapper.update(changedcontact);
	}
	
	public void deleteContact(Contact contact, boolean owner, User user) throws IllegalArgumentException {
		
		Vector<Value> allValues = getAllValuesOfContact(contact);
		Vector<ContactList> allContactLists = getAllContactListsWithContact(contact);
		Vector<User> allUsers = getAllParticipantsOfContact(contact);
		
		if(owner == true){
			for (User u : allUsers) {
				deletePermission(u, contact);
			}
			for (Value v :  allValues) {
				deleteValue(v);
			}
			for (ContactList cl : allContactLists) {
				removeContactFromContactList(cl, contact);
			}
			
			cMapper.delete(contact);
		}else{
			deletePermission(user, contact);
		}
		
	}

	public Contact getContactByID(int id) throws IllegalArgumentException {

		Contact contact = new Contact();
		contact.setId(id);
		return cMapper.findByID(contact);
	}

	public Vector<Contact> getAllContactsOfActiveUser(User user) throws IllegalArgumentException {
		
		Vector<Contact> result = new Vector<Contact>();
		Vector<Contact> allContacts = cMapper.findAllByUID(user);
		Vector<Contact> sharedContacts = pmMapper.getAllContactsByUID(user);
		
		for (Contact c : allContacts) {
			if(c.getIsUser()) {
				result.add(c);
			}
		}
		for (Contact c : allContacts) {
			if(!c.getIsUser()) {
				result.add(c);
			}
		}
		for (Contact c : sharedContacts) {
			result.add(c);
		}
		
		return result;
	}
	
	public Vector<Contact> getAllOwnedContactsOfUser(String email) throws IllegalArgumentException {

		Vector<Contact> resultcontacts = new Vector<Contact>();
		resultcontacts = cMapper.findAllByUID(getUserByEmail(email));
		return resultcontacts;
	}
	
	public Vector<Contact> getAllSharedContactsWithUser(String email) throws IllegalArgumentException {

		return pmMapper.getAllContactsByUID(getUserByEmail(email));
	}
	
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
	
	public Vector<Contact> getAllContactsOfContactlistForUser(ContactList contactlist, User user) throws IllegalArgumentException {
		
		Vector<Contact> allContactsOfActiveUser = getAllContactsOfActiveUser(user);
		Vector<Contact> allContactsOfContactList = getAllContactsOfContactList(contactlist);
		
		Vector<Contact> result = new Vector<Contact>();
		
		for(Contact c : allContactsOfContactList){
			if(allContactsOfActiveUser.contains(c)){
				result.add(c);
			}
		}
		return result;		
	}

	public Vector<Contact> getAllContactsOfContactList(ContactList contactlist) throws IllegalArgumentException {
		
		return clMapper.getAllContacts(contactlist);
	}
	
	public Vector<Contact> getAllContactsWithValue(String content) throws IllegalArgumentException {
		
		Value value = new Value();
		value.setContent(content);
		
		return vMapper.findAllContactsByValue(value);
	}
	
	public Vector<Contact> getAllContactsWithName(String name) throws IllegalArgumentException {
		
		return cMapper.findAllByName(name);
	}

	public Vector<Contact> getAllSharedContactsOfContactList(ContactList contactlist, User user)
			throws IllegalArgumentException {
		Vector<Contact> allContactsInCL = getAllContactsOfContactList(contactlist);
		Vector<Contact> allContactsWithPermission = pmMapper.getAllContactsByUID(user);
		Vector<Contact> result = new Vector<Contact>();
		
		for (Contact loop : allContactsInCL) {
			if (allContactsWithPermission.contains(loop)) {
				result.add(loop);
			}
		}
		
		return result;
	}
	
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
	
	public Vector<Contact> getContactsOfUserWithProperty(User user, Property property) throws IllegalArgumentException{
		Vector<Contact> allContactsOfUser= getAllContactsOfActiveUser(user);
		Vector<Property> allPropertiesWithType= new Vector<Property>();
		Contact c1 = new Contact();
		Contact c2 = new Contact();
		Vector<Contact> result=new Vector<Contact>();
		
		for (Contact c :allContactsOfUser){
			System.out.println("Property: "+property.getType()+ " KontaktID: "+ c.getId());
			allPropertiesWithType=pMapper.findByTypeAndCID(property, c);
			if(allPropertiesWithType.size()>0){
			for(Property p: allPropertiesWithType){
				c1.setId(p.getContactID());
				c2 = cMapper.findByID(c1);
				System.out.println(c2.getId());
				result.addElement(c2);
			}}
			
		}
		return result;
	}

	public Vector<Contact> getContactsOfUserWithDefaultProperty(User user, Property property) throws IllegalArgumentException{
		Vector<Contact> allContactsOfUser= new Vector<Contact>();
		allContactsOfUser = getAllContactsOfActiveUser(user);
		Vector <Property> pv=new Vector <Property>();
		Vector<Contact> result=new Vector<Contact>();
		Contact c1 = new Contact();
		c1.setId(20000000);
		pv=pMapper.findByTypeAndCID(property, c1);
		for(Property p: pv){
		Vector<Contact> allContactsWithProperty=vMapper.getAllContactsByPID(p);
		
		for (Contact c :allContactsOfUser){
			if(allContactsWithProperty.contains(c)){
				result.add(c);
			}
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
		allContactsOfList = getAllContactsOfContactList(selectedContactList);
		nameResults = getAllContactsWithName(textBox);
		
			if(selectedContactList.getMyContactsFlag()) {
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
		allContactsOfList = getAllContactsOfContactList(selectedContactList); 
		valueResults = getAllContactsWithValue(textBox);
		
		if(selectedContactList.getMyContactsFlag()) {
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

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden für Kontakt-Objekte
	   * ***************************************************************************
	   */

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden für Kontaktlisten-Objekte
	   * create, edit, und delete für Kontaktlisten-Objekte, sowie alle 
	   * Methoden, welche Kontaktlisten-Objekte oder Vektoren von 
	   * Kontaktlisten-Objekten zurück geben.
	   * ***************************************************************************
	   */
	
	public ContactList createContactList(String name, User user) throws IllegalArgumentException {

		ContactList newcontactlist = new ContactList();
		newcontactlist.setName(name);
		newcontactlist.setOwner(user.getId());
		
		return clMapper.insert(newcontactlist, user);
	}
	
	public ContactList editContactList(int id, String name) throws IllegalArgumentException {

		ContactList changedcontactlist = new ContactList();
		changedcontactlist.setId(id);
		changedcontactlist.setName(name);
		
		return clMapper.update(changedcontactlist);
	}
	
	public void deleteContactList(ContactList contactlist, boolean owner, User user) throws IllegalArgumentException {
		
		Vector<Contact> allContacts = getAllContactsOfContactList(contactlist);
		Vector<User> allUsers = getAllParticipantsOfContactList(contactlist);
		
		if(owner == true){
			pmMapper.deleteAllByCLID(contactlist);
			for(Contact c : allContacts) {
				for(User u : allUsers) {
					deletePermission(u, c);	
				}
				removeContactFromContactList(contactlist, c);
			}
			clMapper.delete(contactlist);
		}else{
			for(Contact c : allContacts) {
				for(User u : allUsers) {
					deletePermission(u, c);	
				}
			}
			deletePermission(user, contactlist);
		}	
	}

	public ContactList addContactToContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {
		
		Vector<User> participants = new Vector<User>();
		participants = pmMapper.findAllParticipantsByCLID(contactlist);
		for(User u : participants){
			shareContact(uMapper.findByID(contactlist.getOwner()), u.getEmail(), contact);
		}
		
		return clMapper.addContact(contactlist, contact);
	}

	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {

		return clMapper.removeContact(contactlist, contact);
	}
	
	public Vector<ContactList> getAllOwnedContactListsOfActiveUser(User user) throws IllegalArgumentException {
		
		return clMapper.findAllByUID(user);
	}
	
	public Vector<ContactList> getAllContactListsOfUser(String email) throws IllegalArgumentException {
		
		Vector<ContactList> result = new Vector<ContactList>();
		
		result = clMapper.findAllByUID(getUserByEmail(email));
		result.addAll(pmMapper.getAllContactListsByUID(getUserByEmail(email)));

		return result;
	}

	public Vector<ContactList> getAllContactListsWithContact(Contact contact) throws IllegalArgumentException {
		return clMapper.findAllByCID(contact);
	}

	 /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden für Kontaktlisten-Objekte
	   * ***************************************************************************
	   */

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden für Eigenschaften-Objekte
	   * create, edit, und delete für Eigenschaften-Objekte, sowie alle 
	   * Methoden, welche Eigenschaften-Objekte oder Vektoren von 
	   * Eigenschaften-Objekten zurück geben.
	   * ***************************************************************************
	   */
	
	public Property createProperty(Contact contact, String type) throws IllegalArgumentException {
		
		Property newProperty = new Property();
		newProperty.setType(type);
	//	pMapper.findByTypeAndCID(newProperty, contact);
		
		if(pMapper.findByTypeAndCID(newProperty, contact).size() == 0) {
			return null;
		}else {
			//Hier Abfrage: gibt es schon einen Datensatz in der T_Property zu diesem Contact mit dieser Type? Wenn ja, return null
			return pMapper.insert(newProperty, contact);
		}
	}

	public Property editProperty(Property property) throws IllegalArgumentException{
		return pMapper.update(property);
	}
	
	public void deleteProperty(Property property) throws IllegalArgumentException{
		pMapper.delete(property);
	}
	
	public Property getPropertyOfValue(Value value) throws IllegalArgumentException {
		
		Property property = new Property();
		property.setId(value.getPropertyid());
		
		return pMapper.findByID(property);
	}
	
	public Property getPropertyByType(String type, Contact contact) throws IllegalArgumentException {
		
		Contact defaultContact = null;
		
		if (contact == null) {
			defaultContact = new Contact();
			defaultContact.setId(20000000);
		}
		else {
			defaultContact = contact;
		}
		Property property = new Property();
		Property result = new Property();
		property.setType(type);
		Vector<Property> vp = pMapper.findByType(property);
		for(Property p: vp){
			if(p.getContactID()==defaultContact.getId()){
				result=p;
			}
		}
		return result;
	}
	
	public Vector<Property> getAllPredefinedPropertiesOf() throws IllegalArgumentException {
		
		return pMapper.findAllDefault();
	}

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden für Eigenschaften-Objekte
	   * ***************************************************************************
	   */

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden für Ausprägungs-Objekte
	   * create, edit, und delete für Ausprägungs-Objekte, sowie alle 
	   * Methoden, welche Ausprägungs-Objekte oder Vektoren von 
	   * Ausprägungs-Objekten zurück geben.
	   * ***************************************************************************
	   */
	
	public Value createValue(Contact contact, int propertyid, String content) throws IllegalArgumentException {
		
		Value newvalue = new Value();
		newvalue.setContent(content);
		Property property = new Property();
		property.setId(propertyid);
		
		Value addedValue = vMapper.insert(newvalue, contact, property);
		
		cMapper.update(vMapper.findContactByVID(addedValue));
		
		return addedValue;
	}
	
	public Value editValue(Contact contact, int propertyId, Value value, String content, boolean isshared)
			throws IllegalArgumentException {
		
		Value newvalue = new Value();
		newvalue = value;
		newvalue.setIsShared(isshared);
		newvalue.setContent(content);
		Property property = new Property();
		property.setId(propertyId);
		
		Value updatedValue = vMapper.update(newvalue, contact, property);
		
		cMapper.update(vMapper.findContactByVID(updatedValue));
		
		return updatedValue;
	}
	
	public void deleteValue(Value value) throws IllegalArgumentException {
		
		Property extraProperty = new Property();
		Vector<Property> predefined = getAllPredefinedPropertiesOf();
		
		extraProperty.setId(value.getPropertyid());
		
		extraProperty = pMapper.findByID(extraProperty);
		
		cMapper.update(vMapper.findContactByVID(value));
		
		if(predefined.contains(extraProperty)) {
			vMapper.delete(value);
		}
		else {
			if (vMapper.findAllByPID(extraProperty, vMapper.findContactByVID(value)).size() == 1) {
				pMapper.delete(extraProperty);

				vMapper.delete(value);
			}
		}
	}
	
	public Value createAddress(String street, String housenumber, String zip, String city, Contact contact) {
		
		Value streetValue = createValue(contact, 6, street);
		createValue(contact, 7, housenumber);
		createValue(contact, 8, zip);
		createValue(contact, 9, city);
		
		return streetValue;
	}
	
	public Vector<Value> getAllValuesOfContact(Contact contact) throws IllegalArgumentException {
		
		return vMapper.getAllValueByCID(contact);
	}
	
	public Vector<Value> getAllSharedValuesOfContact(Contact contact) throws IllegalArgumentException {
		
		return vMapper.getAllSharedValueByCID(contact);
	}
	
	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden für Ausprägungs-Objekte
	   * ***************************************************************************
	   */

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden für Teilhaberschaft-Objekte
	   * create (im Falle von Teilhaberschaften entspricht dies den 
	   * "share"-Methoden), edit, und delete für Teilhaberschaft-Objekte, sowie alle 
	   * Methoden, welche Teilhaberschaft-Objekte oder Vektoren von 
	   * Teilhaberschaft-Objekten zurück geben.
	   * ***************************************************************************
	   */
	
	public Permission shareContact(User sourceUser, String shareUserEmail, Contact shareContact) throws IllegalArgumentException {
		
		if(uMapper.findByEMail(shareUserEmail).getId() != shareContact.getOwner()) {
			Permission newpermission = new Permission();
			newpermission.setSourceUserID(sourceUser.getId());
			newpermission.setParticipantID(uMapper.findByEMail(shareUserEmail).getId());
			newpermission.setShareableObjectID(shareContact.getId());
		
			return pmMapper.shareContact(newpermission);
		}
		else {
			return null;
		}
	}

	public Permission shareContactList(User sourceUser, String shareUserEmail, ContactList shareContactList) throws IllegalArgumentException {
		
		if(uMapper.findByEMail(shareUserEmail).getId() != shareContactList.getOwner()) {
			Permission newCLpermission = new Permission();
			newCLpermission.setSourceUserID(sourceUser.getId());
			newCLpermission.setParticipantID(uMapper.findByEMail(shareUserEmail).getId());
			newCLpermission.setShareableObjectID(shareContactList.getId());
			
			return pmMapper.shareContactList(newCLpermission);
		}
		else {
			return null;
		}
	}

	public Permission editPermissionContact(Permission permission) throws IllegalArgumentException{
		return pmMapper.updateContact(permission);
	}
	
	public Permission editPermissionContactList(Permission permission) throws IllegalArgumentException{
		return pmMapper.updateContactList(permission);
	}

	public void deletePermission(User user, BusinessObject bo) throws IllegalArgumentException {
		
		ContactList cl = new ContactList();
		Vector<Contact> allContacts = new Vector<Contact>();
		
		Permission permission = new Permission();
		permission.setParticipantID(user.getId());
		permission.setShareableObjectID(bo.getId());

		if(bo.getClass().isInstance(cl)) {
			allContacts = getAllContactsOfContactList((ContactList) bo);
			if(allContacts.size()>0){
				for(Contact c : allContacts){
					
					Permission p = new Permission();
					p.setShareableObjectID(c.getId());
					p.setParticipantID(permission.getParticipantID());
					
					pmMapper.deleteContact(p);
				}
				pmMapper.deleteContactList(permission);
			}
		}
		else {
			pmMapper.deleteContact(permission);
		}
	}
			
	public Vector<Permission> getAllPermissions() throws IllegalArgumentException {

		return pmMapper.findAll();
	}

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Ende: Methoden für Teilhaberschaft-Objekte
	   * ***************************************************************************
	   */

	  /*
	   * ***************************************************************************
	   * ABSCHNITT, Beginn: Methoden, welche sonstige Funktionen erfüllen.
	   * ***************************************************************************
	   */

	public Vector<String> getAllUserSuggestions(User activeUser) throws IllegalArgumentException {
		
		Vector<User> allUsers = uMapper.findAll();
		Vector<Contact> allContacts = cMapper.findAllUserContacts();

		Vector<String> result = new Vector<String>();

		
		allUsers.removeElement(activeUser);
		
		for(Contact c : allContacts) {
			for(User u : allUsers) {
				if (c.getOwner() == u.getId()) {
					result.add(c.getFirstname() + " " + c.getLastname() + " - " + u.getEmail());
				}
			}
		}
		return result;
	}

	public Vector<String> getFullNamesOfUsers(Vector<User> user) throws IllegalArgumentException {
		Vector<Contact> contacts = null;
		Vector<String> result = new Vector<String>();
		
		for (User u : user) {
			contacts = getAllOwnedContactsOfUser(u.getEmail());
			for(Contact c : contacts) {
				if (c.getIsUser()) {
					result.addElement(c.getFirstname() + " " + c.getLastname());
				}
			}
		}
		return result;
	}
}

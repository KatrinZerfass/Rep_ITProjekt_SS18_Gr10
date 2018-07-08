package com.google.gwt.sample.itProjekt.server;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.*;
import com.google.gwt.sample.itProjekt.shared.bo.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.sample.itProjekt.client.ClientsideFunctions.InputDialogBox;
import com.google.gwt.sample.itProjekt.server.db.*;

/**
 * Implementierungsklasse des Interface EditorAdministration.
 * 
 * @author JanNoller
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
	}
	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von GWT
     * RPC zusätzlich zum No Argument Constructor der implementierenden Klasse
     * {@link EditorAdministrationImpl} notwendig.
     * 
     * @throws IllegalArgumentException
	 */
	public void init() throws IllegalArgumentException {
		
		/**
		 * Referenzen auf die DatenbankMapper, der Businessobjekte mit der Datenbank abgleichen.
		 */
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
	
	/**
	 * Legt einen Benutzer in der Datenbank an.
	 *
	 * @param email Email des Nutzers
	 * @return ein vollständiges User Objekt
	 * @throws IllegalArgumentException
	 */
	public User createUser(String email) throws IllegalArgumentException {

		User newuser = new User();
		newuser.setEmail(email);
		
		return uMapper.insert(newuser);
	}
	/**
	 * Überschreibt einen Nutzer in der Datenbank.
	 * 
	 * @param user neuer Nutzer
	 * @return vollständiges Nutzer-Objekt
	 * @throws IllegalArgumentException
	 */
	public User editUser(User user) throws IllegalArgumentException{
		return uMapper.update(user);
	}
	
	/**
	 * Löscht einen Nutzer aus der Datenbank.
	 * 
	 * @param user Nutzer
	 * @throws IllegalArgumentException
	 */
	public void deleteUser(User user) throws IllegalArgumentException{
		uMapper.delete(user);
	}
	
	/**
	 * Holt einen Nutzer anhand seiner Emailadresse aus der Datenbank.
	 * 
	 * @param email Emailadresse des Nutzers
	 */
	public User getUserByEmail(String email) {
		return uMapper.findByEMail(email);
	}
	
	/**
	 * Holt die Information des Nutzers anhand seiner ID aus der Datenbank.
	 *
	 * @param ID id des Nutzer
	 * @return ein vollständiges User Objekt
	 * @throws IllegalArgumentException
	 */
	public User getUserByID(int ID) throws IllegalArgumentException {

		return uMapper.findByID(ID);
	}
	
	/**
	 * Legt einen neuen Kontakt, welcher einen Nutzer repräsentiert, an.
	 *
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param sexList Geschlecht
	 * @param email Email-Adresse
	 * @return vollständiges Nutzer-Objekt
	 * @throws IllegalArgumentException
	 */
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
		
		newContact = cMapper.insert(newContact, newUser);
		
		createValue(newContact, 3, email);
		
		return newUser;
	}
	
	/**
	 * Holt den Urheber eines Kontakts aus der Datenbank.
	 *
	 * @param c Kontakt
	 * @return vollständiges Nutzer-Objekt des Urhebers
	 * @throws IllegalArgumentException
	 */
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

	/**
	 * Holt zu einem Teilhaber eines Kontakt und dem Kontakt den Nutzer aus der Datenbank,
	 * welcher den Kontakt geteilt hat.
	 *
	 * @param contact Kontakt
	 * @param receivingUser Teilhaber des Kontakts
	 * @return vollständiges Nutzer-Objekt des Nutzers, welcher den Kontakt geteilt hat
	 * @throws IllegalArgumentException
	 */
	public User getSourceToSharedContact(Contact contact, User receivingUser) throws IllegalArgumentException {
		
		return pmMapper.getSourceUserByUIDAndCID(receivingUser, contact);
	}
	
	/**
	 * Holt alle Nutzer aus der Datenbank.
	 *
	 * @return Vector der Nutzer
	 * @throws IllegalArgumentException
	 */
	public Vector<User> getAllUsers() throws IllegalArgumentException {

		return uMapper.findAll();
	}
	
	/**
	 * Holt alle Teilhaber eines Kontakts aus der Datenbank.
	 *
	 * @param contact Kontaktliste
	 * @return Vector der betroffenen Nutzer
	 * @throws IllegalArgumentException
	 */
	public Vector<User> getAllParticipantsOfContact(Contact contact) throws IllegalArgumentException {
		
		return pmMapper.findAllParticipantsByCID(contact);
	}

	/**
	 * Holt alle Teilhaber einer Kontaktliste aus der Datenbank.
	 *
	 * @param contactlist Kontaktliste
	 * @return Vector der betroffenen Nutzer
	 * @throws IllegalArgumentException
	 */
	public Vector<User> getAllParticipantsOfContactList(ContactList contactlist) throws IllegalArgumentException {
		
		return pmMapper.findAllParticipantsByCLID(contactlist);
	}

	/**
	 * Holt die Information ob der Nutzer bereits aus der Datenbank.
	 * Identifiziert wird dies über einen boolschen Rückgabewert, true wenn Nutzer bereits existiert, sonst false.
	 * 
	 * @param email Email des Nutzers
	 * @return boolscher Wert
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Legt einen neuen Kontakt an.
	 *
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param sex Geschlecht
	 * @param user Nutzer (welcher den Kontakt erstellt)
	 * @return vollständiges Kontakt-Objekt
	 * @throws IllegalArgumentException
	 */
	public Contact createContact(String firstname, String lastname, String sex, User user) throws IllegalArgumentException {

		Contact newcontact = new Contact();
		newcontact.setFirstname(firstname);
		newcontact.setLastname(lastname);
		newcontact.setSex(sex);
		newcontact.setOwner(user.getId());
		
		return cMapper.insert(newcontact, user);
	}
	
	/**
	 * Ändert den Kontaktstamm eines Kontakts in der Datenbank. 
	 *
	 * @param id ID des Kontakts
	 * @param firstname neuer Vorname
	 * @param lastname neuer Nachname
	 * @param sex neues Geschlecht
	 * @return vollständiges Kontakt-Objekt
	 * @throws IllegalArgumentException
	 */
	public Contact editContact(int id, String firstname, String lastname, String sex) throws IllegalArgumentException {
		
		Contact changedcontact = new Contact();
		changedcontact.setFirstname(firstname);
		changedcontact.setLastname(lastname);
		changedcontact.setSex(sex);
		changedcontact.setId(id);

		return cMapper.update(changedcontact);
	}
	
	/**
	 * Löscht einen Kontakt aus der Datenbank, falls der Nutzer jener ist, welcher den Kontakt erstellt hat.
	 * In diesem Fall werden zusätzlich auch alle Teilhaberschaften am Kontakt und Ausprägungen des Kontakts gelöscht.
	 * Ist der Nutzer nur Teilhaber des Kontakts wird nur die Teilhaberschaft des Nutzers gelöscht.
	 * Dies wird über einen boolschen Wert identifiziert (true für Urheber, false für Teilhaber)
	 *
	 * @param contact Kontakt
	 * @param owner boolscher Wert zur Idetifikation der Beziehung zum Nutzer
	 * @param user Nutzer
	 * @throws IllegalArgumentException
	 */
	public void deleteContact(Contact contact, boolean owner, User user) throws IllegalArgumentException {
		
		Vector<Value> allValues = getAllValuesOfContact(contact);
		Vector<ContactList> allContactLists = getAllContactListsWithContact(contact);
		Vector<User> allUsers = getAllParticipantsOfContact(contact);
		
		if(contact.getOwner() == user.getId()){
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

	/**
	 * Holt einen Kontakt anhand seiner ID aus der Datenbank.
	 *
	 * @param id die ID
	 * @return der Kontakt
	 * @throws IllegalArgumentException
	 */
	public Contact getContactByID(int id) throws IllegalArgumentException {

		Contact contact = new Contact();
		contact.setId(id);
		return cMapper.findByID(contact);
	}

	/**
	 * Holt alle Kontakte des angemeldeten Nutzers aus der Datenbank.
	 * Sortiert werden diese wie folgt:<br>
	 * 1. Kontakt welcher den Nutzer repräsentiert.<br>
	 * 2. Kontakte, welche der Nutzer erstellt hat.<br>
	 * 3. Kontakte, welche dem Nutzer geteilt wurden.
	 *
	 * @param user der angemeldete Nutzer
	 * @return Vector aller Kontakte des angemeldeten Nutzers 
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt alle Kontakte eines Nutzers aus der Datenbank, welche der Nutzer selbst erstellt hat.
	 *
	 * @param email Email des Nutzers
	 * @return Vector der selbst erstellten Kontakte des Nutzers
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllOwnedContactsOfUser(String email) throws IllegalArgumentException {

		Vector<Contact> resultcontacts = new Vector<Contact>();
		resultcontacts = cMapper.findAllByUID(getUserByEmail(email));
		return resultcontacts;
	}
	
	/**
	 * Holt alle Kontakte aus der Datenbank, welche mit einem bestimmten Nutzer geteilt wurden.
	 *
	 * @param email Email des Nutzers
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllSharedContactsWithUser(String email) throws IllegalArgumentException {

		return pmMapper.getAllContactsByUID(getUserByEmail(email));
	}
	
	/**
	 * Holt alle Kontakte aus der Datenbank, welche ein Nutzer mit einem anderen geteilt hat.
	 *
	 * @param source der "teilende" Nutzer
	 * @param receiver die Email-Adresse des Empfängers der Kontakte
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt alle enthaltenen Kontakte einer Kontaktliste für einen bestimmten Nutzer aus der Datenbank.
	 *
	 * @param contactlist betroffene Kontaktliste
	 * @param user betroffener Nutzer
	 * @return Vector aller Kontakte der Kontaktliste für den Nutzer
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsOfContactlistForUser(ContactList contactlist, User user) throws IllegalArgumentException {
		
		Vector<Contact> allContactsOfActiveUser = getAllContactsOfActiveUser(user);
		Vector<Contact> allContactsOfContactList = getAllContactsOfContactList(contactlist);
		
		Vector<Contact> result = new Vector<Contact>();
		
		for(Contact c : allContactsOfContactList){
			if(allContactsOfActiveUser.contains(c) && c.getOwner() == user.getId()){
				result.add(c);
			}
		}
		for(Contact c : allContactsOfContactList){
			if(allContactsOfActiveUser.contains(c) && c.getOwner() != user.getId()){
				result.add(c);
			}
		}
		return result;		
	}

	/**
	 * Holt alle Kontagte einer bestimmten Kontaktliste aus der Datenbank, unabhängig vom Nutzer.
	 *
	 * @param contactlist die Kontaktliste
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsOfContactList(ContactList contactlist) throws IllegalArgumentException {
		
		return clMapper.getAllContacts(contactlist);
	}
	
	/**
	 * Holt alle Kontakte mit einem bestimmten Inhalt in den Ausprägungen aus der Datenbank.
	 *
	 * @param content der gesuchte Inhalt
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsWithValue(String content) throws IllegalArgumentException {
		
		Value value = new Value();
		value.setContent(content);
		
		return vMapper.findAllContactsByValue(value);
	}
	
	/**
	 * Holt alle Kontakte mit einem bestimmten String im Namen (Vor oder Nachname) aus der Datenbank.
	 *
	 * @param name gesuchter Name
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsWithName(String name) throws IllegalArgumentException {
		
		return cMapper.findAllByName(name);
	}
	
	/**
	 * Holt alle Kontakte einer Kontaktliste aus der Datenbank, für die ein Nutzer eine Teilhaberschaft besitzt.
	 * Falls der Nutzer einen Kontakt aus einer ihm geteilten Kontaktliste entfernt wird dieser mit Hilfe dieser Methode
	 * nicht mehr angezeigt.
	 *
	 * @param contactlist Kontaktliste
	 * @param user Nutzer
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
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
	/**
	 * Holt alle Kontakte mit einer bestimmten Ausprägung eines bestimmten Nutzers aus der Datenbank.
	 *
	 * @param user Nutzer
	 * @param value Ausprägung
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt alle Kontakte mit einer bestimmten Eigenschaft eines bestimmten Nutzers aus der Datenbank.
	 *
	 * @param user Nutzer
	 * @param property Eigenschaft
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt alle Kontakte eines Nutzers, welche eine bestimmte Eigenschaft haben aus der Datenbank.
	 * 
	 * @param user Nutzer
	 * @param property Eigenschaft
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getContactsOfUserWithDefaultProperty(User user, Property property) throws IllegalArgumentException{
		
		Vector<Contact> allContactsOfUser= new Vector<Contact>();
		allContactsOfUser = getAllContactsOfActiveUser(user);
		
		Vector <Property> pv = new Vector <Property>();
		Vector<Contact> result = new Vector<Contact>();
		Contact c1 = new Contact();
		
		c1.setId(20000000);
		pv = pMapper.findByTypeAndCID(property, c1);
		
		for(Property p: pv){
			Vector<Contact> allContactsWithProperty = vMapper.getAllContactsByPID(p);
		
			for (Contact c :allContactsOfUser){
				if(allContactsWithProperty.contains(c)){
					result.add(c);
				}
			}
		}
		return result;
	}
	
	/**
	 * Holt alle Kontakte welche im Namen (Vor- oder Nachname) eine Zeichenfolge beinhalten,
	 * welche dem Suchfeld entspricht. Verwendet wird hierfür unter anderem die {@link #getAllContactsWithName(String) getAllContactsWithName}
	 * Methode.
	 *
	 * @param user Nutzer
	 * @param textBox Eingabe im Suchfeld
	 * @param selectedContactList ausgewählte Kontaktliste
	 * @return Vector aller Kontakte welche im Namen der Suche entsprechen
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt alle Kontakte welche in ihren Ausprägungen eine Zeichenfolge beinhalten, welche dem Suchfeld entspricht.
	 * Verwendet wird hierfür unter anderem die {@link #getAllContactsWithValue(String) getAllContactsWithValue} Methode.
	 *
	 * @param user Nutzer
	 * @param textBox Eingabe im Suchfeld
	 * @param selectedContactList ausgewählte Kontaktliste
	 * @return Vector aller Kontakte welche in min. einer Ausprägung der Suche entsprechen
	 * @throws IllegalArgumentException the illegal argument exception
	 */
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
	
	/**
	 * Legt einen neue Kontaktliste an.
	 *
	 * @param name Name der Kontaktliste
	 * @param user Nutzer (welcher die Kontaktliste erstellt)
	 * @return vollständiges Kontaktlisten-Objekt
	 * @throws IllegalArgumentException
	 */
	public ContactList createContactList(String name, User user) throws IllegalArgumentException {

		ContactList newcontactlist = new ContactList();
		newcontactlist.setName(name);
		newcontactlist.setOwner(user.getId());
		
		return clMapper.insert(newcontactlist, user);
	}
	
	/**
	 * Ändert den Namen der Kontaktliste in der Datenbank.
	 *
	 * @param id ID der Kontaktliste
	 * @param name neuer Name
	 * @return vollständiges Kontaktlisten-Objekt
	 * @throws IllegalArgumentException
	 */
	public ContactList editContactList(int id, String name) throws IllegalArgumentException {

		ContactList changedcontactlist = new ContactList();
		changedcontactlist.setId(id);
		changedcontactlist.setName(name);
		
		return clMapper.update(changedcontactlist);
	}
	
	/**
	 * Löscht eine Kontaktliste aus der Datenbank, falls der Nutzer jener ist, welcher die Kontaktliste erstellt hat.
	 * Löscht in diesem Fall auch alle Teilhaberschaften an der Kontaktliste und den Kontakten.
	 * Ist der Nutzer nur Teilhaber der Kontaktliste wird nur die Teilhaberschaft des Nutzers gelöscht.
	 * Dies wird über einen boolschen Wert identifiziert (true für Urheber, false für Teilhaber)
	 *
	 * @param contactlist Kontaktliste
	 * @param owner boolscher Wert zur Idetifikation der Beziehung zum Nutzer
	 * @param user Nutzer
	 * @throws IllegalArgumentException
	 */
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

	/**
	 * Fügt einen Kontakt zu einer Kontaktliste hinzu.
	 *
	 * @param contactlist Kontaktliste
	 * @param contact Kontakt
	 * @return vollständiges Kontaktlisten-Objekt
	 * @throws IllegalArgumentException
	 */
	public ContactList addContactToContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {
		
		Vector<User> participants = new Vector<User>();
		participants = pmMapper.findAllParticipantsByCLID(contactlist);
		for(User u : participants){
			shareContact(uMapper.findByID(contactlist.getOwner()), u.getEmail(), contact);
		}
		
		return clMapper.addContact(contactlist, contact);
	}

	/**
	 * Entfernt einen Kontakt aus einer Kontakliste.
	 *
	 * @param contactlist Kontaktliste
	 * @param contact Kontakt
	 * @return vollständiges Kontaktlisten-Objekt
	 * @throws IllegalArgumentException
	 */
	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact)
			throws IllegalArgumentException {

		return clMapper.removeContact(contactlist, contact);
	}
	
	/**
	 * Holt alle Kontaktlisten des angemeldeten Nutzers aus der Datenbank.
	 *
	 * @param user der angemeldete Nutzer
	 * @return Vector aller Kontaktlisten des angemeldeten Nutzers 
	 * @throws IllegalArgumentException
	 */
	public Vector<ContactList> getAllOwnedContactListsOfActiveUser(User user) throws IllegalArgumentException {
		
		return clMapper.findAllByUID(user);
	}
	
	/**
	 * Holt alle Kontaktlisten eines Nutzers aus der Datenbank, welche der Nutzer selbst erstellt hat.
	 *
	 * @param email Email des Nutzers
	 * @return Vector der selbst erstellten Kontaktlisten des Nutzers
	 * @throws IllegalArgumentException
	 */
	public Vector<ContactList> getAllContactListsOfUser(String email) throws IllegalArgumentException {
		
		Vector<ContactList> result = new Vector<ContactList>();
		
		result = clMapper.findAllByUID(getUserByEmail(email));
		result.addAll(pmMapper.getAllContactListsByUID(getUserByEmail(email)));

		return result;
	}

	/**
	 * Holte alle Kontaktlisten, welche einen bestimmten Kontakt enthalten, aus der Datenbank.
	 *
	 * @param contact der Kontakt
	 * @return Vector der betroffenen Kontaktlisten
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Legt eine neuen, nicht vordedinierte Eigenschaft für einen bestimmten Kontakt in der Datenbank an.
	 *
	 * @param contact Kontakt
	 * @param type Art der Eigenschaft
	 * @return vollständiges Eigenschaft-Objekt
	 * @throws IllegalArgumentException
	 */
	public Property createProperty(Contact contact, String type) throws IllegalArgumentException {
		
		Property newProperty = new Property();
		newProperty.setType(type);
		
		if(pMapper.findByTypeAndCID(newProperty, contact).size() == 0) {
			return pMapper.insert(newProperty, contact);
		}else {
			return null;
		}
	}

	/**
	 * Ändert eine Eigenschaft in der Datenbank.
	 * 
	 * @param property neue Eigenschaft
	 * @return vollständiges Eigenschaft Objekt
	 * @throws IllegalArgumentException
	 */
	public Property editProperty(Property property) throws IllegalArgumentException{
		return pMapper.update(property);
	}
	
	/**
	 * Löscht eine Eigenschaft aus der Datenbank.
	 * 
	 * @param property Eigenschaft
	 * @throws IllegalArgumentException
	 */
	public void deleteProperty(Property property) throws IllegalArgumentException{
		pMapper.delete(property);
	}
	
	/**
	 * Holt die Eigenschaft aus der Datenbanke, zu welcher eine bestimmte Ausprägung gehört.
	 *
	 * @param value Ausprägung
	 * @return vollständiges Eigenschaft-Objekt
	 * @throws IllegalArgumentException
	 */
	public Property getPropertyOfValue(Value value) throws IllegalArgumentException {
		
		Property property = new Property();
		property.setId(value.getPropertyid());
		
		return pMapper.findByID(property);
	}
	
	/**
	 * Holt eine Eigenschaft anhand ihrer Art aus der Datenbank
	 *
	 * @param type die Eigenschaftsart
	 * @return vollständiges Eigenschaft-Objekt
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt alle vordefinierten Eigenschaften (Telefonnummer geschäftlich, Telefonnummer privat, Email-Adresse,
	 * Geburtstag, Arbeitsplatz, Straße, Hausnummer, PLZ, Wohnort, Homepage) aus der Datenbank.
	 *
	 * @return Vector der vordefinierten Eigenschaften
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Legt eine Ausprägung zu einer Eigenschaft für einen bestimmten Kontakt in der Datenbank an.
	 * Nur der Urheber eines Kontakts kann Ausprägungen zum Kontakt hinzufügen. 
	 *
	 * @param contact Kontakt
	 * @param propertyid ID der Eigenschaft
	 * @param content Inhalt der Ausprägung
	 * @return vollständiges Ausprägung-Objekt
	 * @throws IllegalArgumentException
	 */
	public Value createValue(Contact contact, int propertyid, String content) throws IllegalArgumentException {
				
		Value newvalue = new Value();
		newvalue.setPropertyid(propertyid);
		newvalue.setContent(content);
		Property property = new Property();
		property.setId(propertyid);
		
		Value addedValue = vMapper.insert(newvalue, contact, property);
		
		cMapper.update(vMapper.findContactByVID(addedValue));
		
		return addedValue;
	}
	
	/**
	 * Ändert eine Ausprägung in der Datenbank.
	 * Wird sowohl für die Änderung des Inhalts der Ausprägung genutzt, als auch für das Ändern ob eine
	 * Ausprägung geteilt werden soll oder nicht (true für teilen, false für nicht teilen).
	 *
	 * @param contact Kontakt
	 * @param propertyId ID der Eigenschaft
	 * @param value Ausprägung
	 * @param content neuer Inhalt der Ausprägung
	 * @param isshared Flag ob Ausprägung geteilt wird oder nicht
	 * @return vollständiges Ausprägung-Objekt
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Löscht eine bestimmte Ausprägung aus der Datenbank.
	 * Nur der Urheber eines Kontakts kann Ausprägungen löschen.
	 *
	 * @param value betroffene Ausprägung
	 * @throws IllegalArgumentException
	 */
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
				vMapper.delete(value);
				pMapper.delete(extraProperty);
			}
		}
	}
	
	/**
	 * Legt neue Adresse, bestehend aus Straßenname, Hausnummer, PLZ und dem Wohnort (Stadt)
	 * in der Datenbank an.
	 *
	 * @param street Straßenname
	 * @param housenumber Hausnummer
	 * @param zip PLZ
	 * @param city Wohnort bzw. Stadt
	 * @param contact Kontakt
	 * @return vollständiges Ausprägung-Objekt
	 * @throws IllegalArgumentException
	 */
	public Value createAddress(String street, String housenumber, String zip, String city, Contact contact) throws IllegalArgumentException{
		
		Value streetValue = createValue(contact, 6, street);
		createValue(contact, 7, housenumber);
		createValue(contact, 8, zip);
		createValue(contact, 9, city);
		
		return streetValue;
	}
	
	/**
	 * Löscht die vier Ausprägungen Straße, Hausnummer, PLZ und Stadt, welche eine Adresse repräsentieren.
	 * 
	 * @param street Straße
	 * @param houseNr Hausnummer
	 * @param zip PLZ
	 * @param city Stadt
	 * @throws IllegalArgumentException
	 */
	public void deleteAddress(Value street, Value houseNr, Value zip, Value city) throws IllegalArgumentException {
		
		deleteValue(street);
		deleteValue(houseNr);
		deleteValue(zip);
		deleteValue(city);
	}
	
	/**
	 * Holt alle Ausprägungen eines bestimmten Kontakts aus der Datenbank.
	 *
	 * @param contact Kontakt
	 * @return Vector der Ausprägungen
	 * @throws IllegalArgumentException
	 */
	public Vector<Value> getAllValuesOfContact(Contact contact) throws IllegalArgumentException {
		
		return vMapper.getAllValueByCID(contact);
	}
	
	/**
	 * Holt alle Ausprägungen eines Kontakts aus der Datenbank, welche als geteilt markiert sind.
	 *
	 * @param contact Kontakt
	 * @return Vector der geteilten Ausprägungen
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Teilt einen Kontakt mit einem bestimmten Nutzer.
	 * Entspricht "creatPermissionForContact".
	 *
	 * @param sourceUser "teilender" Nutzer
	 * @param shareUserEmail Email-Adresse des Empfängers
	 * @param shareContact Kontakt
	 * @return ein vollständiges Teilhaberschaft-Objekt
	 * @throws IllegalArgumentException
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

	/**
	 * Teilt Kontaktliste mit einem Nutzer.
	 * Entspricht "createPermissionForContactList".
	 *
	 * @param sourceUser "teilender" Nutzer
	 * @param shareUserEmail Email-Adress des Empfängers
	 * @param shareContactList Kontaktliste
	 * @return vollständiges Teilhaberschaft-Objekt
	 * @throws IllegalArgumentException
	 */
	public Permission shareContactList(User sourceUser, String shareUserEmail, ContactList shareContactList) throws IllegalArgumentException {
		
		if(uMapper.findByEMail(shareUserEmail).getId() != shareContactList.getOwner()) {
			Permission newCLpermission = new Permission();
			newCLpermission.setSourceUserID(sourceUser.getId());
			newCLpermission.setParticipantID(uMapper.findByEMail(shareUserEmail).getId());
			newCLpermission.setShareableObjectID(shareContactList.getId());
			
			Vector<Contact> allContacts = getAllContactsOfContactList(shareContactList);
			for(Contact c : allContacts) {
				shareContact(sourceUser, shareUserEmail, c);
			}
			
			return pmMapper.shareContactList(newCLpermission);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Ändert eine Teilhaberschaft an einem Kontakt in der Datenbank.
	 * 
	 * @param permission neue Teilhaberschaft
	 * @return vollständiges Teilhaberschaft Objekt
	 * @throws IllegalArgumentException
	 */
	public Permission editPermissionContact(Permission permission) throws IllegalArgumentException{
		return pmMapper.updateContact(permission);
	}
	
	/**
	 * Ändert eine Teilhaberschaft an einer Kontaktliste in der Datenbank.
	 * 
	 * @param permission neue Teilhaberschaft
	 * @return vollständiges Teilhaberschaft Objekt
	 * @throws IllegalArgumentException
	 */
	public Permission editPermissionContactList(Permission permission) throws IllegalArgumentException{
		return pmMapper.updateContactList(permission);
	}

	/**
	 * Löscht die Teilhaberschaft eines Nutzers an einem Geschäftsobjekt (Kontakt oder Kontaktliste).
	 * Wird in den Methoden {@link #deleteContact(Contact, boolean, User) deleteContact} und
	 * {@link #deleteContactList(ContactList, boolean, User) deleteContactList} aufgerufen falls der Nutzer
	 * nur Teilhaber ist.
	 *
	 * @param user Nutzer
	 * @param bo Geschäftsobjekt (Kontakt oder Kontaktliste)
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt alle Teilhaberschaften aus der Datenbank.
	 *
	 * @return Vector aller Teilhaberschaften
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt Klarnamen eines Nutzers aus der Datenbank.
	 * 
	 * @param user Nutzer
	 * @return Klarname des Nutzers
	 * @throws IllegalArgumentException
	 */
	public String getFullNameOfUser(User user) throws IllegalArgumentException {
		
		String result = null;
		Vector<Contact> contacts = getAllOwnedContactsOfUser(user.getEmail());
		for(Contact c : contacts) {
			if (c.getIsUser()) {
				result = c.getFirstname() + " " + c.getLastname();
				return result;
			}
		}
		return result;
	}
	
	/**
	 * Holt Klarnamen mehrer Nutzer auf einmal aus der Datenbank.
	 * 
	 * @param user Nutzer
	 * @return Vector der Klarnamen
	 * @throws IllegalArgumentException
	 */
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
	
	/**
	 * Holt Strings zum füllen der SuggestBox der {@link InputDialogBox} Klasse aus der Datenbank.
	 * Diese beinhalten nicht den Namen des angemeldeten Nutzers.
	 * 
	 * @param activeUser angemeldeter Nutzer
	 * @return Vector der Strings
	 * @throws IllegalArgumentException
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
}

package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.client.ClientsideFunctions.InputDialogBox;
import com.google.gwt.sample.itProjekt.shared.bo.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/*
 * ***************************************************************************
 * ABSCHNITT, Beginn: Initialisierung
 * ***************************************************************************
 */

/**
 * <p>
 * Synchrone Schnittstelle für eine RPC-fähige Klasse zur Verwaltung von Kontakten
 * und Kontaktlisten.
 * </p>
 * 
 * @author JanNoller
 */
@RemoteServiceRelativePath("editoradministration")
public interface EditorAdministration extends RemoteService{
	
	/**
	 * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von GWT
     * RPC zusätzlich zum No Argument Constructor der implementierenden Klasse
     * {@link EditorAdministrationImpl} notwendig.
     * 
     * @throws IllegalArgumentException
	 */
	public void init() throws IllegalArgumentException;
	
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
	public User createUser(String email) throws IllegalArgumentException;

	/**
	 * Überschreibt einen Nutzer in der Datenbank.
	 * 
	 * @param user neuer Nutzer
	 * @return vollständiges Nutzer-Objekt
	 * @throws IllegalArgumentException
	 */
	public User editUser(User user) throws IllegalArgumentException;

	/**
	 * Löscht einen Nutzer aus der Datenbank.
	 * 
	 * @param u Nutzer
	 * @throws IllegalArgumentException
	 */
	public void deleteUser(User u) throws IllegalArgumentException;
	
	/**
	 * Holt die Information des Nutzers anhand seiner Email-Adresse aus der Datenbank.
	 *
	 * @param email Email des Nutzers
	 * @return ein vollständiges User Objekt
	 * @throws IllegalArgumentException
	 */
	public User getUserByEmail (String email) throws IllegalArgumentException;

	/**
	 * Holt die Information des Nutzers anhand seiner ID aus der Datenbank.
	 *
	 * @param ID ID des Nutzers in der Datenbank
	 * @return ein vollständiges User-Objekt
	 * @throws IllegalArgumentException
	 */
	public User getUserByID (int ID) throws IllegalArgumentException;

	/**
	 * Legt einen neuen Kontakt, welcher einen Nutzer repräsentiert, an.
	 *
	 * @param firstname Vorname
	 * @param lastname Nachname
	 * @param sex Geschlecht
	 * @param email Email-Adresse
	 * @return vollständiges Nutzer-Objekt
	 * @throws IllegalArgumentException
	 */
	public User createUserContact(String firstname, String lastname, String sex, String email) throws IllegalArgumentException;

	/**
	 * Holt den Urheber eines Kontakts aus der Datenbank.
	 *
	 * @param contact Kontakt
	 * @return vollständiges Nutzer-Objekt des Urhebers
	 * @throws IllegalArgumentException
	 */
	public User getOwnerOfContact(Contact contact) throws IllegalArgumentException;

	/**
	 * Holt zu einem Teilhaber eines Kontakt und dem Kontakt den Nutzer aus der Datenbank,
	 * welcher den Kontakt geteilt hat.
	 *
	 * @param contact Kontakt
	 * @param receivingUser Teilhaber des Kontakts
	 * @return vollständiges Nutzer-Objekt des Nutzers, welcher den Kontakt geteilt hat
	 * @throws IllegalArgumentException
	 */
	public User getSourceToSharedContact(Contact contact, User receivingUser) throws IllegalArgumentException;

	/**
	 * Holt alle Nutzer aus der Datenbank.
	 *
	 * @return Vector der Nutzer
	 * @throws IllegalArgumentException
	 */
	public Vector<User> getAllUsers() throws IllegalArgumentException;

	/**
	 * Holt alle Teilhaber eines Kontakts aus der Datenbank.
	 *
	 * @param contact Kontaktliste
	 * @return Vector der betroffenen Nutzer
	 * @throws IllegalArgumentException
	 */
	public Vector<User> getAllParticipantsOfContact(Contact contact) throws IllegalArgumentException;
	
	/**
	 * Holt alle Teilhaber einer Kontaktliste aus der Datenbank.
	 *
	 * @param contactlist Kontaktliste
	 * @return Vector der betroffenen Nutzer
	 * @throws IllegalArgumentException
	 */
	public Vector<User> getAllParticipantsOfContactList(ContactList contactlist) throws IllegalArgumentException;
	
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
	 * Holt die Information ob der Nutzer bereits aus der Datenbank.
	 * Identifiziert wird dies über einen boolschen Rückgabewert, true wenn Nutzer bereits existiert, sonst false.
	 * 
	 * @param email Email des Nutzers
	 * @return boolscher Wert
	 * @throws IllegalArgumentException
	 */
	public boolean isUserKnown (String email) throws IllegalArgumentException;
	
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
	public Contact createContact(String firstname, String lastname, String sex, User user) throws IllegalArgumentException;

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
	public Contact editContact(int id, String firstname, String lastname, String sex) throws IllegalArgumentException;

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
	public void deleteContact(Contact contact, boolean owner, User user) throws IllegalArgumentException;

	/**
	 * Holt einen Kontakt anhand seiner ID aus der Datenbank.
	 *
	 * @param id die ID
	 * @return der Kontakt
	 * @throws IllegalArgumentException
	 */
	public Contact getContactByID(int id) throws IllegalArgumentException;

	/**
	 * Holt alle Kontakte des angemeldeten Nutzers aus der Datenbank.
	 *
	 * @param user der angemeldete Nutzer
	 * @return Vector aller Kontakte des angemeldeten Nutzers 
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsOfActiveUser(User user) throws IllegalArgumentException;
	
	/**
	 * Holt alle Kontakte eines Nutzers aus der Datenbank, welche der Nutzer selbst erstellt hat.
	 *
	 * @param email Email des Nutzers
	 * @return Vector der selbst erstellten Kontakte des Nutzers
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllOwnedContactsOfUser(String email) throws IllegalArgumentException;

	/**
	 * Holt alle Kontakte aus der Datenbank, welche mit einem bestimmten Nutzer geteilt wurden.
	 *
	 * @param email Email des Nutzers
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllSharedContactsWithUser(String email) throws IllegalArgumentException;

	/**
	 * Holt alle Kontakte aus der Datenbank, welche ein Nutzer mit einem anderen geteilt hat.
	 *
	 * @param source der "teilende" Nutzer
	 * @param receiver die Email-Adresse des Empfängers der Kontakte
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllSharedContactsOfUserWithOtherUser(User source, String receiver) throws IllegalArgumentException;

	/**
	 * Holt alle enthaltenen Kontakte einer Kontaktliste für einen bestimmten Nutzer aus der Datenbank.
	 *
	 * @param contactlist betroffene Kontaktliste
	 * @param user betroffener Nutzer
	 * @return Vector aller Kontakte der Kontaktliste für den Nutzer
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsOfContactlistForUser(ContactList contactlist, User user) throws IllegalArgumentException;

	/**
	 * Holt alle Kontagte einer bestimmten Kontaktliste aus der Datenbank, unabhängig vom Nutzer.
	 *
	 * @param contactlist die Kontaktliste
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsOfContactList(ContactList contactlist) throws IllegalArgumentException;

	/**
	 * Holt alle Kontakte mit einem bestimmten Inhalt in den Ausprägungen aus der Datenbank.
	 *
	 * @param content der gesuchte Inhalt
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsWithValue(String content) throws IllegalArgumentException;

	/**
	 * Holt alle Kontakte mit einem bestimmten String im Namen (Vor oder Nachname) aus der Datenbank.
	 *
	 * @param name gesuchter Name
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsWithName(String name) throws IllegalArgumentException;
	
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
	public Vector<Contact> getAllSharedContactsOfContactList(ContactList contactlist, User user) throws IllegalArgumentException;

	/**
	 * Holt alle Kontakte mit einer bestimmten Ausprägung eines bestimmten Nutzers aus der Datenbank.
	 *
	 * @param user Nutzer
	 * @param value Ausprägung
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getAllContactsOfUserWithValue(User user, Value value) throws IllegalArgumentException;

	/**
	 * Holt alle Kontakte mit einer bestimmten Eigenschaft eines bestimmten Nutzers aus der Datenbank.
	 *
	 * @param user Nutzer
	 * @param Property Eigenschaft
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getContactsOfUserWithProperty(User user, Property Property) throws IllegalArgumentException;

	/**
	 * Holt alle Kontakte eines Nutzers, welche eine bestimmte Eigenschaft haben aus der Datenbank.
	 * 
	 * @param user Nutzer
	 * @param property Eigenschaft
	 * @return Vector der betroffenen Kontakte
	 * @throws IllegalArgumentException
	 */
	public Vector<Contact> getContactsOfUserWithDefaultProperty(User user, Property property) throws IllegalArgumentException;

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
	public Vector<Contact> getContactsOfNameSearchResult(User user, String textBox, ContactList selectedContactList) throws IllegalArgumentException;

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
	public Vector<Contact> getContactsOfValueSearchResult(User user, String textBox, ContactList selectedContactList) throws IllegalArgumentException;

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
	public ContactList createContactList(String name, User user) throws IllegalArgumentException;

	/**
	 * Ändert den Namen der Kontaktliste in der Datenbank.
	 *
	 * @param id ID der Kontaktliste
	 * @param name neuer Name
	 * @return vollständiges Kontaktlisten-Objekt
	 * @throws IllegalArgumentException
	 */
	public ContactList editContactList(int id, String name) throws IllegalArgumentException;

	/**
	 * Löscht eine Kontaktliste aus der Datenbank, falls der Nutzer jener ist, welcher die Kontaktliste erstellt hat.
	 * Ist der Nutzer nur Teilhaber der Kontaktliste wird nur die Teilhaberschaft des Nutzers gelöscht.
	 * Dies wird über einen boolschen Wert identifiziert (true für Urheber, false für Teilhaber)
	 *
	 * @param contactlist Kontaktliste
	 * @param owner boolscher Wert zur Idetifikation der Beziehung zum Nutzer
	 * @param user Nutzer
	 * @throws IllegalArgumentException
	 */
	public void deleteContactList(ContactList contactlist, boolean owner, User user) throws IllegalArgumentException;

	/**
	 * Fügt einen Kontakt zu einer Kontaktliste hinzu.
	 *
	 * @param contactlist Kontaktliste
	 * @param contact Kontakt
	 * @return vollständiges Kontaktlisten-Objekt
	 * @throws IllegalArgumentException
	 */
	public ContactList addContactToContactList(ContactList contactlist, Contact contact) throws IllegalArgumentException;

	/**
	 * Entfernt einen Kontakt aus einer Kontakliste.
	 *
	 * @param contactlist Kontaktliste
	 * @param contact Kontakt
	 * @return vollständiges Kontaktlisten-Objekt
	 * @throws IllegalArgumentException
	 */
	public ContactList removeContactFromContactList(ContactList contactlist, Contact contact) throws IllegalArgumentException;

	/**
	 * Holt alle Kontaktlisten des angemeldeten Nutzers aus der Datenbank.
	 *
	 * @param user der angemeldete Nutzer
	 * @return Vector aller Kontaktlisten des angemeldeten Nutzers 
	 * @throws IllegalArgumentException
	 */
	public Vector<ContactList> getAllOwnedContactListsOfActiveUser(User user) throws IllegalArgumentException;
	
	/**
	 * Holt alle Kontaktlisten eines Nutzers aus der Datenbank, welche der Nutzer selbst erstellt hat.
	 *
	 * @param email Email des Nutzers
	 * @return Vector der selbst erstellten Kontaktlisten des Nutzers
	 * @throws IllegalArgumentException
	 */
	public Vector<ContactList> getAllContactListsOfUser(String email) throws IllegalArgumentException;
	
	/**
	 * Holte alle Kontaktlisten, welche einen bestimmten Kontakt enthalten, aus der Datenbank.
	 *
	 * @param contact der Kontakt
	 * @return Vector der betroffenen Kontaktlisten
	 * @throws IllegalArgumentException
	 */
	public Vector<ContactList> getAllContactListsWithContact(Contact contact) throws IllegalArgumentException;
	
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
	public Property createProperty(Contact contact, String type) throws IllegalArgumentException;

	/**
	 * Ändert eine Eigenschaft in der Datenbank.
	 * 
	 * @param property neue Eigenschaft
	 * @return vollständiges Eigenschaft Objekt
	 * @throws IllegalArgumentException
	 */
	public Property editProperty(Property property) throws IllegalArgumentException;

	/**
	 * Löscht eine Eigenschaft aus der Datenbank.
	 * 
	 * @param property Eigenschaft
	 * @throws IllegalArgumentException
	 */
	public void deleteProperty(Property property) throws IllegalArgumentException;

	/**
	 * Holt die Eigenschaft aus der Datenbanke, zu welcher eine bestimmte Ausprägung gehört.
	 *
	 * @param value Ausprägung
	 * @return vollständiges Eigenschaft-Objekt
	 * @throws IllegalArgumentException
	 */
	public Property getPropertyOfValue(Value value) throws IllegalArgumentException;

	/**
	 * Holt eine Eigenschaft anhand ihrer Art aus der Datenbank
	 *
	 * @param type die Eigenschaftsart
	 * @param contact Kontakt
	 * @return vollständiges Eigenschaft-Objekt
	 * @throws IllegalArgumentException
	 */
	public Property getPropertyByType(String type, Contact contact) throws IllegalArgumentException;

	/**
	 * Holt alle vordefinierten Eigenschaften (Telefonnummer geschäftlich, Telefonnummer privat, Email-Adresse,
	 * Geburtstag, Arbeitsplatz, Straße, Hausnummer, PLZ, Wohnort, Homepage) aus der Datenbank.
	 *
	 * @return Vector der vordefinierten Eigenschaften
	 * @throws IllegalArgumentException
	 */
	public Vector<Property> getAllPredefinedPropertiesOf() throws IllegalArgumentException;
	
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
	public Value createValue(Contact contact, int propertyid, String content) throws IllegalArgumentException;

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
	public Value editValue(Contact contact, int propertyId, Value value, String content, boolean isshared);

	/**
	 * Löscht eine bestimmte Ausprägung aus der Datenbank.
	 * Nur der Urheber eines Kontakts kann Ausprägungen löschen.
	 *
	 * @param value betroffene Ausprägung
	 * @throws IllegalArgumentException
	 */
	public void deleteValue(Value value) throws IllegalArgumentException;

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
	public Value createAddress(String street, String housenumber, String zip, String city, Contact contact) throws IllegalArgumentException;

	/**
	 * Löscht die vier Ausprägungen Straße, Hausnummer, PLZ und Stadt, welche eine Adresse repräsentieren.
	 * 
	 * @param street Straße
	 * @param houseNr Hausnummer
	 * @param zip PLZ
	 * @param city Stadt
	 * @throws IllegalArgumentException
	 */
	public void deleteAddress(Value street, Value houseNr, Value zip, Value city) throws IllegalArgumentException;
	
	/**
	 * Holt alle Ausprägungen eines bestimmten Kontakts aus der Datenbank.
	 *
	 * @param contact Kontakt
	 * @return Vector der Ausprägungen
	 * @throws IllegalArgumentException
	 */
	public Vector<Value> getAllValuesOfContact(Contact contact) throws IllegalArgumentException;

	/**
	 * Holt alle Ausprägungen eines Kontakts aus der Datenbank, welche als geteilt markiert sind.
	 *
	 * @param contact Kontakt
	 * @return Vector der geteilten Ausprägungen
	 * @throws IllegalArgumentException
	 */
	public Vector<Value> getAllSharedValuesOfContact(Contact contact) throws IllegalArgumentException;
	
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
	 *
	 * @param sourceUser "teilender" Nutzer
	 * @param shareUserEmail Email-Adresse des Empfängers
	 * @param shareContact Kontakt
	 * @return ein vollständiges Teilhaberschaft-Objekt
	 * @throws IllegalArgumentException
	 */
	public Permission shareContact(User sourceUser, String shareUserEmail, Contact shareContact) throws IllegalArgumentException;

	/**
	 * Teilt Kontaktliste mit einem Nutzer.
	 *
	 * @param sourceUser "teilender" Nutzer
	 * @param shareUserEmail Email-Adress des Empfängers
	 * @param shareContactList Kontaktliste
	 * @return vollständiges Teilhaberschaft-Objekt
	 * @throws IllegalArgumentException
	 */
	public Permission shareContactList(User sourceUser, String shareUserEmail, ContactList shareContactList) throws IllegalArgumentException;

	/**
	 * Ändert eine Teilhaberschaft an einem Kontakt in der Datenbank.
	 * 
	 * @param permission neue Teilhaberschaft
	 * @return vollständiges Teilhaberschaft Objekt
	 * @throws IllegalArgumentException
	 */
	public Permission editPermissionContact(Permission permission) throws IllegalArgumentException;
	
	/**
	 * Ändert eine Teilhaberschaft an einer Kontaktliste in der Datenbank.
	 * 
	 * @param permission neue Teilhaberschaft
	 * @return vollständiges Teilhaberschaft Objekt
	 * @throws IllegalArgumentException
	 */
	public Permission editPermissionContactList(Permission permission) throws IllegalArgumentException;

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
	public void deletePermission(User user, BusinessObject bo) throws IllegalArgumentException;

	/**
	 * Holt alle Teilhaberschaften aus der Datenbank.
	 *
	 * @return Vector aller Teilhaberschaften
	 * @throws IllegalArgumentException
	 */
	public Vector<Permission> getAllPermissions() throws IllegalArgumentException;
	
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
	public String getFullNameOfUser(User user) throws IllegalArgumentException;

	/**
	 * Holt Klarnamen mehrer Nutzer auf einmal aus der Datenbank.
	 * 
	 * @param user Nutzer
	 * @return Vector der Klarnamen
	 * @throws IllegalArgumentException
	 */
	public Vector<String> getFullNamesOfUsers(Vector<User> user) throws IllegalArgumentException;
	
	/**
	 * Holt Strings zum füllen der SuggestBox der {@link InputDialogBox} Klasse aus der Datenbank.
	 * Diese beinhalten nicht den Namen des angemeldeten Nutzers.
	 * 
	 * @param activeUser angemeldeter Nutzer
	 * @return Vector der Strings
	 * @throws IllegalArgumentException
	 */
	public Vector<String> getAllUserSuggestions(User activeUser) throws IllegalArgumentException;
}

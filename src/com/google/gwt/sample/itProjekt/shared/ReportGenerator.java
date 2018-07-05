package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactInformationOfContactReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithPropertyReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllValuesOfContactReport;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Das ReportGenerator Interface.
 */
@RemoteServiceRelativePath("reportgenerator")
public interface ReportGenerator extends RemoteService{
	
	/**
	 * Die Methode init, welche die Editor Administrations instanziiert.
	 *
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	
	public void init() throws IllegalArgumentException;
	
	/**
	 * Die Methode getUserInformation welche das Nutzer Obekt befüllt, welche in der Präsentationsschicht benötigt wird.
	 *
	 * @param email the email
	 * @return das User-Objekt
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	
	public User getUserInformation (String email) throws IllegalArgumentException;
	
	/**
	 * Generiert den Report für alle Kontakte des angemeldeten Users.
	 *
	 * @param u the u
	 * @return den Report für alle Kontakte eines Users
	 * @throws IllegalArgumentException die illegale Argument Exception
	 */
	public abstract AllContactsOfUserReport generateAllContactsOfUserReport(User u) throws IllegalArgumentException;
	
	/**
	 * Generiert den Report für alle geteilten Kontakte des angemeldeten Users mit einem bestimmten Nutzer.
	 *
	 * @param owner the owner
	 * @param receiver the receiver
	 * @return den Report für alle geteilten Kontakte eines Users
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public abstract AllSharedContactsOfUserReport generateAllSharedContactsOfUserReport(User owner, User receiver) throws IllegalArgumentException;
	

	/**
	 * Generiert den Report für alle Kontakte des angemeldeten Users mit einer bestimmten Eigenschaft.
	 *
	 * @param user the user
	 * @param property the property
	 * @return Report für alle Kontakte mit einer bestimmten Eigenschaft
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public abstract AllContactsWithPropertyReport generateAllContactsWithPropertyReport(User user, Property property) throws IllegalArgumentException;

	/**
	 * Generiert den Report für alle Kontakte des angemeldeten Users mit einer bestimmten Ausprägung.
	 *
	 * @param user the user
	 * @param value the value
	 * @return den Report für alle Kontakte mit einer bestimmten Ausprägung.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	
	public abstract AllContactsWithValueReport generateAllContactsWithValueReport(User user, Value value) throws IllegalArgumentException;

	public abstract AllValuesOfContactReport generateAllValuesOfContactReport(Contact contact, User user) throws IllegalArgumentException;
	
	public abstract AllContactInformationOfContactReport generateAllContactInformationOfContactReport(Contact contact, User user) throws IllegalArgumentException;
		
	/**
	 * Auslesen aller im System angemeldeten Nutzer.
	 *
	 * @return Alle angemeldeten Nutzer all users
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public abstract Vector<User> getAllUsers() throws IllegalArgumentException;

	/**
	 * Auslesen der vordefinierten Eigenschaften.
	 *
	 * @return alle vordefinierten Eigenschaften für den Report
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public abstract Vector<Property> getAllPredefinedPropertiesOfReport() throws IllegalArgumentException;



	
}

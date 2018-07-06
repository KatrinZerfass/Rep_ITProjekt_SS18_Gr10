package com.google.gwt.sample.itProjekt.server;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.EditorAdministration;
import com.google.gwt.sample.itProjekt.shared.ReportGenerator;
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
import com.google.gwt.sample.itProjekt.shared.report.Column;
import com.google.gwt.sample.itProjekt.shared.report.CompositeParagraph;
import com.google.gwt.sample.itProjekt.shared.report.Row;
import com.google.gwt.sample.itProjekt.shared.report.SimpleParagraph;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Die ReportGeneratorImpl Klasse.
 */
@SuppressWarnings("serial")
public class ReportGeneratorImpl extends RemoteServiceServlet implements ReportGenerator{
	
	/** Die Instanz der Klasse der Editor Administration . */
	private EditorAdministration admin = null;
	
	/**
	 * Der Konstruktor für der ReportGeneratorImpl.
	 *
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public ReportGeneratorImpl () throws IllegalArgumentException {
	}

	
/**
 * Initiiert die EditorAdministrationImpl, damit man auf die Funktionen der EditorAdministration Impl zugreifen kann.
 *
 * @throws IllegalArgumentException the illegal argument exception
 * @see javax.servlet.GenericServlet#init()
 */
@Override
	public void init() throws IllegalArgumentException{
		EditorAdministrationImpl a = new EditorAdministrationImpl();
		a.init();
		this.admin=a;
	}

	/**
	 * Getter für das EditorAdministrationsobjekt.
	 *
	 * @return das editor administrations objekt
	 */
	protected EditorAdministration getEditorAdministration() {
		return this.admin;
	}	
	
	
	/**
	 * Identifizierung des angemeldeten Users.
	 *
	 * @param email the email
	 * @return the user information
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	
	public User getUserInformation (String email) throws IllegalArgumentException{
		return this.admin.getUser(email);
	}
	

/* (non-Javadoc)
 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#generateAllContactsOfUserReport(com.google.gwt.sample.itProjekt.shared.bo.User)
 */
@Override	
	public AllContactsOfUserReport generateAllContactsOfUserReport(User user) {
		if(this.getEditorAdministration()==null) {
			return null;
		} 
		else {
			AllContactsOfUserReport report = new AllContactsOfUserReport();
			
			report.setTitle("Alle Kontakte des Nutzers");
			report.setCreated(new Date());

			CompositeParagraph header=new CompositeParagraph();
			
			Vector<User> owneru=new Vector<User>();
			owneru.add(user);
			Vector<String> exactNameOfOwner=this.admin.getFullNamesOfUsers(owneru);
			for (String s: exactNameOfOwner){
				header.addSubParagraph(new SimpleParagraph("Alle Kontakte des Nutzers: " + s));
			}
			
			report.setHeaderData(header);
						
			Vector<Contact> allContacts=this.admin.getAllContactsOfActiveUser(user);
			if(allContacts.size()!=0){
				for (Contact c: allContacts) {
					report.addSubReport(this.generateAllContactInformationOfContactReport(c, user));
					Vector<Value> allValues=this.admin.getAllValuesOfContact(c);
					if (allValues.size()!=0){
					report.addSubReport(this.generateAllValuesOfContactReport(c, user));
					}
					}			
			}else{
				SimpleParagraph errornote=new SimpleParagraph("Es wurden leider keine Kontakte des Nutzers gefunden");
				header.addSubParagraph(errornote);
				report.setHeaderData(header);
			}
			return report;
		}
}
	
/* (non-Javadoc)
 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#generateAllSharedContactsOfUserReport(com.google.gwt.sample.itProjekt.shared.bo.User)
 */
@Override	
	public AllSharedContactsOfUserReport generateAllSharedContactsOfUserReport(User owner, User receiver) {
	if(this.getEditorAdministration()==null) {
		return null;
	} 
	else {
		AllSharedContactsOfUserReport report = new AllSharedContactsOfUserReport();
		
		report.setTitle("Alle mit dem angegebenen Nutzer geteilten Kontakte");
		report.setCreated(new Date());
		
		CompositeParagraph header=new CompositeParagraph();
		
		Vector<User> owneru=new Vector<User>();
		owneru.add(owner);
		Vector<String> exactNameOfOwner=this.admin.getFullNamesOfUsers(owneru);
		for (String s: exactNameOfOwner){
			header.addSubParagraph(new SimpleParagraph("Angemeldeter Nutzer: " + s));
		}
		
		Vector<User> receiveru=new Vector<User>();
		receiveru.add(receiver);
		Vector<String> exactNameOfReceiver=this.admin.getFullNamesOfUsers(receiveru);
		for (String s: exactNameOfReceiver){
			header.addSubParagraph(new SimpleParagraph("Gesuchter Nutzer: " + s));
		}
		

		
		report.setHeaderData(header);
		
		Vector<Contact> allContacts=this.admin.getAllSharedContactsOfUserWithOtherUser(owner, receiver.getEmail());
		if(allContacts.size()!=0){
			for (Contact c: allContacts) {
				report.addSubReport(this.generateAllContactInformationOfContactReport(c, owner));
				Vector<Value> allValues=this.admin.getAllValuesOfContact(c);
				if (allValues.size()!=0){
				report.addSubReport(this.generateAllValuesOfContactReport(c, owner));
				}
			}			
		}else{
			SimpleParagraph errornote=new SimpleParagraph("Bisher wurden keine Kontakte mit dem eingegebenen Nutzer geteilt.");
			header.addSubParagraph(errornote);
			report.setHeaderData(header);
		}
		return report;
	}
}

/* (non-Javadoc)
 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#generateAllContactsWithValueReport(com.google.gwt.sample.itProjekt.shared.bo.Value)
 */
@Override	
	public AllContactsWithValueReport generateAllContactsWithValueReport(User user, Value value) {
	if(this.getEditorAdministration()==null) {
		return null;
	} 
	else {
		AllContactsWithValueReport report = new AllContactsWithValueReport();
		report.setTitle("Alle Kontakte mit der Ausprägung");
		report.setCreated(new Date());
		
		CompositeParagraph header=new CompositeParagraph();
		
		Vector<User> owneru=new Vector<User>();
		owneru.add(user);
		Vector<String> exactNameOfOwner=this.admin.getFullNamesOfUsers(owneru);
		for (String s: exactNameOfOwner){
			header.addSubParagraph(new SimpleParagraph("Angemeldeter Nutzer: " + s));
		}
		header.addSubParagraph(new SimpleParagraph("Gesuchte Ausprägung: " + value.getContent()));
		
		report.setHeaderData(header);
		
		Vector<Contact> allContacts=this.admin.getAllContactsOfUserWithValue(user, value);
		
		if(allContacts.size()!=0){
			for (Contact c: allContacts) {
				report.addSubReport(this.generateAllContactInformationOfContactReport(c, user));
				Vector<Value> allValues=this.admin.getAllValuesOfContact(c);
				if (allValues.size()!=0){
				report.addSubReport(this.generateAllValuesOfContactReport(c, user));
				}
			}			
		}else{
			SimpleParagraph errornote=new SimpleParagraph("Es wurden leider keine Kontakte mit der angegebenen Ausprägung gefunden");
			header.addSubParagraph(errornote);
			report.setHeaderData(header);
		}
		return report;
	}
}

/* (non-Javadoc)
 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#generateAllContactsWithPropertyReport(com.google.gwt.sample.itProjekt.shared.bo.User, com.google.gwt.sample.itProjekt.shared.bo.Property)
 */
@Override
	public AllContactsWithPropertyReport generateAllContactsWithPropertyReport(User user, Property property){
	
	if(this.getEditorAdministration()==null) {
		
		return null;
	} 
	else {
		AllContactsWithPropertyReport report = new AllContactsWithPropertyReport();
		
		report.setTitle("Alle Kontakte mit der Eigenschaft");
		report.setCreated(new Date());
		CompositeParagraph header=new CompositeParagraph();	
		
		Vector<User> owneru=new Vector<User>();
		owneru.add(user);
		Vector<String> exactNameOfOwner=this.admin.getFullNamesOfUsers(owneru);
		for (String s: exactNameOfOwner){
			header.addSubParagraph(new SimpleParagraph("Angemeldeter Nutzer: " + s));
		}	
		
		header.addSubParagraph(new SimpleParagraph("Gesuchte Eigenschaft: " + property.getType()));
		report.setHeaderData(header);
		Vector <Property> prop=this.getAllPredefinedPropertiesOfReport();
		for (Property p : prop){
			if(p.getType().contains(property.getType())){
				Vector<Contact> allContacts=this.admin.getContactsOfUserWithDefaultProperty(user, property);
				if(allContacts.size()!=0){
					for (Contact c: allContacts) {
						report.addSubReport(this.generateAllContactInformationOfContactReport(c, user));
						Vector<Value> allValues=this.admin.getAllValuesOfContact(c);
						if (allValues.size()!=0){
						report.addSubReport(this.generateAllValuesOfContactReport(c, user));
						}
					}			
				}else{
					SimpleParagraph errornote=new SimpleParagraph("Es wurden leider keine Kontakte mit der angegebenen Eigenschaft gefunden");
					header.addSubParagraph(errornote);
					report.setHeaderData(header);
				}
			}
		}
		Vector<Contact> allContacts=this.admin.getContactsOfUserWithProperty(user, property);
		if(allContacts.size()!=0){
			for (Contact c: allContacts) {
				report.addSubReport(this.generateAllContactInformationOfContactReport(c, user));
				Vector<Value> allValues=this.admin.getAllValuesOfContact(c);
				if (allValues.size()!=0){
				report.addSubReport(this.generateAllValuesOfContactReport(c, user));
				}
			}			
		}else{
			SimpleParagraph errornote=new SimpleParagraph("Es wurden leider keine Kontakte mit der angegebenen Eigenschaft gefunden");
			header.addSubParagraph(errornote);
			report.setHeaderData(header);
		}
		return report;
	}
}

@Override
	public AllValuesOfContactReport generateAllValuesOfContactReport(Contact contact, User user)
			throws IllegalArgumentException {
		if(this.getEditorAdministration()==null) {
			return null;
		} 
		else {
			AllValuesOfContactReport report = new AllValuesOfContactReport();
			
			Row headline = new Row();
			
			headline.addColumn(new Column("Eigenschaft"));
			headline.addColumn(new Column("Ausprägung"));
						
			report.addRow(headline);
			
			Vector<Value> allValues=this.admin.getAllValuesOfContact(contact);

			if(allValues.size()!=0){
				for (Value val: allValues){
					if(val.getIsShared()==true || contact.getOwner() == user.getId()){
					Property prop=admin.getPropertyOfValue(val);
					Row valueRow=new Row();
						
					valueRow.addColumn(new Column(String.valueOf(prop.getType())));
					valueRow.addColumn(new Column(String.valueOf(val.getContent())));
					
					report.addRow(valueRow);
					}}
			}
			return report;
		}
		}

@Override
public AllContactInformationOfContactReport generateAllContactInformationOfContactReport(Contact contact, User user)
		throws IllegalArgumentException {
		if(this.getEditorAdministration()==null) {
			return null;
		} 
		else {
		AllContactInformationOfContactReport report = new AllContactInformationOfContactReport();
					
		Row headline = new Row();

		Column colSharingUser=new Column("Erhalten von");
		Column colParticipant=new Column("Teilhaber");

		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));
		headline.addColumn(new Column("Eigentümer"));
		headline.addColumn(colSharingUser);
		headline.addColumn(new Column("Erstellungsdatum"));
		headline.addColumn(new Column("Modifikationsdatum"));
		
		report.addRow(headline);
		
		User owner=new User();
		User sharedUser = new User();
		
		Row contactRow=new Row();
		
		owner=admin.getOwnerOfContact(contact);
		sharedUser = admin.getSourceToSharedContact(contact, user);
		
		contactRow.addColumn(new Column(String.valueOf(contact.getFirstname())));
		contactRow.addColumn(new Column(String.valueOf(contact.getLastname())));
		
		
		switch (contact.getSex()){
			case "f":
			contactRow.addColumn(new Column("weiblich"));
			break;
			case "m":
			contactRow.addColumn(new Column("männlich"));
			break;
			case "o":
			contactRow.addColumn(new Column("sonstige"));
			break;
		}
		Vector<User> owneru=new Vector<User>();
		owneru.add(owner);
		Vector<String> exactNameOfOwner=this.admin.getFullNamesOfUsers(owneru);
		for (String s: exactNameOfOwner){
			contactRow.addColumn(new Column(String.valueOf(s)));
		}
			
		if (sharedUser.getEmail() != user.getEmail()){	
			Vector<User> name=new Vector<User>();
			name.add(sharedUser);
			Vector<String> exactName=this.admin.getFullNamesOfUsers(name);
			for (String s: exactName){
				contactRow.addColumn(new Column(String.valueOf(s)));
			}
			}else{
				headline.removeColumn(colSharingUser);
			}
		
		contactRow.addColumn(new Column(String.valueOf(contact.getCreationDate())));
		contactRow.addColumn(new Column(String.valueOf(contact.getModificationDate())));
		if (user.getEmail().equals(owner.getEmail())){
			Vector<User> allParticipants=this.admin.getAllParticipantsOfContact(contact);
			if(allParticipants.size()!=0){
				Vector <String> allNamesOfParticipants=this.admin.getFullNamesOfUsers(allParticipants);
				headline.addColumn(colParticipant);
				StringBuffer result=new StringBuffer();
				for (String s: allNamesOfParticipants){
					result.append(s);
					result.append(", ");
				}
				result.deleteCharAt(result.length()-1);
				result.deleteCharAt(result.length()-1);
				contactRow.addColumn(new Column(String.valueOf(result)));
			}
		}		
		report.addRow(contactRow);		
		return report;

		}			
}

	/* (non-Javadoc)
	 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#getAllUsers()
	 */
	public Vector<User> getAllUsers(){
		return admin.getAllUsers();
	}
	
	public Vector<String> getAllUserSuggestions(User user){
		return admin.getAllUserSuggestions(user);
	}
	
	public Vector<User> getAllParticipants(Contact c){
		return this.admin.getAllParticipantsOfContact(c);
	}
	/* (non-Javadoc)
	 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#getAllPredefinedPropertiesOfReport()
	 */
	@Override
	public Vector<Property> getAllPredefinedPropertiesOfReport() throws IllegalArgumentException {
		
		return admin.getAllPredefinedPropertiesOf();
	}
}

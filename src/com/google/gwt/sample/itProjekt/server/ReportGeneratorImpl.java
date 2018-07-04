package com.google.gwt.sample.itProjekt.server;

import java.util.Date;
import java.util.Vector;
import com.google.gwt.sample.itProjekt.shared.EditorAdministration;
import com.google.gwt.sample.itProjekt.server.EditorAdministrationImpl;
import com.google.gwt.sample.itProjekt.shared.ReportGenerator;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
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
						
			Vector<Contact> allContacts=this.admin.getAllContactsOfActiveUser(user);
			if(allContacts.size()!=0){
				for (Contact c: allContacts) {
					report.addSubReport(this.generateAllValuesOfContactReport(c, user));
					}			
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
		
		report.setTitle("Alle mit dem Nutzer geteilten Kontakte");
		report.setCreated(new Date());
		
		CompositeParagraph header=new CompositeParagraph();
		
		header.addSubParagraph(new SimpleParagraph("Eingegebener Nutzer: " + receiver.getEmail()));
		
		report.setHeaderData(header);
		
		Row headline = new Row();

		headline.addColumn(new Column("Besitzer"));
		headline.addColumn(new Column("Erhalten von"));
		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));
		
		
		report.addRow(headline);
		
		User contactowner=new User();
		User sharedUser = new User();
		
		Vector<Contact> allContacts=this.admin.getAllSharedContactsOfUserWithOtherUser(owner, receiver.getEmail());
		
		if(allContacts.size()!=0){
			for (Contact c: allContacts) {
				
				contactowner=admin.getOwnerOfContact(c);
				sharedUser = admin.getSourceToSharedContact(c, owner);

				
				Vector<Value> allValues=this.admin.getAllValuesOfContact(c);
				Row contactRow=new Row();
				
				contactRow.addColumn(new Column(String.valueOf(contactowner.getEmail())));
				
				if (sharedUser.getEmail() != owner.getEmail()){	
					contactRow.addColumn(new Column(String.valueOf(sharedUser.getEmail())));
					}else{
						contactRow.addColumn(new Column(String.valueOf("")));
					}
				
				contactRow.addColumn(new Column(String.valueOf(c.getFirstname())));
				contactRow.addColumn(new Column(String.valueOf(c.getLastname())));
				
				
				switch (c.getSex()){
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
				
			
				report.addRow(contactRow);
				if(allValues.size() != 0){
					
					if(headline.getNumColumns() < 9){
					headline.addColumn(new Column("Eigenschaft"));
					headline.addColumn(new Column("Ausprägung"));
					
					}
					
				for (Value v: allValues){
					if(v.getIsShared()==true || c.getOwner() == owner.getId()){
					Property p=this.admin.getPropertyOfValue(v);
					Row valueRow=new Row();
					
					for(int i = 0; i < 5; i++){
						valueRow.addColumn(new Column(""));
					}
					
					valueRow.addColumn(new Column(String.valueOf(p.getType())));
					valueRow.addColumn(new Column(String.valueOf(v.getContent())));
					
					report.addRow(valueRow);
					}
				}}

				if(headline.getNumColumns() < 9){
					
					headline.addColumn(new Column("Erstellungsdatum"));
					headline.addColumn(new Column("Modifikationsdatum"));
					contactRow.addColumn(new Column(""));
					contactRow.addColumn(new Column(""));
				}
				
				contactRow.addColumn(new Column(String.valueOf(c.getCreationDate())));
				contactRow.addColumn(new Column(String.valueOf(c.getModificationDate())));
				
				
		}}
		else{
			report.removeRow(headline);
			SimpleParagraph errornote=new SimpleParagraph("Es wurden leider keine Kontakte mit der eingegebenen Ausprägung gefunden");
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
		
		header.addSubParagraph(new SimpleParagraph("Nutzer: " + user.getEmail()));
		header.addSubParagraph(new SimpleParagraph("Gesuchte Ausprägung: " + value.getContent()));
		
		report.setHeaderData(header);
		
		Row headline = new Row();

		headline.addColumn(new Column("Besitzer"));
		headline.addColumn(new Column("Erhalten von"));
		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));

		
		report.addRow(headline);
		
		User owner=new User();
		User sharedUser = new User();

		Vector<Contact> allContacts=this.admin.getAllContactsOfUserWithValue(user, value);
		
		if(allContacts.size()!=0){
			for (Contact c: allContacts) {
			Vector<Value> allValues=this.admin.getAllValuesOfContact(c);
			Row contactRow=new Row();
			
			owner=admin.getOwnerOfContact(c);
			sharedUser = admin.getSourceToSharedContact(c, user);
			
			contactRow.addColumn(new Column(String.valueOf(owner.getEmail())));
			
			if (sharedUser.getEmail() != user.getEmail()){	
				contactRow.addColumn(new Column(String.valueOf(sharedUser.getEmail())));
				}else{
					contactRow.addColumn(new Column(String.valueOf("")));
				}
			
			contactRow.addColumn(new Column(String.valueOf(c.getFirstname())));
			contactRow.addColumn(new Column(String.valueOf(c.getLastname())));
			
			
			switch (c.getSex()){
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
			
			
			report.addRow(contactRow);
			
			if(allValues.size() != 0){
				
				if(headline.getNumColumns() < 9){
				headline.addColumn(new Column("Eigenschaft"));
				headline.addColumn(new Column("Ausprägung"));
				
				}
				
			for (Value val: allValues){
				if(val.getIsShared()==true || c.getOwner() == user.getId()){
				Property p=this.admin.getPropertyOfValue(val);
				Row valueRow=new Row();
				
				for(int i = 0; i < 5; i++){
					valueRow.addColumn(new Column(""));
				}
				
				valueRow.addColumn(new Column(String.valueOf(p.getType())));
				valueRow.addColumn(new Column(String.valueOf(val.getContent())));
				
				report.addRow(valueRow);
				}
			}
			}

			if(headline.getNumColumns() < 9){
				
				headline.addColumn(new Column("Erstellungsdatum"));
				headline.addColumn(new Column("Modifikationsdatum"));
				contactRow.addColumn(new Column(""));
				contactRow.addColumn(new Column(""));
			
			}

			contactRow.addColumn(new Column(String.valueOf(c.getCreationDate())));
			contactRow.addColumn(new Column(String.valueOf(c.getModificationDate())));
			
			
			}
			
		}else{
			report.removeRow(headline);
			SimpleParagraph errornote=new SimpleParagraph("Es wurden leider keine Kontakte mit der eingegebenen Ausprägung gefunden");
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
		header.addSubParagraph(new SimpleParagraph("Nutzer: " + user.getEmail()));
		header.addSubParagraph(new SimpleParagraph("Gesuchte Eigenschaft: " + property.getType()));
		report.setHeaderData(header);
		
		Row headline = new Row();

		headline.addColumn(new Column("Besitzer"));
		headline.addColumn(new Column("Erhalten von"));
		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));
				
		report.addRow(headline);

		User owner=new User();
		User sharedUser = new User();
		
		Vector<Contact> allContacts=this.admin.getContactsOfUserWithProperty(user, property);
		
		if(allContacts.size()>0){
		for (Contact c: allContacts) {

			

			owner= admin.getOwnerOfContact(c);

			

			sharedUser = admin.getSourceToSharedContact(c, user);

			Vector<Value> allValues=this.admin.getAllValuesOfContact(c);
			
			
			Row contactRow=new Row();
			
			contactRow.addColumn(new Column(String.valueOf(owner.getEmail())));

			if (sharedUser.getEmail() != user.getEmail()){	
				contactRow.addColumn(new Column(String.valueOf(sharedUser.getEmail())));
				}else{
					contactRow.addColumn(new Column(String.valueOf("")));
				}
			
			contactRow.addColumn(new Column(String.valueOf(c.getFirstname())));
			contactRow.addColumn(new Column(String.valueOf(c.getLastname())));
			
			switch (c.getSex()){
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
			
			
			report.addRow(contactRow);
			
			if(allValues.size() != 0){

				if(headline.getNumColumns() < 9){
				headline.addColumn(new Column("Eigenschaft"));
				headline.addColumn(new Column("Ausprägung"));
				
				}
				
			for (Value val: allValues){
				if(val.getIsShared()==true || c.getOwner() == user.getId()){
				Property prop=this.admin.getPropertyOfValue(val);
				Row valueRow=new Row();
				
				for(int i = 0; i < 5; i++){
					valueRow.addColumn(new Column(""));
				}
				
				valueRow.addColumn(new Column(String.valueOf(prop.getType())));
				valueRow.addColumn(new Column(String.valueOf(val.getContent())));
				
				report.addRow(valueRow);
				}}
			}
			
			if(headline.getNumColumns() < 9){
				
				headline.addColumn(new Column("Erstellungsdatum"));
				headline.addColumn(new Column("Modifikationsdatum"));
				contactRow.addColumn(new Column(""));
				contactRow.addColumn(new Column(""));
			
			}

				contactRow.addColumn(new Column(String.valueOf(c.getCreationDate())));
				contactRow.addColumn(new Column(String.valueOf(c.getModificationDate())));
				
		}
		}else{
			
			report.removeRow(headline);
			SimpleParagraph errornote=new SimpleParagraph("Es wurden leider keine Kontakte mit der eingegebenen Eigenschaft gefunden");
			header.addSubParagraph(errornote);	
			report.setHeaderData(header);
		}
		return report;
	}
}

@Override
	public AllValuesOfContactReport generateAllValuesOfContactReport(Contact contact, User user)
			throws IllegalArgumentException {
			System.out.println("in der GenerateAllValuesOfContact");
		if(this.getEditorAdministration()==null) {
			return null;
		} 
		else {
			System.out.println("geht in die Else schleife");
			AllValuesOfContactReport report = new AllValuesOfContactReport();
			
			report.setTitle("Alle Eigenschaften des Kontaktes");
			CompositeParagraph header=new CompositeParagraph();
			SimpleParagraph sp = new SimpleParagraph("Kontakt: " + contact.getFirstname()+ contact.getLastname());
			
			header.addSubParagraph(sp);
	
			report.setHeaderData(header);
			
			Row headline = new Row();
			
			headline.addColumn(new Column("Besitzer"));
			headline.addColumn(new Column("Erhalten von"));
			headline.addColumn(new Column("Vorname"));
			headline.addColumn(new Column("Nachname"));
			headline.addColumn(new Column("Geschlecht"));
					
			report.addRow(headline);
			
			User owner=admin.getOwnerOfContact(contact);
			User sharedUser = admin.getSourceToSharedContact(contact, user);

			Vector<Permission> allPermissions = admin.getAllPermissions();
			
			Row contactRow=new Row();
			
			
			contactRow.addColumn(new Column(String.valueOf(owner.getEmail())));
			
			if (sharedUser.getEmail() != user.getEmail()){	
			contactRow.addColumn(new Column(String.valueOf(sharedUser.getEmail())));
			}else{
				contactRow.addColumn(new Column(String.valueOf("")));
			}
			System.out.println("contact get firstname vorher");
			contactRow.addColumn(new Column(String.valueOf(contact.getFirstname())));
			contactRow.addColumn(new Column(String.valueOf(contact.getLastname())));
			
			switch(contact.getSex()){
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
			
			
			report.addRow(contactRow);
			System.out.println("contact get firstname vorher");

			if(allPermissions.size()> 0){
				User u = new User();
				headline.addColumn(new Column("Teilhaber"));
				for (Permission p: allPermissions){
					
					if(p.getShareableObjectID() == contact.getId() && contact.getOwner() == user.getId()){
						
					for(int i = 0; i < 1; i++){
						contactRow.addColumn(new Column(""));
					}
					
					u = admin.getUserByID(p.getParticipantID());
				}
					contactRow.addColumn(new Column(String.valueOf(u.getEmail())));
			}}
					
			if(headline.getNumColumns() < 10){
				
				headline.addColumn(new Column("Erstellungsdatum"));
				headline.addColumn(new Column("Modifikationsdatum"));
			
			}
					
					
			contactRow.addColumn(new Column(String.valueOf(contact.getCreationDate())));
			contactRow.addColumn(new Column(String.valueOf(contact.getModificationDate())));
		
	
			report.addRow(headline);
		
			Vector<Value> allValues=this.admin.getAllValuesOfContact(contact);
			System.out.println("Der Vektor enthält:" +allValues.size());

			if(allValues.size()!=0){
				if(headline.getNumColumns() < 9){
					headline.addColumn(new Column("Eigenschaft"));
					headline.addColumn(new Column("Ausprägung"));
					
					}
				for (Value val: allValues){
					if(val.getIsShared()==true || contact.getOwner() == user.getId()){
					Property prop=admin.getPropertyOfValue(val);
					Row valueRow=new Row();
					
					for(int i = 0; i < 5; i++){
						valueRow.addColumn(new Column(""));
					}
					
					valueRow.addColumn(new Column(String.valueOf(prop.getType())));
					valueRow.addColumn(new Column(String.valueOf(val.getContent())));
					
					report.addRow(valueRow);
					}}
			}
			return report;
		}
		}

//@Override
//public AllValuesOfContactReport generateAllValuesOfContactReport(Contact contact, User user)
//		throws IllegalArgumentException {
//	if(this.getEditorAdministration()==null) {
//		return null;
//	} 
//	else {
//		AllValuesOfContactReport report = new AllValuesOfContactReport();
//		
//		report.setTitle("Alle Eigenschaften des Kontaktes");
//		CompositeParagraph header=new CompositeParagraph();
//		SimpleParagraph sp = new SimpleParagraph("Kontakt: " + contact.getFirstname()+ contact.getLastname());
//		
//		header.addSubParagraph(sp);
//
//		report.setHeaderData(header);
//		
//		Row headline = new Row();
//
//		headline.addColumn(new Column("Eigenschaft"));
//		headline.addColumn(new Column("Ausprägung"));
//
//		report.addRow(headline);
//	
//		Vector<Value> allValues=this.admin.getAllValuesOfContact(contact);
//		
//		if(allValues.size()!=0){
//			for(Value value :allValues){
//				if(value.getIsShared()==true || contact.getOwner() == user.getId()){
//					Property p=this.admin.getPropertyOfValue(value);
//					Row valueRow=new Row();
//					
//					valueRow.addColumn(new Column(String.valueOf(p.getType())));
//					valueRow.addColumn(new Column(String.valueOf(value.getContent())));
//					
//					report.addRow(valueRow);
//					}
//			}
//		}
//		return report;
//	}
//}
	/* (non-Javadoc)
	 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#getAllUsers()
	 */
	public Vector<User> getAllUsers(){
		return admin.getAllUsers();
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#getAllPredefinedPropertiesOfReport()
	 */
	@Override
	public Vector<Property> getAllPredefinedPropertiesOfReport() throws IllegalArgumentException {
		
		return admin.getAllPredefinedPropertiesOf();
	}


}
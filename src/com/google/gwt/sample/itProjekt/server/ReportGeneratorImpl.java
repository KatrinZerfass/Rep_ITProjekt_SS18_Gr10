package com.google.gwt.sample.itProjekt.server;

import java.util.Date;
import java.util.Vector;
import com.google.gwt.sample.itProjekt.shared.EditorAdministration;
import com.google.gwt.sample.itProjekt.server.EditorAdministrationImpl;
import com.google.gwt.sample.itProjekt.shared.ReportGenerator;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithPropertyReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.Column;
import com.google.gwt.sample.itProjekt.shared.report.CompositeParagraph;
import com.google.gwt.sample.itProjekt.shared.report.Row;
import com.google.gwt.sample.itProjekt.shared.report.SimpleParagraph;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


// 
/**
 * Die ReportGeneratorImpl Klasse.
 */
@SuppressWarnings("serial")
public class ReportGeneratorImpl extends RemoteServiceServlet implements ReportGenerator{
	
	/** Die Instanz der Klasse der . */
	private EditorAdministration admin = null;
	
	/**
	 * Instantiates a new report generator impl.
	 *
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public ReportGeneratorImpl () throws IllegalArgumentException {
	}

	
/**
 * @see javax.servlet.GenericServlet#init()
 */
@Override
	public void init() throws IllegalArgumentException{
		EditorAdministrationImpl a = new EditorAdministrationImpl();
		a.init();
		this.admin=a;
	}

	/**
	 * Gets the editor administration.
	 *
	 * @return the editor administration
	 */
	protected EditorAdministration getEditorAdministration() {
		return this.admin;
	}	
	
	
	/**
	 * Identifizierung des angemeldeten Users. 
	 */
	
	public User getUserInformation (String email) throws IllegalArgumentException{
		return this.admin.getUserInformation(email);
	}
	

/* (non-Javadoc)
 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#generateAllContactsOfUserReport(com.google.gwt.sample.itProjekt.shared.bo.User)
 */
@Override	
	public AllContactsOfUserReport generateAllContactsOfUserReport(User u) {
		if(this.getEditorAdministration()==null) {
			return null;
		} 
		else {
			AllContactsOfUserReport report = new AllContactsOfUserReport();
			
			report.setTitle("Alle Kontakte des Nutzers");
			report.setCreated(new Date());
			CompositeParagraph header=new CompositeParagraph();
			SimpleParagraph sp = new SimpleParagraph(u.getEmail());
			header.addSubParagraph(sp);
			
			report.setHeaderData(header);

			Row headline = new Row();
			Row propertyheadline=new Row();
			propertyheadline.addColumn(new Column(""));
			propertyheadline.addColumn(new Column("Eigenschaft"));
			propertyheadline.addColumn(new Column("Ausprägung"));
			
			headline.addColumn(new Column ("Kontakt-ID"));
			headline.addColumn(new Column("Vorname"));
			headline.addColumn(new Column("Nachname"));
			headline.addColumn(new Column("Geschlecht"));
			
			report.addRow(headline);

			Vector<Contact> allContacts=this.admin.getAllOwnedContactsOf(u.getEmail());
			allContacts.addAll(this.admin.getAllSharedContactsWith(u.getEmail()));
			
	
			for (Contact c: allContacts) {
				Vector<Value> allValues=this.admin.getAllValuesOf(c);
				System.out.println(allValues.toString());
				Row contactRow=new Row();
				contactRow.addColumn(new Column(String.valueOf(c.getId())));
				contactRow.addColumn(new Column(String.valueOf(c.getFirstname())));
				contactRow.addColumn(new Column(String.valueOf(c.getLastname())));
				switch(c.getSex()){
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
				if(allValues.size()!= 0){
					report.addRow(propertyheadline);
					for (Value v: allValues){
						Property p=this.admin.getPropertyOfValue(v);
						Row valueRow=new Row();
						valueRow.addColumn(new Column(""));
						valueRow.addColumn(new Column(p.getType()));
						valueRow.addColumn(new Column(v.getContent()));
						report.addRow(valueRow);
					}
				}
				
			}
			return report;
		}
	}
	
/* (non-Javadoc)
 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#generateAllSharedContactsOfUserReport(com.google.gwt.sample.itProjekt.shared.bo.User)
 */
@Override	
	public AllSharedContactsOfUserReport generateAllSharedContactsOfUserReport(User u) {
	if(this.getEditorAdministration()==null) {
		return null;
	} 
	else {
		AllSharedContactsOfUserReport report = new AllSharedContactsOfUserReport();
		
		report.setTitle("Alle mit dem Nutzer geteilten Kontakte");
		report.setCreated(new Date());
		
		CompositeParagraph header=new CompositeParagraph();
		
		header.addSubParagraph(new SimpleParagraph("Nutzer: " + u.getEmail()));
		header.addSubParagraph(new SimpleParagraph("Nutzer-ID: " + u.getId()));
		
		report.setHeaderData(header);
		
		Row headline = new Row();
		Row propertyheadline=new Row();

		headline.addColumn(new Column ("Kontakt-ID"));
		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));

		propertyheadline.addColumn(new Column(""));
		propertyheadline.addColumn(new Column("Eigenschaft"));
		propertyheadline.addColumn(new Column("Ausprägung"));
		
		report.addRow(headline);

		Vector<Contact> allContacts=this.admin.getAllSharedContactsWith(u.getEmail());
		
		for (Contact c: allContacts) {
			Vector<Value> allValues=this.admin.getAllValuesOf(c);
			Row contactRow=new Row();
			contactRow.addColumn(new Column(String.valueOf(c.getId())));
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
			report.addRow(propertyheadline);
			for (Value v: allValues){
				Property p=this.admin.getPropertyOfValue(v);
				Row valueRow=new Row();
				valueRow.addColumn(new Column(""));
				valueRow.addColumn(new Column(p.getType()));
				valueRow.addColumn(new Column(v.getContent()));
				report.addRow(valueRow);
				}
			}
		}
		return report;
	}
}

/* (non-Javadoc)
 * @see com.google.gwt.sample.itProjekt.shared.ReportGenerator#generateAllContactsWithValueReport(com.google.gwt.sample.itProjekt.shared.bo.Value)
 */
@Override	
	public AllContactsWithValueReport generateAllContactsWithValueReport(User u, Value v) {
	if(this.getEditorAdministration()==null) {
		return null;
	} 
	else {
		AllContactsWithValueReport report = new AllContactsWithValueReport();
		
		report.setTitle("Alle Kontakte mit der Ausprägung");
		report.setCreated(new Date());
			
		report.setHeaderData(new SimpleParagraph("Gesuchte Ausprägung: " + v.getContent()));
		
		Row headline = new Row();
		Row propertyheadline=new Row();

		headline.addColumn(new Column ("Kontakt-ID"));
		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));
		
		propertyheadline.addColumn(new Column(""));
		propertyheadline.addColumn(new Column("Eigenschaft"));
		propertyheadline.addColumn(new Column("Ausprägung"));
		
		report.addRow(headline);

		Vector<Contact> allContacts=this.admin.getAllContactsOfUserWithValue(u, v);
		
		for (Contact c: allContacts) {
			Vector<Value> allValues=this.admin.getAllValuesOf(c);
			Row contactRow=new Row();
			contactRow.addColumn(new Column(String.valueOf(c.getId())));
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
			report.addRow(propertyheadline);
			for (Value val: allValues){
				Property p=this.admin.getPropertyOfValue(val);
				Row valueRow=new Row();
				valueRow.addColumn(new Column(""));
				valueRow.addColumn(new Column(p.getType()));
				valueRow.addColumn(new Column(val.getContent()));
				report.addRow(valueRow);
				}
			}
		}
		return report;
	}
}

@Override
	public AllContactsWithPropertyReport generateAllContactsWithPropertyReport(User user, Property property){
	if(this.getEditorAdministration()==null) {
		return null;
	} 
	else {
		AllContactsWithPropertyReport report = new AllContactsWithPropertyReport();
		
		report.setTitle("Alle Kontakte mit der Eigenschaft");
		report.setCreated(new Date());
			
		report.setHeaderData(new SimpleParagraph("Gesuchte Eigenschaft: " + property.getType()));
		
		Row headline = new Row();
		Row propertyheadline=new Row();

		headline.addColumn(new Column ("Kontakt-ID"));
		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));
		propertyheadline.addColumn(new Column(""));
		propertyheadline.addColumn(new Column("Eigenschaft"));
		propertyheadline.addColumn(new Column("Ausprägung"));
		
		report.addRow(headline);

		Vector<Contact> allContacts=this.admin.getContactsByOfUserWithProperty(user, property);
		
		for (Contact c: allContacts) {
			Vector<Value> allValues=this.admin.getAllValuesOf(c);
			Row contactRow=new Row();
			contactRow.addColumn(new Column(String.valueOf(c.getId())));
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
			report.addRow(propertyheadline);
			for (Value val: allValues){
				Property prop=this.admin.getPropertyOfValue(val);
				Row valueRow=new Row();
				valueRow.addColumn(new Column(""));
				valueRow.addColumn(new Column(prop.getType()));
				valueRow.addColumn(new Column(val.getContent()));
				report.addRow(valueRow);
				}
			}
		}
		return report;
}
}
}
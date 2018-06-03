package com.google.gwt.sample.itProjekt.server;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.EditorAdministration;
import com.google.gwt.sample.itProjekt.shared.ReportGenerator;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.Column;
import com.google.gwt.sample.itProjekt.shared.report.CompositeParagraph;
import com.google.gwt.sample.itProjekt.shared.report.Row;
import com.google.gwt.sample.itProjekt.shared.report.SimpleParagraph;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


@SuppressWarnings("serial")
public class ReportGeneratorImpl extends RemoteServiceServlet implements ReportGenerator{
	
	private EditorAdministration admin = null;
	
	public ReportGeneratorImpl () throws IllegalArgumentException {
		
	}
@Override
	public void init() throws IllegalArgumentException{
		EditorAdministrationImpl a =new EditorAdministrationImpl();
		a.init();
		this.admin=a;
	}

	protected EditorAdministration getEditorAdministration() {
		return this.admin;
	}

/*@Override
	public AllContactsReport generateAllContactsReport() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		AllContactsReport report=new AllContactsReport();
		
		report.setTitle("Alle vorhandenen Kontakte");
	    report.setCreated(new Date());
	}*/

@Override	
	public AllContactsOfUserReport generateAllContactsOfUserReport(User u) throws IllegalArgumentException {
		if(this.getEditorAdministration()==null) {
			return null;
		} 
		else {
			AllContactsOfUserReport report = new AllContactsOfUserReport();
			
			report.setTitle("Alle Kontakte des Nutzers");
			report.setCreated(new Date());
			
			CompositeParagraph header=new CompositeParagraph();
			
			header.addSubParagraph(new SimpleParagraph("Nutzer: " + u.getFirstname() + " " + u.getLastname()));
			header.addSubParagraph(new SimpleParagraph("Nutzer-ID: " + u.getId()));
			
			report.setHeaderData(header);
			
			Row headline = new Row();
			
			headline.addColumn(new Column ("Kontakt-ID"));
			headline.addColumn(new Column("Vorname"));
			headline.addColumn(new Column("Nachname"));
			headline.addColumn(new Column("Geschlecht"));
			
			report.addRow(headline);

			Vector<Contact> allContacts=this.admin.getAllOwnedContactsOf(u);
			allContacts.addAll(this.admin.getAllSharedContactsWith(u));
			
			for (Contact c: allContacts) {
				Row contactRow=new Row();
				contactRow.addColumn(new Column(String.valueOf(c.getId())));
				contactRow.addColumn(new Column(String.valueOf(c.getFirstname())));
				contactRow.addColumn(new Column(String.valueOf(c.getLastname())));
				contactRow.addColumn(new Column(String.valueOf(c.getSex())));
				report.addRow(contactRow);
			}
			return report;
		}
	}
	
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
		
		header.addSubParagraph(new SimpleParagraph("Nutzer: " + u.getFirstname() + " " + u.getLastname()));
		header.addSubParagraph(new SimpleParagraph("Nutzer-ID: " + u.getId()));
		
		report.setHeaderData(header);
		
		Row headline = new Row();
		
		headline.addColumn(new Column ("Kontakt-ID"));
		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));
		
		report.addRow(headline);

		Vector<Contact> allContacts=this.admin.getAllSharedContactsWith(u);
		
		for (Contact c: allContacts) {
			Row contactRow=new Row();
			contactRow.addColumn(new Column(String.valueOf(c.getId())));
			contactRow.addColumn(new Column(String.valueOf(c.getFirstname())));
			contactRow.addColumn(new Column(String.valueOf(c.getLastname())));
			contactRow.addColumn(new Column(String.valueOf(c.getSex())));
			report.addRow(contactRow);
		}
		return report;
	}
}

@Override	
	public AllContactsWithValueReport generateAllContactsWithValueReport(Value v) {
	if(this.getEditorAdministration()==null) {
		return null;
	} 
	else {
		AllContactsWithValueReport report = new AllContactsWithValueReport();
		
		report.setTitle("Alle Kontakte mit der Ausprägung");
		report.setCreated(new Date());
			
		report.setHeaderData(new SimpleParagraph("Gesuchte Ausprägung: " + v.getContent()));
		
		Row headline = new Row();
		
		headline.addColumn(new Column ("Kontakt-ID"));
		headline.addColumn(new Column("Vorname"));
		headline.addColumn(new Column("Nachname"));
		headline.addColumn(new Column("Geschlecht"));
		
		report.addRow(headline);

		Vector<Contact> allContacts=this.admin.getAllContactsWith(v);
		
		for (Contact c: allContacts) {
			Row contactRow=new Row();
			contactRow.addColumn(new Column(String.valueOf(c.getId())));
			contactRow.addColumn(new Column(String.valueOf(c.getFirstname())));
			contactRow.addColumn(new Column(String.valueOf(c.getLastname())));
			contactRow.addColumn(new Column(String.valueOf(c.getSex())));
			report.addRow(contactRow);
		}
		return report;
	}
}
}
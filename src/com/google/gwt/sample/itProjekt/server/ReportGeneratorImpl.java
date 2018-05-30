package com.google.gwt.sample.itProjekt.server;

import java.util.Date;
import java.util.Vector;

import com.google.gwt.sample.itProjekt.server.db.ContactListMapper;
import com.google.gwt.sample.itProjekt.server.db.ContactMapper;
import com.google.gwt.sample.itProjekt.server.db.PropertyMapper;
import com.google.gwt.sample.itProjekt.server.db.UserMapper;
import com.google.gwt.sample.itProjekt.server.db.ValueMapper;
import com.google.gwt.sample.itProjekt.shared.EditorAdministration;
import com.google.gwt.sample.itProjekt.shared.ReportGenerator;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.CompositeParagraph;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.thies.bankProjekt.shared.BankAdministration;


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

@Override
	public AllContactsReport generateAllContactsReport() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		AllContactsReport report=new AllContactsReport();
		
		report.setTitle("Alle vorhandenen Kontakte");
	    report.setCreated(new Date());

	    CompositeParagraph header = new CompositeParagraph();
	    header.
	    
	    
	    
	}
@Override	
	public AllContactsOfUserReport generateAllContactsOfUserReport(User u) {
		AllContactsOfUserReport result=new AllContactsOfUserReport();
		
	}
	
@Override	
	public AllSharedContactsOfUserReport generateAllSharedContactsOfUserReport(User u) {
		
	}

@Override	
	public AllContactsWithValueReport generateAllContactsWithValueReport(Value v) {
		
	}
}

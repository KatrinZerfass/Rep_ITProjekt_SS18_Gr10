package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.server.db.ContactListMapper;
import com.google.gwt.sample.itProjekt.server.db.ContactMapper;
import com.google.gwt.sample.itProjekt.server.db.PropertyMapper;
import com.google.gwt.sample.itProjekt.server.db.UserMapper;
import com.google.gwt.sample.itProjekt.server.db.ValueMapper;

import com.google.gwt.sample.itProjekt.shared.report.AllContactsReport;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReportGenerator extends RemoteServiceServlet{
	
	private UserMapper uMapper;
	private ContactMapper cMapper;
	private PropertyMapper pMapper;
	private ValueMapper vMapper;
	
	public void init() throws IllegalArgumentException{
		this.uMapper = UserMapper.userMapper();
		this.cMapper = ContactMapper.contactMapper();
		this.pMapper = PropertyMapper.propertyMapper();
		this.vMapper = ValueMapper.valueMapper();
		
	}
	
	public AllContactsReport showAllContacts() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}
	
	public AllContactsOfUserReport showAllContactsOf(User u) {
		AllContactsOfUserReport result=new AllContactsOfUserReport();
	}
	
	public AllSharedContactsOfUserReport showAllSharedContactsWith(User u) {
		
	}
	
	public AllContactsWithValueReport showAllshowAllContactsWith(Value v) {
		
	}

}

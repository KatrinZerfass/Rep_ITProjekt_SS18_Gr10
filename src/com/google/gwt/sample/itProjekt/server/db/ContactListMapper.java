package com.google.gwt.sample.itProjekt.server.db;

public class ContactListMapper {
	
private static ContactListMapper contactlistmapper = null;
	
	public static ContactListMapper contactListMapper() {
		if (contactlistmapper == null){
			contactlistmapper = new ContactListMapper();
		}
		return contactlistmapper;
		}
	
	
	
	
	
	
	
	

}

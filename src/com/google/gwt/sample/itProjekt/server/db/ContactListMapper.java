package com.google.gwt.sample.itProjekt.server.db;

public class ContactlistMapper {
	
private static ContactlistMapper contactlistmapper = null;
	
	protected ContactlistMapper contactlistMapper() {
		if (contactlistmapper == null){
			contactlistmapper = new ContactlistMapper();
		}
		return contactlistmapper;
		}
	
	
	
	
	
	
	
	

}

package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;

public class ContactListCell extends AbstractCell<ContactList> {

	
	@Override
	public void render(Context context, ContactList value, SafeHtmlBuilder sb) {
		if(value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div>");
	    sb.appendEscaped(value.getName());
	    sb.appendHtmlConstant("<div>");
		
	}

}

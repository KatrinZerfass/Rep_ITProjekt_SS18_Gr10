package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;

public class ContactCell extends AbstractCell<Contact> {

	@Override
	public void render(Context context, Contact value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value.getFirstname());
		sb.appendHtmlConstant(" ");
		sb.appendEscaped(value.getLastname());
		sb.appendHtmlConstant("</div>");
		
	}

}

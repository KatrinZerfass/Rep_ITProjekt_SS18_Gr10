package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;


/**
 * Die Klasse ContactCell erstellt Cells, mit denen Kontakte im CellBrowser angezeigt werden. 
 * Es wird immer der Vor- und der Nachname eines Kontakts angezeigt. 
 */
public class ContactCell extends AbstractCell<Contact> {

	/* (non-Javadoc)
	 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
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

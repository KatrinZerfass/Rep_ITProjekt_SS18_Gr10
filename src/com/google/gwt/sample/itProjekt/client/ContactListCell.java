package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.user.client.Window;


/**
 * Die Klasse ContactListCell erstellt Cells, mit denen Kontaktlisten im CellBrowser angezeigt werden.
 * Eine Kontaktliste wird immer mit ihrem Namen angezeigt. 
 */
public class ContactListCell extends AbstractCell<ContactList> {

	
	@Override
	public void render(Context context, ContactList value, SafeHtmlBuilder sb) {
		if(value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div id =\"contactListCell\">");
		if(value.getOwner() == ClientsideSettings.getUser().getId()) {
			sb.appendHtmlConstant("<img src=\"owner_symbol.png\"> id= \"itemSymbol\"");	
		}else {
			sb.appendHtmlConstant("<img src=\"shared_symbol.png\"> id= \"itemSymbol\"");
		}
		Window.alert("symbol-icon von Kontaktliste wurde gesetzt");
		sb.appendEscaped(" ");
	    sb.appendEscaped(value.getName());
	    sb.appendHtmlConstant("</div>");
		
	}

}

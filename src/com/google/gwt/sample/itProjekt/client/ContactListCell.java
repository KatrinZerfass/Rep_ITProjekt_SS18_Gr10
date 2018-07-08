package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.user.client.Window;


/**
 * Die Klasse ContactListCell erstellt Cells, mit denen Kontaktlisten im CellBrowser angezeigt werden.
 * Eine Kontaktliste wird immer mit einem Icon und dahinter ihrem Namen angezeigt. 
 * 
 * @author KatrinZerfass
 */
public class ContactListCell extends AbstractCell<ContactList> {

	
	@Override
	public void render(Context context, ContactList value, SafeHtmlBuilder sb) {
		if(value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<div id =\"contactListCell\">");
		if(value.getOwner() == ClientsideSettings.getUser().getId() ||
				value.getName() == "Meine Kontakte" ||
				value.getName() == "Suchergebnis im Namen" ||
				value.getName() == "Suchergebnis in den Eigenschaften"){
			//der Nutzer ist Eigentümer der Kontaktliste bzw. es handelt sich um die virtuellen Listen für Suchergebnisse
			sb.appendHtmlConstant("<img src=\"owner_symbol.png\" id= \"itemSymbol\">");	
		}else {
			//der Nutzer ist Teilhaber an der Kontaktliste
			sb.appendHtmlConstant("<img src=\"shared_symbol.png\" id= \"itemSymbol\">");
		}
		sb.appendEscaped(" ");
	    sb.appendEscaped(value.getName());
	    sb.appendHtmlConstant("</div>");
		
	}

}

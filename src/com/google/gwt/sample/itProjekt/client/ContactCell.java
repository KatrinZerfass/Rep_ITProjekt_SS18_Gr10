package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.user.client.Window;


/**
 * Die Klasse ContactCell erstellt Cells, mit denen Kontakte im CellBrowser angezeigt werden. 
 * Es wird immer ein Icon und dahinter der Vor- und der Nachname eines Kontakts angezeigt. 
 * 
 * @author KatrinZerfass
 */
public class ContactCell extends AbstractCell<Contact> {

	public void render(Context context, Contact value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		
		
		sb.appendHtmlConstant("<div id =\"contactCell\">");
		
		if(ClientsideFunctions.isOwner(value, ClientsideSettings.getUser())) {
			
			if(value.getIsUser()) {
				//wenn der Kontakt derjenige ist, welcher den Nutzer selbst verkörpert
				sb.appendHtmlConstant("<img src=\"userContact_Symbol.png\" id= \"itemSymbol\">");	
				
			}
			else{
				//wenn der Nutzer Eigentümer dieses Kontakt ist
				sb.appendHtmlConstant("<img src=\"owner_symbol.png\" id= \"itemSymbol\">");	
			}
		}else {
			//wenn der Nutzer Teilhaber am Kontkat ist
			sb.appendHtmlConstant("<img src=\"shared_symbol.png\" id= \"itemSymbol\">");
		}
		sb.appendEscaped(" ");
		sb.appendEscaped(value.getFirstname());
		sb.appendHtmlConstant(" ");
		sb.appendEscaped(value.getLastname());
		sb.appendHtmlConstant("</div>");
		
	}

}

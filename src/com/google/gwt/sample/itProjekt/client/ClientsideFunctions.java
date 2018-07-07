package com.google.gwt.sample.itProjekt.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.sample.itProjekt.client.ContactForm.ValueTextBox;

/**
 * Ergänzt die Klasse {@link ClientsideSettingsie} um weitere Funktionen und Dienste,
 * die für alle Client-seitigen Klassen relevant sind.
 * 
 * @author JanNoller
 */
public abstract class ClientsideFunctions {
	
	/** Objekt zur Kommunikation mit dem Server-seitgen 
	 * Dienst namens editorAdministration*/
	private static EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	
	/** Client-seitiges Nutzer-Object, repräsentiert den angemeldeten Nutzer. */
	private static User user = ClientsideSettings.getUser();
	
	/**
	 * Überprüft den Inhalt der Klasse {@link ValueTextBox} auf Korrektheit mit Hilfe von regulären Ausdrücken.
	 *
	 * Reguläre Ausdruck für Email-Adressen entstammt der Seite:
	 * <a href="http://emailregex.com/">http://emailregex.com/</a><br>
	 * 
	 * Regulärer Ausdruck für Websites entstammt der Seite:
	 * <a href="https://stackoverflow.com/questions/2490310/regular-expression-for-checking-website-url">https://stackoverflow.com/</a>
	 *
	 * @param vtb zu überprüfende ValueTextBox
	 * @return true wenn Inhalt in Ordnung
	 */
	public static boolean checkValue (ValueTextBox vtb) {
		
		/**
		 * Holt den zu überprüfenden Text aus der ValueTextBox und normalisiert diesen.
		 * (nur Kleinbuchstaben, keine Leerzeichen)
		 */
		String identifier = vtb.getIdentifier();
		String text = vtb.getText().toLowerCase().trim();
		
		/**
		 * Schützt Datenbank vor SQL-Injections
		 */
		if(text.contains("dropdatabase") || text.contains("drop database")) {
			ClientsideFunctions.popUpBox no = new ClientsideFunctions.popUpBox("For droping the database please find secret drop database pixel...", new CloseButton());
			no.getCloseButton().addCloseDBClickHandler(no);
			return false;
		}
		
		/**
		 * Schützt Datenbank vor SQL-Injections
		 */
		if(text.contains("'") || text.contains("\"") || text.contains(";")) {
			ClientsideFunctions.popUpBox signs = new ClientsideFunctions.popUpBox("Die Zeichen: ' \" und ; können nicht verwendet werden.", new CloseButton());
			signs.getCloseButton().addCloseDBClickHandler(signs);
			return false;
		}
		
		/**
		 * Zeichenfolgen werden hier mit Hilfe von regulären Ausdrücken überprüft
		 */
		switch(identifier) {
			case "Name":
				if (!text.matches("\\d+")) {
					if(!text.isEmpty()) {
						return true;
					}else {
						final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige Zeichen im Namen!", new ClientsideFunctions.OkButton());
						failed.getOkButton().addCloseDBClickHandler(failed);
						return false;
					}
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige Zeichen im Namen!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
			case "Straße":
				if (!text.matches("\\d+")) {
					return true;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültiger Straßenname!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
			case "Hausnummer":
				if (text.matches("\\d+")) {
					return true;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige Hausnummer!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
			case "PLZ":
				if (text.matches("\\d+") && text.length() == 5) {
					return true;
				}
				else if (text.matches("\\d+") && text.length() != 5) {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Bitte geben Sie eine gültige PLZ ein!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
				else if(!text.matches("\\d+") && text.length() == 5) {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige Zeichen in der PLZ!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige PLZ!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
			case "Stadt":
				if (!text.matches("\\d+")) {
					return true;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültiger Stadtname!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
			case "Telefonnummer":
				if (text.matches("\\d+")) {
					return true;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige Telefonnummer!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
			case "Geburtstag":
				if (text.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*$")) {
					return true;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültiges Geburtsdatum!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
			case "Email":
				if (text.matches("(?:[a-zäöü0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
					return true;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige e-Mail-Adresse!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
			case "Homepage": 
				if (text.matches("(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?")) {
					return true;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige Homepage!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
				
			case "Arbeitsplatz":
				if (text.matches("[A-ZÜÄÖa-züäöß\\s]*")) {
					return true;
				}
				else {
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige Zeichen im Arbeitgebernamen!", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					return false;
				}
				
			case "Sonstiges":
				return true;
				
			default: 
				Window.alert("Switch case hat nicht ausgelöst!");
				return false;
		}	
	}
	
	/**
	 * Weitere Funktion zur Überprüfung von Text mit Hilfe eines regulären Ausdrucks,
	 * hier allerdings nur für den Namen mit einem String als Übergabeparameter.
	 *
	 * @param text zu überprüfender String
	 * @return true wenn Zeichenfolge in Ordnung
	 */
	public static boolean checkName (String text) {
			
		if (!text.matches("\\d+")) {
			if(!text.isEmpty()) {
				return true;
			}else {
				return false;
			}
		}
		else {
			final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ungültige Zeichen im Namen!", new ClientsideFunctions.OkButton());
			failed.getOkButton().addCloseDBClickHandler(failed);
			return false;
		}
	}
	
	/**
	 * Überprüft ob übergebener Nutzer der Urheber eines Kontakt-Objekts ist.
	 *
	 * @param c Kontakt
	 * @param user Nutzer
	 * @return true wenn der Nutzer der Urheber ist
	 */
	public static boolean isOwner (Contact c, User user) {
		
		if (user.getId() == c.getOwner()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Überprüft ob übergebener Nutzer der Urheber eines Kontaktlisten-Objekts ist.
	 *
	 * @param cl Kontaktliste
	 * @param user Nutzer
	 * @return true wenn der Nutzer der Urheber ist
	 */
	public static boolean isOwner (ContactList cl, User user) {
		
		if (user.getId() == cl.getOwner()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Die Klasse InputDialogBox wird genutzt um Eingaben des Nutzers zu ermöglichen.
	 * Hierfür stehen einige GWT-Elemente zur Auswahl, welche für unterschiedliche Funktionen zu
	 * Fenstern zusammengesetzt werden.
	 */
	public static class InputDialogBox extends DialogBox{
		
		/** The input. */
		private String input;
		
		/** The dialog box label. */
		Label dialogBoxLabel = new Label();
		
        /** The multi use text box. */
        private TextBox multiUseTextBox;
        
        /** The name text box. */
        private TextBox nameTextBox;
        
        /** The list box. */
        private ListBox listBox;
        
        /** The v text box. */
        private ValueTextBox vTextBox;
        
        /** The sb. */
        private SuggestBox sb;
        
        /** The oracle. */
        private MultiWordSuggestOracle oracle;
        
        /** The contact lists. */
        private Vector<ContactList> contactLists;
		
		/** The close. */
		CloseButton close=new CloseButton(this);

        /** The ok. */
        Button ok = new Button("OK");
		
		/**
		 * Instanziert eine InputDialogBox, welche genutzt wird um einen neuen Nutzer anzulegen.
		 *
		 * @param userEmail Email des neuen Nutzers
		 */
		public InputDialogBox(String userEmail) {
			
			setMultiUseTextBox(new TextBox());
			getMultiUseTextBox().getElement().setPropertyString("placeholder", "Vorname...");
			getMultiUseTextBox().setText("");
			
			setNameTextBox(new TextBox());
			getNameTextBox().getElement().setPropertyString("placeholder", "Nachname...");
			getNameTextBox().setText("");
			
			listBox = new ListBox();
			listBox.addItem("männlich");
			listBox.addItem("weiblich");
			listBox.addItem("Sonstiges");
			
			ok.addStyleName("okbutton");
	        close.addStyleName("closebutton");

			setText("Eingabe");
			setdialogBoxLabel(userEmail + " ist noch nicht registriert.\nBitte geben Sie Ihre Informationen an:");
			
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
	        
			VerticalPanel panel = new VerticalPanel();
	        panel.setHeight("100");
	        panel.setWidth("600");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			HorizontalPanel hpanel=new HorizontalPanel();
			hpanel.add(close);
	        hpanel.add(ok);
	        panel.add(multiUseTextBox);
	        panel.add(nameTextBox);
	        panel.add(listBox);
	        panel.add(hpanel);

	        setWidget(panel);
	        
	        show();
			center();
		}
        
		/**
		 * Instanziert eine InputDialogBox, welche genutzt wird um eine neue Kontaktliste anzulegen.
		 *
		 * @param inputtb TextBox, welche genutzt wird um Namen der Kontaktliste einzutragen
		 */
		public InputDialogBox(TextBox inputtb) {
			
			setMultiUseTextBox(inputtb);
	        ok.addStyleName("okbutton");
	        close.addStyleName("closebutton");
			
			setText("Eingabe");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
	        
			VerticalPanel panel = new VerticalPanel();
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			HorizontalPanel hpanel=new HorizontalPanel();
			hpanel.add(close);
	        hpanel.add(ok);
	        panel.add(multiUseTextBox);
	        panel.add(hpanel);

	        setWidget(panel);
	        
	        show();
	        center();
	    }
		
		/**
		 * Instanziert eine InputDialogBox, welche genutzt wird um eine neue Kontaktliste anzulegen.
		 * Verfügbare Kontaktlisten werden anhand des angemeldeten Nutzers identifiziert
		 *
		 * @param list ListBox in welcher verfügbare Kontaktlisten angezeigt werden sollen
		 * @param currentUser angemeldeter Nutzer
		 */
		public InputDialogBox(ListBox list, User currentUser) {
			
			setListBox(list);
	        ok.addStyleName("okbutton");
	        close.addStyleName("closebutton");
	        close.addCloseDBClickHandler(this);
			
			setText("Bitte wählen Sie eine Kontaktliste aus");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
			 editorAdministration.getAllOwnedContactListsOfActiveUser(currentUser, new AsyncCallback<Vector<ContactList>>() {
		        	
	        	public void onFailure(Throwable t) {
	        		Window.alert("Fehler beim Abruf der Kontaklisten des Nutzers");
	        	}
	        	
	        	public void onSuccess(Vector<ContactList> result) {
	        		
	        		setContactLists(contactLists);
	        		
	        		setContactLists(result);
	        		
	        		/*
	        		 * Alle Kontaktlisten, von denen der Nutzer Eigentümer ist, werden der ListBox hinzugefügt.
	        		 */
	        		for (ContactList cl : result) {
			        	getListBox().addItem(cl.getName());
	        		}
	        	}
			});
			
			VerticalPanel panel = new VerticalPanel();
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			HorizontalPanel hpanel=new HorizontalPanel();
			hpanel.add(close);
	        hpanel.add(ok);
	        panel.add(listBox);
	        panel.add(hpanel);

	        setWidget(panel);
	        
	        show();
	        center();
	    }
		
		/**
		 * Instantiates a new input dialog box.
		 *
		 * @param vtb the vtb
		 */
		public InputDialogBox(ValueTextBox vtb, String title) {
			
			setVTextBox(vtb);
	        ok.addStyleName("okbutton");
	        close.addStyleName("closebutton");
			
			setText(title);
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
	        
			VerticalPanel panel = new VerticalPanel();
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			HorizontalPanel hpanel=new HorizontalPanel();
			hpanel.add(close);
	        hpanel.add(ok);
	        panel.add(vTextBox);
	        panel.add(hpanel);

	        setWidget(panel);
	        
	        show();
	        center();
	    }
		
		/**
		 * Instantiates a new input dialog box.
		 *
		 * @param pid the pid
		 * @param row the row
		 * @param vtb the vtb
		 */
		public InputDialogBox(int pid, int row, ValueTextBox vtb) {
			
			setText("Neue Ausprägung hinzufügen");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
			/*Das innere Panel der DialogBox */
			VerticalPanel addValueDBPanel = new VerticalPanel();
			addValueDBPanel.setHeight("100px");
			addValueDBPanel.setWidth("230px");
		    addValueDBPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		    this.add(addValueDBPanel);
		    			
			Label addValueLabel = new Label();
			addValueDBPanel.add(addValueLabel);
					
		
			/*
			 * Je nachdem, um welche Art von Eigenschaft es sich handelt, wird der Identifier gesetzt, mit dem nach der switch-case
			 * die ValueTextBox erstellt wird. Der Identifier dient der Methode checkValue aus den ClientsideFunctions, welche auf korrekte 
			 * Eingabe überprüft.
			 */
			String identifier = null;
			
			switch(pid) {
			
				case 1: identifier = "Telefonnummer";
						addValueLabel.setText("Neue geschäftliche Telefonnummer: ");
						break;
						
				case 2: identifier = "Telefonnummer";
						addValueLabel.setText("Neue private Telefonnummer: ");
						break;
						
				case 3: identifier = "Email";
						addValueLabel.setText("Neue e-Mail-Adresse: ");
						break;
						
				case 5: identifier = "Arbeitsplatz";
						addValueLabel.setText("Neue Arbeitsstelle: ");
						break;
						
				case 10: identifier = "Homepage";
						addValueLabel.setText("Neue Homepage: ");
						break;
				default: 
						identifier = "Sonstiges";
						addValueLabel.setText("Neue Ausprägung: ");
						break;
			}
			
			setVTextBox(vtb);
			vtb.setIdentifier(identifier);
			addValueDBPanel.add(vTextBox);
			
			HorizontalPanel dbButtonsPanel=new HorizontalPanel();
			addValueDBPanel.add(dbButtonsPanel);
			
			close = new CloseButton(this);
			dbButtonsPanel.add(close);
			
			setOKButton(new OkButton("Hinzufügen"));
			dbButtonsPanel.add(ok);

	        show();
	        center();
	    }
		
		/**
		 * Instantiates a new input dialog box.
		 *
		 * @param inputOracle the input oracle
		 * @param labelString the label string
		 */
		public InputDialogBox(MultiWordSuggestOracle inputOracle, String labelString) {
			
			setOracle(inputOracle);
	        ok.addStyleName("okbutton");
	        close.addStyleName("closebutton");
	       // this.setPopupPosition(500, 200);
	        
			setdialogBoxLabel(labelString);
					
			editorAdministration.getAllUserSuggestions(user, new AsyncCallback<Vector<String>>() {
				public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
				public void onSuccess(Vector<String> arg0) {
					
					for(String s : arg0) {
						getOracle().add(s);
					}
					
					setSuggestBox(new SuggestBox(getOracle()));
					
					setText("Eingabe");
					setAnimationEnabled(true);
					setGlassEnabled(true);
					
					VerticalPanel panel = new VerticalPanel();
	
			        panel.setHeight("100");
			        panel.setWidth("300");
			        panel.setSpacing(10);
			        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			        panel.add(dialogBoxLabel);
					panel.add(getSuggestBox());
					HorizontalPanel hpanel= new HorizontalPanel();
			        hpanel.add(close);
			        hpanel.add(ok);
			        panel.add(hpanel);
			        
			        setWidget(panel);
			        
			        show();
			        center();
				}
			});
		}
		
		/**
		 * Gets the OK button.
		 *
		 * @return the OK button
		 */
		public Button getOKButton() {
			return this.ok;
		}
		
		/**
		 * Sets the OK button.
		 *
		 * @param b the new OK button
		 */
		public void setOKButton(Button b) {
			this.ok = b;
		}
		
		/**
		 * Getter von input.
		 *
		 * @return den Input
		 */
		public String getInput() {
			return this.input;
		}
		
		/**
		 * Setter von input.
		 *
		 * @param input der Input
		 */
		public void setInput(String input) {
			this.input = input;
		}

		/**
		 * Gets the label.
		 *
		 * @return the label
		 */
		public Label getdialogBoxLabel () {
			return this.dialogBoxLabel;
		}
		
		/**
		 * Setter des Labels.
		 *
		 * @param labelText der Text des Labels.
		 */
		public void setdialogBoxLabel (String labelText) {
			this.dialogBoxLabel.setText(labelText);
		}
		
		/**
		 * Gets the multi use text box.
		 *
		 * @return the multi use text box
		 */
		public TextBox getMultiUseTextBox() {
			return this.multiUseTextBox;
		}
		
		/**
		 * Sets the multi use text box.
		 *
		 * @param tb the new multi use text box
		 */
		public void setMultiUseTextBox(TextBox tb) {
			this.multiUseTextBox = tb;
		}

		/**
		 * Gets the suggest box.
		 *
		 * @return the suggest box
		 */
		public SuggestBox getSuggestBox() {
			return sb;
		}

		/**
		 * Sets the suggest box.
		 *
		 * @param sb the new suggest box
		 */
		public void setSuggestBox(SuggestBox sb) {
			this.sb = sb;
		}

		/**
		 * Gets the oracle.
		 *
		 * @return the oracle
		 */
		public MultiWordSuggestOracle getOracle() {
			return oracle;
		}

		/**
		 * Sets the oracle.
		 *
		 * @param oracle the new oracle
		 */
		public void setOracle(MultiWordSuggestOracle oracle) {
			this.oracle = oracle;
		}

		/**
		 * Gets the name text box.
		 *
		 * @return the name text box
		 */
		public TextBox getNameTextBox() {
			return nameTextBox;
		}

		/**
		 * Sets the name text box.
		 *
		 * @param nameTextBox the new name text box
		 */
		public void setNameTextBox(TextBox nameTextBox) {
			this.nameTextBox = nameTextBox;
		}

		/**
		 * Gets the list box.
		 *
		 * @return the list box
		 */
		public ListBox getListBox() {
			return listBox;
		}

		/**
		 * Sets the list box.
		 *
		 * @param listBox the new list box
		 */
		public void setListBox(ListBox listBox) {
			this.listBox = listBox;
		}

		/**
		 * Gets the v text box.
		 *
		 * @return the v text box
		 */
		public ValueTextBox getVTextBox() {
			return vTextBox;
		}

		/**
		 * Sets the v text box.
		 *
		 * @param vTextBox the new v text box
		 */
		public void setVTextBox(ValueTextBox vTextBox) {
			this.vTextBox = vTextBox;
		}

		/**
		 * Gets the contact lists.
		 *
		 * @return the contact lists
		 */
		public Vector<ContactList> getContactLists() {
			return contactLists;
		}

		/**
		 * Sets the contact lists.
		 *
		 * @param contactLists the new contact lists
		 */
		public void setContactLists(Vector<ContactList> contactLists) {
			this.contactLists = contactLists;
		}
	}
	
	/**
	 * The Class popUpBox.
	 */
	public static class popUpBox extends DialogBox {
		
		/** The close button. */
		CloseButton closeButton = null;
		
		/** The ok button. */
		OkButton okButton = null;
		
		/** The panel. */
		VerticalPanel panel = null;
		
		/** The hpanel. */
		HorizontalPanel hpanel = null;
		
		/** The dialog box label. */
		Label dialogBoxLabel = new Label();
		
		/**
		 * Instantiates a new pop up box.
		 *
		 * @param db the db
		 * @param dbLabel the db label
		 */
		public popUpBox(DialogBox db, String dbLabel) {
			closeButton = new CloseButton(db);
			okButton = new OkButton();
			dialogBoxLabel.setText(dbLabel);
			
			this.setText("Meldung");
		//	this.setPopupPosition(500, 200);
			panel = new VerticalPanel();
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			hpanel = new HorizontalPanel();
			hpanel.add(closeButton);
	        hpanel.add(okButton);
	        panel.add(hpanel);

	        setWidget(panel);
	        
	        show();
	        center();
		}
		
		/**
		 * Instantiates a new pop up box.
		 *
		 * @param dbLabel the db label
		 * @param ok the ok
		 * @param close the close
		 */
		public popUpBox(String dbLabel, OkButton ok, CloseButton close) {
			closeButton = close;
			okButton = ok;
			dialogBoxLabel.setText(dbLabel);
			
			this.setText("Meldung");
		//	this.setPopupPosition(500, 200);
			panel = new VerticalPanel();
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			hpanel = new HorizontalPanel();
			hpanel.add(closeButton);
	        hpanel.add(okButton);
	        panel.add(hpanel);
	        
	        setWidget(panel);
	        
	        show();
	        center();
		}
		
		/**
		 * Instantiates a new pop up box.
		 *
		 * @param dbLabel the db label
		 * @param ok the ok
		 */
		public popUpBox(String dbLabel, OkButton ok) {
			okButton = ok;
			dialogBoxLabel.setText(dbLabel);
			
			this.setText("Meldung");
		//	this.setPopupPosition(500, 200);
			panel = new VerticalPanel();
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			hpanel = new HorizontalPanel();
	        hpanel.add(okButton);
	        panel.add(hpanel);
	        
	        setWidget(panel);
	        
	        show();
	        center();
		}
		
		/**
		 * Instantiates a new pop up box.
		 *
		 * @param dbLabel the db label
		 * @param close the close
		 */
		public popUpBox(String dbLabel, CloseButton close) {
			closeButton = close;
			dialogBoxLabel.setText(dbLabel);
			
			this.setText("Meldung");
		//	this.setPopupPosition(500, 200);
			panel = new VerticalPanel();
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(dialogBoxLabel);
			hpanel = new HorizontalPanel();
	        hpanel.add(closeButton);
	        panel.add(hpanel);
	        
	        setWidget(panel);
	        
	        show();
	        center();
		}
		
		/**
		 * Gets the close button.
		 *
		 * @return the close button
		 */
		public CloseButton getCloseButton() {
			return this.closeButton;
		}
		
		/**
		 * Sets the close button.
		 *
		 * @param b the new close button
		 */
		public void setCloseButton(CloseButton b) {
			this.closeButton = b;
		}
		
		/**
		 * Gets the ok button.
		 *
		 * @return the ok button
		 */
		public OkButton getOkButton() {
			return this.okButton;
		}
		
		/**
		 * Sets the ok button.
		 *
		 * @param b the new ok button
		 */
		public void setOkButton(OkButton b) {
			this.okButton = b;
		}
	}
	
	/**
	 * The Class CloseButton.
	 */
	public static class CloseButton extends Button{
		
		/** The db. */
		DialogBox db;
		
		/**
		 * Instantiates a new close button.
		 *
		 * @param db the db
		 */
		public CloseButton(DialogBox db) {
			this.db = db;
			this.addClickHandler(new CloseDBClickHandler(db)); 
			this.setText("Abbrechen");
			this.addStyleName("closebutton");
		}
		
		/**
		 * Instantiates a new close button.
		 *
		 * @param text the text
		 */
		public CloseButton(String text) {
			this.setText(text);
			this.addStyleName("closebutton");
		}
		
		/**
		 * Instantiates a new close button.
		 */
		public CloseButton() {
			this.setText("Abbrechen");
			this.addStyleName("closebutton");
		}
		
		/**
		 * Adds the close DB click handler.
		 *
		 * @param db the db
		 */
		public void addCloseDBClickHandler(DialogBox db) {
			this.addClickHandler(new CloseDBClickHandler(db));
		}
		
		/**
		 * The Class CloseDBClickHandler.
		 */
		private class CloseDBClickHandler implements ClickHandler{
			
			/** The db. */
			DialogBox db;
	
			
			/**
			 * Instantiates a new close DB click handler.
			 *
			 * @param db the db
			 */
			public CloseDBClickHandler(DialogBox db) {
				this.db=db;
			}
			
			/* (non-Javadoc)
			 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				db.hide();
			}
		}
	}
	
	/**
	 * The Class OkButton.
	 */
	public static class OkButton extends Button{

		
		/**
		 * Instantiates a new ok button.
		 *
		 * @param text the text
		 */
		public OkButton(String text) {
			this.setText(text);
			this.addStyleName("okbutton");
		}
		
		/**
		 * Instantiates a new ok button.
		 */
		public OkButton() {
			this.setText("OK");
			this.addStyleName("okbutton");
		}
		
		/**
		 * Adds the close DB click handler.
		 *
		 * @param db the db
		 */
		public void addCloseDBClickHandler(DialogBox db) {
			this.addClickHandler(new CloseDBClickHandler(db));
		}
		
		/**
		 * The Class CloseDBClickHandler.
		 */
		private class CloseDBClickHandler implements ClickHandler{
			
			/** The db. */
			DialogBox db;
	
			
			/**
			 * Instantiates a new close DB click handler.
			 *
			 * @param db the db
			 */
			public CloseDBClickHandler(DialogBox db) {
				this.db=db;
			}
			
			/* (non-Javadoc)
			 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
			 */
			public void onClick(ClickEvent event) {
				db.hide();
			}
		}
	}
}

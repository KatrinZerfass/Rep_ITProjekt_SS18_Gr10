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
 * Ergänzt die Klasse {@link ClientsideSettings} um weitere Funktionen und Dienste,
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
				final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Unbekannter Wert!", new ClientsideFunctions.OkButton());
				failed.getOkButton().addCloseDBClickHandler(failed);
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
		
		/** Diverse Attribute und GWT Elemente die zur Realisierung der InputDialogBox benötigt werden. */
		private String input;
		Label dialogBoxLabel = new Label();
        private TextBox multiUseTextBox;
        private TextBox nameTextBox;
        private ListBox listBox;
        private ValueTextBox vTextBox;
        private SuggestBox sb;
        private MultiWordSuggestOracle oracle;
        private Vector<ContactList> contactLists;
		CloseButton close = new CloseButton(this);
        OkButton ok = new OkButton();
		
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
	        		System.out.println(t.getMessage());
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
		 * Instanziert eine InputDialogBox, welche genutzt wird um eine neue Eigenschaft anzulegen.
		 *
		 * @param vtb {@link ValueTextBox}, welche mit Namen der neuen Eigenschaft gefüllt werden soll
		 * @param title Text der angezeigt wird
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
		 * Instanziert eine InputDialogBox, welche genutzt wird um eine neue Ausprägungen hinzuzufügen.
		 * Wichtig sind hierbei wie viele Ausprägungen bereits angezeigt werden.
		 *
		 * @param pid ID der betroffenen Eigenschaft
		 * @param row Zeile im {@link ContactForm} in der die neue Ausprägung angezeigt werden soll
		 * @param vtb {@link ValueTextBox}
		 */
		public InputDialogBox(int pid, int row, ValueTextBox vtb) {
			
			setText("Neue Ausprägung hinzufügen");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
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
		 * Instanziert eine InputDialogBox, welche genutzt wird um Kontakte und Kontaktlisten zu teilen.
		 * Enthält eine SuggestBox, welche mit Nutzer Klarnamen und deren Emailadressen befüllt wird.
		 *
		 * @param inputOracle neues Oracle, Input für die SuggestBox
		 * @param labelString angezeigte Nachricht
		 */
		public InputDialogBox(MultiWordSuggestOracle inputOracle, String labelString) {
			
			setOracle(inputOracle);
	        ok.addStyleName("okbutton");
	        close.addStyleName("closebutton");
	        
			setdialogBoxLabel(labelString);
					
			editorAdministration.getAllUserSuggestions(user, new AsyncCallback<Vector<String>>() {
				public void onFailure(Throwable t) {
					System.out.println(t.getMessage());
					
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
		 * Getter für OkButton
		 *
		 * @return OkButton
		 */
		public OkButton getOKButton() {
			return this.ok;
		}
		
		/**
		 * Setter für OkButton
		 *
		 * @param b neuer OkButton
		 */
		public void setOKButton(OkButton b) {
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
		 * Getter für Label
		 *
		 * @return Label
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
		 * Getter für MultiUseTextBox.
		 *
		 * @return MultiUseTextBox
		 */
		public TextBox getMultiUseTextBox() {
			return this.multiUseTextBox;
		}
		
		/**
		 * Setter für MultiUseTextBox.
		 *
		 * @param tb neue MultiUseTextBox
		 */
		public void setMultiUseTextBox(TextBox tb) {
			this.multiUseTextBox = tb;
		}

		/**
		 * Getter für SuggestBox.
		 *
		 * @return SuggestBox
		 */
		public SuggestBox getSuggestBox() {
			return sb;
		}

		/**
		 * Setter für SuggestBox.
		 *
		 * @param sb neue SuggestBox
		 */
		public void setSuggestBox(SuggestBox sb) {
			this.sb = sb;
		}

		/**
		 * Getter für Oracle.
		 *
		 * @return Oracle
		 */
		public MultiWordSuggestOracle getOracle() {
			return oracle;
		}

		/**
		 * Setter für Oracle.
		 *
		 * @param oracle neues Oracle
		 */
		public void setOracle(MultiWordSuggestOracle oracle) {
			this.oracle = oracle;
		}

		/**
		 * Getter für NameTextBox.
		 *
		 * @return NameTextBox
		 */
		public TextBox getNameTextBox() {
			return nameTextBox;
		}

		/**
		 * Setter für NameTextBox.
		 *
		 * @param nameTextBox neue NameTextBox
		 */
		public void setNameTextBox(TextBox nameTextBox) {
			this.nameTextBox = nameTextBox;
		}

		/**
		 * Getter für ListBox.
		 *
		 * @return ListBox
		 */
		public ListBox getListBox() {
			return listBox;
		}

		/**
		 * Setter für ListBox.
		 *
		 * @param listBox neue ListBox
		 */
		public void setListBox(ListBox listBox) {
			this.listBox = listBox;
		}

		/**
		 * Getter für ValueTextBox.
		 *
		 * @return ValueTextBox
		 */
		public ValueTextBox getVTextBox() {
			return vTextBox;
		}

		/**
		 * Setter für ValueTextBox.
		 *
		 * @param vTextBox ValueTextBox
		 */
		public void setVTextBox(ValueTextBox vTextBox) {
			this.vTextBox = vTextBox;
		}

		/**
		 * Getter für Kontaktliste.
		 *
		 * @return Kontaktliste.
		 */
		public Vector<ContactList> getContactLists() {
			return contactLists;
		}

		/**
		 * Setter für Kontaktliste.
		 *
		 * @param contactLists Kontaktliste
		 */
		public void setContactLists(Vector<ContactList> contactLists) {
			this.contactLists = contactLists;
		}
	}
	
	/**
	 * Die Klasse popUpBox ersetzt Window.alert() und dient zusätzlich zur Sicherheitsabfrage beim löschen.
	 */
	public static class popUpBox extends DialogBox {
		

		CloseButton closeButton = null;
		OkButton okButton = null;
		VerticalPanel panel = null;
		HorizontalPanel hpanel = null;
		Label dialogBoxLabel = new Label();
		
		/**
		 * Instanziert popUpBox, welche zur Sicherheitsabfrage beim löschen dient.
		 *
		 * @param dbLabel angezeigter Text
		 * @param ok OkButton
		 * @param close CloseButton
		 */
		public popUpBox(String dbLabel, OkButton ok, CloseButton close) {
			closeButton = close;
			okButton = ok;
			dialogBoxLabel.setText(dbLabel);
			
			this.setText("Meldung");
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
		 * Instantiates neue popUpBox, dient als simples Feedback für gerade ausgeführte Aktion.
		 *
		 * @param dbLabel angezeigter Text
		 * @param ok OkButton
		 */
		public popUpBox(String dbLabel, OkButton ok) {
			okButton = ok;
			dialogBoxLabel.setText(dbLabel);
			
			this.setText("Meldung");
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
		 * Instantiates neue popUpBox, dient als simples Feedback für gerade ausgeführte Aktion.
		 *
		 * @param dbLabel angezeigter Text
		 * @param close CloseButton
		 */
		public popUpBox(String dbLabel, CloseButton close) {
			closeButton = close;
			dialogBoxLabel.setText(dbLabel);
			
			this.setText("Meldung");
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
		 * Getter für CloseButton.
		 *
		 * @return CloseButton
		 */
		public CloseButton getCloseButton() {
			return this.closeButton;
		}
		
		/**
		 * Setter für CloseButton.
		 *
		 * @param b neuer CloseButton
		 */
		public void setCloseButton(CloseButton b) {
			this.closeButton = b;
		}
		
		/**
		 * Getter für OkButton.
		 *
		 * @return OkButton
		 */
		public OkButton getOkButton() {
			return this.okButton;
		}
		
		/**
		 * Setter für OkButton
		 *
		 * @param b neuer OkButton
		 */
		public void setOkButton(OkButton b) {
			this.okButton = b;
		}
	}
	
	/**
	 * Die Klasse CloseButton erweitert Button um einen Style und standard Clickhandler.
	 */
	public static class CloseButton extends Button{
		
		/** zu schließende DialogBox */
		DialogBox db;
		
		/**
		 * Instanziert CloseButton, übergabe enthält zu schließende DialogBox.
		 *
		 * @param db zu schließende DialogBox
		 */
		public CloseButton(DialogBox db) {
			this.db = db;
			this.addClickHandler(new CloseDBClickHandler(db)); 
			this.setText("Abbrechen");
			this.addStyleName("closebutton");
		}
		
		/**
		 * Instanziert CloseButton mit verändertem Text.
		 *
		 * @param text neuer Text des CloseButtons
		 */
		public CloseButton(String text) {
			this.setText(text);
			this.addStyleName("closebutton");
		}
		
		/**
		 * Instanziert CloseButton.
		 */
		public CloseButton() {
			this.setText("Abbrechen");
			this.addStyleName("closebutton");
		}
		
		/**
		 * Fügt standard ClickHandler zum schließen einer DialogBox zum Button hinzu.
		 *
		 * @param db DialogBox
		 */
		public void addCloseDBClickHandler(DialogBox db) {
			this.addClickHandler(new CloseDBClickHandler(db));
		}
		
		/**
		 * Standard ClickHandler zum schließen einer DialogBox.
		 */
		private class CloseDBClickHandler implements ClickHandler{
			
			DialogBox db;
	
			public CloseDBClickHandler(DialogBox db) {
				this.db=db;
			}
			public void onClick(ClickEvent event) {
				db.hide();
			}
		}
	}
	
	/**
	 * Die Klasse OkButton erweitert Button um einen Style und standard Clickhandler.
	 */
	public static class OkButton extends Button{

		
		/**
		 * Instanziert OkButton mit verändertem Text.
		 *
		 * @param text der neue Text des Buttons
		 */
		public OkButton(String text) {
			this.setText(text);
			this.addStyleName("okbutton");
		}
		
		/**
		 * Instanziert OkButton.
		 */
		public OkButton() {
			this.setText("OK");
			this.addStyleName("okbutton");
		}
		
		/**
		 * Fügt standard ClickHandler zum schließen einer DialogBox zum Button hinzu.
		 *
		 * @param db DialogBox
		 */
		public void addCloseDBClickHandler(DialogBox db) {
			this.addClickHandler(new CloseDBClickHandler(db));
		}
		
		/**
		 * Standard ClickHandler zum schließen einer DialogBox.
		 */
		private class CloseDBClickHandler implements ClickHandler{
			
			DialogBox db;
	
			public CloseDBClickHandler(DialogBox db) {
				this.db=db;
			}
			public void onClick(ClickEvent event) {
				db.hide();
			}
		}
	}
}

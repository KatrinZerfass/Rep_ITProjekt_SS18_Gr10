package com.google.gwt.sample.itProjekt.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die Klasse ContactForm dient zur Darstellung von Kontakten mit all ihren Eigenschaften und deren Ausprägungen.
 * @author KatrinZerfass
 */

public class ContactForm extends VerticalPanel {
	
	
	EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	
	ContactListContactTreeViewModel clctvm = null;
	
	/** Der anzuzeigende Kontakt */
	Contact contactToDisplay = null;
	
	/**Alle Ausprägungen des anzuzeigenden Kontakts*/
	Vector<Value> allValuesOfContact = null;
	
	/**Ein Vector, in dem alle im Kontaktformular instantiierten ValueTextBoxes gespeichert werden. */
	Vector<ValueTextBox> allValueTextBoxes = new Vector<ValueTextBox>();
	
	
	/** The firstname text box. */
	TextBox firstnameTextBox = new TextBox();
	
	/** The lastname text box. */
	TextBox lastnameTextBox = new TextBox();
	
	/** The birthday text box. */
	ValueTextBox birthdayTextBox ;
	
	/** The sex list box. */
	ListBox sexListBox = new ListBox();
	
	/** The street text box. */
	TextBox streetTextBox = new TextBox();
	
	/** The house nr text box. */
	TextBox houseNrTextBox = new TextBox();
	
	/** The plz text box. */
	TextBox plzTextBox = new TextBox();
	
	/** The city text box. */
	TextBox cityTextBox = new TextBox();
	
	
	/**
	 * Die innere Klasse LockButton.
	 * Sie dient der Darstellung der Buttons mit dem Schloss-Symbol, welche hinter
	 * jeder einzelnen Eigenschaftsausprägung die Möglichkeit bieten, diese Ausprägung nicht zu teilen bzw. wieder zu teilen. 
	 */
	public class LockButton extends PushButton{
			
		/** Die Ausprägung, auf welche der jeweilige LockButton referenziert */
		private Value value;
		
		private boolean isLocked = false;
		
		private Image lockUnlocked = new Image("lock_unlocked.png");
		private Image lockLocked = new Image ("lock_locked.png");
		
		/**
		 * Der Konstruktor von LockButton. Es wird dem Button ein ClickHandler hinzugefügt, welcher den Übergang der Zustände von 
		 * <code>isLocked</code> bzw. von <code>isShared</code> (bezogen auf die Ausprägung) regelt.
		 */
		public LockButton() {
			
			lockUnlocked.setPixelSize(20, 20);
			lockLocked.setPixelSize(20, 20);
			
			//per default sind alle Ausprägungen geteilt, d.h. das Schloss ist zu Beginn unlocked.
			this.getUpFace().setImage(lockUnlocked);
			
			this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(isLocked ==false) { // =Schloss ist geöffnet
				setValueToUnshared();	// =Schloss soll geschlossen werden, d.h. die Ausprägung wird nicht geteilt.
				}else setValueToShared();
			}
		});
		}
		
		/**
		 * Setter von <code>value</code>
		 * 
		 * @param Value die referenzierte Ausprägung
		 */
		public void setValue(Value v) {
			this.value = v;
		}
	
		
		/**
		 * Methode, die beim Klicken eines geöffneten LockButtons aufgerufen wird. Folge: das Schloss wird geschlossen
		 * und die Variable <code>isShared</code> der Ausprägung wird auf false gesetzt. Diese Veränderung wird mit einem 
		 * Aufruf der Methode <code>editValue()</code> an den Server weitergegeben.
		 */
		public void setValueToUnshared() {
			this.getUpFace().setImage(lockLocked);
			isLocked = true;
			editorAdministration.editValue(contactToDisplay, value.getPropertyid(), this.value, value.getContent(), false, new AsyncCallback<Value>() {
				public void onFailure(Throwable t) {
					
				}
				public void onSuccess(Value v) {
								
				}
			});
		}
		
		/**
		 * Methode, die beim Klicken eines geschlossenen LockButtons aufgerufen wird. Folge: das Schloss wird geöffnet
		 * und die Variable <code>isShared</code> der Ausprägung wird auf <code>true</code> gesetzt. Diese Veränderung wird mit einem 
		 * Aufruf der Methode <code>editValue()</code> an den Server weitergegeben.
		 */
		public void setValueToShared() {
			this.getUpFace().setImage(lockUnlocked);
			isLocked = false;
			editorAdministration.editValue(contactToDisplay, value.getPropertyid(), this.value, value.getContent(), true, new AsyncCallback<Value>() {
				public void onFailure(Throwable t) {
					
				}
				public void onSuccess(Value v) {
								
				}
			});
		}	
	}
			
	
	
	/**
	 * Die innere Klasse DeleteValueButton.
	 * Sie dient der Darstellung der Buttons, welche hinter jeder einzelnen Ausprägung die Möglichkeit geben, diese zu löschen.
	 * 
	 */
	public class DeleteValueButton extends Button{
		
		/** Die Ausprägung, auf welche der jeweilige DeleteValueButton referenziert */
		private Value value;
		
		/**
		 * Konstruktor von DeleteValueButton. Es wird dem Button ein ClickHandler hinzugefügt, welcher die Methode <code>deleteValue()</code> aufruft.
		 *
		 * @param String Text auf dem Button
		 */
		public DeleteValueButton(String text) {
			super(text);
			
			this.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					editorAdministration.deleteValue(value, new AsyncCallback<Void>() {
						public void onFailure(Throwable t) {	
						}
						
						public void onSuccess(Void result) {	
						}

					});
				}
			});
				
		}
		
		/**
		 * Setter von <code>value</code>
		 * 
		 * @param Value die referenzierte Ausprägung
		 */
		public void setValue(Value v) {
			this.value=v;
		}
		
		/**
		 * Methode, um die DeleteValueButtons auszugrauen. Wird aufgerufen, wenn der angemeldete Nutzer nicht der Eigentümer des
		 * angezeigten Kontakts ist.
		 */
		public void disableButtons() {
			this.setEnabled(false);
		}
	}
	
	
	
	/**
	 * Die innere Klasse AddValueButton.
	 * Sie dient der Darstellung der Buttons, mit welchen man eine neue Ausprägung der jeweiligen Eigenschaft hinzufügen kann.
	 */
	public class AddValueButton extends Button{
		
		/** Die referenzierte Eigenschaftsart. */
		private int propertyId;
		
		/**
		 * Konstruktor von AddValueButton. Setzt die Id der referenzierten Eigenschaft und fügt dem Button einen ClickHandler
		 * hinzu, welcher ein neues PopUp-Fenster für das Hinzufügen einer Eigenschaftsausprägung öffnen.
		 *
		 * @param int die ID der referenzierten Eigenschaft
		 */
		public AddValueButton(int pid) {
			this.propertyId= pid;
		
			
			this.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					addValuePopUp();	
				}
			});
		}
		
		/**
		 * Gibt die ID der referenzierten Eigenschaft zurück.
		 *
		 * @return die ID der referenzierten Eigenschaft
		 */
		public int getPropertyId() {
			return this.propertyId;
		}
	}
	
	
	
	
	/**
	 * Die innere Klasse ValueTextBox.
	 * Sie dient der Darstellungen der Textboxen, in denen Eigenschaftsausprägungen angezeigt und bearbeitet werden können.
	 * Die Besonderheit ist hier ein valueChangeHandler, welcher reagiert, wenn der Benutzer den Wert in der Textbox verändert.
	 * 
	 */
	public class ValueTextBox extends TextBox{
		
		/**Um später auslesen zu können, bei welcher TextBox Änderungen vorgenommen wurden.*/
		private boolean isChanged=false;
		
		/** Die referenzierte Ausprägung, welche in der TextBox angezeigt wird. */
		private Value value;
		
		/**
		 * Konstruktor von ValueTextBox. Fügt der TextBox den valueChangeHandler hinzu,  welcher den Zustand von <code>isChanged</code> ändert.
		 */
		public ValueTextBox() {
			
			this.addValueChangeHandler(new ValueChangeHandler<String>(){

				public void onValueChange(ValueChangeEvent<String> event) {
					isChanged = true;					
				}
				
			});
		}
		
		/**
		 * Getter von <code>isChanged</code>
		 *
		 * @return Wert der Variable <code>isChanged</code>
		 */
		public boolean getIsChanged() {
			return this.isChanged;
		}
		
		/**
		 * Getter von <code>value</code>
		 *
		 * @return die referenzierte Ausprägung
		 */
		public Value getTextBoXValue() {
			return this.value;
		}
		
		/**
		 * Setter von <code>value</code>
		 * 
		 * @param Value die anzuzeigende Ausprägung
		 */
		public void setValue(Value v) {
			this.value = v;
		}
	}
	
	
	
	/**
	 * Die innere Klasse ValueDisplay. Sie erbt von HorizontalPanel.
	 * Sie dient der Darstellung einer Eigenschaftsausprägung in den jeweiligen FlexTables der einzelnen Eigenschaften.
	 * Diese besteht immer aus einer <code>ValueTextBox</code>, einem <code>LockButton</code> und einem <code>DeleteValueButton</code>.
	 */
	public class ValueDisplay extends HorizontalPanel{
		
		/** Die anzuzeigende Ausprägung */
		private Value value = null;
		
		private ValueTextBox valueTextBox;
		private LockButton lockButton;
		private DeleteValueButton deleteValueButton;
		
		/**
		 * Konstruktor von ValueDisplay. Der Inhalt der Ausprägung wird in der <code>ValueTextBox</code> angezeigt und <code>ValueTextBox</code>, 
		 * <code>LockButton</code> und <code>DeleteValueButton</code> werden dem Panel hinzugefügt. 
		 *
		 *@param ValueTextBox die dazugehörige TextBox
		 */
		public ValueDisplay(ValueTextBox vtb) {
			valueTextBox = vtb;
			valueTextBox.setValue(getValue());
			valueTextBox.setText(getValue().getContent());
			
			lockButton = new LockButton();
			lockButton.setValue(getValue());
			
			deleteValueButton = new DeleteValueButton(" x ");
			deleteValueButton.setValue(getValue());
			
			this.add(valueTextBox);
			this.add(lockButton);
			this.add(deleteValueButton);
			
		}
		
		/**
		 * Setter von <code>value</code>
		 * 
		 * @param Value die anzuzeigende Ausprägung
		 */
		public void setValue(Value v) {
			this.value = v;
		}
		
		/**
		 * Getter von <code>value</code>
		 * 
		 * @return die anzuzeigende Ausprägung
		 */
		public Value getValue() {
			return this.value;
		}
	}
	

	
	
	

	/**
	 * Die Methode <code>onLoad()</code> wird in der EntryPoint-Klasse aufgerufen, um im GUI eine Instanz von ContactForm zu erzeugen.
	 */
	public void onLoad() {
		
		super.onLoad();
		
		/* 
		 * Zunächst wird der angemeldete Nutzer abgefragt. Ist dieser nicht Eigentümer des anzuzeigenden Kontakts, so werden alle Funktionen, 
		 * die zur Bearbeitung des Kontakts dienen (Buttons etc.) ausgegraut bzw. disabled.
		 */
		//To Do! 
		
		
		/*
		 * Als nächstes wird geprüft, ob das Kontaktformular infolge des Auswählens eines bestimmten Kontakts aufgerufen wird. 
		 * Ist dies der Fall, so werden alle Ausprägungen des contactToDisplay ausgelesen und in einem Vector<Values> gespeichert.
		 */
		if(contactToDisplay!= null) {
			editorAdministration.getAllValuesOf(contactToDisplay, new AsyncCallback<Vector<Value>>() {
				public void onFailure(Throwable t) {
					
				}
				public void onSuccess(Vector<Value> values) {
					for (Value v : values) {
						allValuesOfContact = new Vector<Value>();
						allValuesOfContact.add(v);
					}
				}
			});
		}
		
	
		/*
		 * Allumfassende Tabelle zur Darstellung von Kontakten
		 */
		FlexTable contactTable = new FlexTable();
		this.add(contactTable);
		
		/*
		 * Nullte Zeile
		 */
		contactTable.getFlexCellFormatter().setColSpan(0, 0, 4);
		contactTable.setText(0, 0, "  ");
		
		
		/*
		 * Erste Zeile: Überschrift
		 */
		contactTable.getFlexCellFormatter().setColSpan(1, 0, 4);
		Label contactInfoLabel = new Label("Kontaktinformationen");
		contactTable.setWidget(1, 0, contactInfoLabel);
		
		
		/*
		 * Zweite Zeile: Vor- und Nachname
		 */
		Label firstnameLabel = new Label("Vorname: ");
		contactTable.setWidget(2, 0, firstnameLabel);
		contactTable.setWidget(2, 1, firstnameTextBox);
		
		Label lastnameLabel = new Label("Nachname: ");
		contactTable.setWidget(2, 2, lastnameLabel);
		contactTable.setWidget(2, 3, lastnameTextBox);
		
		/*
		 * Wenn ein anzuzeigender Kontakt vorliegt, wird dessen Vor- und Nachname in den dazugehörigen TextBoxes angezeigt.
		 * Ist dies nicht der Fall, werden die TextBoxes mit einem Placeholder aufgefüllt.
		 */
		if (contactToDisplay !=null) {
			firstnameTextBox.setText(contactToDisplay.getFirstname());
			lastnameTextBox.setText(contactToDisplay.getLastname());
		}else {
			firstnameTextBox.getElement().setPropertyString("placeholder", "Vorname...");
			lastnameTextBox.getElement().setPropertyString("placeholder", "Nachname...");
		}
		
		
		/*
		 * Dritte Zeile: Geschlecht und Geburtsdatum
		 */
		Label sexLabel = new Label("Geschlecht: ");
		sexListBox.addItem("männlich");
		sexListBox.addItem("weiblich");
		sexListBox.addItem("Sonstiges");
		
		contactTable.setWidget(3, 0, sexLabel);
		contactTable.setWidget(3, 1, sexListBox);
		
		/*
		 * Abfrage und Anzeigen des Geschlechts, wenn ein Kontakt vorliegt.
		 */
		if(contactToDisplay!=null) {
			if (contactToDisplay.getSex() == "m") {
				sexListBox.setItemSelected(0, true);
			}
			else if(contactToDisplay.getSex() == "f"){
				sexListBox.setItemSelected(1, true);
			}
			else if(contactToDisplay.getSex() == "o") {
				sexListBox.setItemSelected(2, true);
			}
			sexListBox.setEnabled(false);
		}
		
		
		
		Label birthdateLabel = new Label("Geburtsdatum: ");
		contactTable.setWidget(3, 2, birthdateLabel);
		
		birthdayTextBox = new ValueTextBox();
		allValueTextBoxes.add(birthdayTextBox);
		
		ValueDisplay birthdayDisplay = new ValueDisplay(birthdayTextBox);
		contactTable.setWidget(3, 2, birthdayDisplay);
		
		/*
		 * Wenn ein anzuzeigender Kontakt vorliegt, wird der dazugehörige Vector seiner Ausprägungen nach der Ausprägung mit der Eigenschaftsart
		 * "Geburtsdatum" durchsucht (P_ID = 00000004). Ist sie gefunden, wird sie als Ausprägung des birthdayDisplay gesetzt.
		 * Liegt kein anzuzeigender Kontakt vor, wird die TextBox mit einem Placeholder aufgefüllt.
		 */ 
		if (contactToDisplay !=null){
			for(int i=0; i<allValuesOfContact.size(); i++) {
				if(allValuesOfContact.get(i).getPropertyid()== 000000004) {
					birthdayDisplay.setValue(allValuesOfContact.get(i));
				}
			}
		}
		else {
			birthdayTextBox.getElement().setPropertyString("placeholder", "Geburtsdatum...");
		}
		
		

		
		//Vierte Zeile
		Label addressLabel = new Label("Anschrift: ");
		contactTable.setWidget(4, 0, addressLabel);
		
		contactTable.getFlexCellFormatter().setColSpan(4, 1, 3);
		FlexTable addressTable = new FlexTable();
		contactTable.setWidget(4, 1, addressTable);
		
		
		streetTextBox.getElement().setPropertyString("placeholder", "Straße...");
		addressTable.setWidget(0, 0, streetTextBox);
		houseNrTextBox.getElement().setPropertyString("placeholder", "Hausnummer...");
		addressTable.setWidget(0, 1, houseNrTextBox);
		plzTextBox.getElement().setPropertyString("placeholder", "PLZ...");
		addressTable.setWidget(1, 0, plzTextBox);
		cityTextBox.getElement().setPropertyString("placeholder", "Wohnort...");
		addressTable.setWidget(1, 1, cityTextBox);
		
		addressTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
		addressTable.setWidget(0, 2, new LockButton());
		
//		//nur zum innere Rahmenlinien anzeigen lassen, zu Debug-Zwecken
//		for (int i= 0; i<addressTable.getRowCount(); i++) {
//			for (int a=0; a<addressTable.getCellCount(i); a++) {
//				addressTable.getCellFormatter().addStyleName(i, a, "cellBordersGreen");
//			}
//		}
		
		//Fünfte Zeile
		Label privatePhoneNumbersLabel = new Label("Telefonnummern privat: ");
		contactTable.setWidget(5, 0, privatePhoneNumbersLabel);
		
		FlexTable privatePhoneNumbersTable = new FlexTable();
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(0,1,2);
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(1,1,2);
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(2,1,2);
		contactTable.getFlexCellFormatter().setColSpan(5, 1, 3);
		contactTable.setWidget(5, 1, privatePhoneNumbersTable);
		
		
		Button addPrivatePhoneNumberButton = new Button(" + ");
		privatePhoneNumbersTable.setWidget(0, 0, addPrivatePhoneNumberButton);
		
		
		TextBox privateNrTextBox = new TextBox();
		privateNrTextBox.getElement().setPropertyString("placeholder", "Private Nummer...");
		privatePhoneNumbersTable.setWidget(0, 1, privateNrTextBox);
		
		privatePhoneNumbersTable.setWidget(0, 2, new LockButton());
		
		Button deletePrivatePhoneNumberButton = new Button("Nr. löschen");
		privatePhoneNumbersTable.setWidget(0, 3, deletePrivatePhoneNumberButton);
		
		
		//Sechste Zeile
		Label businessPhoneNumbersLabel = new Label("Telefonnummern geschäftl: ");
		contactTable.setWidget(6, 0, businessPhoneNumbersLabel);
		
		FlexTable businessPhoneNumbersTable = new FlexTable();
		contactTable.getFlexCellFormatter().setColSpan(6, 1, 3);
		contactTable.setWidget(6, 1, businessPhoneNumbersTable);
		
		Button addBusinessPhoneNumberButton = new Button(" + ");
		businessPhoneNumbersTable.setWidget(0, 0, addBusinessPhoneNumberButton);
		
		TextBox businessNrTextBox = new TextBox();
		businessNrTextBox.getElement().setPropertyString("placeholder", "Geschäftl. Nummer...");
		businessPhoneNumbersTable.setWidget(0, 1, businessNrTextBox);
		
		businessPhoneNumbersTable.setWidget(0, 2, new LockButton());
			
		Button deleteBusinessPhoneNumberButton = new Button("Nr. löschen");
		businessPhoneNumbersTable.setWidget(0, 3, deleteBusinessPhoneNumberButton);
		
//		//nur zum innere Rahmenlinien anzeigen lassen, zu Debug-Zwecken
//		for (int i= 0; i<phoneNumbersTable.getRowCount(); i++) {
//			for (int a=0; a<phoneNumbersTable.getCellCount(i); a++) {
//				phoneNumbersTable.getCellFormatter().addStyleName(i, a, "cellBordersGreen");
//			}
//		}
		
		
		//Siebte Zeile: eMail-Adresse
		Label eMailsLabel = new Label("e-Mail-Adressen: ");
		contactTable.setWidget(7, 0, eMailsLabel);
		
		FlexTable eMailsTable = new FlexTable();
		contactTable.getFlexCellFormatter().setColSpan(7, 1, 3);
		contactTable.setWidget(7, 1, eMailsTable);

		Button addEmailButton = new Button(" + ");
		eMailsTable.setWidget(0, 0, addEmailButton);
		
		TextBox emailTextBox = new TextBox();
		emailTextBox.getElement().setPropertyString("placeholder", "e-Mail-Adresse...");
		eMailsTable.setWidget(0, 1, emailTextBox);
		eMailsTable.setWidget(0, 2, new LockButton());
		
		Button deleteEmailButton = new Button ("e-Mail löschen");
		eMailsTable.setWidget(0, 3, deleteEmailButton);
		
		
		
		
		//Achte Zeile: Arbeitsstelle
		contactTable.getFlexCellFormatter().setColSpan(8, 1, 3);
		Label jobLabel = new Label("Arbeitsstelle: ");
		contactTable.setWidget(8, 0, jobLabel);
		
		FlexTable jobTable = new FlexTable();
		contactTable.setWidget(8, 1, jobTable);
		
		Button addJobButton = new Button(" + ");
		jobTable.setWidget(0, 0, addJobButton);
		
		TextBox jobTextBox = new TextBox();
		jobTextBox.getElement().setPropertyString("placeholder", "Arbeitsstelle...");
		jobTable.setWidget(0,1, jobTextBox);
		jobTable.setWidget(0, 2, new LockButton());
		
		Button deleteJobButton = new Button("Arbeitsstelle löschen");
		jobTable.setWidget(0, 3, deleteJobButton);
		
		//Neunte Zeile: Homepage
		contactTable.getFlexCellFormatter().setColSpan(9, 1, 3);
		Label homepageLabel = new Label("Homepage: ");
		contactTable.setWidget(9, 0, homepageLabel);
		
		FlexTable homepageTable = new FlexTable();
		contactTable.setWidget(9, 1, homepageTable);
		
		Button addHomepageButton = new Button(" + ");
		homepageTable.setWidget(0, 0, addHomepageButton);
		
		TextBox homepageTextBox = new TextBox();
		homepageTextBox.getElement().setPropertyString("placeholder", "Homepage...");
		homepageTable.setWidget(0, 1, homepageTextBox);
		homepageTable.setWidget(0, 2, new LockButton());
		
		Button deleteHomepageButton = new Button("Homepage löschen");
		homepageTable.setWidget(0, 3, deleteHomepageButton);
		
		//Zehnte Zeile: Buttons
		contactTable.getFlexCellFormatter().setColSpan(10, 0, 4);
		contactTable.getFlexCellFormatter().setHeight(10, 0, "30px");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		Button addContactButton = new Button("Neuen Kontakt anlegen");
		Button shareContactButton = new Button("Kontakt teilen");
		Button deleteContactButton = new Button("Kontakt löschen");
		Button saveChangesButton = new Button("Änderungen speichern");
	
		contactTable.setWidget(10, 0, buttonPanel);
		buttonPanel.add(addContactButton);
		buttonPanel.add(shareContactButton);
		buttonPanel.add(deleteContactButton);
		buttonPanel.add(saveChangesButton);
		
	
		
				
		
	//	Innere Rahmenlinien markieren zu Debug-Zwecken
		for (int i= 0; i<contactTable.getRowCount(); i++) {
			for (int a=0; a<contactTable.getCellCount(i); a++) {
				contactTable.getCellFormatter().addStyleName(i, a, "cellBordersBlack");
			}
		}
		
		
		//ClickHandler
		addPrivatePhoneNumberButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				newPhoneNumberPopUp();
			}	
		});
		
		addBusinessPhoneNumberButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				newPhoneNumberPopUp();
			}	
		});
		
		addEmailButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				newEmailPopUp();
			}	
		});
		
		shareContactButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				shareContactPopUp();
			}
		});
		
		deleteContactButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				deletePopUp();
			}
		});
		
		saveChangesButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				//tdb: wie Änderungen übernehmen, wenn mehreres geändert wurde?! + zuerst check, ob es der Eigentümer ist
			}
		});
	
		addContactButton.addClickHandler(new newContactClickHandler());
		
		Window.alert("1. Ende der Methode onLoad von contactForm");	
		
	} //ende der Methode onLoad();
	
	
	
	
	
	/**
	 * New phone number pop up.
	 */
	public void newPhoneNumberPopUp() {
		//check, ob es der Eigentümer ist --> if not: Fehlermeldung-Popup
		Window.alert("Here you can add a new phone Number");
		
		
	}
	
	/**
	 * New email pop up.
	 */
	public void newEmailPopUp() {
		//check, ob es der Eigentümer ist --> if not: Fehlermeldung-Popup
		Window.alert("Here you can add a new e-Mail adress");
		
	}

	/**
	 * Share contact pop up.
	 */
	public void shareContactPopUp() {
		Window.alert("Here you can select another user to share the contact with");
		
	}
	
	/**
	 * Delete pop up.
	 */
	public void deletePopUp() {
		//check, ob es der Eigentümer ist --> if not: nur Teilhaberschaft löschen
		Window.alert("Here is going to appear a Window where you can select which values you want to delelte");
		
	}
	
	
	/**
	 * Adds the value pop up.
	 */
	public void addValuePopUp() {
		//abfrage, welche pid
		//button "Hinzufügen" hat ClickHandler, welcher createValue aufruft
	}
	
	
	
	/**
	 * The Class newContactClickHandler.
	 */
	private class newContactClickHandler implements ClickHandler{

		/* (non-Javadoc)
		 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
		 */
		@Override
		public void onClick(ClickEvent event) {
			clearContactForm();
			ContactList myContactsContactList = clctvm.getMyContactsContactList();

		//TODO: if abfrage für m und w beim Kontakt anlegen
			
			editorAdministration.createContact(firstnameTextBox.getText(),
					lastnameTextBox.getText(), sexListBox.getValue(0).toString(),
					new GetContactCallback(myContactsContactList));
				
		}		
	}
	
	
	/**
	 * The Class GetContactCallback.
	 */
	private class GetContactCallback implements AsyncCallback<Contact>{
		
		/** The cl. */
		ContactList cl = null;
		
		/**
		 * Instantiates a new gets the contact callback.
		 *
		 * @param cl the cl
		 */
		public GetContactCallback(ContactList cl) {
			this.cl = cl;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
		 */
		public void onFailure(Throwable caught) {
			
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
		 */
		public void onSuccess(Contact result) {
			clctvm.addContactOfContactList(cl, result);
				
			
		}
	}
	

	/**
	 * Clear contact form.
	 */
	public void clearContactForm() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	/**
	 * Sets the selected.
	 *
	 * @param c the new selected
	 */
	//todo: Methode "setSelected"
	public void setSelected(Contact c) {
		if (c != null){
			contactToDisplay = c;
			//test-zwecke
			this.firstnameTextBox.setText(c.getFirstname());
			this.lastnameTextBox.setText(c.getLastname());
		//	this.birthdayLabel.setText(c.get); 				Geburtsdatum fehlt!!
			this.sexListBox.setItemSelected(1, true);
		}
	}
	
	/**
	 * Sets the clctvm.
	 *
	 * @param clctvm the new clctvm
	 */
	public void setClctvm(ContactListContactTreeViewModel clctvm) {
		this.clctvm= clctvm;
	}
}

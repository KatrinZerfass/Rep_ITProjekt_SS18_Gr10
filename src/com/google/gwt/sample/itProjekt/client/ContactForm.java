package com.google.gwt.sample.itProjekt.client;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.sample.itProjekt.client.ContactForm.ValueDisplay;
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
import com.google.gwt.user.client.ui.ValueBoxBase;
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
	
	ValueDisplay birthdayDisplay;
	Vector<ValueDisplay> privatePhoneNumberDisplays;
	Vector<ValueDisplay> businessPhoneNumberDisplays;
	Vector<ValueDisplay> eMailDisplays;
	Vector<ValueDisplay> jobDisplays;
	Vector<ValueDisplay> homepageDisplays;
	
	
	/** Allumfassende Tabelle zur Darstellung von Kontakten */
	FlexTable contactTable = new FlexTable();
	
	/** The firstname text box. */
	ValueTextBox firstnameTextBox = new ValueTextBox();
	
	/** The lastname text box. */
	ValueTextBox lastnameTextBox = new ValueTextBox();
	
	/** The birthday text box. */
	ValueTextBox birthdayTextBox ;
	
	/** The sex list box. */
	ListBox sexListBox = new ListBox();
	
	/** Tabelle, in der Anschrift angezeigt wird */
	FlexTable addressTable = new FlexTable();
	
	/** Tabelle, in der die privaten Telefonnummern eines Kontakt angezeigt werden */
	FlexTable privatePhoneNumbersTable = new FlexTable();
	
	/**Tabelle, in der die geschäftlichen Telefonnummern eines Kontakt angezeigt werden */
	FlexTable businessPhoneNumbersTable = new FlexTable();
	
	/**Tabelle, in der die e-Mail-Adressen eines Kontakts angezeigt werden */
	FlexTable eMailsTable = new FlexTable();
	
	/**Tabelle, in der die Homepages eines Kontakts angezeigt werden */
	FlexTable homepagesTable = new FlexTable();
	
	/**Tabelle, in der die Arbeitsstellen eines Kontakts angezeigt werden */
	FlexTable jobsTable = new FlexTable();
	

	
	
	ValueTextBox streetTextBox = new ValueTextBox();

	ValueTextBox houseNrTextBox = new ValueTextBox();
	
	ValueTextBox plzTextBox = new ValueTextBox();
	
	ValueTextBox cityTextBox = new ValueTextBox();
	
	
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
			this.setText(" neu ");
			this.setStyleName("addButton");
			this.propertyId= pid;
		
					
			this.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					addValuePopUp(propertyId);	
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
		private Value value = null;
		
		/**
		 * Konstruktor von ValueTextBox. Fügt die TextBox dem Vector aller TextBoxen hinzu.
		 * Und fügt der TextBox den valueChangeHandler hinzu,  welcher den Zustand von <code>isChanged</code> ändert.
		 */
		public ValueTextBox() {
	
			
			// brauchen wir nicht mehr, passiert jetzt in der setSelected
			//allValueTextBoxes.add(this);
			
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
		 * Setter von <code>value</code>. Setzt auch gleichzeitig den Inhalt als Text der TextBox.
		 * 
		 * @param Value die anzuzeigende Ausprägung
		 */
		public void setValue(Value v) {
			this.value = v;
			this.setText(value.getContent());
			allValueTextBoxes.add(this);
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
			lockButton = new LockButton();
			deleteValueButton = new DeleteValueButton(" x ");
			this.add(valueTextBox);
			this.add(lockButton);
			this.add(deleteValueButton);
				
		}
		
		/**
		 * Setter von <code>value</code>. Setzt die Ausprägung in alle Elemente des Displays, welche auf diese referenzieren.
		 * 
		 * @param Value die anzuzeigende Ausprägung
		 */
		public void setValue(Value v) {
			this.value = v;
			valueTextBox.setValue(value);
			lockButton.setValue(value);
			deleteValueButton.setValue(value);
			
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
		 * Dritte Zeile: Geschlecht und Geburtsdatum
		 */
		Label sexLabel = new Label("Geschlecht: ");
		sexListBox.addItem("männlich");
		sexListBox.addItem("weiblich");
		sexListBox.addItem("Sonstiges");
		
		contactTable.setWidget(3, 0, sexLabel);
		contactTable.setWidget(3, 1, sexListBox);
		
		
		Label birthdateLabel = new Label("Geburtsdatum: ");
		contactTable.setWidget(3, 2, birthdateLabel);
		
		contactTable.setWidget(3, 3, new ValueDisplay(new ValueTextBox()));
		
		
		
		/*
		 * Vierte Zeile: Anschrift
		 */
		Label addressLabel = new Label("Anschrift: ");
		contactTable.setWidget(4, 0, addressLabel);
		
		contactTable.getFlexCellFormatter().setColSpan(4, 1, 3);
		contactTable.setWidget(4, 1, addressTable);
		
		addressTable.setWidget(0, 0, streetTextBox);
		addressTable.setWidget(0, 1, houseNrTextBox);
		addressTable.setWidget(1, 0, plzTextBox);
		addressTable.setWidget(1, 1, cityTextBox);
			
		addressTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
		addressTable.setWidget(0, 2, new LockButton());
		addressTable.getFlexCellFormatter().setRowSpan(0, 3, 2);
		addressTable.setWidget(0, 3, new DeleteValueButton(" x "));
		
//		//nur zum innere Rahmenlinien anzeigen lassen, zu Debug-Zwecken
//		for (int i= 0; i<addressTable.getRowCount(); i++) {
//			for (int a=0; a<addressTable.getCellCount(i); a++) {
//				addressTable.getCellFormatter().addStyleName(i, a, "cellBordersGreen");
//			}
//		}
		

		/*
		 * Fünfte Zeile: Telefonnummern privat (PID 2)
		 */
		VerticalPanel privatePhoneNumbersPanel = new VerticalPanel();
		contactTable.setWidget(5, 0, privatePhoneNumbersPanel);
		
		Label privatePhoneNumbersLabel = new Label("Telefonnummern privat: ");
		privatePhoneNumbersPanel.add(privatePhoneNumbersLabel);
			
		AddValueButton addPrivatePhoneNumberButton = new AddValueButton(2);
		privatePhoneNumbersPanel.add(addPrivatePhoneNumberButton);
		
		
		contactTable.getFlexCellFormatter().setColSpan(5, 1, 3);
		contactTable.setWidget(5, 1, privatePhoneNumbersTable);
		
		privatePhoneNumbersTable.setWidget(0, 0, new ValueDisplay(new ValueTextBox()));
	
		
		
		/*
		 * Sechste Zeile: Telefonnummer privat (PID 1)
		 */
		
		VerticalPanel businessPhoneNumbersPanel = new VerticalPanel();
		contactTable.setWidget(6, 0,  businessPhoneNumbersPanel);
		
		Label businessPhoneNumbersLabel = new Label("Telefonnummern geschäftl: ");
		businessPhoneNumbersPanel.add(businessPhoneNumbersLabel);
		
		AddValueButton addBusinessPhoneNumberButton = new AddValueButton(1);
		businessPhoneNumbersPanel.add(addBusinessPhoneNumberButton);
			
		
		contactTable.getFlexCellFormatter().setColSpan(6, 1, 3);
		contactTable.setWidget(6, 1, businessPhoneNumbersTable);
		
		businessPhoneNumbersTable.setWidget(0, 0, new ValueDisplay(new ValueTextBox()));
		
		
			
		
		
//		//nur zum innere Rahmenlinien anzeigen lassen, zu Debug-Zwecken
//		for (int i= 0; i<phoneNumbersTable.getRowCount(); i++) {
//			for (int a=0; a<phoneNumbersTable.getCellCount(i); a++) {
//				phoneNumbersTable.getCellFormatter().addStyleName(i, a, "cellBordersGreen");
//			}
//		}
		
		

		/*
		 * Siebte Zeile: Telefonnummer privat (PID 1)
		 */
		
		VerticalPanel eMailsPanel = new VerticalPanel();
		contactTable.setWidget(7, 0,  eMailsPanel);
		
		Label eMailsLabel = new Label("e-Mail-Adressen: ");
		eMailsPanel.add(eMailsLabel);
		
		AddValueButton addEmailButton = new AddValueButton(3);
		eMailsPanel.add(addEmailButton);
			
		
		contactTable.getFlexCellFormatter().setColSpan(7, 1, 3);
		contactTable.setWidget(7, 1, eMailsTable);
		
		eMailsTable.setWidget(0, 0, new ValueDisplay(new ValueTextBox()));
		
		
		
		/*
		 * Achte Zeile: Homepages (PID 10)
		 */
		VerticalPanel homepagesPanel = new VerticalPanel();
		contactTable.setWidget(8, 0,  homepagesPanel);
		
		Label homepagesLabel = new Label("Homepages: ");
		homepagesPanel.add(homepagesLabel);
		
		AddValueButton addHomepageButton = new AddValueButton(10);
		homepagesPanel.add(addHomepageButton);
			
		
		contactTable.getFlexCellFormatter().setColSpan(8, 1, 3);
		contactTable.setWidget(8, 1, homepagesTable);
		
		homepagesTable.setWidget(0, 0, new ValueDisplay(new ValueTextBox()));
		
		
		
		/*
		 * Neunte Zeile: Arbeitsstelle (PID 5)
		 */
		VerticalPanel jobsPanel = new VerticalPanel();
		contactTable.setWidget(9, 0,  jobsPanel);
		
		Label jobsLabel = new Label("Arbeitsstellen: ");
		jobsPanel.add(jobsLabel);
		
		AddValueButton addJobButton = new AddValueButton(5);
		jobsPanel.add(addJobButton);
			
		
		contactTable.getFlexCellFormatter().setColSpan(9, 1, 3);
		contactTable.setWidget(9, 1, jobsTable);
		
		jobsTable.setWidget(0, 0, new ValueDisplay(new ValueTextBox()));
		
		
		
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
		
		
//		//ClickHandler für die AddValueButtons
//		addPrivatePhoneNumberButton.addClickHandler(new ClickHandler(){
//			public void onClick(ClickEvent event) {
//				
//			}	
//		});
//		
//		addBusinessPhoneNumberButton.addClickHandler(new ClickHandler(){
//			public void onClick(ClickEvent event) {
//				
//			}	
//		});
//		
//		addEmailButton.addClickHandler(new ClickHandler(){
//			public void onClick(ClickEvent event) {
//				
//			}	
//		});
//		
//		addHomepageButton.addClickHandler(new ClickHandler(){
//			public void onClick(ClickEvent event) {
//				
//			}	
//		});
//		
//		addJobButton.addClickHandler(new ClickHandler(){
//			public void onClick(ClickEvent event) {
//				
//			}	
//		});
		
		//ClickHandler für die Funktionsbuttons --> jeweils eigene innere Klasse, siehe unten
		addContactButton.addClickHandler(new newContactClickHandler());
		
		shareContactButton.addClickHandler(new shareContactClickHandler());
		
		deleteContactButton.addClickHandler(new deleteContactClickHandler());
		
		saveChangesButton.addClickHandler(new saveChangesClickHandler());
				//tdb: wie Änderungen übernehmen, wenn mehreres geändert wurde?! 
		
	
		
		
		Window.alert("1. Ende der Methode onLoad() von contactForm");	
	} 
	
	
	
	
	
	
	/**
	 * Die innere Klasse newContactClickHandler.
	 */
	private class newContactClickHandler implements ClickHandler{

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
		
		
		public void onFailure(Throwable caught) {
			
		}
		
		
		public void onSuccess(Contact result) {
			clctvm.addContactOfContactList(cl, result);
				
			
		}
	}
	
	/**
	 * Die innere Klasse shareContactClickHandler.
	 */
	private class shareContactClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
		
		}
	}
	
	/**
	 * Die innere Klasse deleteContactClickHandler.
	 */
	private class deleteContactClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
		
		}
	}
	
	/**
	 * Die innere Klasse saveChangesClickHandler.
	 */
	private class saveChangesClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
		
			for(ValueTextBox vtb : allValueTextBoxes) {
				if (vtb.getIsChanged()) {
					editorAdministration.editValue(contactToDisplay, vtb.getTextBoXValue().getPropertyid(), vtb.getTextBoXValue(), vtb.getTextBoXValue().getContent(), 
							vtb.getTextBoXValue().getIsShared(), new AsyncCallback<Value>() {
						
						public void onFailure(Throwable arg0) {
							// TODO Auto-generated method stub
							
						}
					
						public void onSuccess(Value arg0) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
			
		}
	}
	
	
	public void addValuePopUp(int pid) {
		//popup, wo man neuen wert einträgt
		//der wert aus der TextBox wird ausgelesen und mit ihm
		// a) ein neues ValueDisplay erstellt und
		// b) der Value in die Datenbank abgespeichert
	}
	
	/**
	 * Die Methode wird aufgerufen, wenn der addContactButton gedrückt wird. Die Felder leeren sich und ein neuer Kontakt
	 * kann eingetragen werden.
	 */
	public void clearContactForm() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	/**
	 * Zeigt den selektierten Kontakt an.
	 *
	 * @param Contact der selektierte Kontakt
	 */
	public void setSelected(Contact c) {
		
		allValueTextBoxes = null;
		
		if (c != null){
			contactToDisplay = c;
			
			/*
			 * Alle Ausprägungen des contactToDisplay werden ausgelesen und in einem Vector<Values> gespeichert.
			 */
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
			
			/*
			 * Vor und Nachname des Kontakts werden gesetzt.
			 */
			firstnameTextBox.setText(contactToDisplay.getFirstname());
			allValueTextBoxes.add(firstnameTextBox);
			lastnameTextBox.setText(contactToDisplay.getLastname());
			allValueTextBoxes.add(lastnameTextBox);
			
			/*
			 * Das Geschlecht des Kontaktes wird abgefragt und gesetzt.
			 */
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
			
			
			
			/*
			 * Der Vector allValuesOfContact, welcher alle Ausprägungen des anzuzeigenden Kontaktes enthält, wird durchiteriert
			 * und jede Ausprägung wird im dazugehörigen Display angezeigt.
			 */ 
			for(int i=0; i<allValuesOfContact.size(); i++) {
				int pid = allValuesOfContact.get(i).getPropertyid();
				switch (pid) {
					case 1: pid= 1; // Tel.Nr. geschäftlich
						if(((ValueDisplay) businessPhoneNumbersTable.getWidget(0,0)).getValue() == null){
							((ValueDisplay) businessPhoneNumbersTable.getWidget(0,0)).setValue(allValuesOfContact.get(i));
						}else {
							/*
							 * Gibt es mehrere Ausprägungen zu geschäftlichen Telefonnummern, wird eine neue Zeile in der FlexTable
							 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefügt.
							 */
							businessPhoneNumbersTable.setWidget(businessPhoneNumbersTable.getRowCount(), 0, 
																					new ValueDisplay(new ValueTextBox()));
							((ValueDisplay) businessPhoneNumbersTable.getWidget(businessPhoneNumbersTable.getRowCount(), 0))
																					.setValue(allValuesOfContact.get(i));
						}
							
					
					case 2: pid= 2; // Tel.Nr. privat
							if(((ValueDisplay) privatePhoneNumbersTable.getWidget(0,0)).getValue() == null){
								((ValueDisplay) privatePhoneNumbersTable.getWidget(0,0)).setValue(allValuesOfContact.get(i));
							}else {
								/*
								 * Gibt es mehrere Ausprägungen zu privaten Telefonnummern, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefügt.
								 */
								privatePhoneNumbersTable.setWidget(privatePhoneNumbersTable.getRowCount(), 0, 
																						new ValueDisplay(new ValueTextBox()));
								((ValueDisplay) privatePhoneNumbersTable.getWidget(privatePhoneNumbersTable.getRowCount(), 0))
																						.setValue(allValuesOfContact.get(i));
							}
							
					
					case 3: pid= 3; // e-Mail
							if(((ValueDisplay) eMailsTable.getWidget(0,0)).getValue() == null){
								((ValueDisplay) eMailsTable.getWidget(0,0)).setValue(allValuesOfContact.get(i));
							}else {
								/*
								 * Gibt es mehrere Ausprägungen zu e-Mail-Adressen, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefügt.
								 */
								eMailsTable.setWidget(eMailsTable.getRowCount(), 0, new ValueDisplay(new ValueTextBox()));
								
								((ValueDisplay) eMailsTable.getWidget(eMailsTable.getRowCount(), 0))
																						.setValue(allValuesOfContact.get(i));
							}
					
					case 4: pid= 4; // Geburtstag
							((ValueDisplay) contactTable.getWidget(3,3)).setValue(allValuesOfContact.get(i));
							
							
					case 5: pid= 5; // Arbeitsplatz
							if(((ValueDisplay) jobsTable.getWidget(0,0)).getValue() == null){
								((ValueDisplay) jobsTable.getWidget(0,0)).setValue(allValuesOfContact.get(i));
							}else {
								/*
								 * Gibt es mehrere Ausprägungen zu Arbeitsstellen, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefügt.
								 */
								jobsTable.setWidget(jobsTable.getRowCount(), 0, new ValueDisplay(new ValueTextBox()));
								
								((ValueDisplay) jobsTable.getWidget(jobsTable.getRowCount(), 0)).setValue(allValuesOfContact.get(i));
							}
							
					
					case 6: pid= 6; // Straße
							streetTextBox.setValue(allValuesOfContact.get(i));
							((LockButton) addressTable.getWidget(0, 2)).setValue(allValuesOfContact.get(i));
							((DeleteValueButton) addressTable.getWidget(0,3)).setValue(allValuesOfContact.get(i));
			
					case 7: pid= 7; // Hausnummer
							houseNrTextBox.setValue(allValuesOfContact.get(i));
							
					
					case 8: pid= 8; // PLZ
							plzTextBox.setValue(allValuesOfContact.get(i));
					
					
					case 9: pid= 9; // Wohnort
							cityTextBox.setValue(allValuesOfContact.get(i));
							
					
					case 10: pid= 10; // Homepage
							 if(((ValueDisplay) homepagesTable.getWidget(0,0)).getValue() == null){
								((ValueDisplay) homepagesTable.getWidget(0,0)).setValue(allValuesOfContact.get(i));
							 }else {
								/*
								 * Gibt es mehrere Ausprägungen zu e-Mail-Adressen, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefügt.
								 */
								homepagesTable.setWidget(homepagesTable.getRowCount(), 0, new ValueDisplay(new ValueTextBox()));
								
								((ValueDisplay) homepagesTable.getWidget(homepagesTable.getRowCount(), 0)).setValue(allValuesOfContact.get(i));
							 }
						
				}
			}
				
		
	
		}else {
			firstnameTextBox.getElement().setPropertyString("placeholder", "Vorname...");
			lastnameTextBox.getElement().setPropertyString("placeholder", "Nachname...");
			contactTable.getWidget(3,3).getElement().setPropertyString("placeholder", "Geburtsdatum...");
			streetTextBox.getElement().setPropertyString("placeholder", "Straße...");
			houseNrTextBox.getElement().setPropertyString("placeholder", "Hausnummer...");
			plzTextBox.getElement().setPropertyString("placeholder", "PLZ...");
			cityTextBox.getElement().setPropertyString("placeholder", "Wohnort...");
		//	privatePhoneNumbersTable.getElement().setPropertyString("placeholder", "Private Nummer...");
		// sind alles valueTextBoxen, vllt irgendwie vereinheitlichen?! EVtl oben in Konstruktor von
			//ValueTextBox: pid abfragen und dann placeholder setzen?!
			
			//businessNrTextBox.getElement().setPropertyString("placeholder", "Geschäftl. Nummer...");
			//mailTextBox.getElement().setPropertyString("placeholder", "e-Mail-Adresse...");
			//homepageTextBox.getElement().setPropertyString("placeholder", "Homepage...");
			//jobTextBox.getElement().setPropertyString("placeholder", "Arbeitsstelle...");
		}
	}
	
	/**
	 * Setzt das referenzierte TreeViewModel
	 *
	 * @param ContactListContactTreeViewModel das referenzierte TreeViewModel
	 */
	public void setClctvm(ContactListContactTreeViewModel clctvm) {
		this.clctvm= clctvm;
		
	}
}

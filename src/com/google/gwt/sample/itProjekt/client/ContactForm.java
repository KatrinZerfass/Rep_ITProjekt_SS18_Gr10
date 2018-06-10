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

public class ContactForm extends VerticalPanel {
	
	EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	Contact contactToDisplay = null;
	Vector<Value> allValuesOfContact = null;
	ContactListContactTreeViewModel clctvm = null;
	
	Vector<ValueTextBox> valueTextBoxes = new Vector<ValueTextBox>();
	
	TextBox firstnameTextBox = new TextBox();
	TextBox lastnameTextBox = new TextBox();
	ValueTextBox birthdayTextBox ;
	ListBox sexListBox = new ListBox();
	TextBox streetTextBox = new TextBox();
	TextBox houseNrTextBox = new TextBox();
	TextBox plzTextBox = new TextBox();
	TextBox cityTextBox = new TextBox();
	
	
	//innere Klasse für LockButtons
	public class LockButton extends PushButton{
			
		private Value value;
		private boolean isLocked = false;
		Image lockUnlocked = new Image("lock_unlocked.png");
		Image lockLocked = new Image ("lock_locked.png");
		
		public LockButton(Value v) {
			this.value= v;
			
			lockUnlocked.setPixelSize(20, 20);
			lockLocked.setPixelSize(20, 20);
			
			this.getUpFace().setImage(lockUnlocked);
			
			this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(isLocked ==false) { //Schloss ist geöffnet
				setValueToUnshared();	//Wert wird nicht geteilt, Schloss wird geschlossen
				}else setValueToShared();
			}
		});
		}
		
		public void setIsLocked(boolean b) {
		}
		
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
			
	
	//innere Klasse für DeleteValue-Button
	public class DeleteValueButton extends Button{
		private Value value;
		
		public DeleteValueButton(String html, Value v) {
			super(html);
			this.value = v;
			
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
		
		public void disableButtons() {
			this.setEnabled(false);
		}
	}
	
	
	//innere Klasse für AddValue-Button
	public class AddValueButton extends Button{
		private int propertyId;
		
		public AddValueButton(int pid) {
			this.propertyId= pid;
		
			
			this.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					addValuePopUp();	
				}
			});
		}
		
		public int getPropertyId() {
			return this.propertyId;
		}
	}
	
	
	//innere Klasse für Kontakt-Textboxen (Vor-/Nachname)
	public class ValueTextBox extends TextBox{
		private boolean isChanged=false;
		private Value value;
		
		public ValueTextBox(Value v) {
			this.value = v;
			
			this.addValueChangeHandler(new ValueChangeHandler<String>(){

				public void onValueChange(ValueChangeEvent<String> event) {
					isChanged = true;					
				}
				
			});
		}
		
		public boolean getIsChanged() {
			return this.isChanged;
		}
		
		public Value getTextBoxValue() {
			return this.value;
		}
	}
	
	
	//innere Klasse für das Anzeigen von Eigenschaftsausprägungen
	public class ValueDisplay extends HorizontalPanel{
		private Value value;
		private ValueTextBox valueTextBox;
		private LockButton lockButton;
		private DeleteValueButton deleteValueButton;
		
		public ValueDisplay(Value v) {
			valueTextBox = new ValueTextBox(v);
			valueTextBox.setText(v.getContent());
			
			lockButton = new LockButton(value);
			deleteValueButton = new DeleteValueButton(" x " , value );
			
			this.add(valueTextBox);
			this.add(lockButton);
			this.add(deleteValueButton);
			
		}
		
		
	}
	

	
	
	

	public void onLoad() {
		
		super.onLoad();
		
		//Abfrage über den aktuellen Nutzer --> wenn nicht Eigentümer, werden Buttons ausgegraut
		
		//wenn das Kontaktformular in Folge des Anklickens eines Kontakts aufgerufen wird, so werden auch alle Values geladen
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
		
	
		//allumfassende Tabelle für die Darstellung von Kontakten
		FlexTable contactTable = new FlexTable();
		this.add(contactTable);
		
		//Nullte Zeile
		contactTable.getFlexCellFormatter().setColSpan(0, 0, 4);
		contactTable.setText(0, 0, "  ");
		
		//Erste Zeile
		contactTable.getFlexCellFormatter().setColSpan(1, 0, 4);
		Label contactInfoLabel = new Label("Kontaktinformationen");
		contactTable.setWidget(1, 0, contactInfoLabel);
		
		//Zweite Zeile
		Label firstnameLabel = new Label("Vorname: ");
		Label lastnameLabel = new Label("Nachname: ");
		
						
		firstnameTextBox.getElement().setPropertyString("placeholder", "Vorname...");
		lastnameTextBox.getElement().setPropertyString("placeholder", "Nachname...");
		contactTable.setWidget(2, 0, firstnameLabel);
		contactTable.setWidget(2, 1, firstnameTextBox);
		contactTable.setWidget(2, 2, lastnameLabel);
		contactTable.setWidget(2, 3, lastnameTextBox);
		
		//Dritte Zeile
		Label birthdateLabel = new Label("Geburtsdatum: ");
		
		//wenn ein Kontakt angezeigt werden soll, ist der Vector allValuesOfContact gefüllt und wird jetzt Schritt für Schritt ausgelesen
		if (allValuesOfContact !=null){
			for(int i=0; i<allValuesOfContact.size(); i++) {
				if(allValuesOfContact.get(i).getPropertyid()== 000000004) {
					birthdayTextBox = new ValueTextBox(allValuesOfContact.get(i)); 
				}
			}
		}else { //wird das ContactForm nur per default angezeigt, gibt es placeholder
			birthdayTextBox.getElement().setPropertyString("placeholder", "Geburtsdatum...");
		//	birthdayTextBox.setReadOnly(true);
		}
		
		
		Label sexLabel = new Label("Geschlecht: ");
		
		sexListBox.addItem("männlich");
		sexListBox.addItem("weiblich");
		
		contactTable.setWidget(3, 0, sexLabel);
		contactTable.setWidget(3, 1, sexListBox);
		contactTable.setWidget(3, 2, birthdateLabel);
		
		HorizontalPanel birthdayPanel = new HorizontalPanel();
		birthdayPanel.add(birthdayTextBox);
		birthdayPanel.add(new LockButton());
		contactTable.setWidget(3, 3, birthdayPanel);
		
		
		
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
	
	
	
	
	
	public void newPhoneNumberPopUp() {
		//check, ob es der Eigentümer ist --> if not: Fehlermeldung-Popup
		Window.alert("Here you can add a new phone Number");
		
		
	}
	
	public void newEmailPopUp() {
		//check, ob es der Eigentümer ist --> if not: Fehlermeldung-Popup
		Window.alert("Here you can add a new e-Mail adress");
		
	}

	public void shareContactPopUp() {
		Window.alert("Here you can select another user to share the contact with");
		
	}
	
	public void deletePopUp() {
		//check, ob es der Eigentümer ist --> if not: nur Teilhaberschaft löschen
		Window.alert("Here is going to appear a Window where you can select which values you want to delelte");
		
	}
	
	
	public void addValuePopUp() {
		//abfrage, welche pid
		//button "Hinzufügen" hat ClickHandler, welcher createValue aufruft
	}
	
	
	
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
	
	
	private class GetContactCallback implements AsyncCallback<Contact>{
		ContactList cl = null;
		
		public GetContactCallback(ContactList cl) {
			this.cl = cl;
		}
		public void onFailure(Throwable caught) {
			
		}
		
		public void onSuccess(Contact result) {
			clctvm.addContactOfContactList(cl, result);
				
			
		}
	}
	

	public void clearContactForm() {
		// TODO Auto-generated method stub
		
	}
	
	
	
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
	
	public void setClctvm(ContactListContactTreeViewModel clctvm) {
		this.clctvm= clctvm;
	}
}

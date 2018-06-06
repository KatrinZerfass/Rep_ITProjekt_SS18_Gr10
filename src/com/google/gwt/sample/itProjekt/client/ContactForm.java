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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactForm extends VerticalPanel {
	
	EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	Contact contactToDisplay = null;
	ContactListContactTreeViewModel clctvm = null;
	
	TextBox firstnameTextBox = new TextBox();
	TextBox lastnameTextBox = new TextBox();
	Label birthdayLabel = new Label();
	ListBox sexListBox = new ListBox();
	
	
	//innere Klasse f�r LockButtons
	public class LockButton extends PushButton{
		
		public LockButton() {
			lockUnlocked.setPixelSize(20, 20);
			lockLocked.setPixelSize(20, 20);
			
			this.getUpFace().setImage(lockUnlocked);
			
			this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!isLocked) {
				changeSchlossToLocked();
				}else changeSchlossToUnlocked();
			}
		});
		}
		private boolean isLocked = false;
		Image lockUnlocked = new Image("schlossUnlocked.png");
		Image lockLocked = new Image ("schlossLocked.png");
		
		
		
		public void setIsLocked(boolean b) {
		}
		
		public void changeSchlossToLocked() {
			this.getUpFace().setImage(lockLocked);
			isLocked = true;
			
		}
		public void changeSchlossToUnlocked() {
			this.getUpFace().setImage(lockUnlocked);
			isLocked = false;
		}
			
		
	}
	
	
	
	
//	public ContactForm() {
	public void onLoad() {
		
		super.onLoad();
		
		
//		initWidget(this.contactTable);
		//allumfassende Tabelle f�r die Darstellung von Kontakten
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
				
		contactTable.setWidget(2, 0, firstnameLabel);
		contactTable.setWidget(2, 1, firstnameTextBox);
		contactTable.setWidget(2, 2, lastnameLabel);
		contactTable.setWidget(2, 3, lastnameTextBox);
		
		//Dritte Zeile
		Label birthdateLabel = new Label("Geburtsdatum: ");
		birthdayLabel.setText("01.01.2000");
		Label sexLabel = new Label("Geschlecht: ");
		

		sexListBox.addItem("männlich");
		sexListBox.addItem("weiblich");
		
		contactTable.setWidget(3, 0, birthdateLabel);
		contactTable.setWidget(3, 1, birthdayLabel);
		contactTable.setWidget(3, 2, sexLabel);
		contactTable.setWidget(3, 3, sexListBox);
		
		
		//Vierte Zeile
		Label addressLabel = new Label("Anschrift: ");
		contactTable.setWidget(4, 0, addressLabel);
		
		contactTable.getFlexCellFormatter().setColSpan(4, 1, 3);
		FlexTable addressTable = new FlexTable();
		contactTable.setWidget(4, 1, addressTable);
		
	//	addressTable.getFlexCellFormatter().setColSpan(0,0,2);
	//	addressTable.getFlexCellFormatter().setColSpan(1,1,2);
		
		TextBox streetTextBox = new TextBox();
		addressTable.setWidget(0, 0, streetTextBox);
		TextBox houseNrTextBox = new TextBox();
		addressTable.setWidget(0, 1, houseNrTextBox);
		TextBox plzTextBox = new TextBox();
		addressTable.setWidget(1, 0, plzTextBox);
		TextBox cityTextBox = new TextBox();
		addressTable.setWidget(1, 1, cityTextBox);
		
		addressTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
		
		addressTable.setWidget(0, 2, new LockButton());
		
//		//nur zum innere Rahmenlinien anzeigen lassen, zu Debug-Zwecken
//		for (int i= 0; i<addressTable.getRowCount(); i++) {
//			for (int a=0; a<addressTable.getCellCount(i); a++) {
//				addressTable.getCellFormatter().addStyleName(i, a, "cellBordersGreen");
//			}
//		}
		
		//F�nfte Zeile
		contactTable.getFlexCellFormatter().setColSpan(5, 1, 3);
		Label phoneNumbersLabel = new Label("Telefonnummer: ");
		contactTable.setWidget(5, 0, phoneNumbersLabel);
		
		FlexTable phoneNumbersTable = new FlexTable();
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(0,1,2);
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(1,1,2);
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(2,1,2);
		
		contactTable.setWidget(5, 1, phoneNumbersTable);
		
		
		Label mobileNrLabel = new Label("Mobil: ");
		Label privateNrLabel = new Label("Privat: ");
		Label businessNrLabel = new Label("Geschäftlich: ");
		phoneNumbersTable.setWidget(0, 0, mobileNrLabel);
		phoneNumbersTable.setWidget(1, 0, privateNrLabel);
		phoneNumbersTable.setWidget(2, 0, businessNrLabel);

		TextBox mobileNrTextBox = new TextBox();
		TextBox privateNrTextBox = new TextBox();
		TextBox businessNrTextBox = new TextBox();
		phoneNumbersTable.setWidget(0, 1, mobileNrTextBox);
		phoneNumbersTable.setWidget(1, 1, privateNrTextBox);
		phoneNumbersTable.setWidget(2, 1, businessNrTextBox);
		
		phoneNumbersTable.setWidget(0, 2, new LockButton());
		phoneNumbersTable.setWidget(1, 2, new LockButton());
		phoneNumbersTable.setWidget(2, 2, new LockButton());
		
		Button addPhoneNumberButton = new Button("Hinzufügen");
		phoneNumbersTable.getFlexCellFormatter().setRowSpan(0, 3, 3);
		phoneNumbersTable.setWidget(0, 3, addPhoneNumberButton);


//		//nur zum innere Rahmenlinien anzeigen lassen, zu Debug-Zwecken
//		for (int i= 0; i<phoneNumbersTable.getRowCount(); i++) {
//			for (int a=0; a<phoneNumbersTable.getCellCount(i); a++) {
//				phoneNumbersTable.getCellFormatter().addStyleName(i, a, "cellBordersGreen");
//			}
//		}
		
		
		//Sechste Zeile: eMail-Adresse
		contactTable.getFlexCellFormatter().setColSpan(6, 1, 3);
		Label eMailsLabel = new Label("e-Mail-Adressen: ");
		contactTable.setWidget(6, 0, eMailsLabel);
		
		FlexTable eMailsTable = new FlexTable();
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(0,1,2);
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(1,1,2);
//		phoneNumbersTable.getFlexCellFormatter().setColSpan(2,1,2);
		
		contactTable.setWidget(6, 1, eMailsTable);
		
		Label privateEmailLabel = new Label("Privat: ");
		Label businessEmailLabel = new Label("Geschäftlich: ");
		eMailsTable.setWidget(0, 0, privateEmailLabel);
		eMailsTable.setWidget(1, 0, businessEmailLabel);

		TextBox privateEmailTextBox = new TextBox();
		TextBox businessEmailTextBox = new TextBox();
		eMailsTable.setWidget(0, 1, privateEmailTextBox);
		eMailsTable.setWidget(1, 1, businessEmailTextBox);
		
		eMailsTable.setWidget(0, 2, new LockButton());
		eMailsTable.setWidget(1, 2, new LockButton());
		
		Button addEmailButton = new Button("Hinzufügen");
		eMailsTable.getFlexCellFormatter().setRowSpan(0, 3, 2);
		eMailsTable.setWidget(0, 3, addEmailButton);
		
		
		//Siebte Zeile: Arbeitsstelle
		contactTable.getFlexCellFormatter().setColSpan(7, 1, 3);
		Label jobLabel = new Label("Arbeitsstelle: ");
		contactTable.setWidget(7, 0, jobLabel);
		
		FlexTable jobTable = new FlexTable();
		contactTable.setWidget(7, 1, jobTable);
		TextBox jobTextBox = new TextBox();
		jobTable.setWidget(0,0, jobTextBox);
		jobTable.setWidget(0, 2, new LockButton());
		
		//Achte Zeile: Buttons
		
		Button shareButton = new Button("Teilen");
		Button deleteButton = new Button("Löschen");
		Button saveChangesButton = new Button("Änderungen speichern");
		Button addContactButton = new Button("Neuen Kontakt anlegen");
		
		contactTable.setWidget(8, 1, shareButton);
		contactTable.setWidget(8, 2, deleteButton);
		contactTable.setWidget(8, 3, saveChangesButton);
		
				
		//Innere Rahmenlinien markieren zu Debug-Zwecken
		for (int i= 0; i<contactTable.getRowCount(); i++) {
			for (int a=0; a<contactTable.getCellCount(i); a++) {
				contactTable.getCellFormatter().addStyleName(i, a, "cellBordersBlack");
			}
		}
		
		
		//ClickHandler
		addPhoneNumberButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				newPhoneNumberPopUp();
			}	
		});
		
		addEmailButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				newEmailPopUp();
			}	
		});
		
		shareButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				shareContactPopUp();
			}
		});
		
		deleteButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				deletePopUp();
			}
		});
		
		saveChangesButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				//tdb: wie �nderungen �bernehmen, wenn mehreres ge�ndert wurde?! + zuerst check, ob es der Eigent�mer ist
			}
		});
		
		addContactButton.addClickHandler(new newContactClickHandler());
	
		Window.alert("1. Ende der Methode onLoad von contactForm");
	} //ende der Methode onLoad();
		
	
	
	public void newPhoneNumberPopUp() {
		//check, ob es der Eigent�mer ist --> if not: Fehlermeldung-Popup
		Window.alert("Here you can add a new phone Number");
		
		
	}
	
	public void newEmailPopUp() {
		//check, ob es der Eigent�mer ist --> if not: Fehlermeldung-Popup
		Window.alert("Here you can add a new e-Mail adress");
		
	}

	public void shareContactPopUp() {
		Window.alert("Here you can select another user to share the contact with");
		
	}
	
	public void deletePopUp() {
		//check, ob es der Eigent�mer ist --> if not: nur Teilhaberschaft l�schen
		Window.alert("Here is going to appear a Window where you can select which values you want to delelte");
		
	}
	
	private class newContactClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			clearContactForm();
			ContactList myContactsContactList = clctvm.getMyContactsContactList();

		//TODO: if abfrage für m und w beim Kontakt anlegen
			
			editorAdministration.createContact(new User(), firstnameTextBox.getText(),
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
	
	//todo: "Neuer Kontakt anlegen" - Button unten links
	
	
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

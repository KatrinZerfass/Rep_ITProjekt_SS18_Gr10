package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactForm extends VerticalPanel {
	
	//tdb: contactverwaltung
	Contact contactToDisplay = null;
	//tbd: treeviewmodel
	
	public void onLoad(){
		
		super.onLoad();
		
		FlexTable contactTable = new FlexTable();
		this.add(contactTable);
		
		//Nullte Zeile
		//leer
		
		//Erste Zeile
		contactTable.getFlexCellFormatter().setColSpan(1, 1, 4);
		Label contactInfoLabel = new Label("Kontaktinformationen");
		contactTable.setWidget(1, 0, contactInfoLabel);
		
		//Zweite Zeile
		Label firstnameLabel = new Label("Vorname: ");
		TextBox firstnameTextBox = new TextBox();
		Label lastnameLabel = new Label("Nachname: ");
		TextBox lastnameTextBox = new TextBox();
		
		contactTable.setWidget(2, 0, firstnameLabel);
		contactTable.setWidget(2, 1, firstnameTextBox);
		contactTable.setWidget(2, 2, lastnameLabel);
		contactTable.setWidget(2, 3, lastnameTextBox);
		
		//Dritte Zeile
		Label birthdateLabel = new Label("Geburtsdatum: ");
		Label birthdayLabel = new Label();
		
		contactTable.setWidget(3, 0, birthdateLabel);
		contactTable.setWidget(3, 1, birthdayLabel);
		
		//Vierte Zeile
		contactTable.getFlexCellFormatter().setColSpan(4, 1, 3);
		Label addressLabel = new Label("Anschrift: ");
		contactTable.setWidget(4, 0, addressLabel);
		
		FlexTable addressTable = new FlexTable();
		addressTable.getFlexCellFormatter().setColSpan(0,0,2);
		addressTable.getFlexCellFormatter().setColSpan(1,1,2);
		
		TextBox streetTextBox = new TextBox();
		addressTable.setWidget(0, 0, streetTextBox);
		TextBox houseNrTextBox = new TextBox();
		addressTable.setWidget(0, 2, houseNrTextBox);
		TextBox plzTextBox = new TextBox();
		addressTable.setWidget(1, 0, plzTextBox);
		TextBox cityTextBox = new TextBox();
		addressTable.setWidget(1, 1, cityTextBox);
		
		//Fünfte Zeile
		contactTable.getFlexCellFormatter().setColSpan(5, 1, 3);
		Label phoneNumbersLabel = new Label("Telefonnummer: ");
		contactTable.setWidget(5, 0, phoneNumbersLabel);
		
		FlexTable phoneNumbersTable = new FlexTable();
		phoneNumbersTable.getFlexCellFormatter().setColSpan(0,1,2);
		
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
		phoneNumbersTable.setWidget(1, 1, mobileNrTextBox);
		phoneNumbersTable.setWidget(2, 1, mobileNrTextBox);

		//to do next time: Hinzufügen-buttons hinter jede Eigenschaft!
		// + Löschen-button hinter jede einzelne Ausprägung
		// + Symbole vor jede einzelne Ausprägung für Schlösser
	}
	

}

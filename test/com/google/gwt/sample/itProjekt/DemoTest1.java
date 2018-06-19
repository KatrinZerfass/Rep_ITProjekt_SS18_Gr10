//package com.google.gwt.sample.itProjekt;
//
//import com.google.gwt.sample.itProjekt.client.ContactForm.ValueDisplay;
//import com.google.gwt.sample.itProjekt.client.ContactForm.ValuePanel;
//import com.google.gwt.sample.itProjekt.client.ContactForm.ValueTable;
//import com.google.gwt.sample.itProjekt.client.ContactForm.ValueTextBox;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.VerticalPanel;
//
//public class DemoTest1 {
//	/*
//	 * Vierte Zeile: Anschrift
//	 */
//	Label addressLabel = new Label("Anschrift: ")
//	contactTable.setWidget(4, 0, addressLabel);
//	
//	contactTable.getFlexCellFormatter().setColSpan(4, 1, 3);
//	contactTable.setWidget(4, 1, addressTable);
//	
//	addressTable.setWidget(0, 0, streetTextBox);
//	addressTable.setWidget(0, 1, houseNrTextBox);
//	addressTable.setWidget(1, 0, plzTextBox);
//	addressTable.setWidget(1, 1, cityTextBox);
//		
//	addressTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
//	addressTable.setWidget(0, 2, new ValueDisplay(new ValueTextBox("")));
//	((ValueDisplay) addressTable.getWidget(0, 2)).remove(0);
//
//	
//
//	/*
//	 * Fünfte Zeile: Telefonnummern privat (PID 2)
//	 */
//	contactTable.setWidget(5, 0, new ValuePanel(2, 5, "Telefonnummern privat: "));
//	contactTable.getFlexCellFormatter().setVerticalAlignment(5, 0, ALIGN_TOP);	
//	
//	contactTable.getFlexCellFormatter().setColSpan(5, 1, 3);
//	contactTable.setWidget(5, 1, new ValueTable(2));
//	
//	((ValueTable) contactTable.getWidget(5, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
//	
//	
//	
//	/*
//	 * Sechste Zeile: Telefonnummer geschäftlich (PID 1)
//	 */
//	contactTable.setWidget(6, 0, new ValuePanel(1, 6, "Telefonnummern geschäftl: "));
//	contactTable.getFlexCellFormatter().setVerticalAlignment(6, 0, ALIGN_TOP);
//	
//	contactTable.getFlexCellFormatter().setColSpan(6, 1, 3);
//	contactTable.setWidget(6, 1, new ValueTable(1));
//	
//	((ValueTable) contactTable.getWidget(6, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
//		
//	
//
//	/*
//	 * Siebte Zeile: eMail (PID 3)
//	 */
//	contactTable.setWidget(7, 0, new ValuePanel(3, 7, "e-Mail-Adressen: "));
//	contactTable.getFlexCellFormatter().setVerticalAlignment(7, 0, ALIGN_TOP);
//
//	contactTable.getFlexCellFormatter().setColSpan(7, 1, 3);
//	contactTable.setWidget(7, 1, new ValueTable(3));
//	
//	((ValueTable) contactTable.getWidget(7, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Email")));
//	
//	
//	
//	/*
//	 * Achte Zeile: Homepages (PID 10)
//	 */
//	contactTable.setWidget(8, 0, new ValuePanel(10, 8,  "Homepages: "));
//	contactTable.getFlexCellFormatter().setVerticalAlignment(8, 0, ALIGN_TOP);
//	
//	contactTable.getFlexCellFormatter().setColSpan(8, 1, 3);
//	contactTable.setWidget(8, 1, new ValueTable(10));
//	
//	((ValueTable) contactTable.getWidget(8, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Homepage")));
//	
//	
//	
//	
//	/*
//	 * Neunte Zeile: Arbeitsstelle (PID 5)
//	 */
//	contactTable.setWidget(9, 0,  new ValuePanel(5, 9,  "Arbeitsstellen: "));
//	contactTable.getFlexCellFormatter().setVerticalAlignment(9, 0, ALIGN_TOP);
//	
//	contactTable.getFlexCellFormatter().setColSpan(9, 1, 3);
//	contactTable.setWidget(9, 1, new ValueTable(5));
//	
//	((ValueTable) contactTable.getWidget(9, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Arbeitsplatz")));
//
//	
//	/*
//	 * Zehnte Zeile: Buttons
//	 */
//	contactTable.getFlexCellFormatter().setColSpan(10, 0, 4);
//	contactTable.getFlexCellFormatter().setHeight(10, 0, "30px");
//	
//	VerticalPanel buttonPanel = new VerticalPanel();
//	contactTable.setWidget(10, 0, buttonPanel);
//	buttonPanel.setStyleName("buttonPanel");
//	
//	//Buttons, welche sich ausschließlich auf Kontakte beziehen
//	HorizontalPanel contactButtonsPanel = new HorizontalPanel();
//	buttonPanel.add(contactButtonsPanel);
//	
//	Button addContactButton = new Button("Neuen Kontakt anlegen");
//	Button shareContactButton = new Button("Kontakt teilen");
//	Button deleteContactButton = new Button("Kontakt löschen");
//	saveChangesButton = new Button("Änderungen speichern");
//
//	contactButtonsPanel.add(addContactButton);
//	contactButtonsPanel.add(shareContactButton);
//	contactButtonsPanel.add(deleteContactButton);
//	contactButtonsPanel.add(saveChangesButton);
//	
//	//Buttons, welche sich auf Kontakte in Kontaktlisten beziehen
//	HorizontalPanel contactListButtonsPanel = new HorizontalPanel();
//	buttonPanel.add(contactListButtonsPanel);
//	
//	Button addContactToContactListButton = new Button("Kontakt zu einer Kontaktliste hinzufügen");
//	removeContactFromContactListButton = new Button("Kontakt aus der aktuellen Kontaktliste entfernen");
//	
//	contactListButtonsPanel.add(addContactToContactListButton);
//	contactListButtonsPanel.add(removeContactFromContactListButton);
//	
//}

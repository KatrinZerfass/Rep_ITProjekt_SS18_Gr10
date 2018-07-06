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

public abstract class ClientsideFunctions {
	
	private static EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	private static User user = ClientsideSettings.getUser();
	
	public static boolean checkValue (ValueTextBox vtb) {
		
		String identifier = vtb.getIdentifier();
		String text = vtb.getText().toLowerCase().trim();
		
		if(text.contains("dropdatabase") || text.contains("drop database")) {
			ClientsideFunctions.popUpBox no = new ClientsideFunctions.popUpBox("For droping the database please find secret drop database pixel...", new CloseButton());
			no.getCloseButton().addCloseDBClickHandler(no);
			return false;
		}
		
		if(text.contains("'") || text.contains("\"") || text.contains(";")) {
			ClientsideFunctions.popUpBox signs = new ClientsideFunctions.popUpBox("Die Zeichen: ' \" und ; können nicht verwendet werden.", new CloseButton());
			signs.getCloseButton().addCloseDBClickHandler(signs);
			return false;
		}
		
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
	
	public static boolean isOwner (Contact c, User user) {
		
		if (user.getId() == c.getOwner()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isOwner (ContactList cl, User user) {
		
		if (user.getId() == cl.getOwner()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static class InputDialogBox extends DialogBox{
		
		private String input;
		
		Label dialogBoxLabel = new Label();
		
        private TextBox multiUseTextBox;
        private TextBox nameTextBox;
        private ListBox sexListBox;
        private SuggestBox sb;
        private MultiWordSuggestOracle oracle;
		CloseButton close=new CloseButton(this);

        Button ok = new Button("OK");
		
		public InputDialogBox(String userEmail) {
			
	    //    this.setPopupPosition(500, 200);
			
			setMultiUseTextBox(new TextBox());
			getMultiUseTextBox().getElement().setPropertyString("placeholder", "Vorname...");
			getMultiUseTextBox().setText("");
			
			setNameTextBox(new TextBox());
			getNameTextBox().getElement().setPropertyString("placeholder", "Nachname...");
			getNameTextBox().setText("");
			
			sexListBox = new ListBox();
			sexListBox.addItem("männlich");
			sexListBox.addItem("weiblich");
			sexListBox.addItem("Sonstiges");
			
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
	        panel.add(sexListBox);
	        panel.add(hpanel);

	        setWidget(panel);
	        
	        show();
			center();
		}
        
		public InputDialogBox(TextBox inputtb) {
			
			setMultiUseTextBox(inputtb);
	        ok.addStyleName("okbutton");
	        close.addStyleName("closebutton");
	        //this.setPopupPosition(500, 200);
			
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
		
		public InputDialogBox(MultiWordSuggestOracle inputOracle, String labelString) {
			
			setOracle(inputOracle);
	        ok.addStyleName("okbutton");
	        close.addStyleName("closebutton");
	       // this.setPopupPosition(500, 200);
	        
			setdialogBoxLabel(labelString);
					
			editorAdministration.getAllUserSuggestions(user, new AsyncCallback<Vector<String>>() {
				@Override
				public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
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
		
		public Button getOKButton() {
			return this.ok;
		}
		
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
		
		public TextBox getMultiUseTextBox() {
			return this.multiUseTextBox;
		}
		
		public void setMultiUseTextBox(TextBox tb) {
			this.multiUseTextBox = tb;
		}

		public SuggestBox getSuggestBox() {
			return sb;
		}

		public void setSuggestBox(SuggestBox sb) {
			this.sb = sb;
		}

		public MultiWordSuggestOracle getOracle() {
			return oracle;
		}

		public void setOracle(MultiWordSuggestOracle oracle) {
			this.oracle = oracle;
		}

		public TextBox getNameTextBox() {
			return nameTextBox;
		}

		public void setNameTextBox(TextBox nameTextBox) {
			this.nameTextBox = nameTextBox;
		}

		public ListBox getSexListBox() {
			return sexListBox;
		}

		public void setSexListBox(ListBox sexListBox) {
			this.sexListBox = sexListBox;
		}
	}
	
	public static class popUpBox extends DialogBox {
		CloseButton closeButton = null;
		OkButton okButton = null;
		VerticalPanel panel = null;
		HorizontalPanel hpanel = null;
		
		Label dialogBoxLabel = new Label();
		
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
		
		public CloseButton getCloseButton() {
			return this.closeButton;
		}
		
		public void setCloseButton(CloseButton b) {
			this.closeButton = b;
		}
		
		public OkButton getOkButton() {
			return this.okButton;
		}
		
		public void setOkButton(OkButton b) {
			this.okButton = b;
		}
	}
	
	public static class CloseButton extends Button{
		DialogBox db;
		
		public CloseButton(DialogBox db) {
			this.db = db;
			this.addClickHandler(new CloseDBClickHandler(db)); 
			this.setText("Abbrechen");
			this.addStyleName("closebutton");
		}
		
		public CloseButton(String text) {
			this.setText(text);
			this.addStyleName("closebutton");
		}
		
		public CloseButton() {
			this.setText("Abbrechen");
			this.addStyleName("closebutton");
		}
		
		public void addCloseDBClickHandler(DialogBox db) {
			this.addClickHandler(new CloseDBClickHandler(db));
		}
		
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
	
	public static class OkButton extends Button{

		
		public OkButton(String text) {
			this.setText(text);
			this.addStyleName("okbutton");
		}
		
		public OkButton() {
			this.setText("OK");
			this.addStyleName("okbutton");
		}
		
		public void addCloseDBClickHandler(DialogBox db) {
			this.addClickHandler(new CloseDBClickHandler(db));
		}
		
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

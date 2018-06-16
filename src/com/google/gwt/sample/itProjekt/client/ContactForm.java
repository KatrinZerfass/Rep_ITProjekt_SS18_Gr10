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
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Die Klasse ContactForm dient zur Darstellung von Kontakten mit all ihren Eigenschaften und deren AusprÃ¤gungen.
 * @author KatrinZerfass
 */

public class ContactForm extends VerticalPanel {
	
	
	EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	
	ContactListContactTreeViewModel clctvm = null;
	
	/** Der anzuzeigende Kontakt */
	Contact contactToDisplay = null;
	
	/**Alle AusprÃ¤gungen des anzuzeigenden Kontakts*/
	Vector<Value> allValuesOfContact = null;
	
	/**Ein Vector, in dem alle im Kontaktformular instantiierten ValueTextBoxes gespeichert werden. */
	Vector<ValueTextBox> allValueTextBoxes = new Vector<ValueTextBox>();
	
	
	
	
	/** Allumfassende Tabelle zur Darstellung von Kontakten */
	FlexTable contactTable = new FlexTable();
	
	/** Für den Kontaktstamm */ 
	ValueTextBox firstnameTextBox = new ValueTextBox("Name");
	ValueTextBox lastnameTextBox = new ValueTextBox("Name");
	ListBox sexListBox = new ListBox();
	
	/** Tabelle, in der Anschrift angezeigt wird */
	FlexTable addressTable = new FlexTable();
	
		
	/**ValueTextBoxen für die Anschrift*/
	ValueTextBox streetTextBox = new ValueTextBox("Straße");
	ValueTextBox houseNrTextBox = new ValueTextBox("Hausnummer");
	ValueTextBox plzTextBox = new ValueTextBox("PLZ");
	ValueTextBox cityTextBox = new ValueTextBox("Stadt");
	
	User currentUser = null;
	
	
	
	/**
	 * Die innere Klasse LockButton.
	 * Sie dient der Darstellung der Buttons mit dem Schloss-Symbol, welche hinter
	 * jeder einzelnen EigenschaftsausprÃ¤gung die MÃ¶glichkeit bieten, diese AusprÃ¤gung nicht zu teilen bzw. wieder zu teilen. 
	 */
	public class LockButton extends PushButton{
			
		/** Die AusprÃ¤gung, auf welche der jeweilige LockButton referenziert */
		private Value value;
		
		private boolean isLocked = false;
		
		private Image lockUnlocked = new Image("lock_unlocked.png");
		private Image lockLocked = new Image ("lock_locked.png");
		
		/**
		 * Der Konstruktor von LockButton. Es wird dem Button ein ClickHandler hinzugefÃ¼gt, welcher den Ãœbergang der ZustÃ¤nde von 
		 * <code>isLocked</code> bzw. von <code>isShared</code> (bezogen auf die AusprÃ¤gung) regelt.
		 */
		public LockButton() {
			
			this.addStyleName("lockButton");
			
			lockUnlocked.setPixelSize(17, 17);
			lockLocked.setPixelSize(17, 17);
			
			//per default sind alle AusprÃ¤gungen geteilt, d.h. das Schloss ist zu Beginn unlocked.
			this.getUpFace().setImage(lockUnlocked);
			
			this.setEnabled(false);
			
			this.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(isLocked ==false) { // =Schloss ist geÃ¶ffnet
					setValueToUnshared();	// =Schloss soll geschlossen werden, d.h. die AusprÃ¤gung wird nicht geteilt.
					}else setValueToShared();
				}
			});
		}
		
		/**
		 * Setter von <code>value</code>
		 * 
		 * @param Value die referenzierte AusprÃ¤gung
		 */
		public void setValue(Value v) {
			this.value = v;
		}
	
		
		/**
		 * Methode, die beim Klicken eines geÃ¶ffneten LockButtons aufgerufen wird. Folge: das Schloss wird geschlossen
		 * und die Variable <code>isShared</code> der AusprÃ¤gung wird auf false gesetzt. Diese VerÃ¤nderung wird mit einem 
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
		 * Methode, die beim Klicken eines geschlossenen LockButtons aufgerufen wird. Folge: das Schloss wird geÃ¶ffnet
		 * und die Variable <code>isShared</code> der AusprÃ¤gung wird auf <code>true</code> gesetzt. Diese VerÃ¤nderung wird mit einem 
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
	 * Sie dient der Darstellung der Buttons, welche hinter jeder einzelnen AusprÃ¤gung die MÃ¶glichkeit geben, diese zu lÃ¶schen.
	 * 
	 */
	public class DeleteValueButton extends PushButton{
		
		/** Die AusprÃ¤gung, auf welche der jeweilige DeleteValueButton referenziert */
		private Value value;
		
		private Image bin = new Image("bin.png");
		
		
		/**
		 * Konstruktor von DeleteValueButton. Es wird dem Button ein ClickHandler hinzugefÃ¼gt, welcher die Methode <code>deleteValue()</code> aufruft.
		 *
		 * @param String Text auf dem Button
		 */
		public DeleteValueButton() {
			this.getUpFace().setImage(bin);
			bin.setPixelSize(17, 17);
			
			this.addStyleName("deleteValueButton");
			
			this.setEnabled(false);
			
			this.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					editorAdministration.deleteValue(value, new AsyncCallback<Void>() {
						public void onFailure(Throwable t) {	
						}
						
						public void onSuccess(Void result) {	
							Window.alert("Die Ausprägung wurde gelöscht.");
						}

					});
				}
			});
				
		}
		
		/**
		 * Setter von <code>value</code>
		 * 
		 * @param Value die referenzierte AusprÃ¤gung
		 */
		public void setValue(Value v) {
			this.value=v;
		}
		
		/**
		 * Methode, um die DeleteValueButtons auszugrauen. Wird aufgerufen, wenn der angemeldete Nutzer nicht der EigentÃ¼mer des
		 * angezeigten Kontakts ist.
		 */
		public void disableButtons() {
			this.setEnabled(false);
		}
	}
	
	
	
	/**
	 * Die innere Klasse AddValueButton.
	 * Sie dient der Darstellung der Buttons, mit welchen man eine neue AusprÃ¤gung der jeweiligen Eigenschaft hinzufÃ¼gen kann.
	 */
	public class AddValueButton extends Button{
		
		/** Die referenzierte Eigenschaftsart. */
		private int propertyId;
		
			
		/**
		 * Konstruktor von AddValueButton. Setzt die Id der referenzierten Eigenschaft und fÃ¼gt dem Button einen ClickHandler
		 * hinzu, welcher ein neues PopUp-Fenster fÃ¼r das HinzufÃ¼gen einer EigenschaftsausprÃ¤gung Ã¶ffnen.
		 *
		 * @param int die ID der referenzierten Eigenschaft
		 */
		public AddValueButton(int pid) {
			this.setText("+");
			this.setStyleName("addValueButton");
			this.propertyId= pid;
			
		//wieder zurück kommentieren!
			this.setEnabled(true);
					
			this.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					//if(contactToDisplay != null) {
						addValuePopUp(propertyId);	
					//}else { //AddValueButton.this.get
						
				//	}
				}
			});
		}
		
		/**
		 * Gibt die ID der referenzierten Eigenschaft zurÃ¼ck.
		 *
		 * @return die ID der referenzierten Eigenschaft
		 */
		public int getPropertyId() {
			return this.propertyId;
		}
		
		public void addValuePopUp(int pid) {

			DialogBox addValuePopUp = new DialogBox();
			addValuePopUp.show();
			addValuePopUp.setText("Neue Ausprägung hinzufügen");
			addValuePopUp.setAnimationEnabled(true);
			addValuePopUp.setGlassEnabled(true);
			
			VerticalPanel addValueDialogBoxPanel = new VerticalPanel();
			addValueDialogBoxPanel.setHeight("100px");
			addValueDialogBoxPanel.setWidth("230px");
		    addValueDialogBoxPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
			
			Label addValueLabel = new Label();
			ValueTextBox addValueTextBox = null;
			Button addValueButton = new Button("Hinzufügen");
			
			
			
		
			switch(pid) {
			case 1: addValueTextBox = new ValueTextBox("Telefonnummer");
					addValueLabel.setText("Neue geschäftliche Telefonnummer: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(6, 1)), pid, addValueTextBox.getText()));
					break;
			case 2: addValueTextBox = new ValueTextBox("Telefonnummer");
					addValueLabel.setText("Neue private Telefonnummer: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(5, 1)), pid, addValueTextBox.getText()));
					break;
			case 3: addValueTextBox = new ValueTextBox("Email");
					addValueLabel.setText("Neue e-Mail-Adresse: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(7, 1)), pid, addValueTextBox.getText()));
					break;
			case 5: addValueTextBox = new ValueTextBox("Arbeitsplatz");
					addValueLabel.setText("Neue Arbeitsstelle: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(9, 1)), pid, addValueTextBox.getText()));
					break;
			case 10:addValueTextBox = new ValueTextBox("Homepage");
					addValueLabel.setText("Neue Homepage: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(8, 1)), pid, addValueTextBox.getText()));
					break;
		
			}
		
			addValueDialogBoxPanel.add(addValueLabel);
			addValueDialogBoxPanel.add(addValueTextBox);
			addValueDialogBoxPanel.add(addValueButton);
			addValuePopUp.add(addValueDialogBoxPanel);
		
		}
		
		
		private class AddValueClickHandler implements ClickHandler {
			DialogBox popup;
			ValueTextBox tb;
			FlexTable ft;
			int pid;
			String content;
			
			
			public AddValueClickHandler(DialogBox popup, ValueTextBox tb, FlexTable ft, int pid, String content) {
				this.popup = popup;
				this.tb = tb;
				this.ft = ft;
				this.pid = pid;
				this. content = content;
						
			}
			
			public void onClick(ClickEvent event) {
			
				if(!checkValue(tb)){
					tb.setText("");				
				}
				else {
					popup.hide();
							
					editorAdministration.createValue(contactToDisplay, pid, content, new AsyncCallback<Value>() {
						public void onFailure(Throwable t) {
							Window.alert("Ausprägung konnte nicht hinzugefügt werden. Versuchen Sie es erneut.");
//							ft.setWidget(ft.getRowCount(), 0, new ValueDisplay(new ValueTextBox(tb.getIdentifier())));
//							ValueTextBox vtb2= (ValueTextBox)((ValueDisplay) ft.getWidget(ft.getRowCount(),0)).getWidget(0);
//							vtb2.setValue("Hallo");
							
						}
						public void onSuccess(Value v) {
							ft.setWidget(ft.getRowCount(), 0, new ValueDisplay(new ValueTextBox(tb.getIdentifier())));
							((ValueDisplay) ft.getWidget(ft.getRowCount(),0)).setValue(v);
						}
					});
				
				}
				
			}
		}
	}
	
	
	
	
	/**
	 * Die innere Klasse ValueTextBox.
	 * Sie dient der Darstellungen der Textboxen, in denen EigenschaftsausprÃ¤gungen angezeigt und bearbeitet werden kÃ¶nnen.
	 * Die Besonderheit ist hier ein valueChangeHandler, welcher reagiert, wenn der Benutzer den Wert in der Textbox verÃ¤ndert.
	 * 
	 */
	public class ValueTextBox extends TextBox{
		
		/**Um spÃ¤ter auslesen zu kÃ¶nnen, bei welcher TextBox Ã„nderungen vorgenommen wurden.*/
		private boolean isChanged=false;
		
		/** Die referenzierte AusprÃ¤gung, welche in der TextBox angezeigt wird. */
		private Value value = null;
		
		private String identifier;
		
		/**
		 * Konstruktor von ValueTextBox. Setzt den Identifier der TextBox, welcher für die Methode checkValue notwendig ist.
		 * Und fÃ¼gt der TextBox den valueChangeHandler hinzu,  welcher den Zustand von <code>isChanged</code> Ã¤ndert.
		 * @param String identifier der Identifier
		 */
		public ValueTextBox(String identifier) {
	
			this.setIdentifier(identifier);
			
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
		 * @return die referenzierte AusprÃ¤gung
		 */
		public Value getTextBoxValue() {
			return this.value;
		}
		
		/**
		 * Setter von <code>value</code>. Setzt auch gleichzeitig den Inhalt als Text der TextBox.
		 * 
		 * @param Value die anzuzeigende AusprÃ¤gung
		 */
		public void setValue(Value v) {
			this.value = v;
			if(value!= null) {
				this.setText(value.getContent());
				allValueTextBoxes.add(this);
			}else {
				this.setText("");
				allValueTextBoxes.remove(this);
			}
			
		}

		public String getIdentifier() {
			return identifier;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

	
	}
	
	
	
	/**
	 * Die innere Klasse ValueDisplay. Sie erbt von HorizontalPanel.
	 * Sie dient der Darstellung einer EigenschaftsausprÃ¤gung in den jeweiligen FlexTables der einzelnen Eigenschaften.
	 * Diese besteht immer aus einer <code>ValueTextBox</code>, einem <code>LockButton</code> und einem <code>DeleteValueButton</code>.
	 */
	public class ValueDisplay extends HorizontalPanel{
		
		/** Die anzuzeigende AusprÃ¤gung */
		private Value value = null;
		
		private ValueTextBox valueTextBox;
		private LockButton lockButton;
		private DeleteValueButton deleteValueButton;
		
		/**
		 * Konstruktor von ValueDisplay. Der Inhalt der AusprÃ¤gung wird in der <code>ValueTextBox</code> angezeigt und <code>ValueTextBox</code>, 
		 * <code>LockButton</code> und <code>DeleteValueButton</code> werden dem Panel hinzugefÃ¼gt. 
		 *
		 *@param ValueTextBox die dazugehÃ¶rige TextBox
		 */
		public ValueDisplay(ValueTextBox vtb) {
			valueTextBox = vtb;
			lockButton = new LockButton();
			deleteValueButton = new DeleteValueButton();
			this.add(valueTextBox);
			this.add(lockButton);
			this.add(deleteValueButton);
			
			this.getWidget(0).setWidth("290px");
			

				
		}
		
		/**
		 * Setter von <code>value</code>. Setzt die AusprÃ¤gung in alle Elemente des Displays, welche auf diese referenzieren.
		 * 
		 * @param Value die anzuzeigende AusprÃ¤gung
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
		 * @return die anzuzeigende AusprÃ¤gung
		 */
		public Value getValue() {
			return this.value;
		}
		
		public ValueTextBox getValueTextBox() {
			return this.valueTextBox;
		}
		
		public void enableButtons() {
			this.deleteValueButton.setEnabled(true);
			this.lockButton.setEnabled(true);
		}
		
		public void disableButtons() {
			this.deleteValueButton.setEnabled(false);
			this.lockButton.setEnabled(false);
		}
		
		
		
	}
	

	public class ValuePanel extends VerticalPanel{
		int propertyId;
		Label valueLabel = new Label();
		AddValueButton addValueButton;
		
		public ValuePanel(int pid, String label){
			this.propertyId= pid;
			this.valueLabel.setText(label);
			this.addValueButton = new AddValueButton(propertyId);
			this.add(valueLabel);
			this.add(addValueButton);
			
		}
		
		public AddValueButton getAddValueButton(int index) {
			return (AddValueButton) getWidget(index);
		}
		
	}
	
	public class ValueTable extends FlexTable{
		int propertyId;
		ValueDisplay valueDisplay;
		
		public ValueTable(int pid) {
			this.propertyId=pid;
			
		}
		
		public ValueDisplay getValueDisplay(int row, int col) {
			return (ValueDisplay) getWidget(row, col);
		}
	}
	
	

	
	public class EmailDialogBox extends DialogBox{
		
		private String email;
		
        private ValueTextBox tb = new ValueTextBox("Email");
		
		public EmailDialogBox() {
			setText("Teilen");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
			Button ok = new Button("OK");
	        ok.addClickHandler(new ClickHandler() {
	        	public void onClick(ClickEvent event) {
	        		if(!checkValue(tb)){
						tb.setText("");				
					}
	        		else {
		        		checkValue(tb);
		        		email = tb.getText();
		        		
		        		if (contactToDisplay == null) {
		    				Window.alert("kein Kontakt ausgewählt!");
		    			}
		    			else {
		    				editorAdministration.shareContact(contactToDisplay, getEmail(), new AsyncCallback<Permission>() {
		    					public void onFailure(Throwable arg0) {
		    						Window.alert("Fehler beim teilen des Kontakts!");
		    					}
		    					public void onSuccess(Permission arg0) {
		    						Window.alert("Kontakt erfolgreich geteilt.");
		    					}
		    				});
		    			}
		        		
		            	EmailDialogBox.this.hide();
	        		}
	            }
	        });
	        

			
			Label label = new Label("Bitte geben Sie die Email-Adresse des Nutzers ein mit dem Sie teilen möchten.");
			
			VerticalPanel panel = new VerticalPanel();
			
	        panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	        panel.add(label);
	        panel.add(tb);
	        panel.add(ok);

	        setWidget(panel);
		}
		
		public String getEmail() {
			return this.email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
	}

	public boolean compareUser () {
		
		if (currentUser.getId() == contactToDisplay.getOwner()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Die Methode <code>onLoad()</code> wird in der EntryPoint-Klasse aufgerufen, um im GUI eine Instanz von ContactForm zu erzeugen.
	 */
	
	public void onLoad() {
		
		super.onLoad();
		
		/* 
		 * ZunÃ¤chst wird der angemeldete Nutzer abgefragt und als Instanzenvariable gespeichert.
		 */
		
		
		editorAdministration.getUser(new AsyncCallback<User>() {
			public void onFailure(Throwable caught) {
				System.out.println("Kein User ist angemeldet");
			}
			public void onSuccess(User result) {
				currentUser = result;
			}		
		});

		
		
		this.add(contactTable);
		

		
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
		
		contactTable.setWidget(3, 3, new ValueDisplay(new ValueTextBox("Geburtstag")));
		((ValueDisplay) contactTable.getWidget(3, 3)).getWidget(0).setWidth("105px");
				
		
		
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
		addressTable.setWidget(0, 2, new ValueDisplay(new ValueTextBox("")));
		((ValueDisplay) addressTable.getWidget(0, 2)).remove(0);

		

		/*
		 * Fünfte Zeile: Telefonnummern privat (PID 2)
		 */
		
		contactTable.setWidget(5, 0, new ValuePanel(2, "Telefonnummern privat: "));
		contactTable.getFlexCellFormatter().setVerticalAlignment(5, 0, ALIGN_TOP);	
		
		contactTable.getFlexCellFormatter().setColSpan(5, 1, 3);
		contactTable.setWidget(5, 1, new ValueTable(2));
		
		((ValueTable) contactTable.getWidget(5, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
		
		
		
		/*
		 * Sechste Zeile: Telefonnummer geschäftlich (PID 1)
		 */
		
		
		contactTable.setWidget(6, 0, new ValuePanel(1, "Telefonnummern geschäftl: "));
		contactTable.getFlexCellFormatter().setVerticalAlignment(6, 0, ALIGN_TOP);
		
		contactTable.getFlexCellFormatter().setColSpan(6, 1, 3);
		contactTable.setWidget(6, 1, new ValueTable(1));
		
		((ValueTable) contactTable.getWidget(6, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
	

		
//		//nur zum innere Rahmenlinien anzeigen lassen, zu Debug-Zwecken
//		for (int i= 0; i<privatePhoneNumbersTable.getRowCount(); i++) {
//			for (int a=0; a<privatePhoneNumbersTable.getCellCount(i); a++) {
//				privatePhoneNumbersTable.getCellFormatter().addStyleName(i, a, "cellBordersGreen");
//			}
//		}
		
		

		/*
		 * Siebte Zeile: eMail (PID 3)
		 */
		
		
		contactTable.setWidget(7, 0, new ValuePanel(3, "e-Mail-Adressen: "));
		contactTable.getFlexCellFormatter().setVerticalAlignment(7, 0, ALIGN_TOP);
	
		contactTable.getFlexCellFormatter().setColSpan(7, 1, 3);
		contactTable.setWidget(7, 1, new ValueTable(3));
		
		((ValueTable) contactTable.getWidget(7, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Email")));
		
		
		
		/*
		 * Achte Zeile: Homepages (PID 10)
		 */
		
		contactTable.setWidget(8, 0, new ValuePanel(10, "Homepages: "));
		contactTable.getFlexCellFormatter().setVerticalAlignment(8, 0, ALIGN_TOP);
		
		contactTable.getFlexCellFormatter().setColSpan(8, 1, 3);
		contactTable.setWidget(8, 1, new ValueTable(10));
		
		((ValueTable) contactTable.getWidget(8, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Homepage")));
		
		
		
		
		/*
		 * Neunte Zeile: Arbeitsstelle (PID 5)
		 */
		
		contactTable.setWidget(9, 0,  new ValuePanel(5, "Arbeitsstellen: "));
		contactTable.getFlexCellFormatter().setVerticalAlignment(9, 0, ALIGN_TOP);
		
		contactTable.getFlexCellFormatter().setColSpan(9, 1, 3);
		contactTable.setWidget(9, 1, new ValueTable(5));
		
		((ValueTable) contactTable.getWidget(9, 1)).setWidget(0,0, new ValueDisplay(new ValueTextBox("Arbeitsplatz")));
		
		
		
		
		//Zehnte Zeile: Buttons
		contactTable.getFlexCellFormatter().setColSpan(10, 0, 4);
		contactTable.getFlexCellFormatter().setHeight(10, 0, "30px");
		
		VerticalPanel buttonPanel = new VerticalPanel();
		contactTable.setWidget(10, 0, buttonPanel);
		buttonPanel.setStyleName("buttonPanel");
		
		HorizontalPanel contactButtonsPanel = new HorizontalPanel();
		buttonPanel.add(contactButtonsPanel);
		
		Button addContactButton = new Button("Neuen Kontakt anlegen");
		Button shareContactButton = new Button("Kontakt teilen");
		Button deleteContactButton = new Button("Kontakt löschen");
		Button saveChangesButton = new Button("Änderungen speichern");
	
		contactButtonsPanel.add(addContactButton);
		contactButtonsPanel.add(shareContactButton);
		contactButtonsPanel.add(deleteContactButton);
		contactButtonsPanel.add(saveChangesButton);
		
		HorizontalPanel contactListButtonsPanel = new HorizontalPanel();
		buttonPanel.add(contactListButtonsPanel);
		
		Button addContactToContactListButton = new Button("Kontakt zu einer Kontaktliste hinzufügen");
		addContactToContactListButton.addClickHandler(new AddContactToContactListClickHandler());
		
		Button removeContactFromContactListButton = new Button("Kontakt aus der aktuellen Kontaktliste entfernen");
		contactListButtonsPanel.add(addContactToContactListButton);
		contactListButtonsPanel.add(removeContactFromContactListButton);
		
		
		
	
	
		
				
		
//	//	Innere Rahmenlinien markieren zu Debug-Zwecken
//		for (int i= 0; i<contactTable.getRowCount(); i++) {
//			for (int a=0; a<contactTable.getCellCount(i); a++) {
//				contactTable.getCellFormatter().addStyleName(i, a, "cellBordersBlack");
//			}
//			contactTable.getCellFormatter().addStyleName(0,0, "firstRow");
//		}
//		contactTable.addStyleName("contactTableStyle");
		
		
		

		addContactButton.addClickHandler(new NewContactClickHandler());
		
		shareContactButton.addClickHandler(new ShareContactClickHandler());
		
		deleteContactButton.addClickHandler(new DeleteContactClickHandler());
		
		saveChangesButton.addClickHandler(new SaveChangesClickHandler());
				
	} 
	
	
	
	
	
	
	/**
	 * Die innere Klasse NewContactClickHandler.
	 */
	
	
	
	private class NewContactClickHandler implements ClickHandler{

	
		public void onClick(ClickEvent event) {
			clearContactForm();
			Window.alert("Füllen Sie das Formular für den neuen Kontakt aus.");
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
			Window.alert("Fehler beim Kontakt anlegen!");
			
		}
		
		
		public void onSuccess(Contact result) {
			clctvm.addContactOfContactList(cl, result);
			Window.alert("Kontakt wurde erfolgreich angelegt.");
			
		}
	}
	
	/**
	 * Die innere Klasse shareContactClickHandler.
	 */
	private class ShareContactClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt!");
			}
			else {
				EmailDialogBox dialog = new EmailDialogBox();
				dialog.show();
			}
		}
	}
	
	/**
	 * Die innere Klasse deleteContactClickHandler.
	 */
	private class DeleteContactClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt");
			}
			else {	
				clearContactForm();
				editorAdministration.deleteContact(contactToDisplay.getId(), new AsyncCallback<Void>() {
					public void onFailure(Throwable arg0) {
						Window.alert("Fehler beim löschen des Kontakts!");
					}
					public void onSuccess(Void arg0) {
						Window.alert("Kontakt erfolgreich gelöscht.");
					}
				});
			clctvm.removeContactOfContactList(clctvm.getSelectedContactList(), contactToDisplay);
			}
		}
	}
	
	/**
	 * Die innere Klasse saveChangesClickHandler.
	 */
	private class SaveChangesClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt");
			}
			else {
				for(ValueTextBox vtb : allValueTextBoxes) {
					if (vtb.getIsChanged() && vtb.getTextBoxValue() != null) {
						editorAdministration.editValue(contactToDisplay, vtb.getTextBoxValue().getPropertyid(), vtb.getTextBoxValue(), vtb.getTextBoxValue().getContent(), 
							vtb.getTextBoxValue().getIsShared(), new AsyncCallback<Value>() {
							
							public void onFailure(Throwable arg0) {	
							}
							public void onSuccess(Value arg0) {
							}
						});
					}
					
					//das kann so nicht stimmen!
					else if(vtb.getIsChanged() && vtb.getTextBoxValue() == null){
						editorAdministration.editContact(contactToDisplay.getId(), firstnameTextBox.getText(), lastnameTextBox.getText(), 
							contactToDisplay.getSex(), new AsyncCallback<Contact>() {
	
							public void onFailure(Throwable arg0) {
							}
							public void onSuccess(Contact arg0) {
							}
						});
					}
					else {
						Window.alert("Problem in saveChangesClickHandler");
					}
				}
			}
			
		}
	}
	
	private class AddContactToContactListClickHandler implements ClickHandler {
		
		ListBox listbox;
		VerticalPanel panel;
		ContactList choice;
		
        Vector<ContactList> contactLists;
		
		public void onClick(ClickEvent event) {
			
			panel = new VerticalPanel();
			panel.setHeight("100");
	        panel.setWidth("300");
	        panel.setSpacing(10);
	        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

	        editorAdministration.getAllContactListsOfActiveUser(new AsyncCallback<Vector<ContactList>>() {
	        	@Override
	        	public void onFailure(Throwable arg0) {	
	        	}
	        	@Override
	        	public void onSuccess(Vector<ContactList> arg0) {
	        		contactLists = arg0;
	        	}
			});
	        
	        listbox = new ListBox();
	        
	        for (ContactList cl : contactLists) {
	        	listbox.addItem(cl.getName());
	        }
	        
	        Button ok = new Button("OK");
	        ok.addClickHandler(new ClickHandler() {
	        	public void onClick(ClickEvent event) {
	        		for (ContactList cl : contactLists) {
	        			if (listbox.getSelectedItemText() == cl.getName()) {
	        				choice = cl;
	        			}
	        		}       		
	        		editorAdministration.addContactToContactList(choice, contactToDisplay, new AsyncCallback<ContactList>() {
	        			@Override
	        			public void onFailure(Throwable arg0) {
	        				Window.alert("Fehler beim Hinzufügen des Kontakts zur Kontaktliste!");
	        			}
	        			@Override
	        			public void onSuccess(ContactList arg0) {
	        				Window.alert("Kontakt zur Kontaktliste hinzugefügt.");
	        			}
					});
	        	}
	        });
	        
	        Label label = new Label("Bitte wählen sie die Kontaktliste aus.");
	        
	        panel.add(label);
	        panel.add(listbox);
	        panel.add(ok);
		}
	}
	
	private boolean checkValue (ValueTextBox vtb) {
		
		String identifier = vtb.getIdentifier();
		String text = vtb.getText().toLowerCase().trim();
	// TODO: work on RegExs!
		
		switch(identifier) {
			case "Name":
				if (text.matches("\\w+")) {
					return true;
				}
				else {
					Window.alert("Ungültige Zeichen im Namen!");
					return false;
				}
			case "Straße":
				if (text.matches("\\w+")) {
					return true;
				}
				else {
					Window.alert("Ungültiger Straßenname!");
					return false;
				}
			case "Hausnummer":
				if (text.matches("\\d+")) {
					return true;
				}
				else {
					Window.alert("Ungültige Hausnummer!");
					return false;
				}
			case "PLZ":
				if (text.matches("\\d+") && text.length() == 5) {
					return true;
				}
				else if (text.matches("\\d+") && text.length() != 5) {
					Window.alert("Bitte geben Sie eine gültige PLZ ein!");
					return false;
				}
				else if(!text.matches("\\d+") && text.length() == 5) {
					Window.alert("Ungültige Zeichen in der PLZ!");
					return false;
				}
				else {
					Window.alert("Ungültige Eingabe!");
					return false;
				}
			case "Stadt":
				if (text.matches("\\w+")) {
					return true;
				}
				else {
					Window.alert("Ungültiger Stadtname!");
					return false;
				}
			case "Telefonnummer":
				if (text.matches("\\d+")) {
					return true;
				}
				else {
					Window.alert("Ungültige Telefonnummer!");
					return false;
				}
			case "Geburtstag":
				if (text.matches("[0-3]\\d\\.[0\\d\\|1[0-2]].\\d\\d\\d\\d")) {
					return true;
				}
				else {
					Window.alert("Ungültige Telefonnummer!");
					return false;
				}
			case "Email":
				if (text.matches("(?:[a-zäöü0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
					return true;
				}
				else {
					Window.alert("Ungültige e-Mail-Adresse!");
					return false;
				}
			case "Homepage": 
				//TODO: 
				
			case "Arbeitsplatz":
				
			default: 
				Window.alert("Switch case hat nicht ausgelöst!");
				return false;
		}
		
	}
	
	
	
	
	
	/**
	 * Die Methode wird aufgerufen, wenn der addContactButton gedrÃ¼ckt wird. Die Felder leeren sich und ein neuer Kontakt
	 * kann eingetragen werden.
	 */
	public void clearContactForm() {
		setSelected(null);
	}
	
	
	
	/**
	 * Zeigt den selektierten Kontakt an.
	 *
	 * @param Contact der selektierte Kontakt
	 */
	public void setSelected(Contact c) {
		
		for(ValueTextBox vtb : allValueTextBoxes) {
			Value v = null;
			vtb.setValue(v);
		}
		
		this.allValueTextBoxes = null;
		
	
		
		if (c != null){
			contactToDisplay = c;
			
			/*
			 * Alle AusprÃ¤gungen des contactToDisplay werden ausgelesen und in einem Vector<Values> gespeichert.
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
			 * Der Vector allValuesOfContact, welcher alle AusprÃ¤gungen des anzuzeigenden Kontaktes enthÃ¤lt, wird durchiteriert
			 * und jede AusprÃ¤gung wird im dazugehÃ¶rigen Display angezeigt.
			 */ 
			for(int i=0; i<allValuesOfContact.size(); i++) {
				int pid = allValuesOfContact.get(i).getPropertyid();
		
				ValuePanel vp;
				ValueTable vt;
				
				switch (pid) {

					case 1: // Tel.Nr. geschäftlich
							vp = (ValuePanel) contactTable.getWidget(6, 0);
							vt = (ValueTable) contactTable.getWidget(6, 1);
							
							if(vt.getValueDisplay(0, 0).getValue() == null){
								vt.getValueDisplay(0, 0).setValue(allValuesOfContact.get(i));
							
								if(compareUser()) {
									vt.getValueDisplay(0, 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(0, 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							
							}else {
								/*
								 * Gibt es mehrere AusprÃ¤gungen zu geschÃ¤ftlichen Telefonnummern, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefÃ¼gt.
								 */
								vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
								vt.getValueDisplay(vt.getRowCount(), 0).setValue(allValuesOfContact.get(i));
								
								if (compareUser()) {
									vt.getValueDisplay(vt.getRowCount(), 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(vt.getRowCount(), 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							}
							break;
							
					
					case 2:  // Tel.Nr. privat
							vp = (ValuePanel) contactTable.getWidget(5, 0); 
							vt = (ValueTable) contactTable.getWidget(5, 1); 
							
							if(vt.getValueDisplay(0, 0).getValue() == null){
								vt.getValueDisplay(0, 0).setValue(allValuesOfContact.get(i));
							
								if(compareUser()) {
									vt.getValueDisplay(0, 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(0, 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							}else {
								/*
								 * Gibt es mehrere AusprÃ¤gungen zu privaten Telefonnummern, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefÃ¼gt.
								 */
								vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
								vt.getValueDisplay(vt.getRowCount(), 0).setValue(allValuesOfContact.get(i));
								
								if (compareUser()) {
									vt.getValueDisplay(vt.getRowCount(), 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(vt.getRowCount(), 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							}
							break;
							
					
					case 3:  // e-Mail
							vp = (ValuePanel) contactTable.getWidget(7, 0);
							vt = (ValueTable) contactTable.getWidget(7, 1);
							
							if(vt.getValueDisplay(0, 0).getValue() == null){
								vt.getValueDisplay(0, 0).setValue(allValuesOfContact.get(i));
							
								if(compareUser()) {
									vt.getValueDisplay(0, 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(0, 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							}else {
								
								/*
								 * Gibt es mehrere AusprÃ¤gungen zu e-Mail-Adressen, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefÃ¼gt.
								 */
								vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Email")));
								vt.getValueDisplay(vt.getRowCount() , 0).setValue(allValuesOfContact.get(i));
								
								if(compareUser()) {
									vt.getValueDisplay(vt.getRowCount() , 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(vt.getRowCount() , 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							}
							break;
							
							
					case 4:  // Geburtstag
							((ValueDisplay) contactTable.getWidget(3,3)).setValue(allValuesOfContact.get(i));
							break;
							
							
					case 5: // Arbeitsplatz
							vp = (ValuePanel) contactTable.getWidget(9, 0); 
							vt = (ValueTable) contactTable.getWidget(9, 1);
							
							if(vt.getValueDisplay(0, 0).getValue() == null){
								vt.getValueDisplay(0, 0).setValue(allValuesOfContact.get(i));
							
								if(compareUser()) {
									vt.getValueDisplay(0, 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(0, 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							}else {
								/*
								 * Gibt es mehrere AusprÃ¤gungen zu Arbeitsstellen, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefÃ¼gt.
								 */
								vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Arbeitsplatz")));
								vt.getValueDisplay(vt.getRowCount() , 0).setValue(allValuesOfContact.get(i));
								
								if(compareUser()) {
									vt.getValueDisplay(vt.getRowCount() , 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(vt.getRowCount() , 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							}
							break;
							
					

					case 6:  // Straße


							streetTextBox.setValue(allValuesOfContact.get(i));
							((LockButton) addressTable.getWidget(0, 2)).setValue(allValuesOfContact.get(i));
							((DeleteValueButton) addressTable.getWidget(0,3)).setValue(allValuesOfContact.get(i));
							
							if(compareUser()) {
								((LockButton) addressTable.getWidget(0, 2)).setEnabled(true);
								((DeleteValueButton) addressTable.getWidget(0,3)).setEnabled(true);
							}
							else {
								((LockButton) addressTable.getWidget(0, 2)).setEnabled(false);
								((DeleteValueButton) addressTable.getWidget(0,3)).setEnabled(false);
							}
							break;
			
					case 7:  // Hausnummer
							houseNrTextBox.setValue(allValuesOfContact.get(i));
							break;
							
					
					case 8:  // PLZ
							plzTextBox.setValue(allValuesOfContact.get(i));
							break;
					
					
					case 9:  // Wohnort
							cityTextBox.setValue(allValuesOfContact.get(i));
							break;
							
					
					case 10:  // Homepage
							vp = (ValuePanel) contactTable.getWidget(8, 0); 
							vt = (ValueTable) contactTable.getWidget(8, 1);
							
							if(vt.getValueDisplay(0, 0).getValue() == null){
								vt.getValueDisplay(0, 0).setValue(allValuesOfContact.get(i));
							
								if(compareUser()) {
									vt.getValueDisplay(0, 0).enableButtons();
									vp.getAddValueButton(1).setEnabled(true);
								}
								else {
									vt.getValueDisplay(0, 0).disableButtons();
									vp.getAddValueButton(1).setEnabled(false);
								}
							 }else {
								/*
								 * Gibt es mehrere AusprÃ¤gungen zu e-Mail-Adressen, wird eine neue Zeile in der FlexTable
								 * erstellt und dieser ebenfalls ein ValueDisplay hinzugefÃ¼gt.
								 */
								 vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Homepage")));
									vt.getValueDisplay(vt.getRowCount() , 0).setValue(allValuesOfContact.get(i));
									
									if(compareUser()) {
										vt.getValueDisplay(vt.getRowCount() , 0).enableButtons();
										vp.getAddValueButton(1).setEnabled(true);
									}
									else {
										vt.getValueDisplay(vt.getRowCount() , 0).disableButtons();
										vp.getAddValueButton(1).setEnabled(false);
									}
								}
								break;
						
				}
			}
				
		
	
		}else {
			firstnameTextBox.getElement().setPropertyString("placeholder", "Vorname...");
			lastnameTextBox.getElement().setPropertyString("placeholder", "Nachname...");
			((ValueDisplay) contactTable.getWidget(3,3)).getValueTextBox().getElement().setPropertyString("placeholder", "Geburtsdatum...");			
			streetTextBox.getElement().setPropertyString("placeholder", "Straße...");
			houseNrTextBox.getElement().setPropertyString("placeholder", "Hausnummer...");
			plzTextBox.getElement().setPropertyString("placeholder", "PLZ...");
			cityTextBox.getElement().setPropertyString("placeholder", "Wohnort...");
			((ValueTable) contactTable.getWidget(5, 1)).getValueDisplay(0,0).getValueTextBox().getElement().setPropertyString("placeholder", "Private Nummer...");		
			((ValueTable) contactTable.getWidget(6, 1)).getValueDisplay(0,0).getValueTextBox().getElement().setPropertyString("placeholder", "Geschätl. Nummer...");
			((ValueTable) contactTable.getWidget(7, 1)).getValueDisplay(0,0).getValueTextBox().getElement().setPropertyString("placeholder", "e-Mail-Adresse...");
			((ValueTable) contactTable.getWidget(8, 1)).getValueDisplay(0,0).getValueTextBox().getElement().setPropertyString("placeholder", "Homepage...");
			((ValueTable) contactTable.getWidget(9, 1)).getValueDisplay(0,0).getValueTextBox().getElement().setPropertyString("placeholder", "Arbeitsstelle...");
		}
		
		
		//Add-, Lock-, DeleteButtons + saveChangesButton TODO: richtige buttons disablen
				
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
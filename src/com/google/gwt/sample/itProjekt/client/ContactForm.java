package com.google.gwt.sample.itProjekt.client;

import java.util.Vector;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.sample.itProjekt.client.ClientsideFunctions.InputDialogBox;
import com.google.gwt.sample.itProjekt.shared.EditorAdministrationAsync;
import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.ContactList;
import com.google.gwt.sample.itProjekt.shared.bo.Permission;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Die Klasse ContactForm dient der Darstellung von Kontakten mit all ihren Eigenschaften und deren Ausprägungen.
 * @author KatrinZerfass & JanNoller & Anna-MariaGmeiner 
 */

public class ContactForm extends VerticalPanel {
	
	
	EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	
	/** Das verknüpfte TreeViewModel */
	ContactListContactTreeViewModel clctvm = null;
	
	/** Der anzuzeigende Kontakt */
	Contact contactToDisplay = null;
	
	/**Alle Ausprägungen des anzuzeigenden Kontakts*/
	Vector<Value> allValuesOfContact = new Vector<Value>();
	
	/**Alle vordefinierten Eigenschaften des Systems*/
	Vector<Property> allPredefinedProperties = new Vector<Property>();
	
	/**Alle dem Kontakt neu hinzugefügten Eigenschaften*/
	Vector<Property> allNewPropertiesOfContact = new Vector<Property>();
	
	/**Ein Vektor, in dem alle im Kontaktformular instantiierten ValueTextBoxes gespeichert werden. 
	 * Dieser Vektor wird ausgelesen, wenn Änderungen getätigt wurden und gespeichert werden sollen. 
	 */
	Vector<ValueTextBox> allValueTextBoxes = new Vector<ValueTextBox>();
	
	/** Allumfassende Tabelle zur Darstellung von Kontakten */
	FlexTable contactTable = new FlexTable();

	/** Ein Panel, welches alle Elemente rund um die ListBox für das Hinzufügen von Eigenschaften enthält */
	HorizontalPanel newPropertyPanel = new HorizontalPanel();
	
	/** Ein Panel, welches alle Buttons enthält, die man im Zusammenhang mit dem im Kontaktformular angezeigten Kontakt betätigen kann*/
	VerticalPanel buttonsPanel = new VerticalPanel();
	
	/* Instanzenvariablen für das Anzeigen des Kontaktstamms. Dieser wird immer im Kontaktformular angezeigt. */ 
	ValueTextBox firstnameTextBox;
	ValueTextBox lastnameTextBox;
	ListBox sexListBox = new ListBox();
	
	/** Tabelle, in der die Anschrift angezeigt wird */
	FlexTable addressTable = new FlexTable();
		
	/*ValueTextBoxen für die Anschrift*/
	ValueTextBox streetTextBox;
	ValueTextBox houseNrTextBox;
	ValueTextBox plzTextBox;
	ValueTextBox cityTextBox;
	
	/**Label für das Hinzufügen neuer Eigenschaften */
	Label newPropertyLabel = new Label("Eigenschaft hinzufügen");
	
	/**Listbox für das Hinzufügen neuer Eigenschaften */
	ListBox newPropertyListBox = new ListBox();
	
	/** Der Button, mit dem dem Kontakt neue Eigenschaften hinzugefügt werden können*/
	Button addNewPropertyButton = new Button("Hinzufügen");
	
	/*Die zwei ClickHandler für das Speichern von Änderungen und das Hinzufügen neuer Eigenschaften*/
	ClickHandler saveChangesClickHandler = null;
	ClickHandler addPropertyClickHandler = null;
	
	/**Der aktuell angemeldete Nutzer */
	User currentUser = new User();
	
	/* Buttons, um Interaktionen mit Kontakten zu ermöglichen */
	Button shareContactButton = null;	
	Button deleteContactButton = null;
	Button addContactToContactListButton = null;
	Button saveChangesButton = null;
	Button removeContactFromContactListButton = null;
	

//	/**
//	 * Die innere Klasse CloseButton.
//	 * Sie dient der Darstellung von "Abbrechen"-Buttons auf jeder instantiierten DialogBox. Dieser bietet dem Nutzer die Möglichkeit,
//	 * den Vorgang abzubrechen und die DialogBox zu schließen.
//	 * 
//	 *  @author KatrinZerfass
//	 */
//	
//	public class CloseButton extends Button{
//		
//		DialogBox db;
//		
//		/**
//		 * Der Konstruktor von LockButton. Die DialogBox, in der der CloseButton hinzugefügt wird, wird als Übergabeparameter definiert.
//		 */
//		public CloseButton(DialogBox db) {
//			this.db = db;
//			this.addClickHandler(new CloseDBClickHandler(db)); 
//			this.setText("Abbrechen");
//			this.addStyleName("closebutton");
//		}
//		
//		/**
//		 * Der ClickHandler wird im Konstruktor dem CloseButton hinzugefügt. Er schließt die DialogBox.
//		 * @author Zerfass
//		 *
//		 */
//		private class CloseDBClickHandler implements ClickHandler{
//			DialogBox db;
//	
//			
//			public CloseDBClickHandler(DialogBox db) {
//				this.db=db;
//			}
//			
//			public void onClick(ClickEvent event) {
//				db.hide();
//			}
//			
//		}
//		
//	}
	
	
	
	
	/**
	 * Die innere Klasse ValueTable. Erbt von FlexTable.
	 * Sie dient der Darstellung aller Ausprägungen einer Eigenschaft in Form von ValueDisplays (s.u.).
	 * 
	 *  @author KatrinZerfass
	 */
	public class ValueTable extends FlexTable{
		
		/**Die Eigenschaftsart der angezeigten Ausprägungen */
		int propertyId;
		
		
		/**
		 * Konstruktor von ValueTable. 
		 * @param pid die ID der referenzierten Eigenschaftsart 
		 */
		public ValueTable(int pid) {
			this.propertyId=pid;			
		}
		
		/**
		 * Getter für das valueDisplay in einer bestimmten Zeile. Später wichtig, um die Buttons in einem valueDisplay zu disablen.
		 * 
		 * @param row die Zeile in der ValueTable
		 * @return das ValueDisplay an der Stelle <code>row, 0</code>
		 */
		public ValueDisplay getValueDisplay(int row) {
			return (ValueDisplay) getWidget(row,  0);
		}
		
		
	}
	
	
	
	/**
	 * Die innere Klasse ValueDisplay. Sie erbt von HorizontalPanel.
	 * Sie dient der Darstellung einer Eigenschaftsausprägung in den jeweiligen ValueTables der einzelnen Eigenschaften.
	 * Sie besteht immer aus einer <code>ValueTextBox</code>, einem <code>LockButton</code> und einem <code>DeleteValueButton</code>.
	 * 
	 *  @author KatrinZerfass
	 */
	public class ValueDisplay extends HorizontalPanel{
		
		/** Die anzuzeigende und referenzierte Ausprägung */
		private Value value = null;
		
		private ValueTextBox valueTextBox;
		private LockButton lockButton;
		private DeleteValueButton deleteValueButton;
		
		/**
		 * Konstruktor von ValueDisplay. <code>ValueTextBox</code>, <code>LockButton</code> und <code>DeleteValueButton</code> werden gesetzt. 
		 *
		 *@param vtb die dazugehörige TextBox
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
		 * Setter von <code>value</code>. Setzt die Ausprägung in alle Elemente des Displays, welche somit auf diese referenzieren.
		 * 
		 * @param v die anzuzeigende Ausprägung
		 */
		public void setValue(Value v) {
			this.value = v;
			valueTextBox.setValue(value);
			lockButton.setValue(value);
			deleteValueButton.setValue(value);
		}
		
		
		/**
		 * Setter von <code>value</code>, wenn es sich bei der Ausprägung um die Straße handelt. Hinter der Anschrift existiert ebenfalls
		 * ein ValueDisplay, allerdings wurde die ValueTextBox wieder entfernt, deshalb wird die Ausprägung nur noch in den Lock- und
		 * DeleteButton gesetzt 
		 * @param v die referenzierte Ausprägung
		 * @param addressTable boolean, ob es sich um die addressTable handelt
		 */
		public void setValue(Value v, boolean addressTable) {
			this.value =v;
			lockButton.setValue(value);
			deleteValueButton.setValue(value);
		}
		
		
		
		/**
		 * Die Methode enableButton() wird aufgerufen, wenn der aktuelle Nutzer auch der Eigentümer des Kontakts ist.
		 */
		public void enableButtons() {
			this.deleteValueButton.setVisible(true);
			this.lockButton.setVisible(true);
		}
		
		
		/**
		 * Die Methode disableButton() wird aufgerufen, wenn der aktuelle Nutzer nur Teilhaber des Kontakts ist.
		 */
		public void disableButtons() {
			this.deleteValueButton.setVisible(false);
			this.lockButton.setVisible(false);
		}
		
		
		/**
		 * Methode, die beim Auslesen aller Ausprägungen eines Kontakts in jedem ValueDisplay den richtigen Stand des LockButtons setzt.
		 * Der Wert "false" zeigt an, dass nichts an die Datenbank kommuniziert werden muss. Lediglich das richtige Bild soll angezeigt werden. 
		 * 
		 * @param isShared 
		 */
		public void setLockButtonTo(boolean isShared) {
			if(isShared) {	
				this.lockButton.setValueToShared(false);
			}else {
				this.lockButton.setValueToUnshared(false);
			}
		}
			
	}
	
	
	
	/**
	 * Die innere Klasse ValueTextBox.
	 * Sie dient der Darstellungen der Textboxen, in denen Eigenschaftsausprägungen angezeigt und bearbeitet werden können.
	 * Die Besonderheit ist hier ein valueChangeHandler, welcher reagiert, wenn der Benutzer den Wert in der Textbox verändert.
	 * 
	 *  @author KatrinZerfass
	 */
	public class ValueTextBox extends TextBox{
		
		/**Um später auslesen zu können, bei welcher TextBox Änderungen vorgenommen wurden.*/
		private boolean isChanged=false;
		
		/** Die referenzierte Ausprägung, welche in der TextBox angezeigt wird. */
		private Value value = null;
		
		/** Ein Identifier, der notwendig ist, um den Inhalt der TextBox später mit <code>checkValue()</code> zu überprüfen. */
		private String identifier;
		
		/**
		 * Konstruktor von ValueTextBox. Setzt den Identifier der TextBox, welcher für die Methode <code>checkValue</code> notwendig ist.
		 * Außerdem wird die ValueTextBox dem Vektor aller VTBs hinzugefügt und ihr ein valueChangeHandler hinzugefügt,  
		 * welcher den Zustand von <code>isChanged</code> ändert.
		 * 
		 * @param identifier der Identifier für checkValue()
		 */
		public ValueTextBox(String identifier) {
	
			this.identifier = identifier;
			this.setText("");
			
			allValueTextBoxes.add(this);
		
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
		public Value getTextBoxValue() {
			return this.value;
		}
		
		
		/**
		 * Setter von <code>value</code>. 
		 * Setzt auch gleichzeitig den Inhalt der Value als Text der TextBox. (Wird null übergeben, dann wird der Inhalt gelöscht 
		 * und die TextBox aus dem  Vektor aller VTBs wieder entfernt. Dies passiert, wenn <code>setSelected(null)</code> aufgerufen wird.) 
		 * 
		 * @param v die anzuzeigende Ausprägung
		 */
		public void setValue(Value v) {
			this.value = v;
			
			if(value!= null) {
				setText(value.getContent());
			}else {
				/*
				 * Wird als Übergabeparameter null übergeben, so wird der Text aus der TextBox gelöscht und die TextBox aus dem Vector entfernt.
				 */
				this.setText("");
				allValueTextBoxes.remove(this);
			}
			
		}
		
		
		/**
		 * der Getter des Identifiers
		 * 
		 * @return der Identifier
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * Setter für identifier.
		 *
		 * @param newIndentifier der neue identifier
		 */
		public void setIdentifier(String newIndentifier) {
			this.identifier = newIndentifier;
		}

	}
	
	
	/**
	 * Die innere Klasse LockButton.
	 * Sie dient der Darstellung der Buttons mit dem Schloss-Symbol, welche hinter
	 * jeder einzelnen Eigenschaftsausprägung die Möglichkeit bieten, diese Ausprägung nicht zu teilen bzw. wieder zu teilen. 
	 * 
	 *  @author KatrinZerfass
	 */
	public class LockButton extends PushButton{
			
		/** Die Ausprägung, auf welche der jeweilige LockButton referenziert */
		private Value value;
		
		private boolean isLocked = false;
		
		private Image lockUnlocked = new Image("lock_unlocked.png");
		private Image lockLocked = new Image ("lock_locked.png");
		
		
		public LockButton() {
			
			this.addStyleName("lockButton");
			
			lockUnlocked.setPixelSize(17, 17);
			lockLocked.setPixelSize(17, 17);
			
			/*
			 * Per default sind alle Ausprägungen geteilt, d.h. das Schloss ist zu Beginn unlocked.
			 */
			this.getUpFace().setImage(lockUnlocked);
			
			this.setVisible(true);
			
			/*
			 * Es wird dem Button ein ClickHandler hinzugefügt, welcher die referenzierte Ausprägung auf "Nicht geteilt" bzw. "Geteilt" setzt. 
			 */
			this.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(isLocked ==false) { // =Schloss ist geöffnet, Ausprägung ist geteilt
					setValueToUnshared(true);	// =Schloss soll geschlossen werden, d.h. die Ausprägung wird nicht geteilt.
					}else setValueToShared(true);
				}
			});
		}
		
		/**
		 * Setter von <code>value</code>
		 * 
		 * @param v die referenzierte Ausprägung
		 */
		public void setValue(Value v) {
			this.value = v;
		}
	
		
		/**
		 * Die Methode setValueToShared schließt das Schloss und setzt die Variable <code>isShared</code> der Ausprägung auf <code>false</code>.
		 * Wird die Methode infolge einer Änderung durch den Benutzer aufgerufen, wird diese Änderung auch an den Server weitergegeben.
		 * 
		 */
		public void setValueToUnshared(boolean wasChanged) {
			this.getUpFace().setImage(lockLocked);
			isLocked = true;
			
			if(wasChanged) {

					editorAdministration.editValue(contactToDisplay, value.getPropertyid(), this.value, value.getContent(), false, new AsyncCallback<Value>() {
				
						public void onFailure(Throwable t) {
							Window.alert("Das Setzen dieser Ausprägung zu \"Nicht geteilt\" ist fehlgeschlagen.");
							
						}
						public void onSuccess(Value v) {
							
						}
					});
			}
		}
		
		
		/**
		 * Die Methode setValueToShared öffnet das Schloss und setzt die Variable <code>isShared</code> der Ausprägung auf <code>true</code>.
		 * Wird die Methode infolge einer Änderung durch den Benutzer aufgerufen, wird diese Änderung auch an den Server weitergegeben.
		 */
		public void setValueToShared(boolean wasChanged) {
			this.getUpFace().setImage(lockUnlocked);
			isLocked = false;
			
			if(wasChanged) {
				editorAdministration.editValue(contactToDisplay, value.getPropertyid(), this.value, value.getContent(), true, new AsyncCallback<Value>() {
			
					public void onFailure(Throwable t) {
						Window.alert("Das Setzen dieser Ausprägung zu \"Geteilt\" ist fehlgeschlagen.");
						
					}
					public void onSuccess(Value v) {
					
					}
				});
			}
		}	
		
		
	}
			
	
	
	/**
	 * Die innere Klasse DeleteValueButton.
	 * Sie dient der Darstellung der Buttons, welche hinter jeder einzelnen Ausprägung die Möglichkeit geben, diese zu löschen.
	 * 
	 *  @author KatrinZerfass
	 */
	public class DeleteValueButton extends PushButton{
		
		/** Die Ausprägung, auf welche der jeweilige DeleteValueButton referenziert */
		private Value value = null;
		
		private Image bin = new Image("bin.png");
		
		
		/**
		 * Konstruktor von DeleteValueButton. Es wird dem Button ein ClickHandler hinzugefügt, welcher die Methode <code>deleteValue()</code> aufruft.
		 */
		public DeleteValueButton() {
			
			this.getUpFace().setImage(bin);
			bin.setPixelSize(17, 17);
			
			this.addStyleName("deleteValueButton");
			
			this.setVisible(true);
			
			this.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					
					if(value.getPropertyid() == 6) { 
					
					/*
					 * Es handelt sich um den DeleteValueButton, welcher sich auf die Straße bezieht. Wird eine Straße gelöscht, 
					 * müssen auch Hausnummer, PLZ und Wohnort gelöscht werden, damit die gesamte Anschrift aus der GUI fliegt. 
					 */
						for(int i= 0; i<allValuesOfContact.size(); i++) {
							int pid = allValuesOfContact.get(i).getPropertyid();
							
							if(pid == 6 || pid == 7 || pid == 8 || pid ==9) {
								editorAdministration.deleteValue(allValuesOfContact.get(i), new AsyncCallback<Void>() {
									public void onFailure(Throwable arg0) {
													
									}
									public void onSuccess(Void arg0) {
										
									}
								});
							}
						}	
						//Bestätigungsmeldung
						final ClientsideFunctions.popUpBox deleted = new ClientsideFunctions.popUpBox("Adresse gelöscht.", new ClientsideFunctions.OkButton());
						deleted.getOkButton().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent arg0) {
								setSelected(contactToDisplay);
								deleted.hide();
							}
						});
					}
					
					else{
						editorAdministration.deleteValue(value, new AsyncCallback<Void>() {
							public void onFailure(Throwable t) {};{};
							public void onSuccess(Void result) {}; 
							{
								final ClientsideFunctions.popUpBox deleted = new ClientsideFunctions.popUpBox("Value gelöscht.", new ClientsideFunctions.OkButton());
								deleted.getOkButton().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent arg0) {
										setSelected(contactToDisplay);
										deleted.hide();
									}
								});
							};
						});
					}
				}
			});
		}
		
		/**
		 * Setter von <code>value</code>
		 * 
		 * @param v die referenzierte Ausprägung
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
	 * Die innere Klasse ValuePanel.
	 * Sie dient der Darstellung des Eigenschaftslabels und des AddValueButtons vor jeder ValueTable..
	 *
	 * @author KatrinZerfass
	 */
	public class ValuePanel extends VerticalPanel{
		
		/**Die referenzierte PropertyID*/
		int propertyId;
		
		Label valueLabel;
		AddValueButton addValueButton;
		
		/**
		 * Der Konstruktor von ValuePanel. Die Eigenschafts-ID wird gesetzt und das Label und der AddValueButton werden hinzugefügt..
		 * 
		 * @param pid die ID der referenzierten Eigenschaft
		 * @param row die Zeile, in der sich das Panel in der <code>contactTable</code> befindet
		 * @param label das Label, welches auf die Art der Eigenschaft hinweist
		 */
		public ValuePanel(int pid, int row, String label){
			
			this.propertyId= pid;
			
			valueLabel = new Label();
			this.valueLabel.setText(label);
			this.add(valueLabel);
			
			this.addValueButton = new AddValueButton(propertyId, row);
			this.add(addValueButton);
			
		}
		
		/**
		 * Getter des addValueButtons.
		 * @return den AddValueButton des ValuePanels
		 */
		public AddValueButton getAddValueButton() {
			return addValueButton;
		}
		
	}
	
	
	
	/**
	 * Die innere Klasse AddValueButton.
	 * Sie dient der Darstellung der Buttons, mit welchen man eine neue Ausprägung der jeweiligen Eigenschaftsart hinzufügen kann.
	 * 
	 *  @author KatrinZerfass
	 */
	public class AddValueButton extends Button{
		
		/** Die referenzierte Eigenschaftsart. */
		private int propertyId;
		
		/**Die Nummer der Zeile, in welcher sich die Eigenschaftsart in der <code>contactTable</code> befindet */
		private int row;
		
			
		/**
		 * Konstruktor von AddValueButton. Setzt die Id der referenzierten Eigenschaft und fügt dem Button einen ClickHandler
		 * hinzu, welcher ein neues PopUp-Fenster für das Hinzufügen einer Eigenschaftsausprägung öffnet.
		 *
		 * @param pid die ID der referenzierten Eigenschaft
		 * @param r die Zeile, in welcher der Button sich befindet
		 */
		public AddValueButton(int pid, int r) {
			this.setText("+");
			this.setStyleName("addValueButton");
			this.propertyId= pid;
			this.row= r;
		
			this.setVisible(true);
			
					
			this.addClickHandler(new ClickHandler() {
				
				public void onClick (ClickEvent event) {
						
					ValueTextBox vtb = new ValueTextBox("");
					final ClientsideFunctions.InputDialogBox input = new ClientsideFunctions.InputDialogBox(propertyId, row, vtb);
					input.getOKButton().addClickHandler(new AddValueClickHandler(input, input.getVTextBox(), ((ValueTable) contactTable.getWidget(row, 1)), propertyId));
					
				}
			});
		}
		

		
		/**
		 * Der innere Klasse AddValueClickHandler innerhalb der inneren Klasse AddValueButton.
		 * Wird aufgerufen, nachdem der Benutzer eine neue Ausprägung einträgt und in der InputDialogBox auf "Hinzufügen" klickt.
		 *
		 * @author KatrinZerfass
		 */
		private class AddValueClickHandler implements ClickHandler {
			
			DialogBox db;
			ValueTextBox vtb;
			ValueTable vt;
			int pid;
			
			/**
			 * Der Konstruktor von AddValueClickHandler. Ihm müssen alle Parameter aus der DialogBox übergeben werden, damit er in einem 
			 * Aufruf zum Server die neue Ausprägung anlegen und diese außerdem in der zugehörigen ValueTable anzeigen kann.
			 * 
			 * @param db die addValueDB
			 * @param vtb die addValueTextBox aus der DialogBox
			 * @param vt die ValueTable, der die neue Ausprägung hinzugefügt werden soll
			 * @param pid die PropertyID der Eigenschaftsart des AddValueButtons
			 */
			public AddValueClickHandler(DialogBox db, ValueTextBox vtb, ValueTable vt, int pid) {
				
				this.db = db;
				this.vtb = vtb;
				this.vt = vt;
				this.pid = pid;		
			}
			
			public void onClick(ClickEvent event) {
				/*
				 * Die Eingabe des Nutzers in die addValueTextBox wird mithilfe der Methode checkValue() auf Korrektheit überprüft.
				 */
				if(!ClientsideFunctions.checkValue(vtb)){
					vtb.setText("");				
				}
				else {
					db.hide();
							
					editorAdministration.createValue(contactToDisplay, pid, vtb.getText(), new AsyncCallback<Value>() {
						public void onFailure(Throwable t) {
							Window.alert("Ausprägung konnte nicht hinzugefügt werden. Versuchen Sie es erneut.");
						}
						public void onSuccess(Value v) {
							/*
							 * War das Anlegen der Ausprägung auf dem Server erfolgreich, so wird sie auch im GUI als neue Zeile in
							 * der jeweiligen ValueTable angezeigt.
							 */
							int vtRowCount = vt.getRowCount();
							vt.setWidget(vtRowCount, 0, new ValueDisplay(new ValueTextBox(vtb.getIdentifier())));
							((ValueDisplay) vt.getWidget(vtRowCount ,0)).setValue(v);
							((ValueDisplay) vt.getWidget(vtRowCount ,0)).enableButtons();
						}
					});
				
				}
				
			}
		}
	}

	
	/**
	 * Die Methode <code>onLoad()</code> wird in der EntryPoint-Klasse aufgerufen, um im GUI eine Instanz von ContactForm zu erzeugen.
	 * 
	 * @author KatrinZerfass & Anna-MariaGmeiner
	 */
	
	public void onLoad() {
		
		super.onLoad();
		
		this.add(contactTable);
		contactTable.setStyleName("contactTable");
		contactTable.getColumnFormatter().setWidth(0, "30px");
		
		
		/* 
		 * Der angemeldete Nutzer wird abgefragt und als Instanzenvariable gespeichert.
		 */
		currentUser = ClientsideSettings.getUser();
		
		
		
		/*
		 * Alle vordefinierten Eigenschaften werden aus der Datenbank ausgelesen und in einem Vektor gespeichert.
		 */
		editorAdministration.getAllPredefinedPropertiesOf(new AsyncCallback<Vector<Property>>(){
			public void onFailure(Throwable t) {
				Window.alert("Auslesen aller vordefinierten Eigenschaften fehlgeschlagen");
				
			}
			
			public void onSuccess(Vector<Property> properties) {
				allPredefinedProperties.clear();
				for (Property p : properties){
					allPredefinedProperties.add(p);
				}
				/*
				 * Die vordefinierten Eigenschaften werden der ListBox aller Eigenschaften hinzugefügt.
				 */
				for (Property p : allPredefinedProperties) {
					if(p.getType()!="Straße" && p.getType()!= "Hausnummer" && p.getType()!= "PLZ" && p.getType()!= "Wohnort") {
					newPropertyListBox.addItem(p.getType());
					}
				}
				
				newPropertyListBox.addItem("Anschrift");
				newPropertyListBox.addItem("Neue Eigenschaft anlegen");	

				
				newPropertyLabel.addStyleName("newPropertyLabel");
				newPropertyPanel.add(newPropertyLabel);
				newPropertyPanel.add(newPropertyListBox);
				newPropertyPanel.addStyleName("propertyPanel");
				newPropertyPanel.add(addNewPropertyButton);
				
				
			}
		});
		
		
		
		/*
		 * Elemente zum Anzeigen des Vor- und Nachnamens 
		 */
		Label firstnameLabel = new Label("Vorname: ");
		contactTable.setWidget(2, 0, firstnameLabel);
		firstnameTextBox = new ValueTextBox("Name");
		contactTable.setWidget(2, 1, firstnameTextBox);
		
		Label lastnameLabel = new Label("Nachname: ");
		contactTable.setWidget(2, 2, lastnameLabel);
		lastnameTextBox = new ValueTextBox("Name");
		contactTable.setWidget(2, 3, lastnameTextBox);
		
		
		
		/*
		 * Elemente zum Anzeigen bzw. Auswählen des Geschlechts
		 */
		Label sexLabel = new Label("Geschlecht: ");
		contactTable.setWidget(3, 0, sexLabel);
		
		sexListBox.addItem("-Auswählen-");
		sexListBox.addItem("männlich");
		sexListBox.addItem("weiblich");
		sexListBox.addItem("Sonstiges");
		contactTable.setWidget(3, 1, sexListBox);		
	
		
		/*
		 * Die Buttons für das buttonPanel werden instantiiert, mit Stylenamen versehen und zum buttonPanel hinzugefügt
		 */
		Button addContactButton = new Button("Neuen Kontakt anlegen");
		addContactButton.addStyleName("buttonPanel");
		buttonsPanel.add(addContactButton);
		
		shareContactButton = new Button("Kontakt teilen");		
		shareContactButton.addStyleName("buttonPanel");
		buttonsPanel.add(shareContactButton);
		
		deleteContactButton = new Button("Kontakt löschen");
		deleteContactButton.addStyleName("buttonPanel");
		buttonsPanel.add(deleteContactButton);
		
		addContactToContactListButton = new Button("Kontakt zu einer Kontaktliste hinzufügen");
		addContactToContactListButton.addStyleName("buttonPanel");
		buttonsPanel.add(addContactToContactListButton);
		
		removeContactFromContactListButton = new Button("Kontakt aus der aktuellen Kontaktliste entfernen");
		removeContactFromContactListButton.addStyleName("buttonPanel");
		buttonsPanel.add(removeContactFromContactListButton);
		
		saveChangesButton = new Button("Änderungen speichern");
		saveChangesButton.addStyleName("buttonPanel");
		buttonsPanel.add(saveChangesButton);
		
	
		buttonsPanel.setStyleName("buttonsPanel");
		addNewPropertyButton.addStyleName("addNewPropertyButton");
		newPropertyListBox.addStyleName("newPropertyListBox");
		
		
		/*
		 * Hinzufügen der ClickHandler zu allen Buttons
		 */
		addContactButton.addClickHandler(new NewContactClickHandler());
		
		shareContactButton.addClickHandler(new ShareContactClickHandler());
		
		deleteContactButton.addClickHandler(new DeleteContactClickHandler());
	
		addContactToContactListButton.addClickHandler(new AddContactToContactListClickHandler());
		
		removeContactFromContactListButton.addClickHandler(new RemoveContactFromContactListClickHandler());
		
		if(saveChangesClickHandler ==null) {
			saveChangesClickHandler = new SaveChangesClickHandler();
		}
		saveChangesButton.addClickHandler(saveChangesClickHandler);
		
		if (addPropertyClickHandler == null) {
			addPropertyClickHandler = new NewPropertyClickHandler();
			addNewPropertyButton.addClickHandler(addPropertyClickHandler);
		}
		
	
	} 
	
	
	/*
	 * Im Folgenden sind alle soeben hinzugefügten ClickHandler als eigene innere Klassen definiert.
	 */
	
	/**
	 * Die innere Klasse NewContactClickHandler. Betätigt der Nutzer den "Neuen Kontakt anlegen"-Button, wird eine Instanz des
	 * NewContactClickHandlers aufgerufen.  
	 * 
	 * @author KatrinZerfass
	 */
	private class NewContactClickHandler implements ClickHandler{
		
		public void onClick(ClickEvent event) {
			
			if(contactToDisplay != null) {
				//das ContactForm wird gecleared, damit ein neuer Kontakt angelegt werden kann 
				setSelected(null);
				
			}else {
				if(!ClientsideFunctions.checkValue(firstnameTextBox) || !ClientsideFunctions.checkValue(lastnameTextBox) ) {
					//Wurden ungültige Werte für Vor- und Nachnamen eingetragen, werden die Texte resettet und es kommt eine Fehlermeldung
					firstnameTextBox.setText("");
					lastnameTextBox.setText("");
					
					//Fehlermeldung, falls die Werte ungültig sind
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Ihr Kontakt konnte nicht angelegt werden, bitte versuchen Sie es erneut.", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
					
				}else if(ClientsideFunctions.checkValue(firstnameTextBox) && ClientsideFunctions.checkValue(lastnameTextBox)){
					
					/*
					 * Sind alle Eingaben gültig, wird im Folgenden ein neuer Kontakt im System angelegt
					 */
					
					String sex = "o";
					switch(sexListBox.getSelectedItemText()) {
						case "-Auswählen-":
							//Fehlermeldung, falls noch kein Geschlecht ausgewählt wurde
							final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Bitte wählen Sie ein Geschlecht aus.", new ClientsideFunctions.OkButton());
							failed.getOkButton().addClickHandler(new ClickHandler() {
								
								public void onClick(ClickEvent arg0) {
								
									failed.hide();
								}
							});
							return;
							
						case "männlich": 
							sex = "m";
							break;
						case "weiblich": 
							sex = "f";
							break;
						case "Sonstiges": 
							sex = "o";
							break;
					}
	
					editorAdministration.createContact(firstnameTextBox.getText(), lastnameTextBox.getText(), sex, currentUser, new AsyncCallback<Contact>(){
						public void onFailure(Throwable t) {
							Window.alert("Fehler beim Kontakt Anlegen.");
							
						}
						public void onSuccess(final Contact result) {
							
							//Bestätigungsmeldung
							final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontakt erfolgreich angelegt!", new ClientsideFunctions.OkButton());
							success.getOkButton().addClickHandler(new ClickHandler() {
								
								public void onClick(ClickEvent click) {
									//Ein neu angelegter Kontakt wird per Default immer der "Meine Kontakte"-Standardkontaktliste hinzugefügt.
									clctvm.addContactOfContactList(clctvm.getMyContactsContactList(), result);
									success.hide();
								}
							});
							
						}
					});
				}
			}
		}
	}
	
	
	
	/**
	 * Die innere Klasse shareContactClickHandler. Klickt der Benutzer auf den "Kontakt teilen"-Button, so wird eine
	 * Instanz des ShareContactClickHandlers aufgerufen.
	 * 
	 *  @author JanNoller
	 */
	private class ShareContactClickHandler implements ClickHandler{
		
		//DialogBox, in der der Nutzer, mit dem man teilen möchte, ausgewählt werden kann 
		ClientsideFunctions.InputDialogBox inputDB = null;
		
		public void onClick(ClickEvent event) {
			//Fehlermeldung, falls kein Kontakt ausgewählt ist
			if (contactToDisplay == null) {
				final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Kein Kontakt ausgewählt.", new ClientsideFunctions.OkButton());
				failed.getOkButton().addCloseDBClickHandler(failed);
			
			}else {
		
				inputDB = new ClientsideFunctions.InputDialogBox(new MultiWordSuggestOracle(), "Bitte geben Sie den Nutzer ein, mit dem Sie den Kontakt teilen möchten.");
				
				inputDB.getOKButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if(inputDB.getSuggestBox().getText()== "") {
							
							//falls kein Nutzer ausgewählt wurde, erscheint eine Fehlermeldung
							final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Bitte wählen Sie einen der vorgeschlagenen Nutzer aus.", new ClientsideFunctions.OkButton());
							failed.getOkButton().addCloseDBClickHandler(failed);
							
						}else {	
							/*
							 * Einträge der Suggestbox bestehen aus Klarnamen und e-mail-Adressen der Nutzer
							 * Im folgenden wird die e-Mail-Adresse vom restlichen String getrennt, um diese im Serveraufruf
							 * shareContact() als Argument zu übergeben.
							 */
							String[] split = inputDB.getSuggestBox().getText().split(" - ");
							String userEmail = split[1].substring(0, split[1].length());
	
							editorAdministration.shareContact(currentUser, userEmail, clctvm.getSelectedContact(), new AsyncCallback<Permission>() {
		
								public void onFailure(Throwable arg0) {
									//Fehlermeldung
									inputDB.hide();
									final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Bitte wählen Sie einen der vorgeschlagenen Nutzer aus.", new ClientsideFunctions.OkButton());
									failed.getOkButton().addCloseDBClickHandler(failed);
								}
								public void onSuccess(Permission arg0) {
									if(arg0 != null) {
										inputDB.hide();
										//Bestätigungsmeldung für das Teilen des Kontakts
										final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontakt erfolgreich geteilt.", new ClientsideFunctions.OkButton());
										success.getOkButton().addCloseDBClickHandler(success);
									}
									else if(arg0 == null) {
										inputDB.hide();
										//Meldet, falls es sich bei dem eingetragenen Nutzer um den Eigentümer des Kontakts handelt
										final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Nutzer ist der Besitzer des Kontakts.", new ClientsideFunctions.OkButton());
										failed.getOkButton().addCloseDBClickHandler(failed);
									}
								}
							});
						}
					}
				});
			}
		}
	}
	
	
	
	/**
	 * Die innere Klasse deleteContactClickHandler. Klickt der Nutzer den Button "Kontakt löschen", wird eine
	 * Instanz von DeleteContactClickHandler aufgerufen. 
	 *
	 * @author JanNoller
	 */
	private class DeleteContactClickHandler implements ClickHandler{
	
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				//Fehlermeldung, falls kein Kontakt ausgewählt ist
				final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Kein Kontakt ausgewählt.", new ClientsideFunctions.OkButton());
				failed.getOkButton().addCloseDBClickHandler(failed);
			}
			else if(contactToDisplay.getIsUser()) { 
				// der ausgewählte Kontakt ist der, welcher den User selbst repräsentiert
				final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Es ist nicht möglich, den eigenen Kontakt zu löschen.", new ClientsideFunctions.OkButton());
				failed.getOkButton().addCloseDBClickHandler(failed);
			}
			else{
				//PopUp, die den Benutzer fragt, ob er sich sicher ist, dass er den Kontakt löschen will
				final ClientsideFunctions.popUpBox safety = new ClientsideFunctions.popUpBox("Sind Sie sicher, dass Sie den Kontakt löschen möchten?", new ClientsideFunctions.OkButton(), new ClientsideFunctions.CloseButton());
				safety.getCloseButton().addCloseDBClickHandler(safety);
				
				safety.getOkButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						safety.hide();
						//Bestätigt der Nutzer, wird der Kontakt gelöscht
						editorAdministration.deleteContact(contactToDisplay, ClientsideFunctions.isOwner(contactToDisplay, currentUser), currentUser, new AsyncCallback<Void>() {
							public void onFailure(Throwable arg0) {
								Window.alert("Fehler beim Löschen des Kontakts!");
							}
							public void onSuccess(Void arg0){
								//Bestätigungsmeldung
								final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontakt erfolgreich gelöscht.", new ClientsideFunctions.OkButton());
								
								success.getOkButton().addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent arg0) {
										//der Kontakt fliegt auch im GUI aus der Default-Liste "Meine Kontakte" raus
										clctvm.removeContactOfContactList(clctvm.getSelectedContactList(), contactToDisplay);
										success.hide();
									}
								});
							}
						});
					}
				});
			}
		}
	}

	
	/**
	 * Die innere Klasse saveChangesClickHandler. Klickt ein Benutzer den "Änderungen speichern"-Button, wird eine
	 * Instanz von SaveChangesClickHandler aufgerufen.
	 * 
	 * @author JanNoller
	 */
	private class SaveChangesClickHandler implements ClickHandler{
		
		boolean changes = false;

		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				//Fehlermeldung, falls kein Kontakt ausgewählt ist
				final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Kein Kontakt ausgewählt.", new ClientsideFunctions.OkButton());
				failed.getOkButton().addCloseDBClickHandler(failed);
			}
			else {
				/*
				 * Der Vektor allValuesTextBoxes enthält alle im Kontaktformular instantiierten ValueTextBoxen
				 * Er wird durchiteriert und zu jeder ValueTextBox wird der Wert der boolschen Variable 
				 * "isChanged" überprüft. Ist er "true", werden dort Änderungen übernommen. 
				 */
				for(ValueTextBox vtb : allValueTextBoxes) {
					
					if (vtb.getIsChanged() && vtb.getTextBoxValue() != null) {
						if(!ClientsideFunctions.checkValue(vtb)) {
							//ist eine Ausprägung geändert worden, aber enthält ungültige Zeichen, so bricht die Methode ab
							return;
						}else {
							//ist die Änderung stimmig, wird sie in die Datenbank übernommen
							editorAdministration.editValue(contactToDisplay, vtb.getTextBoxValue().getPropertyid(), vtb.getTextBoxValue(), vtb.getText(), 
								vtb.getTextBoxValue().getIsShared(), new AsyncCallback<Value>() {
								
								public void onFailure(Throwable arg0) {	
									Window.alert("Fehler beim Änderungen speichern. \n (Ausprägungen)");
								}
								public void onSuccess(Value arg0) {
									changes = true;
								}
							});
						}
					}
					
					/*
					 * Wenn es sich bei der ValueTextBox um die firstnameTextBox oder lastnameTextBox handelt, wurde eine Veränderung am Kontaktstamm 
					 * vorgenommen und demzufolge wird die Methode editContact aufgerufen.
					 */
					else if(vtb.getIsChanged() && (vtb.equals(firstnameTextBox) || vtb.equals(lastnameTextBox))){
						if(!ClientsideFunctions.checkValue(vtb)) {
							//befinden sich ungültige Zeichen im Namen, bricht die Methode ab
							return;
						}else {
							//andernfalls werden die Änderungen am Namen in die Datenbank übernommen
							editorAdministration.editContact(contactToDisplay.getId(), firstnameTextBox.getText(), lastnameTextBox.getText(), 
								contactToDisplay.getSex(), new AsyncCallback<Contact>() {
								
									public void onFailure(Throwable arg0) {
										Window.alert("Fehler beim Änderungen speichern. \n (Kontaktstamm)");
									}
									public void onSuccess(Contact arg0) {
										changes = true;
										//der neue Kontaktname wird auch im TreeViewModel upgedatet
										clctvm.updateContact(arg0);
										
									}
							});
						}
					}
				}
			}
			if(changes) {
				//Besätigungsmeldung, dass Änderungen übernommen worden sind 
				final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Die Änderungen wurden übernommen.", new ClientsideFunctions.OkButton());
				success.getOkButton().addCloseDBClickHandler(success);			
			}
		}
	}
	
	
	/**
	 * Die innere Klasse AddContactToContactListClickHandler. Klickt ein Benutzer auf den Button "Kontakt zu einer 
	 * Kontaktliste hinzufügen, wird eine Instanz von AddContactToContactListClickHandler erzeugt. 
	 * 
	 * @author JanNoller & KatrinZerfass
	 */
	private class AddContactToContactListClickHandler implements ClickHandler {
		
		ContactList chosenCL;
		
		public void onClick(ClickEvent event) {
			
			if(contactToDisplay ==null) {
				//Fehlermeldung, falls kein Kontakt ausgewählt ist
				final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Kein Kontakt ausgewählt.", new ClientsideFunctions.OkButton());
				failed.getOkButton().addCloseDBClickHandler(failed);
			}
			else {
				//es erscheint eine DialogBox, in der der Nutzer eine Kontaktliste auswählen kann 
				final InputDialogBox input = new InputDialogBox(new ListBox(), currentUser);
				input.getOKButton().addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent arg0) {
						
						for (ContactList cl : input.getContactLists()) {
		        			if (input.getListBox().getSelectedItemText() == cl.getName()) {
		        				chosenCL = cl;
		        				//die ausgewählte Kontaktliste wird der Variable chosenCL zugewiesen
		        			}
		        		}
		        		
		        		input.hide();
		        		
		 		        //Hat der Nutzer eine Kontaktliste ausgewählt und klickt "OK", so wird der Kontakt dieser Kontaktliste hinzugefügt
		        		editorAdministration.addContactToContactList(chosenCL, contactToDisplay, new AsyncCallback<ContactList>() {
		        			
		        			public void onFailure(Throwable z) {
		        				Window.alert("Fehler beim Hinzufügen des Kontakts zur Kontaktliste.");
		        			}
		        			
		        			public void onSuccess(ContactList result) {
		        				//Bestätigungsmeldung, dass der Kontakt der Liste hinzugefügt wird 
		        				final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontakt zur Kontaktliste hinzugefügt.", new ClientsideFunctions.OkButton());
								success.getOkButton().addCloseDBClickHandler(success);
		        			}
						});				
					}
				});
			}   
		}
	}
	

	
	/**
	 * Die innere Klasse RemoveContactFromContactListClickHandler. Klickt der Nutzer den "Kontakt aus der
	 * Kontaktliste entfernen"-Button, so wird eine Instanz von RemoveContactFromContactListClickHandler erzeugt. 
	 * 
	 * @author JanNoller
	 */
	private class RemoveContactFromContactListClickHandler implements ClickHandler{
		
		public void onClick(ClickEvent event) {
			if(contactToDisplay == null) {
				//Fehlermeldung, falls kein Kontakt ausgewählt ist
				final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Kein Kontakt ausgewählt.", new ClientsideFunctions.OkButton());
				failed.getOkButton().addCloseDBClickHandler(failed);
			}
			else {
				if(clctvm.getSelectedContactList() == clctvm.getMyContactsContactList()) {
					//Fehlermeldung, falls es sich bei der Kontaktliste um die Default Liste "Meine Kontakte" handelt
					final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Sie können aus dieser Kontaktliste keine Kontakte löschen.", new ClientsideFunctions.OkButton());
					failed.getOkButton().addCloseDBClickHandler(failed);
				}
				else if(!ClientsideFunctions.isOwner(clctvm.getSelectedContactList(), currentUser)) {
					//Fehlermeldung, wenn der Nutzer nicht der Eigentümer der Kontaktliste ist
					final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Sie können den Kontakt aus dieser Liste nicht entfernen, da es eine geteilte Liste ist.", new ClientsideFunctions.OkButton());
					success.getOkButton().addCloseDBClickHandler(success);
				}
				else {
					/*
					 * Handelt es sich weder um die Default-Liste "Meine Kontakte", noch um eine geteilte Liste, so wird der Benutzer gefragt, ob
					 * er den Kontakt wirklich entfernen möchte. 
					 */
					final ClientsideFunctions.popUpBox safety = new ClientsideFunctions.popUpBox("Sind Sie sicher, dass Sie den Kontakt aus der Liste entfernen möchten?", new ClientsideFunctions.OkButton(), new ClientsideFunctions.CloseButton());
					safety.getCloseButton().addCloseDBClickHandler(safety);
					safety.getOkButton().addClickHandler(new ClickHandler() {
						
						public void onClick(ClickEvent click) {
							safety.hide();
							//hat der Benutzer mit "Ok" bestätigt, wird der Kontakt aus der Liste entfernt
							editorAdministration.removeContactFromContactList(clctvm.getSelectedContactList(), contactToDisplay, new AsyncCallback<ContactList>() {
								
								public void onFailure(Throwable arg0) {	
									Window.alert("Fehler beim Entfernen des Kontakts aus der Kontaktliste.");
								}
						
								public void onSuccess(final ContactList arg0) {
									//Bestätigungsmeldung, wenn der Kontakt entfernt wurde
									final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Kontakt erfolgreich aus der Kontaktliste entfernt.", new ClientsideFunctions.OkButton());
									success.getOkButton().addClickHandler(new ClickHandler() {
										
									
										public void onClick(ClickEvent click) {
											//der Konakt wird auch im GUI aus der Kontaktliste entfernt
											clctvm.removeContactOfContactList(clctvm.getSelectedContactList(), contactToDisplay);
											success.hide();
										}
									});
								}
							});
						}
					});
				}
			}
		}
	}
	
	
	/**
	 * Die innere Klasse NewPropertyClickHandler. Klickt ein Benutzer auf den "Hinzufügen" Button unten
	 * im NewPropertyPanel, so wird eine Instanz von NewPropertyClickHandler erzeugt.
	 * Im Folgenden wird abgeprüft, um welche Eigenschaftsart es sich handelt. Bei Geburtsdatum, Anschrift und
	 * neu anzulegenden Eigenschaften besteht eine spezielle Behandlung (s.u.).
	 * 
	 * @author KatrinZerfass
	 */
	private class NewPropertyClickHandler implements ClickHandler{
		
		ValueTextBox inputTextBox = null;
		int pid;
		String ptype;
		int row;

		public void onClick(ClickEvent event) {
			//ptype wird die aus der Listbox ausgewählten Eigenschaftsart zugewiesen
			ptype = newPropertyListBox.getSelectedItemText();
			row = contactTable.getRowCount();
			
			if(ptype == "Geburtstag") {
				inputTextBox = new ValueTextBox("Geburtstag");
				inputTextBox.getElement().setPropertyString("placeholder", "dd.mm.yyyy");
				
				//es erscheint eine DialogBox, in welche ein Geburtsdatum eingetragen werden kann
				final InputDialogBox input = new InputDialogBox(inputTextBox, "Geburtsdatum eintragen");
				input.getOKButton().addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						input.hide();

						
						if(!ClientsideFunctions.checkValue(inputTextBox)) {
							//wird ein ungültiges Geburtsdatum eingetragen, wird die Textbox resettet
							inputTextBox.setText("");
							
						}else {
							//andernfalls wird das Geburtsdatum für den Kontakt erstellt
							editorAdministration.createValue(contactToDisplay, 4, inputTextBox.getText(), new AsyncCallback<Value>() {
								public void onFailure (Throwable t) {
									Window.alert("Geburtsdatum anlegen gescheitert.");
								}
								
								public void onSuccess(Value result) {
								
									/*
									 * Ein neu angelegtes Geburtsdatum erscheint im Kontaktformular oben neben dem Geschlecht.
									 * Das zugehörige Label und das zugehörige ValueDisplay werden direkt in die entsprechenden
									 * Zellen der contactTable gesetzt.
									 */
									Label birthdateLabel = new Label("Geburtsdatum: ");
									contactTable.setWidget(3, 2, birthdateLabel);
									
									contactTable.setWidget(3, 3, new ValueDisplay(new ValueTextBox("Geburtstag")));
									((ValueDisplay) contactTable.getWidget(3, 3)).getWidget(0).setWidth("105px");
									((ValueDisplay) contactTable.getWidget(3,3)).setValue(result);
								
									//wurde einmal ein Geburtsdatum angelegt, wird die Auswahlmöglichkeit aus der ListBox entfernt, da Geburtsdatum einmalig ist
									for(int c=0; c<newPropertyListBox.getItemCount(); c++) {
										if (newPropertyListBox.getItemText(c) == ptype) {
											newPropertyListBox.removeItem(c);
										}
									}
								}
							});
						}
					}
				});
				
				
			}
			else if(ptype == "Neue Eigenschaft anlegen") {
				
				//DialogBox, in der man eine neue Eigenschaftsart anlegen kann 
				final InputDialogBox input = new InputDialogBox(new ValueTextBox("Sonstiges"), "Neue Eigenschaftsart anlegen");
				input.getOKButton().addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent arg0) {
						input.hide();
						//die neue Eigenschaft für diesen Kontakt wird erstellt
						editorAdministration.createProperty(contactToDisplay, input.getVTextBox().getText(), new AsyncCallback<Property>() {
							public void onFailure (Throwable t) {
								Window.alert("Eigenschaft anlegen gescheitert.");
							}
							
							public void onSuccess(Property result) {
					
								if(result == null) {
									//Fehlermeldung, falls diese Eigenschaft schonmal für den Kontakt angelegt wurde
									final ClientsideFunctions.popUpBox failed = new ClientsideFunctions.popUpBox("Diese Eigenschaft haben Sie bereits angelegt.", new ClientsideFunctions.OkButton());
									failed.getOkButton().addClickHandler(new ClickHandler() {
										
										public void onClick(ClickEvent click) {
											failed.hide();
											return;
										}
									});
								}else {
								
									ptype = result.getType();
									pid = result.getId();
									//die entsprechenden Elemente für die neue Eigenschaft werden in der contactTable hinzugefügt
									contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
									contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
									
									contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
									contactTable.setWidget(row, 1, new ValueTable(pid));
								}
							}				
						});
					}
				});

			}
			
			else if(ptype == "Anschrift") {
				/*
				 * Bei der Anschrift besteht die Besonderheit, dass sie immer aus den 4 Ausprägungen Straße, Hausnummer,
				 * PLZ und Wohnort bestehen muss. Demensprechend wird sie im Kontaktformular auch in einer speziellen
				 * Tabelle angezeigt.
				 */
				VerticalPanel addressPanel = new VerticalPanel();
				contactTable.setWidget(row, 0, addressPanel);
				
				Label addressLabel = new Label("Anschrift: ");
				addressPanel.add(addressLabel);
				
				//Button, um die Anschrift nach Eintragen aller Werte anzulegen
				Button addAddressButton = new Button("Anlegen");
				addAddressButton.addStyleName("addNewPropertyButton");
				addAddressButton.addStyleName("anlegenbutton");
				
				addressPanel.add(addAddressButton);
				
				contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
				contactTable.setWidget(row, 1, addressTable);
				
				//Alle zur Anschrift gehörenden TextBoxen werden initialisiert
				streetTextBox = new ValueTextBox("Straße");
				houseNrTextBox = new ValueTextBox("Hausnummer");
				plzTextBox = new ValueTextBox("PLZ");
				cityTextBox = new ValueTextBox("Stadt");
				
				addressTable.setWidget(0, 0, streetTextBox);
				addressTable.setWidget(0, 1, houseNrTextBox);
				addressTable.setWidget(1, 0, plzTextBox);
				addressTable.setWidget(1, 1, cityTextBox);
				
				streetTextBox.getElement().setPropertyString("placeholder", "Straße...");
				houseNrTextBox.getElement().setPropertyString("placeholder", "Hausnummer...");
				plzTextBox.getElement().setPropertyString("placeholder", "PLZ...");
				cityTextBox.getElement().setPropertyString("placeholder", "Wohnort...");
					
				addressTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
				addressTable.setWidget(0, 2, new ValueDisplay(new ValueTextBox("Sonstiges")));
				((ValueDisplay) addressTable.getWidget(0, 2)).remove(0);
				
				newPropertyListBox.setSelectedIndex(0);
				
				//ClickHandler des Buttons "Anlegen"
				addAddressButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						
						if(!ClientsideFunctions.checkValue(streetTextBox) || !ClientsideFunctions.checkValue(houseNrTextBox) 
							|| !ClientsideFunctions.checkValue(plzTextBox) || !ClientsideFunctions.checkValue(cityTextBox)){
							//befinden sich ungültige Werte in der Anschrift, bricht die Methode ab
							return;
						}
						
						//der Anlegen Button wird entfernt und es wird die Adressen angelegt
						((VerticalPanel) contactTable.getWidget(row,0)).remove(1);
						
						editorAdministration.createAddress(streetTextBox.getText(), houseNrTextBox.getText(),
								plzTextBox.getText(), cityTextBox.getText(), contactToDisplay, new AsyncCallback<Value>(){
							public void onFailure(Throwable t) {
								Window.alert("Probleme beim Anlegen der Adresse.");
							}
							public void onSuccess(Value street) {
								streetTextBox.setValue(street);
								((ValueDisplay) addressTable.getWidget(0, 2)).setValue(street, true);
							
								//die Anschrift kann nur einmal hinzugefügt werden, daher wird die Option aus der ListBox genommen
								for(int c=0; c<newPropertyListBox.getItemCount(); c++) {
									if (newPropertyListBox.getItemText(c) == ptype) {
										newPropertyListBox.removeItem(c);
									}
								}
								
								//Bestätigungsmeldung, dass die Anschrift angelegt wurde
								final ClientsideFunctions.popUpBox success = new ClientsideFunctions.popUpBox("Anschrift erfolgreich angelegt!", new ClientsideFunctions.OkButton());
								success.getOkButton().addCloseDBClickHandler(success);
							}
						});

						
					}
				});
			
				
			}else {	
				//handelt es sich um eine ganz normale Eigenschaft ohne besondere Behandlung, wird diese angelegt
				editorAdministration.getPropertyByType(ptype, null, new AsyncCallback<Property>() {
					public void onFailure (Throwable t) {
						
					}
					
					public void onSuccess(Property result) {
						pid = result.getId();
						//die entsprechenden Elemente werden in die contactTable hinzugefügt
						contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, new ValueTable(pid));
						
						//eine bereits hinzugefügte Eigenschaft wird aus der ListBox entfernt
						for(int c=0; c<newPropertyListBox.getItemCount(); c++) {
							if (newPropertyListBox.getItemText(c) == ptype) {
								newPropertyListBox.removeItem(c);
							}
						}
					}
				});
			}
			
			
			
			

		}
	}
	
		
	
	/**
	 * Die Methode clearContactFrom() wird immer zu Beginn der Methode setSelected(Contact c) aufgerufen, um
	 * bei Aufruf eines neuen Kontakts das Kontaktformular zu leeren und komplett neu zu laden. 
	 */
	public void clearContactForm() {
		
		contactTable.clear();
		contactTable.removeAllRows();
		buttonsPanel.clear();
		sexListBox.clear();
		newPropertyPanel.clear();
		newPropertyListBox.clear();
		allValuesOfContact.clear();
	
		for(ValueTextBox vtb : allValueTextBoxes) {
			if(vtb.getTextBoxValue() !=null) {
				vtb.setValue((Value) null);
			}
		}
		allValueTextBoxes.clear();
		
		this.onLoad();
		
	}
	
	
	
	/**
	 * Die Methode setSelected wird immer bei Auswahl eines Elements aus dem Cellbrowser ausgewählt.
	 * Wird eine Kontaktlitse angeklickt, wird sie mit dem Argument null aufgerufen.
	 * Wird ein Kontakt angeklickt, wird sie mit dem entsprechenden Kontakt als Argument aufgerufen.
	 *
	 * @param c der selektierte Kontakt bzw. null
	 * @author KatrinZerfass
	 */
	public void setSelected(Contact c) {
		
		clearContactForm();
		
		firstnameTextBox.setEnabled(true);
		lastnameTextBox.setEnabled(true);
		sexListBox.setEnabled(true);
		
		shareContactButton.setVisible(false);
		deleteContactButton.setVisible(false);
		addContactToContactListButton.setVisible(false);
		removeContactFromContactListButton.setVisible(false);
		saveChangesButton.setVisible(false);
	
		
		if (c != null){
			
			//es wurde ein Kontakt-Objekt übergeben, welches in die Instanzenvariable contactToDisplay gesetzt wird
			contactToDisplay = c;
			
			//Optionen und Interaktionsmöglichkeiten für Kontakte werden eingeblendet
			newPropertyPanel.setVisible(true);
			shareContactButton.setVisible(true);
			deleteContactButton.setVisible(true);
			addContactToContactListButton.setVisible(true);
			removeContactFromContactListButton.setVisible(true);
			
			
			
			/*
			 * Der angemeldete Nutzer wird mit dem Eigentümer verglichen. Ist er ausschließlich Teilhaber, so werden bestimmte Buttons
			 * und Funktionen, an denen er keine Berechtigungen hat, nicht mehr angezeigt.
			 */
			if(!ClientsideFunctions.isOwner(contactToDisplay, currentUser)) {
				firstnameTextBox.setEnabled(false);
				lastnameTextBox.setEnabled(false);
				sexListBox.setEnabled(false);
				saveChangesButton.setVisible(false);
				newPropertyPanel.setVisible(false);
			}else {
				
				saveChangesButton.setVisible(true);
				newPropertyPanel.setVisible(true);
			}
			
			
			/*
			 * Vor und Nachname des Kontakts werden befüllt.
			 */
			firstnameTextBox.setText(contactToDisplay.getFirstname());
			lastnameTextBox.setText(contactToDisplay.getLastname());
			
			
			/*
			 * Das Geschlecht des Kontaktes wird abgefragt und gesetzt.
			 */
			if (contactToDisplay.getSex() == "m") {
				sexListBox.setItemSelected(1, true);
			}
			else if(contactToDisplay.getSex() == "f"){
				sexListBox.setItemSelected(2, true);
			}
			else if(contactToDisplay.getSex() == "o") {
				sexListBox.setItemSelected(3, true);
			}
			sexListBox.setEnabled(false);
			
			
			/*
			 * Alle Ausprägungen des contactToDisplay werden ausgelesen und in einem Vector<Values> gespeichert.
			 */
			editorAdministration.getAllValuesOfContact(contactToDisplay, new AsyncCallback<Vector<Value>>() {
				public void onFailure(Throwable t) {
					Window.alert("Fehler beim Auslesen der Ausprägungen des Kontakts.");
					
				}
				public void onSuccess(Vector<Value> values) {
					allValuesOfContact = new Vector<Value>();
					for(Value v: values) {
						allValuesOfContact.add(v);
					}
					
					/*
					 * Außerdem wird für jede Ausprägung, die eine EigenschaftsID über 10 hat (d.h. es handelt sich
					 * nicht um Default-Eigenschaften, sondern um neu hinzugefügte), die jeweilige Eigenschaft dazu 
					 * ebenfalls aus der Datenbank ausgelesen. Dies geschieht in der inneren Klasse GetPropertyOfValueCallback.
					 * Diese ist nach der Methode setSelected(Contact c) definiert.
					 */
					for(Value v: allValuesOfContact) {
						if(v.getPropertyid() >10) {
							editorAdministration.getPropertyOfValue(v, new GetPropertyOfValueCallback(v));
						}else{
							if(v == allValuesOfContact.lastElement()) {
								/*
								 * Wenn die letzte Ausprägung aus dem Vektor ausgelesen wurde, wird die Methode 
								 * displayAllValuesOfContact aufgerufen, welche die Ausprägungen dann im GUI anzeigt. 
								 */
								displayAllValuesOfContact();
							}
						}
					}
				}
			});
			
			
		
		/*
		 * Wenn setSelected mit dem Argument "null" aufgerufen wird, so werden anstelle der Ausprägungen in die 
		 * bestehenden TextBoxen jeweils Placeholder gesetzt, die andeuten, was in diese TextBox einzutragen ist.
		 */
		}else {
			contactToDisplay = null;
			newPropertyPanel.setVisible(false);
			firstnameTextBox.getElement().setPropertyString("placeholder", "Vorname...");
			firstnameTextBox.setText("");
			lastnameTextBox.getElement().setPropertyString("placeholder", "Nachname...");
			lastnameTextBox.setText("");
		}
				
	}
	
	/**
	 * Die innere Klasse GetPropertyOfValueCallback vollzieht den Callback für das Auslesen einer
	 * selbst hinzugefügten Eigenschaft zu einer bestimmen Ausprägung. Alle solcher Eigenschaften 
	 * eines Kontakts werden dem Vektor allNewPropertiesOfContact hinzugefügt. 
	 * 
	 * @author KatrinZerfass
	 */
	public class GetPropertyOfValueCallback implements AsyncCallback<Property>{
		Value v;
		
		public GetPropertyOfValueCallback(Value v) {
			this.v=v;
		}
		
		public void onFailure(Throwable t) {
			Window.alert("Fehler beim Auslesen der neuen Eigenschaft.");
			
		}
		
		public void onSuccess(Property p) {
			allNewPropertiesOfContact.add(p);
			if(v == allValuesOfContact.lastElement()) {
				displayAllValuesOfContact();
				/* wenn es sich bei der Ausprägung um die letzte im Vektor handelte, wird die Methode
				 * displayAllValuesOfContact aufgerufen, welche nachfolgend die Ausprägungen im GUI anzeigt. 
				 */
				
			}
		}
	}
	
	public void displayAllValuesOfContact() {
		
		/*
		 * Der Vector allValuesOfContact, welcher alle Ausprägungen des anzuzeigenden Kontaktes enthält, wird durchiteriert
		 * und jede Ausprägung wird im dazugehörigen ValueDisplay der jeweiligen Eigenschaftsart angezeigt.
		 * 
		 * Bei den Eigenschaftsarten mit der P_ID 1, 2, 3, 5, und 10 können jeweils mehrere Ausprägungen vorhanden sein.
		 * Die genaue Funktionalität hierzu ist in "case 1" vollständig durchkommentiert, in den folgenden Cases verhält es sich 
		 * immer genau gleich. Der einzige Unterschied sind nur das ValuePanel und ValueTable, welche zu Beginn jedes Cases gesetzt werden
		 * und sich jeweils in einer anderen Zeile der contactTable befinden, je nachdem um welche Eigenschaftsart es sich handelt.
		 * 
		 */ 
		
		int row;
		int vtRow;
		
		ValuePanel vp = null; //das ValuePanel der jeweiligen Eigenschaftsart
		ValueTable vt = null;	 //die ValueTable der jeweiligen Eigenschaftsart
	
	
		
		for(int i=0; i<=allValuesOfContact.size(); i++) {
			int pid = allValuesOfContact.get(i).getPropertyid();
			
			String identifier = null;
			
			boolean isFirstValue = false;
			
			if(ClientsideFunctions.isOwner(contactToDisplay, currentUser)){
				if(i==0) {
					isFirstValue = true;
				}else if(i!=0 && allValuesOfContact.get(i-1).getPropertyid() != pid) {
					isFirstValue = true;
				}else {
					isFirstValue = false;
				}
			}else {
				if(i==0 && allValuesOfContact.get(i).getIsShared() ==true) {
					isFirstValue = true;
				}else if(i!=0 && allValuesOfContact.get(i-1).getPropertyid() != pid && allValuesOfContact.get(i).getIsShared() ==true) {
					isFirstValue = true;
				}else if(i!=0 && allValuesOfContact.get(i-1).getPropertyid() == pid &&  allValuesOfContact.get(i-1).getIsShared() == false && allValuesOfContact.get(i).getIsShared() ==true) {
					for(int j=1; j<=i; j++) {
						if(allValuesOfContact.get(i-j).getPropertyid() == pid) {
							if(allValuesOfContact.get(i-j).getIsShared() == true){
								isFirstValue = false;
							}else {
								isFirstValue = true;
							}
						}
					}
				
				}else {
					isFirstValue = false;
				}
			}
			
			String ptype = null;
			
			if(pid<=10) {
				for (Property p: allPredefinedProperties) {
					if(pid == p.getId()){
						ptype = p.getType();				
					}
				}
			}else {
				for(Property p: allNewPropertiesOfContact) {
					if (pid == p.getId()) {
						ptype = p.getType();
							
					}
				}
			}
		
		
			
			switch (pid) {
				
				case 1: // Tel.Nr. geschäftlich
						identifier = "Telefonnummer";
						
						break;
						
				
				case 2:  // Tel.Nr. privat
						identifier = "Telefonnummer";
						
						break;
						
				case 3:  // e-Mail
						identifier = "Email";
						break;
						
						
				case 4:  // Geburtstag
					identifier = "Geburtstag";
					if(ClientsideFunctions.isOwner(contactToDisplay, currentUser) || (!ClientsideFunctions.isOwner(contactToDisplay, currentUser) && allValuesOfContact.get(i).getIsShared()==true)) {
							/*
							 * Eine Ausprägung zu Geburtstag kann nur einmal vorhanden sein. Demzufolge gibt es hierfür auch keine ValueTable.
							 * Das ValueDisplay, in dem sich die TextBox für das Geburtsdatum befindet, wird direkt angesprochen.
							 */
							Label birthdateLabel = new Label(ptype + ": ");
							contactTable.setWidget(3, 2, birthdateLabel);
							
							contactTable.setWidget(3, 3, new ValueDisplay(new ValueTextBox(identifier)));
							((ValueDisplay) contactTable.getWidget(3, 3)).getWidget(0).setWidth("105px");
							((ValueDisplay) contactTable.getWidget(3,3)).setValue(allValuesOfContact.get(i));
							
							if (ClientsideFunctions.isOwner(contactToDisplay, currentUser)) {
								((ValueDisplay) contactTable.getWidget(3, 3)).enableButtons();
								
								if(allValuesOfContact.get(i).getIsShared() == true){
									((ValueDisplay) contactTable.getWidget(3, 3)).setLockButtonTo(true);
								}else {
									((ValueDisplay) contactTable.getWidget(3, 3)).setLockButtonTo(false);
								}
								
							}
							else {
								((ValueDisplay) contactTable.getWidget(3, 3)).disableButtons();
							}
							
							for(int c=0; c<newPropertyListBox.getItemCount(); c++) {
								if (newPropertyListBox.getItemText(c) == ptype) {
									newPropertyListBox.removeItem(c);
								}
							}
//							
						}
						break;
					
						
						
				case 5: // Arbeitsplatz
						identifier = "Arbeitsplatz";
						break;
						
				
				/*
				 * Bei der Anschrift kann ebenfalls nur eine Ausprägung vorhanden sein. Demzufolge wurden die ValueTextBoxen hierfür
				 * als Instanzenvariablen von ContactForm deklariert und können nun hier direkt angesprochen werden.
				 * Sie befinden sich alle in der umschließenden FlexTable adressTable.
				 */
				case 6:  // Straße
						
						row = contactTable.getRowCount();
						if(ClientsideFunctions.isOwner(contactToDisplay, currentUser) || (!ClientsideFunctions.isOwner(contactToDisplay, currentUser) && allValuesOfContact.get(i).getIsShared()==true)) {
						
							Label addressLabel = new Label("Anschrift: ");
							contactTable.setWidget(row, 0, addressLabel);
							
							contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
							contactTable.setWidget(row, 1, addressTable);
							
							streetTextBox = new ValueTextBox("Straße");
							houseNrTextBox = new ValueTextBox("Hausnummer");
							plzTextBox = new ValueTextBox("PLZ");
							cityTextBox = new ValueTextBox("Stadt");
				
							addressTable.setWidget(0, 0, streetTextBox);
							addressTable.setWidget(0, 1, houseNrTextBox);
							addressTable.setWidget(1, 0, plzTextBox);
							addressTable.setWidget(1, 1, cityTextBox);
								
							addressTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
							addressTable.setWidget(0, 2, new ValueDisplay(new ValueTextBox("")));
							((ValueDisplay) addressTable.getWidget(0, 2)).remove(0);
							
							/*
							 * Da es sich bei der Anschrift nicht um ValueDisplays handelt, muss auf die beiden Buttons seperat
							 * operiert werden. Ihnen wird jeweils die Straße als Ausprägung gesetzt, da es nur möglich ist, einen
							 * einzelnen Wert als Value zu setzten. Trotzdem operieren diese Buttons beim Klicken auf die gesamten
							 * vier Ausprägungen, die zur Anschrift gehören.
							 */
							streetTextBox.setValue(allValuesOfContact.get(i));
							((ValueDisplay) addressTable.getWidget(0, 2)).setValue(allValuesOfContact.get(i));
							
							
							if(ClientsideFunctions.isOwner(contactToDisplay, currentUser)) {
								((ValueDisplay) addressTable.getWidget(0, 2)).enableButtons();
//								((LockButton) addressTable.getWidget(0, 2)).setEnabled(true);
//								((DeleteValueButton) addressTable.getWidget(0,3)).setEnabled(true);
							}
							else {
								
								((ValueDisplay) addressTable.getWidget(0, 2)).disableButtons();
//								((LockButton) addressTable.getWidget(0, 2)).setEnabled(false);
//								((DeleteValueButton) addressTable.getWidget(0,3)).setEnabled(false);
							
							}
							
							for(int c=0; c<newPropertyListBox.getItemCount(); c++) {
								if (newPropertyListBox.getItemText(c) == "Anschrift") {
									newPropertyListBox.removeItem(c);
								}
							}
							
						}
						break;
		
				case 7:  // Hausnummer
						if(houseNrTextBox != null) {
							houseNrTextBox.setValue(allValuesOfContact.get(i));
						}
						break;
						
				
				case 8:  // PLZ
						if(plzTextBox != null) {
							plzTextBox.setValue(allValuesOfContact.get(i));
						}
						break;
				
				
				case 9:  // Wohnort
						if(cityTextBox != null) {
							cityTextBox.setValue(allValuesOfContact.get(i));
						}
						break;
						
				
				case 10:  // Homepage
						identifier = "Homepage";
						break;
		
				default: 
					
						identifier = "Sonstiges";	
				
						break;
						
					
				} //ende der switch case
			

			if(pid != 4 && pid!= 6 && pid!=7 && pid !=8 && pid!=9) {
				

				row = contactTable.getRowCount();

				if(ClientsideFunctions.isOwner(contactToDisplay, currentUser) || (!ClientsideFunctions.isOwner(contactToDisplay, currentUser) && allValuesOfContact.get(i).getIsShared()==true)) {

					
					if(isFirstValue) {
						contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						vp = (ValuePanel) contactTable.getWidget(row, 0);
						
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, new ValueTable(pid));
						vt = (ValueTable) contactTable.getWidget(row, 1);
					}else {
						vp = (ValuePanel) contactTable.getWidget(row-1, 0);
						vt = (ValueTable) contactTable.getWidget(row-1, 1);
					}

					
					vtRow = vt.getRowCount();
					vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox(identifier)));
					vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
					
					

					/*
					 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
					 */
					if (ClientsideFunctions.isOwner(contactToDisplay, currentUser)) {
						vt.getValueDisplay(vtRow).enableButtons();
					//	vp.getAddValueButton().setEnabled(true);
						vp.getAddValueButton().setVisible(true);
						
						if(allValuesOfContact.get(i).getIsShared() == true){
							vt.getValueDisplay(vtRow).setLockButtonTo(true);
						}else {
							vt.getValueDisplay(vtRow).setLockButtonTo(false);
						}
					}
					else {
						vt.getValueDisplay(vtRow).disableButtons();
					//	vp.getAddValueButton().setEnabled(false);
						vp.getAddValueButton().setVisible(false);
						
						
					}

				
					for(int c=0; c<newPropertyListBox.getItemCount(); c++) {
						if (newPropertyListBox.getItemText(c) == ptype) {
							newPropertyListBox.removeItem(c);
						}
					}
			
				}
		
			}//ende der for-schleife
		}
	}
	
//	private class GetPropertyOfValueCallback implements AsyncCallback<Property>{
//		int row;
//		int pid;
//		ValueTable vt;
//		ValuePanel vp;
//		Value value;
//		
//		public GetPropertyOfValueCallback(int row, int pid, ValueTable vt, ValuePanel vp, Value value) {
//			this.row=row;
//			this.pid = pid;
//			this.vt=vt;
//			this.vp=vp;
//			this.value = value;
//		}
//		
//		public void onFailure(Throwable t) {
//			
//		}
//		public void onSuccess(Property result) {
//			Window.alert("row im getpropertyofvaluecallback: " + ((Integer) row).toString());
//			//11
//			
//			if(contactTable.isCellPresent(row, 0)) {
//				if (contactTable.getWidget(row, 0) == null) {
//					contactTable.setWidget(row, 0, new ValuePanel(pid, row, result.getType() + ": "));
//					contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
//				}
//			}else {
//				Window.alert("else: neues Value Panel");
//				contactTable.setWidget(row, 0, new ValuePanel(pid, row, result.getType() + ": "));
//				contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
//			}
//			vp = (ValuePanel) contactTable.getWidget(row, 0);
//							
//			
//			if (contactTable.isCellPresent(row, 1)) {
//				if (contactTable.getWidget(row, 1) == null) {
//					contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
//					contactTable.setWidget(row, 1, new ValueTable(pid));
//				}
//				
//			}else {
//				contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
//				contactTable.setWidget(row, 1, new ValueTable(pid));
//			}
//			vt = (ValueTable) contactTable.getWidget(row, 1);
//			int vtRow = vt.getRowCount();
//			vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox("Sonstiges")));
//			vt.getValueDisplay(vtRow).setValue(value);
//			
//			
//			/*
//			 * Gleiches Prinzip wie gerade schon, nur jetzt für das soeben neu hinzugefügte ValueDisplay.
//			 */
//			if (compareUser()) {
//				vt.getValueDisplay(vtRow).enableButtons();
//				vp.getAddValueButton().setEnabled(true);
//			}
//			else {
//				vt.getValueDisplay(vtRow).disableButtons();
//				vp.getAddValueButton().setEnabled(false);
//			}
//			
//		}
//	}
//	
	
	
	/**
	 * Setzt das referenzierte TreeViewModel
	 *
	 * @param ContactListContactTreeViewModel das referenzierte TreeViewModel
	 */
	public void setClctvm(ContactListContactTreeViewModel clctvm) {
		this.clctvm= clctvm;
		
	}
	
	public VerticalPanel getButtonsPanel() {
		return this.buttonsPanel;
	}
	
	public HorizontalPanel getNewPropertyPanel() {
		return this.newPropertyPanel;
	}
	
//	public void disableRemoveContactButton() {
//		this.removeContactFromContactListButton.setVisible(false);
//	}
	
}

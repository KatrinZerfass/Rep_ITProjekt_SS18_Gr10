package com.google.gwt.sample.itProjekt.client;

import java.util.Vector;

import org.omg.CORBA.Current;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.sample.itProjekt.client.ContactForm.ValueDisplay;
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
 * Die Klasse ContactForm dient zur Darstellung von Kontakten mit all ihren Eigenschaften und deren Ausprägungen.
 * @author KatrinZerfass & JanNoller
 */

public class ContactForm extends VerticalPanel {
	
	
	EditorAdministrationAsync editorAdministration = ClientsideSettings.getEditorAdministration();
	
	ContactListContactTreeViewModel clctvm = null;
	
	/** Der anzuzeigende Kontakt */
	Contact contactToDisplay = null;
	
	/**Alle Ausprägungen des anzuzeigenden Kontakts*/
	Vector<Value> allValuesOfContact = new Vector<Value>();
	
	/**Alle vordefinierten Eigenschaften des Systems*/
	Vector<Property> allPredefinedProperties = new Vector<Property>();
	
	/**Alle von einem Nutzer neu hinzugefügten Eigenschaften*/
	Vector<Property> allNewPropertiesOfContact = new Vector<Property>();
	
	/**Ein Vector, in dem alle im Kontaktformular instantiierten ValueTextBoxes gespeichert werden. */
	Vector<ValueTextBox> allValueTextBoxes = new Vector<ValueTextBox>();
	
	/** Allumfassende Tabelle zur Darstellung von Kontakten */
	FlexTable contactTable = new FlexTable();

	HorizontalPanel newPropertyPanel = new HorizontalPanel();
	
	VerticalPanel buttonsPanel = new VerticalPanel();
	
	/* Für den Kontaktstamm */ 
	ValueTextBox firstnameTextBox = new ValueTextBox("Name");
	ValueTextBox lastnameTextBox = new ValueTextBox("Name");
	ListBox sexListBox = new ListBox();
	
	/** Tabelle, in der die Anschrift angezeigt wird */
	FlexTable addressTable = new FlexTable();
		
	/*ValueTextBoxen für die Anschrift*/
	ValueTextBox streetTextBox = new ValueTextBox("Straße");
	ValueTextBox houseNrTextBox = new ValueTextBox("Hausnummer");
	ValueTextBox plzTextBox = new ValueTextBox("PLZ");
	ValueTextBox cityTextBox = new ValueTextBox("Stadt");
	
	Label newPropertyLabel = new Label("Eigenschaft hinzufügen  ");
	Button addNewPropertyButton = new Button("Hinzufügen");
	
	/**Listbox für das Hinzufügen neuer Eigenschaften */
	ListBox newPropertyListBox = new ListBox();
	
	/**Der aktuell angemeldete Nutzer wird lokal zwischengespeichert.*/
	User currentUser = new User();
	
	/*
	 * Buttons, auf die später auch außerhalb der Methode onLoad zugegriffen werden muss (wegen disablen), 
	 * deshalb wurden sie als Instanzenvariablen deklariert,
	 */
	Button saveChangesButton;
	Button removeContactFromContactListButton;
	
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
		
		/**
		 * Der Konstruktor von LockButton. Es wird dem Button ein ClickHandler hinzugefügt, welcher den Übergang der Zustände von 
		 * <code>isLocked</code> bzw. von <code>isShared</code> (bezogen auf die Ausprägung) regelt.
		 */
		public LockButton() {
			
			this.addStyleName("lockButton");
			
			lockUnlocked.setPixelSize(17, 17);
			lockLocked.setPixelSize(17, 17);
			
			/*
			 * per default sind alle Ausprägungen geteilt, d.h. das Schloss ist zu Beginn unlocked.
			 */
			this.getUpFace().setImage(lockUnlocked);
			
			this.setEnabled(false);
			
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
		 * @param v die referenzierte Ausprägung
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
			
			if(value.getPropertyid() == 6) { 
				/*
				 * Es handelt sich um den LockButton, welcher sich auf die Straße bezieht. Folglich muss auch isShared der Ausprägungen
				 * Hausnummer, PLZ und Wohnort neu gesetzt werden.	
				 */
					for(int i= 0; i<allValuesOfContact.size(); i++) {
						int pid = allValuesOfContact.get(i).getPropertyid();
						if(pid == 6 || pid == 7 || pid == 8 || pid ==9) {
							editorAdministration.editValue(contactToDisplay, pid, allValuesOfContact.get(i), 
											allValuesOfContact.get(i).getContent(), false, new AsyncCallback<Value>() {
								public void onFailure(Throwable t) {
									Window.alert("Das Setzen dieser Ausprägung zu \"Nicht geteilt\" ist fehlgeschlagen.");
									
								}
								public void onSuccess(Value v) {
												
								}
							});
						}
					}			
				}
				
			else{
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
		 * Methode, die beim Klicken eines geschlossenen LockButtons aufgerufen wird. Folge: das Schloss wird geöffnet
		 * und die Variable <code>isShared</code> der Ausprägung wird auf <code>true</code> gesetzt. Diese Veränderung wird mit einem 
		 * Aufruf der Methode <code>editValue()</code> an den Server weitergegeben.
		 */
		public void setValueToShared() {
			this.getUpFace().setImage(lockUnlocked);
			isLocked = false;
			editorAdministration.editValue(contactToDisplay, value.getPropertyid(), this.value, value.getContent(), true, new AsyncCallback<Value>() {
				public void onFailure(Throwable t) {
					Window.alert("Das Setzen dieser Ausprägung zu \"Geteilt\" ist fehlgeschlagen.");
					
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
	 *  @author KatrinZerfass
	 */
	public class DeleteValueButton extends PushButton{
		
		/** Die Ausprägung, auf welche der jeweilige DeleteValueButton referenziert */
		private Value value;
		
		private Image bin = new Image("bin.png");
		
		
		/**
		 * Konstruktor von DeleteValueButton. Es wird dem Button ein ClickHandler hinzugefügt, welcher die Methode <code>deleteValue()</code> aufruft.
		 */
		public DeleteValueButton() {
			this.getUpFace().setImage(bin);
			bin.setPixelSize(17, 17);
			
			this.addStyleName("deleteValueButton");
			
			this.setEnabled(false);
			
			this.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					
					if(value.getPropertyid() == 6) { 
					/*
					 * Es handelt sich um den DeleteValueButton, welcher sich auf die Straße bezieht. Folglich müssen auch die Ausprägungen
					 * Hausnummer, PLZ und Wohnort mitgelöscht werden.	
					 */
						for(int i= 0; i<allValuesOfContact.size(); i++) {
							int pid = allValuesOfContact.get(i).getPropertyid();
							if(pid == 6 || pid == 7 || pid == 8 || pid ==9) {
								editorAdministration.deleteValue(allValuesOfContact.get(i), new AsyncCallback<Void>() {
								
									public void onFailure(Throwable t) {
										Window.alert("Das Löschen der Ausprägung ist fehlgeschlagen.");
									}
									
									public void onSuccess(Void result) {	
											Window.alert("Die Ausprägung wurde gelöscht.");
									}
								

								});
							}
						}			
					}
					
					else{
						editorAdministration.deleteValue(value, new AsyncCallback<Void>() {
					
							public void onFailure(Throwable t) {
								Window.alert("Das Löschen der Ausprägung ist fehlgeschlagen.");
							}
							
							public void onSuccess(Void result) {	
								Window.alert("Die Ausprägung wurde gelöscht.");
							}

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
	 * Die innere Klasse AddValueButton.
	 * Sie dient der Darstellung der Buttons, mit welchen man eine neue Ausprägung der jeweiligen Eigenschaft hinzufügen kann.
	 * 
	 *  @author KatrinZerfass
	 */
	public class AddValueButton extends Button{
		
		/** Die referenzierte Eigenschaftsart. */
		private int propertyId;
		/**Die Nummer der Zeile, in welcher der Button sich in der <code>contactTable</code> befindet */
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
			
			
		//wieder zurück kommentieren!
			this.setEnabled(true);
					
			this.addClickHandler(new ClickHandler() {
				
				public void onClick (ClickEvent event) {
					if(contactToDisplay != null) {
						addValuePopUp(propertyId, row);	
					
					}else { 
						Window.alert("geht in AddValueclickHandler rein. \nKatrin");
						int valueTableRow = ((ValueTable)contactTable.getWidget(row, 1)).getRowCount();
						Window.alert("Widget der contactTable an der Stelle " + row + ", 1: " + ((ValueTable) contactTable.getWidget(row, 1)).getPid() + "\nKatrin");
						Window.alert("Aktuelle Reihe der ValueTable: " + valueTableRow + "\nKatrin");
						
						switch(propertyId) {
						case 1: ((ValueTable) contactTable.getWidget(row, 1)).setWidget(valueTableRow, 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
								break;
						
						case 2: ((ValueTable) contactTable.getWidget(row, 1)).setWidget(((ValueTable) contactTable.getWidget(row, 1))
									.getRowCount(),0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
								break;
						case 3: ((ValueTable) contactTable.getWidget(row, 1)).setWidget(((ValueTable) contactTable.getWidget(row, 1))
									.getRowCount(),0, new ValueDisplay(new ValueTextBox("Email")));
								break;
						case 5: ((ValueTable) contactTable.getWidget(row, 1)).setWidget(((ValueTable) contactTable.getWidget(row, 1))
									.getRowCount(),0, new ValueDisplay(new ValueTextBox("Arbeitsplatz")));
								break;
						case 10: ((ValueTable) contactTable.getWidget(row, 1)).setWidget(((ValueTable) contactTable.getWidget(row, 1))
									.getRowCount(),0, new ValueDisplay(new ValueTextBox("Homepage")));
								break;
						default:
							if (propertyId>10) {
								((ValueTable) contactTable.getWidget(row, 1)).setWidget(((ValueTable) contactTable.getWidget(row, 1))
										.getRowCount(),0, new ValueDisplay(new ValueTextBox("")));
							}
							break;
					
						}
					}
				
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
		/**
		 * Die Methode AddValuePopUp wird im ClickHandler der AddValueButtons aufgerufen.
		 * 
		 * @param pid die referenzierte Eigenschaft, wird vom ClickHandler übergeben
		 */
		public void addValuePopUp(int pid, int row) {

			DialogBox addValuePopUp = new DialogBox();
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
			addValueDialogBoxPanel.add(addValueLabel);
			
			
			
			/*
			 * Je nachdem, um welche Art von Eigenschaft es sich handelt, wir ein anderer Text im Pop-Up angezeigt und dem ClickHandler
			 * des "Hinzufügen"-Buttons wird u.a. die jeweilige ValueTable übergeben, in die die Ausprägung hinzugefügt werden muss.
			 */
			switch(pid) {
			case 1: addValueTextBox = new ValueTextBox("Telefonnummer");
					addValueLabel.setText("Neue geschäftliche Telefonnummer: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(row, 1)), pid));
					break;
			case 2: addValueTextBox = new ValueTextBox("Telefonnummer");
					addValueLabel.setText("Neue private Telefonnummer: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(row, 1)), pid));
					break;
			case 3: addValueTextBox = new ValueTextBox("Email");
					addValueLabel.setText("Neue e-Mail-Adresse: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(row, 1)), pid));
					break;
			case 5: addValueTextBox = new ValueTextBox("Arbeitsplatz");
					addValueLabel.setText("Neue Arbeitsstelle: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(row, 1)), pid));
					break;
			case 10:addValueTextBox = new ValueTextBox("Homepage");
					addValueLabel.setText("Neue Homepage: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(row, 1)), pid));
					break;
			default: 
				if(pid>10) {
					addValueTextBox = new ValueTextBox("");
					addValueLabel.setText("Neue Ausprägung: ");
					addValueButton.addClickHandler(new AddValueClickHandler(addValuePopUp, addValueTextBox,
							((ValueTable) contactTable.getWidget(row, 1)), pid));
					break;
					
				}
			}
			
			addValueDialogBoxPanel.add(addValueTextBox);
			addValueDialogBoxPanel.add(addValueButton);
			addValuePopUp.add(addValueDialogBoxPanel);
			addValuePopUp.show();
			
			
		
		}
		
		/**
		 * Der innere Klasse AddValueClickHandler innerhalb der inneren Klasse AddValueButton.
		 * Wird aufgerufen, nachdem der Benutzer eine neue Ausprägung einträgt und im Popup auf "Hinzufügen" klickt.
		 *
		 * @author KatrinZerfass
		 */
		private class AddValueClickHandler implements ClickHandler {
			DialogBox popup;
			ValueTextBox tb;
			FlexTable vt;
			int pid;
			
			/**
			 * Der Konstruktor von AddValueClickHandler. Ihm müssen alle Parameter aus dem Popup übergeben werden, damit er in einem 
			 * Aufruf zum Server die neue Ausprägung anlegen und diese außerdem in der ValueTable anzeigen kann.
			 * 
			 * @param popup Das AddValuePopUp
			 * @param tb die TextBox aus dem PopUp
			 * @param ft die ValueTable, der die neue Ausprägung hinzugefügt werden soll
			 * @param pid die ID der Eigenschaftsart des AddValueButtons
			 * @param content der vom Nutzer eingetragene Inhalt der neuen Ausprägung
			 */
			public AddValueClickHandler(DialogBox popup, ValueTextBox tb, ValueTable vt, int pid) {
				this.popup = popup;
				this.tb = tb;
				this.vt = vt;
				this.pid = pid;
				Window.alert("AddValueclickhandler instantiiert \nKatrin");
						
			}
			
			public void onClick(ClickEvent event) {
				/*
				 * Die Eingabe des Nutzers wird mithilfe der Methode checkValue() auf Korrektheit überprüft.
				 */
				if(!checkValue(tb)){
					tb.setText("");				
				}
				else {
					popup.hide();
							
					editorAdministration.createValue(contactToDisplay, pid, tb.getText(), new AsyncCallback<Value>() {
						public void onFailure(Throwable t) {
							Window.alert("Ausprägung konnte nicht hinzugefügt werden. Versuchen Sie es erneut.");
						}
						public void onSuccess(Value v) {
							Window.alert("Vorname vom contacttoDisplay: " + contactToDisplay.getFirstname()
							+ "\n  Text aus der TextBox: " + tb.getText() + "\nKatrin");
							Window.alert("Value aus der Datenbank: " + v.getContent() + "\nKatrin");
							/*
							 * War das Anlegen der Ausprägung auf dem Server erfolgreich, so wird sie auch im GUI als neue Zeile in
							 * der jeweiligen ValueTable angezeigt.
							 */
							int rowCount = vt.getRowCount();
							Window.alert(tb.getIdentifier() + " \nKatrin");
							Window.alert(((Integer) rowCount).toString()+ "\nKatrin");
							vt.setWidget(rowCount, 0, new ValueDisplay(new ValueTextBox(tb.getIdentifier())));
							((ValueDisplay) vt.getWidget(rowCount ,0)).setValue(v);
							Window.alert("addvalueclikchandler durchgelaufen  \nKatrin");
						}
					});
				
				}
				
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
		 * Und fügt der TextBox den valueChangeHandler hinzu,  welcher den Zustand von <code>isChanged</code> ändert.
		 * @param identifier der Identifier
		 */
		public ValueTextBox(String identifier) {
	
			setIdentifier(identifier);
			allValueTextBoxes.add(this);
			this.setText("");
			
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
		 * Setter von <code>value</code>. Setzt auch gleichzeitig den Inhalt der Value als Text der TextBox und fügt die ValueTextBox dem
		 * Vector aller instantiierten ValueTextBoxes hinzu. (Wird null übergeben, dann wird der Text gelöscht und die TextBox aus dem
		 * Vector wieder entfernt. Dies passiert, wenn <code>setSelected(null)</code> aufgerufen wird.) 
		 * 
		 * @param v die anzuzeigende Ausprägung
		 */
		public void setValue(Value v) {
			Window.alert("Springt in die setValue der ValueTextBox \n Katrin");
			this.value = v;
			if(value!= null) {
				setText(value.getContent());
			//	allValueTextBoxes.add(this);
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
		 * @return der Identifier
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * der Setter des Identifiers
		 * @param identifier der Identifier
		 */
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
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
		
		/** Die anzuzeigende Ausprägung */
		private Value value = null;
		
		private ValueTextBox valueTextBox;
		private LockButton lockButton;
		private DeleteValueButton deleteValueButton;
		
		/**
		 * Konstruktor von ValueDisplay.  <code>ValueTextBox</code>, <code>LockButton</code> und <code>DeleteValueButton</code> werden dem Panel hinzugefügt. 
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
		 * Setter von <code>value</code>. Setzt die Ausprägung in alle Elemente des Displays, welche auf diese referenzieren.
		 * Fügt außerdem in der geschachtelten Methode valueTextBox.setValue() die TextBox dem Vector aller TextBoxen hinzu.
		 * 
		 * @param v die anzuzeigende Ausprägung
		 */
		public void setValue(Value v) {
			Window.alert("Springt in die setValue von ValueDisplay \n Katrin");
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
		
		/**
		 * Getter der <code>valueTextBox</code>
		 * @return
		 */
		public ValueTextBox getValueTextBox() {
			return this.valueTextBox;
		}
		/**
		 * Die Methode enableButton() wird aufgerufen, wenn der aktuelle Nutzer auch der Eigentümer des Kontakts ist.
		 */
		public void enableButtons() {
			this.deleteValueButton.setEnabled(true);
			this.lockButton.setEnabled(true);
		}
		
		/**
		 * Die Methode disableButton() wird aufgerufen, wenn der aktuelle Nutzer nur Teilhaber des Kontakts ist.
		 */
		public void disableButtons() {
			this.deleteValueButton.setEnabled(false);
			this.lockButton.setEnabled(false);
		}
			
	}
	
	
	/**
	 * Die innere Klasse ValuePanel.
	 * Sie dient der Darstellung des Eigenschaftslabels und des AddValueButtons in der jeweils ersten Spalte der <code>contactTable</code>.
	 *
	 * @author KatrinZerfass
	 */
	public class ValuePanel extends VerticalPanel{
		int propertyId;
		Label valueLabel = new Label();
		AddValueButton addValueButton;
		
		/**
		 * Der Konstruktor von ValuePanel. Das Label und der AddValueButton werden hinzugefügt und die Eigenschafts-ID wird gesetzt.
		 * @param pid die ID der referenzierten Eigenschaft
		 * @param row die Zeile, in der sich das Panel in der <code>contactTable</code> befindet
		 * @param label das Label, welches auf die Art der Eigenschaft hinweist
		 */
		public ValuePanel(int pid, int row, String label){
			this.propertyId= pid;
			this.valueLabel.setText(label);
			this.addValueButton = new AddValueButton(propertyId, row);
			this.add(valueLabel);
			this.add(addValueButton);
			
		}
		
		/**
		 * Getter des addValueButtons.
		 * @return den AddValueButton des ValuePanels
		 */
		public AddValueButton getAddValueButton() {
			return (AddValueButton) getWidget(1);
		}
		
	}
	
	
	
	
	/**
	 * Die innere Klasse ValueTable. Erbt von FlexTable.
	 * Sie dient der Darstellung aller ValueDisplays zu jeder Eigenschaftsart.
	 * 
	 *  @author KatrinZerfass
	 */
	public class ValueTable extends FlexTable{
		int propertyId;
		ValueDisplay valueDisplay;
		
		/**
		 * Konstruktor von ValueTable. 
		 * @param pid die ID der referenzierten Eigenschaftsart 
		 */
		public ValueTable(int pid) {
			this.propertyId=pid;
			
		}
		
		/**
		 * Getter für valueDisplay
		 * @param row die Zeile in der ValueTable
		 * @return das ValueDisplay an der Stelle <code>row, 0</code>
		 */
		public ValueDisplay getValueDisplay(int row) {
			return (ValueDisplay) getWidget(row,  0);
		}
		
		public int getPid() {
			return this.propertyId;
		}
	}
	
	
	/**
	 * Die innere Klasse EmailDialogBox
	 * 
	 * @author JanNoller
	 */
	
	public class EmailDialogBox extends DialogBox{
		
		private String email;
		
        private ValueTextBox tb = new ValueTextBox("Email");
		
		public EmailDialogBox() {
			setText("Teilen");
			setAnimationEnabled(true);
			setGlassEnabled(true);
			
			Window.alert("EmailDialogBox Konstruktor");
			
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
		    				editorAdministration.shareContact(currentUser, getEmail(), contactToDisplay, new AsyncCallback<Permission>() {
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

	
	
	/**
	 * Die Methode <code>onLoad()</code> wird in der EntryPoint-Klasse aufgerufen, um im GUI eine Instanz von ContactForm zu erzeugen.
	 */
	
	public void onLoad() {
		
		super.onLoad();
		
		/* 
		 * Zunächst wird der angemeldete Nutzer abgefragt und als Instanzenvariable gespeichert.
		 */
		
		currentUser = ClientsideSettings.getUser();
		
		this.add(contactTable);
		
		editorAdministration.getAllPredefinedPropertiesOf(new AsyncCallback<Vector<Property>>(){
			public void onFailure(Throwable t) {
				Window.alert("Auslesen aller vordefinierten Eigenschaften fehlgeschlagen");
				
			}
			
			public void onSuccess(Vector<Property> properties) {
				for (Property p : properties){
					allPredefinedProperties.add(p);
				}
			}
		});
		
		
		
		//Zeile 0 und 1 der Tabelle contactTable sind leer
		contactTable.setStyleName("contactTable");
		
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
	
		
		Button addContactButton = new Button("Neuen Kontakt anlegen");
		Button shareContactButton = new Button("Kontakt teilen");
		Button deleteContactButton = new Button("Kontakt löschen");
		saveChangesButton = new Button("Änderungen speichern");
		Button addContactToContactListButton = new Button("Kontakt zu einer Kontaktliste hinzufügen");
		removeContactFromContactListButton = new Button("Kontakt aus der aktuellen Kontaktliste entfernen");
		
		buttonsPanel.add(addContactButton);
		buttonsPanel.add(shareContactButton);
		buttonsPanel.add(deleteContactButton);
		buttonsPanel.add(addContactToContactListButton);
		buttonsPanel.add(removeContactFromContactListButton);
		buttonsPanel.add(saveChangesButton);
		
		buttonsPanel.setStyleName("buttonsPanel");
		
		
		
		
		
		
		/*
		 * Hinzufügen der ClickHandler zu den Buttons
		 */
		addContactButton.addClickHandler(new NewContactClickHandler());
		
		shareContactButton.addClickHandler(new ShareContactClickHandler());
		
		deleteContactButton.addClickHandler(new DeleteContactClickHandler());
		
		saveChangesButton.addClickHandler(new SaveChangesClickHandler());
		
		addContactToContactListButton.addClickHandler(new AddContactToContactListClickHandler());
		
		removeContactFromContactListButton.addClickHandler(new RemoveContactFromContactListClickHandler());
			
		addNewPropertyButton.addClickHandler(new NewPropertyClickHandler());
	
	} //Ende von onLoad()
	
	
	/*
	 * Im Folgenden sind alle soeben hinzugefügten ClickHandler als eigene innere Klassen definiert.
	 */
	
	/**
	 * Die innere Klasse NewContactClickHandler.
	 * 
	 * @author KatrinZerfass
	 */
	private class NewContactClickHandler implements ClickHandler{
		DialogBox db = new DialogBox();
		
		public void onClick(ClickEvent event) {
			/*
			 * Ein neuer Button, der oben rechts erscheint, wenn man einen neuen Kontakt anlegen will.
			 */
//			contactTable.getFlexCellFormatter().setRowSpan(2, 4, 2);
//			Button addContactButton = new Button("Kontakt-stamm anlegen");
//			addContactButton.setWidth("60px");
//			contactTable.setWidget(2, 4, addContactButton);
			
			/*
			 * Bevor ein neuer Kontakt angelegt werden kann, muss der bestehende Kontakt aus dem Formular genommen werden.
			 */
			if(contactToDisplay != null) {
				clearContactForm();
			}
			
			/*
			 * Die Dialogbox, die dem Benutzer sagt, was er tun muss, um einen neuen Kontakt anzulegen, wird konfiguriert.
			 */
			VerticalPanel vp = new VerticalPanel();
			Label label = new Label("Tragen Sie im Formular die Daten des Kontakts ein und klicken Sie anschließend auf \"Änderungen speichern\".");
			Button ok = new Button("Ok");
			
			vp.add(label);
			vp.add(ok);
			vp.setCellHorizontalAlignment(ok, ALIGN_RIGHT);
			db.setTitle("Neuen Kontakt anlegen");
			db.add(vp);
			db.setWidth("250px");
			db.setPopupPosition(500, 200);
			db.show();
		
			ok.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					db.hide();
				}
			});
			
//			/*
//			 * Mit Klick auf den neu entstandenen Button wird der Kontaktstamm im System angelegt. Anschließend wird der Kontakt selektiert.
//			 */
//			addContactButton.addClickHandler(new ClickHandler() {
//				public void onClick(ClickEvent event){
//					editorAdministration.createContact(firstnameTextBox.getText(),
//							lastnameTextBox.getText(), sexListBox.getValue(0).toString(), new AsyncCallback<Contact>() {
//						
//						public void onFailure(Throwable caught) {
//							Window.alert("Fehler beim Kontakt anlegen!");
//							
//						}
//						
//						
//						public void onSuccess(Contact result) {
//							clctvm.addContactOfContactList(clctvm.getMyContactsContactList(), result);
//							Window.alert("Kontakt wurde erfolgreich angelegt.");
//							
//						}
//					});
//				}
//			});		
//	
		}
	}
	
	
	
	/**
	 * Die innere Klasse shareContactClickHandler.
	 * 
	 *  @author JanNoller
	 */
	private class ShareContactClickHandler implements ClickHandler{
		
		@Override
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt!");
			}
			else {
				/*
				 * Über eine Instanz der inneren Klasse EmailDialogBox können Objekte mit anderen Nutzern geteilt werden.
				 */
				EmailDialogBox dialog = new EmailDialogBox();
				Window.alert("EmailDialogBox instanziert");
				dialog.show();
			}
		}
	}
	
	/**
	 * Die innere Klasse deleteContactClickHandler.
	 * 
	 * @author JanNoller
	 */
	private class DeleteContactClickHandler implements ClickHandler{
		
		@Override
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt");
			}
			else {	
				if (compareUser()) {
					editorAdministration.deleteContact(contactToDisplay.getId(), new AsyncCallback<Void>() {
						public void onFailure(Throwable arg0) {
							Window.alert("Fehler beim Löschen des Kontakts!");
						}
						public void onSuccess(Void arg0) {
							Window.alert("Kontakt erfolgreich gelöscht.");
							clctvm.removeContactOfContactList(clctvm.getSelectedContactList(), contactToDisplay);
						}
					});
				}
				else {
					editorAdministration.deletePermission(currentUser, contactToDisplay, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable arg0) {
							Window.alert("Fehler beim Löschen der Permission!");	
						}
						@Override
						public void onSuccess(Void arg0) {
							Window.alert("Kontakt-Teilhaberschaft erfolgreich entfernt.");
							clctvm.removeContactOfContactList(clctvm.getSelectedContactList(), contactToDisplay);
							
						}
					});
				}
				clearContactForm();
			}
		}
	}

	
	/**
	 * Die innere Klasse saveChangesClickHandler.#
	 * 
	 * @author JanNoller
	 */
	private class SaveChangesClickHandler implements ClickHandler{
		Contact newContact = new Contact();
		
		@Override
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				if(firstnameTextBox.getText() == "" && lastnameTextBox.getText() == "") {
					Window.alert("kein Kontakt ausgewählt");
				}else {
					if(!checkValue(firstnameTextBox) || !checkValue(lastnameTextBox) ) {
						firstnameTextBox.setText("");
						lastnameTextBox.setText("");
						Window.alert("Ihr Kontakt konnte nicht angelegt werden, bitte versuchen Sie es erneut.");
						
					}else if (checkValue(firstnameTextBox) && checkValue(lastnameTextBox) ) {
						String sex = "o";
						switch(sexListBox.getSelectedItemText()) {
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
								Window.alert("Fehler im Kontakt anlegen");
								
							}
							public void onSuccess(Contact result) {
								newContact= result;
								Window.alert("Kontakt erfolgreich angelegt.");
								clctvm.addContactOfContactList(clctvm.getMyContactsContactList(), result);
							}
						});
						
					
//						for (final ValueTextBox vtb : allValueTextBoxes) {
//							if ((!vtb.equals(firstnameTextBox) || !vtb.equals(lastnameTextBox)) && vtb.getText() != "") {
//								editorAdministration.createValue(newContact, 
//										((ValueTable) ((ValueDisplay) vtb.getParent()).getParent()).getPid(), vtb.getText(), new AsyncCallback<Value>() {
//									public void onFailure(Throwable t) {
//										Window.alert("Fehler beim Anlegen einer Ausprägung");
//										
//									}
//									public void onSuccess(Value result) {
//										vtb.setValue(result);
//										//geht final hier?
//									}
//								});
//							}
//							
//							
//						}
						
					}
					
					
				}
					
			}
			else {
				for(ValueTextBox vtb : allValueTextBoxes) {
					/*
					 * Wenn in einer ValueTextBox der Inhalt verändert wurde, so wird für diese Ausprägung die Methode editValue() aufgerufen.
					 */
					if (vtb.getIsChanged() && vtb.getTextBoxValue() != null) {
						editorAdministration.editValue(contactToDisplay, vtb.getTextBoxValue().getPropertyid(), vtb.getTextBoxValue(), vtb.getTextBoxValue().getContent(), 
							vtb.getTextBoxValue().getIsShared(), new AsyncCallback<Value>() {
							
							public void onFailure(Throwable arg0) {	
								Window.alert("Fehler beim Änderungen speichern. \n (Ausprägungen)");
							}
							public void onSuccess(Value arg0) {
								Window.alert("Die Änderungen wurden gespeichert.\n (Ausprägungen)");
							}
						});
					}
					
					/*
					 * Wenn es sich bei der ValueTextBox um die firstnameTextBox oder lastnameTextBox handelt, wurde eine Veränderung am Kontaktstamm 
					 * vorgenommen und demzufolge wird die Methode editContact aufgerufen.
					 */
					else if(vtb.getIsChanged() && (vtb.equals(firstnameTextBox) ||
								vtb.equals(lastnameTextBox))){
						editorAdministration.editContact(contactToDisplay.getId(), firstnameTextBox.getText(), lastnameTextBox.getText(), 
							contactToDisplay.getSex(), new AsyncCallback<Contact>() {
	
							public void onFailure(Throwable arg0) {
								Window.alert("Fehler beim Änderungen speichern. \n (Kontaktstamm)");
							}
							public void onSuccess(Contact arg0) {
								Window.alert("Die Änderungen wurden gespeichert. \n (Kontaktstamm)");
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
	
	/**
	 * Die innere Klasse AddContactToContactListClickHandler.
	 * 
	 * @author JanNoller (& KatrinZerfass)
	 */
	private class AddContactToContactListClickHandler implements ClickHandler {
		
		DialogBox db = new DialogBox();
	
		ListBox listbox = new ListBox();
		VerticalPanel panel= new VerticalPanel();
		ContactList choice;
		
        Vector<ContactList> contactLists;
		
		public void onClick(ClickEvent event) {
			
			if(contactToDisplay ==null) {
				Window.alert("kein Kontakt ausgewählt");
			}
			else {
		
				panel.setHeight("100");
		        panel.setWidth("300");
		        panel.setSpacing(10);
		        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		        
		        Label label = new Label("Bitte wählen Sie die Kontaktliste aus.");
		        final Button ok = new Button("OK");
		        
		        panel.add(label);
		        panel.add(listbox);
		        panel.add(ok);
		        
		        db.add(panel);
		    	db.show();
	
		        editorAdministration.getAllOwnedContactListsOfActiveUser(currentUser, new AsyncCallback<Vector<ContactList>>() {
		        	
		        	public void onFailure(Throwable t) {
		        		Window.alert("Fehler beim Abruf der Kontaklisten des Nutzers");
		        	}
		        	
		        	public void onSuccess(Vector<ContactList> result) {
		        		contactLists = result;
		        		for (ContactList cl : contactLists) {
				        	listbox.addItem(cl.getName());
				        }
		        		ok.addClickHandler(new ClickHandler() {
				        	
				        	public void onClick(ClickEvent event) {
				        		for (ContactList cl : contactLists) {
				        			if (listbox.getSelectedItemText() == cl.getName()) {
				        				choice = cl;
				        			}
				        		} 
				        		db.hide();
				        		
				        		editorAdministration.addContactToContactList(choice, contactToDisplay, new AsyncCallback<ContactList>() {
				        			
				        			public void onFailure(Throwable z) {
				        				Window.alert("Fehler beim Hinzufügen des Kontakts zur Kontaktliste!");
				        			}
				        			
				        			public void onSuccess(ContactList result) {
				        				Window.alert("Kontakt zur Kontaktliste hinzugefügt.");
				        			}
								});
				        	}
				        });
		        	}
				});
		        
		        /*
		         * In der Dialogbox soll eine ListBox angezeigt werden, die alle Kontaktlisten des Nutzers beinhaltet.
		         */
		        
		        
		       /*
		        * Hat der Nutzer eine Kontaktliste ausgewählt und klickt "OK", so wird der Kontakt dieser Kontaktliste hinzugefügt.
		        */
		        
			}
	        
		}
	}
	
//	private class AddContactToContactListCallback implements AsyncCallback<ContactList>{
//		
//		Button ok = null;
//		
//		AddContactToContactListCallback(Button b){
//			ok=b;
//		}
//
//		@Override
//		public void onFailure(Throwable arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onSuccess(ContactList arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
	
	/**
	 * Die innere Klasse RemoveContactFromContactListClickHandler.
	 * 
	 * @author JanNoller
	 */
	private class RemoveContactFromContactListClickHandler implements ClickHandler{
		public void onClick(ClickEvent event) {
			clearContactForm();
			editorAdministration.removeContactFromContactList(clctvm.getSelectedContactList(), contactToDisplay, new AsyncCallback<ContactList>() {
				@Override
				public void onFailure(Throwable arg0) {	
					Window.alert("Fehler beim entfernen des Kontakts aus der Kontaktliste!");
				}
				@Override
				public void onSuccess(ContactList arg0) {
					Window.alert("Kontakt erfolgreich aus Kontaktliste entfernt.");
					clctvm.removeContactOfContactList(arg0, contactToDisplay);
				}
			});
		}
	}
	
	private class NewPropertyClickHandler implements ClickHandler{
		DialogBox db = new DialogBox();
		TextBox newPropertyTextBox;
		int pid;
		String ptype;
		int row;
		
		
		public void onClick(ClickEvent event) {
			ptype = newPropertyListBox.getSelectedItemText();
			row = contactTable.getRowCount();
			
			if(ptype == "Sonstiges") {
				newPropertyTextBox = new TextBox();
				db.show();
				VerticalPanel dbPanel = new VerticalPanel();
				db.setText("Neue Eigenschaftsart hinzufügen");
				
				Button addPropertyButton = new Button("Eigenschaftsart anlegen");
				dbPanel.add(newPropertyTextBox);
				dbPanel.add(addPropertyButton);
				db.add(dbPanel);
				
					
				addPropertyButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						db.hide();
						
						editorAdministration.createProperty(contactToDisplay, newPropertyTextBox.getText(), new AsyncCallback<Property>() {
							public void onFailure (Throwable t) {
								Window.alert("Eigenschaft anlegen gescheitert");
							}
							
							public void onSuccess(Property result) {
								ptype = result.getType();
								pid = result.getId();
								Window.alert("Eigenschaft anlegen erfolgreich");
								contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
								contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
								
								contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
								contactTable.setWidget(row, 1, new ValueTable(pid));
								Window.alert("Reihe der neu hinzugefügten Eigenschaft: " +((Integer) row).toString() + "\nKatrin");
								Window.alert("Pid der neuen ValueTable" + ((Integer) ((ValueTable)contactTable.getWidget(row, 1)).getPid()).toString() + "\nKatrin");
							}
						
						});
					
					}
				});
				
			}else {	
				editorAdministration.getPropertyByType(ptype, new AsyncCallback<Property>() {
					public void onFailure (Throwable t) {
						
					}
					
					public void onSuccess(Property result) {
						pid = result.getId();
						contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, new ValueTable(pid));
					}
				});
			}
			
			

		}
	}
	
	/**
	 * Die Methode compareUser() vergleicht den aktuell angemeldeten Nutzer mit dem Eigentümer des Kontakts.
	 * 
	 * @return true= Eigentümer oder false= Teilhaber
	 * @author JanNoller
	 */
	public boolean compareUser () {
			
			if (currentUser.getId() == contactToDisplay.getOwner()) {
				return true;
			}
			else {
				return false;
			}
		}
	
	/**
	 * Die Methode checkValue() überprüft die Eingabe einer Ausprägung in eine ValueTextBox auf Korrektheit.
	 * Enthält die Eingabe nicht mit der Eigenschaftsart übereinstimmende Zeichen, so muss der Wert erneut eingegeben werden.
	 * 
	 * @param vtb die ValueTextBox, deren Wert überprüft wird
	 * @return true= Eingabe passt oder false= Eingabe passt nicht
	 * @author JanNoller
	 */
	public boolean checkValue (ValueTextBox vtb) {
		
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
				if (text.matches("(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?")) {
					return true;
				}
				else {
					Window.alert("Ungültige Homepage!");
					return false;
				}
				
			case "Arbeitsplatz":
				if (text.matches("[\\w|\\s]*")) {
					return true;
				}
				else {
					Window.alert("Ungültige Zeichen im Arbeitgebernamen!");
					return false;
				}
				
			default: 
				Window.alert("Switch case hat nicht ausgelöst!");
				return false;
		}
		
	}
	
	
	
	
	
	/**
	 * Die Methode clearContactFrom() wird aufgerufen, wenn der addContactButton gedrückt wird. Die Felder leeren sich und ein neuer Kontakt
	 * kann eingetragen werden.
	 * 
	 */
	public void clearContactForm() {
		setSelected(null);
	}
	
	
	
	/**
	 * Zeigt den selektierten Kontakt an.
	 *
	 * @param c der selektierte Kontakt
	 * @author KatrinZerfass & JanNoller
	 */
	public void setSelected(Contact c) {
		Window.alert("setSelected von ContactForm \nKatrin");
		
		if(newPropertyPanel.isVisible()==true) {
			newPropertyPanel.setVisible(false);
		}
		
		for (int i = 4; i < contactTable.getRowCount(); i++) {
			for (int a =0; a<4; a++) {
				if(contactTable.isCellPresent(i, a)) {
					contactTable.removeCell(i, a);
				}
			}
		}
		
		if(contactTable.isCellPresent(3, 2)) {
			contactTable.removeCell(3, 2);
		}
		
		if(contactTable.isCellPresent(3, 3)) {
			contactTable.removeCell(3, 3);
		}
		
		// allValuesOfContact = null;
		
		/*
		 * Bei jedem neuen Aufruf von setSelected werden die ausgefüllten ValueTextBoxen geleert und aus dem Vector alle TextBoxen entfernt.
		 */
		
		for(ValueTextBox vtb : allValueTextBoxes) {
			if(vtb.getTextBoxValue() !=null) {
				vtb.setValue((Value) null);
			}
		}
		
	//	allValueTextBoxes = null;
		Window.alert("alles anfängliche von setSelected durchgelaufen. \nKatrin");
		if (c != null){
			
			contactToDisplay = c;
			
			Window.alert("Kontakt ungleich null. Ausgewählter Kontakt: " + contactToDisplay.getFirstname() + "\nKatrin");
			/*
			 * Befüllen der Eigenschaften aus der Datenbank
			 */
			
			newPropertyPanel.setVisible(true);
			
			for (Property p : allPredefinedProperties) {
				newPropertyListBox.addItem(p.getType());
			}
			newPropertyListBox.addItem("Sonstiges");	
			
			
			newPropertyPanel.add(newPropertyLabel);
			newPropertyPanel.add(newPropertyListBox);
			newPropertyPanel.addStyleName("propertyPanel");
			newPropertyPanel.add(addNewPropertyButton);
			
			
			firstnameTextBox.setEnabled(true);
			lastnameTextBox.setEnabled(true);
			saveChangesButton.setEnabled(true);
			removeContactFromContactListButton.setEnabled(true);
			
			
			
			
			/*
			 * Der angemeldete Nutzer wird mit dem Eigentümer verglichen. Ist er ausschließlich Teilhaber, so werden bestimmte Buttons
			 * bereits im Vorfeld ausgegraut.
			 */
		
			
			if(!compareUser()) {
				saveChangesButton.setEnabled(false);
				removeContactFromContactListButton.setEnabled(false);
				firstnameTextBox.setEnabled(false);
				lastnameTextBox.setEnabled(false);
				Window.alert("buttons wurden ausgegraut, weil der User nicht der Eigentümer ist. \nKatrin");
			}
			
			/*
			 * Alle Ausprägungen des contactToDisplay werden ausgelesen und in einem Vector<Values> gespeichert.
			 */
			editorAdministration.getAllValuesOf(contactToDisplay, new AsyncCallback<Vector<Value>>() {
				public void onFailure(Throwable t) {
					Window.alert("Fehler beim Auslesen der Ausprägungen des Kontakts");
					
				}
				public void onSuccess(Vector<Value> values) {
					Window.alert("onsuccess von getAllValuesOf . \nKatrin");
					allValuesOfContact = new Vector<Value>();
					for(Value v: values) {
						allValuesOfContact.add(v);
					}
					displayAllValuesOfContact();
					
					Window.alert("Alle Ausprägungen des Kontaktes ausgelesen. \nKatrin");
				}
			});
			
			
			/*
			 * Vor und Nachname des Kontakts werden gesetzt und die TextBoxen dem Vector aller ValueTextBoxen hinzugefügt.
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
		 * Wenn setSelected mit dem Parameter "null" aufgerufen wird, so werden anstelle der Ausprägungen in die einzelnen TextBoxen jeweils
		 * Placeholder gesetzt, die andeuten, was in diese TextBox einzutragen ist.
		 */
		}else {
			firstnameTextBox.getElement().setPropertyString("placeholder", "Vorname...");
			firstnameTextBox.setText("");
			lastnameTextBox.getElement().setPropertyString("placeholder", "Nachname...");
			lastnameTextBox.setText("");
//			((ValueDisplay) contactTable.getWidget(3,3)).getValueTextBox().getElement().setPropertyString("placeholder", "Geburtsdatum...");			
//			streetTextBox.getElement().setPropertyString("placeholder", "Straße...");
//			houseNrTextBox.getElement().setPropertyString("placeholder", "Hausnummer...");
//			plzTextBox.getElement().setPropertyString("placeholder", "PLZ...");
//			cityTextBox.getElement().setPropertyString("placeholder", "Wohnort...");
//			((ValueTable) contactTable.getWidget(5, 1)).getValueDisplay(0).getValueTextBox().getElement().setPropertyString("placeholder", "Private Nummer...");		
//			((ValueTable) contactTable.getWidget(6, 1)).getValueDisplay(0).getValueTextBox().getElement().setPropertyString("placeholder", "Geschäftl. Nummer...");
//			((ValueTable) contactTable.getWidget(7, 1)).getValueDisplay(0).getValueTextBox().getElement().setPropertyString("placeholder", "e-Mail-Adresse...");
//			((ValueTable) contactTable.getWidget(8, 1)).getValueDisplay(0).getValueTextBox().getElement().setPropertyString("placeholder", "Homepage...");
//			((ValueTable) contactTable.getWidget(9, 1)).getValueDisplay(0).getValueTextBox().getElement().setPropertyString("placeholder", "Arbeitsstelle...");
		}
				
	}
	
	public void displayAllValuesOfContact() {
		
		allValueTextBoxes = new Vector<ValueTextBox>();
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
		Window.alert("Size vom allValuesOfContact: " + ((Integer) allValuesOfContact.size()).toString() + ". \nKatrin");
		for(int i=0; i<allValuesOfContact.size(); i++) {
			int pid = allValuesOfContact.get(i).getPropertyid(); 
			ValuePanel vp = null; //das ValuePanel der jeweiligen Eigenschaftsart
			ValueTable vt = null; //die ValueTable der jeweiligen Eigenschaftsart
			int row = contactTable.getRowCount();
			
			switch (pid) {
				
				case 1: // Tel.Nr. geschäftlich
						/*
						 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
						 */
						vp = new ValuePanel(pid, row, "Geschäftliche Telefonnummer: ");
						contactTable.setWidget(row, 0, vp);
						contactTable.getFlexCellFormatter().setVerticalAlignment(6, 0, ALIGN_TOP);
						
						vt = new ValueTable(pid);
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, vt);
						int vtRow = vt.getRowCount();
						
						/*
						 * Ist noch keine Ausprägung im ersten (bereits im GUI bestehenden) ValueDisplay gesetzt worden, so passiert dies nun.
						 */
						if(vt.getValueDisplay(0) == null) {
							vt.setWidget(0, 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
							vt.getValueDisplay(0).setValue(allValuesOfContact.get(i));
							Window.alert("ValueDisplay in der ValueTable wurde gesetzt. \nKatrin");
						
							/*
							 * Je nachdem, ob der angemeldete Nutzer der Eigentümer des Kontakts ist oder nicht, werden die Buttons 
							 * AddValueButton, LockButton, und DeleteValueButton aktiviert oder nicht.
							 */
							if(compareUser()) {
								vt.getValueDisplay(0).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(0).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
							
						/*
						 * Das erste ValueDisplay wurde bereits befüllt. 
						 * Gibt es nun mehrere Ausprägungen zu geschäftlichen Telefonnummern, wird eine neue Zeile in der 
						 * zugehörigen ValueTable vt erstellt und dieser ebenfalls ein ValueDisplay hinzugefügt. In dieses ValueDisplay
						 * wird die Ausprägung gesetzt.
						 */
						}else {
							vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
							vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
							
							/*
							 * Gleiches Prinzip wie gerade schon, nur jetzt für das soeben neu hinzugefügte ValueDisplay.
							 */
							if (compareUser()) {
								vt.getValueDisplay(vtRow).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(vtRow).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}
						Window.alert("case 1 durchgelaufen \nKatrin");
						break;
						
				
				case 2:  // Tel.Nr. privat
						contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Private Telefonnummer: "));
						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						vp = (ValuePanel) contactTable.getWidget(row, 0); 
						
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, new ValueTable(pid));
						vt = (ValueTable) contactTable.getWidget(row, 1); 
						
						if(vt.getValueDisplay(0) == null) {
							vt.setWidget(0, 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
							vt.getValueDisplay(0).setValue(allValuesOfContact.get(i));
						
							if(compareUser()) {
								vt.getValueDisplay(0).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(0).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}else {
							/*
							 * Es gibt mehrere Ausprägungen zu privaten Telefonnummern.
							 */
							vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
							vt.getValueDisplay(vt.getRowCount()).setValue(allValuesOfContact.get(i));
							
							if (compareUser()) {
								vt.getValueDisplay(vt.getRowCount()).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(vt.getRowCount()).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}
						break;
						
				
				case 3:  // e-Mail
						contactTable.setWidget(row, 0, new ValuePanel(pid, row, "e-Mail-Adressen "));
						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						vp = (ValuePanel) contactTable.getWidget(row, 0); 
						
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, new ValueTable(pid));
						vt = (ValueTable) contactTable.getWidget(row, 1);
						
						if(vt.getValueDisplay(0).getValue() == null){
							vt.getValueDisplay(0).setValue(allValuesOfContact.get(i));
						
							if(compareUser()) {
								vt.getValueDisplay(0).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(0).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}else {
							/*
							 * Es gibt mehrere Ausprägungen zu e-Mail-Adressen.
							 */
							vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Email")));
							vt.getValueDisplay(vt.getRowCount()).setValue(allValuesOfContact.get(i));
							
							if(compareUser()) {
								vt.getValueDisplay(vt.getRowCount()).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(vt.getRowCount()).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}
						break;
						
						
				case 4:  // Geburtstag
						/*
						 * Eine Ausprägung zu Geburtstag kann nur einmal vorhanden sein. Demzufolge gibt es hierfür auch keine ValueTable.
						 * Das ValueDisplay, in dem sich die TextBox für das Geburtsdatum befindet, wird direkt angesprochen.
						 */
						Label birthdateLabel = new Label("Geburtsdatum: ");
						contactTable.setWidget(3, 2, birthdateLabel);
						
						contactTable.setWidget(3, 3, new ValueDisplay(new ValueTextBox("Geburtstag")));
						((ValueDisplay) contactTable.getWidget(3, 3)).getWidget(0).setWidth("105px");
						((ValueDisplay) contactTable.getWidget(3,3)).setValue(allValuesOfContact.get(i));
						break;
						
						
				case 5: // Arbeitsplatz
						contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Arbeitsplatz "));
						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						vp = (ValuePanel) contactTable.getWidget(row, 0);
						
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, new ValueTable(pid));
						vt = (ValueTable) contactTable.getWidget(row, 1);
						
						if(vt.getValueDisplay(0).getValue() == null){
							vt.getValueDisplay(0).setValue(allValuesOfContact.get(i));
						
							if(compareUser()) {
								vt.getValueDisplay(0).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(0).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}else {
							/*
							 * Es gibt mehrere Ausprägungen zu Arbeitsplatz.
							 */
							vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Arbeitsplatz")));
							vt.getValueDisplay(vt.getRowCount()).setValue(allValuesOfContact.get(i));
							
							if(compareUser()) {
								vt.getValueDisplay(vt.getRowCount()).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(vt.getRowCount()).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}
						break;
						
				
				/*
				 * Bei der Anschrift kann ebenfalls nur eine Ausprägung vorhanden sein. Demzufolge wurden die ValueTextBoxen hierfür
				 * als Instanzenvariablen von ContactForm deklariert und können nun hier direkt angesprochen werden.
				 * Sie befinden sich alle in der umschließenden FlexTable adressTable.
				 */
				case 6:  // Straße

						
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
						 * Da es sich bei der Anschrift nicht um ValueDisplays handelt, muss auf die beiden Buttons seperat
						 * operiert werden. Ihnen wird jeweils die Straße als Ausprägung gesetzt, da es nur möglich ist, einen
						 * einzelnen Wert als Value zu setzten. Trotzdem operieren diese Buttons beim Klicken auf die gesamten
						 * vier Ausprägungen, die zur Anschrift gehören.
						 */
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
						contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Homepages: "));
						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						vp = (ValuePanel) contactTable.getWidget(row, 0); 
						
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, new ValueTable(pid));
						vt = (ValueTable) contactTable.getWidget(row, 1);
						
						if(vt.getValueDisplay(0).getValue() == null){
							vt.getValueDisplay(0).setValue(allValuesOfContact.get(i));
						
							if(compareUser()) {
								vt.getValueDisplay(0).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(0).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						 }else {
							 /*
							 * Es gibt mehrere Ausprägungen zu Homepage.
							 */
							 vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Homepage")));
								vt.getValueDisplay(vt.getRowCount()).setValue(allValuesOfContact.get(i));
								
								if(compareUser()) {
									vt.getValueDisplay(vt.getRowCount()).enableButtons();
									vp.getAddValueButton().setEnabled(true);
								}
								else {
									vt.getValueDisplay(vt.getRowCount()).disableButtons();
									vp.getAddValueButton().setEnabled(false);
								}
							}
							break;
				//TODO: neu hinzugefügte Eigenschaft
				default: 
					if (pid > 10){
					
						editorAdministration.getPropertyOfValue(allValuesOfContact.get(i), new AsyncCallback<Property>() {
							public void onFailure (Throwable t) {
								
							}
							
							public void onSuccess(Property result) {
								allNewPropertiesOfContact.add(result);
								
							}
						});
						
						
						contactTable.setWidget(row, 0, new ValuePanel(pid, row, allNewPropertiesOfContact.get(i).getType() + ": "));
						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						vp = (ValuePanel) contactTable.getWidget(row, 0); 
						
						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
						contactTable.setWidget(row, 1, new ValueTable(pid));
						vt = (ValueTable) contactTable.getWidget(row, 1);
						
						
						if(vt.getValueDisplay(0).getValue() == null){
							vt.getValueDisplay(0).setValue(allValuesOfContact.get(i));
						
							if(compareUser()) {
								vt.getValueDisplay(0).enableButtons();
								vp.getAddValueButton().setEnabled(true);
							}
							else {
								vt.getValueDisplay(0).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						 }else {
							 /*
							 * Es gibt mehrere Ausprägungen zur neuen Eigenschaft.
							 */
							 vt.setWidget(vt.getRowCount(), 0, new ValueDisplay(new ValueTextBox("Homepage")));
								vt.getValueDisplay(vt.getRowCount()).setValue(allValuesOfContact.get(i));
								
								if(compareUser()) {
									vt.getValueDisplay(vt.getRowCount()).enableButtons();
									vp.getAddValueButton().setEnabled(true);
								}
								else {
									vt.getValueDisplay(vt.getRowCount()).disableButtons();
									vp.getAddValueButton().setEnabled(false);
								}
							}
					
					}else {
						Window.alert("Unbekannte Eigenschaft kann nicht hinzugefügt werden.");
					}
				}
		}Window.alert("Auslesen aller Ausprägungen ist durchgelaufen \nKatrin");
	}
	
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
	}
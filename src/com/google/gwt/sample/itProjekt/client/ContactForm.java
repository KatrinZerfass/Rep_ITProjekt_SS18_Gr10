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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	Label newPropertyLabel = new Label("Eigenschaft hinzufügen");
	Button addNewPropertyButton = new Button("Hinzufügen");
	ClickHandler addPropertyClickHandler = null;
	
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
	
	
	public class CloseButton extends Button{
		DialogBox db;
		public CloseButton(DialogBox db) {
			this.db = db;
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
						Window.alert("Ausprägung zu \"Nicht geteilt\" gesetzt" );
					
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
					Window.alert("Ausprägung zu \"Geteilt\" gesetzt" );
				
				}
			});
		}	
		
		public void setImage(boolean b) {
			if(b==true) {
				this.getUpFace().setImage(lockUnlocked);
			}else {
				this.getUpFace().setImage(lockLocked);
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
						Window.alert("geht in AddValueclickHandler rein. Kontakt gleich null\nKatrin");
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
					addValueTextBox = new ValueTextBox("Sonstiges");
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
							Window.alert("Value aus der Datenbank: " + v.getContent() + "\nKatrin");
							/*
							 * War das Anlegen der Ausprägung auf dem Server erfolgreich, so wird sie auch im GUI als neue Zeile in
							 * der jeweiligen ValueTable angezeigt.
							 */
							int rowCount = vt.getRowCount();
							vt.setWidget(rowCount, 0, new ValueDisplay(new ValueTextBox(tb.getIdentifier())));
							((ValueDisplay) vt.getWidget(rowCount ,0)).setValue(v);
							((ValueDisplay) vt.getWidget(rowCount ,0)).enableButtons();
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
		
		public void setLockButtonTo(boolean b) {
			if(b==true) {
				this.lockButton.setImage(b);
			}else {
				this.lockButton.setImage(b);
			}
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
			return addValueButton;
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
		
		private String input;
		
		Label dialogBoxLabel = new Label();
		
        private SuggestBox sb;
        private MultiWordSuggestOracle oracle;
        
        Button ok = new Button("OK");
        
		public EmailDialogBox() {
			
			oracle = new MultiWordSuggestOracle();
			
			setDialogBoxLabel("Bitte geben Sie die Email-Adresse des Nutzers ein mit dem Sie den Kontakt teilen möchten.");
			
			editorAdministration.getAllUsers(new AsyncCallback<Vector<User>>() {
				public void onFailure(Throwable arg0) {
					Window.alert("Fehler beim holen aller User in der InputDialogBox");
				}
				@Override
				public void onSuccess(Vector<User> arg0) {
					
					for(User loopUser : arg0) {
						if (!loopUser.equals(currentUser)) {
							getOracle().add(loopUser.getEmail());
						}
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
			        panel.add(ok);
			        
			        setWidget(panel);
			        
			        show();
				}
			});
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

		public String getInput() {
			return input;
		}

		public void setInput(String input) {
			this.input = input;
		}

		public void setDialogBoxLabel(String labelString) {
			this.dialogBoxLabel.setText(labelString);;
		}
		
		public Button getOKButton() {
			return this.ok;
		}
		
		public void setOKButton(Button b) {
			this.ok = b;
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
		addNewPropertyButton.addStyleName("addNewPropertyButton");
		newPropertyLabel.addStyleName("newPropertyLabel");
		newPropertyListBox.addStyleName("newPropertyListBox");
		
		this.add(contactTable);
		contactTable.getColumnFormatter().setWidth(0, "30px");
		
		editorAdministration.getAllPredefinedPropertiesOf(new AsyncCallback<Vector<Property>>(){
			public void onFailure(Throwable t) {
				Window.alert("Auslesen aller vordefinierten Eigenschaften fehlgeschlagen");
				
			}
			
			public void onSuccess(Vector<Property> properties) {
				allPredefinedProperties.clear();
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
		firstnameTextBox = new ValueTextBox("Name");
		contactTable.setWidget(2, 1, firstnameTextBox);
		
		Label lastnameLabel = new Label("Nachname: ");
		contactTable.setWidget(2, 2, lastnameLabel);
		lastnameTextBox = new ValueTextBox("Name");
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
		addContactButton.addStyleName("buttonPanel");
		Button shareContactButton = new Button("Kontakt teilen");		
		shareContactButton.addStyleName("buttonPanel");
		Button deleteContactButton = new Button("Kontakt löschen");
		deleteContactButton.addStyleName("buttonPanel");
		saveChangesButton = new Button("Änderungen speichern");
		saveChangesButton.addStyleName("buttonPanel");
		Button addContactToContactListButton = new Button("Kontakt zu einer Kontaktliste hinzufügen");
		addContactToContactListButton.addStyleName("buttonPanel");
		removeContactFromContactListButton = new Button("Kontakt aus der aktuellen Kontaktliste entfernen");
		removeContactFromContactListButton.addStyleName("buttonPanel");
		
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
		
		
		if (addPropertyClickHandler == null) {
			addPropertyClickHandler = new NewPropertyClickHandler();
			addNewPropertyButton.addClickHandler(addPropertyClickHandler);
			Window.alert("Setzen des addPropertyclickHandlers");
		}
		
	
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
	//	DialogBox db = new DialogBox();
		
		public void onClick(ClickEvent event) {
			if(contactToDisplay != null) {
				setSelected(null);
			}else {
				if(!checkValue(firstnameTextBox) || !checkValue(lastnameTextBox) ) {
					firstnameTextBox.setText("");
					lastnameTextBox.setText("");
					Window.alert("Ihr Kontakt konnte nicht angelegt werden, bitte versuchen Sie es erneut.");
					
				}else if(checkValue(firstnameTextBox) && checkValue(lastnameTextBox)){
					
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
							Window.alert("Kontakt erfolgreich angelegt.");
							clctvm.addContactOfContactList(clctvm.getMyContactsContactList(), result);
							Window.alert("Owner des neuen Kontakts: " + result.getOwner());
						}
					});
				}
			}
//			if(firstnameTextBox.isEnabled()==false) {
//				firstnameTextBox.setEnabled(true);
//				lastnameTextBox.setEnabled(true);
//			}
//			if(sexListBox.isEnabled()==false) {
//				sexListBox.setEnabled(true);
//			}
//			/*
//			 * Bevor ein neuer Kontakt angelegt werden kann, muss der bestehende Kontakt aus dem Formular genommen werden.
//			 */
//			
//			
//			
//			/*
//			 * Die Dialogbox, die dem Benutzer sagt, was er tun muss, um einen neuen Kontakt anzulegen, wird konfiguriert.
//			 */
//			VerticalPanel vp = new VerticalPanel();
//			Label label = new Label("Tragen Sie im Formular die Daten des Kontakts ein und klicken Sie anschließend auf \"Änderungen speichern\".");
//			Button ok = new Button("Ok");
//			
//			vp.add(label);
//			vp.add(ok);
//			vp.setCellHorizontalAlignment(ok, ALIGN_RIGHT);
//			db.setTitle("Neuen Kontakt anlegen");
//			db.add(vp);
//			db.setWidth("250px");
//			db.setPopupPosition(500, 200);
//			db.show();
//		
//			ok.addClickHandler(new ClickHandler() {
//				public void onClick(ClickEvent event) {
//					db.hide();
//				}
//			});
//			
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
		
		EmailDialogBox dialog;
		
		@Override
		public void onClick(ClickEvent event) {
			
			dialog = new EmailDialogBox();
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt!");
			}
			else {
				/*
				 * Über eine Instanz der inneren Klasse EmailDialogBox können Objekte mit anderen Nutzern geteilt werden.
				 */
				
				Window.alert("EmailDialogBox instanziert");
				
				dialog.getOKButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						
						Window.alert(dialog.getSuggestBox().getText());
						
						editorAdministration.shareContact(currentUser, dialog.getSuggestBox().getText(), clctvm.getSelectedContact(), new AsyncCallback<Permission>() {

							public void onFailure(Throwable arg0) {
								Window.alert("Fehler beim Teilen des Kontakts!");
								dialog.hide();
							}
							public void onSuccess(Permission arg0) {
								if(arg0 != null) {
									Window.alert("Kontakt erfolgreich geteilt.");
									dialog.hide();
								}
								else {
									Window.alert("User ist der Owner des Kontakts!");
								}
							}
						});
					}
				});
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
			
			}else{
				
				editorAdministration.deleteContact(contactToDisplay, compareUser(), currentUser, new AsyncCallback<Void>() {
					public void onFailure(Throwable arg0) {
						Window.alert("Fehler beim Löschen des Kontakts!");
					}
					public void onSuccess(Void arg0){
						Window.alert("Kontakt erfolgreich gelöscht");
						clctvm.removeContactOfContactList(clctvm.getSelectedContactList(), contactToDisplay);
						clctvm.getNodeInfo(clctvm.getSelectedContactList());
					}
				});
				
		       setSelected(null);
			}
		}
	}

	
	/**
	 * Die innere Klasse saveChangesClickHandler.#
	 * 
	 * @author JanNoller
	 */
	private class SaveChangesClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt");
				
						
						
					
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
			else {
				
				Window.alert("allValueTextBoxes toString: " +allValueTextBoxes.toString()+
								"\n allValueTextBoxes size: " +allValueTextBoxes.size());
				
				
				
				for(ValueTextBox vtb : allValueTextBoxes) {
					Window.alert("vtb Wert ist: " +vtb.getText()+
							"\n vtb isChanged ist: " +vtb.getIsChanged());
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
								Window.alert(arg0.getContent());
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
					clctvm.getNodeInfo(clctvm.getSelectedContactList());
				}
			});
		}
	}
	
	private class NewPropertyClickHandler implements ClickHandler{
		DialogBox db1;
		DialogBox db2;
		TextBox inputTextBox1;
		TextBox inputTextBox2;
		int pid;
		String ptype;
		int row;
		
		
		public void onClick(ClickEvent event) {
			ptype = newPropertyListBox.getSelectedItemText();
			row = contactTable.getRowCount();
			
			if(ptype == "Geburtstag") {
				db1 = new DialogBox();
				inputTextBox1 = new TextBox();
				VerticalPanel db1Panel = new VerticalPanel();
				db1.setText("Geburtsdatum eintragen");
			
				Button addBirthdayButton = new Button("Geburtsdatum hinzufügen");
				db1Panel.add(inputTextBox1);
				db1Panel.add(addBirthdayButton);
				db1.add(db1Panel);
				db1.show();
				
				addBirthdayButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						db1.hide();
						db1.hide();
						Window.alert("Dialogbox Titel: " + db1.getText());
						
						
						editorAdministration.createValue(contactToDisplay, 4, inputTextBox1.getText(), new AsyncCallback<Value>() {
							public void onFailure (Throwable t) {
								Window.alert("Geburtsdatum anlegen gescheitert");
							}
							
							public void onSuccess(Value result) {
							
								Label birthdateLabel = new Label("Geburtsdatum: ");
								contactTable.setWidget(3, 2, birthdateLabel);
								
								contactTable.setWidget(3, 3, new ValueDisplay(new ValueTextBox("Geburtstag")));
								((ValueDisplay) contactTable.getWidget(3, 3)).getWidget(0).setWidth("105px");
								((ValueDisplay) contactTable.getWidget(3,3)).setValue(result);
							}
						});
					
					}
				});
				
				
			}
			else if(ptype == "Sonstiges") {
				db2 = new DialogBox();
				inputTextBox2 = new TextBox();
				
				VerticalPanel db2Panel = new VerticalPanel();
				db2.setText("Neue Eigenschaftsart hinzufügen");
				
				Button addPropertyButton = new Button("Eigenschaftsart anlegen");
				db2Panel.add(inputTextBox2);
				db2Panel.add(addPropertyButton);
				db2.add(db2Panel);
				db2.show();
				
					
				addPropertyButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						db2.hide();
						db2.hide();
						Window.alert("Dialogbox Titel: " + db2.getText());
						
						
						editorAdministration.createProperty(contactToDisplay, inputTextBox2.getText(), new AsyncCallback<Property>() {
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
								
							}
						
						});
					
					}
				});
			}
			//else if(ptype == "Straße" || ptype == "Hausnummer" || ptype== "PLZ" || ptype == "Wohnort") {
			else if(ptype == "Anschrift") {
				Window.alert("Springt in Anschrift else if");
				Label addressLabel = new Label("Anschrift: ");
				contactTable.setWidget(row, 0, addressLabel);
				Window.alert("row in die es die Anschrift-label setzt: " + ((Integer) row).toString());
				
				contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
				contactTable.setWidget(row, 1, addressTable);
				Window.alert("Addresstable in row " + ((Integer) row).toString() + " eingesetzt.");
				
				
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
				
				
				Button addAddressButton = new Button("Anlegen");
				addAddressButton.addStyleName("addNewPropertyButton");
				addressTable.setWidget(0,3, addAddressButton);
				
				addAddressButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						addressTable.removeCell(0,3);
						
						editorAdministration.createAddress(streetTextBox.getText(), houseNrTextBox.getText(),
								plzTextBox.getText(), cityTextBox.getText(), contactToDisplay, new AsyncCallback<Value>(){
							public void onFailure(Throwable t) {
								Window.alert("Probleme beim Anlegen der Adresse");
							}
							public void onSuccess(Value street) {
								streetTextBox.setValue(street);
								((LockButton) addressTable.getWidget(0, 2)).setValue(street);
								((DeleteValueButton) addressTable.getWidget(0,3)).setValue(street);
								Window.alert("Adresse erfolgreich angelegt");
							}
						});

						
					}
				});
				
				Window.alert("Ende der if-else von Anschrift");
				
				/*
				 * Da es sich bei der Anschrift nicht um ValueDisplays handelt, muss auf die beiden Buttons seperat
				 * operiert werden. Ihnen wird jeweils die Straße als Ausprägung gesetzt, da es nur möglich ist, einen
				 * einzelnen Wert als Value zu setzten. Trotzdem operieren diese Buttons beim Klicken auf die gesamten
				 * vier Ausprägungen, die zur Anschrift gehören.
				 */
				
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
				if (!text.matches("\\d+")) {
					return true;
				}
				else {
					Window.alert("Ungültige Zeichen im Namen!");
					return false;
				}
			case "Straße":
				if (!text.matches("\\d+")) {
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
				if (text.matches("[A-ZÜÄÖ][a-züäöß]*")) {
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
				if (text.matches("[A-ZÜÄÖa-züäöß\\s]*")) {
					return true;
				}
				else {
					Window.alert("Ungültige Zeichen im Arbeitgebernamen!");
					return false;
				}
				
			case "Sonstiges":
				return true;
				
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
		contactTable.clear();
		contactTable.removeAllRows();
		buttonsPanel.clear();
		sexListBox.clear();
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
	 * Zeigt den selektierten Kontakt an.
	 *
	 * @param c der selektierte Kontakt
	 * @author KatrinZerfass & JanNoller
	 */
	public void setSelected(Contact c) {
		clearContactForm();
		
		if(newPropertyPanel.isVisible()==true) {
			newPropertyPanel.setVisible(false);
		}
		
//		for (int i = 4; i < contactTable.getRowCount(); i++) {
//			for (int a =0; a<4; a++) {
//				if(contactTable.isCellPresent(i, a)) {
//					contactTable.removeCell(i, a);
//				}
//			}
//		}
//		
//		if(contactTable.isCellPresent(3, 2)) {
//			contactTable.removeCell(3, 2);
//		}
//		
//		if(contactTable.isCellPresent(3, 3)) {
//			contactTable.removeCell(3, 3);
//		}
	
		
		/*
		 * Bei jedem neuen Aufruf von setSelected werden die ausgefüllten ValueTextBoxen geleert und aus dem Vector alle TextBoxen entfernt.
		 */
		
		
		firstnameTextBox.setEnabled(true);
		lastnameTextBox.setEnabled(true);
		sexListBox.setEnabled(true);
		saveChangesButton.setEnabled(true);
		removeContactFromContactListButton.setEnabled(true);
		
	
		if (c != null){
			
			contactToDisplay = c;
			
			
			/*
			 * Befüllen der Eigenschaften aus der Datenbank
			 */
			
			
		
			for (Property p : allPredefinedProperties) {
				if(p.getType()!="Straße" && p.getType()!= "Hausnummer" && p.getType()!= "PLZ" && p.getType()!= "Wohnort") {
				newPropertyListBox.addItem(p.getType());
				}
			}
			newPropertyListBox.addItem("Anschrift");
			newPropertyListBox.addItem("Sonstiges");	
			
			
			newPropertyPanel.add(newPropertyLabel);
			newPropertyPanel.add(newPropertyListBox);
			newPropertyPanel.addStyleName("propertyPanel");
			newPropertyPanel.add(addNewPropertyButton);
			
			newPropertyPanel.setVisible(true);
			
			
			
			
			
			/*
			 * Der angemeldete Nutzer wird mit dem Eigentümer verglichen. Ist er ausschließlich Teilhaber, so werden bestimmte Buttons
			 * bereits im Vorfeld ausgegraut.
			 */
		
			
			if(!compareUser()) {
				saveChangesButton.setEnabled(false);
				removeContactFromContactListButton.setEnabled(false);
				firstnameTextBox.setEnabled(false);
				lastnameTextBox.setEnabled(false);
				sexListBox.setEnabled(false);
				newPropertyPanel.setVisible(false);
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
					allValuesOfContact = new Vector<Value>();
					for(Value v: values) {
						allValuesOfContact.add(v);
					//	Window.alert("isShared des Values " + v.getContent() + " = " + v.getIsShared()); 
					}
//					Window.alert("Alle Ausprägungen des Kontaktes ausgelesen. \n"
//							+ "Anzahl der Values im Vektor: " + ((Integer)allValuesOfContact.size()).toString());
					
					for(Value v: allValuesOfContact) {
						if(v.getPropertyid() >10) {
							editorAdministration.getPropertyOfValue(v, new GetPropertyOfValueCallback(v));
						}else{
							if(v == allValuesOfContact.lastElement()) {
								displayAllValuesOfContact();
							}
						}
					}
					
					
					
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
			contactToDisplay = null;
			firstnameTextBox.getElement().setPropertyString("placeholder", "Vorname...");
			firstnameTextBox.setText("");
			lastnameTextBox.getElement().setPropertyString("placeholder", "Nachname...");
			lastnameTextBox.setText("");

		}
				
	}
	
	public class GetPropertyOfValueCallback implements AsyncCallback<Property>{
		Value v;
		
		public GetPropertyOfValueCallback(Value v) {
			this.v=v;
		}
		
		public void onFailure(Throwable t) {
			Window.alert("Fehler beim Auslesen der neuen Eigenschaft");
			
		}
		
		public void onSuccess(Property p) {
			allNewPropertiesOfContact.add(p);
			if(v == allValuesOfContact.lastElement()) {
				displayAllValuesOfContact();
			}
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
		
		int row;
		int vtRow;
		ValuePanel vp = null; //das ValuePanel der jeweiligen Eigenschaftsart
		ValueTable vt = null;	 //die ValueTable der jeweiligen Eigenschaftsart
	
		
		
		int count = allValuesOfContact.size();
		for(int i=0; i<count; i++) {
			int pid = allValuesOfContact.get(i).getPropertyid();
			
		
		
			
			switch (pid) {
				
				case 1: // Tel.Nr. geschäftlich
						row = contactTable.getRowCount();
						if(compareUser() || (!compareUser() && allValuesOfContact.get(i).getIsShared()==true)) {
							
								/*
								 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
								 */
							if(contactTable.isCellPresent(row, 0)) {
								if (contactTable.getWidget(row, 0) == null) {
									contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Geschäftliche Telefonnummer: "));
									contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
								}
							}else {
								contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Geschäftliche Telefonnummer: "));
								contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
							}
							vp = (ValuePanel) contactTable.getWidget(row, 0);
											
							
							if (contactTable.isCellPresent(row, 1)) {
								if (contactTable.getWidget(row, 1) == null) {
									contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
									contactTable.setWidget(row, 1, new ValueTable(pid));
								}
								
							}else {
								contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
								contactTable.setWidget(row, 1, new ValueTable(pid));
							}
							vt = (ValueTable) contactTable.getWidget(row, 1);
							vtRow = vt.getRowCount();
							vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
							vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
							
							
							/*
							 * Gleiches Prinzip wie gerade schon, nur jetzt für das soeben neu hinzugefügte ValueDisplay.
							 */
							if (compareUser()) {
								vt.getValueDisplay(vtRow).enableButtons();
								vp.getAddValueButton().setEnabled(true);
								if(allValuesOfContact.get(i).getIsShared() == true){
									vt.getValueDisplay(vtRow).setLockButtonTo(true);
								}else {
									vt.getValueDisplay(vtRow).setLockButtonTo(false);
								}
							}
							else {
								vt.getValueDisplay(vtRow).disableButtons();
								vp.getAddValueButton().setEnabled(false);
								
							}
						}
					
						break;
						
				
				case 2:  // Tel.Nr. privat
						row = contactTable.getRowCount();
						if(compareUser() || (!compareUser() && allValuesOfContact.get(i).getIsShared()==true)) {
							/*
							 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
							 */
							
							if(contactTable.isCellPresent(row, 0)) {
								if (contactTable.getWidget(row, 0) == null) {
									contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Private Telefonnummer: "));
									contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
								}
							}else {
								contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Private Telefonnummer: "));
								contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
							}
							vp = (ValuePanel) contactTable.getWidget(row, 0);
											
							
							if (contactTable.isCellPresent(row, 1)) {
								if (contactTable.getWidget(row, 1) == null) {
									contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
									contactTable.setWidget(row, 1, new ValueTable(pid));
								}
								
							}else {
								contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
								contactTable.setWidget(row, 1, new ValueTable(pid));
							}
							vt = (ValueTable) contactTable.getWidget(row, 1);
							vtRow = vt.getRowCount();
							vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox("Telefonnummer")));
							vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
							
							
							/*
							 * Gleiches Prinzip wie gerade schon, nur jetzt für das soeben neu hinzugefügte ValueDisplay.
							 */
							if (compareUser()) {
								vt.getValueDisplay(vtRow).enableButtons();
								vp.getAddValueButton().setEnabled(true);
								if(allValuesOfContact.get(i).getIsShared() == true){
									vt.getValueDisplay(vtRow).setLockButtonTo(true);
								}else {
									vt.getValueDisplay(vtRow).setLockButtonTo(false);
								}
							}
							else {
								vt.getValueDisplay(vtRow).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}
						break;
						
				case 3:  // e-Mail
						row = contactTable.getRowCount();
						if(compareUser() || (!compareUser() && allValuesOfContact.get(i).getIsShared()==true)) {
							/*
							 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
							 */
							
							if(contactTable.isCellPresent(row, 0)) {
								if (contactTable.getWidget(row, 0) == null) {
									contactTable.setWidget(row, 0, new ValuePanel(pid, row, "e-Mail-Adressen: "));
									contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
								}
							}else {
								contactTable.setWidget(row, 0, new ValuePanel(pid, row, "e-Mail-Adressen: "));
								contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
							}
							vp = (ValuePanel) contactTable.getWidget(row, 0);
											
							
							if (contactTable.isCellPresent(row, 1)) {
								if (contactTable.getWidget(row, 1) == null) {
									contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
									contactTable.setWidget(row, 1, new ValueTable(pid));
								}
								
							}else {
								contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
								contactTable.setWidget(row, 1, new ValueTable(pid));
							}
							vt = (ValueTable) contactTable.getWidget(row, 1);
							vtRow = vt.getRowCount();
							vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox("Email")));
							vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
							
							
							/*
							 * Gleiches Prinzip wie gerade schon, nur jetzt für das soeben neu hinzugefügte ValueDisplay.
							 */
							if (compareUser()) {
								vt.getValueDisplay(vtRow).enableButtons();
								vp.getAddValueButton().setEnabled(true);
								if(allValuesOfContact.get(i).getIsShared() == true){
									vt.getValueDisplay(vtRow).setLockButtonTo(true);
								}else {
									vt.getValueDisplay(vtRow).setLockButtonTo(false);
								}
							}
							else {
								vt.getValueDisplay(vtRow).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}
						break;
						
						
				case 4:  // Geburtstag
					if(compareUser() || (!compareUser() && allValuesOfContact.get(i).getIsShared()==true)) {
							/*
							 * Eine Ausprägung zu Geburtstag kann nur einmal vorhanden sein. Demzufolge gibt es hierfür auch keine ValueTable.
							 * Das ValueDisplay, in dem sich die TextBox für das Geburtsdatum befindet, wird direkt angesprochen.
							 */
							Label birthdateLabel = new Label("Geburtsdatum: ");
							contactTable.setWidget(3, 2, birthdateLabel);
							
							contactTable.setWidget(3, 3, new ValueDisplay(new ValueTextBox("Geburtstag")));
							((ValueDisplay) contactTable.getWidget(3, 3)).getWidget(0).setWidth("105px");
							((ValueDisplay) contactTable.getWidget(3,3)).setValue(allValuesOfContact.get(i));
							
							if (compareUser()) {
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
							
							for (Property p : allPredefinedProperties) {
								if(p.getType()== "Geburtstag") {
									allPredefinedProperties.remove(p);
									newPropertyListBox.clear();
									for (Property prop : allPredefinedProperties) {
										if(prop.getType()!="Straße" && p.getType()!= "Hausnummer" && p.getType()!= "PLZ" && p.getType()!= "Wohnort") {
											newPropertyListBox.addItem(prop.getType());
										}
									}
									newPropertyListBox.addItem("Anschrift");
									newPropertyListBox.addItem("Sonstiges");
								}
							}
						}
						break;
						
						
				case 5: // Arbeitsplatz
						row = contactTable.getRowCount();
						if(compareUser() || (!compareUser() && allValuesOfContact.get(i).getIsShared()==true)) {
							
							/*
							 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
							 */
							
							if(contactTable.isCellPresent(row, 0)) {
								if (contactTable.getWidget(row, 0) == null) {
									contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Arbeitsplatz: "));
									contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
								}
							}else {
								contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Arbeitsplatz: "));
								contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
							}
							vp = (ValuePanel) contactTable.getWidget(row, 0);
											
							
							if (contactTable.isCellPresent(row, 1)) {
								if (contactTable.getWidget(row, 1) == null) {
									contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
									contactTable.setWidget(row, 1, new ValueTable(pid));
								}
								
							}else {
								contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
								contactTable.setWidget(row, 1, new ValueTable(pid));
							}
							vt = (ValueTable) contactTable.getWidget(row, 1);
							vtRow = vt.getRowCount();
							vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox("Arbeitsplatz")));
							vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
							
							
							/*
							 * Gleiches Prinzip wie gerade schon, nur jetzt für das soeben neu hinzugefügte ValueDisplay.
							 */
							if (compareUser()) {
								vt.getValueDisplay(vtRow).enableButtons();
								vp.getAddValueButton().setEnabled(true);
								if(allValuesOfContact.get(i).getIsShared() == true){
									vt.getValueDisplay(vtRow).setLockButtonTo(true);
								}else {
									vt.getValueDisplay(vtRow).setLockButtonTo(false);
								}
							}
							else {
								vt.getValueDisplay(vtRow).disableButtons();
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
						
						row = contactTable.getRowCount();
						if(compareUser() || (!compareUser() && allValuesOfContact.get(i).getIsShared()==true)) {
						
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
						row = contactTable.getRowCount();
						if(compareUser() || (!compareUser() && allValuesOfContact.get(i).getIsShared()==true)) {
						
							/*
							 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
							 */
							
							if(contactTable.isCellPresent(row, 0)) {
								if (contactTable.getWidget(row, 0) == null) {
									contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Homepage: "));
									contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
								}
							}else {
								contactTable.setWidget(row, 0, new ValuePanel(pid, row, "Homepage: "));
								contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
							}
							vp = (ValuePanel) contactTable.getWidget(row, 0);
											
							
							if (contactTable.isCellPresent(row, 1)) {
								if (contactTable.getWidget(row, 1) == null) {
									contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
									contactTable.setWidget(row, 1, new ValueTable(pid));
								}
								
							}else {
								contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
								contactTable.setWidget(row, 1, new ValueTable(pid));
							}
							vt = (ValueTable) contactTable.getWidget(row, 1);
							vtRow = vt.getRowCount();
							vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox("Homepage")));
							vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
							
							
							/*
							 * Gleiches Prinzip wie gerade schon, nur jetzt für das soeben neu hinzugefügte ValueDisplay.
							 */
							if (compareUser()) {
								vt.getValueDisplay(vtRow).enableButtons();
								vp.getAddValueButton().setEnabled(true);
								if(allValuesOfContact.get(i).getIsShared() == true){
									vt.getValueDisplay(vtRow).setLockButtonTo(true);
								}else {
									vt.getValueDisplay(vtRow).setLockButtonTo(false);
								}
							}
							else {
								vt.getValueDisplay(vtRow).disableButtons();
								vp.getAddValueButton().setEnabled(false);
							}
						}
						break;
		
				default: 
					row = contactTable.getRowCount();
					if(compareUser() || (!compareUser() && allValuesOfContact.get(i).getIsShared()==true)) {
						String ptype = null;
						for(Property p: allNewPropertiesOfContact) {
							if (pid == p.getId()) {
								ptype = p.getType();
									
							}else {
								Window.alert("Fehler beim auslesen der ptype");
							}
						}
					
				
						if(contactTable.isCellPresent(row, 0)) {
							if (contactTable.getWidget(row, 0) == null) {
								contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
								contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
							}
						}else {
							contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
							contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
						}
						vp = (ValuePanel) contactTable.getWidget(row, 0);
										
						
						if (contactTable.isCellPresent(row, 1)) {
							if (contactTable.getWidget(row, 1) == null) {
								contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
								contactTable.setWidget(row, 1, new ValueTable(pid));
							}
							
						}else {
							contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
							contactTable.setWidget(row, 1, new ValueTable(pid));
						}
						vt = (ValueTable) contactTable.getWidget(row, 1);
						vtRow = vt.getRowCount();
						vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox("Sonstiges")));
						vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
						
						
						/*
						 * Gleiches Prinzip wie gerade schon, nur jetzt für das soeben neu hinzugefügte ValueDisplay.
						 */
						if (compareUser()) {
							vt.getValueDisplay(vtRow).enableButtons();
							vp.getAddValueButton().setEnabled(true);
							if(allValuesOfContact.get(i).getIsShared() == true){
								vt.getValueDisplay(vtRow).setLockButtonTo(true);
							}else {
								vt.getValueDisplay(vtRow).setLockButtonTo(false);
							}
						}
						else {
							vt.getValueDisplay(vtRow).disableButtons();
							vp.getAddValueButton().setEnabled(false);
						}
						
						
					}	
				
					break;
						
					
				}
		
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
	
	public void disableRemoveContactButton() {
		this.removeContactFromContactListButton.setEnabled(false);
	}
	
	}
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
	
	/*
	 * Buttons, auf die später auch außerhalb der Methode onLoad() zugegriffen werden muss (wegen disablen, falls der Nutzer 
	 * nur Teilhaber ist). Deshalb wurden sie als Instanzenvariablen deklariert.
	 */
	Button saveChangesButton;
	Button removeContactFromContactListButton;
	

	/**
	 * Die innere Klasse CloseButton.
	 * Sie dient der Darstellung von "Abbrechen"-Buttons auf jeder instantiierten DialogBox. Dieser bietet dem Nutzer die Möglichkeit,
	 * den Vorgang abzubrechen und die DialogBox zu schließen.
	 * 
	 *  @author KatrinZerfass
	 */
	
	public class CloseButton extends Button{
		
		DialogBox db;
		
		/**
		 * Der Konstruktor von LockButton. Die DialogBox, in der der CloseButton hinzugefügt wird, wird als Übergabeparameter definiert.
		 */
		public CloseButton(DialogBox db) {
			this.db = db;
			this.addClickHandler(new CloseDBClickHandler(db)); 
			this.setText("Abbrechen");
			this.addStyleName("closebutton");
		}
		
		/**
		 * Der ClickHandler wird im Konstruktor dem CloseButton hinzugefügt. Er schließt die DialogBox.
		 * @author Zerfass
		 *
		 */
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
			
			this.setEnabled(false);
			
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
				if(value.getPropertyid() == 6) { //die PropertyID 6 bezieht sich auf die Ausprägung "Straße"
					/*
					 * Wir der LockButton betätigt, welcher sich auf die Straße bezieht, so müssen folglich auch isShared der Ausprägungen
					 * Hausnummer, PLZ und Wohnort neu gesetzt werden, da diese vier Ausprägungen immer zusammenhängen.	
					 * Dazu wird der Vektor allValuesOfContact durchiteriert und nach den Ausprägungen der Anschrift durchsucht.
					 */
						for(int i= 0; i<allValuesOfContact.size(); i++) {
							int pid = allValuesOfContact.get(i).getPropertyid();
							
							// || Straße||Hausnummer||   PLZ    || Wohnort
							if(pid == 6 || pid == 7 || pid == 8 || pid ==9) {
								editorAdministration.editValue(contactToDisplay, pid, allValuesOfContact.get(i), 
												allValuesOfContact.get(i).getContent(), false, new AsyncCallback<Value>() {
									
									public void onFailure(Throwable t) {
										Window.alert("Das Setzen deer Ausprägung zu \"Nicht geteilt\" ist fehlgeschlagen.");
										
									}
									public void onSuccess(Value v) {
										
													
									}
								});
							}
						}			
					}
			
				
				else{ //es handelt sich um jede andere "normale" Ausprägung
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
						Window.alert("Ausprägung zu \"Geteilt\" gesetzt" );
					
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
//						editorAdministration.deleteValue(value, new AsyncCallback<Void>() {
//					
//							public void onFailure(Throwable t) {
//								Window.alert("Das Löschen der Ausprägung ist fehlgeschlagen.");
//							}
//							
//							public void onSuccess(Void result) {	
//								Window.alert("Die Ausprägung wurde gelöscht.");
//								
//								
//							}
//
//						});
						editorAdministration.deleteValue(value, new AsyncCallback<Void>() {
							public void onFailure(Throwable t) {
								Window.alert("Das Löschen der Ausprägung ist fehlgeschlagen.");
								};
							{Window.alert("Das Löschen der Ausprägung ist fehlgeschlagen 2.");};
							public void onSuccess(Void result) {Window.alert("Die Ausprägung wurde gelöscht.");}; 
							{setSelected(contactToDisplay);
								Window.alert("Die Ausprägung wurde gelöscht. 2");};
						});//TODO WTF is this?!
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
			
			
		//wieder zurück kommentieren!
			this.setEnabled(true);
			//TODO: ?
			
					
			this.addClickHandler(new ClickHandler() {
				
				public void onClick (ClickEvent event) {
					
					addValuePopUp(propertyId, row);	
					
				}
			});
		}
		
		
		/**
		 * Die Methode addValuePopUp erzeugt ein PopUp, in das man eine neue Ausprägung eintragen kann
		 * 
		 * @param pid die referenzierte Eigenschaftsart. Wird vom ClickHandler übergeben.
		 * @param row die Zeile der ContactTable, in der sich die Eigenschaftsart befindet. Wird vom ClickHandler übergeben.
		 */
		public void addValuePopUp(int pid, int row) {
			
			/* Die DialogBox, welche aufpoppt */
			DialogBox addValueDB = new DialogBox();
			addValueDB.setText("Neue Ausprägung hinzufügen");
			addValueDB.setAnimationEnabled(true);
			addValueDB.setGlassEnabled(true);
			
			/*Das innere Panel der DialogBox */
			VerticalPanel addValueDBPanel = new VerticalPanel();
			addValueDBPanel.setHeight("100px");
			addValueDBPanel.setWidth("230px");
		    addValueDBPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		    addValueDB.add(addValueDBPanel);
		    			
			Label addValueLabel = new Label();
			addValueDBPanel.add(addValueLabel);
			
			/*Die ValueTextBox, in die man die neue Ausprägung eintragen kann. Sie wird nach der switch-case gesetzt */
			ValueTextBox addValueTextBox = null;
					
		
			/*
			 * Je nachdem, um welche Art von Eigenschaft es sich handelt, wird der Identifier gesetzt, mit dem nach der switch-case
			 * die ValueTextBox erstellt wird. Der Identifier dient der Methode checkValue aus den ClientsideFunctions, welche auf korrekte 
			 * Eingabe überprüft.
			 */
			String identifier = null;
			
			switch(pid) {
			
				case 1: identifier = "Telefonnummer";
						addValueLabel.setText("Neue geschäftliche Telefonnummer: ");
						break;
						
				case 2: identifier = "Telefonnummer";
						addValueLabel.setText("Neue private Telefonnummer: ");
						break;
						
				case 3: identifier = "Email";
						addValueLabel.setText("Neue e-Mail-Adresse: ");
						break;
						
				case 5: identifier = "Arbeitsplatz";
						addValueLabel.setText("Neue Arbeitsstelle: ");
						break;
						
				case 10: identifier = "Homepage";
						addValueLabel.setText("Neue Homepage: ");
						break;
				default: 
						identifier = "Sonstiges";
						addValueLabel.setText("Neue Ausprägung: ");
						break;
					
				
			}
			
			addValueTextBox = new ValueTextBox(identifier);
			addValueDBPanel.add(addValueTextBox);
			
			HorizontalPanel dbButtonsPanel=new HorizontalPanel();
			addValueDBPanel.add(dbButtonsPanel);
			
			CloseButton closeButton = new CloseButton(addValueDB);
			dbButtonsPanel.add(closeButton);
			
			Button addValueButton = new Button("Hinzufügen");
			addValueButton.addStyleName("okbutton");
			dbButtonsPanel.add(addValueButton);
			
			
			addValueButton.addClickHandler(new AddValueClickHandler(addValueDB, addValueTextBox,
					((ValueTable) contactTable.getWidget(row, 1)), pid));
			
			
			addValueDB.show();

		}
		
		/**
		 * Der innere Klasse AddValueClickHandler innerhalb der inneren Klasse AddValueButton.
		 * Wird aufgerufen, nachdem der Benutzer eine neue Ausprägung einträgt und in der DialogBox auf "Hinzufügen" klickt.
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
	 * Die innere Klasse EmailDialogBox
	 * 
	 * @author JanNoller
	 * TODO: nicht nach Clientside Funtcions auslagern?!
	 */
	
	public class EmailDialogBox extends DialogBox{
		
		private String input;
		
		Label dialogBoxLabel = new Label();
		
        private SuggestBox sb;
        private MultiWordSuggestOracle oracle;
        CloseButton close=new CloseButton(this);

        Button ok = new Button("OK");
        

		public EmailDialogBox() {
			
			oracle = new MultiWordSuggestOracle();
			ok.addStyleName("okbutton");
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
					HorizontalPanel hpanel=new HorizontalPanel();
			        panel.setHeight("100");
			        panel.setWidth("300");
			        panel.setSpacing(10);
			        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			        panel.add(dialogBoxLabel);
			        panel.add(getSuggestBox());
			        hpanel.add(close);
			        hpanel.add(ok);
			        panel.add(hpanel);
			        
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
	 * 
	 * @author KatrinZerfass & Anna-MariaGmeiner
	 */
	
	public void onLoad() {
		
		super.onLoad();
		
		this.add(contactTable);
		contactTable.setStyleName("contactTable");
		//TODO: hier mit anderen werten probieren?!
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
				
				for (Property p : allPredefinedProperties) {
					if(p.getType()!="Straße" && p.getType()!= "Hausnummer" && p.getType()!= "PLZ" && p.getType()!= "Wohnort") {
					newPropertyListBox.addItem(p.getType());
					}
				}
				newPropertyListBox.addItem("Anschrift");
				newPropertyListBox.addItem("Sonstiges");	
				

				newPropertyLabel.addStyleName("newPropertyLabel");
				newPropertyPanel.add(newPropertyLabel);
				newPropertyPanel.add(newPropertyListBox);
				newPropertyPanel.addStyleName("propertyPanel");
				newPropertyPanel.add(addNewPropertyButton);
				newPropertyPanel.setVisible(false);
				
				
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
		
		sexListBox.addItem("männlich");
		sexListBox.addItem("weiblich");
		sexListBox.addItem("Sonstiges");
		contactTable.setWidget(3, 1, sexListBox);		
	
		
		/*
		 *Die Buttons für das buttonPanel werden instantiiert, mit Stylenamen versehen und zum buttonPanel hinzugefügt
		 */
		Button addContactButton = new Button("Neuen Kontakt anlegen");
		addContactButton.addStyleName("buttonPanel");
		buttonsPanel.add(addContactButton);
		
		Button shareContactButton = new Button("Kontakt teilen");		
		shareContactButton.addStyleName("buttonPanel");
		buttonsPanel.add(shareContactButton);
		
		Button deleteContactButton = new Button("Kontakt löschen");
		deleteContactButton.addStyleName("buttonPanel");
		buttonsPanel.add(deleteContactButton);
		
		Button addContactToContactListButton = new Button("Kontakt zu einer Kontaktliste hinzufügen");
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
	 * Die innere Klasse NewContactClickHandler.
	 * 
	 * @author KatrinZerfass
	 */
	private class NewContactClickHandler implements ClickHandler{
		
		public void onClick(ClickEvent event) {
			
			if(contactToDisplay != null) {
				setSelected(null);
				//das ContactForm wird gecleared, damit ein neuer Kontakt angelegt werden kann 
				
			}else {
				if(!ClientsideFunctions.checkValue(firstnameTextBox) || !ClientsideFunctions.checkValue(lastnameTextBox) ) {
					firstnameTextBox.setText("");
					lastnameTextBox.setText("");
					//Wurden ungültige Werte für Vor- und Nachnamen eingetragen, werden die Texte resettet und es kommt eine Fehlermeldung
					Window.alert("Ihr Kontakt konnte nicht angelegt werden, bitte versuchen Sie es erneut.");
					
				}else if(ClientsideFunctions.checkValue(firstnameTextBox) && ClientsideFunctions.checkValue(lastnameTextBox)){
					
					/*
					 * Im Folgenden wird ein neuer Kontakt im System angelegt
					 */
					
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
							Window.alert("Fehler beim Kontakt anlegen");
							
						}
						public void onSuccess(Contact result) {
							Window.alert("Kontakt erfolgreich angelegt.");
							clctvm.addContactOfContactList(clctvm.getMyContactsContactList(), result);
							//Ein neu angelegter Kontakt wird per Default immer der "Meine Kontakte"-Standardkontaktliste hinzugefügt.
						}
					});
				}
			}

		}
	}
	
	
	
	/**
	 * Die innere Klasse shareContactClickHandler.
	 * TODO: comments
	 * 
	 *  @author JanNoller
	 */
	private class ShareContactClickHandler implements ClickHandler{
		
		EmailDialogBox dialog;
		
		
		@Override
		public void onClick(ClickEvent event) {
			
			
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt!");
			}
			else {
				dialog = new EmailDialogBox();
				/*
				 * Über eine Instanz der inneren Klasse EmailDialogBox können Objekte mit anderen Nutzern geteilt werden.
				 */
				
				dialog.show();
				
				dialog.getOKButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						
						if(dialog.getSuggestBox().getText()== "") {
							Window.alert("Fehler beim Teilen des Kontakts!");
						}
						
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
									dialog.hide();
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
	 * TODO: comments
	 * @author JanNoller
	 */
	private class DeleteContactClickHandler implements ClickHandler{
		
		@Override
		public void onClick(ClickEvent event) {
			
			if (contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt");
			
			}
			else if(contactToDisplay.getIsUser()) { // der ausgewählte Kontakt ist der, welcher den User repräsentiert
				Window.alert("Sie können sich selbst nicht löschen!");
			}
			else{
				editorAdministration.deleteContact(contactToDisplay, ClientsideFunctions.isOwner(contactToDisplay, currentUser), currentUser, new AsyncCallback<Void>() {
					public void onFailure(Throwable arg0) {
						Window.alert("Fehler beim Löschen des Kontakts!");
					}
					public void onSuccess(Void arg0){
						Window.alert("Kontakt erfolgreich gelöscht");
						clctvm.removeContactOfContactList(clctvm.getSelectedContactList(), contactToDisplay);
					}
				});
				
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
						
			}
			else {
				
				for(ValueTextBox vtb : allValueTextBoxes) {
				
					/*
					 * Wenn in einer ValueTextBox der Inhalt verändert wurde, so wird für diese Ausprägung die Methode editValue() aufgerufen.
					 */
					if (vtb.getIsChanged() && vtb.getTextBoxValue() != null) {
						editorAdministration.editValue(contactToDisplay, vtb.getTextBoxValue().getPropertyid(), vtb.getTextBoxValue(), vtb.getText(), 
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
					else if(vtb.getIsChanged() && (vtb.equals(firstnameTextBox) || vtb.equals(lastnameTextBox))){
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
		VerticalPanel dbPanel= new VerticalPanel();
		HorizontalPanel dbButtonsPanel= new HorizontalPanel();
		ListBox clListbox = new ListBox();
		ContactList chosenCL;
		
        Vector<ContactList> contactLists = new Vector<ContactList>();
		
		public void onClick(ClickEvent event) {
			
			if(contactToDisplay ==null) {
				Window.alert("kein Kontakt ausgewählt");
			}
			else {
				/*
				 * Im Folgenden wird die DialogBox aufgebaut.
				 */
				dbPanel.setHeight("100");
		        dbPanel.setWidth("300");
		        dbPanel.setSpacing(10);
		        dbPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		    	db.add(dbPanel);
		        
		        Label label = new Label("Bitte wählen Sie eine Kontaktliste aus.");
		        dbPanel.add(label);
		        
		        /*
		         * In der Dialogbox soll eine ListBox angezeigt werden, die alle Kontaktlisten des Nutzers beinhaltet.
		         */
		        dbPanel.add(clListbox);
		        
		      
		        CloseButton closeButton = new CloseButton(db);
				dbButtonsPanel.add(closeButton);
				
				//TODO: why final?
		        final Button okButton = new Button("OK");
		        okButton.addStyleName("okbutton");
		        dbButtonsPanel.add(okButton);
		        
		       
				
		        dbPanel.add(dbButtonsPanel);
		       	     
		    	db.show();
	
		        editorAdministration.getAllOwnedContactListsOfActiveUser(currentUser, new AsyncCallback<Vector<ContactList>>() {
		        	
		        	public void onFailure(Throwable t) {
		        		Window.alert("Fehler beim Abruf der Kontaklisten des Nutzers");
		        	}
		        	
		        	public void onSuccess(Vector<ContactList> result) {
		        		
		        		contactLists = result;
		        		
		        		/*
		        		 * Alle Kontaktlisten, von denen der Nutzer Eigentümer ist, werden der ListBox hinzugefügt.
		        		 */
		        		for (ContactList cl : contactLists) {
				        	clListbox.addItem(cl.getName());
				        }
		        		
		        		okButton.addClickHandler(new ClickHandler() {
				        	
				        	public void onClick(ClickEvent event) {
				        		for (ContactList cl : contactLists) {
				        			if (clListbox.getSelectedItemText() == cl.getName()) {
				        				chosenCL = cl;
				        			}
				        		}
				        		
				        		db.hide();
				        		
				        		/*
				 		        * Hat der Nutzer eine Kontaktliste ausgewählt und klickt "OK", so wird der Kontakt dieser Kontaktliste hinzugefügt.
				 		        */
				        		editorAdministration.addContactToContactList(chosenCL, contactToDisplay, new AsyncCallback<ContactList>() {
				        			
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
		        
		       
		        
		        
		       
		        
			}
	        
		}
	}
	

	
	/**
	 * Die innere Klasse RemoveContactFromContactListClickHandler.
	 * 
	 * @author JanNoller
	 */
	private class RemoveContactFromContactListClickHandler implements ClickHandler{
		
		public void onClick(ClickEvent event) {
			if(contactToDisplay == null) {
				Window.alert("kein Kontakt ausgewählt");
			}
			else { 
				if(clctvm.getSelectedContactList() == clctvm.getMyContactsContactList()) {
					Window.alert("Sie können Kontakte in dieser Kontaktliste nur löschen, nicht entfernen!");
				}
				else if(!ClientsideFunctions.isOwner(clctvm.getSelectedContactList(), currentUser)) {
					Window.alert("Sie können den Kontakt aus dieser Liste nicht entfernen, da es eine geteilte List ist.");
				}
				else {
					/*
					 * Handelt es sich weder um die Default-Liste "Meine Kontakte", noch um eine geteilte Liste, so wird der ausgewählte Kontakt 
					 * aus der ausgewählten Liste entfernt
					 */
					
					//clearContactForm();
					editorAdministration.removeContactFromContactList(clctvm.getSelectedContactList(), contactToDisplay, new AsyncCallback<ContactList>() {
						@Override
						public void onFailure(Throwable arg0) {	
							Window.alert("Fehler beim entfernen des Kontakts aus der Kontaktliste!");
						}
						@Override
						public void onSuccess(ContactList arg0) {
							Window.alert("Kontakt erfolgreich aus der Kontaktliste entfernt.");
							clctvm.removeContactOfContactList(arg0, contactToDisplay);
							//clctvm.getNodeInfo(clctvm.getSelectedContactList());
						}
					});
				}
			}
		}
	}
	
	
	/**
	 * Die innere Klasse NewPropertyClickHandler.
	 * 
	 * @author KatrinZerfass
	 */
	private class NewPropertyClickHandler implements ClickHandler{
		DialogBox db = null;
		VerticalPanel dbPanel = null;
		ValueTextBox inputTextBox = null;
		HorizontalPanel dbButtonsPanel = null;
		int pid;
		String ptype;
		int row;

		
		
		public void onClick(ClickEvent event) {
			ptype = newPropertyListBox.getSelectedItemText();
			row = contactTable.getRowCount();
			
			if(ptype == "Geburtstag") {
				db = new DialogBox();
				dbPanel = new VerticalPanel();
				inputTextBox = new ValueTextBox("Geburtstag");
				inputTextBox.getElement().setPropertyString("placeholder", "dd.mm.yyyy");
				dbButtonsPanel = new HorizontalPanel();

				db.setText("Geburtsdatum eintragen");
			
				CloseButton closeButton = new CloseButton(db);
				dbButtonsPanel.add(closeButton);
				
				Button addBirthdayButton = new Button("Hinzufügen");
				dbButtonsPanel.add(addBirthdayButton);
				addBirthdayButton.addStyleName("okbutton");
			
				dbPanel.add(inputTextBox);
				dbPanel.add(dbButtonsPanel);

				
				db.add(dbPanel);
				db.show();
				
				
				addBirthdayButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {

						db.hide();
						
						if(!ClientsideFunctions.checkValue(inputTextBox)) {
							Window.alert("Ungültiges Geburtsdatum oder falsches Format");
							inputTextBox.setText("");
							
						}else {
							editorAdministration.createValue(contactToDisplay, 4, inputTextBox.getText(), new AsyncCallback<Value>() {
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
					}
				});
				
				
			}
			else if(ptype == "Sonstiges") {
				db = new DialogBox();
				dbPanel = new VerticalPanel();
				inputTextBox = new ValueTextBox("Sonstiges");
				dbButtonsPanel = new HorizontalPanel();

				db.setText("Neue Eigenschaftsart hinzufügen");
				
				CloseButton closeButton = new CloseButton(db);
				dbButtonsPanel.add(closeButton);
				
				Button addPropertyButton = new Button("Hinzufügen");
				dbButtonsPanel.add(addPropertyButton);
				addPropertyButton.addStyleName("okbutton");
			
				dbPanel.add(inputTextBox);
				dbPanel.add(dbButtonsPanel);
				

				db.add(dbPanel);
				db.show();
					
				addPropertyButton.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {

						db.hide();
						
						
						editorAdministration.createProperty(contactToDisplay, inputTextBox.getText(), new AsyncCallback<Property>() {
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
			else if(ptype == "Anschrift") {
				VerticalPanel addressPanel = new VerticalPanel();
				contactTable.setWidget(row, 0, addressPanel);
				
				Label addressLabel = new Label("Anschrift: ");
				addressPanel.add(addressLabel);
				
				Button addAddressButton = new Button("Anlegen");
				addAddressButton.addStyleName("addNewPropertyButton");
				addAddressButton.addStyleName("anlegenbutton");
				addressPanel.add(addAddressButton);
				
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
				streetTextBox.getElement().setPropertyString("placeholder", "Straße...");
				houseNrTextBox.getElement().setPropertyString("placeholder", "Hausnummer...");
				plzTextBox.getElement().setPropertyString("placeholder", "PLZ...");
				cityTextBox.getElement().setPropertyString("placeholder", "Wohnort...");
					
				addressTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
				addressTable.setWidget(0, 2, new ValueDisplay(new ValueTextBox("Sonstiges")));
				((ValueDisplay) addressTable.getWidget(0, 2)).remove(0);
				
				
				
				
				addAddressButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						((VerticalPanel) contactTable.getWidget(row,0)).remove(1);
						
						editorAdministration.createAddress(streetTextBox.getText(), houseNrTextBox.getText(),
								plzTextBox.getText(), cityTextBox.getText(), contactToDisplay, new AsyncCallback<Value>(){
							public void onFailure(Throwable t) {
								Window.alert("Probleme beim Anlegen der Adresse");
							}
							public void onSuccess(Value street) {
								streetTextBox.setValue(street);
								((ValueDisplay) addressTable.getWidget(0, 2)).setValue(street, true);
								//((DeleteValueButton) addressTable.getWidget(0,3)).setValue(street);
								Window.alert("Adresse erfolgreich angelegt");
							}
						});

						
					}
				});
				
				
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
	 * Die Methode clearContactFrom() wird aufgerufen, wenn der addContactButton gedrückt wird. Die Felder leeren sich und ein neuer Kontakt
	 * kann eingetragen werden.
	 * 
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
	 * Zeigt den selektierten Kontakt an.
	 *
	 * @param c der selektierte Kontakt
	 * @author KatrinZerfass & JanNoller
	 */
	public void setSelected(Contact c) {
		
		clearContactForm();
		
		firstnameTextBox.setEnabled(true);
		lastnameTextBox.setEnabled(true);
		sexListBox.setEnabled(true);
		saveChangesButton.setEnabled(true);
		removeContactFromContactListButton.setEnabled(true);
		newPropertyPanel.setVisible(true);
		
	
		if (c != null){
			
			contactToDisplay = c;
			
			
			/*
			 * Der angemeldete Nutzer wird mit dem Eigentümer verglichen. Ist er ausschließlich Teilhaber, so werden bestimmte Buttons
			 * bereits im Vorfeld ausgegraut.
			 */
		
			
			if(!ClientsideFunctions.isOwner(contactToDisplay, currentUser)) {
				saveChangesButton.setEnabled(false);
			//	removeContactFromContactListButton.setEnabled(false);
				firstnameTextBox.setEnabled(false);
				lastnameTextBox.setEnabled(false);
				sexListBox.setEnabled(false);
				newPropertyPanel.setVisible(false);
			}
			
			/*
			 * Alle Ausprägungen des contactToDisplay werden ausgelesen und in einem Vector<Values> gespeichert.
			 */
			editorAdministration.getAllValuesOfContact(contactToDisplay, new AsyncCallback<Vector<Value>>() {
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
		
		//allValueTextBoxes = new Vector<ValueTextBox>();
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
							
//							newPropertyListBox.removeItem(newPropertyListBox.getItemCount());
							for(int c=0; c<newPropertyListBox.getItemCount(); c++) {
								if (newPropertyListBox.getItemText(c) == "Anschrift") {
									newPropertyListBox.removeItem(c);
								}
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
//				}else if((!compareUser() && allValuesOfContact.get(i).getIsShared()==true))
//					
//						/*
//						 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
//						 */
//					if(firstValue == true) {
//						contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
//						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
//						vp = (ValuePanel) contactTable.getWidget(row, 0);
//						
//						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
//						contactTable.setWidget(row, 1, new ValueTable(pid));
//						vt = (ValueTable) contactTable.getWidget(row, 1);
//						
////					}else if(i !=0 && allValuesOfContact.get(i-1).getPropertyid() != pid ){
////						contactTable.setWidget(row, 0, new ValuePanel(pid, row, ptype + ": "));
////						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
////						vp = (ValuePanel) contactTable.getWidget(row, 0);
////						
////						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
////						contactTable.setWidget(row, 1, new ValueTable(pid));
////						vt = (ValueTable) contactTable.getWidget(row, 1);
//						
//					}else if(i !=0 && allValuesOfContact.get(i-1).getPropertyid() == pid && allValuesOfContact.get(i-1).getIsShared() ==false){
//						if(firstValue)
//						contactTable.setWidget(row, 0, new ValuePanel(pid, row,  ptype +": "));
//						contactTable.getFlexCellFormatter().setVerticalAlignment(row, 0, ALIGN_TOP);
//						vp = (ValuePanel) contactTable.getWidget(row, 0);
//						
//						contactTable.getFlexCellFormatter().setColSpan(row, 1, 3);
//						contactTable.setWidget(row, 1, new ValueTable(pid));
//						vt = (ValueTable) contactTable.getWidget(row, 1);
//					}else {
//						 
//						vp = (ValuePanel) contactTable.getWidget(row-1, 0);
//						vt = (ValueTable) contactTable.getWidget(row-1, 1);
//					}
					
					vtRow = vt.getRowCount();
					vt.setWidget(vtRow, 0, new ValueDisplay(new ValueTextBox(identifier)));
					vt.getValueDisplay(vtRow).setValue(allValuesOfContact.get(i));
					
					

					/*
					 * Das korrekte ValuePanel und ValueTable werden gesetzt und im Folgenden auf ihnen operiert.
					 */
					if (ClientsideFunctions.isOwner(contactToDisplay, currentUser)) {
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
	
	public void disableRemoveContactButton() {
		this.removeContactFromContactListButton.setEnabled(false);
	}
	
}

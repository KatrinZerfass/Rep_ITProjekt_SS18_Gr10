package com.google.gwt.sample.itProjekt.shared;

import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

// 
/**
 * Das ReportGenerator Interface.
 */
@RemoteServiceRelativePath("reportgenerator")
public interface ReportGenerator extends RemoteService{
	
	/**
	 * Die Methode init 
	 *
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	
	public void init() throws IllegalArgumentException;
	
	/**
	 * Generiert den Report f�r alle Kontakte eines bestimmten Users.
	 *
	 * @param User u
	 * @return den Report f�r alle Kontakte eines Users
	 * @throws IllegalArgumentException die illegale Argument Exception
	 */
	//public abstract AllContactsReport generateAllContactsReport() throws IllegalArgumentException;
	public abstract AllContactsOfUserReport generateAllContactsOfUserReport(User u) throws IllegalArgumentException;
	
	/**
	 * Generiert den Report f�r alle geteilten Kontakte eines bestimmten Users.
	 *
	 * @param u the u
	 * @return den Report f�r alle geteilten Kontakte eines Users
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public abstract AllSharedContactsOfUserReport generateAllSharedContactsOfUserReport(User u) throws IllegalArgumentException;
	
	/**
	 * Generiert den Report f�r alle Kontakte mit einer bestimmten Auspr�gung.
	 *
	 * @param die Auspr�gung v
	 * @return den Report f�r alle Kontakte mit einer bestimmten Auspr�gung.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public abstract AllContactsWithValueReport generateAllContactsWithValueReport(Value v) throws IllegalArgumentException;

	public abstract EditorAdministration getEditorAdministration() throws IllegalArgumentException;
}

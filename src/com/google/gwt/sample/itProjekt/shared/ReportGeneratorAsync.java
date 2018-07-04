package com.google.gwt.sample.itProjekt.shared;

import java.util.Vector;

import com.google.gwt.sample.itProjekt.shared.bo.Contact;
import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactInformationOfContactReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithPropertyReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllValuesOfContactReport;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Das asynchrone Gegenstück des Interface {@link ReportGenerator}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. Für weitere Informationen siehe das
 * synchrone Interface {@link ReportGenerator}.
 * 
 */
public interface ReportGeneratorAsync {

	
	void init(AsyncCallback<Void> callback);
	
	void getUserInformation(String email, AsyncCallback<User> callback);
	
	void generateAllContactsOfUserReport(User u, AsyncCallback<AllContactsOfUserReport> callback);

	void generateAllContactsWithValueReport(User user, Value value, AsyncCallback<AllContactsWithValueReport> callback);

	void generateAllSharedContactsOfUserReport(User owner, User receiver, AsyncCallback<AllSharedContactsOfUserReport> callback);

	void generateAllContactsWithPropertyReport(User user, Property property, AsyncCallback<AllContactsWithPropertyReport> callback);

	void generateAllValuesOfContactReport(Contact contact, User user, AsyncCallback<AllValuesOfContactReport> callback);

	void getAllUsers(AsyncCallback<Vector<User>> asyncCallback);

	void getAllPredefinedPropertiesOfReport(AsyncCallback<Vector<Property>> callback);

	void generateAllContactInformationOfContactReport(Contact contact, User user,
			AsyncCallback<AllContactInformationOfContactReport> callback);

	
	


}

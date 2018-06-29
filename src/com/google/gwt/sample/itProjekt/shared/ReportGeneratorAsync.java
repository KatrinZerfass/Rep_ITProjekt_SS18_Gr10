package com.google.gwt.sample.itProjekt.shared;

import com.google.gwt.sample.itProjekt.shared.bo.Property;
import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithPropertyReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportGeneratorAsync {

	void init(AsyncCallback<Void> callback);
	
	void getUserInformation(String email, AsyncCallback<User> callback);

	void generateAllContactsOfUserReport(User u, AsyncCallback<AllContactsOfUserReport> callback);

	void generateAllContactsWithValueReport(User user, Value value, AsyncCallback<AllContactsWithValueReport> callback);

	void generateAllSharedContactsOfUserReport(User owner, User receiver, AsyncCallback<AllSharedContactsOfUserReport> callback);

	void generateAllContactsWithPropertyReport(User user, Property property, AsyncCallback<AllContactsWithPropertyReport> callback);


}

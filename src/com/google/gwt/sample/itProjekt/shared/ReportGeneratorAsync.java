package com.google.gwt.sample.itProjekt.shared;

import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportGeneratorAsync {

	void generateAllContactsOfUserReport(User u, AsyncCallback<AllContactsOfUserReport> callback);

	void generateAllContactsWithValueReport(Value v, AsyncCallback<AllContactsWithValueReport> callback);

	void generateAllSharedContactsOfUserReport(User u, AsyncCallback<AllSharedContactsOfUserReport> callback);

	void init(AsyncCallback<Void> callback);

}

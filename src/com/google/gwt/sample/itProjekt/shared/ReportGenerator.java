package com.google.gwt.sample.itProjekt.shared;

import com.google.gwt.sample.itProjekt.shared.bo.User;
import com.google.gwt.sample.itProjekt.shared.bo.Value;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsOfUserReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsReport;
import com.google.gwt.sample.itProjekt.shared.report.AllContactsWithValueReport;
import com.google.gwt.sample.itProjekt.shared.report.AllSharedContactsOfUserReport;
import com.google.gwt.user.client.rpc.RemoteService;

public interface ReportGenerator extends RemoteService{
	public void init() throws IllegalArgumentException;
	
	public abstract AllContactsReport generateAllContactsReport() throws IllegalArgumentException;
	public abstract AllContactsOfUserReport generateAllContactsOfUserReport(User u) throws IllegalArgumentException;
	public abstract AllSharedContactsOfUserReport generateAllSharedContactsOfUserReport(User u) throws IllegalArgumentException;
	public abstract AllContactsWithValueReport generateAllContactsWithValueReport(Value v) throws IllegalArgumentException;


}

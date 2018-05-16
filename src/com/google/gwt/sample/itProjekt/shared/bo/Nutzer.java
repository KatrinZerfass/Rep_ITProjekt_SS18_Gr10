package com.google.gwt.sample.itProjekt.shared.bo;

public class Nutzer extends BusinessObject {
	
	private String firstname;
	private String lastname;
	private String username;
	
	public Nutzer() {
		super();
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

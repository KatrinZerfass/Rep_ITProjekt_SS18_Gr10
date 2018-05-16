package com.google.gwt.sample.itProjekt.shared.bo;

public class User extends BusinessObject {
	
	private String firstname;
	private String lastname;
	private String username;
// wenn nötig e-Mail und Password, falls diese nicht von Google gestelt werden
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

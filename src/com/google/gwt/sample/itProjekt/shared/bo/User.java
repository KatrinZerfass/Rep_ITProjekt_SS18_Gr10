package com.google.gwt.sample.itProjekt.shared.bo;

public class User extends BusinessObject {
	
	// TODO �berarbeiten nachdem klar ist wie der Login �ber Google UserService funktioniert.
	
	private String firstname;
	private String lastname;
	private String email;
	private String sex;
// wenn n�tig e-Mail und Password, falls diese nicht von Google gestelt werden
	
	
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}

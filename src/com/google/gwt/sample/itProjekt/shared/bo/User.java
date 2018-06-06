package com.google.gwt.sample.itProjekt.shared.bo;

public class User extends BusinessObject {
	
	// TODO �berarbeiten nachdem klar ist wie der Login �ber Google UserService funktioniert.
	
	private String email;

// wenn n�tig e-Mail und Password, falls diese nicht von Google gestelt werden
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

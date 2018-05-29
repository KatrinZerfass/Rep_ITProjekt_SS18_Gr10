package com.google.gwt.sample.itProjekt.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/*
 * Das Ergebnis des Login-Dienstes ist eine Instanz der Klasse "LoginInfo" mit
 * verschiedenen Informationen über den angemeldeten Nutzer.
 */

public class LoginInfo implements IsSerializable{
	
	public LoginInfo() {}
	
	
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;
	
	public boolean isLoggedIn() {
	    return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
	    this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
	    return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
	    this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
	    return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
	    this.logoutUrl = logoutUrl;
	}

	public String getEmailAddress() {
	    return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
	    this.emailAddress = emailAddress;
	}

	public String getNickname() {
	    return nickname;
	}

	public void setNickname(String nickname) {
	    this.nickname = nickname;
	}
}



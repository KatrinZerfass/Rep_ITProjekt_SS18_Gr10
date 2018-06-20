package com.google.gwt.sample.itProjekt.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.itProjekt.client.LoginInfo;
import com.google.gwt.sample.itProjekt.shared.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

 /* 
  * Die serverseitige Implementierung des Login Service prüft
  * ob der Nutzer sich mit seinem Google Konto eingeloggt hat. Falls der Nutzer
  * eingeloggt ist, werden die Attribute des loginInfo Objekts mit Werten aus dem
  * User Objekt befüllt und "setLoggedIn" auf true gesetzt. 
  * 
  */

public class LoginServiceImpl extends RemoteServiceServlet implements
LoginService {
	
	public LoginInfo login(String requestUri) {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LoginInfo loginInfo = new LoginInfo();

	    if (user != null) {
	      loginInfo.setLoggedIn(true);
	      loginInfo.setEmailAddress(user.getEmail());
	      loginInfo.setNickname(user.getNickname());
	      loginInfo.setLogoutUrl(userService.createLogoutURL("https://1-dot-it-projekt-gruppe-10-203610.appspot.com/"));
	    } else {
	      loginInfo.setLoggedIn(false);
	      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
	    }
	    return loginInfo;
	  }

}

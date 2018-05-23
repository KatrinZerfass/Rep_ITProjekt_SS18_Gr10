package com.google.gwt.sample.itProjekt.shared;

import com.google.gwt.sample.itProjekt.client.LoginInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}

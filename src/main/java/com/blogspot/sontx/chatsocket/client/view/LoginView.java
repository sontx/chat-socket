package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.view.BaseView;

public interface LoginView extends BaseView {
    String getUsername();

    String getPassword();

    void setLoginButtonClickListener(Runnable listener);

    void setRegisterButtonClickListener(Runnable listener);

    void setUserName(String loggedUserName);
}

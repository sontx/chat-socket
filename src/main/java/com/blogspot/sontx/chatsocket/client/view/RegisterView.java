package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.lib.view.BaseView;

public interface RegisterView extends BaseView {
    void setRegisterButtonClickListener(Runnable listener);

    String getUsername();

    String getPassword();

    String getDisplayName();
}

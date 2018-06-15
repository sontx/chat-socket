package com.blogspot.sontx.chatsocket.client.view;

import com.blogspot.sontx.chatsocket.client.model.Profile;
import com.blogspot.sontx.chatsocket.lib.Callback;
import com.blogspot.sontx.chatsocket.lib.view.BaseView;

public interface ProfileView extends BaseView {
    void setChangeDisplayNameButtonClickListener(Callback<String> listener);

    void setChangeStatusButtonClickListener(Callback<String> listener);

    void setChangePasswordButtonClickListener(Callback<String> listener);

    void setProfile(Profile profile);
}

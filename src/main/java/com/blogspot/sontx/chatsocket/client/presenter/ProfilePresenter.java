package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.UpdatePasswordEvent;
import com.blogspot.sontx.chatsocket.client.event.UpdateProfileEvent;
import com.blogspot.sontx.chatsocket.client.model.UserProfile;
import com.blogspot.sontx.chatsocket.client.view.ProfileView;
import com.blogspot.sontx.chatsocket.lib.AbstractPresenter;
import com.blogspot.sontx.chatsocket.lib.bean.UpdatePassword;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.utils.Security;

public class ProfilePresenter extends AbstractPresenter<ProfileView> {
    private final UserProfile userProfile;

    public ProfilePresenter(ProfileView profileView, UserProfile userProfile) {
        super(profileView);
        this.userProfile = userProfile;
    }

    @Override
    protected  void wireUpViewEvents() {
        super.wireUpViewEvents();
        view.setChangeDisplayNameButtonClickListener(displayName -> {
            if (Security.checkValidDisplayName(displayName))
                updateDisplayName(displayName);
            else
                postMessageBox("Invalid display name.", "Profile", MessageType.Error);
        });
        view.setChangeStatusButtonClickListener(this::updateStatus);
        view.setChangePasswordButtonClickListener(updatePassword -> {
            if (Security.checkValidPassword(updatePassword.getOldPassword()) && Security.checkValidPassword(updatePassword.getNewPassword()))
                updatePassword(updatePassword);
            else
                postMessageBox("Invalid password.", "Profile", MessageType.Error);
        });
    }

    @Override
    protected void onUserClosesView() {
        stop();
        super.onUserClosesView();
    }

    private void updateDisplayName(String displayName) {
        userProfile.setDisplayName(displayName);
        post(new UpdateProfileEvent(userProfile));
    }

    private void updateStatus(String status) {
        userProfile.setStatus(status);
        post(new UpdateProfileEvent(userProfile));
    }

    private void updatePassword(UpdatePassword updatePassword) {
        UpdatePasswordEvent updatePasswordEvent = new UpdatePasswordEvent();
        updatePasswordEvent.setUpdatePassword(updatePassword);
        post(updatePasswordEvent);
    }

    public void show() {
        start();
        view.setTitle("Profile");
        view.setProfile(userProfile);
        view.showWindow();
    }
}

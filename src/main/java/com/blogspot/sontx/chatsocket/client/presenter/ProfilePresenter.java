package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.UpdatePasswordEvent;
import com.blogspot.sontx.chatsocket.client.event.UpdateProfileEvent;
import com.blogspot.sontx.chatsocket.client.model.UserProfile;
import com.blogspot.sontx.chatsocket.client.view.ProfileView;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.utils.Security;

public class ProfilePresenter extends AbstractService implements Presenter {
    private final ProfileView profileView;
    private final UserProfile userProfile;

    public ProfilePresenter(ProfileView profileView, UserProfile userProfile) {
        this.profileView = profileView;
        this.userProfile = userProfile;
        wireUpViewEvents();
    }

    private void wireUpViewEvents() {
        profileView.setChangeDisplayNameButtonClickListener(displayName -> {
            if (Security.checkValidDisplayName(displayName))
                updateDisplayName(displayName);
            else
                postMessageBox("Invalid display name.", "Profile", MessageType.Error);
        });
        profileView.setChangeStatusButtonClickListener(this::updateStatus);
        profileView.setChangePasswordButtonClickListener(password -> {
            if (Security.checkValidPassword(password))
                updatePassword(password);
            else
                postMessageBox("Invalid password.", "Profile", MessageType.Error);
        });
        profileView.setOnClosingListener(this::stop);
    }

    private void updateDisplayName(String displayName) {
        userProfile.setDisplayName(displayName);
        post(new UpdateProfileEvent(userProfile));
    }

    private void updateStatus(String status) {
        userProfile.setStatus(status);
        post(new UpdateProfileEvent(userProfile));
    }

    private void updatePassword(String password) {
        post(new UpdatePasswordEvent(password));
    }

    public void show() {
        start();
        profileView.setTitle("Profile");
        profileView.setProfile(userProfile);
        profileView.showWindow();
    }
}

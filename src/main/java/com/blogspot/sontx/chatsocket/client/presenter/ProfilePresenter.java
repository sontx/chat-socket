package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.UpdatePasswordEvent;
import com.blogspot.sontx.chatsocket.client.event.UpdateProfileEvent;
import com.blogspot.sontx.chatsocket.client.model.Profile;
import com.blogspot.sontx.chatsocket.client.view.ProfileView;
import com.blogspot.sontx.chatsocket.lib.service.AbstractService;
import com.blogspot.sontx.chatsocket.lib.service.message.MessageType;
import com.blogspot.sontx.chatsocket.lib.utils.Security;

public class ProfilePresenter extends AbstractService implements Presenter {
    private final ProfileView profileView;
    private final Profile profile;

    public ProfilePresenter(ProfileView profileView, Profile profile) {
        this.profileView = profileView;
        this.profile = profile;
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
        profile.setDisplayName(displayName);
        post(new UpdateProfileEvent(profile));
    }

    private void updateStatus(String status) {
        profile.setStatus(status);
        post(new UpdateProfileEvent(profile));
    }

    private void updatePassword(String password) {
        post(new UpdatePasswordEvent(password));
    }

    public void show() {
        start();
        profileView.setTitle("Profile");
        profileView.setProfile(profile);
        profileView.showWindow();
    }
}

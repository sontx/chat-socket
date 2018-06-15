package com.blogspot.sontx.chatsocket.client.presenter;

import com.blogspot.sontx.chatsocket.client.event.UpdatePasswordEvent;
import com.blogspot.sontx.chatsocket.client.event.UpdateProfileEvent;
import com.blogspot.sontx.chatsocket.client.model.Profile;
import com.blogspot.sontx.chatsocket.client.view.ProfileView;
import com.blogspot.sontx.chatsocket.lib.utils.Security;
import org.greenrobot.eventbus.EventBus;

public class ProfilePresenter {
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
                profileView.showMessageBox("Invalid display name.");
        });
        profileView.setChangeStatusButtonClickListener(this::updateStatus);
        profileView.setChangePasswordButtonClickListener(password -> {
            if (Security.checkValidPassword(password))
                updatePassword(password);
            else
                profileView.showMessageBox("Invalid password.");
        });
    }

    private void updateDisplayName(String displayName) {
        profile.setDisplayName(displayName);
        EventBus.getDefault().post(new UpdateProfileEvent(profile));
    }

    private void updateStatus(String status) {
        profile.setStatus(status);
        EventBus.getDefault().post(new UpdateProfileEvent(profile));
    }

    private void updatePassword(String password) {
        EventBus.getDefault().post(new UpdatePasswordEvent(password));
    }

    public void show() {
        profileView.setTitle("Profile");
        profileView.setProfile(profile);
        profileView.showWindow();
    }
}
